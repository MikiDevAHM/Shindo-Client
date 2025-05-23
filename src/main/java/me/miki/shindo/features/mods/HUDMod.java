package me.miki.shindo.features.mods;

import me.miki.shindo.Shindo;
import me.miki.shindo.features.color.AccentColor;
import me.miki.shindo.features.color.ColorManager;
import me.miki.shindo.features.nanovg.NanoVGManager;
import me.miki.shindo.features.nanovg.font.Font;
import me.miki.shindo.helpers.ColorHelper;
import me.miki.shindo.ui.hudeditor.HudEditor;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.io.File;

public class HUDMod extends Mod {

    private int x, y, draggingX, draggingY, width, height;
    private float scale;
    private boolean dragging, draggable;

    public HUDMod(String nameTranslate, String descriptionText) {
        super(nameTranslate, descriptionText, Type.Hud);

        this.x = 100;
        this.y = 100;
        this.width = 100;
        this.height = 100;
        this.scale = 1.0F;
        this.draggable = true;
    }

    public void save() {

        Shindo instance = Shindo.getInstance();
        NanoVGManager nvg = instance.getNanoVGManager();

        nvg.save();
    }

    public void restore() {

        Shindo instance = Shindo.getInstance();
        NanoVGManager nvg = instance.getNanoVGManager();

        nvg.restore();
    }

    public void scissor(float addX, float addY, float width, float height) {

        Shindo instance = Shindo.getInstance();
        NanoVGManager nvg = instance.getNanoVGManager();

        nvg.scissor(x + (addX * scale), y + (addY * scale), width * scale, height * scale);
    }

    public void drawPlayerHead(ResourceLocation location, float addX, float addY, float width, float height, float radius) {

        Shindo instance = Shindo.getInstance();
        NanoVGManager nvg = instance.getNanoVGManager();

        nvg.drawPlayerHead(location, x + (addX * scale), y + (addY * scale), width * scale, height * scale, radius * scale);
    }

    public void drawRoundedImage(int texture, float addX, float addY, float width, float height, float radius) {

        Shindo instance = Shindo.getInstance();
        NanoVGManager nvg = instance.getNanoVGManager();

        nvg.drawRoundedImage(texture, x + (addX * scale), y + (addY * scale), width * scale, height * scale, radius * scale);
    }

    public void drawRoundedImage(File file, float addX, float addY, float width, float height, float radius, float alpha) {

        Shindo instance = Shindo.getInstance();
        NanoVGManager nvg = instance.getNanoVGManager();

        nvg.drawRoundedImage(file, x + (addX * scale), y + (addY * scale), width * scale, height * scale, radius * scale, alpha);
    }

    public void drawRoundedImage(File file, float addX, float addY, float width, float height, float radius) {
        drawRoundedImage(file, addX, addY, width, height, radius, 1.0F);
    }

    public void drawRoundedImage(ResourceLocation location, float addX, float addY, float width, float height, float radius) {

        Shindo instance = Shindo.getInstance();
        NanoVGManager nvg = instance.getNanoVGManager();

        nvg.drawRoundedImage(location, x + (addX * scale), y + (addY * scale), width * scale, height * scale, radius * scale);
    }

    public void drawArc(float addX, float addY, float radius, float startAngle, float endAngle, float strokeWidth, Color color) {

        Shindo instance = Shindo.getInstance();
        NanoVGManager nvg = instance.getNanoVGManager();

        nvg.drawArc(x + (addX * scale), y + (addY * scale), radius * scale, startAngle, endAngle, strokeWidth * scale, color);
    }

    public void drawArc(float addX, float addY, float radius, float startAngle, float endAngle, float strokeWidth) {
        drawArc(addX, addY, radius, startAngle, endAngle, strokeWidth, this.getFontColor());
    }

    public void drawShadow(float addX, float addY, float width, float height, float radius) {

        Shindo instance = Shindo.getInstance();
        NanoVGManager nvg = instance.getNanoVGManager();

        nvg.drawShadow(x + (addX * scale), y + (addY * scale), width * scale, height * scale, radius * scale);
    }

    public void drawRect(float addX, float addY, float width, float height, Color color) {

        Shindo instance = Shindo.getInstance();
        NanoVGManager nvg = instance.getNanoVGManager();

        nvg.drawRect(x + (addX * scale), y + (addY * scale), width * scale, height * scale, color);
    }

    public void drawRect(float addX, float addY, float width, float height) {
        drawRect(addX, addY, width, height, this.getFontColor());
    }

    public void drawRoundedRect(float addX, float addY, float width, float height, float radius, Color color) {

        Shindo instance = Shindo.getInstance();
        NanoVGManager nvg = instance.getNanoVGManager();

        if(width < 0) {
            return;
        }

        if(height < 0) {
            return;
        }

        nvg.drawRoundedRect(x + (addX * scale), y + (addY * scale), width * scale, height * scale, radius * scale, color);
    }

    public void drawRoundedRect(float addX, float addY, float width, float height, float radius) {
        drawRoundedRect(addX, addY, width, height, radius, this.getFontColor());
    }

    public void drawBackground(float addX, float addY, float width, float height, float radius) {

        Shindo instance = Shindo.getInstance();
        NanoVGManager nvg = instance.getNanoVGManager();
        ColorManager colorManager = instance.getColorManager();
        AccentColor currentColor = colorManager.getCurrentColor();
        String theme = Shindo.getInstance().getOptionManager().getOptionByName("HUD Style").getCurrentMode();

        boolean isNormal = theme.equals("Normal");
        boolean isVanilla = theme.equals("Vanilla");
        boolean isGlow = theme.equals("Glow");
        boolean isVanillaGlow = theme.equals("Vanilla Glow");
        boolean isOutline = theme.equals("Outline");
        boolean isOutlineGlow = theme.equals("Outline Glow");
        boolean isShadow = theme.equals("Shadow");
        boolean isDark = theme.equals("Dark");
        boolean isLight = theme.equals("Light");

        float lastWidth = width * scale;
        float lastHeight = height * scale;
        float x = this.x + (addX * scale);
        float y = this.y + (addY * scale);

        if(isNormal || isVanilla || isShadow || isDark || isLight) {
            nvg.drawShadow(x, y, lastWidth, lastHeight, radius);
        }else if(isGlow || isVanillaGlow) {
            nvg.drawGradientShadow(x, y, lastWidth, lastHeight, radius, currentColor.getColor1(), currentColor.getColor2());
        }else if(isOutline || isOutlineGlow) {
            if(isOutline) {
                nvg.drawShadow(x - 2, y - 2, lastWidth + 4, lastHeight + 4, radius + 2);
            }else if(isOutlineGlow) {
                nvg.drawGradientShadow(x - 2, y - 2, lastWidth + 4, lastHeight + 4, radius + 2, currentColor.getColor1(), currentColor.getColor2());
            }
        }

        if(isOutline || isOutlineGlow) {
            nvg.drawGradientOutlineRoundedRect(x - 1, y - 1, lastWidth + 2, lastHeight + 2, radius + 1, 1.5F, currentColor.getColor1(), currentColor.getColor2());
        }

        if(isVanilla || isVanillaGlow || isOutline || isOutlineGlow) {
            nvg.drawRoundedRect(x, y, lastWidth, lastHeight, radius, new Color(0, 0, 0, 100));
        }else if(isNormal || isGlow) {
            nvg.drawGradientRoundedRect(x, y, lastWidth, lastHeight, radius, ColorHelper.applyAlpha(currentColor.getColor1(), 220), ColorHelper.applyAlpha(currentColor.getColor2(), 220));
        } else if(isLight) {
            nvg.drawRoundedRect(x, y, lastWidth, lastHeight, radius, new Color(240, 240, 240, 220));
        } else if(isDark) {
            nvg.drawRoundedRect(x, y, lastWidth, lastHeight, radius, new Color(20, 20, 20, 220));
        }
    }

    public void drawBackground(float width, float height) {
        drawBackground(0, 0, width, height, 6 * scale);
    }

    public void drawBackground(float addX, float addY, float width, float height) {
        drawBackground(addX, addY, width, height, 6 * scale);
    }

    public void drawBackground(float width, float height, float radius) {
        drawBackground(0, 0, width, height, radius);
    }

    public void drawText(String text, float addX, float addY, float size, Font font, Color color) {

        NanoVGManager nvg = Shindo.getInstance().getNanoVGManager();
        float lastSize = size * scale;

        nvg.drawText(text, x + (addX * scale), y + (addY * scale), color, lastSize, font);
    }

    public void scale(float addX, float addY, float width, float height, float nvgScale) {

        NanoVGManager nvg = Shindo.getInstance().getNanoVGManager();

        nvg.scale(x + (addX * scale), y + (addY * scale), width * scale, height * scale, nvgScale);
    }

    public void drawText(String text, float addX, float addY, float size, Font font) {
        drawText(text, addX, addY, size, font, getFontColor());
    }

    public void drawCenteredText(String text, float addX, float addY, float size, Font font, Color color) {

        NanoVGManager nvg = Shindo.getInstance().getNanoVGManager();
        float lastSize = size * scale;

        nvg.drawCenteredText(text, x + (addX * scale), y + (addY * scale), color, lastSize, font);
    }

    public void drawCenteredText(String text, float addX, float addY, float size, Font font) {
        drawCenteredText(text, addX, addY, size, font, getFontColor());
    }

    public float getTextWidth(String text, float size, Font font) {

        NanoVGManager nvg = Shindo.getInstance().getNanoVGManager();

        return nvg.getTextWidth(text, size, font);
    }

    public Color getFontColor(int alpha) {

        String theme = Shindo.getInstance().getOptionManager().getOptionByName("HUD Style").getCurrentMode();

        boolean isDark = theme.equals("Dark");
        boolean isLight = theme.equals("Light");

        if(isDark || isLight) {
            return Shindo.getInstance().getColorManager().getCurrentColor().getInterpolateColor(alpha);
        }

        return new Color(255, 255, 255, alpha);
    }

    public Color getFontColor() {
        return getFontColor(255);
    }

    public boolean isEditing() {
        return mc.currentScreen instanceof HudEditor;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getDraggingX() {
        return draggingX;
    }

    public void setDraggingX(int draggingX) {
        this.draggingX = draggingX;
    }

    public int getDraggingY() {
        return draggingY;
    }

    public void setDraggingY(int draggingY) {
        this.draggingY = draggingY;
    }

    public int getWidth() {
        return (int) (width * scale);
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return (int) (height * scale);
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isDragging() {
        return dragging;
    }

    public void setDragging(boolean dragging) {
        this.dragging = dragging;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {

        if(scale > 5.0 || scale < 0.2) {

            if(scale > 5.0) {
                this.scale = 5.0F;
            }

            if(scale < 0.2) {
                this.scale = 0.2F;
            }

            return;
        }

        this.scale = scale;
    }

    public boolean isDraggable() {
        return draggable;
    }

    public void setDraggable(boolean draggable) {
        this.draggable = draggable;
    }
}
