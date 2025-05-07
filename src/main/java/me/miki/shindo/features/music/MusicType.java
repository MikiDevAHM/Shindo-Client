package me.miki.shindo.features.music;

public enum MusicType {
    ALL(0, "ALL"), FAVORITE(1, "FAVORITE");

    private String name;
    private int id;
    private MusicType(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }
}
