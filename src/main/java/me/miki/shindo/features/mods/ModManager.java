/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package me.miki.shindo.features.mods;

import me.miki.shindo.features.mods.impl.*;
import me.miki.shindo.features.mods.impl.crosshair.CrosshairMod;

import java.util.ArrayList;

public class ModManager {

    public ArrayList<Mod> mods = new ArrayList<>();

    public ModManager() {
        init();
    }

    /**
     * Initializes all mods
     */

    public void init() {
        addMod(new AnimationMod());
        addMod(new ArmorMod());
        addMod(new BlockOverlayMod());
        addMod(new BossbarMod());
        addMod(new CoordinatesMod());
        addMod(new CpsMod());
        addMod(new CrosshairMod());
        addMod(new DiscordRPCMod());
        addMod(new FpsMod());
        addMod(new FullbrightMod());
        addMod(new GlobalSettingsMod());
        addMod(new KeystrokesMod());
        addMod(new NameTagMod());
        addMod(new NickHiderMod());
        addMod(new NoHurtCamMod());
        addMod(new PingMod());
        addMod(new PotionMod());
        addMod(new ScoreboardMod());
        addMod(new SnaplookMod());
        addMod(new TimeMod());
        addMod(new ToggleSprintMod());
        addMod(new ZoomMod());

    }

    /**
     * @return Returns an Arraylist of all mods
     */

    public ArrayList<Mod> getMods() {
        return mods;
    }

    /**
     * Returns a given mod using its name
     * @param name The name of the mod
     * @return The returned mod
     */

    public Mod getMod(String name) {
        for (Mod m : mods) {
            if (m.getName().equals(name)) {
                return m;
            }
        }

        return null;
    }

    /**
     * Adds a mod to the list
     * @param mod The mod which should be added
     */

    public void addMod(Mod mod) {
        mods.add(mod);
    }

    public void disableAll() {
        for(Mod m : mods) {
            m.setToggled(false);
        }
        getMod("GlobalSettings").setToggled(true);
    }
}
