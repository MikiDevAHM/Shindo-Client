package me.miki.shindo.features.mods.impl;

import me.miki.shindo.Shindo;
import me.miki.shindo.features.mods.Mod;
import me.miki.shindo.features.mods.Type;
import me.miki.shindo.features.settings.Setting;

import java.awt.*;

public class NameTagMod extends Mod {

    public NameTagMod() {
        super(
                "NameTag",
                "Adds tweaks to NameTags.",
                Type.Tweaks
        );

        Shindo.getInstance().getSettingManager().addSetting(new Setting("NameTag in 3rd Person", this, true));
        Shindo.getInstance().getSettingManager().addSetting(new Setting("Opacity", this, 255, 64));
        Shindo.getInstance().getSettingManager().addSetting(new Setting("Size", this, 3, 1));
        Shindo.getInstance().getSettingManager().addSetting(new Setting("Y Position", this, 5, 2.5f));
        Shindo.getInstance().getSettingManager().addSetting(new Setting("Disable Player NameTags", this, false));
        Shindo.getInstance().getSettingManager().addSetting(new Setting("Font Color", this, new Color(255, 255, 255), new Color(255, 0, 0), 0, new float[]{0, 0}));
    }
}
