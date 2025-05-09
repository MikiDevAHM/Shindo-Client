/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package me.miki.shindo.features.mods.impl;

import me.miki.shindo.Shindo;
import me.miki.shindo.events.EventTarget;
import me.miki.shindo.events.impl.KeyEvent;
import me.miki.shindo.events.impl.RenderEvent;
import me.miki.shindo.features.mods.Mod;
import me.miki.shindo.features.mods.Type;
import me.miki.shindo.features.settings.Setting;
import me.miki.shindo.helpers.animation.Animate;
import me.miki.shindo.helpers.animation.Easing;
import me.miki.shindo.helpers.hud.ScrollHelper;
import org.lwjgl.input.Keyboard;

public class ZoomMod extends Mod {

    private static final Animate animate = new Animate();
    private static final ScrollHelper scrollHelper = new ScrollHelper(0, 0, 5, 50);
    private static boolean zoom = false;

    public ZoomMod() {
        super(
                "Zoom",
                "Allows you to zoom into the world.",
                Type.Mechanic
        );

        Shindo.getInstance().getSettingManager().addSetting(new Setting("Keybinding", this, Keyboard.KEY_C));
        Shindo.getInstance().getSettingManager().addSetting(new Setting("Zoom Amount", this, 100, 30, 1));
        Shindo.getInstance().getSettingManager().addSetting(new Setting("Smooth Zoom", this, true));

        animate.setEase(Easing.LINEAR).setSpeed(700);
    }

    @EventTarget
    public void onRender2D(RenderEvent e) {
        animate.setMin(getAmount() / 2).setMax(mc.gameSettings.fovSetting).update();
        scrollHelper.setMinScroll(isSmooth() ? animate.getValueI() - 5 : getAmount() - 5);
        scrollHelper.update();

        if (zoom && mc.currentScreen == null) {
            scrollHelper.updateScroll();
        } else {
            scrollHelper.setScrollStep(0);
        }
    }

    @EventTarget
    public void onKey(KeyEvent e) {
        zoom = Keyboard.isKeyDown(getKey());
        animate.setReversed(zoom);
    }

    public static float getFOV() {
        if (isSmooth()) {
            return animate.getValueI() - scrollHelper.getCalculatedScroll();
        }
        return zoom ? getAmount() - scrollHelper.getCalculatedScroll() : mc.gameSettings.fovSetting;
    }

    public static boolean isZoom() {
        return zoom;
    }

    private static boolean isSmooth() {
        return Shindo.getInstance().getSettingManager().getSettingByModAndName("Zoom", "Smooth Zoom").isCheckToggled();
    }

    private static float getAmount() {
        return Shindo.getInstance().getSettingManager().getSettingByModAndName("Zoom", "Zoom Amount").getCurrentNumber();
    }

    private int getKey() {
        return Shindo.getInstance().getSettingManager().getSettingByModAndName(getName(), "Keybinding").getKey();
    }
}
