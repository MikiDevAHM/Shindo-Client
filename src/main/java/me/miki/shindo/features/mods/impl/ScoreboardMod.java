package me.miki.shindo.features.mods.impl;

import me.miki.shindo.Shindo;
import me.miki.shindo.features.mods.Mod;
import me.miki.shindo.features.mods.Type;
import me.miki.shindo.features.settings.Setting;

public class ScoreboardMod extends Mod {

    public ScoreboardMod() {
        super(
                "Scoreboard",
                "Adds Tweaks to the Scoreboard",
                Type.Hud
        );

        Shindo.getInstance().getSettingManager().addSetting(new Setting("Background", this, true));
        Shindo.getInstance().getSettingManager().addSetting(new Setting("Remove Red Numbers", this, true));
    }
}
