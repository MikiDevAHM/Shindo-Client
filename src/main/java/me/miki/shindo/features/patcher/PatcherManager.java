package me.miki.shindo.features.patcher;


import java.util.ArrayList;

public class PatcherManager {

    private ArrayList<Patcher> patcherList = new ArrayList<>();

    public PatcherManager() { init(); }

    public void init() {



    }

    public ArrayList<Patcher> getPatcher() { return patcherList; }

    public void addPatcher(Patcher patcher) { patcherList.add(patcher); }

    public Patcher getPatcherByName(String name) {
        for (Patcher patcher : patcherList) {
            if (patcher.getName().equalsIgnoreCase(name)) {
                return patcher;
            }
        }
        return null;
    }
}
