package me.miki.shindo.management.file;

import me.miki.shindo.Shindo;
import me.miki.shindo.logger.ShindoLogger;
import net.minecraft.client.Minecraft;

import java.io.File;
import java.io.IOException;

public class FileManager {

	private File shindoDir, profileDir, cacheDir, screenshotDir, soarDir, customCapeDir, capeCacheDir;
	private boolean migrationSuccess = false;
	private File musicDir, externalDir;
	public FileManager() {

		shindoDir = new File(Minecraft.getMinecraft().mcDataDir, "shindo");
		soarDir = new File(Minecraft.getMinecraft().mcDataDir, "shindo");
		profileDir = new File(shindoDir, "profile");
		cacheDir = new File(shindoDir, "cache");
		musicDir = new File(shindoDir, "music");
		externalDir = new File(soarDir, "external");
		screenshotDir = new File(shindoDir, "screenshots");
		customCapeDir = new File(cacheDir, "custom-cape");
		capeCacheDir = new File(cacheDir, "cape");

		try{

			if(!shindoDir.exists()){
				if(soarDir.exists()) {
					migrationSuccess = soarDir.renameTo(shindoDir);
					if(!migrationSuccess) createDir(shindoDir);
				} else {
					createDir(shindoDir);
				}
			}

			if(!profileDir.exists()) createDir(profileDir);
			if(!cacheDir.exists()) createDir(cacheDir);
			if(!musicDir.exists()) createDir(musicDir);
			if(!screenshotDir.exists()) createDir(screenshotDir);
			if(!customCapeDir.exists()) createDir(customCapeDir);
			if(!capeCacheDir.exists()) createDir(capeCacheDir);

			createVersionFile();

		} catch (Exception e) {
			ShindoLogger.error("Something has gone very wrong while trying to create the shindo folder which may result in crashes later", e);
		}

	}
	
	private void createVersionFile() {
		
		File versionDir = new File(cacheDir, "version");
		
		createDir(versionDir);
		createFile(new File(versionDir, Shindo.getInstance().getVersionIdentifier() + ".tmp"));
	}
	
	public void createDir(File file) {
		file.mkdir();
	}
	
	public void createFile(File file) {
		try {
			file.createNewFile();
		} catch (IOException e) {
			ShindoLogger.error("Failed to create file " + file.getName(), e);
		}
	}

	public File getScreenshotDir() {
		return screenshotDir;
	}

	public File getShindoDir() {
		return shindoDir;
	}

	public File getProfileDir() {
		return profileDir;
	}

	public File getCacheDir() {
		return cacheDir;
	}

	public File getCustomCapeDir() {
		return customCapeDir;
	}

	public File getCapeCacheDir() {
		return capeCacheDir;
	}

	public File getMusicDir() {
		return musicDir;
	}

	public File getExternalDir() {
		return externalDir;
	}

}
