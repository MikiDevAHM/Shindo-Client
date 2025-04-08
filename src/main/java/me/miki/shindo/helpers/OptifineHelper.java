package me.miki.shindo.helpers;

import net.minecraft.client.Minecraft;

public class OptifineHelper {

    private static final Minecraft mc = Minecraft.getMinecraft();

    
    public static void disableFastRender() {
        mc.gameSettings.ofFastRender = false;
		mc.gameSettings.useVbo = true;
		mc.gameSettings.fboEnable = true;
    }
}
