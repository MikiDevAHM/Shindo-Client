package me.miki.shindo.ui.mainmenu.impl;

import me.miki.shindo.Shindo;
import me.miki.shindo.features.account.Account;
import me.miki.shindo.features.account.AccountManager;
import me.miki.shindo.features.account.AccountType;
import me.miki.shindo.features.account.SessionChanger;
import me.miki.shindo.features.notification.NotificationType;
import me.miki.shindo.helpers.MathHelper;
import me.miki.shindo.helpers.ResolutionHelper;
import me.miki.shindo.helpers.render.Helper2D;
import me.miki.shindo.ui.Style;
import me.miki.shindo.ui.comp.TextField;
import me.miki.shindo.ui.mainmenu.MainMenuScene;
import me.miki.shindo.ui.mainmenu.ShindoMainMenu;
import org.lwjgl.input.Keyboard;

public class MicrosoftLoginScene extends MainMenuScene {
	private final TextField emailTextField = new TextField();
	private final TextField passwordTextField = new TextField();
	public MicrosoftLoginScene(ShindoMainMenu parent) {
		super(parent);
	}


    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

		draw(mouseX, mouseY, partialTicks);

    }

    private void draw(int mouseX, int mouseY, float partialTicks) {

		Helper2D.drawRectangle(ResolutionHelper.getWidth() / 2 - 60, ResolutionHelper.getHeight() / 2 - 80, 120, 160, Style.getColorTheme(5).getRGB());
		Shindo.getInstance().getFontHelper().size30.drawStringWidthShadow("Login", ResolutionHelper.getWidth() / 2F - 20, ResolutionHelper.getHeight() / 2F- 70, -1);

		boolean hovered = MathHelper.withinBox(ResolutionHelper.getWidth() / 2 - 40, ResolutionHelper.getHeight() / 2 + 50, 80, 20, mouseX, mouseY);
		Helper2D.drawRoundedRectangle(ResolutionHelper.getWidth() / 2 - 40, ResolutionHelper.getHeight() / 2 +50, 80, 20, 2, Style.getColorTheme(hovered ? 9 : 7).getRGB(), 0);

		Shindo.getInstance().getFontHelper().size30.drawString("Done", ResolutionHelper.getWidth() / 2F - 20, ResolutionHelper.getHeight() / 2F + 52, -1);

		emailTextField.setPosition(ResolutionHelper.getWidth() / 2 - 40, ResolutionHelper.getHeight() / 2 - 40, 80, 14, "EMAIL");
		emailTextField.render(mouseX, mouseY);

		passwordTextField.setPosition(ResolutionHelper.getWidth() / 2 - 40, ResolutionHelper.getHeight() / 2 - 20, 80, 14, "PASSWORD");
		passwordTextField.setPasswordMode(true);
		passwordTextField.render(mouseX, mouseY);
    }

	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {

		emailTextField.onClick(mouseX, mouseY, mouseButton);
		passwordTextField.onClick(mouseX, mouseY, mouseButton);

		Shindo instance = Shindo.getInstance();
		AccountManager accountManager = instance.getAccountManager();

		if (MathHelper.withinBox(ResolutionHelper.getWidth() / 2 - 40, ResolutionHelper.getHeight() / 2 + 50, 80, 20, mouseX, mouseY)) {

			String email = emailTextField.getText();
			String password = passwordTextField.getText();
			Account account = new Account(null, email, password, AccountType.MICROSOFT);
			if (accountManager.isEmailAvailable(account.getEmail())) {
				SessionChanger.getInstance().setUserMicrosoft(email, password);
				account.setName(mc.getSession().getUsername());
				accountManager.addAccounts(account);
				accountManager.setCurrentAccount(account);
				instance.getNotificationManager().post("Sucesso", " A conta " + account.getName() + " foi adicionada com sucesso!", NotificationType.SUCCESS);
			} else {
				instance.getNotificationManager().post("Aviso", "A conta " + account.getName() + " ja existe!", NotificationType.WARNING);
			}
			accountManager.save();
		}


	}

    public void keyTyped(char typedChar, int keyCode) {

		emailTextField.keyTyped(typedChar, keyCode);
		passwordTextField.keyTyped(typedChar, keyCode);

		if(keyCode == Keyboard.KEY_ESCAPE) {
			backToAccountScene();
		}
    }
    
    private void backToAccountScene() {
		this.setCurrentScene(this.getSceneByClass(AccountScene.class));
    }
}
