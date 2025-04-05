/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package me.miki.shindo.features.mods.impl;

import me.miki.shindo.Shindo;
import me.miki.shindo.features.mods.Mod;
import me.miki.shindo.features.mods.Type;
import me.miki.shindo.features.settings.Setting;

public class ArmorMod extends Mod {

    public ArmorMod() {
        super(
                "Armor Status",
                "Displays your Armor on the HUD.",
                Type.Hud
        );

        String[] mode = {"Modern", "Legacy"};
        Shindo.getInstance().getSettingManager().addSetting(new Setting("Mode", this, "Modern", 0, mode));
        Shindo.getInstance().getSettingManager().addSetting(new Setting("Background", this, true));
        Shindo.getInstance().getSettingManager().addSetting(new Setting("No Armor Background", this, true));
    }
}
