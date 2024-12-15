package me.miki.shindo.feature.mod;

import java.util.ArrayList;

import me.miki.shindo.feature.mod.impl.AnimationMod;
import me.miki.shindo.feature.mod.impl.ArmorMod;
import me.miki.shindo.feature.mod.impl.AutoGGMod;
import me.miki.shindo.feature.mod.impl.BlockInfoMod;
import me.miki.shindo.feature.mod.impl.BlockOverlayMod;
import me.miki.shindo.feature.mod.impl.BossbarMod;
import me.miki.shindo.feature.mod.impl.CoordinatesMod;
import me.miki.shindo.feature.mod.impl.CpsMod;
import me.miki.shindo.feature.mod.impl.FpsMod;
import me.miki.shindo.feature.mod.impl.FreelookMod;
import me.miki.shindo.feature.mod.impl.FullbrightMod;
import me.miki.shindo.feature.mod.impl.HitColorMod;
import me.miki.shindo.feature.mod.impl.ItemPhysicsMod;
import me.miki.shindo.feature.mod.impl.KeystrokesMod;
import me.miki.shindo.feature.mod.impl.MemoryMod;
import me.miki.shindo.feature.mod.impl.MotionblurMod;
import me.miki.shindo.feature.mod.impl.NameTagMod;
import me.miki.shindo.feature.mod.impl.NickHiderMod;
import me.miki.shindo.feature.mod.impl.NoHurtCamMod;
import me.miki.shindo.feature.mod.impl.PingMod;
import me.miki.shindo.feature.mod.impl.PotionMod;
import me.miki.shindo.feature.mod.impl.ScoreboardMod;
import me.miki.shindo.feature.mod.impl.ServerAddressMod;
import me.miki.shindo.feature.mod.impl.SpeedIndicatorMod;
import me.miki.shindo.feature.mod.impl.TNTTimerMod;
import me.miki.shindo.feature.mod.impl.TablistMod;
import me.miki.shindo.feature.mod.impl.TimeChangerMod;
import me.miki.shindo.feature.mod.impl.TimeMod;
import me.miki.shindo.feature.mod.impl.ToggleSprintMod;
import me.miki.shindo.feature.mod.impl.crosshair.CrosshairMod;

public class ModManager {
	
	

	public ArrayList<Mod> mods = new ArrayList<>();

    public ModManager() {
        init();
    }
    
    public void init() {

        addMod(new AnimationMod());
        addMod(new ArmorMod());
        addMod(new AutoGGMod());
        addMod(new BlockInfoMod());
        addMod(new BlockOverlayMod());
        addMod(new BossbarMod());
        addMod(new CoordinatesMod());
        addMod(new CpsMod());
        addMod(new CrosshairMod());
    	addMod(new FpsMod());
        addMod(new FreelookMod());
        addMod(new FullbrightMod());
        addMod(new HitColorMod());
        addMod(new ItemPhysicsMod());
        addMod(new KeystrokesMod());
        addMod(new MemoryMod());
        addMod(new MotionblurMod());
        addMod(new NameTagMod());
        addMod(new NickHiderMod());
        addMod(new NoHurtCamMod());
        addMod(new PingMod());
        addMod(new PotionMod());
        addMod(new ScoreboardMod());
        addMod(new ServerAddressMod());
        addMod(new SpeedIndicatorMod());
        addMod(new TablistMod());
        addMod(new TimeChangerMod());
        addMod(new TimeMod());
        addMod(new TNTTimerMod());
    	addMod(new ToggleSprintMod());
    }
    
    
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
}
