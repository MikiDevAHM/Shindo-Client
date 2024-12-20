package me.miki.shindo.ui.mods;

import java.io.IOException;

import me.miki.shindo.Shindo;
import me.miki.shindo.ui.Style;
import me.miki.shindo.util.MathHelper;
import me.miki.shindo.util.ResolutionHelper;
import me.miki.shindo.util.TimeHelper;
import me.miki.shindo.util.animation.Animate;
import me.miki.shindo.util.animation.Easing;
import me.miki.shindo.util.render.Helper2D;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

public class ModsUI extends GuiScreen 
{
	
    private final Panel panel = new Panel();

    private final Animate animateModMenu = new Animate();
	private final Animate animateClock = new Animate();
	private final Animate animateSnapping = new Animate();
    
    public ModsUI() {
    	animateModMenu.setEase(Easing.CUBIC_OUT).setMin(0).setSpeed(1000).setReversed(false);
        animateClock.setEase(Easing.CUBIC_OUT).setMin(0).setMax(50).setSpeed(100).setReversed(false);
        animateSnapping.setEase(Easing.CUBIC_IN).setMin(0).setMax(50).setSpeed(100).setReversed(false);
    }
	
	
	
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		
		 Helper2D.drawRectangle(0, 0, width, height, 0x70000000);
	     boolean roundedCorners = Shindo.getInstance().getOptionManager().getOptionByName("Rounded Corners").isCheckToggled();
	     int color = Shindo.getInstance().getOptionManager().getOptionByName("Color").getColor().getRGB();
		 
		 float max = ResolutionHelper.getHeight() / 2f + 150;
	     animateModMenu.setMax(max).update();
	     if(!animateModMenu.hasFinished()) {
	         panel.setY(height - animateModMenu.getValueI());
	     }
	     panel.renderPanel(mouseX, mouseY);
	     panel.updatePosition(mouseX, mouseY);

	     /*
	     Draws the time at the top right
	     */

	     animateClock.update();

	     Helper2D.drawRoundedRectangle(width - 130, animateClock.getValueI() - 60, 140, 60, 10, Style.getColor(50).getRGB(), roundedCorners ? 0 : -1);
	     Helper2D.drawPicture(width - 50, 5 - 50 + animateClock.getValueI(), 40, 40, 0, "icon/clock.png");

	     Shindo.getInstance().getFontHelper().size40.drawString(TimeHelper.getFormattedTimeMinute(), width - 120, 10 - 50 + animateClock.getValueI(), color);
	     Shindo.getInstance().getFontHelper().size20.drawString(TimeHelper.getFormattedDate(), width - 120, 30 - 50 + animateClock.getValueI(), color);

	     /*
	     Draws the dark and light mode button on the bottom left
	     */

	     animateSnapping.update();
	     Helper2D.drawRoundedRectangle(10, height - 50, 40, 40, 2, Style.getColor(40).getRGB(), roundedCorners ? 0 : -1);
	     Helper2D.drawPicture(15, height - 45, 30, 30, color, Style.isDarkMode() ? "icon/dark.png" : "icon/light.png");
	     
	     Helper2D.drawRoundedRectangle(60, height - 50 + animateSnapping.getValueI(), 40, 40, 2, Style.getColor(40).getRGB(), roundedCorners ? 0 : -1);
	     Helper2D.drawPicture(65, height - 45 + animateSnapping.getValueI(), 30, 30, color, Style.isSnapping() ? "icon/grid.png" : "icon/nogrid.png");
	}
	
	 @Override
	 public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
	     panel.mouseClicked(mouseX, mouseY, mouseButton);
	     if(mouseButton == 0) {
	         if (MathHelper.withinBox(
	                 panel.getX(), panel.getY(),
	                 panel.getW(), panel.getH(),
	                 mouseX, mouseY
	         )) {
	             panel.setDragging(true);
	             panel.setOffsetX(mouseX - panel.getX());
	             panel.setOffsetY(mouseY - panel.getY());
	         }

	         if (MathHelper.withinBox(10, height - 50, 40, 40, mouseX, mouseY)) {
	             Style.setDarkMode(!Style.isDarkMode());
	         }
	     }
	     super.mouseClicked(mouseX, mouseY, mouseButton);
	 }

	 @Override
	 public void mouseReleased(int mouseX, int mouseY, int state) {
	     panel.setDragging(false);
	     panel.mouseReleased(mouseX, mouseY, state);
	     super.mouseReleased(mouseX, mouseY, state);
	 }

	 @Override
	 public void keyTyped(char typedChar, int keyCode) throws IOException {
	     panel.keyTyped(typedChar, keyCode);
	     super.keyTyped(typedChar, keyCode);
	 }

	 /**
	  * Loads a shader to blur the screen when the gui is opened
	  */

	 @Override
	 public void initGui() {
	     panel.initGui();
	     mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
	     super.initGui();
	 }

	 /**
	  * Deleted all shaderGroups in order to remove the screen blur when the gui is closed
	  */

	 @Override
	 public void onGuiClosed() {
	     if (mc.entityRenderer.getShaderGroup() != null) {
	         mc.entityRenderer.getShaderGroup().deleteShaderGroup();
	     }
	     super.onGuiClosed();
	 }
}
