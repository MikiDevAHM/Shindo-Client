package me.miki.shindo.ui.mods.sidebar.mods.impl;

import me.miki.shindo.Shindo;
import me.miki.shindo.feature.setting.Setting;
import me.miki.shindo.ui.mods.sidebar.mods.Button;

public abstract class Settings {

    public Setting setting;
    public Button button;
    private int h, y;
    private boolean open;
    private int settingHeight;
    private boolean updated;

    public Settings(Setting setting, Button button, int y) {
        Shindo.getInstance().getEventManager().register(this);
        this.setting = setting;
        this.button = button;
        this.open = false;
        this.h = 25;
        this.y = y;
    }

    public abstract void renderSetting(int mouseX, int mouseY);

    public abstract void mouseClicked(int mouseX, int mouseY, int mouseButton);

    public abstract void mouseReleased(int mouseX, int mouseY, int state);

    public abstract void keyTyped(char typedChar, int keyCode);

    public Setting getSetting() {
        return setting;
    }

    public void setSetting(Setting setting) {
        this.setting = setting;
    }

    public Button getButton() {
        return button;
    }

    public void setButton(Button button) {
        this.button = button;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public boolean isUpdated() {
        return updated;
    }

    public void setUpdated(boolean updated) {
        this.updated = updated;
    }

    public int getSettingHeight() {
        return settingHeight;
    }

    public void setSettingHeight(int settingHeight) {
        this.settingHeight = settingHeight;
    }
}
