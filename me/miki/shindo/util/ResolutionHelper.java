package me.miki.shindo.util;

import me.miki.shindo.Shindo;
import net.minecraft.client.gui.ScaledResolution;

public class ResolutionHelper {

    private static ScaledResolution scaledResolution;

    public static int getHeight() {
        scaledResolution = new ScaledResolution(Shindo.getInstance().mc);
        return scaledResolution.getScaledHeight();
    }

    public static int getWidth() {
        scaledResolution = new ScaledResolution(Shindo.getInstance().mc);
        return scaledResolution.getScaledWidth();
    }

    public static int getFactor() {
        scaledResolution = new ScaledResolution(Shindo.getInstance().mc);
        return scaledResolution.getScaleFactor();
    }
}