package me.miki.shindo.ui.mainmenu.impl;

import me.miki.shindo.Shindo;
import me.miki.shindo.features.account.Account;
import me.miki.shindo.features.account.AccountManager;
import me.miki.shindo.features.account.AccountType;
import me.miki.shindo.helpers.ImageHelper;
import me.miki.shindo.helpers.MathHelper;
import me.miki.shindo.helpers.ResolutionHelper;
import me.miki.shindo.helpers.file.FileHelper;
import me.miki.shindo.features.file.FileManager;
import me.miki.shindo.helpers.logger.ShindoLogger;
import me.miki.shindo.helpers.Multithreading;
import me.miki.shindo.helpers.render.Helper2D;
import me.miki.shindo.ui.mainmenu.MainMenuScene;
import me.miki.shindo.ui.mainmenu.ShindoMainMenu;
import me.miki.shindo.ui.mainmenu.impl.welcome.LastMessageScene;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Session;
import org.lwjgl.input.Keyboard;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class AccountScene extends MainMenuScene {

	public CompTextField textBox = new CompTextField() ;
	
	private File offlineSkin;
	
	public AccountScene(ShindoMainMenu parent) {
		super(parent);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		
		ScaledResolution sr = new ScaledResolution(mc);
		Shindo instance = Shindo.getInstance();
		
		draw(mouseX, mouseY, partialTicks, sr, instance);
	}
	
	public void draw(int mouseX, int mouseY, float partialTicks, ScaledResolution sr, Shindo instance) {
		
		int acWidth = 220;
		int acHeight = 138;
		int acX = sr.getScaledWidth() / 2 - (acWidth / 2);
		int acY = sr.getScaledHeight() / 2 - (acHeight / 2);

		textBox.update();
		String loginMessage = "Login";
		String microsoftLogin = "Microsoft Login";
		String offlineLogin = "Offline Login";
		
		Helper2D.drawRoundedRectangle(acX, acY, acWidth, acHeight, 8, this.getBackgroundColor().getRGB(), 0);
		Shindo.getInstance().getFontHelper().size20.drawString(loginMessage, acX + ((float) acWidth / 2) - Shindo.getInstance().getFontHelper().size20.getStringWidth(loginMessage) / 2f, acY + 9, -1);

		Helper2D.drawPicture(acX + 10, acY + 29, 200, 30, 0, new ResourceLocation("shindo/mainmenu/microsoft-background.png"));

		Shindo.getInstance().getFontHelper().size20.drawString(microsoftLogin, acX + 45, acY + 40, -1);

		Helper2D.drawRoundedRectangle(acX + 18, acY + 34, 9, 9, 1, new Color(247, 78, 30).getRGB(), 0);
		Helper2D.drawRoundedRectangle(acX + 18 + 11, acY + 34, 9, 9, 1, new Color(127, 186, 0).getRGB(), 0);
		Helper2D.drawRoundedRectangle(acX + 18, acY + 34 + 11, 9, 9, 1, new Color(0, 164, 239).getRGB(), 0);
		Helper2D.drawRoundedRectangle(acX + 18 + 11, acY + 34 + 11, 9, 9, 1, new Color(255, 185, 0).getRGB(), 0);

		Shindo.getInstance().getFontHelper().size20.drawString(offlineLogin, acX + ((float) acWidth / 2) - Shindo.getInstance().getFontHelper().size20.getStringWidth(offlineLogin) / 2F, acY + 67, -1);

		Helper2D.drawRoundedRectangle(acX + acWidth - 30, acY + 86, 20, 20, 4, this.getBackgroundColor().getRGB(), 0);
		Helper2D.drawPicture( acX + acWidth - 25, acY + 91, 10, 10, 0, "icon/user.png");
		textBox.setPosition(ResolutionHelper.getWidth() / 2 - (220 / 2) + 10, (ResolutionHelper.getHeight() / 2 - (138 / 2)) + 86, 175, 20, "NAME");
		textBox.render(mouseX, mouseY);

		Helper2D.drawRoundedRectangle(acX  + ( acWidth / 2) - (60 / 2)  - 2  , acY + 86 + 25, 60, 20, 4, this.getBackgroundColor().getRGB(), 0);
		Shindo.getInstance().getFontHelper().size20.drawString(loginMessage, acX + ( acWidth / 2F) - Shindo.getInstance().getFontHelper().size20.getStringWidth(loginMessage) / 2F, acY + 86 + 31, -1);
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		
		ScaledResolution sr = new ScaledResolution(mc);
		
		Shindo instance = Shindo.getInstance();
		AccountManager accountManager = instance.getAccountManager();
		FileManager fileManager = instance.getFileManager();
		
		int acWidth = 220;
		int acHeight = 140;
		int acX = sr.getScaledWidth() / 2 - (acWidth / 2);
		int acY = sr.getScaledHeight() / 2 - (acHeight / 2);
		
		if(mouseButton == 0) {
			
			if(MathHelper.withinBox( acX + acWidth - 30, acY + 86, 20, 20, mouseX, mouseY)) {
				Multithreading.runAsync(() -> {
					
					File selectSkin = FileHelper.selectImageFile();
					
					if(selectSkin != null) {
						
						try {
							
							File copyFile = new File(fileManager.getCacheDir(), "skin/" + selectSkin.getName());
							BufferedImage image = ImageIO.read(selectSkin);
							
							if(image.getWidth() == 64 && image.getHeight() == 64) {
								FileHelper.copyFile(selectSkin, copyFile);
								offlineSkin = copyFile;
							}
							
						} catch(Exception e) {
							ShindoLogger.error("Error copying skin: " + selectSkin.getName());
						}
					}
				});
			}
			
			if(MathHelper.withinBox( acX  + ( acWidth / 2) - (60 / 2)  - 2  , acY + 86 + 25, 60, 20, mouseX, mouseY)) {
				
				File renameFile = new File(fileManager.getCacheDir(), "skin/" + textBox.getText() + ".png");
				File headDir = new File(fileManager.getCacheDir(), "head");
				
				Account acc = new Account(textBox.getText(), "0", "0", AccountType.OFFLINE);
				
				if(offlineSkin != null) {
					offlineSkin.renameTo(renameFile);
					offlineSkin = renameFile;
				}
				
				if(offlineSkin != null && offlineSkin.exists()) {
					
					acc.setSkinFile(offlineSkin);
					
					try {
						
						BufferedImage rawImage = ImageIO.read(offlineSkin);
						BufferedImage headImage = ImageHelper.scissor(rawImage, 8, 8, 8, 8);
						BufferedImage layerImage = ImageHelper.scissor(rawImage, 40, 8, 8, 8);
						BufferedImage conbineImage = ImageHelper.combine(headImage, layerImage);
						
						ImageIO.write(ImageHelper.resize(conbineImage, 128, 128), "png", new File(headDir, acc.getName() + ".png"));
					} catch(Exception e) {
						ShindoLogger.error("Error copying head: " + offlineSkin.getName());
					}
				}
				
		        mc.setSession(new Session(acc.getName(), "0", "0", "mojang"));
		        
				if(accountManager.getAccountByName(acc.getName()) == null) {
					accountManager.getAccounts().add(acc);
				}
				
		        accountManager.setCurrentAccount(acc);
		        accountManager.save();
		        
		        offlineSkin = null;
		        
		        getAfterLoginRunnable().run();
			}
			
			if(MathHelper.withinBox( acX + 10, acY + 29, 200, 30, mouseX, mouseY)) {
				this.setCurrentScene(this.getSceneByClass(MicrosoftLoginScene.class));
			}
		}
		
		textBox.onClick(mouseX, mouseY, mouseButton);
	}

	@Override
	public void keyTyped(char typedChar, int keyCode) {

		if(keyCode == Keyboard.KEY_ESCAPE) {
			this.setCurrentScene(this.getSceneByClass(MainScene.class));
		}

		textBox.keyTyped(typedChar, keyCode);
	}


	private Runnable getAfterLoginRunnable() {
		
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
		        if(Shindo.getInstance().getShindoAPI().isFirstLogin()) {
					setCurrentScene(getSceneByClass(LastMessageScene.class));
		        } else {
					setCurrentScene(getSceneByClass(MainScene.class));
		        }
			}
		};
		
		return runnable;
	}
}
