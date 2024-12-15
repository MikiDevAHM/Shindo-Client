package me.miki.shindo.feature.mod.impl;

import me.miki.shindo.Shindo;
import me.miki.shindo.feature.mod.Mod;
import me.miki.shindo.feature.mod.Type;
import me.miki.shindo.feature.setting.Setting;

import java.awt.*;

public class BlockInfoMod extends Mod {

    public BlockInfoMod() {
        super(
                "BlockInfo",
                "Shows you information about the block you are looking at.",
                Type.Hud
        );

        String[] mode = {"Modern", "Legacy"};
        Shindo.getInstance().getSettingsManager().addSetting(new Setting("Mode", this, "Modern", 0, mode));
        Shindo.getInstance().getSettingsManager().addSetting(new Setting("Background", this, true));
        Shindo.getInstance().getSettingsManager().addSetting(new Setting("Font Color", this, new Color(255, 255, 255), new Color(255, 0, 0), 0, new float[]{0, 0}));
    }
}
