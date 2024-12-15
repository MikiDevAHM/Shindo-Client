package me.miki.shindo.ui.hudeditor.impl.impl;

import org.lwjgl.opengl.GL11;

import me.miki.shindo.Shindo;
import me.miki.shindo.event.EventTarget;
import me.miki.shindo.event.impl.render.Render2DEvent;
import me.miki.shindo.ui.Style;
import me.miki.shindo.ui.hudeditor.HudEditor;
import me.miki.shindo.ui.hudeditor.impl.HudMod;
import me.miki.shindo.util.render.GLHelper;
import me.miki.shindo.util.render.Helper2D;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class BlockinfoHud extends HudMod {

    public BlockinfoHud() {
        super("BlockInfo", 10, 10);
        setW(130);
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
                setW(Shindo.getInstance().getFontHelper().size20.getStringWidth("Grass Block") + 42);
                Shindo.getInstance().getFontHelper().size20.drawString("Grass Block", getX() + 35, getY() + 10, getColor());

                ItemStack itemStack = new ItemStack(Blocks.grass);
                renderItem(itemStack);
            } else {
                if (isBackground()) {
                    Helper2D.drawRectangle(getX(), getY(), getW(), getH(), Style.getColor(50).getRGB());
                }
                setW(mc.fontRendererObj.getStringWidth("Grass Block") + 42);
                mc.fontRendererObj.drawString("Grass Block", getX() + 35, getY() + 10, getColor());

                ItemStack itemStack = new ItemStack(Blocks.grass);
                renderItem(itemStack);
            }
            super.renderMod(mouseX, mouseY);
        }
        GLHelper.endScale();
    }

    @EventTarget
    public void onRender2D(Render2DEvent e) {
        IBlockState blockState = getLookingAtBlockState();
        if (blockState == null) {
            return;
        }

        GLHelper.startScale(getX(), getY(), getSize());
        if (Shindo.getInstance().getModManager().getMod(getName()).isToggled() && !(mc.currentScreen instanceof HudEditor)) {

            World world = mc.theWorld;
            BlockPos blockPos = getLookingAtBlockPos();
            int meta = blockState.getBlock().getDamageValue(world, blockPos);
            int id = Item.itemRegistry.getIDForObject(blockState.getBlock().getItem(world, blockPos));
            ItemStack finalItem = new ItemStack(Item.getItemById(id), 1, meta);
            if (finalItem.getItem() == null)
                finalItem = new ItemStack(blockState.getBlock());
            if (finalItem.getItem() == null) {
                GLHelper.endScale();
                return;
            }

            if (isModern()) {
                setW(Shindo.getInstance().getFontHelper().size20.getStringWidth(finalItem.getDisplayName()) + 42);
                if (isBackground()) {
                    Helper2D.drawRoundedRectangle(getX(), getY(), getW(), getH(), 2, 0x50000000, 0);
                }
                Shindo.getInstance().getFontHelper().size20.drawString(finalItem.getDisplayName(), getX() + 35, getY() + 10, getColor());
                renderItem(finalItem);
            } else {
                setW(Shindo.getInstance().getFontHelper().size20.getStringWidth(finalItem.getDisplayName()) + 42);
                if (isBackground()) {
                    Helper2D.drawRectangle(getX(), getY(), getW(), getH(), 0x50000000);
                }
                mc.fontRendererObj.drawString(finalItem.getDisplayName(), getX() + 35, getY() + 10, getColor());
                renderItem(finalItem);
            }
        }
        GLHelper.endScale();
    }

    private BlockPos getLookingAtBlockPos() {
        MovingObjectPosition objectMouseOver = mc.objectMouseOver;
        if (objectMouseOver == null) {
            return null;
        }
        return objectMouseOver.getBlockPos();
    }

    public int getColor() {
        return Shindo.getInstance().getSettingsManager().getSettingByModAndName(getName(), "Font Color").getColor().getRGB();
    }

    private boolean isModern() {
        return Shindo.getInstance().getSettingsManager().getSettingByModAndName(getName(), "Mode").getCurrentMode().equalsIgnoreCase("Modern");
    }

    private boolean isBackground() {
        return Shindo.getInstance().getSettingsManager().getSettingByModAndName(getName(), "Background").isCheckToggled();
    }

    private void renderItem(ItemStack stack) {
        GL11.glPushMatrix();
        RenderHelper.enableGUIStandardItemLighting();

        GL11.glTranslatef(getX() + 3, getY() + 3, 1);
        GL11.glScalef(1.5f, 1.5f, 1);
        GL11.glTranslatef(-(getX() + 3), -(getY() + 3), -1);
        mc.getRenderItem().renderItemAndEffectIntoGUI(
                stack, getX() + 3, getY() + 3
        );

        GL11.glPopMatrix();
    }

    private IBlockState getLookingAtBlockState() {
        if (mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && mc.objectMouseOver.getBlockPos() != null) {
            BlockPos blockpos = mc.objectMouseOver.getBlockPos();
            return mc.theWorld.getBlockState(blockpos);
        }
        return null;
    }
}