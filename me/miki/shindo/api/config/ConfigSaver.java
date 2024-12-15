package me.miki.shindo.api.config;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

import com.google.gson.Gson;

import me.miki.shindo.Shindo;
import me.miki.shindo.feature.mod.Mod;
import me.miki.shindo.feature.option.Option;
import me.miki.shindo.ui.Style;
import me.miki.shindo.util.OSHelper;

public class ConfigSaver {

    /**
     * Creates and saves a configuration in .minecraft/cloud/config.json
     */

    public static void saveConfig() throws IOException {
        createDir();

        FileWriter writer = new FileWriter(OSHelper.getShindoDir() + "config.json");

        Config config = new Config();

        for (Mod mod : Shindo.getInstance().getModManager().getMods()) {
            ModConfig modConfig = new ModConfig(
                    mod.getName(),
                    mod.isToggled(),
                    Shindo.getInstance().getSettingsManager().getSettingsByMod(mod),
                    Shindo.getInstance().getHudEditor().getHudMod(mod.getName()) != null ?
                            new int[] { Shindo.getInstance().getHudEditor().getHudMod(mod.getName()).getX(), Shindo.getInstance().getHudEditor().getHudMod(mod.getName()).getY() } :
                            new int[] { 0, 0 },
                            Shindo.getInstance().getHudEditor().getHudMod(mod.getName()) != null ?
                            Shindo.getInstance().getHudEditor().getHudMod(mod.getName()).getSize() : 1
            );
            config.addConfig(modConfig);
        }

        for(Option option : Shindo.getInstance().getOptionManager().getOptions()){
            config.addConfigOption(option);
        }

        config.setDarkMode(Style.isDarkMode());
        config.setSnapping(Style.isSnapping());

        String json = new Gson().toJson(config);
        writer.write(json);
        writer.close();
    }

    /**
     * Creates the .minecraft/cloud directory if it cannot be found
     */

    private static void createDir() {
        File file = new File(OSHelper.getShindoDir());
        if (!file.exists()) {
            try {
                Files.createDirectory(file.toPath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        createFile();
    }

    /**
     * Creates the .minecraft/cloud/config.json file if it cannot be found
     */

    private static void createFile() {
        File file = new File(OSHelper.getShindoDir() + "config.json");
        if (!file.exists()) {
            try {
                Files.createFile(file.toPath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Checks if the config .minecraft/cloud/config.json file exists
     *
     * @return Config can be found
     */

    public static boolean configExists() {
        File file = new File(OSHelper.getShindoDir() + "config.json");
        return file.exists();
    }
}
