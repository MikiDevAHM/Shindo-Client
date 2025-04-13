package me.miki.shindo.features.patcher;


import me.miki.shindo.features.patcher.impl.bugfix.PatcherBugFixer;

import java.util.ArrayList;

public class PatcherManager {
    private ArrayList<Patcher> features = new ArrayList<Patcher>();

    public PatcherManager() {
        features.add(new PatcherBugFixer());
    }

    public ArrayList<Patcher> getFeatures() {
        return features;
    }
}
