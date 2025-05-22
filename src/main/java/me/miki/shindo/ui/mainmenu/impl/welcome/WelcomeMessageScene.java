package me.miki.shindo.ui.mainmenu.impl.welcome;

import me.miki.shindo.Shindo;
import me.miki.shindo.helpers.TimerHelper;
import me.miki.shindo.helpers.render.BlurHelper;
import me.miki.shindo.ui.mainmenu.MainMenuScene;
import me.miki.shindo.ui.mainmenu.ShindoMainMenu;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;

import java.awt.*;

public class WelcomeMessageScene extends MainMenuScene {

	private int step;
	private String message;
	
	private TimerHelper timer = new TimerHelper();
	
	public WelcomeMessageScene(ShindoMainMenu parent) {
		super(parent);
		
		step = 0;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		
		ScaledResolution sr = new ScaledResolution(mc);
		String welcomeMessage = "Welcome to Shindo Client";
		String setupMessage = "Let's start setting up the Shindo Client!";
		
		BlurHelper.drawBlurScreen(14);

		if (timer.delay(1200)&& message == null) {
			message = welcomeMessage;
		}

		if (timer.delay(1200)&& message.equals(welcomeMessage)) {
			message = setupMessage;
		}

		if (timer.delay(1200)&& message.equals(setupMessage)) {
			message = "Press SPACE to continue";
		}


		Shindo.getInstance().getFontHelper().size20.drawString(message, (float) sr.getScaledWidth() / 2,
				((float) sr.getScaledHeight() / 2) - ((float) Shindo.getInstance().getFontHelper().size20.getFontHeight() / 2),
				new Color(255, 255, 255).getRGB());
	}

	@Override
	public void keyTyped(char typedChar, int keyCode) {
		if (keyCode == Keyboard.KEY_SPACE) {
			this.setCurrentScene(this.getSceneByClass(LoginMessageScene.class));
		}
	}
}
