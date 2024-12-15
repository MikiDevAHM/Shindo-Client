package me.miki.shindo.feature.mod.impl;

import java.awt.Color;

import me.miki.shindo.Shindo;
import me.miki.shindo.feature.mod.Mod;
import me.miki.shindo.feature.mod.Type;
import me.miki.shindo.feature.setting.Setting;

public class NameTagMod extends Mod {

    public NameTagMod() {
        super(
                "NameTag",
                "Adds tweaks to NameTags.",
                Type.Tweaks
        );

        Shindo.getInstance().getSettingsManager().addSetting(new Setting("NameTag in 3rd Person", this, true));
        Shindo.getInstance().getSettingsManager().addSetting(new Setting("Opacity", this, 255, 64));
        Shindo.getInstance().getSettingsManager().addSetting(new Setting("Size", this, 3, 1));
        Shindo.getInstance().getSettingsManager().addSetting(new Setting("Y Position", this, 5, 2.5f));
        Shindo.getInstance().getSettingsManager().addSetting(new Setting("Disable Player NameTags", this, false));
        Shindo.getInstance().getSettingsManager().addSetting(new Setting("Font Color", this, new Color(255, 255, 255), new Color(255, 0, 0), 0, new float[]{0, 0}));
    }
}
