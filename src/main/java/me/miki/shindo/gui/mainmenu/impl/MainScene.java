package me.miki.shindo.gui.mainmenu.impl;

import java.awt.Color;

import me.miki.shindo.Shindo;
import me.miki.shindo.gui.mainmenu.GuiShindoMainMenu;
import me.miki.shindo.gui.mainmenu.MainMenuScene;
import me.miki.shindo.management.language.TranslateText;
import me.miki.shindo.management.nanovg.NanoVGManager;
import me.miki.shindo.management.nanovg.font.Fonts;
import me.miki.shindo.management.nanovg.font.LegacyIcon;
import me.miki.shindo.utils.mouse.MouseUtils;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.gui.ScaledResolution;

public class MainScene extends MainMenuScene {

	public MainScene(GuiShindoMainMenu parent) {
		super(parent);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		
		Shindo instance = Shindo.getInstance();
		if(instance.getSoar8Released()){
			instance.setSoar8Released(false);
			this.setCurrentScene(this.getSceneByClass(DiscontinuedSoar8.class));
		} else if(instance.getUpdateNeeded()){
			instance.setUpdateNeeded(false);
			this.setCurrentScene(this.getSceneByClass(UpdateScene.class));
		}
		NanoVGManager nvg = instance.getNanoVGManager();

		nvg.setupAndDraw(() -> drawNanoVG(nvg));
	}
	
	private void drawNanoVG(NanoVGManager nvg) {
		
		ScaledResolution sr = new ScaledResolution(mc);
		
		float yPos = sr.getScaledHeight() / 2 - 22;
		
		nvg.drawCenteredText(LegacyIcon.SOAR, sr.getScaledWidth() / 2, sr.getScaledHeight() / 2 - (nvg.getTextHeight(LegacyIcon.SOAR, 54, Fonts.LEGACYICON) / 2) - 60, Color.WHITE, 54, Fonts.LEGACYICON);
		
		nvg.drawRoundedRect(sr.getScaledWidth() / 2 - (180 / 2), yPos, 180, 20, 4.5F, this.getBackgroundColor());
		nvg.drawCenteredText(TranslateText.SINGLEPLAYER.getText(), sr.getScaledWidth() / 2, yPos + 6.5F, Color.WHITE, 9.5F, Fonts.REGULAR);
		
		nvg.drawRoundedRect(sr.getScaledWidth() / 2 - (180 / 2), yPos + 26, 180, 20, 4.5F, this.getBackgroundColor());
		nvg.drawCenteredText(TranslateText.MULTIPLAYER.getText(), sr.getScaledWidth() / 2, yPos + 6.5F + 26, Color.WHITE, 9.5F, Fonts.REGULAR);
		
		nvg.drawRoundedRect(sr.getScaledWidth() / 2 - (180 / 2), yPos + (26 * 2), 180, 20, 4.5F, this.getBackgroundColor());
		nvg.drawCenteredText(TranslateText.SETTINGS.getText(), sr.getScaledWidth() / 2, yPos + 6.5F + (26 * 2), Color.WHITE, 9.5F, Fonts.REGULAR);
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		
		ScaledResolution sr = new ScaledResolution(mc);
		
		float yPos = sr.getScaledHeight() / 2 - 22;
		
		if(mouseButton == 0) {
			
			if(MouseUtils.isInside(mouseX, mouseY, sr.getScaledWidth() / 2 - (160 / 2), yPos, 160, 20)) {
				mc.displayGuiScreen(new GuiSelectWorld(this.getParent()));
			}
			
			if(MouseUtils.isInside(mouseX, mouseY, sr.getScaledWidth() / 2 - (180 / 2), yPos + 26, 180, 20)) {
				mc.displayGuiScreen(new GuiMultiplayer(this.getParent()));
			}
			
			if(MouseUtils.isInside(mouseX, mouseY, sr.getScaledWidth() / 2 - (180 / 2), yPos + (26 * 2), 180, 20)) {
				mc.displayGuiScreen(new GuiOptions(this.getParent(), mc.gameSettings));
			}
		}
	}
}
