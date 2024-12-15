package me.miki.shindo.feature.mod.impl;

import java.awt.Color;

import org.lwjgl.input.Keyboard;

import me.miki.shindo.Shindo;
import me.miki.shindo.event.EventTarget;
import me.miki.shindo.event.impl.KeyEvent;
import me.miki.shindo.event.impl.tick.PlayerTickEvent;
import me.miki.shindo.feature.mod.Mod;
import me.miki.shindo.feature.mod.Type;
import me.miki.shindo.feature.setting.Setting;
import net.minecraft.client.settings.KeyBinding;

public class ToggleSprintMod extends Mod {

    private static boolean toggled = false;

    public ToggleSprintMod() {
        super(
                "ToggleSprint",
                "Allows you to toggle the Sprint button instead of holding it.",
                Type.Mechanic
        );
        Shindo.getInstance().getSettingsManager().addSetting(new Setting("Keybinding", this, Keyboard.KEY_LCONTROL));

        String[] mode = {"Modern", "Legacy"};
        Shindo.getInstance().getSettingsManager().addSetting(new Setting("Mode", this, "Modern", 0, mode));
        Shindo.getInstance().getSettingsManager().addSetting(new Setting("Background", this, true));
        Shindo.getInstance().getSettingsManager().addSetting(new Setting("Font Color", this, new Color(255, 255, 255), new Color(255, 0, 0), 0, new float[]{0, 0}));
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
    public void onTick(PlayerTickEvent e) {
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), toggled);
    }

    @EventTarget
    public void key(KeyEvent e) {
        if(Keyboard.isKeyDown(getKey())){
            toggled = !toggled;
        }
    }

    private int getKey(){
        return Shindo.getInstance().getSettingsManager().getSettingByModAndName(getName(), "Keybinding").getKey();
    }
}
