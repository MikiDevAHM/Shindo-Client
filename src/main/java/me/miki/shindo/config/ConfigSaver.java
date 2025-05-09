/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package me.miki.shindo.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.miki.shindo.Shindo;
import me.miki.shindo.features.chat.Chat;
import me.miki.shindo.features.mods.Mod;
import me.miki.shindo.features.options.Option;
import me.miki.shindo.features.patcher.Patcher;
import me.miki.shindo.features.settings.Setting;
import me.miki.shindo.ui.Style;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

public class ConfigSaver {

    /**
     * Creates and saves a configuration in .minecraft/cloud/config.json
     */

    public static void saveConfig() throws IOException {
        createDir();

        FileWriter writer = new FileWriter("shindo/config.json");

        Config config = new Config();

        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .registerTypeAdapter(Color.class, new ColorAdapter())
                .setPrettyPrinting()
                .create();

        for (Mod mod : Shindo.getInstance().getModManager().getMods()) {
            ArrayList<Setting> settings = Shindo.getInstance().getSettingManager().getSettingsByMod(mod);

            if (settings == null) {
                settings = new ArrayList<>();
            }

            ModConfig modConfig = new ModConfig(
                    mod.getName(),
                    mod.isToggled(),
                    settings,
                    Shindo.getInstance().getHudEditor().getHudMod(mod.getName()) != null ?
                            new int[] { Shindo.getInstance().getHudEditor().getHudMod(mod.getName()).getX(), Shindo.getInstance().getHudEditor().getHudMod(mod.getName()).getY() } :
                            new int[] { 0, 0 },
                    Shindo.getInstance().getHudEditor().getHudMod(mod.getName()) != null ?
                            Shindo.getInstance().getHudEditor().getHudMod(mod.getName()).getSize() : 1
            );
            config.addConfig(modConfig);


            System.out.println("Salvando configurações para o módulo: " + mod.getName());
            System.out.println("Settings: " + gson.toJson(settings));
        }

        for(Option option : Shindo.getInstance().getOptionManager().getOptions()){
            config.addConfigOption(option);
        }

        for (Chat chat : Shindo.getInstance().getChatManager().getChat()) {
            config.addConfigChat(chat);
        }

        for (Patcher patcher : Shindo.getInstance().getPatcherManager().getPatcher()) {
            config.addConfigPatcher(patcher);
        }

        config.setDarkMode(Style.isDarkMode());
        config.setSnapping(Style.isSnapping());

        String json = gson.toJson(config);
        writer.write(json);
        writer.close();
    }

    /**
     * Creates the .minecraft/cloud directory if it cannot be found
     */

    private static void createDir() {
        File file = new File("shindo");
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
        File file = new File("shindo/config.json");
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
        File file = new File("shindo/config.json");
        return file.exists();
    }
}