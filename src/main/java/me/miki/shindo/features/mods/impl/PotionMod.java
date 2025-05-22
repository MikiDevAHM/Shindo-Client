/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package me.miki.shindo.features.mods.impl;

import me.miki.shindo.Shindo;
import me.miki.shindo.events.EventTarget;
import me.miki.shindo.events.impl.EventRender2D;
import me.miki.shindo.events.impl.EventUpdate;
import me.miki.shindo.features.mods.HUDMod;
import me.miki.shindo.features.nanovg.NanoVGManager;
import me.miki.shindo.features.nanovg.font.Fonts;
import me.miki.shindo.features.settings.Setting;
import me.miki.shindo.helpers.render.GLHelper;
import me.miki.shindo.helpers.render.RenderHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;

import java.util.Arrays;
import java.util.Collection;

public class PotionMod extends HUDMod {

    private int maxString, prevPotionCount;

    private Collection<PotionEffect> potions;

    public PotionMod() {
        super(
                "Potion Status",
                "Shows your active potions on the HUD."
        );

        Shindo.getInstance().getSettingManager().addSetting(new Setting("Compact", this, false));
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {

        if(this.isEditing() || mc.thePlayer == null) {
            potions = Arrays.asList(new PotionEffect(1, 0), new PotionEffect(10, 0));
        }else {
            potions = mc.thePlayer.getActivePotionEffects();
        }
    }

    @EventTarget
    public void onRender2D(EventRender2D event) {

        NanoVGManager nvg = Shindo.getInstance().getNanoVGManager();

        nvg.setupAndDraw(() -> drawNanoVG(nvg));

        if (!potions.isEmpty()) {

            int ySize = isCompact() ? 22 : 23;
            int offsetY = 16;

            for (PotionEffect potioneffect : potions) {

                Potion potion = Potion.potionTypes[potioneffect.getPotionID()];
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                mc.getTextureManager().bindTexture(new ResourceLocation("textures/gui/container/inventory.png"));
                int index = potion.getStatusIconIndex();
                GlStateManager.enableBlend();

                GLHelper.startScale(this.getX(), this.getY(), this.getScale());

                if(isCompact()) {
                    GLHelper.startScale((this.getX() + 21) - 20, (this.getY() + offsetY) - 11 - offsetY - 2F, 18, 18, 0.72F);
                    RenderHelper.drawTexturedModalRect((this.getX() + 21) - 20, (this.getY() + offsetY) - 11, index % 8 * 18, 198 + index / 8 * 18, 18, 18);
                    GLHelper.endScale();
                } else {
                    RenderHelper.drawTexturedModalRect((this.getX() + 21) - 17, (this.getY() + offsetY) - 12, index % 8 * 18, 198 + index / 8 * 18, 18, 18);
                }

                GLHelper.endScale();

                offsetY+=ySize;
            }
        }
    }

    private void drawNanoVG(NanoVGManager nvg) {

        int ySize = isCompact() ? 16 : 23;
        int offsetY = 16;

        if(potions.isEmpty()) {
            maxString = 0;
        }

        if(!potions.isEmpty()) {

            this.drawBackground(maxString + 29, (ySize * potions.size()) + 2);

            for (PotionEffect potioneffect : potions) {

                Potion potion = Potion.potionTypes[potioneffect.getPotionID()];

                String name = I18n.format(potion.getName(), new Object[0]);

                if (potioneffect.getAmplifier() == 1) {
                    name = name + " " + I18n.format("enchantment.level.2", new Object[0]);
                } else if (potioneffect.getAmplifier() == 2) {
                    name = name + " " + I18n.format("enchantment.level.3", new Object[0]);
                } else if (potioneffect.getAmplifier() == 3) {
                    name = name + " " + I18n.format("enchantment.level.4", new Object[0]);
                }

                String time = Potion.getDurationString(potioneffect);

                if(isCompact()) {
                    this.drawText(name + " | " + time, 20, offsetY - 10.5F, 9, Fonts.REGULAR);
                } else {
                    this.drawText(name, 25, offsetY - 12, 9, Fonts.REGULAR);
                    this.drawText(time, 25, offsetY - 1, 8, Fonts.REGULAR);
                }

                offsetY+=ySize;

                if(isCompact()) {

                    float totalWidth = nvg.getTextWidth(name + " | " + time, 9, Fonts.REGULAR);

                    if(maxString < totalWidth || prevPotionCount != potions.size()) {
                        maxString = (int) totalWidth - 4;
                    }
                } else {
                    float levelWidth = nvg.getTextWidth(name, 9, Fonts.REGULAR);
                    float timeWidth = nvg.getTextWidth(time, 9, Fonts.REGULAR);

                    if(maxString < levelWidth || maxString < timeWidth || prevPotionCount != potions.size()) {

                        if(levelWidth > timeWidth) {
                            maxString = (int) (levelWidth);
                        } else {
                            maxString = (int) (timeWidth);
                        }

                        prevPotionCount = potions.size();
                    }
                }
            }
        }

        this.setWidth(maxString + 29);
        this.setHeight((ySize * 2) + 2);
    }

    private boolean isCompact() {
        return Shindo.getInstance().getSettingManager().getSettingByModAndName(getName(), "Compact").isCheckToggled();
    }
}
