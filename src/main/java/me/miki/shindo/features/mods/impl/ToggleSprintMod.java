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
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

import java.awt.*;

public class ToggleSprintMod extends Mod {

    private static boolean toggled = false;

    public ToggleSprintMod() {
        super(
                "ToggleSprint",
                "Allows you to toggle the Sprint button instead of holding it.",
                Type.Mechanic
        );
        Shindo.getInstance().getSettingManager().addSetting(new Setting("Keybinding", this, Keyboard.KEY_LCONTROL));

        String[] mode = {"Modern", "Legacy"};
        Shindo.getInstance().getSettingManager().addSetting(new Setting("Mode", this, "Modern", 0, mode));
        Shindo.getInstance().getSettingManager().addSetting(new Setting("Background", this, true));
        Shindo.getInstance().getSettingManager().addSetting(new Setting("Font Color", this, new Color(255, 255, 255), new Color(255, 0, 0), 0, new float[]{0, 0}));
    }

    public static boolean isSprinting() {
        return toggled;
    }

    @Override
    public void onDisable(){
        super.onDisable();
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), false);
    }

    @EventTarget
    public void onTick(TickEvent e) {
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), toggled);
    }

    @EventTarget
    public void key(KeyEvent e) {
        if(Keyboard.isKeyDown(getKey())){
            toggled = !toggled;
        }
    }

    private int getKey(){
        return Shindo.getInstance().getSettingManager().getSettingByModAndName(getName(), "Keybinding").getKey();
    }
}
