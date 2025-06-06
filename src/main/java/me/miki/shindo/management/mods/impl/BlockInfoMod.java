package me.miki.shindo.management.mods.impl;

import me.miki.shindo.management.event.EventTarget;
import me.miki.shindo.management.event.impl.EventRender2D;
import me.miki.shindo.management.language.TranslateText;
import me.miki.shindo.management.mods.HUDMod;
import me.miki.shindo.utils.GlUtils;
import me.miki.shindo.utils.animation.normal.Animation;
import me.miki.shindo.utils.animation.normal.Direction;
import me.miki.shindo.utils.animation.normal.easing.EaseBackIn;
import me.miki.shindo.utils.buffer.ScreenAnimation;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;

public class BlockInfoMod extends HUDMod {

	private long lastSelection;
	
	private Animation introAnimation;
	private ScreenAnimation screenAnimation = new ScreenAnimation();
	
    private BlockPos pos;
    private IBlockState state;
    private Block block;
    
	public BlockInfoMod() {
		super(TranslateText.BLOCK_INFO, TranslateText.BLOCK_INFO_DESCRIPTION, "waila");
	}

	@Override
	public void setup() {
		introAnimation = new EaseBackIn(320, 1.0F, 2.0F);
		introAnimation.setDirection(Direction.BACKWARDS);
	}
	
	@EventTarget
	public void onRender2D(EventRender2D event) {

		screenAnimation.wrap(() -> drawBlock(), () -> drawNanoVG(), this.getX(), this.getY(), this.getWidth(), this.getHeight(), 2 - introAnimation.getValueFloat(), introAnimation.getValueFloat());
		
		this.setWidth(80);
		this.setHeight(80);
	}
	
	private void drawBlock() {
		
		if(block != null && !block.equals(Blocks.portal) && !block.equals(Blocks.end_portal)) {
			GlUtils.startScale(this.getX(), this.getY(), this.getWidth(), this.getHeight(), 2F * this.getScale());
			RenderHelper.enableGUIStandardItemLighting();
			GlStateManager.enableColorMaterial();
            GlStateManager.colorMask(true, true, true, false);
			mc.getRenderItem().renderItemAndEffectIntoGUI(new ItemStack(block), this.getX() + (this.getWidth() / 2) - 8, this.getY() + (this.getHeight() / 2) - 8);
			RenderHelper.disableStandardItemLighting();
            GlStateManager.colorMask(true, true, true, true);
			GlUtils.stopScale();
		}
	}
	
	private void drawNanoVG() {
		
		if((mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == MovingObjectType.BLOCK) || this.isEditing()) {
			
			if(this.isEditing()) {
				block = Blocks.grass;
			}else {
				pos = mc.objectMouseOver.getBlockPos();
				state = mc.theWorld.getBlockState(pos);
				block = state.getBlock();
			}
			
			introAnimation.setDirection(Direction.FORWARDS);
			lastSelection = System.currentTimeMillis();
		}else {
			if(System.currentTimeMillis() - lastSelection > 1000) {
				introAnimation.setDirection(Direction.BACKWARDS);
			}
		}
		
		if(block != null && !block.equals(Blocks.portal) && !block.equals(Blocks.end_portal)) {
			
			this.drawBackground(80, 80);
			
			this.drawCenteredText(block.getLocalizedName(), 40, 6, 9, getHudFont(1));
		}
	}
}
