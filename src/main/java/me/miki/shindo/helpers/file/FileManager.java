package me.miki.shindo.helpers.file;

import me.miki.shindo.Shindo;
import me.miki.shindo.helpers.logger.ShindoLogger;
import net.minecraft.client.Minecraft;

import java.io.File;
import java.io.IOException;

public class FileManager {

    private File shindoDir, cacheDir, externalDir;

    public FileManager() {
        shindoDir = new File(Minecraft.getMinecraft().mcDataDir, "shindo");
        cacheDir = new File(shindoDir, "cache");
        externalDir = new File(shindoDir, "external");

        createDir(shindoDir);
        createDir(cacheDir);
        createDir(externalDir);
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


}
