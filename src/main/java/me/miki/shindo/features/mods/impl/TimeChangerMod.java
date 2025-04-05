package me.miki.shindo.features.mods.impl;

import me.miki.shindo.Shindo;
import me.miki.shindo.features.mods.Mod;
import me.miki.shindo.features.mods.Type;
import me.miki.shindo.features.settings.Setting;

public class TimeChangerMod extends Mod {
    public TimeChangerMod() {
        super(
                "TimeChanger",
                "Changes the time of the current World visually.",
                Type.Visual
        );

        Shindo.getInstance().getSettingManager().addSetting(new Setting("Offset", this, 12000, 0));
        Shindo.getInstance().getSettingManager().addSetting(new Setting("Speed", this, 50, 1));
    }
}
