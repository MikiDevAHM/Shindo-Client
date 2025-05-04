/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package me.miki.shindo.ui.modmenu.impl;

import me.miki.shindo.Shindo;
import me.miki.shindo.helpers.MathHelper;
import me.miki.shindo.helpers.ResolutionHelper;
import me.miki.shindo.helpers.TimeHelper;
import me.miki.shindo.helpers.animation.Animate;
import me.miki.shindo.helpers.animation.Easing;
import me.miki.shindo.helpers.render.Helper2D;
import me.miki.shindo.ui.Style;
import me.miki.shindo.ui.modmenu.category.Category;
import me.miki.shindo.ui.modmenu.category.impl.ChatCategory;
import me.miki.shindo.ui.modmenu.category.impl.ModCategory;
import me.miki.shindo.ui.modmenu.category.impl.OptionCategory;
import me.miki.shindo.ui.modmenu.category.impl.PatcherCategory;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.Date;

public class Panel {

    private static final Minecraft mc = Minecraft.getMinecraft();

    private final String[] sideButtons = {"Mods", "Settings", "Chat", "Patcher", "Performance", "Graphics", "Profiles"};
    private final Animate animateSideBar = new Animate();
    private final Animate animateTransition = new Animate();

    private int x, y, w, h;
    private int offsetX, offsetY;
    private boolean dragging;
    private boolean anyButtonOpen;

    //
    private ArrayList<Category> categories = new ArrayList<Category>();
    private Category currentCategory;
    private boolean canClose;
    boolean needsRefreshOptions = false;
    //


    public Panel() {
        categories.add(new ModCategory(this));
        categories.add(new OptionCategory(this));
        categories.add(new ChatCategory(this));
        categories.add(new PatcherCategory(this));

        currentCategory = getCategoryByClass(ModCategory.class);
    }

    public void initGui() {
        this.x = ResolutionHelper.getWidth() / 2 - 250;
        this.y = ResolutionHelper.getHeight() / 2 - 150;
        this.w = 530;
        this.h = 30;
        this.offsetX = 0;
        this.offsetY = 0;
        this.dragging = false;

        for(Category c : categories) {
            c.initGui();
        }

        animateSideBar.setEase(Easing.CUBIC_IN_OUT).setMin(0).setMax(40).setSpeed(200);
        animateTransition.setEase(Easing.CUBIC_IN_OUT).setMin(0).setMax(300).setSpeed(500);
        canClose = true;
    }

    /**
     * Renders the panel background, the sidebar and all the buttons
     *
     * @param mouseX The current X position of the mouse
     * @param mouseY The current Y position of the mouse
     */

    public void renderPanel(int mouseX, int mouseY) {
        boolean roundedCorners = Shindo.getInstance().getOptionManager().getOptionByName("Rounded Corners").isCheckToggled();
        int color = Shindo.getInstance().getOptionManager().getOptionByName("Color").getColor().getRGB();

        //PANEL FRAME
        Helper2D.drawRoundedRectangle(x, y + 30, w, h - 28, 2, Style.getColorTheme(2).getRGB(), 0);
        Helper2D.drawRoundedRectangle(x,
                currentCategory == getCategoryByClass(ModCategory.class) ? y + 60 : y + 30, w,
                currentCategory == getCategoryByClass(ModCategory.class)  ? h + 240 : h + 270, 2,
                Style.getColorTheme(2).getRGB(), 0
        );
        Helper2D.drawRectangle(x, y + 30, w, 30, Style.getColorTheme(3).getRGB());
        Helper2D.drawRoundedRectangle(x, y + 30, 70, h + 270, 2, Style.getColorTheme(4).getRGB(), 0);

        // CLOCK
        Helper2D.drawRoundedRectangle(x + 70, y - 5, 120, 20, 2, Style.getColorTheme(4).getRGB(), 0);
        Helper2D.drawPicture(x + 20, y - 10, 30, 30, color,  "logo.png");

        Shindo.getInstance().getFontHelper().size20.drawString(TimeHelper.getFormattedDate(), x + 124 , y + 2, -1);
        Shindo.getInstance().getFontHelper().size20.drawString(TimeHelper.getFormattedTimeMinute(), x + 94 , y + 2, -1);

        Date date = new Date();
        boolean isDay = date.getHours() < 19 && date.getHours() > 6 ;
        Helper2D.drawPicture(x + 72, y - 2, 15, 15, color,  isDay ? "icon/light.png" : "icon/dark.png" );

        // CLOSE BUTTON
        boolean hovered = MathHelper.withinBox(x + w - 30, y - 6, 25, 25, mouseX, mouseY);
        Helper2D.drawRoundedRectangle(x + w - 30, y - 6, 25, 25, 2, Style.getColorTheme(hovered ? 7 : 5).getRGB(), roundedCorners ? 0 : -1);
        Helper2D.drawPicture(x + w - 30, y - 6, 25, 25, color, "icon/cross.png");

        // CATEGORY TITLE
        if (currentCategory != getCategoryByClass(ModCategory.class)) {
            Helper2D.drawRoundedRectangle(x + 78, y + 38, 6 + Shindo.getInstance().getFontHelper().size20.getStringWidth(currentCategory.getName()), 4 + Shindo.getInstance().getFontHelper().size20.getFontHeight(), 2, Style.getColorTheme(7).getRGB(), 0);
            Shindo.getInstance().getFontHelper().size20.drawStringWidthShadow(currentCategory.getName(), x + 80, y + 40, color);
        }

        currentCategory.getScrollHelper().update();
        animateTransition.update();

        Shindo.getInstance().getMessageHelper().renderMessage();

        for (Category c : categories) {

            if(c.equals(currentCategory)) {
                if(!c.isInitialized()) {
                    c.setInitialized(true);
                    c.initCategory();
                    c.setCanClose(true);
                }

                if (needsRefreshOptions) {
                    c.initCategory();
                    needsRefreshOptions = false;
                }

                c.drawScreen(mouseX, mouseY);
            } else if(c.isInitialized()) {
                c.setInitialized(false);
            }
        }


        animateSideBar.update();
        drawSideBar();
    }

    private void drawSideBar() {
        boolean roundedCorners = Shindo.getInstance().getOptionManager().getOptionByName("Rounded Corners").isCheckToggled();
        int color = Shindo.getInstance().getOptionManager().getOptionByName("Color").getColor().getRGB();

        int space = 0;
        int value = 0;
        for (int i = 0 ; i <= 6; i++) {
            Helper2D.drawRoundedRectangle(x + 20, y  + 45 + i * 40 + MathHelper.addIntToInt(space, 1, i), 30, 30, 2, Style.getColorTheme(6).getRGB(), roundedCorners ? 0 : -1);
        }

        Helper2D.drawRoundedRectangle(x + 20 , y + 45 + MathHelper.addIntToInt(value, 1, currentCategory.getValue())  * 40 + MathHelper.addIntToInt(space, 1, currentCategory.getValue()) , 30 , 30, 2, Style.getColorTheme(8).getRGB(), roundedCorners ? 0 : -1);

        int index = 0;
        int space1 = 0;
        for (Category c : categories) {

            //Shindo.getInstance().getFontHelper().size15.drawString(button, x + 25 - Shindo.getInstance().getFontHelper().size15.getStringWidth(button) / 2f + index * 40 + MathHelper.addIntToInt(space1, 5, index), y + 18 , color);
            Helper2D.drawPicture(x + 28 , y + 52 + index * 40 + MathHelper.addIntToInt(space1, 1, index), 15, 15, color, "icon/button/sidebar/" + c.getName() + ".png");

            index++;
        }
    }

    /**
     * Closes the modmenu and opens the editor if the close button is pressed
     * Sets the "selected" variable to whatever tab is pressed in the sidebar
     *
     * @param mouseX      The current X position of the mouse
     * @param mouseY      The current Y position of the mouse
     * @param mouseButton The current mouse button which is pressed
     */

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0) {
            int index = 0;
            int space = 0;
            for (Category c : categories) {
                if (MathHelper.withinBox(x + 20 , y + 45 + index * 40 + MathHelper.addIntToInt(space, 1, index) , 35 , 35, mouseX, mouseY)) {
                    currentCategory = c;
                }
                index++;
            }
            currentCategory.mouseClicked(mouseX, mouseY, mouseButton);


            if (MathHelper.withinBox(x + w - 30, y - 6, 25, 25, mouseX, mouseY)) {
                mc.displayGuiScreen(Shindo.getInstance().getHudEditor());
            }
        }

    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
        currentCategory.mouseReleased(mouseX, mouseY, state);
    }

    public void keyTyped(char typedChar, int keyCode) {
        currentCategory.keyTyped(typedChar, keyCode);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(int offsetX) {
        this.offsetX = offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(int offsetY) {
        this.offsetY = offsetY;
    }

    public boolean isDragging() {
        return dragging;
    }

    public void setDragging(boolean dragging) {
        this.dragging = dragging;
    }

    public boolean isAnyButtonOpen() { return anyButtonOpen; }

    public void setAnyButtonOpen(boolean anyButtonOpen) { this.anyButtonOpen = anyButtonOpen; }

    public void setNeedsRefreshOptions(boolean needsRefreshOptions) {
        this.needsRefreshOptions = needsRefreshOptions;
    }

    public ArrayList<Category> getCategories() { return categories; }

    public Category getCategoryByClass(Class<?> clazz) {

        for(Category c : categories) {
            if(c.getClass().equals(clazz)) {
                return c;
            }
        }

        return null;
    }

    public boolean isCanClose() {
        return canClose;
    }

    public void setCanClose(boolean canClose) {
        this.canClose = canClose;
    }

}
