package me.miki.shindo.api.config;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.FileReader;

import com.google.gson.Gson;

import me.miki.shindo.Shindo;
import me.miki.shindo.feature.mod.Mod;
import me.miki.shindo.feature.option.Option;
import me.miki.shindo.feature.setting.Setting;
import me.miki.shindo.ui.Style;
import me.miki.shindo.util.OSHelper;

public class ConfigLoader {

    /**
     * Loads the config from .minecraft/cloud/config.json
     */

    public static void loadConfig() throws FileNotFoundException {
        FileReader reader = new FileReader(OSHelper.getShindoDir() + "config.json");

        Config config = new Gson().fromJson(reader, Config.class);

        for (int i = 0; i < config.getConfig().size(); i++) {
            Mod mod = Shindo.getInstance().getModManager().getMods().get(i);
            mod.setToggled(config.getConfig().get(i).isToggled());
            for (int j = 0; j < config.getConfig().get(i).getSettings().size(); j++) {
                Setting configSetting = config.getConfig().get(i).getSettings().get(j);
                Setting clientSetting = Shindo.getInstance().getSettingsManager().getSettingsByMod(mod).get(j);
                switch (config.getConfig().get(i).getSettings().get(j).getMode()) {
                    case "CheckBox":
                        boolean toggled = configSetting.isCheckToggled();
                        clientSetting.setCheckToggled(toggled);
                        break;
                    case "Slider":
                        float amount = configSetting.getCurrentNumber();
                        clientSetting.setCurrentNumber(amount);
                        break;
                    case "ModePicker":
                        String mode = configSetting.getCurrentMode();
                        int index = configSetting.getModeIndex();
                        clientSetting.setCurrentMode(mode);
                        clientSetting.setModeIndex(index);
                        break;
                    case "ColorPicker":
                        Color color = configSetting.getColor();
                        Color sideColor = configSetting.getSideColor();
                        float sideSlider = configSetting.getSideSlider();
                        float[] mainSlider = configSetting.getMainSlider();
                        clientSetting.setColor(color);
                        clientSetting.setSideColor(sideColor);
                        clientSetting.setSideSlider(sideSlider);
                        clientSetting.setMainSlider(mainSlider);
                        break;
                    case "CellGrid":
                        boolean[][] cells = configSetting.getCells();
                        clientSetting.setCells(cells);
                        break;
                    case "Keybinding":
                        int key = configSetting.getKey();
                        clientSetting.setKey(key);
                        break;
                }
            }

            if (Shindo.getInstance().getHudEditor().getHudMod(mod.getName()) != null) {
            	Shindo.getInstance().getHudEditor().getHudMod(mod.getName()).setX(config.getConfig().get(i).getPositions()[0]);
            	Shindo.getInstance().getHudEditor().getHudMod(mod.getName()).setY(config.getConfig().get(i).getPositions()[1]);
            	Shindo.getInstance().getHudEditor().getHudMod(mod.getName()).setSize(config.getConfig().get(i).getSize());
            }
        }

        for(int i = 0; i < config.getOptionsConfigList().size(); i++){
            Option configOption = config.getOptionsConfigList().get(i);
            Option clientOption = Shindo.getInstance().getOptionManager().getOptions().get(i);
            switch (configOption.getMode()) {
                case "CheckBox":
                    boolean toggled = configOption.isCheckToggled();
                    clientOption.setCheckToggled(toggled);
                    break;
                case "Slider":
                    float amount = configOption.getCurrentNumber();
                    clientOption.setCurrentNumber(amount);
                    break;
                case "ModePicker":
                    String mode = configOption.getCurrentMode();
                    int index = configOption.getModeIndex();
                    clientOption.setCurrentMode(mode);
                    clientOption.setModeIndex(index);
                    break;
                case "ColorPicker":
                    Color color = configOption.getColor();
                    Color sideColor = configOption.getSideColor();
                    float sideSlider = configOption.getSideSlider();
                    float[] mainSlider = configOption.getMainSlider();
                    clientOption.setColor(color);
                    clientOption.setSideColor(sideColor);
                    clientOption.setSideSlider(sideSlider);
                    clientOption.setMainSlider(mainSlider);
                    break;
                case "CellGrid":
                    boolean[][] cells = configOption.getCells();
                    clientOption.setCells(cells);
                    break;
                case "Keybinding":
                    int key = configOption.getKey();
                    clientOption.setKey(key);
                    break;
            }
        }

        Style.setDarkMode(config.isDarkMode());
        Style.setSnapping(config.isSnapping());
    }
}