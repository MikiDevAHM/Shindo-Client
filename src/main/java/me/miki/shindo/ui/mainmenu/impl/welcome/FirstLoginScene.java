package me.miki.shindo.ui.mainmenu.impl.welcome;

import me.miki.shindo.Shindo;
import me.miki.shindo.helpers.animation.Animate;
import me.miki.shindo.helpers.render.BlurHelper;
import me.miki.shindo.ui.mainmenu.ShindoMainMenu;
import me.miki.shindo.ui.mainmenu.impl.AccountScene;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;

public class FirstLoginScene extends AccountScene {
	
	private Animate fadeAnimation;
	
	public FirstLoginScene(ShindoMainMenu parent) {
		super(parent);
	}

	@Override
	public void initScene() {
		super.initScene();
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		
		ScaledResolution sr = new ScaledResolution(mc);
		Shindo instance = Shindo.getInstance();
		
		BlurHelper.drawBlurScreen(14);
		
		draw(mouseX, mouseY, partialTicks, sr, instance);
	}
	
	/*@Override
	public Runnable afterMicrosoftLogin() {
		
		AccountManager accountManager = Soar.getInstance().getAccountManager();
		
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				if(accountManager.getCurrentAccount() != null) {
					accountManager.save();
					fadeAnimation.setDirection(Direction.BACKWARDS);
				}
			}
		};
		
		return runnable;
	}*/
	
	@Override
	public void keyTyped(char typedChar, int keyCode) {

		if (keyCode == Keyboard.KEY_ESCAPE) {
			this.setCurrentScene(this.getSceneByClass(LastMessageScene.class));
		}

		textBox.keyTyped(typedChar, keyCode);
	}

}