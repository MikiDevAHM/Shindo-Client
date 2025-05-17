package me.miki.shindo.ui.mainmenu.impl;

import me.miki.shindo.Shindo;
import me.miki.shindo.helpers.MathHelper;
import me.miki.shindo.helpers.animation.simple.SimpleAnimation;
import me.miki.shindo.helpers.render.Helper2D;
import me.miki.shindo.ui.mainmenu.MainMenuScene;
import me.miki.shindo.ui.mainmenu.ShindoMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;

public class MainScene extends MainMenuScene {

	public MainScene(ShindoMainMenu parent) {
		super(parent);
	}


	private SimpleAnimation singleplayer = new SimpleAnimation();
	private SimpleAnimation multiplayer = new SimpleAnimation();
	private SimpleAnimation settings = new SimpleAnimation();

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		
		Shindo instance = Shindo.getInstance();

		draw(mouseX, mouseY);
	}
	
	private void draw(int mouseX, int mouseY) {
		
		ScaledResolution sr = new ScaledResolution(mc);
		
		int yPos = sr.getScaledHeight() / 2 - 22;
		
		Helper2D.drawPicture(sr.getScaledWidth() / 2 - 27, sr.getScaledHeight() / 2 - 84, 54, 54, 0, "logo.png");

		singleplayer.setAnimation(MathHelper.withinBox(sr.getScaledWidth() / 2 - (180 / 2), yPos, 180, 20, mouseX, mouseY) ? 1.0F : 0.0F, 16);

		Helper2D.drawRoundedRectangle(sr.getScaledWidth() / 2 - (180 / 2), yPos, 180, 20, 4, this.getBackgroundColor().getRGB(), 0);
		Shindo.getInstance().getFontHelper().size20.drawString("Singleplayer", (float) sr.getScaledWidth() / 2 - (Shindo.getInstance().getFontHelper().size20.getStringWidth("Singleplayer") / 2F), yPos + 6, new Color(255 - (int) (singleplayer.getValue() * 200), 255, 255 - (int) (singleplayer.getValue() * 200)).getRGB());

		multiplayer.setAnimation(MathHelper.withinBox(sr.getScaledWidth() / 2 - (180 / 2), yPos + 26, 180, 20, mouseX, mouseY) ? 1.0F : 0.0F, 16);
		Helper2D.drawRoundedRectangle(sr.getScaledWidth() / 2 - (180 / 2), yPos + 26, 180, 20, 4, this.getBackgroundColor().getRGB(), 0);
		Shindo.getInstance().getFontHelper().size20.drawString("Multiplayer", (float) sr.getScaledWidth() / 2 - (Shindo.getInstance().getFontHelper().size20.getStringWidth("Multiplayer") / 2F), yPos + 6.5F + 26, new Color(255 - (int) (multiplayer.getValue() * 200), 255, 255 - (int) (multiplayer.getValue() * 200)).getRGB());

		settings.setAnimation(MathHelper.withinBox(sr.getScaledWidth() / 2 - (180 / 2), yPos + (26 * 2), 180, 20, mouseX, mouseY) ? 1.0F : 0.0F, 16);
		Helper2D.drawRoundedRectangle(sr.getScaledWidth() / 2 - (180 / 2), yPos + (26 * 2), 180, 20, 4, this.getBackgroundColor().getRGB(), 0);
		Shindo.getInstance().getFontHelper().size20.drawString("Settings", (float) sr.getScaledWidth() / 2 - (Shindo.getInstance().getFontHelper().size20.getStringWidth("Settings") / 2F), yPos + 6.5F + (26 * 2), new Color(255 - (int) (settings.getValue() * 200), 255, 255 - (int) (settings.getValue() * 200)).getRGB());
	}

	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		
		ScaledResolution sr = new ScaledResolution(mc);
		
		int yPos = sr.getScaledHeight() / 2 - 22;
		
		if(mouseButton == 0) {
			
			if(MathHelper.withinBox(sr.getScaledWidth() / 2 - (160 / 2), yPos, 160, 20, mouseX, mouseY)) {
				mc.displayGuiScreen(new GuiSelectWorld(this.getParent()));
			}
			
			if(MathHelper.withinBox( sr.getScaledWidth() / 2 - (180 / 2), yPos + 26, 180, 20, mouseX, mouseY)) {
				mc.displayGuiScreen(new GuiMultiplayer(this.getParent()));
			}
			
			if(MathHelper.withinBox( sr.getScaledWidth() / 2 - (180 / 2), yPos + (26 * 2), 180, 20, mouseX, mouseY)) {
				mc.displayGuiScreen(new GuiOptions(this.getParent(), mc.gameSettings));
			}
		}
	}
}
