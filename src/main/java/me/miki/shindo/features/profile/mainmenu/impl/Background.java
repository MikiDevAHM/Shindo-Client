package me.miki.shindo.features.profile.mainmenu.impl;


public class Background {

    private final int id;
    private final String name;

    public Background(int id, String name) {
        this.name = name;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name == null ? "null" : name;
    }
}
