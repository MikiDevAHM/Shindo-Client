/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package me.miki.shindo.ui.hudeditor.impl.impl;

import dev.cloudmc.Cloud;
import dev.cloudmc.gui.Style;
import dev.cloudmc.gui.hudeditor.HudEditor;
import dev.cloudmc.gui.hudeditor.impl.HudMod;
import dev.cloudmc.helpers.render.GLHelper;
import dev.cloudmc.helpers.render.Helper2D;
import dev.cloudmc.helpers.MathHelper;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class CoordinatesHud extends HudMod {

    public CoordinatesHud(String name, int x, int y) {
        super(name, x, y);
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

    @SubscribeEvent
    public void onRender2D(RenderGameOverlayEvent.Pre.Text e) {
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

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent e) {
        if (isBiome()) {
            setW(120);
            setH(60);
        } else {
            setW(70);
            setH(45);
        }
    }

    private int getColor() {
        return Shindo.getInstance().getSettingManager().getSettingByModAndName(getName(), "Font Color").getColor().getRGB();
    }

    private boolean isModern() {
        return Shindo.getInstance().getSettingManager().getSettingByModAndName(getName(), "Mode").getCurrentMode().equalsIgnoreCase("Modern");
    }

    private boolean isBackground() {
        return Shindo.getInstance().getSettingManager().getSettingByModAndName(getName(), "Background").isCheckToggled();
    }

    private boolean isBiome() {
        return Shindo.getInstance().getSettingManager().getSettingByModAndName(getName(), "Biome").isCheckToggled();
    }

    private String getBiomeName() {
        return mc.theWorld.getBiomeGenForCoords(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ)).biomeName;
    }
}
