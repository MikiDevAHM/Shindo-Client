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
import me.miki.shindo.helpers.ServerHelper;

public class PingMod extends SimpleHUDMod {

    public PingMod() {
        super(
                "Ping",
                "Shows your ping on the HUD."
        );

        Shindo.getInstance().getSettingManager().addSetting(new Setting("Icon", this, true));
    }

    @EventTarget
    public void onRender2D(EventRender2D event) {
        this.draw();
    }

    @Override
    public String getText() {
        return ServerHelper.getPing() + " ms";
    }

    @Override
    public String getIcon() {
        return isIcon() ? Icon.BAR_CHERT : null;
    }

    private boolean isIcon() {
        return Shindo.getInstance().getSettingManager().getSettingByModAndName(getName(), "Icon").isCheckToggled();
    }
}
