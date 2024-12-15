package me.miki.shindo.feature.mod.impl;

import me.miki.shindo.Shindo;
import me.miki.shindo.feature.mod.Mod;
import me.miki.shindo.feature.mod.Type;
import me.miki.shindo.feature.setting.Setting;

public class ArmorMod extends Mod {

    public ArmorMod() {
        super(
                "Armor Status",
                "Displays your Armor on the HUD.",
                Type.Hud
        );

        String[] mode = {"Modern", "Legacy"};
        Shindo.getInstance().getSettingsManager().addSetting(new Setting("Mode", this, "Modern", 0, mode));
        Shindo.getInstance().getSettingsManager().addSetting(new Setting("Background", this, true));
        Shindo.getInstance().getSettingsManager().addSetting(new Setting("No Armor Background", this, true));
    }
}
