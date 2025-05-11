package me.miki.shindo.helpers.file;

import me.miki.shindo.Shindo;
import me.miki.shindo.helpers.logger.ShindoLogger;
import net.minecraft.client.Minecraft;

import java.io.File;
import java.io.IOException;

public class FileManager {

    private final File shindoDir;
    private final File cacheDir;
    private final File externalDir;
    private final File screenshotDir;
    private final File musicDir;
    private final File profileDir;

    public FileManager() {
        shindoDir = new File(Minecraft.getMinecraft().mcDataDir, "shindo");
        cacheDir = new File(shindoDir, "cache");
        externalDir = new File(shindoDir, "external");
        screenshotDir = new File(shindoDir, "screenshot");
        musicDir = new File(shindoDir, "music");
        profileDir = new File(shindoDir, "profile");

        createDir(shindoDir);
        createDir(cacheDir);
        createDir(externalDir);
        createDir(screenshotDir);
        createDir(musicDir);
        createDir(profileDir);

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

    public File getShindoDir() {
        return shindoDir;
    }

    public File getCacheDir() {
        return cacheDir;
    }

    public File getExternalDir() {
        return externalDir;
    }

    public File getScreenshotDir() {
        return screenshotDir;
    }

    public File getMusicDir() {
        return musicDir;
    }

    public File getProfileDir() {
        return profileDir;
    }


}
