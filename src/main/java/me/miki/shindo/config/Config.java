/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package me.miki.shindo.config;

import com.google.gson.annotations.Expose;
import me.miki.shindo.features.chat.Chat;
import me.miki.shindo.features.options.Option;
import me.miki.shindo.features.patcher.Patcher;

import java.util.ArrayList;

public class Config {

    @Expose
    private final ArrayList<ModConfig> modConfigList = new ArrayList<>();

    @Expose
    private final ArrayList<Option> optionsConfigList = new ArrayList<>();

    @Expose
    private final ArrayList<Chat> chatConfigList = new ArrayList<>();

    @Expose
    private final ArrayList<Patcher> patcherConfigList = new ArrayList<>();

    @Expose
    private boolean darkMode = false;

    @Expose
    private boolean snapping = true;

    public void addConfig(ModConfig modConfig) {
        modConfigList.add(modConfig);
    }

    public void addConfigOption(Option option) {
        optionsConfigList.add(option);
    }

    public void addConfigChat(Chat chat) {
        chatConfigList.add(chat);
    }

    public void addConfigPatcher(Patcher patcher) {
        patcherConfigList.add(patcher);
    }

    public boolean isDarkMode() {
        return darkMode;
    }

    public void setDarkMode(boolean toggled) {
        darkMode = toggled;
    }

    public boolean isSnapping() {
        return snapping;
    }

    public void setSnapping(boolean snapping) {
        this.snapping = snapping;
    }

    public ArrayList<ModConfig> getConfig() {
        return modConfigList;
    }

    public ArrayList<Option> getOptionsConfigList() {
        return optionsConfigList;
    }

    public ArrayList<Chat> getChatConfigList() {
        return chatConfigList;
    }

    public ArrayList<Patcher> getPatcherConfigList() {
        return patcherConfigList;
    }
}
