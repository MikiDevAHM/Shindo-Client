package me.miki.shindo.helpers.render;

import me.miki.shindo.helpers.logger.ShindoLogger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.util.ResourceLocation;

public class BlurHelper {

    private static final Minecraft mc = Minecraft.getMinecraft();

    private static ShaderGroup blurShader;
    private static Framebuffer buffer;

    private static float lastScale = 0;
    private static float lastScaleWidth = 0;
    private static float lastScaleHeight = 0;

    public static void reinitShader() {
        try {
            buffer = new Framebuffer(mc.displayWidth, mc.displayHeight, true);
            buffer.setFramebufferColor(0.0F, 0.0F, 0.0F, 0.0F);
            blurShader = new ShaderGroup(mc.getTextureManager(), mc.getResourceManager(), mc.getFramebuffer(), new ResourceLocation("shaders/post/blurArea.json"));
            blurShader.createBindFramebuffers(mc.displayWidth, mc.displayHeight);
        } catch (Exception e) {
            blurShader = null; // importante!
            ShindoLogger.error("Failed to load blur shader.", e);
        }
        if (blurShader == null || ShaderGroup.getListShaders().isEmpty()) {
            ShindoLogger.error("Failed to initialize blur shader!");
        }

    }

    public static void drawBlurScreen(float radius) {

        ScaledResolution sr = new ScaledResolution(mc);

        int factor = sr.getScaleFactor();
        int factor2 = sr.getScaledWidth();
        int factor3 = sr.getScaledHeight();
        int x = 0;
        int y = 0;
        int width = sr.getScaledWidth();
        int height = sr.getScaledHeight();

        if (lastScale != factor || lastScaleWidth != factor2 || lastScaleHeight != factor3) {
            reinitShader();
        }

        lastScale = factor;
        lastScaleWidth = factor2;
        lastScaleHeight = factor3;

        ShaderGroup.getListShaders().get(0).getShaderManager().getShaderUniform("BlurXY").set(x * (sr.getScaleFactor() / 2.0F), (factor3 - height) * (sr.getScaleFactor() / 2.0F));
        ShaderGroup.getListShaders().get(1).getShaderManager().getShaderUniform("BlurXY").set(x * (sr.getScaleFactor() / 2.0F), (factor3 - height) * (sr.getScaleFactor() / 2.0F));
        ShaderGroup.getListShaders().get(0).getShaderManager().getShaderUniform("BlurCoord").set((width - x) * (sr.getScaleFactor() / 2.0F), (height - y) * (sr.getScaleFactor() / 2.0F));
        ShaderGroup.getListShaders().get(1).getShaderManager().getShaderUniform("BlurCoord").set((width - x) * (sr.getScaleFactor() / 2.0F), (height - y) * (sr.getScaleFactor() / 2.0F));
        ShaderGroup.getListShaders().get(0).getShaderManager().getShaderUniform("Radius").set(radius);
        ShaderGroup.getListShaders().get(1).getShaderManager().getShaderUniform("Radius").set(radius);
        blurShader.loadShaderGroup(mc.getTimer().renderPartialTicks);
        mc.getFramebuffer().bindFramebuffer(true);
    }

    public static void drawBlurRegion(int x, int y, int width, int height, float radius) {
        ScaledResolution sr = new ScaledResolution(mc);
        int scaleFactor = sr.getScaleFactor();

        int scaledX = x * scaleFactor;
        int scaledY = (sr.getScaledHeight() - (y + height)) * scaleFactor; // invertido verticalmente
        int scaledWidth = width * scaleFactor;
        int scaledHeight = height * scaleFactor;

        if (lastScale != scaleFactor || lastScaleWidth != sr.getScaledWidth() || lastScaleHeight != sr.getScaledHeight()) {
            reinitShader();
        }

        lastScale = scaleFactor;
        lastScaleWidth = sr.getScaledWidth();
        lastScaleHeight = sr.getScaledHeight();

        // Atualiza uniforms personalizados
        ShaderGroup.getListShaders().get(0).getShaderManager().getShaderUniform("BlurXY").set((float) scaledX, (float) scaledY);
        ShaderGroup.getListShaders().get(1).getShaderManager().getShaderUniform("BlurXY").set((float) scaledX, (float) scaledY);

        ShaderGroup.getListShaders().get(0).getShaderManager().getShaderUniform("BlurCoord").set((float) scaledWidth, (float) scaledHeight);
        ShaderGroup.getListShaders().get(1).getShaderManager().getShaderUniform("BlurCoord").set((float) scaledWidth, (float) scaledHeight);

        ShaderGroup.getListShaders().get(0).getShaderManager().getShaderUniform("Radius").set(radius);
        ShaderGroup.getListShaders().get(1).getShaderManager().getShaderUniform("Radius").set(radius);

        blurShader.loadShaderGroup(mc.getTimer().renderPartialTicks);
        mc.getFramebuffer().bindFramebuffer(true);
    }
}
