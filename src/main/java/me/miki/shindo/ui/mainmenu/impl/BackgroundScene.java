package me.miki.shindo.ui.mainmenu.impl;

import me.miki.shindo.Shindo;
import me.miki.shindo.features.profile.mainmenu.BackgroundManager;
import me.miki.shindo.features.profile.mainmenu.impl.Background;
import me.miki.shindo.features.profile.mainmenu.impl.CustomBackground;
import me.miki.shindo.features.profile.mainmenu.impl.DefaultBackground;
import me.miki.shindo.helpers.MathHelper;
import me.miki.shindo.helpers.mouse.Scroll;
import me.miki.shindo.helpers.file.FileHelper;
import me.miki.shindo.features.file.FileManager;
import me.miki.shindo.helpers.logger.ShindoLogger;
import me.miki.shindo.helpers.Multithreading;
import me.miki.shindo.helpers.render.GLHelper;
import me.miki.shindo.helpers.render.Helper2D;
import me.miki.shindo.ui.mainmenu.MainMenuScene;
import me.miki.shindo.ui.mainmenu.ShindoMainMenu;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class BackgroundScene extends MainMenuScene {

    private Scroll scroll = new Scroll();

    public BackgroundScene(ShindoMainMenu parent) {
        super(parent);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        ScaledResolution sr = new ScaledResolution(mc);

        Shindo instance = Shindo.getInstance();

        draw(mouseX, mouseY, sr, instance);
    }

    private void draw(int mouseX, int mouseY, ScaledResolution sr, Shindo instance) {

        BackgroundManager backgroundManager = instance.getProfileManager().getBackgroundManager();

        int acWidth = 240;
        int acHeight = 148;
        int acX = sr.getScaledWidth() / 2 - (acWidth / 2);
        int acY = sr.getScaledHeight() / 2 - (acHeight / 2);
        int offsetX = 0;
        int offsetY = 0;
        int index = 1;
        int prevIndex = 1;

        scroll.onScroll();
        scroll.onAnimation();

        Helper2D.drawRoundedRectangle(acX, acY, acWidth, acHeight, 8, this.getBackgroundColor().getRGB(), 0);
        Helper2D.drawRectangle(acX, acY + 24, acWidth, 1, Color.WHITE.getRGB());
        Shindo.getInstance().getFontHelper().size20.drawString("Select Background", acX + ((float) acWidth / 2) - (Shindo.getInstance().getFontHelper().size20.getStringWidth("Select Background") / 2F), acY + 8, -1);

        GLHelper.startScissor(acX, acY + 25, acWidth, acHeight - 25);

        int scrollY = (int)scroll.getValue();
        for (Background bg : backgroundManager.getBackgrounds()) {

            if (bg instanceof DefaultBackground) {

                DefaultBackground defBackground = (DefaultBackground) bg;

                if (bg.getId() == 999) {
                    Helper2D.drawRoundedRectangle(acX + 11 + offsetX, acY + 35 + offsetY + scrollY, 102, 57, 6, Color.BLACK.getRGB(), 0);
                    Helper2D.drawPicture(acX + 10 + offsetX + 51 - 13, acY + 42 + offsetY + scrollY, 26, 26, 0, "icon/plus.png");
                } else {
                    Helper2D.drawPicture(acX + 11 + offsetX, acY + 35 + offsetY + scrollY, 102, 57, 0, defBackground.getImage());
                }
            }

            if (bg instanceof CustomBackground) {

                CustomBackground cusBackground = (CustomBackground) bg;

                boolean hovered = MathHelper.withinBox(acX + 11 + offsetX, acY + 35 + offsetY + scrollY, 102, 57, mouseX, mouseY);

                Helper2D.drawPicture(acX + 11 + offsetX, acY + 35 + offsetY + scrollY, 102, 57, 0, cusBackground.getImage());
                Helper2D.drawPicture(acX + offsetX + 100, acY + 38 + offsetY + scrollY, 10, 10, 0, "icon/cross.png");
            }
            if (bg.getId() == 999) {
                Helper2D.drawRoundedRectangle(acX + offsetX + 11, acY + offsetY + 76 + scrollY, 102, 16, 6, this.getBackgroundColor().getRGB(), 2);
            } else {
                Helper2D.drawRoundedRectangle(acX + offsetX + 11, acY + offsetY + 76 + scrollY, 102, 16, 0, this.getBackgroundColor().getRGB(), 0);
            }
            Shindo.getInstance().getFontHelper().size20.drawString(bg.getName(), acX + offsetX  + 11 + 51 - (Shindo.getInstance().getFontHelper().size20.getStringWidth(bg.getName()) / 2F), acY + offsetY + 80 + scrollY, -1);

            offsetX += 115;

            if (index % 2 == 0) {
                offsetY += 70;
                offsetX = 0;
                prevIndex++;
            }

            index++;
        }
        GLHelper.endScissor();


        scroll.setMaxScroll(prevIndex == 1 ? 0 : offsetY - (70 / 1.56F) - (index % 2 == 1 ? 70 : 0));
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {

        ScaledResolution sr = new ScaledResolution(mc);

        Shindo instance = Shindo.getInstance();
        FileManager fileManager = instance.getFileManager();
        BackgroundManager backgroundManager = instance.getProfileManager().getBackgroundManager();

        int acWidth = 240;
        int acHeight = 148;
        int acX = sr.getScaledWidth() / 2 - (acWidth / 2);
        int acY = sr.getScaledHeight() / 2 - (acHeight / 2);
        int offsetX = 0;
        int offsetY = (int) (0 + scroll.getValue());
        int index = 1;

        for (Background bg : backgroundManager.getBackgrounds()) {

            if (mouseButton == 0) {

                if (MathHelper.withinBox(acX + 11 + offsetX, acY + 35 + offsetY, 102, 57, mouseX, mouseY)) {

                    if (bg.getId() == 999) {

                        Multithreading.runAsync(() -> {
                            File file = FileHelper.selectImageFile();
                            File bgCacheDir = new File(fileManager.getCacheDir(), "background");

                            if (file != null && bgCacheDir.exists() && file.exists() && FileHelper.getExtension(file).equals("png")) {

                                File destFile = new File(bgCacheDir, file.getName());

                                try {
                                    FileHelper.copyFile(file, destFile);
                                    backgroundManager.addCustomBackground(destFile);
                                } catch (IOException e) {
									ShindoLogger.error("Error while copying file " + file.getName() + " to " + destFile.getName());
                                }
                            }
                        });
                    } else {

                        backgroundManager.setCurrentBackground(bg);
                    }
                }

                if (bg instanceof CustomBackground && MathHelper.withinBox(acX + offsetX + 98, acY + 35 + offsetY, 14, 14, mouseX, mouseY)) {

                    CustomBackground cusBackground = (CustomBackground) bg;

                    if (backgroundManager.getCurrentBackground().equals(cusBackground)) {
                        backgroundManager.setCurrentBackground(backgroundManager.getBackgroundById(0));
                    }

                    backgroundManager.removeCustomBackground(cusBackground);
                }
            }

            offsetX += 115;

            if (index % 2 == 0) {
                offsetY += 70;
                offsetX = 0;
            }

            index++;
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {

        if (keyCode == Keyboard.KEY_ESCAPE) {
            this.setCurrentScene(this.getSceneByClass(MainScene.class));
        }
    }
}
