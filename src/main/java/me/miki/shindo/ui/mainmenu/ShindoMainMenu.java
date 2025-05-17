/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package me.miki.shindo.ui.mainmenu;

import me.miki.shindo.Shindo;
import me.miki.shindo.events.impl.RenderNotificationEvent;
import me.miki.shindo.features.account.Account;
import me.miki.shindo.features.account.AccountManager;
import me.miki.shindo.features.account.AccountType;
import me.miki.shindo.features.account.SessionChanger;
import me.miki.shindo.features.notification.NotificationType;
import me.miki.shindo.features.profile.mainmenu.impl.Background;
import me.miki.shindo.features.profile.mainmenu.impl.CustomBackground;
import me.miki.shindo.features.profile.mainmenu.impl.DefaultBackground;
import me.miki.shindo.helpers.MathHelper;
import me.miki.shindo.helpers.animation.simple.SimpleAnimation;
import me.miki.shindo.helpers.font.GlyphPageFontRenderer;
import me.miki.shindo.helpers.logger.ShindoLogger;
import me.miki.shindo.helpers.network.HttpHelper;
import me.miki.shindo.helpers.render.Helper2D;
import me.miki.shindo.ui.mainmenu.impl.AccountScene;
import me.miki.shindo.ui.mainmenu.impl.BackgroundScene;
import me.miki.shindo.ui.mainmenu.impl.MainScene;
import me.miki.shindo.ui.mainmenu.impl.MicrosoftLoginScene;
import me.miki.shindo.ui.mainmenu.impl.welcome.*;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ShindoMainMenu extends GuiScreen {

    private MainMenuScene currentScene;
    private ArrayList<MainMenuScene> scenes = new ArrayList<MainMenuScene>();

    private SimpleAnimation accountAnimation = new SimpleAnimation();
    private Account removeAccount;
    private boolean openAccount;

    private SimpleAnimation closeFocusAnimation = new SimpleAnimation();
    private SimpleAnimation backgroundSelectFocusAnimation = new SimpleAnimation();

    public ShindoMainMenu() {
        Shindo instance = Shindo.getInstance();
        scenes.add(new MainScene(this));
        scenes.add(new AccountScene(this));
        scenes.add(new BackgroundScene(this));
        scenes.add(new WelcomeMessageScene(this));
        scenes.add(new LoginMessageScene(this));
        scenes.add(new FirstLoginScene(this));
        scenes.add(new LastMessageScene(this));
        scenes.add(new CheckingDataScene(this));
        scenes.add(new MicrosoftLoginScene(this));

        if(instance.getShindoAPI().isFirstLogin()) {
            currentScene = getSceneByClass(WelcomeMessageScene.class);
        } else {
            if(instance.getAccountManager().getCurrentAccount() == null) {
                currentScene = getSceneByClass(AccountScene.class);
            } else {
                currentScene = getSceneByClass(MainScene.class);
            }
        }
    }

    @Override
    public void initGui() {
        currentScene.initGui();
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

        ScaledResolution sr = new ScaledResolution(mc);
        Shindo instance = Shindo.getInstance();
        boolean isFirstLogin = instance.getShindoAPI().isFirstLogin();

        if(removeAccount != null) {
            instance.getAccountManager().getAccounts().remove(removeAccount);
            removeAccount = null;
        }

        draw(sr, instance);

        if(!isFirstLogin) {
            drawButtons(mouseX, mouseY, sr);
            drawAccount(mouseX, mouseY);
        }

        if(currentScene != null) {
            currentScene.drawScreen(mouseX, mouseY, partialTicks);
        }

        //drawLogo();
        drawCopyright();

        new RenderNotificationEvent().call();

        super.drawScreen(mouseX, mouseY, partialTicks);
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

            Helper2D.drawRoundedRectangle(6, 6, 20 + (maxUserWidth + 18), 20, 4, getBackgroundColor().getRGB(), 0);



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
                        Helper2D.drawRoundedRectangle(6, 26, 20 + (maxUserWidth + 18), 20 * size, 4, getBackgroundColor().getRGB(), 2);

                        // Aqui você pode iterar a lista de contas extras
                        for (int i = 0; i < size; i++) {
                            int y = 26 + (i * 20);
                            boolean hovered = MathHelper.withinBox(6, y, 20 + (maxUserWidth + 18), 20, mouseX, mouseY);

                            if (hovered) {
                                Helper2D.drawRoundedRectangle(6, y, 20 + (maxUserWidth + 18), 20, 4, getBackgroundColor().getRGB(), 0);
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

    private void draw(ScaledResolution sr, Shindo instance) {

        String copyright = "Copyright Mojang AB. Do not distribute!";
        Background currentBackground = instance.getProfileManager().getBackgroundManager().getCurrentBackground();

        if(currentBackground instanceof DefaultBackground) {

            DefaultBackground bg = (DefaultBackground) currentBackground;

            Helper2D.drawPicture( 0, 0, sr.getScaledWidth(), sr.getScaledHeight(),0,  bg.getImage());
        }else if(currentBackground instanceof CustomBackground) {

            CustomBackground bg = (CustomBackground) currentBackground;

            Helper2D.drawPicture( 0, 0, sr.getScaledWidth(), sr.getScaledHeight(),0,  bg.getImage());
        }
    }

    private void drawButtons(int mouseX, int mouseY, ScaledResolution sr) {


        closeFocusAnimation.setAnimation(MathHelper.withinBox( sr.getScaledWidth() - 28, 6, 22, 22, mouseX, mouseY) ? 1.0F : 0.0F, 16);

        Helper2D.drawRoundedRectangle(sr.getScaledWidth() - 28, 6, 22, 22, 4, this.getBackgroundColor().getRGB(), 0);
        Helper2D.drawPicture(sr.getScaledWidth() - 26, 8, 18, 18, new Color(255 - (int) (closeFocusAnimation.getValue() * 200), 255, 255 - (int) (closeFocusAnimation.getValue() * 200)).getRGB(), "icon/cross.png");

        backgroundSelectFocusAnimation.setAnimation(MathHelper.withinBox( sr.getScaledWidth() - 28 - 28, 6, 22, 22, mouseX, mouseY) ? 1.0F : 0.0F, 16);

        Helper2D.drawRoundedRectangle(sr.getScaledWidth() - 28 - 28, 6, 22, 22, 4, this.getBackgroundColor().getRGB(), 0);
        Helper2D.drawPicture( sr.getScaledWidth() - 28 - 26, 8, 18, 18, new Color(255 - (int) (backgroundSelectFocusAnimation.getValue() * 200), 255, 255 - (int) (backgroundSelectFocusAnimation.getValue() * 200)).getRGB(), "icon/image.png");

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
        Account currentAccount = accountManager.getCurrentAccount();
        boolean isFirstLogin = instance.getShindoAPI().isFirstLogin();
        ScaledResolution sr = new ScaledResolution(mc);
        int maxUserWidth = Shindo.getInstance().getFontHelper().size20.getStringWidth(currentAccount.getName());
        if (MathHelper.withinBox(maxUserWidth + 31, 11, 10, 10, mouseX, mouseY)) {
            currentScene = getSceneByClass(AccountScene.class);
        }

        if(mouseButton == 0 && !isFirstLogin) {

            if (MathHelper.withinBox( sr.getScaledWidth() - 28, 6, 22, 22,mouseX, mouseY)) {
                mc.shutdown();
            }

            if (MathHelper.withinBox(sr.getScaledWidth() - 28 - 28, 6, 22, 22, mouseX, mouseY)) {
                this.setCurrentScene(this.getSceneByClass(BackgroundScene.class));
            }


            if (openAccount) {

                int offsetY = 20;

                for (Account acc : accountManager.getAccounts()) {

                    int tWidth = Shindo.getInstance().getFontHelper().size20.getStringWidth(acc.getName());

                    if (tWidth > maxUserWidth) {
                        maxUserWidth = tWidth;
                    }
                }



                for (Account acc : accountManager.getAccounts()) {

                    if (!acc.equals(currentAccount)) {

                        if (MathHelper.withinBox(maxUserWidth + 28, 8 + offsetY, 15, 15, mouseX, mouseY)) {
                            instance.getNotificationManager().post("Conta", "A conta " + mc.getSession().getUsername() + " foi removida!", NotificationType.INFO);
                            removeAccount = acc;

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
        }

        currentScene.mouseClicked(mouseX, mouseY, mouseButton);

        try {
            super.mouseClicked(mouseX, mouseY, mouseButton);
        } catch (IOException e) {
            ShindoLogger.error("Error: " + e.getMessage());
        }
    }



    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        currentScene.mouseReleased(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        currentScene.keyTyped(typedChar, keyCode);
    }

    @Override
    public void handleInput() throws IOException {

        if(currentScene instanceof MicrosoftLoginScene) {
            currentScene.handleInput();
        } else {
            super.handleInput();
        }
    }

    @Override
    public void onGuiClosed() {
        currentScene.onGuiClosed();
    }

    public MainMenuScene getCurrentScene() {
        return currentScene;
    }

    public void setCurrentScene(MainMenuScene currentScene) {

        if(this.currentScene != null) {
            this.currentScene.onSceneClosed();
        }

        this.currentScene = currentScene;

        if(this.currentScene != null) {
            this.currentScene.initScene();
        }
    }


    public MainMenuScene getSceneByClass(Class<? extends MainMenuScene > clazz) {

        for(MainMenuScene s : scenes) {
            if(s.getClass().equals(clazz)) {
                return s;
            }
        }

        return null;
    }

    public Color getBackgroundColor() {
        return new Color(23, 23, 26, 220);
    }
}