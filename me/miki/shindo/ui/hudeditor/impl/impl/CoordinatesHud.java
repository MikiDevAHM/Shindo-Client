package me.miki.shindo.ui.hudeditor.impl.impl;

import me.miki.shindo.Shindo;
import me.miki.shindo.event.EventTarget;
import me.miki.shindo.event.impl.render.Render2DEvent;
import me.miki.shindo.event.impl.tick.ClientTickEvent;
import me.miki.shindo.ui.Style;
import me.miki.shindo.ui.hudeditor.HudEditor;
import me.miki.shindo.ui.hudeditor.impl.HudMod;
import me.miki.shindo.util.MathHelper;
import me.miki.shindo.util.render.GLHelper;
import me.miki.shindo.util.render.Helper2D;
import net.minecraft.util.BlockPos;

public class CoordinatesHud extends HudMod {

    public CoordinatesHud() {
        super("Coordinates", 10, 10);
        setW(120);
        setH(60);
    }

    @Override
    public void renderMod(int mouseX, int mouseY) {
        GLHelper.startScale(getX(), getY(), getSize());
        if (Shindo.getInstance().getModManager().getMod(getName()).isToggled()) {
            if (isModern()) {
                if (isBackground()) {
                    Helper2D.drawRoundedRectangle(getX(), getY(), getW(), getH(), 2, Style.getColor(50).getRGB(), 0);
                }
                Shindo.getInstance().getFontHelper().size20.drawString("X: " + MathHelper.round(mc.thePlayer.posX, 1), getX() + 5, getY() + 5, getColor());
                Shindo.getInstance().getFontHelper().size20.drawString("Y: " + MathHelper.round(mc.thePlayer.posY, 1), getX() + 5, getY() + 5 + 14, getColor());
                Shindo.getInstance().getFontHelper().size20.drawString("Z: " + MathHelper.round(mc.thePlayer.posZ, 1), getX() + 5, getY() + 5 + 28, getColor());
                Shindo.getInstance().getFontHelper().size20.drawString(isBiome() ? "Biome: " + getBiomeName() : "", getX() + 5, getY() + 5 + 42, getColor());
            } else {
                if (isBackground()) {
                    Helper2D.drawRectangle(getX(), getY(), getW(), getH(), Style.getColor(50).getRGB());
                }
                mc.fontRendererObj.drawString("X: " + MathHelper.round(mc.thePlayer.posX, 1), getX() + 5, getY() + 5, getColor());
                mc.fontRendererObj.drawString("Y: " + MathHelper.round(mc.thePlayer.posY, 1), getX() + 5, getY() + 5 + 14, getColor());
                mc.fontRendererObj.drawString("Z: " + MathHelper.round(mc.thePlayer.posZ, 1), getX() + 5, getY() + 5 + 28, getColor());
                mc.fontRendererObj.drawString(isBiome() ? "Biome: " + getBiomeName() : "", getX() + 5, getY() + 5 + 42, getColor());
            }
            super.renderMod(mouseX, mouseY);
        }
        GLHelper.endScale();
    }

    @EventTarget
    public void onRender2D(Render2DEvent e) {
        GLHelper.startScale(getX(), getY(), getSize());
        if (Shindo.getInstance().getModManager().getMod(getName()).isToggled() && !(mc.currentScreen instanceof HudEditor)) {
            if (isModern()) {
                if (isBackground()) {
                    Helper2D.drawRoundedRectangle(getX(), getY(), getW(), getH(), 2, 0x50000000, 0);
                }
                Shindo.getInstance().getFontHelper().size20.drawString("X: " + MathHelper.round(mc.thePlayer.posX, 1), getX() + 5, getY() + 5, getColor());
                Shindo.getInstance().getFontHelper().size20.drawString("Y: " + MathHelper.round(mc.thePlayer.posY, 1), getX() + 5, getY() + 5 + 14, getColor());
                Shindo.getInstance().getFontHelper().size20.drawString("Z: " + MathHelper.round(mc.thePlayer.posZ, 1), getX() + 5, getY() + 5 + 28, getColor());
                Shindo.getInstance().getFontHelper().size20.drawString(isBiome() ? "Biome: " + getBiomeName() : "", getX() + 5, getY() + 5 + 42, getColor());
            } else {
                if (isBackground()) {
                    Helper2D.drawRectangle(getX(), getY(), getW(), getH(), 0x50000000);
                }
                mc.fontRendererObj.drawString("X: " + MathHelper.round(mc.thePlayer.posX, 1), getX() + 5, getY() + 5, getColor());
                mc.fontRendererObj.drawString("Y: " + MathHelper.round(mc.thePlayer.posY, 1), getX() + 5, getY() + 5 + 14, getColor());
                mc.fontRendererObj.drawString("Z: " + MathHelper.round(mc.thePlayer.posZ, 1), getX() + 5, getY() + 5 + 28, getColor());
                mc.fontRendererObj.drawString(isBiome() ? "Biome: " + getBiomeName() : "", getX() + 5, getY() + 5 + 42, getColor());
            }
        }
        GLHelper.endScale();
    }

    @EventTarget
    public void onTick(ClientTickEvent e) {
        if (isBiome()) {
            setW(120);
            setH(60);
        } else {
            setW(70);
            setH(45);
        }
    }

    private int getColor() {
        return Shindo.getInstance().getSettingsManager().getSettingByModAndName(getName(), "Font Color").getColor().getRGB();
    }

    private boolean isModern() {
        return Shindo.getInstance().getSettingsManager().getSettingByModAndName(getName(), "Mode").getCurrentMode().equalsIgnoreCase("Modern");
    }

    private boolean isBackground() {
        return Shindo.getInstance().getSettingsManager().getSettingByModAndName(getName(), "Background").isCheckToggled();
    }

    private boolean isBiome() {
        return Shindo.getInstance().getSettingsManager().getSettingByModAndName(getName(), "Biome").isCheckToggled();
    }

    private String getBiomeName() {
        return mc.theWorld.getBiomeGenForCoords(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ)).biomeName;
    }
}
