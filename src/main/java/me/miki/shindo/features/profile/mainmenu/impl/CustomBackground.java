package me.miki.shindo.features.profile.mainmenu.impl;

import java.io.File;

public class CustomBackground extends Background {

    private final File image;

    public CustomBackground(int id, String name, File image) {
        super(id, name);
        this.image = image;
    }

    public File getImage() {
        return image;
    }
}
