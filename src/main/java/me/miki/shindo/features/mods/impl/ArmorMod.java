/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package me.miki.shindo.features.mods.impl;

import me.miki.shindo.Shindo;
import me.miki.shindo.events.EventTarget;
import me.miki.shindo.events.impl.EventRender2D;
import me.miki.shindo.features.mods.HUDMod;
import me.miki.shindo.features.settings.Setting;
import me.miki.shindo.helpers.render.GLHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class ArmorMod extends HUDMod {

    public ArmorMod() {
        super(
                "Armor Status",
                "Displays your Armor on the HUD."
        );

        String[] mode = {"Horizontal", "Vertical"};
        Shindo.getInstance().getSettingManager().addSetting(new Setting("Mode", this, "Horizontal", 0, mode));
        Shindo.getInstance().getSettingManager().addSetting(new Setting("Value", this, false));
    }

    @EventTarget
    public void onRender2D(EventRender2D event) {


        ItemStack[] fakeStack = new ItemStack[4];

        fakeStack[3] = new ItemStack(Items.diamond_helmet);
        fakeStack[2] = new ItemStack(Items.diamond_chestplate);
        fakeStack[1] = new ItemStack(Items.diamond_leggings);
        fakeStack[0] = new ItemStack(Items.diamond_boots);

        GLHelper.startScale(this.getX(), this.getY(), this.getScale());
        this.renderItems(this.isEditing() ? fakeStack : mc.thePlayer.inventory.armorInventory, isVertical());
        GLHelper.endScale();

        this.setWidth(isVertical() ? 16 : 16 * 4);
        this.setHeight(isVertical() ? 16 * 4 : 16);
    }

    private void renderItems(ItemStack[] items, boolean vertical) {

        for(int i = 0; i < 4; i++) {

            ItemStack item = items[Math.abs(3 - i)];
            int addX = vertical ? 0 : 16 * i;
            int addY = vertical ? 16 * i : 0;

            if(item != null) {
                this.drawItemStack(item, this.getX() + addX, this.getY() + addY);
            }
        }
    }

    private void drawItemStack(ItemStack stack, int x, int y) {

        GlStateManager.pushMatrix();
        RenderHelper.enableGUIStandardItemLighting();
        RenderItem itemRender = mc.getRenderItem();

        GlStateManager.translate(0, 0, 32);
        float prevZ = itemRender.zLevel;

        itemRender.zLevel = 0.0F;

        itemRender.renderItemAndEffectIntoGUI(stack, x, y);

        if(isValue()) {
            itemRender.renderItemOverlayIntoGUI(fr, stack, x, y, "");
        }

        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableLighting();
        GlStateManager.popMatrix();
        itemRender.zLevel = prevZ;
    }

    private boolean isVertical() {
        return Shindo.getInstance().getSettingManager().getSettingByModAndName(getName(), "Mode").getCurrentMode().equalsIgnoreCase("Vertical");
    }

    private boolean isValue() {
        return Shindo.getInstance().getSettingManager().getSettingByModAndName(getName(), "Value").isCheckToggled();
    }
}
