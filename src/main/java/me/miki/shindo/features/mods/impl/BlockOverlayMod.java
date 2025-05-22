/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package me.miki.shindo.features.mods.impl;

import me.miki.shindo.Shindo;
import me.miki.shindo.features.mods.Mod;
import me.miki.shindo.features.mods.Type;
import me.miki.shindo.features.settings.Setting;
import me.miki.shindo.helpers.render.Helper3D;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class BlockOverlayMod extends Mod {

    public BlockOverlayMod() {
        super(
                "BlockOverlay",
                "Adds an customizable overlay to blocks.",
                Type.Visual
        );

        Shindo.getInstance().getSettingManager().addSetting(new Setting("Outline", this, true));
        Shindo.getInstance().getSettingManager().addSetting(new Setting("Filling", this, true));
        Shindo.getInstance().getSettingManager().addSetting(new Setting("Thickness", this, 20, 3, 1));
        Shindo.getInstance().getSettingManager().addSetting(new Setting("Outline Color", this, new Color(0, 0, 0), new Color(255, 0, 0), 0, new float[]{0, 65}));
        Shindo.getInstance().getSettingManager().addSetting(new Setting("Filling Color", this, new Color(0, 0, 0), new Color(255, 0, 0), 0, new float[]{0, 65}));
        Shindo.getInstance().getSettingManager().addSetting(new Setting("Alpha", this, 255, 100, 1));
    }

    public void drawSelectionBox(EntityPlayer player, MovingObjectPosition movingObjectPositionIn, int execute, float partialTicks) {
        if (execute == 0 && movingObjectPositionIn.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GL11.glLineWidth(getThickness());
            GlStateManager.disableTexture2D();
            GlStateManager.depthMask(false);
            BlockPos blockpos = movingObjectPositionIn.getBlockPos();
            Block block = mc.theWorld.getBlockState(blockpos).getBlock();

            if (block.getMaterial() != Material.air && mc.theWorld.getWorldBorder().contains(blockpos)) {
                block.setBlockBoundsBasedOnState(mc.theWorld, blockpos);
                double d0 = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double) partialTicks;
                double d1 = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double) partialTicks;
                double d2 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double) partialTicks;

                double var = 0.002F;
                AxisAlignedBB box =
                        block.getSelectedBoundingBox(mc.theWorld, blockpos)
                                .expand(var, var, var)
                                .offset(-d0, -d1, -d2);

                if (isFilling()) {
                    GlStateManager.color(
                            getFillColor().getRed() / 255f,
                            getFillColor().getGreen() / 255f,
                            getFillColor().getBlue() / 255f,
                            getAlpha() / 255f
                    );
                    Helper3D.drawFilledBoundingBox(box);
                }
                if (isOutline()) {
                    GlStateManager.color(
                            getOutlineColor().getRed() / 255f,
                            getOutlineColor().getGreen() / 255f,
                            getOutlineColor().getBlue() / 255f,
                            getAlpha() / 255f
                    );
                    RenderGlobal.drawSelectionBoundingBox(box);
                }
            }

            GlStateManager.depthMask(true);
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
        }
    }

    private float getThickness() {
        return Shindo.getInstance().getSettingManager().getSettingByModAndName(getName(), "Thickness").getCurrentNumber();
    }

    private boolean isOutline() {
        return Shindo.getInstance().getSettingManager().getSettingByModAndName(getName(), "Outline").isCheckToggled();
    }

    private boolean isFilling() {
        return Shindo.getInstance().getSettingManager().getSettingByModAndName(getName(), "Filling").isCheckToggled();
    }

    private Color getFillColor() {
        return Shindo.getInstance().getSettingManager().getSettingByModAndName(getName(), "Filling Color").getColor();
    }

    private Color getOutlineColor() {
        return Shindo.getInstance().getSettingManager().getSettingByModAndName(getName(), "Outline Color").getColor();
    }

    private int getAlpha() {
        return (int) Shindo.getInstance().getSettingManager().getSettingByModAndName(getName(), "Alpha").getCurrentNumber();
    }
}
