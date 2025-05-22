 

package me.miki.shindo.helpers.render;

import me.miki.mp3agic.Mp3File;
import me.miki.mp3agic.interfaces.ID3v2;
import me.miki.shindo.helpers.ColorHelper;
import me.miki.shindo.helpers.logger.ShindoLogger;
import me.miki.shindo.ui.Style;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.lwjgl.opengl.GL11.*;

public class Helper2D {

    private static final Minecraft mc = Minecraft.getMinecraft();
    private static final ResourceLocation DEFAULT_COVER = new ResourceLocation("shindo/icon/music.png");
    private static final Map<File, ResourceLocation> cache = new HashMap<>();

    /**
     * Draws a rounded Rectangle on the HUD using quarter circles and rectangles
     *
     * @param x      Left X coordinate of the rectangle
     * @param y      Top Y coordinate of the rectangle
     * @param w      Width of the rectangle
     * @param h      Height of the rectangle
     * @param radius The radius of the corners
     * @param color  The Color of the rectangle
     * @param index  -1: No Rounded Corners; 0: Rounded Corners on all sides; 1: Rounded Corners on the top; 2: Rounded Corners on the bottom;
     */

    public static void drawRoundedRectangle(int x, int y, int w, int h, int radius, int color, int index) {
        if (index == -1) {
            drawRectangle(x, y, w, h, color);
        } else if (index == 0) {
            drawRectangle(x + radius, y + radius, w - radius * 2, h - radius * 2, color);
            drawRectangle(x + radius, y, w - radius * 2, radius, color);
            drawRectangle(x + w - radius, y + radius, radius, h - radius * 2, color);
            drawRectangle(x + radius, y + h - radius, w - radius * 2, radius, color);
            drawRectangle(x, y + radius, radius, h - radius * 2, color);
            drawCircle(x + radius, y + radius, radius, 180, 270, color);
            drawCircle(x + w - radius, y + radius, radius, 270, 360, color);
            drawCircle(x + radius, y + h - radius, radius, 90, 180, color);
            drawCircle(x + w - radius, y + h - radius, radius, 0, 90, color);
        } else if (index == 1) {
            drawRectangle(x + radius, y, w - radius * 2, radius, color);
            drawRectangle(x, y + radius, w, h - radius, color);
            drawCircle(x + radius, y + radius, radius, 180, 270, color);
            drawCircle(x + w - radius, y + radius, radius, 270, 360, color);
        } else if (index == 2) {
            drawRectangle(x, y, w, h - radius, color);
            drawRectangle(x + radius, y + h - radius, w - radius * 2, radius, color);
            drawCircle(x + radius, y + h - radius, radius, 90, 180, color);
            drawCircle(x + w - radius, y + h - radius, radius, 0, 90, color);
        }
    }

    /**
     * Draws a rectangle using width and height, instead of 4 coordinate positions
     *
     * @param x     Left X coordinate of the rectangle
     * @param y     Top Y coordinate of the rectangle
     * @param w     Width of the rectangle
     * @param h     Height of the rectangle
     * @param color The Color of the rectangle
     */

    public static void drawRectangle(int x, int y, int w, int h, int color) {
        Gui.drawRect(x, y, x + w, y + h, color);
    }

    /**
     * @param startX The first X position
     * @param endX   The second X position
     * @param y      The Y position
     * @param color  The color
     */
    public static void drawHorizontalLine(int startX, int endX, int y, int color) {
        if (endX < startX) {
            int i = startX;
            startX = endX;
            endX = i;
        }

        Gui.drawRect(startX, y, endX + 1, y + 1, color);
    }

    /**
     * @param x      The X position
     * @param startY The first Y position
     * @param endY   The second Y position
     * @param color  The color
     */
    public static void drawVerticalLine(int x, int startY, int endY, int color) {
        if (endY < startY) {
            int i = startY;
            startY = endY;
            endY = i;
        }

        Gui.drawRect(x, startY + 1, x + 1, endY, color);
    }

    /**
     * Draws a given picture
     *
     * @param x        Left X coordinate of the image
     * @param y        Top Y coordinate of the image
     * @param w        Width of the image
     * @param h        Height of the image
     * @param color    The Color of the image, if value is 0, normal color is used
     * @param location The location of the image to be loaded from
     */

    public static void drawPicture(int x, int y, int w, int h, int color, String location) {
        if (color == 0) {
            GlStateManager.color(1, 1, 1);
        } else {
            ColorHelper.setColor(color);
        }
        ResourceLocation resourceLocation = new ResourceLocation("shindo/" + location);
        mc.getTextureManager().bindTexture(resourceLocation);
        GlStateManager.enableBlend();
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0F, 0.0F, w, h, w, h);
        GlStateManager.disableBlend();
    }

    public static void drawPicture(int x, int y, int w, int h, int color, ResourceLocation location) {
        if (color == 0) {
            GlStateManager.color(1, 1, 1);
        } else {
            ColorHelper.setColor(color);
        }
        mc.getTextureManager().bindTexture(location);
        GlStateManager.enableBlend();
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0F, 0.0F, w, h, w, h);
        GlStateManager.disableBlend();
    }

    public static void drawPicture(int x, int y, int w, int h, int color, File location) {
        if (color == 0) {
            GlStateManager.color(1, 1, 1);
        } else {
            ColorHelper.setColor(color);
        }
        renderImage(location, x, y, w, h);
    }

    /**
     * Draws an outline of a rectangle
     *
     * @param x     Left X coordinate of the rectangle outline
     * @param y     Top Y coordinate of the rectangle outline
     * @param w     Width of the rectangle outline
     * @param h     Height of the rectangle outline
     * @param t     The Width of the rectangle outline
     * @param color The Color of the rectangle outline
     */

    public static void drawOutlinedRectangle(int x, int y, int w, int h, int t, int color) {
        drawRectangle(x, y, w, t, color);
        drawRectangle(x + w - t, y, t, h, color);
        drawRectangle(x, y + h - t, w, t, color);
        drawRectangle(x, y, t, h, color);
    }

    /**
     * Draws a rectangle gradient from 4 given coordinates and 2 given colors vertically
     *
     * @param x          X coordinate of the rectangle
     * @param y          Y coordinate of the rectangle
     * @param w          Width of the rectangle
     * @param h          Height of the rectangle
     * @param startColor The first color of the gradient
     * @param endColor   The second color of the gradient
     */

    public static void drawGradientRectangle(float x, float y, float w, float h, int startColor, int endColor) {
        float f1 = (float) (startColor >> 24 & 255) / 255.0F;
        float f2 = (float) (startColor >> 16 & 255) / 255.0F;
        float f3 = (float) (startColor >> 8 & 255) / 255.0F;
        float f4 = (float) (startColor & 255) / 255.0F;
        float f5 = (float) (endColor >> 24 & 255) / 255.0F;
        float f6 = (float) (endColor >> 16 & 255) / 255.0F;
        float f7 = (float) (endColor >> 8 & 255) / 255.0F;
        float f8 = (float) (endColor & 255) / 255.0F;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldRenderer.pos(x + w, y, 0f).color(f2, f3, f4, f1).endVertex();
        worldRenderer.pos(x, y, 0f).color(f2, f3, f4, f1).endVertex();
        worldRenderer.pos(x, y + h, 0f).color(f6, f7, f8, f5).endVertex();
        worldRenderer.pos(x + w, y + h, 0f).color(f6, f7, f8, f5).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    /**
     * Draws a rectangle gradient from 4 given coordinates and 2 given colors horizontally
     *
     * @param x          X coordinate of the rectangle
     * @param y          Y coordinate of the rectangle
     * @param w          Width of the rectangle
     * @param h          Height of the rectangle
     * @param startColor The first color of the gradient
     * @param endColor   The second color of the gradient
     */

    public static void drawHorizontalGradientRectangle(float x, float y, float w, float h, int startColor, int endColor) {
        float f1 = (float) (startColor >> 24 & 255) / 255.0F;
        float f2 = (float) (startColor >> 16 & 255) / 255.0F;
        float f3 = (float) (startColor >> 8 & 255) / 255.0F;
        float f4 = (float) (startColor & 255) / 255.0F;
        float f5 = (float) (endColor >> 24 & 255) / 255.0F;
        float f6 = (float) (endColor >> 16 & 255) / 255.0F;
        float f7 = (float) (endColor >> 8 & 255) / 255.0F;
        float f8 = (float) (endColor & 255) / 255.0F;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldRenderer.pos(x, y, 0f).color(f2, f3, f4, f1).endVertex();
        worldRenderer.pos(x, y + h, 0f).color(f2, f3, f4, f1).endVertex();
        worldRenderer.pos(x + w, y + h, 0f).color(f6, f7, f8, f5).endVertex();
        worldRenderer.pos(x + w, y, 0f).color(f6, f7, f8, f5).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height) {
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(x, y + height, 0).tex((float) (textureX) * f, (float) (textureY + height) * f1).endVertex();
        worldrenderer.pos(x + width, y + height, 0).tex((float) (textureX + width) * f, (float) (textureY + height) * f1).endVertex();
        worldrenderer.pos(x + width, y, 0).tex((float) (textureX + width) * f, (float) (textureY) * f1).endVertex();
        worldrenderer.pos(x, y, 0).tex((float) (textureX) * f, (float) (textureY) * f1).endVertex();
        tessellator.draw();
    }

    /**
     * Draws a circle
     *
     * @param x     Left X coordinate of the circle
     * @param y     Top Y coordinate of the circle
     * @param r     The radius of the circle
     * @param h     The beginning of from where the circle should be drawn
     * @param j     The ending of from where the circle should be drawn
     * @param color The color of the circle
     */

    public static void drawCircle(float x, float y, float r, int h, int j, int color) {
        glEnable(GL_BLEND);
        glDisable(GL_CULL_FACE);
        glDisable(GL_TEXTURE_2D);
        glBegin(GL_TRIANGLE_FAN);

        ColorHelper.setColor(color);

        float var;
        glVertex2f(x, y);
        for (var = h; var <= j; var++) {
            ColorHelper.setColor(color);
            glVertex2f(
                    (float) (r * Math.cos(Math.PI * var / 180) + x),
                    (float) (r * Math.sin(Math.PI * var / 180) + y)
            );
        }

        glEnd();
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_CULL_FACE);
        glDisable(GL_BLEND);
    }

    public static void drawImage(int x, int y, int width, int height) {
        drawImage(x, y, width, height, 0F, 0F, 1F, 1F);
    }

    public static void drawImage(int x, int y, int width, int height, float u, float v, float uWidth, float vHeight) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer renderer = tessellator.getWorldRenderer();

        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.color(1F, 1F, 1F, 1F);

        renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        renderer.pos(x, y + height, 0).tex(u, v + vHeight).endVertex();
        renderer.pos(x + width, y + height, 0).tex(u + uWidth, v + vHeight).endVertex();
        renderer.pos(x + width, y, 0).tex(u + uWidth, v).endVertex();
        renderer.pos(x, y, 0).tex(u, v).endVertex();
        tessellator.draw();

        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
    }

    public static void renderCover(File mp3File, int x, int y, int size) {
        ResourceLocation texture = getOrLoadTexture(mp3File);
        if (texture == null) return;

        // Desenha fundo com cantos arredondados (máscara visual)
        Helper2D.drawRoundedRectangle(x, y, size, size, 6, Style.getColorTheme(3).getRGB(), 0); // cor de fundo ou máscara

        // Renderiza textura da capa por cima
        mc.getTextureManager().bindTexture(texture);
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.color(1F, 1F, 1F, 1F);

        Helper2D.drawImage(x + 1, y + 1, size - 2, size - 2); // levemente menor que o fundo

        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
    }

    public static ResourceLocation getOrLoadTexture(File file) {
        if (cache.containsKey(file)) return cache.get(file);

        try {
            Mp3File mp3 = new Mp3File(file);
            if (mp3.hasId3v2Tag()) {
                ID3v2 tag = mp3.getId3v2Tag();
                byte[] imageData = tag.getAlbumImage();
                if (imageData != null) {
                    BufferedImage cover = ImageIO.read(new ByteArrayInputStream(imageData));
                    if (cover != null) {
                        ResourceLocation texture = registerTexture(cover);
                        cache.put(file, texture);
                        return texture;
                    }
                }
            }
        } catch (Exception e) {
            ShindoLogger.error("Failed to load texture: " + file, e);
        }

        cache.put(file, DEFAULT_COVER);
        return DEFAULT_COVER;
    }

    private static ResourceLocation registerTexture(BufferedImage image) {
        try {
            DynamicTexture dynamicTexture = new DynamicTexture(image);
            return mc.getTextureManager().getDynamicTextureLocation("cover_" + UUID.randomUUID(), dynamicTexture);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ResourceLocation registerImage(File file) {
        if (cache.containsKey(file)) return cache.get(file);

        try {
            BufferedImage image = ImageIO.read(file);
            if (image == null) {
                ShindoLogger.error("Arquivo não é uma imagem válida: " + file.getAbsolutePath());
                return DEFAULT_COVER;
            }

            DynamicTexture dynamicTexture = new DynamicTexture(image);
            ResourceLocation texture = mc.getTextureManager()
                    .getDynamicTextureLocation("custom_image_" + UUID.randomUUID(), dynamicTexture);

            cache.put(file, texture);
            return texture;
        } catch (IOException e) {
            ShindoLogger.error("Erro ao carregar imagem: " + file.getAbsolutePath(), e);
        }

        return DEFAULT_COVER;
    }

    public static void unregisterImage(File file) {
        ResourceLocation texture = cache.remove(file);
        if (texture != null) {
            mc.getTextureManager().deleteTexture(texture); // libera da GPU
        }
    }

    public static void renderImage(File file, int x, int y, int width, int height) {
        ResourceLocation texture = cache.get(file);
        if (texture == null) {
            texture = registerImage(file); // Registra automaticamente se ainda não estiver no cache
            if (texture == null) return;
        }

        // Desenha fundo com cantos arredondados
        Helper2D.drawRoundedRectangle(x, y, width, height, 6, Style.getColorTheme(3).getRGB(), 0);

        // Renderiza a imagem por cima
        mc.getTextureManager().bindTexture(texture);
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.color(1F, 1F, 1F, 1F);

        Helper2D.drawImage(x + 1, y + 1, width - 2, height - 2);

        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
    }
}