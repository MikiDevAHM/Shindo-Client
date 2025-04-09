/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package me.miki.shindo.config;

import com.google.gson.annotations.Expose;
import me.miki.shindo.features.options.Option;

import java.util.ArrayList;

public class Config {

    @Expose
    private final ArrayList<ModConfig> modConfigList = new ArrayList<>();

    @Expose
    private final ArrayList<Option> optionsConfigList = new ArrayList<>();

    @Expose
    private boolean darkMode = false;

    @Expose
    private boolean snapping = true;

    public void addConfig(ModConfig modConfig) {
        modConfigList.add(modConfig);
    }

    public void addConfigOption(Option option){
        optionsConfigList.add(option);
    }

    public void setDarkMode(boolean toggled){
        darkMode = toggled;
    }

    public boolean isDarkMode(){
        return darkMode;
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
}
