package me.miki.shindo.features.patcher.impl;

import me.miki.shindo.Shindo;

public class PatcherInstances {

    public static int patcher$modifyColor(int original) {
        if (Shindo.getInstance().getPatcherManager().getPatcherByName("Tab Opacity").getCurrentNumber() >= 1.0F) return original;
        int prevOpacity = Math.abs(original >> 24);
        int opacity = (int) (prevOpacity * Shindo.getInstance().getPatcherManager().getPatcherByName("Tab Opacity").getCurrentNumber());
        return (opacity << 24) | (original & 0xFFFFFF);
    }
}

