/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */
package me.miki.shindo.features.mods.impl;

import me.miki.shindo.Shindo;
import me.miki.shindo.events.EventTarget;
import me.miki.shindo.events.impl.EventRender2D;
import me.miki.shindo.events.impl.EventTick;
import me.miki.shindo.events.impl.EventUpdate;
import me.miki.shindo.features.mods.SimpleHUDMod;
import me.miki.shindo.features.settings.Setting;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

public class ToggleSprintMod extends SimpleHUDMod {

    private long startTime;
    private boolean wasDown;

    private State state;

    public ToggleSprintMod() {
        super(
                "ToggleSprint",
                "Allows you to toggle the Sprint button instead of holding it."
        );

        Shindo.getInstance().getSettingManager().addSetting(new Setting("Show HUD", this, true));
        Shindo.getInstance().getSettingManager().addSetting(new Setting("Always Sprint", this, false));
    }

    @Override
    public void setup() {
        state = State.WALK;
    }

    @EventTarget
    public void onRender2D(EventRender2D event) {

        if(isHud()) {
            this.draw();
        }

        this.setDraggable(isHud());
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), state.equals(State.HELD) || state.equals(State.TOGGLED) || isAlways());
    }

    @EventTarget
    public void onTick(EventTick event) {

        boolean down = Keyboard.isKeyDown(mc.gameSettings.keyBindSprint.getKeyCode());

        if(isAlways() || mc.currentScreen != null) {
            return;
        }

        if(down) {
            if(!wasDown) {

                startTime = System.currentTimeMillis();

                if(state.equals(State.TOGGLED)) {
                    state = State.HELD;
                }else {
                    state = State.TOGGLED;
                }
            }else if((System.currentTimeMillis() - startTime) > 250) {
                state = State.HELD;
            }
        }else if(state.equals(State.HELD) && mc.thePlayer.isSprinting()) {
            state = State.VANILLA;
        }else if((state.equals(State.VANILLA) || state.equals(State.HELD)) && !mc.thePlayer.isSprinting()) {
            state = State.WALK;
        }

        wasDown = down;
    }

    @Override
    public String getText() {

        String prefix = "Sprinting";

        if(isAlways()) {
            return prefix + " (Always)";
        }

        if(state.equals(State.WALK)) {
            return "Walking";
        }

        return prefix + " (" + state.name + ")";
    }

    private  enum State {
        WALK("Walking"), VANILLA("Vanilla"), HELD("Key Held"), TOGGLED("Toggled");

        private String name;

        private State(String name) {
            this.name = name;
        }
    }

    private boolean isHud() {
        return Shindo.getInstance().getSettingManager().getSettingByModAndName(getName(), "Show HUD").isCheckToggled();
    }

    private boolean isAlways() {
        return Shindo.getInstance().getSettingManager().getSettingByModAndName(getName(), "Always Sprint").isCheckToggled();
    }

}
