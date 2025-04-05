package me.miki.shindo.helpers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class ResolutionHelper {

    private static final Minecraft mc = Minecraft.getMinecraft();
    private static ScaledResolution scaledResolution;

    public static int getHeight() {
        scaledResolution = new ScaledResolution(mc);
        return scaledResolution.getScaledHeight();
    }

    public static int getWidth() {
        scaledResolution = new ScaledResolution(mc);
        return scaledResolution.getScaledWidth();
    }

    public static int getFactor() {
        scaledResolution = new ScaledResolution(mc);
        return scaledResolution.getScaleFactor();
    }
}