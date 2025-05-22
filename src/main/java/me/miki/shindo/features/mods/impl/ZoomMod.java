/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package me.miki.shindo.features.mods.impl;

import me.miki.shindo.Shindo;
import me.miki.shindo.events.EventTarget;
import me.miki.shindo.events.impl.EventScrollMouse;
import me.miki.shindo.events.impl.EventTick;
import me.miki.shindo.events.impl.EventZoomFov;
import me.miki.shindo.features.mods.Mod;
import me.miki.shindo.features.mods.Type;
import me.miki.shindo.features.settings.Setting;
import me.miki.shindo.helpers.animation.simple.SimpleAnimation;
import org.lwjgl.input.Keyboard;

public class ZoomMod extends Mod {

    private SimpleAnimation zoomAnimation = new SimpleAnimation();

    private boolean active;
    private float lastSensitivity;
    private float currentFactor = 1;

    public boolean wasCinematic;

    public ZoomMod() {
        super(
                "Zoom",
                "Allows you to zoom into the world.",
                Type.Mechanic
        );


        Shindo.getInstance().getSettingManager().addSetting(new Setting("Scroll Zoom", this, false));
        Shindo.getInstance().getSettingManager().addSetting(new Setting("Smooth Zoom", this, false));

        Shindo.getInstance().getSettingManager().addSetting(new Setting("Zoom Speed", this, 20, 14, 5));
        Shindo.getInstance().getSettingManager().addSetting(new Setting("Zoom Factor", this, 15, 4, 2));

        Shindo.getInstance().getSettingManager().addSetting(new Setting("Smooth Camera", this, true));

        Shindo.getInstance().getSettingManager().addSetting(new Setting("Keybinding", this, Keyboard.KEY_C));
    }

    @EventTarget
    public void onTick(EventTick event) {
        if(Keyboard.isKeyDown(getKeybinding())) {
            if(!active) {
                active = true;
                lastSensitivity = mc.gameSettings.mouseSensitivity;
                resetFactor();
                wasCinematic = this.mc.gameSettings.smoothCamera;
                mc.gameSettings.smoothCamera = isSmoothCamera();
                mc.renderGlobal.setDisplayListEntitiesDirty();
            }
        }else if(active) {
            active = false;
            setFactor(1);
            mc.gameSettings.mouseSensitivity = lastSensitivity;
            mc.gameSettings.smoothCamera = wasCinematic;
        }
    }

    @EventTarget
    public void onFov(EventZoomFov event) {

        zoomAnimation.setAnimation(currentFactor, getZoomSpeed());

        event.setFov(event.getFov() * (isSmooth()? zoomAnimation.getValue() : currentFactor));
    }

    @EventTarget
    public void onScroll(EventScrollMouse event) {
        if(active && isScroll()) {
            event.setCancelled(true);
            if(event.getAmount() < 0) {
                if(currentFactor < 0.98) {
                    currentFactor+=0.03;
                }
            }else if(event.getAmount() > 0) {
                if(currentFactor > 0.06) {
                    currentFactor-=0.03;
                }
            }
        }
    }

    public void resetFactor() {
        setFactor(1 / getZoomFactor());
    }

    public void setFactor(float factor) {
        if(factor != currentFactor) {
            mc.renderGlobal.setDisplayListEntitiesDirty();
        }
        currentFactor = factor;
    }

    public boolean isScroll() {
        return Shindo.getInstance().getSettingManager().getSettingByModAndName(getName(), "Scroll Zoom").isCheckToggled();
    }

    public boolean isSmooth() {
        return Shindo.getInstance().getSettingManager().getSettingByModAndName(getName(), "Smooth Zoom").isCheckToggled();
    }

    public float getZoomSpeed() {
        return Shindo.getInstance().getSettingManager().getSettingByModAndName(getName(), "Zoom Speed").getCurrentNumber();
    }

    public float getZoomFactor() {
        return Shindo.getInstance().getSettingManager().getSettingByModAndName(getName(), "Zoom Factor").getCurrentNumber();
    }

    public boolean isSmoothCamera() {
        return Shindo.getInstance().getSettingManager().getSettingByModAndName(getName(), "Smooth Camera").isCheckToggled();
    }

    public int getKeybinding() {
        return Shindo.getInstance().getSettingManager().getSettingByModAndName(getName(), "Keybinding").getKey();
    }
}