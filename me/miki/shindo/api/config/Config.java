package me.miki.shindo.api.config;

import java.util.ArrayList;

import me.miki.shindo.feature.option.Option;

public class Config {

    private final ArrayList<ModConfig> modConfigList;
    private final ArrayList<Option> optionsConfigList;
    private boolean darkMode;
    private boolean snapping;

    public Config() {
        modConfigList = new ArrayList<>();
        optionsConfigList = new ArrayList<>();
        darkMode = false;
        snapping = true;
    }

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
