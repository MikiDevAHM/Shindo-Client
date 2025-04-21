package me.miki.shindo.features.tweaker;

import java.util.ArrayList;

public class TweakerManager {
    private ArrayList<Tweaker> tweakerList = new ArrayList<>();

    public TweakerManager() { init(); }

    public void init() {
        addTweaker(new Tweaker("Network"));
    }

    public ArrayList<Tweaker> getTweaker() { return tweakerList; }

    public void addTweaker(Tweaker tweaker) { tweakerList.add(tweaker); }

    public Tweaker getTweakerByName(String name) {
        for (Tweaker tweaker : tweakerList) {
            if (tweaker.getName().equalsIgnoreCase(name)) {
                return tweaker;
            }
        }
        return null;
    }
}