package me.miki.shindo.feature.mod.impl;

import me.miki.shindo.Shindo;
import me.miki.shindo.event.EventTarget;
import me.miki.shindo.event.impl.tick.ClientTickEvent;
import me.miki.shindo.feature.mod.Mod;
import me.miki.shindo.feature.mod.Type;
import me.miki.shindo.feature.setting.Setting;

public class FullbrightMod extends Mod {

    private float oldGamma;

    public FullbrightMod() {
        super(
                "Fullbright",
                "Changes the Gamma of the game to a given value.",
                Type.Visual
        );

        Shindo.getInstance().getSettingsManager().addSetting(new Setting("Brightness", this, 100, 10));
    }

    @Override
    public void onEnable() {
        super.onEnable();
        oldGamma = mc.gameSettings.gammaSetting;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        mc.gameSettings.gammaSetting = oldGamma;
    }

    @EventTarget
    public void onTick(ClientTickEvent e) {
        mc.gameSettings.gammaSetting = Shindo.getInstance().getSettingsManager().getSettingByModAndName(getName(), "Brightness").getCurrentNumber();
    }
}
