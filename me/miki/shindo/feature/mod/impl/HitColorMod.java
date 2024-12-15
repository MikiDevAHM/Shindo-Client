package me.miki.shindo.feature.mod.impl;

import java.awt.Color;

import me.miki.shindo.Shindo;
import me.miki.shindo.feature.mod.Mod;
import me.miki.shindo.feature.mod.Type;
import me.miki.shindo.feature.setting.Setting;

public class HitColorMod extends Mod {
    public HitColorMod() {
        super(
                "Hit Color",
                "Changes the color of damaged entities.",
                Type.Visual
        );

        Shindo.getInstance().getSettingsManager().addSetting(new Setting("Damage Color", this, new Color(255, 0, 0), new Color(255, 0, 0), 0, new float[]{145, 0}));
        Shindo.getInstance().getSettingsManager().addSetting(new Setting("Alpha", this, 255, 80));
    }
}
