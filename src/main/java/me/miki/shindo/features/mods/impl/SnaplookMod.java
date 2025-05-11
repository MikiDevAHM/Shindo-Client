/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package me.miki.shindo.features.mods.impl;

import me.miki.shindo.Shindo;
import me.miki.shindo.events.EventTarget;
import me.miki.shindo.events.impl.KeyEvent;
import me.miki.shindo.events.impl.TickEvent;
import me.miki.shindo.features.mods.Mod;
import me.miki.shindo.features.mods.Type;
import me.miki.shindo.features.settings.Setting;
import org.lwjgl.input.Keyboard;

public class SnaplookMod extends Mod {
    private boolean cameraToggled = false;

    public SnaplookMod() {
        super(
                "Snaplook",
                "Allows you to see you in 3rd person, by only holding a button.",
                Type.Mechanic
        );

        Shindo.getInstance().getSettingManager().addSetting(new Setting("Keybinding", this, Keyboard.KEY_F));
    }

    @EventTarget
    public void onKey(KeyEvent e) {
        if (Keyboard.isKeyDown(getKey()) && !cameraToggled) {
            cameraToggled = true;
            mc.gameSettings.thirdPersonView = 1;
        }
    }

    @EventTarget
    public void onTick(TickEvent e) {
        if (!Keyboard.isKeyDown(getKey()) && cameraToggled) {
            cameraToggled = false;
            mc.gameSettings.thirdPersonView = 0;
        }
    }

    private int getKey() {
        return Shindo.getInstance().getSettingManager().getSettingByModAndName(getName(), "Keybinding").getKey();
    }
}
