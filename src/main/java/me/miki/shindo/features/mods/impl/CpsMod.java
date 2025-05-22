/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package me.miki.shindo.features.mods.impl;


import me.miki.shindo.Shindo;
import me.miki.shindo.events.EventTarget;
import me.miki.shindo.events.impl.EventClickMouse;
import me.miki.shindo.events.impl.EventRender2D;
import me.miki.shindo.events.impl.EventTick;
import me.miki.shindo.features.mods.SimpleHUDMod;
import me.miki.shindo.features.nanovg.font.Icon;
import me.miki.shindo.features.settings.Setting;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;

public class CpsMod extends SimpleHUDMod {

    private ArrayList<Long> leftPresses = new ArrayList<Long>();
    private ArrayList<Long> rightPresses = new ArrayList<Long>();

    public CpsMod() {
        super(
                "CPS",
                "Shows your CPS on the HUD."
        );

        Shindo.getInstance().getSettingManager().addSetting(new Setting("Right Click", this, true));
        Shindo.getInstance().getSettingManager().addSetting(new Setting("Icon", this, true));
    }

    @EventTarget
    public void onRender2D(EventRender2D event) {
        this.draw();
    }

    @EventTarget
    public void onClickMouse(EventClickMouse event) {

        if(Mouse.getEventButtonState()) {

            if(event.getButton() == 0) {
                leftPresses.add(System.currentTimeMillis());
            }

            if(event.getButton() == 1) {
                rightPresses.add(System.currentTimeMillis());
            }
        }
    }

    @EventTarget
    public void onTick(EventTick event) {
        leftPresses.removeIf(t -> System.currentTimeMillis() - t > 1000);
        rightPresses.removeIf(t -> System.currentTimeMillis() - t > 1000);
    }

    @Override
    public String getText() {
        return (isRightClick() ? leftPresses.size() + " | " + rightPresses.size() : leftPresses.size()) + " CPS";
    }

    @Override
    public String getIcon() {
        return isIcon() ? Icon.MOUSE_POINTER : null;
    }

    private boolean isRightClick() {
        return Shindo.getInstance().getSettingManager().getSettingByModAndName(getName(), "Right Click").isCheckToggled();
    }

    private boolean isIcon() {
        return Shindo.getInstance().getSettingManager().getSettingByModAndName(getName(), "Icon").isCheckToggled();
    }
}
