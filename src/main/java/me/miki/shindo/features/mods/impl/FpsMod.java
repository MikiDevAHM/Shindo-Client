/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package me.miki.shindo.features.mods.impl;

import me.miki.shindo.Shindo;
import me.miki.shindo.events.EventTarget;
import me.miki.shindo.events.impl.EventRender2D;
import me.miki.shindo.features.mods.SimpleHUDMod;
import me.miki.shindo.features.nanovg.font.Icon;
import me.miki.shindo.features.settings.Setting;

public class FpsMod extends SimpleHUDMod {

    public FpsMod() {
        super(
                "FPS",
                "Shows your FPS on the HUD."
        );

        Shindo.getInstance().getSettingManager().addSetting(new Setting("Icon", this, true));
    }

    @EventTarget
    public void onRender2D(EventRender2D event) {
        this.draw();
    }

    @Override
    public String getText() {
        return mc.getDebugFPS() + " FPS";
    }

    @Override
    public String getIcon() {
        return isIcon() ? Icon.MONITOR : null;
    }

    private boolean isIcon() {
        return Shindo.getInstance().getSettingManager().getSettingByModAndName(getName(), "Icon").isCheckToggled();
    }
}
