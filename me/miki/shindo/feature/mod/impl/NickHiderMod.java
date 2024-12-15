package me.miki.shindo.feature.mod.impl;

import me.miki.shindo.Shindo;
import me.miki.shindo.feature.mod.Mod;
import me.miki.shindo.feature.mod.Type;
import me.miki.shindo.feature.setting.Setting;

public class NickHiderMod extends Mod {

    public NickHiderMod() {
        super(
                "NickHider",
                "Hides your nickname in game by replacing it.",
                Type.Tweaks
        );

        Shindo.getInstance().getSettingsManager().addSetting(new Setting("Nickname", this, "Name", "You", 3));
    }

}
