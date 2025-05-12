/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package me.miki.shindo.ui.titlescreen;

import me.miki.shindo.Shindo;
import me.miki.shindo.events.impl.RenderNotificationEvent;
import me.miki.shindo.features.account.Account;
import me.miki.shindo.features.account.AccountManager;
import me.miki.shindo.features.account.AccountType;
import me.miki.shindo.features.account.SessionChanger;
import me.miki.shindo.features.notification.NotificationType;
import me.miki.shindo.helpers.MathHelper;
import me.miki.shindo.helpers.ResolutionHelper;
import me.miki.shindo.helpers.font.GlyphPageFontRenderer;
import me.miki.shindo.helpers.network.HttpHelper;
import me.miki.shindo.helpers.render.Helper2D;
import me.miki.shindo.ui.Style;
import me.miki.shindo.ui.comp.TextField;
import me.miki.shindo.ui.comp.buttons.IconButton;
import me.miki.shindo.ui.comp.buttons.TextButton;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiSelectWorld;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ShindoMainMenu extends Panorama {

    private final ArrayList<TextButton> textButtons = new ArrayList<>();
    private final ArrayList<IconButton> iconButtons = new ArrayList<>();


    private TextField nameTextField;
    private TextField emailTextField;
    private TextField passwordTextField;
    private boolean offline;
    private boolean openAccount;
    private boolean accountFrame;

    public ShindoMainMenu() {
        textButtons.add(new TextButton("Singleplayer", width / 8 - 65, height / 2));
        textButtons.add(new TextButton("Multiplayer", width / 8 - 65, height / 2 + 25));
        textButtons.add(new TextButton("Settings", width / 8 - 65, height / 2 + 50));
        iconButtons.add(new IconButton("cross.png", width - 25, 5));
    }


    @Override
    public void initGui() {

        if (Shindo.getInstance().getShindoAPI().isFirstLogin() || Shindo.getInstance().getAccountManager().getCurrentAccount() == null || Shindo.getInstance().getAccountManager().getAccounts().isEmpty()) {
            accountFrame = true;
            Account account = new Account("ShindoUser", null, null, AccountType.OFFLINE);
            SessionChanger.getInstance().setUserOffline(account.getName());
            Shindo.getInstance().getAccountManager().setCurrentAccount(account);
            Shindo.getInstance().getAccountManager().addAccounts(account);
            Shindo.getInstance().getAccountManager().save();
        }

        nameTextField = new TextField(ResolutionHelper.getWidth() - 120, ResolutionHelper.getHeight() - 130, 80, 14, "NAME");
        emailTextField = new TextField(ResolutionHelper.getWidth() - 120, ResolutionHelper.getHeight() - 130, 80, 14, "EMAIL");
        passwordTextField = new TextField(ResolutionHelper.getWidth() - 120, ResolutionHelper.getHeight() - 110, 80, 14, "PASSWORD");
        passwordTextField.setPasswordMode(true);
    }

    /**
     * Renders button text and logos on the screen
     *
     * @param mouseX       The current X position of the mouse
     * @param mouseY       The current Y position of the mouse
     * @param partialTicks The partial ticks used for rendering
     */

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        super.drawScreen(mouseX, mouseY, partialTicks);

        Helper2D.drawRectangle(0, 0, width / 4, height, 0x70000000);

        int y = 0;
        for (TextButton textButton : textButtons) {
            textButton.renderButton(width / 8 - 65, height / 2 + y * 25, mouseX, mouseY);
            y++;
        }

        for (IconButton iconButton : iconButtons) {
            if (iconButton.getIcon().equals("cross.png")) {
                iconButton.renderButton(width - 25, 5, mouseX, mouseY);
            }
        }

        drawAccount(mouseX, mouseY);
        drawAccountFrame(mouseX, mouseY);
        drawLogo();
        drawCopyright();

        new RenderNotificationEvent().call();
    }

    /**
     * Is called when any mouse button is pressed. Adds functionality to every button on screen
     *
     * @param mouseX      The current X position of the mouse
     * @param mouseY      The current Y position of the mouse
     * @param mouseButton The current mouse button which is pressed
     */

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {

        Shindo instance = Shindo.getInstance();
        AccountManager accountManager = instance.getAccountManager();
        for (TextButton textButton : textButtons) {
            if (textButton.isHovered(mouseX, mouseY)) {
                switch (textButton.getText()) {
                    case "Singleplayer":
                        mc.displayGuiScreen(new GuiSelectWorld(this));
                        break;
                    case "Multiplayer":
                        mc.displayGuiScreen(new GuiMultiplayer(this));
                        break;
                    case "Settings":
                        mc.displayGuiScreen(new GuiOptions(this, mc.gameSettings));
                        break;
                }
            }
        }

        for (IconButton iconButton : iconButtons) {
            if (iconButton.isHovered(mouseX, mouseY)) {
                if (iconButton.getIcon().equals("cross.png")) {
                    mc.shutdown();
                }
            }
        }



        Account currentAccount = accountManager.getCurrentAccount();
        int maxUserWidth = Shindo.getInstance().getFontHelper().size20.getStringWidth(currentAccount.getName());
        int offsetY = 20;

        for (Account acc : accountManager.getAccounts()) {

            int tWidth = Shindo.getInstance().getFontHelper().size20.getStringWidth(acc.getName());

            if (tWidth > maxUserWidth) {
                maxUserWidth = tWidth;
            }
        }


        if (MathHelper.withinBox(maxUserWidth + 31, 11, 10, 10, mouseX, mouseY)) {
            accountFrame = !accountFrame;
        }

        if (openAccount) {
            for (Account acc : accountManager.getAccounts()) {

                if (!acc.equals(currentAccount)) {

                    if (MathHelper.withinBox(maxUserWidth + 28, 8 + offsetY, 15, 15, mouseX, mouseY)) {
                        instance.getNotificationManager().post("Conta", "A conta " + mc.getSession().getUsername() + " foi removida!", NotificationType.INFO);
                        Shindo.getInstance().getAccountManager().getAccounts().remove(acc);
                        Shindo.getInstance().getAccountManager().save();

                    }

                    if (MathHelper.withinBox(6, 6 + offsetY, maxUserWidth + 20, 20, mouseX, mouseY)) {
                        if (acc.getType().equals(AccountType.MICROSOFT)) {
                            SessionChanger.getInstance().setUserMicrosoft(acc.getEmail(), acc.getPassword());
                            acc.setName(mc.getSession().getUsername());
                        } else {
                            SessionChanger.getInstance().setUserOffline(acc.getName());
                        }
                        instance.getNotificationManager().post("Conta", "A conta atual foi alterada para " + mc.getSession().getUsername() + " !", NotificationType.INFO);
                        Shindo.getInstance().getAccountManager().setCurrentAccount(acc);
                        Shindo.getInstance().getAccountManager().save();
                    }

                    offsetY += 20;
                }
            }
        }


        if (accountFrame) {


            if (offline) {
                nameTextField.onClick(mouseX, mouseY, mouseButton);
            } else {
                emailTextField.onClick(mouseX, mouseY, mouseButton);
                passwordTextField.onClick(mouseX, mouseY, mouseButton);
            }

            if (MathHelper.withinBox(ResolutionHelper.getWidth() - 100, ResolutionHelper.getHeight() - 150, 40, 14, mouseX, mouseY)) {
                offline = !offline;
            }

            if (MathHelper.withinBox(ResolutionHelper.getWidth() - 120, ResolutionHelper.getHeight() - 60, 80, 20, mouseX, mouseY)) {

                String name = nameTextField.getText();

                if (offline) {
                    Account account = new Account(name, null, null, AccountType.OFFLINE);
                    if (accountManager.isAccountNameAvailable(account.getName())) {
                        SessionChanger.getInstance().setUserOffline(name);
                        accountManager.addAccounts(account);
                        accountManager.setCurrentAccount(account);
                        instance.getNotificationManager().post("Sucesso", " A conta " + account.getName() + " foi adicionada com sucesso!", NotificationType.SUCCESS);
                    } else {
                        instance.getNotificationManager().post("Aviso", "A conta " + account.getName() + " ja existe!", NotificationType.WARNING);
                    }
                } else {

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
                }
                accountManager.save();

                if (Shindo.getInstance().getShindoAPI().isFirstLogin()) {
                    Shindo.getInstance().getShindoAPI().createFirstLoginFile();
                }

                accountFrame = false;
            }
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {

        if (offline) {
            nameTextField.keyTyped(typedChar, keyCode);
        } else {
            emailTextField.keyTyped(typedChar, keyCode);
            passwordTextField.keyTyped(typedChar, keyCode);
        }
    }

    private void drawAccount(int mouseX, int mouseY) {
        Shindo instance = Shindo.getInstance();
        AccountManager accountManager = instance.getAccountManager();
        Account currentAccount = accountManager.getCurrentAccount();

        File headDir = new File(instance.getFileManager().getCacheDir(), "head");
        int offsetY = 20;
        if (currentAccount != null) {

            String username = currentAccount.getName();
            File headFile = new File(headDir, username + ".png");
            int maxUserWidth = instance.getFontHelper().size20.getStringWidth(username);
            int size = accountManager.getAccounts().size() - 1;

            for(Account acc : accountManager.getAccounts()) {

                int tWidth = instance.getFontHelper().size20.getStringWidth(acc.getName());

                if(tWidth > maxUserWidth) {
                    maxUserWidth = tWidth;
                }
            }

            // Verifica e baixa a head se necessário
            if (!headFile.exists() && currentAccount.getType() == AccountType.MICROSOFT) {
                headDir.mkdirs(); // Garante que o diretório exista
                HttpHelper.downloadFile("https://minotar.net/helm/" + username + "/16.png", headFile);
            }



            Helper2D.drawRoundedRectangle(6, 6, 20 + (maxUserWidth + 18), 20, 4, Style.getColorTheme(5).getRGB(), 0);



            if (!headFile.exists()) {
                Helper2D.drawPicture(9, 9, 14, 14, 0, "icon/steve.png");
            } else {
                Helper2D.drawPicture(9, 9, 14, 14, 0, headFile);
            }

            instance.getFontHelper().size20.drawString(username, 26, 13, -1);
            Helper2D.drawPicture(maxUserWidth + 31, 11, 10, 10, 0, "icon/plus.png");


            for (Account acc : accountManager.getAccounts()) {

                if (!acc.equals(currentAccount)) {
                    String accName = acc.getName();
                    File accHeadFile = new File(headDir, accName + ".png");

                    // Verifica e baixa head se necessário
                    if (!accHeadFile.exists() && acc.getType() == AccountType.MICROSOFT) {
                        HttpHelper.downloadFile("https://minotar.net/helm/" + accName + "/16.png", accHeadFile);
                    }

                    boolean hoveredMain = MathHelper.withinBox(6, 6, 20 + (maxUserWidth + 18), 20, mouseX, mouseY);
                    boolean hoveredDropdown = MathHelper.withinBox(6, 26, 20 + (maxUserWidth + 18), 20 * size, mouseX, mouseY);

                    if (hoveredMain) {
                        openAccount = true;
                    }

                    // Fecha o dropdown se não estiver em nenhuma área
                    if (!hoveredMain && !hoveredDropdown) {
                        openAccount = false;
                    }

                    if (openAccount) {
                        // Renderiza a dropdown das contas extras
                        Helper2D.drawRoundedRectangle(6, 26, 20 + (maxUserWidth + 18), 20 * size, 4, Style.getColorTheme(5).getRGB(), 2);

                        // Aqui você pode iterar a lista de contas extras
                        for (int i = 0; i < size; i++) {
                            int y = 26 + (i * 20);
                            boolean hovered = MathHelper.withinBox(6, y, 20 + (maxUserWidth + 18), 20, mouseX, mouseY);

                            if (hovered) {
                                Helper2D.drawRoundedRectangle(6, y, 20 + (maxUserWidth + 18), 20, 4, Style.getColorTheme(7).getRGB(), 0);
                            }


                            if (!accHeadFile.exists()) {
                                Helper2D.drawPicture(9, y + 3, 14, 14, 0, "icon/steve.png");
                            } else {
                                Helper2D.drawPicture(9, y + 3, 14, 14, 0, accHeadFile);
                            }

                            instance.getFontHelper().size20.drawString(accName, 26, y + 7, -1);
                            Helper2D.drawPicture(maxUserWidth + 30, y + 5, 10, 10, 0, "icon/cross.png");
                        }
                    }

                    offsetY += 20;
                }
            }
        }
    }

    private void drawAccountFrame(int mouseX, int mouseY) {
        if (accountFrame) {
            Helper2D.drawRectangle(ResolutionHelper.getWidth() - 140, ResolutionHelper.getHeight() - 190, 120, 165, Style.getColorTheme(5).getRGB());
            Shindo.getInstance().getFontHelper().size30.drawStringWidthShadow("Login", ResolutionHelper.getWidth() - 106, ResolutionHelper.getHeight() - 180, -1);

            boolean hovered = MathHelper.withinBox(ResolutionHelper.getWidth() - 120, ResolutionHelper.getHeight() - 60, 80, 20, mouseX, mouseY);
            Helper2D.drawRoundedRectangle(ResolutionHelper.getWidth() - 120, ResolutionHelper.getHeight() - 60, 80, 20, 2, Style.getColorTheme(hovered ? 9 : 7).getRGB(), 0);

            Shindo.getInstance().getFontHelper().size30.drawString("Done", ResolutionHelper.getWidth() - 100, ResolutionHelper.getHeight() - 57, -1);

            boolean hovered1 = MathHelper.withinBox(ResolutionHelper.getWidth() - 100, ResolutionHelper.getHeight() - 150, 40, 14, mouseX, mouseY);
            Helper2D.drawRoundedRectangle(ResolutionHelper.getWidth() - 100, ResolutionHelper.getHeight() - 150, 40, 14, 2, offline ? new Color(hovered1 ? 60 : 0, 255, hovered1 ? 60 : 0).getRGB() : new Color(255, hovered1 ? 60 : 0, hovered1 ? 60 : 0).getRGB(), 0);
            Shindo.getInstance().getFontHelper().size20.drawString("Offline", ResolutionHelper.getWidth() - 95, ResolutionHelper.getHeight() - 145, -1);


            if (offline) {
                nameTextField.render(mouseX, mouseY);
            } else {
                emailTextField.render(mouseX, mouseY);
                passwordTextField.render(mouseX, mouseY);
            }

        }
    }

    /**
     * Draws the main "Shindo" Text and the Logo in the middle
     */

    private void drawLogo() {
        GlyphPageFontRenderer fontRenderer = Shindo.getInstance().getFontHelper().size40;
        fontRenderer.drawString(Shindo.NAME, width / 8f - fontRenderer.getStringWidth(Shindo.NAME) / 2f, height / 2f - 27.5f, -1);
        //Helper2D.drawPicture(width / 2 - 30, height / 2 - 78, 60, 60, 0x40ffffff, "logo.png");
    }

    /**
     * Draws the "Shindo Client" Text and Mojang Copyright Notice on the bottom
     */

    private void drawCopyright() {
        GlyphPageFontRenderer fontRenderer = Shindo.getInstance().getFontHelper().size20;
        String copyright = "Copyright Mojang Studios. Do not distribute!";
        String text = Shindo.NAME + " Client " + Shindo.VERSION + " | by " + Shindo.AUTHOR;
        fontRenderer.drawString(copyright, width - fontRenderer.getStringWidth(copyright) - 2, height - fontRenderer.getFontHeight(), 0x50ffffff);
        fontRenderer.drawString(text, 4, height - fontRenderer.getFontHeight(), 0x50ffffff);
    }
}