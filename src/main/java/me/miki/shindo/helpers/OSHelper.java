/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package me.miki.shindo.helpers;

import net.minecraft.client.Minecraft;

import java.io.File;

public class OSHelper {

    private static final Minecraft mc = Minecraft.getMinecraft();
    private static final String currentOS = System.getProperty("os.name").toLowerCase();

    public static boolean isWindows() { return (currentOS.contains("windows")); }
    public static boolean isMac() { return (currentOS.contains("mac")); }
    public static boolean isLinux() { return (currentOS.contains("linux")); }

    /**
     * Returns the Location of the .minecraft directory
     *
     * @return Directory
     */

    public static String getMinecraftDirectory() {
        return mc.mcDataDir.getAbsolutePath() + File.separator;
    }

    public static String getCloudDirectory() {
        return getMinecraftDirectory() + "shindo" + File.separator;
    }
}
