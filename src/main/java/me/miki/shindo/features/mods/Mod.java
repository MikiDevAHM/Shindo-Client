/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package me.miki.shindo.features.mods;


import me.miki.shindo.events.EventManager;
import me.miki.shindo.helpers.animation.simple.SimpleAnimation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

public class Mod {

    public Minecraft mc = Minecraft.getMinecraft();
    public FontRenderer fr = mc.fontRendererObj;
    private String name;
    private String description;
    private Type type;
    private boolean toggled, hide;

    private SimpleAnimation animation = new SimpleAnimation();

    public Mod(String name, String description, Type type) {
        this.name = name;
        this.description = description;
        this.type = type;

        this.setup();
    }

    public void setup() {}

    public void onEnable() {
        EventManager.register(this);
    }

    public void onDisable() {
        EventManager.unregister(this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isToggled() {
        return toggled;
    }

    public void setToggled(boolean toggled) {
        this.toggled = toggled;
        callMethod();
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void toggle() {
        toggled = !toggled;
        callMethod();
    }

    private void callMethod() {
        if (toggled) {
            onEnable();
        } else {
            onDisable();
        }
    }

    public boolean isHide() {
        return hide;
    }

    public void setHide(boolean hide) {
        this.hide = hide;
    }

    public SimpleAnimation getAnimation() {
        return animation;
    }
}
