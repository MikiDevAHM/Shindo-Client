/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package me.miki.shindo.features.mods.impl;

import me.miki.shindo.Shindo;
import me.miki.shindo.features.mods.Mod;
import me.miki.shindo.features.mods.Type;
import me.miki.shindo.features.settings.Setting;

import java.awt.*;

public class KeystrokesMod extends Mod {

    public KeystrokesMod() {
        super(
                "Keystrokes",
                "Shows your Keystrokes on the HUD.",
                Type.Hud
        );

        String[] mode = {"Modern", "Legacy"};
        Shindo.getInstance().getSettingManager().addSetting(new Setting("Mode", this, "Modern", 0, mode));
        Shindo.getInstance().getSettingManager().addSetting(new Setting("Background", this, true));
        Shindo.getInstance().getSettingManager().addSetting(new Setting("Clicks", this, true));
        Shindo.getInstance().getSettingManager().addSetting(new Setting("CPS", this, true));
        Shindo.getInstance().getSettingManager().addSetting(new Setting("Font Color", this, new Color(255, 255, 255), new Color(255, 0, 0), 0, new float[]{0, 0}));
    }
}
