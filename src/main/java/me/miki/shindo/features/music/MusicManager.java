package me.miki.shindo.features.music;


import com.google.gson.*;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.AudioDevice;
import javazoom.jl.player.advanced.AdvancedPlayer;
import me.miki.shindo.Shindo;
import me.miki.shindo.helpers.file.FileHelper;
import me.miki.shindo.helpers.file.FileManager;
import me.miki.shindo.helpers.logger.ShindoLogger;
import me.miki.shindo.helpers.network.JsonHelper;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class MusicManager {




    private CopyOnWriteArrayList<Music> musics = new CopyOnWriteArrayList<Music>();
    private AdvancedPlayer player;
    private Thread playThread;
    private volatile boolean paused = false;
    private volatile boolean stopped = false;
    private final Object lock = new Object();

    private float volume = 0.5f; // De 0.0f a 1.0f

    private Music currentMusic;

    public MusicManager() {
        load();
        try {
            loadData();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void loadData() throws Exception {
        FileManager fileManager = Shindo.getInstance().getFileManager();
        File cacheDir = new File(fileManager.getCacheDir(), "music");
        File dataJson = new File(cacheDir, "Data.json");

        ArrayList<String> favorites = new ArrayList<String>();

        if(!dataJson.exists()) {
            fileManager.createFile(dataJson);
        }

        try (FileReader reader = new FileReader(dataJson)) {

            Gson gson = new GsonBuilder()
                    .create();
            JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);

            if(jsonObject != null) {

                JsonArray jsonArray = JsonHelper.getArrayProperty(jsonObject, "Favorite Musics");

                if(jsonArray != null) {

                    Iterator<JsonElement> iterator = jsonArray.iterator();

                    while(iterator.hasNext()) {

                        JsonElement jsonElement = (JsonElement) iterator.next();
                        JsonObject rJsonObject = gson.fromJson(jsonElement, JsonObject.class);

                        favorites.add(JsonHelper.getStringProperty(rJsonObject, "Favorite", "null"));
                    }
                }
            }
        } catch (Exception e) {
            ShindoLogger.error("error loading data", e);
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
            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .create();

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
            ShindoLogger.error("Error saving data", e);
        }
    }

    public void load() {

        FileManager fileManager = Shindo.getInstance().getFileManager();
        File musicDir = fileManager.getMusicDir();

        for(File f : musicDir.listFiles()) {
            if(FileHelper.isAudioFile(f)) {

                if(getMusicByAudioFile(f) != null) {
                    continue;
                }

                if(FileHelper.getExtension(f).equals("mp3")) {
                    musics.add(new Music(f, null, MusicType.ALL));
                }
            }
        }
    }

    public void play() {

        if (currentMusic == null) {
            return;
        }

        if (paused) {
            resume();
            return;
        }

        if(isPlaying()) {
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

    public void setVolume(float volume) {
        this.volume = Math.max(0f, Math.min(1f, volume));
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
        if(currentMusic == null) {
            return;
        }

        int max = musics.size();
        int index = musics.indexOf(currentMusic);

        if(index > 0) {
            index--;
        }else {
            index = max - 1;
        }

        currentMusic = musics.get(index);
        play();
    }

    public void switchPlayBack() {
        if(player != null) {
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




    private AudioDevice createAudioDevice() throws JavaLayerException {
        return new VolumeAudioDevice();
    }

    // Inner class que permite controle de volume
    private class VolumeAudioDevice extends javazoom.jl.player.JavaSoundAudioDevice {
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

            for (int i = offs; i < offs + len; i++) {
                samples[i] = (short) (samples[i] * volume);
            }

            super.writeImpl(samples, offs, len);
        }
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
}