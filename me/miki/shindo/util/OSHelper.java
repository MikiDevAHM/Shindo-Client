package me.miki.shindo.util;

import java.io.File;

import net.minecraft.client.Minecraft;

public class OSHelper {
	
	private static  Minecraft mc = Minecraft.getMinecraft();

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

    public static String getShindoDir() {
        return getMinecraftDirectory() + "shindo" + File.separator;
    }
}
