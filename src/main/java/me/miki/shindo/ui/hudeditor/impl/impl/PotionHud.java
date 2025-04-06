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
import net.minecraft.client.gui.Gui;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Collection;

public class PotionHud extends HudMod {

    private final ResourceLocation inventoryBackground = new ResourceLocation("textures/gui/container/inventory.png");
    private final Gui gui = new Gui();
    Potion potion = Potion.potionTypes[5];
    Potion potion2 = Potion.potionTypes[10];
    Potion potion3 = Potion.potionTypes[16];

    public PotionHud(String name, int x, int y) {
        super(name, x, y);
        setW(120);
        setH(30);
    }

    @Override
    public void renderMod(int mouseX, int mouseY) {
        GLHelper.startScale(getX(), getY(), getSize());
        if (Shindo.getInstance().getModManager().getMod(getName()).isToggled()) {
            if (isModern()) {
                if (isBackground()) {
                    Helper2D.drawRoundedRectangle(getX(), getY(), getW(), getH(), 2, Style.getColor(50).getRGB(), 0);
                }
                drawPotion(potion, new PotionEffect(5, 1, 1), 0, true, isTime());
                drawPotion(potion2, new PotionEffect(10, 1, 1), 30, true, isTime());
                drawPotion(potion3, new PotionEffect(16, 1, 1), 60, true, isTime());
            } else {
                if (isBackground()) {
                    Helper2D.drawRectangle(getX(), getY(), getW(), getH(), Style.getColor(50).getRGB());
                }
                drawPotion(potion, new PotionEffect(5, 1, 1), 0, false, isTime());
                drawPotion(potion2, new PotionEffect(10, 1, 1), 30, false, isTime());
                drawPotion(potion3, new PotionEffect(16, 1, 1), 60, false, isTime());
            }
            super.renderMod(mouseX, mouseY);
            setH(90);
        }
        GLHelper.endScale();
    }

    @SubscribeEvent
    public void onRender2D(RenderGameOverlayEvent.Pre.Text e) {
        GLHelper.startScale(getX(), getY(), getSize());
        Collection<PotionEffect> collection = mc.thePlayer.getActivePotionEffects();
        if (Shindo.getInstance().getModManager().getMod(getName()).isToggled() && !(mc.currentScreen instanceof HudEditor)) {
            if (isModern()) {
                if (isBackground() && !collection.isEmpty()) {
                    Helper2D.drawRoundedRectangle(getX(), getY(), getW(), getH(), 2, 0x50000000, 0);
                }
                int addY = 0;
                for (PotionEffect effect : mc.thePlayer.getActivePotionEffects()) {
                    Potion potion = Potion.potionTypes[effect.getPotionID()];
                    drawPotion(potion, effect, addY, true, isTime());
                    addY += 30;
                    setH(addY);
                }
            } else {
                if (isBackground() && !collection.isEmpty()) {
                    Helper2D.drawRectangle(getX(), getY(), getW(), getH(), 0x50000000);
                }
                int addY = 0;
                for (PotionEffect effect : mc.thePlayer.getActivePotionEffects()) {
                    Potion potion = Potion.potionTypes[effect.getPotionID()];
                    drawPotion(potion, effect, addY, false, isTime());
                    addY += 30;
                    setH(addY);
                }
            }
        }
        GLHelper.endScale();
    }

    private void drawPotion(Potion potion, PotionEffect effect, int y, boolean modern, boolean time) {
        if (modern) {
            Shindo.getInstance().getFontHelper().size20.drawString(I18n.format(potion.getName()), getX() + 30, time ? getY() + 5 + y : getY() + 10 + y, getColor());
            if (time) {
                Shindo.getInstance().getFontHelper().size20.drawString(Potion.getDurationString(effect), getX() + 30, getY() + 17 + y, getColor());
            }
        } else {
            mc.fontRendererObj.drawString(I18n.format(potion.getName()), getX() + 30, time ? getY() + 5 + y : getY() + 10 + y, getColor());
            if (time) {
                mc.fontRendererObj.drawString(Potion.getDurationString(effect), getX() + 30, getY() + 17 + y, getColor());
            }
        }
        mc.getTextureManager().bindTexture(inventoryBackground);
        gui.drawTexturedModalRect(getX() + 5, getY() + 5 + y, potion.getStatusIconIndex() % 8 * 18, 198 + potion.getStatusIconIndex() / 8 * 18, 18, 18);
    }

    public int getColor() {
        return Shindo.getInstance().getSettingManager().getSettingByModAndName(getName(), "Font Color").getColor().getRGB();
    }

    private boolean isModern() {
        return Shindo.getInstance().getSettingManager().getSettingByModAndName(getName(), "Mode").getCurrentMode().equalsIgnoreCase("Modern");
    }

    private boolean isBackground() {
        return Shindo.getInstance().getSettingManager().getSettingByModAndName(getName(), "Background").isCheckToggled();
    }

    private boolean isTime() {
        return Shindo.getInstance().getSettingManager().getSettingByModAndName(getName(), "Show Time").isCheckToggled();
    }
}
