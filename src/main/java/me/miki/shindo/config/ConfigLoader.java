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
import java.io.FileNotFoundException;
import java.io.FileReader;

public class ConfigLoader {

    /**
     * Loads the config from .minecraft/shindo/config.json
     */
    public static void loadConfig() throws FileNotFoundException {
        FileReader reader = new FileReader("shindo/config.json");

        // Use GsonBuilder para respeitar @Expose
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .registerTypeAdapter(Color.class, new ColorAdapter())
                .create();

        Config config = gson.fromJson(reader, Config.class);

        if (config == null) {
            System.err.println("Config file is empty or malformed.");
            return;
        }

        // Carregar configurações de módulos
        if (config.getConfig() != null) {
            for (ModConfig modConfig : config.getConfig()) {
                Mod mod = Shindo.getInstance().getModManager().getMod(modConfig.getName());
                if (mod != null) {
                    mod.setToggled(modConfig.isToggled());

                    // Sincronizar configurações
                    if (modConfig.getSettings() != null) {
                        for (Setting configSetting : modConfig.getSettings()) {
                            Setting clientSetting = Shindo.getInstance().getSettingManager()
                                    .getSettingByModAndName(mod.getName(), configSetting.getName());
                            if (clientSetting != null) {
                                syncSettings(configSetting, clientSetting);
                            }
                        }
                    }

                    // Atualizar posição e tamanho no HudEditor
                    if (Shindo.getInstance().getHudEditor().getHudMod(mod.getName()) != null) {
                        Shindo.getInstance().getHudEditor().getHudMod(mod.getName()).setX(modConfig.getPositions()[0]);
                        Shindo.getInstance().getHudEditor().getHudMod(mod.getName()).setY(modConfig.getPositions()[1]);
                        Shindo.getInstance().getHudEditor().getHudMod(mod.getName()).setSize(modConfig.getSize());
                    }
                }

                System.out.println("Carregando configurações para o módulo: " + modConfig.getName());
                System.out.println("Settings: " + gson.toJson(modConfig.getSettings()));
            }
        }

        // Carregar opções gerais
        if (config.getOptionsConfigList() != null) {
            for (Option configOption : config.getOptionsConfigList()) {
                Option clientOption = Shindo.getInstance().getOptionManager().getOptionByName(configOption.getName());
                if (clientOption != null) {
                    syncOptions(configOption, clientOption);
                }
            }
        }

        // Carrega opções do chat
        if (config.getChatConfigList() != null) {
            for (Chat configChat : config.getChatConfigList()) {
                Chat clientChat = Shindo.getInstance().getChatManager().getChatByName(configChat.getName());
                if (clientChat != null) {
                    syncChats(configChat, clientChat);
                }
            }
        }

        // Carrega opções dos patches
        if (config.getPatcherConfigList() != null) {
            for (Patcher configPatcher : config.getPatcherConfigList()) {
                Patcher clientPatcher = Shindo.getInstance().getPatcherManager().getPatcherByName(configPatcher.getName());
                if (clientPatcher != null) {
                    syncPatchers(configPatcher, clientPatcher);
                }
            }
        }

        // Atualizar estilo
        Style.setDarkMode(config.isDarkMode());
        Style.setSnapping(config.isSnapping());
    }

    /**
     * Sincroniza configurações entre o arquivo de configuração e o cliente.
     */
    private static void syncSettings(Setting configSetting, Setting clientSetting) {
        switch (configSetting.getMode()) {
            case "CheckBox":
                clientSetting.setCheckToggled(configSetting.isCheckToggled());
                break;
            case "Slider":
                clientSetting.setCurrentNumber(configSetting.getCurrentNumber());
                break;
            case "ModePicker":
                clientSetting.setCurrentMode(configSetting.getCurrentMode());
                clientSetting.setModeIndex(configSetting.getModeIndex());
                break;
            case "ColorPicker":
                clientSetting.setColor(configSetting.getColor());
                clientSetting.setSideColor(configSetting.getSideColor());
                clientSetting.setSideSlider(configSetting.getSideSlider());
                clientSetting.setMainSlider(configSetting.getMainSlider());
                break;
            case "CellGrid":
                clientSetting.setCells(configSetting.getCells());
                break;
            case "Keybinding":
                clientSetting.setKey(configSetting.getKey());
                break;
        }
    }

    /**
     * Sincroniza opções gerais entre o arquivo de configuração e o cliente.
     */
    private static void syncOptions(Option configOption, Option clientOption) {
        switch (configOption.getMode()) {
            case "CheckBox":
                clientOption.setCheckToggled(configOption.isCheckToggled());
                break;
            case "Slider":
                clientOption.setCurrentNumber(configOption.getCurrentNumber());
                break;
            case "ModePicker":
                clientOption.setCurrentMode(configOption.getCurrentMode());
                clientOption.setModeIndex(configOption.getModeIndex());
                break;
            case "ColorPicker":
                clientOption.setColor(configOption.getColor());
                clientOption.setSideColor(configOption.getSideColor());
                clientOption.setSideSlider(configOption.getSideSlider());
                clientOption.setMainSlider(configOption.getMainSlider());
                break;
            case "Keybinding":
                clientOption.setKey(configOption.getKey());
                break;
        }
    }

    private static void syncChats(Chat configChat, Chat clientChat) {
        switch (configChat.getMode()) {
            case "CheckBox":
                clientChat.setCheckToggled(configChat.isCheckToggled());
                break;
            case "Slider":
                clientChat.setCurrentNumber(configChat.getCurrentNumber());
                break;
            case "ModePicker":
                clientChat.setCurrentMode(configChat.getCurrentMode());
                clientChat.setModeIndex(configChat.getModeIndex());
                break;
            case "ColorPicker":
                clientChat.setColor(configChat.getColor());
                clientChat.setSideColor(configChat.getSideColor());
                clientChat.setSideSlider(configChat.getSideSlider());
                clientChat.setMainSlider(configChat.getMainSlider());
                break;
            case "Keybinding":
                clientChat.setKey(configChat.getKey());
                break;
        }
    }

    private static void syncPatchers(Patcher configPatcher, Patcher clientPatcher) {
        switch (configPatcher.getMode()) {
            case "CheckBox":
                clientPatcher.setCheckToggled(configPatcher.isCheckToggled());
                break;
            case "Slider":
                clientPatcher.setCurrentNumber(configPatcher.getCurrentNumber());
                break;
            case "ModePicker":
                clientPatcher.setCurrentMode(configPatcher.getCurrentMode());
                clientPatcher.setModeIndex(configPatcher.getModeIndex());
                break;
            case "ColorPicker":
                clientPatcher.setColor(configPatcher.getColor());
                clientPatcher.setSideColor(configPatcher.getSideColor());
                clientPatcher.setSideSlider(configPatcher.getSideSlider());
                clientPatcher.setMainSlider(configPatcher.getMainSlider());
                break;
            case "Keybinding":
                clientPatcher.setKey(configPatcher.getKey());
                break;
        }
    }
}