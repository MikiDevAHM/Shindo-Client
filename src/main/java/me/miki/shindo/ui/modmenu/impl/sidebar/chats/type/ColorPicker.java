/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package me.miki.shindo.ui.modmenu.impl.sidebar.chats.type;

import me.miki.shindo.Shindo;
import me.miki.shindo.features.chat.Chat;
import me.miki.shindo.helpers.ColorHelper;
import me.miki.shindo.helpers.MathHelper;
import me.miki.shindo.helpers.animation.Animate;
import me.miki.shindo.helpers.animation.Easing;
import me.miki.shindo.helpers.hud.PositionHelper;
import me.miki.shindo.helpers.render.GLHelper;
import me.miki.shindo.helpers.render.Helper2D;
import me.miki.shindo.ui.Style;
import me.miki.shindo.ui.modmenu.impl.Panel;
import me.miki.shindo.ui.modmenu.impl.sidebar.chats.Chats;

import java.awt.*;

public class ColorPicker extends Chats {

    private final PositionHelper sidePosHelper = new PositionHelper(75);
    private final PositionHelper mainPosHelperX = new PositionHelper(100);
    private final PositionHelper mainPosHelperY = new PositionHelper(150);
    private final Animate animate = new Animate();

    private boolean dragSide;
    private boolean dragMain;
    private boolean open;

    public ColorPicker(Chat chat, Panel panel, int y) {
        super(chat, panel, y);
        open = false;
        animate.setEase(Easing.CUBIC_OUT).setMin(0).setMax(70).setSpeed(200);
    }

    @Override
    public void renderChats(int mouseX, int mouseY) {
        int getXW = panel.getX() + panel.getW();
        int getYH = panel.getY() + panel.getH() + getY();
        boolean rounded = Shindo.getInstance().getOptionManager().getOptionByName("Rounded Corners").isCheckToggled();
        int color = Shindo.getInstance().getOptionManager().getOptionByName("Color").getColor().getRGB();

        Shindo.getInstance().getFontHelper().size30.drawString(chat.getName(), panel.getX() + 80, getYH + 6, color);
        animate.update();

        if (open) {
            if (!animate.hasFinished()) {
                GLHelper.startScissor(getXW - 193, getYH + 25, 180, 70);
            }

            int offset = animate.getValueI() - 70;

            Helper2D.drawRoundedRectangle(getXW - 40, getYH + 25 + offset, 20, 70, 2, Style.getColorTheme(5).getRGB(), rounded ? 0 : -1);
            Helper2D.drawRoundedRectangle(getXW - 193, getYH + 25 + offset, 150, 70, 2, Style.getColorTheme(5).getRGB(), rounded ? 0 : -1);

            Helper2D.drawPicture(getXW - 38, getYH + 27 + offset, 16, 66, 0, "icon/hue.png");

            sidePosHelper.pre(chat.getSideSlider());

            if (dragSide) {
                chat.setSideSlider(mouseY - (getYH + 25));
                float sliderHeight = 65;
                if (chat.getSideSlider() < 0) {
                    chat.setSideSlider(0);
                } else if (chat.getSideSlider() > sliderHeight) {
                    chat.setSideSlider(sliderHeight);
                }
            }

            sidePosHelper.post(chat.getSideSlider());
            sidePosHelper.update();

            Color sideColor = ColorHelper.getColorAtPixel(getXW - 35, getYH + 28 + chat.getSideSlider() + offset);
            if (animate.hasFinished() && getYH + 28 + chat.getSideSlider() + offset < (panel.getY() + panel.getH() + 300))
                chat.setSideColor(sideColor);

            float sidePosY = getYH + 25 + chat.getSideSlider() + offset;
            Helper2D.drawRoundedRectangle(getXW - 40, (int) (sidePosHelper.isDirection() ?
                    sidePosY - sidePosHelper.getDifference() - sidePosHelper.getValue() :
                    sidePosY - sidePosHelper.getDifference() + sidePosHelper.getValue()
            ), 20, 5, 2, -1, rounded ? 0 : -1);
            Helper2D.drawHorizontalGradientRectangle(getXW - 191, getYH + 27 + offset, 146, 66, -1, chat.getSideColor().getRGB());
            Helper2D.drawGradientRectangle(getXW - 191, getYH + 27 + offset, 146, 66, 0x00000000, 0xff000000);

            mainPosHelperX.pre(chat.getMainSlider()[0]);
            mainPosHelperY.pre(chat.getMainSlider()[1]);

            if (dragMain) {
                chat.getMainSlider()[0] = mouseX - (getXW - 193);
                chat.getMainSlider()[1] = mouseY - (getYH + 25);
                float sliderWidth = 145;
                float sliderHeight = 65;
                if (chat.getMainSlider()[0] < 0) {
                    chat.getMainSlider()[0] = 0;
                } else if (chat.getMainSlider()[0] > sliderWidth) {
                    chat.getMainSlider()[0] = sliderWidth;
                }
                if (chat.getMainSlider()[1] < 0) {
                    chat.getMainSlider()[1] = 0;
                } else if (chat.getMainSlider()[1] > sliderHeight) {
                    chat.getMainSlider()[1] = sliderHeight;
                }
            }

            mainPosHelperX.post(chat.getMainSlider()[0]);
            mainPosHelperY.post(chat.getMainSlider()[1]);
            mainPosHelperX.update();
            mainPosHelperY.update();

            Color mainColor = ColorHelper.getColorAtPixel(getXW - 191 + chat.getMainSlider()[0], getYH + 28 + chat.getMainSlider()[1] + offset);
            if (animate.hasFinished() && getYH + 28 + chat.getMainSlider()[1] + offset < (panel.getY() + panel.getH() + 300))
                chat.setColor(mainColor);
            float mainPosX = getXW - 193 + chat.getMainSlider()[0];
            float mainPosY = getYH + 25 + chat.getMainSlider()[1] + offset;
            Helper2D.drawRoundedRectangle(
                    (int) (mainPosHelperX.isDirection() ?
                            mainPosX - mainPosHelperX.getDifference() - mainPosHelperX.getValue() :
                            mainPosX - mainPosHelperX.getDifference() + mainPosHelperX.getValue()
                    ),
                    (int) (mainPosHelperY.isDirection() ?
                            mainPosY - mainPosHelperY.getDifference() - mainPosHelperY.getValue() :
                            mainPosY - mainPosHelperY.getDifference() + mainPosHelperY.getValue()
                    ), 5, 5, 3, -1, 0
            );

            if (!animate.hasFinished()) {
                GLHelper.endScissor();
            }
        }

        String rgbText = "R" + chat.getColor().getRed() + " G" + chat.getColor().getGreen() + " B" + chat.getColor().getBlue();
        Shindo.getInstance().getFontHelper().size20.drawString(rgbText, getXW - 45 - Shindo.getInstance().getFontHelper().size20.getStringWidth(rgbText), getYH + 9, -1);
        Helper2D.drawRoundedRectangle(getXW - 40, getYH + 2, 20, 20, 2, Style.getColorTheme(6).getRGB(), rounded ? 0 : -1);
        Helper2D.drawRectangle(getXW - 38, getYH + 4, 16, 16, chat.getColor().getRGB());

        setH(open ? 100 : 25);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        int getXW = panel.getX() + panel.getW();
        int getYH = panel.getY() + panel.getH() + getY();

        if (MathHelper.withinBox(getXW - 38, getYH + 4, 16, 16, mouseX, mouseY)) {
            open = !open;
            animate.reset();
        } else if (MathHelper.withinBox(getXW - 40, getYH + 25, 20, 70, mouseX, mouseY)) {
            dragSide = true;
        } else if (MathHelper.withinBox(getXW - 193, getYH + 25, 150, 70, mouseX, mouseY)) {
            dragMain = true;
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        dragSide = false;
        dragMain = false;
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {

    }
}