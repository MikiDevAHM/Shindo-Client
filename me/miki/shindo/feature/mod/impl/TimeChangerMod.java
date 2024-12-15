package me.miki.shindo.feature.mod.impl;

import me.miki.shindo.Shindo;
import me.miki.shindo.feature.mod.Mod;
import me.miki.shindo.feature.mod.Type;
import me.miki.shindo.feature.setting.Setting;

public class TimeChangerMod extends Mod {
    public TimeChangerMod() {
        super(
                "TimeChanger",
                "Changes the time of the current World visually.",
                Type.Mechanic
        );

        Shindo.getInstance().getSettingsManager().addSetting(new Setting("Offset", this, 12000, 0));
        Shindo.getInstance().getSettingsManager().addSetting(new Setting("Speed", this, 50, 1));
    }
}
