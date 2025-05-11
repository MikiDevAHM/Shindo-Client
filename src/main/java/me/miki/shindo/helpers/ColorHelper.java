/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package me.miki.shindo.helpers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.nio.FloatBuffer;

public class ColorHelper {

    private static Minecraft mc;

    /**
     * Sets the color using an int color value using GlStateManager
     *
     * @param color The given int color value
     */

    public static void color(int color) {
        float alpha = (color >> 24 & 255) / 255f;
        float red = (color >> 16 & 255) / 255f;
        float green = (color >> 8 & 255) / 255f;
        float blue = (color & 255) / 255f;
        GlStateManager.color(red, green, blue, alpha);
    }

    /**
     * Returns a Color from an int color value
     *
     * @param color The given int color value
     * @return The Color value
     */

    public static Color intColorToRGB(int color) {
        float alpha = (color >> 24 & 255) / 255f;
        float red = (color >> 16 & 255) / 255f;
        float green = (color >> 8 & 255) / 255f;
        float blue = (color & 255) / 255f;
        return new Color(red, green, blue, alpha);
    }

    /**
     * Returns a Color from an X and Y position on the screen
     *
     * @param x The X Coordinate
     * @param y The Y Coordinate
     * @return The Color
     */

    public static Color getColorAtPixel(float x, float y) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc == null || mc.displayWidth <= 0 || mc.displayHeight <= 0) {
            return Color.BLACK; // fallback
        }

        int factor = ResolutionHelper.getFactor();
        FloatBuffer floatBuffer = BufferUtils.createFloatBuffer(3);
        GL11.glReadPixels(
                (int) (x * factor),
                (int) (mc.displayHeight - y * factor),
                1, 1, GL11.GL_RGB, GL11.GL_FLOAT, floatBuffer
        );
        float red = floatBuffer.get(0);
        float green = floatBuffer.get(1);
        float blue = floatBuffer.get(2);
        return new Color(red, green, blue);
    }
}