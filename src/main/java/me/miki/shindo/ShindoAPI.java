package me.miki.shindo;

import me.miki.shindo.helpers.file.FileManager;

import java.io.File;

public class ShindoAPI {

    private long launchTime;
    private File firstLoginFile;
    public ShindoAPI() {
        FileManager fm = Shindo.getInstance().getFileManager();

        firstLoginFile = new File(fm.getCacheDir(), "first.tmp");
    }

    public void init() {
        launchTime = System.currentTimeMillis();
    }

    public boolean isSpecialUser()  {
        return true;
    }

    public long getLaunchTime() {
        return launchTime;
    }

    public void createFirstLoginFile() {
        Shindo.getInstance().getFileManager().createFile(firstLoginFile);
    }

    public boolean isFirstLogin() {
        return !firstLoginFile.exists();
    }
}
