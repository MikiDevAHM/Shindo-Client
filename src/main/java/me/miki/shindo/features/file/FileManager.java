package me.miki.shindo.features.file;

import me.miki.shindo.Shindo;
import me.miki.shindo.helpers.logger.ShindoLogger;
import net.minecraft.client.Minecraft;

import java.io.File;
import java.io.IOException;

public class FileManager {

    private final File shindoDir;
    private final File profileDir;
    private final File cacheDir;
    private final File musicDir;
    private final File externalDir;
    private final File screenshotDir;


    public FileManager() {
        shindoDir = new File(Minecraft.getMinecraft().mcDataDir, "shindo");
        profileDir = new File(shindoDir, "profile");
        cacheDir = new File(shindoDir, "cache");
        musicDir = new File(shindoDir, "music");
        externalDir = new File(shindoDir, "external");
        screenshotDir = new File(shindoDir, "screenshots");



        createDir(shindoDir);
        createDir(profileDir);
        createDir(cacheDir);
        createDir(musicDir);
        createDir(externalDir);
        createDir(screenshotDir);


        createVersionFile();
    }

    private void createVersionFile() {
        File versionDir = new File(cacheDir, "version");

        createDir(versionDir);
        createFile(new File(versionDir, Shindo.VERSION + ".tmp"));
    }

    public void createDir(File file) {
        file.mkdir();
    }

    public void createFile(File file) {
        try {
            file.createNewFile();
        } catch (IOException e) {
            ShindoLogger.error("Error creating file: " + file.getName(), e);
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

    public File getMusicDir() {
        return musicDir;
    }

    public File getExternalDir() {
        return externalDir;
    }


}
