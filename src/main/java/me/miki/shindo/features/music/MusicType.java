package me.miki.shindo.features.music;

import me.miki.shindo.helpers.animation.ColorAnimation;
import me.miki.shindo.helpers.animation.simple.SimpleAnimation;

public enum MusicType {
    ALL(0, "ALL"), FAVORITE(1, "FAVORITE");

    private ColorAnimation textColorAnimation;
    private SimpleAnimation backgroundAnimation;

    private String name;
    private int id;

    private MusicType(int id, String name) {
        this.id = id;
        this.name = name;
        this.backgroundAnimation = new SimpleAnimation();
        this.textColorAnimation = new ColorAnimation();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ColorAnimation getTextColorAnimation() {
        return textColorAnimation;
    }

    public SimpleAnimation getBackgroundAnimation() {
        return backgroundAnimation;
    }
}
