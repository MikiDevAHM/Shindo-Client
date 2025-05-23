package me.miki.shindo.features.music;

import me.miki.shindo.helpers.animation.simple.SimpleAnimation;
import me.miki.shindo.helpers.file.FileHelper;

import java.io.File;

public class Music {

    private SimpleAnimation favoriteAnimation = new SimpleAnimation();
    private final String name;
    private final File audio;
    private final File icon;
    private MusicType type;

    public Music(File audio, File icon, MusicType type) {
        this.name = FileHelper.getBaseName(audio);
        this.audio = audio;
        this.icon = icon;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public File getAudio() {
        return audio;
    }

    public File getIcon() {
        return icon;
    }

    public MusicType getType() {
        return type;
    }

    public void setType(MusicType type) {
        this.type = type;
    }

    public SimpleAnimation getFavoriteAnimation() {
        return favoriteAnimation;
    }
}