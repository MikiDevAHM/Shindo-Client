package me.miki.shindo.ui.mainmenu.impl.welcome;

import me.miki.shindo.Shindo;
import me.miki.shindo.helpers.TimerHelper;
import me.miki.shindo.helpers.render.BlurHelper;
import me.miki.shindo.ui.mainmenu.MainMenuScene;
import me.miki.shindo.ui.mainmenu.ShindoMainMenu;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;

import java.awt.*;

public class CheckingDataScene extends MainMenuScene {

	TimerHelper timer = new TimerHelper();

	public CheckingDataScene(ShindoMainMenu parent) {
		super(parent);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		
		ScaledResolution sr = new ScaledResolution(mc);
		String message = "Checking the data...";
		
		BlurHelper.drawBlurScreen(14);

		if(timer.delay(1200)) {
			message = "Press SPACE to continue";
		}


		Shindo.getInstance().getFontHelper().size20.drawString(message, (float) sr.getScaledWidth() / 2,
				((float) sr.getScaledHeight() / 2) - (Shindo.getInstance().getFontHelper().size20.getFontHeight() / 2F),
				new Color(255, 255, 255).getRGB());

	}

	@Override
	public void keyTyped(char typedChar, int keyCode) {
		if (keyCode == Keyboard.KEY_SPACE) {
			this.setCurrentScene(this.getSceneByClass(FirstLoginScene.class));
		}
	}
}