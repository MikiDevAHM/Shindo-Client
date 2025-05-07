package me.miki.shindo.features.patcher;


import java.util.ArrayList;

public class PatcherManager {

    private ArrayList<Patcher> patcherList = new ArrayList<>();

    public PatcherManager() { init(); }

    public void init() {
        addPatcher(new Patcher("Tweaks"));
        addPatcher(new Patcher("Hit Delay Fix", true));
        addPatcher(new Patcher("Mouse Delay Fix", true));

        addPatcher(new Patcher("Performance"));
        addPatcher(new Patcher("Remove Ground Foliage", false));
        addPatcher(new Patcher("Disable Break Particles", false));

        addPatcher(new Patcher("QoL"));
        addPatcher(new Patcher("Clean View", false));
        addPatcher(new Patcher("Max Particle Limit", 10000, 4000, 1));

        addPatcher(new Patcher("Tab List"));
        addPatcher(new Patcher("Tab Ping As Number", false));
        addPatcher(new Patcher("Tab Player Count", 120, 80, 40));
        addPatcher(new Patcher("Tab Opacity", 1, 1, 0));

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
