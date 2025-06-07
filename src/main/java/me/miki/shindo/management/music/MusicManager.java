package me.miki.shindo.management.music;

import com.google.gson.*;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.AudioDevice;
import javazoom.jl.player.JavaSoundAudioDevice;
import javazoom.jl.player.advanced.AdvancedPlayer;
import me.miki.mp3agic.Mp3File;
import me.miki.mp3agic.interfaces.ID3v2;
import me.miki.shindo.Shindo;
import me.miki.shindo.logger.ShindoLogger;
import me.miki.shindo.management.file.FileManager;
import me.miki.shindo.management.language.TranslateText;
import me.miki.shindo.management.mods.impl.InternalSettingsMod;
import me.miki.shindo.management.mods.impl.MusicInfoMod;
import me.miki.shindo.management.mods.settings.impl.ComboSetting;
import me.miki.shindo.management.music.ytdlp.Ytdlp;
import me.miki.shindo.utils.ImageUtils;
import me.miki.shindo.utils.JsonUtils;
import me.miki.shindo.utils.Multithreading;
import me.miki.shindo.utils.file.FileUtils;
import org.jtransforms.fft.FloatFFT_1D;

import javax.imageio.ImageIO;
import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CopyOnWriteArrayList;


public class MusicManager {

	private VolumeSpectrumAudioDevice volumeSpectrumAudioDevice;

	private final Object lock = new Object();
	private final CopyOnWriteArrayList<Music> musics = new CopyOnWriteArrayList<Music>();
	private AdvancedPlayer player;
	private Thread playThread;
	private final Ytdlp ytdlp = new Ytdlp();
	private volatile boolean paused = false;
	private volatile boolean stopped = false;
	private float volume; // De 0.0f a 1.0f

	private Music currentMusic;


	
	public MusicManager() {
		load();
		loadData();
	}
	
	public void loadData() {
		
		FileManager fileManager = Shindo.getInstance().getFileManager();
		File cacheDir = new File(fileManager.getCacheDir(), "music");
		File dataJson = new File(cacheDir, "Data.json");
		
		ArrayList<String> favorites = new ArrayList<String>();
		
		if(!dataJson.exists()) {
			fileManager.createFile(dataJson);
		}
		
		try (FileReader reader = new FileReader(dataJson)) {
			
			Gson gson = new GsonBuilder().create();
			JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);
			
			if(jsonObject != null) {
				
				JsonArray jsonArray = JsonUtils.getArrayProperty(jsonObject, "Favorite Musics");
				
				if(jsonArray != null) {

                    for (JsonElement jsonElement : jsonArray) {

                        JsonObject rJsonObject = gson.fromJson(jsonElement, JsonObject.class);

                        favorites.add(JsonUtils.getStringProperty(rJsonObject, "Favorite", "null"));
                    }
				}
			}
		} catch (Exception e) {
			ShindoLogger.error("An Error Occurred while loading data from file " + dataJson.getAbsolutePath(), e);
		}
		
		for(Music m : musics) {
			if(favorites.contains(m.getName())) {
				m.setType(MusicType.FAVORITE);
			}
		}
	}
	
	public void saveData() {
		
		FileManager fileManager = Shindo.getInstance().getFileManager();
		File cacheDir = new File(fileManager.getCacheDir(), "music");
		File dataJson = new File(cacheDir, "Data.json");
		
		if(!dataJson.exists()) {
			fileManager.createFile(dataJson);
		}
		
		try(FileWriter writer = new FileWriter(dataJson)) {
			
			JsonObject jsonObject = new JsonObject();
			JsonArray jsonArray = new JsonArray();
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			
			for(Music m : musics) {
				
				if(m.getType().equals(MusicType.FAVORITE)) {
					
					JsonObject innerJsonObject = new JsonObject();
					
					innerJsonObject.addProperty("Favorite", m.getName());
					
					jsonArray.add(innerJsonObject);
				}
			}
			
			jsonObject.add("Favorite Musics", jsonArray);
			
			gson.toJson(jsonObject, writer);
			
		} catch(Exception e) {
			ShindoLogger.error("An Error Occurred while saving data to file " + dataJson.getAbsolutePath(), e);
		}
	}

	public void load() {

		FileManager fileManager = Shindo.getInstance().getFileManager();
		File musicDir = fileManager.getMusicDir();
		File cacheDir = new File(fileManager.getCacheDir(), "music");

		if(!cacheDir.exists()) {
			fileManager.createDir(cacheDir);
		}

		for(File f : musicDir.listFiles()) {

			if(FileUtils.getExtension(f).equals("mp3")) {

				File imageFile = new File(cacheDir, f.getName().replace(".mp3", ""));

				if(!imageFile.exists()) {

					try {

						Mp3File mp3File = new Mp3File(f);

						if(mp3File.hasId3v2Tag()) {

							ID3v2 id3v2tag = mp3File.getId3v2Tag();

							if(id3v2tag.getAlbumImage() != null) {

								byte[] imageData = id3v2tag.getAlbumImage();

								FileOutputStream fos = new FileOutputStream(imageFile);

								fos.write(imageData);
								fos.close();

								ImageIO.write(ImageUtils.resize(ImageIO.read(imageFile), 256, 256), "png", imageFile);
							}
						}

					} catch(Exception e) {
						ShindoLogger.error("An Error Occurred while loading data from file " + imageFile.getAbsolutePath(), e);
					}
				}
			}
		}

		for(File f : musicDir.listFiles()) {
			if(FileUtils.isAudioFile(f)) {

				if(getMusicByAudioFile(f) != null) {
					continue;
				}

				if(FileUtils.getExtension(f).equals("mp3")) {

					File imageFile = new File(cacheDir, f.getName().replace(".mp3", ""));

					if(imageFile.exists()) {
						musics.add(new Music(f, imageFile, MusicType.ALL));
					}else {
						musics.add(new Music(f, null, MusicType.ALL));
					}
				}else {
					musics.add(new Music(f, null, MusicType.ALL));
				}
			}
		}
	}

	public void loadAsync() {
		Multithreading.runAsync(()-> {
			load();
		});
	}

	public void play() {

		if (currentMusic == null) {
			return;
		}

		if (paused) {
			resume();
			return;
		}

		if (isPlaying()) {
			stop();
		}


		playThread = new Thread(() -> {
			try (BufferedInputStream bis = new BufferedInputStream(Files.newInputStream(currentMusic.getAudio().toPath()))) {
				player = new AdvancedPlayer(bis, createAudioDevice());
				stopped = false;
				paused = false;
				player.play();
			} catch (Exception e) {
				ShindoLogger.error("could not play audio", e);
			}
		});
		playThread.start();
	}

	public void setVolume() {
		volume = InternalSettingsMod.getInstance().getVolumeSetting().getValueFloat();
	}
	public void next() {
		if (currentMusic == null) {
			return;
		}

		int max = musics.size();
		int index = musics.indexOf(currentMusic);

		if (index < max - 1) {
			index++;
		} else {
			index = 0;
		}
		currentMusic = musics.get(index);
		play();
	}

	public void back() {
		if (currentMusic == null) {
			return;
		}

		int max = musics.size();
		int index = musics.indexOf(currentMusic);

		if (index > 0) {
			index--;
		} else {
			index = max - 1;
		}

		currentMusic = musics.get(index);
		play();
	}

	public void switchPlayBack() {
		if (player != null) {
			if (isPlaying()) pause();
			else resume();
		}
	}

	public void pause() {
		paused = true;
	}

	public void resume() {
		paused = false;
		synchronized (lock) {
			lock.notifyAll();
		}
	}

	public void stop() {
		stopped = true;
		paused = false;
		if (player != null) {
			player.close();
		}
		if (playThread != null) {
			playThread.interrupt();
		}
	}

	public boolean isPlaying() {
		return !paused && playThread != null && playThread.isAlive();
	}

	public Music getMusicByName(String name) {
		
		for(Music m : musics) {
			if(m.getName().equals(name)) {
				return m;
			}
		}
		
		return null;
	}
	
	public Music getMusicByAudioFile(File file) {
		
		for(Music m : musics) {
			if(m.getAudio().equals(file)) {
				return m;
			}
		}
		
		return null;
	}
	
	public void delete(Music m) {
		musics.remove(m);
		m.getAudio().delete();
		load();
	}

	public CopyOnWriteArrayList<Music> getMusics() {
		return musics;
	}

	public Music getCurrentMusic() {
		return currentMusic;
	}

	public void setCurrentMusic(Music currentMusic) {
		this.currentMusic = currentMusic;
	}

	public Ytdlp getYtdlp() {
		return ytdlp;
	}

	private AudioDevice createAudioDevice() throws JavaLayerException {
		volumeSpectrumAudioDevice = new VolumeSpectrumAudioDevice();
		return volumeSpectrumAudioDevice;
	}

	public float getCurrentTime() {
		if (volumeSpectrumAudioDevice != null) {
			return volumeSpectrumAudioDevice.getCurrentTime();
		}
		return 0;
	}

	public float getEndTime() {
		if (currentMusic == null) {
			return 0;
		}

		try {
			Mp3File mp3 = new Mp3File(currentMusic.getAudio());
			return mp3.getLengthInSeconds();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	private class VolumeSpectrumAudioDevice extends JavaSoundAudioDevice {

		private final int fftSize = 1024;
		private float[] audioBuffer = new float[fftSize];
		private float[] magnitudes = new float[fftSize / 2];
		private float[] phases = new float[fftSize / 2];
		private FloatFFT_1D fft = new FloatFFT_1D(fftSize);

		private long totalSamplesProcessed = 0;

		@Override
		protected void writeImpl(short[] samples, int offs, int len) throws JavaLayerException {
			if (paused) {
				synchronized (lock) {
					try {
						lock.wait();
					} catch (InterruptedException e) {
						return;
					}
				}
			}

			if (stopped) return;

			// Aplica volume
			for (int i = offs; i < offs + len; i++) {
				samples[i] = (short) (samples[i] * (volume / 80f));
			}

			// Envia para o áudio
			super.writeImpl(samples, offs, len);

			// Atualiza total de samples para timestamp
			totalSamplesProcessed += len;

			// Preenche buffer para FFT (com o áudio já no volume aplicado)
			int toCopy = Math.min(len, fftSize);
			for (int i = 0; i < toCopy; i++) {
				audioBuffer[i] = samples[offs + i] / 32768f; // normaliza para [-1,1]
			}
			if (toCopy < fftSize) {
				Arrays.fill(audioBuffer, toCopy, fftSize, 0f);
			}

			// Prepara input complexo para FFT
			float[] fftInput = new float[fftSize * 2];
			for (int i = 0; i < fftSize; i++) {
				fftInput[2 * i] = audioBuffer[i];
				fftInput[2 * i + 1] = 0f;
			}

			// Executa FFT
			fft.complexForward(fftInput);

			// Calcula magnitude em dB e fase
			for (int i = 0; i < fftSize / 2; i++) {
				float re = fftInput[2 * i];
				float im = fftInput[2 * i + 1];

				float mag = (float) Math.sqrt(re * re + im * im);
				float magDb = mag > 1e-7f ? 20f * (float) Math.log10(mag) : -100f;
				magnitudes[i] = magDb;

				phases[i] = (float) Math.atan2(im, re);
			}

			// Calcula timestamp (em segundos)
			int sampleRate = getAudioFormat() != null ? (int) getAudioFormat().getSampleRate() : 44100;
			double timestamp = (double) totalSamplesProcessed / sampleRate;
			double duration = (double) fftSize / sampleRate;

			spectrumDataUpdate(timestamp, duration, magnitudes, phases);
		}

		protected void spectrumDataUpdate(double timestamp, double duration, float[] magnitudes, float[] phases) {

			ComboSetting setting = MusicInfoMod.getInstance().getDesignSetting();
			boolean isWaveform = setting.getOption().getTranslate().equals(TranslateText.WAVEFORM);

			for(int i = 0; i < 100; i++) {

				if(isWaveform) {
					MusicWaveform.visualizer[i] = (float) ((magnitudes[i] + 60) * (-1.17));
				} else {
					MusicWaveform.visualizer[i] = (float) ((magnitudes[i] + 60) * (-3.0));
				}
			}
		}

		public float getCurrentTime() {
			if (getAudioFormat() == null) return 0.0F;
			int sampleRate = (int) getAudioFormat().getSampleRate();
			return  (float) totalSamplesProcessed / sampleRate;
		}
	}
}
