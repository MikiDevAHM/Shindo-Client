package me.miki.shindo.features.screenshot;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.miki.shindo.Shindo;
import me.miki.shindo.helpers.file.FileHelper;
import me.miki.shindo.helpers.file.FileManager;
import me.miki.shindo.helpers.logger.ShindoLogger;
import me.miki.shindo.helpers.network.JsonHelper;
import me.miki.shindo.helpers.render.Helper2D;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

public class ScreenshotManager {

	private final CopyOnWriteArrayList<Screenshot> screenshots = new CopyOnWriteArrayList<Screenshot>();
	private final CopyOnWriteArrayList<File> removeScreenshots = new CopyOnWriteArrayList<File>();
	private int prevSize;
	
	public ScreenshotManager() {
		
		FileManager fileManager = Shindo.getInstance().getFileManager();
		File screenshotCacheDir = new File(fileManager.getCacheDir(), "screenshot");
		File dataJson = new File(screenshotCacheDir, "Data.json");
		ArrayList<String> removeScreenshots = loadData();
		
		if(!screenshotCacheDir.exists()) {
			fileManager.createDir(screenshotCacheDir);
		}
		
		if(!dataJson.exists()) {
			fileManager.createFile(dataJson);
		}
		
		for(File f : Objects.requireNonNull(fileManager.getScreenshotDir().listFiles())) {
			if(!removeScreenshots.isEmpty() && removeScreenshots.contains(f.getName())) {
				f.delete();
			}
		}
		
		loadScreenshots();
	}
	
	public ArrayList<String> loadData() {
		
		FileManager fileManager = Shindo.getInstance().getFileManager();
		File screenshotCacheDir = new File(fileManager.getCacheDir(), "screenshot");
		File dataJson = new File(screenshotCacheDir, "Data.json");
		
		ArrayList<String> output = new ArrayList<String>();
		
		try (FileReader reader = new FileReader(dataJson)) {
			
			Gson gson = new Gson();
			JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);
			
			if(jsonObject != null) {
				
				JsonArray jsonArray = JsonHelper.getArrayProperty(jsonObject, "Remove Screenshots");
				
				if(jsonArray != null) {

                    for (JsonElement jsonElement : jsonArray) {

                        JsonObject rJsonObject = gson.fromJson(jsonElement, JsonObject.class);

                        output.add(JsonHelper.getStringProperty(rJsonObject, "Screenshot", "null"));
                    }
				}
			}
		} catch (Exception e) {
			ShindoLogger.error("Failed to load screenshot data", e);
		}
		
		return output;
	}
	
	public void saveData() {
		
		FileManager fileManager = Shindo.getInstance().getFileManager();
		File screenshotCacheDir = new File(fileManager.getCacheDir(), "screenshot");
		File dataJson = new File(screenshotCacheDir, "Data.json");
		
		try(FileWriter writer = new FileWriter(dataJson)) {
			
			JsonObject jsonObject = new JsonObject();
			JsonArray jsonArray = new JsonArray();
			Gson gson = new Gson();
			
			for(File f : removeScreenshots) {
				
				JsonObject innerJsonObject = new JsonObject();
				
				innerJsonObject.addProperty("Screenshot", f.getName());
				
				jsonArray.add(innerJsonObject);
			}
			
			jsonObject.add("Remove Screenshots", jsonArray);
			
			gson.toJson(jsonObject, writer);
		} catch(Exception e) {
			ShindoLogger.error("Failed to save screenshot data", e);
		}
	}

	public void loadScreenshots()  {

		File screenshotDir = Shindo.getInstance().getFileManager().getScreenshotDir();

		if(prevSize != Objects.requireNonNull(screenshotDir.listFiles()).length) {

			prevSize = Objects.requireNonNull(screenshotDir.listFiles()).length;

			for(File f : Objects.requireNonNull(screenshotDir.listFiles())) {

				if(FileHelper.getExtension(f).equals("png")) {
					if(!removeScreenshots.contains(f) && getScreenshotByFile(f) == null) {
						screenshots.add(new Screenshot(f));
						Helper2D.loadTexture(f);
					}
				}
			}
		}
	}
	
	public Screenshot getNextScreenshot(Screenshot currentScreenshot) {
		
		int max = screenshots.size();
		int index = screenshots.indexOf(currentScreenshot);
		
		if(index < max - 1) {
			index++;
		}else {
			index = 0;
		}
		
		return screenshots.get(index);
	}
	
	public Screenshot getBackScreenshot(Screenshot currentScreenshot) {
		
		int max = screenshots.size();
		int index = screenshots.indexOf(currentScreenshot);
		
		if(index > 0) {
			index--;
		}else {
			index = max - 1;
		}
		
		return screenshots.get(index);
	}
	
	public Screenshot getScreenshotByFile(File file) {
		
		for(Screenshot sc : screenshots) {
			if(sc.getImage().equals(file)) {
				return sc;
			}
		}
		
		return null;
	}
	
	public void delete(Screenshot screenshot) {
		removeScreenshots.add(screenshot.getImage());
		screenshots.remove(screenshot);
		saveData();
		loadScreenshots();
	}

	public CopyOnWriteArrayList<Screenshot> getScreenshots() {
		return screenshots;
	}
}
