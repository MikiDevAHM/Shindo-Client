package me.miki.shindo.features.mods;

public enum Type {

    All("explore.png"),
    Hud("display.png"),
    Mechanic("wheel.png"),
    Visual("camera.png"),
    Tweaks("crop.png"),
    Hidden("hidden.png");

    private final String icon;

    Type(String icon) {
        this.icon = icon;
    }

    public String getIcon() {
        return icon;
    }
}
