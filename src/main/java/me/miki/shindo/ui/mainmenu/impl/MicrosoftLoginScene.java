package me.miki.shindo.ui.mainmenu.impl;

import me.miki.shindo.Shindo;
import me.miki.shindo.features.account.Account;
import me.miki.shindo.features.account.AccountManager;
import me.miki.shindo.features.account.AccountType;
import me.miki.shindo.features.account.SessionChanger;
import me.miki.shindo.features.notification.NotificationType;
import me.miki.shindo.helpers.MathHelper;
import me.miki.shindo.helpers.render.Helper2D;
import me.miki.shindo.ui.Style;
import me.miki.shindo.ui.mainmenu.MainMenuScene;
import me.miki.shindo.ui.mainmenu.ShindoMainMenu;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;

public class MicrosoftLoginScene extends MainMenuScene {

	private CompTextField emailTextField = new CompTextField();

	private CompPasswordField passwordTextField = new CompPasswordField();

	public MicrosoftLoginScene(ShindoMainMenu parent) {
		super(parent);
	}

	@Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

		ScaledResolution sr = new ScaledResolution(mc);
		Shindo instance = Shindo.getInstance();

		draw(mouseX, mouseY, partialTicks, sr, instance);

    }

    private void draw(int mouseX, int mouseY, float partialTicks, ScaledResolution sr, Shindo instance) {

		emailTextField.update();
		passwordTextField.update();

		Helper2D.drawRectangle(sr.getScaledWidth() / 2 - 60, sr.getScaledHeight() / 2 - 80, 120, 160, Style.getColorTheme(5).getRGB());
		Shindo.getInstance().getFontHelper().size30.drawStringWidthShadow("Login", sr.getScaledWidth() / 2F - 20, sr.getScaledHeight() / 2F- 70, -1);

		boolean hovered = MathHelper.withinBox(sr.getScaledWidth() / 2 - 40, sr.getScaledHeight() / 2 + 50, 80, 20, mouseX, mouseY);
		Helper2D.drawRoundedRectangle(sr.getScaledWidth()/ 2 - 40, sr.getScaledHeight() / 2 +50, 80, 20, 2, Style.getColorTheme(hovered ? 9 : 7).getRGB(), 0);

		Shindo.getInstance().getFontHelper().size30.drawString("Done", sr.getScaledWidth() / 2F - 20, sr.getScaledHeight() / 2F + 52, -1);

		emailTextField.setPosition(sr.getScaledWidth() / 2 - 40, sr.getScaledHeight() / 2 - 40, 80, 14, "EMAIL");
		emailTextField.render(mouseX, mouseY);

		passwordTextField.setPosition(sr.getScaledWidth() / 2 - 40, sr.getScaledHeight() / 2 - 20, 80, 14, "PASSWORD");
		passwordTextField.render(mouseX, mouseY);
    }

	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {

		ScaledResolution sr = new ScaledResolution(mc);
		Shindo instance = Shindo.getInstance();
		AccountManager accountManager = instance.getAccountManager();

		if (MathHelper.withinBox(sr.getScaledWidth() / 2 - 40, sr.getScaledHeight() / 2 + 50, 80, 20, mouseX, mouseY)) {

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

		emailTextField.onClick(mouseX, mouseY, mouseButton);
		passwordTextField.onClick(mouseX, mouseY, mouseButton);
	}

	@Override
    public void keyTyped(char typedChar, int keyCode) {

		if(keyCode == Keyboard.KEY_ESCAPE) {
			backToAccountScene();
		}

		emailTextField.keyTyped(typedChar, keyCode);
		passwordTextField.keyTyped(typedChar, keyCode);
    }
    
    private void backToAccountScene() {
		this.setCurrentScene(this.getSceneByClass(AccountScene.class));
    }
}
