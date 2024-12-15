package me.miki.shindo.ui.splash;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import me.miki.shindo.util.render.Helper2D;
import me.miki.shindo.util.render.UnicodeFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.ResourceLocation;

public class SplashProgress {

    // Max amount of progress updates
    private static final int DEFAULT_MAX = 11;

    // Current progress
    private static int PROGRESS;

    // Currently displayed progress text
    private static String CURRENT = "";

    // Background texture
    private static ResourceLocation splash;

    // Texture manager
    private static TextureManager ctm;

    // Font renderer
    private static UnicodeFontRenderer ufr;

    /**
     * Update the splash text
     */
    public static void update() {
        if (Minecraft.getMinecraft() == null || Minecraft.getMinecraft().getLanguageManager() == null) return;
        drawSplash(Minecraft.getMinecraft().getTextureManager());
    }

    /**
     * Update the splash progress
     *
     * @param givenProgress Stage displayed on the splash
     * @param givenSplash   Text displayed on the splash
     */
    public static void setProgress(int givenProgress, String givenSplash) {
        PROGRESS = givenProgress;
        CURRENT = givenSplash;
        update();
    }

    /**
     * Render the splash screen background
     *
     * @param tm {@link TextureManager}
     */
    public static void drawSplash(TextureManager tm) {
        // Initialize the texture manager if null
        if (ctm == null) ctm = tm;

        // Get the users screen width and height to apply
        ScaledResolution scaledresolution = new ScaledResolution(Minecraft.getMinecraft());

        // Create the scale factor
        int scaleFactor = scaledresolution.getScaleFactor();

        // Bind the width and height to the framebuffer
        Framebuffer framebuffer = new Framebuffer(scaledresolution.getScaledWidth() * scaleFactor,
            scaledresolution.getScaledHeight() * scaleFactor, true);
        framebuffer.bindFramebuffer(false);

        // Create the projected image to be rendered
        GlStateManager.matrixMode(GL11.GL_PROJECTION);
        GlStateManager.loadIdentity();
        GlStateManager.ortho(0.0D, scaledresolution.getScaledWidth(), scaledresolution.getScaledHeight(), 0.0D, 1000.0D, 3000.0D);
        GlStateManager.matrixMode(GL11.GL_MODELVIEW);
        GlStateManager.loadIdentity();
        GlStateManager.translate(0.0F, 0.0F, -2000.0F);
        GlStateManager.disableLighting();
        GlStateManager.disableFog();
        GlStateManager.disableDepth();
        GlStateManager.enableTexture2D();

        // Initialize the splash texture
        if (splash == null) splash = new ResourceLocation("shindo/splash.png");

        // Bind the texture
        tm.bindTexture(splash);

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        // Draw the image
        Gui.drawScaledCustomSizeModalRect(0, 0, 0, 0, 1920, 1080,
            scaledresolution.getScaledWidth(), scaledresolution.getScaledHeight(), 1920, 1080);
        
        // Draw the progress bar
        drawProgress();

        // Unbind the width and height as it's no longer needed
        framebuffer.unbindFramebuffer();

        // Render the previously used frame buffer
        framebuffer.framebufferRender(scaledresolution.getScaledWidth() * scaleFactor, scaledresolution.getScaledHeight() * scaleFactor);

        // Update the texture to enable alpha drawing
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1F);

        // Update the users screen
        Minecraft.getMinecraft().updateDisplay();
    }

    /**
     * Render the progress bar and text
     */
    private static void drawProgress() {
        if (Minecraft.getMinecraft().gameSettings == null || Minecraft.getMinecraft().getTextureManager() == null)
            return;
        
        if (ufr == null) ufr = UnicodeFontRenderer.getFontOnPC("Roboto Regular", 25);
        // Get the users screen width and height to apply
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

        // Calculate the progress bar
        double nProgress = PROGRESS;
        double calc = (nProgress / DEFAULT_MAX) * sr.getScaledWidth();

        // Draw the transparent bar before the green bar
        Gui.drawRect(0, sr.getScaledHeight() - 35, sr.getScaledWidth(), sr.getScaledHeight(), new Color(0, 0, 0, 50).getRGB());
        
        GlStateManager.resetColor();
        resetTextureState();
        // Draw the current splash text
        ufr.drawString(CURRENT, 20, sr.getScaledHeight() - 25, 0xffffffff);

        // Draw the current amount of progress / max amount of progress
        String s = PROGRESS + "/" + DEFAULT_MAX;
        ufr.drawString(s, sr.getScaledWidth() - 20 - ufr.getWidth(s) / 2, sr.getScaledHeight() - 25, 0xe1e1e1ff);
        
        
        GlStateManager.resetColor();
        resetTextureState();

        // Render the blue progress bar
        Gui.drawRect(0, sr.getScaledHeight() - 2, (int) calc, sr.getScaledHeight(), new Color(3, 169, 244).getRGB());

        // Render the bar base
        Gui.drawRect(0, sr.getScaledHeight() - 2, sr.getScaledWidth(), sr.getScaledHeight(), new Color(0, 0, 0, 10).getRGB());
    }
    
    private static void resetTextureState() {
    	GlStateManager.textureState[GlStateManager.activeTextureUnit].textureName = -1;
    }
}
