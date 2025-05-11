/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package me.miki.shindo.ui.modmenu.impl.sidebar.chats.type;

import me.miki.shindo.Shindo;
import me.miki.shindo.features.chat.Chat;
import me.miki.shindo.helpers.MathHelper;
import me.miki.shindo.helpers.hud.PositionHelper;
import me.miki.shindo.helpers.render.Helper2D;
import me.miki.shindo.ui.Style;
import me.miki.shindo.ui.modmenu.impl.Panel;
import me.miki.shindo.ui.modmenu.impl.sidebar.chats.Chats;

public class Slider extends Chats {

    private final PositionHelper posHelper = new PositionHelper(125);
    private final int sliderWidth = 150;
    private boolean drag;
    private float sliderPos;

    public Slider(Chat chat, Panel panel, int y) {
        super(chat, panel, y);
        sliderPos = chat.getCurrentNumber() / (chat.getMaxNumber() / sliderWidth);
    }

    /**
     * Renders the Slider Setting
     *
     * @param mouseX The current X position of the mouse
     * @param mouseY The current Y position of the mouse
     */

    @Override
    public void renderChats(int mouseX, int mouseY) {
        boolean roundedCorners = Shindo.getInstance().getOptionManager().getOptionByName("Rounded Corners").isCheckToggled();
        int color = Shindo.getInstance().getOptionManager().getOptionByName("Color").getColor().getRGB();

        Shindo.getInstance().getFontHelper().size30.drawString(
                chat.getName(),
                panel.getX() + 80,
                panel.getY() + panel.getH() + getY() + 6,
                color
        );
        Shindo.getInstance().getFontHelper().size20.drawString(
                String.valueOf(MathHelper.round(chat.getCurrentNumber(), 1)),
                panel.getX() + panel.getW() - 175 -
                        Shindo.getInstance().getFontHelper().size20.getStringWidth(String.valueOf(MathHelper.round(chat.getCurrentNumber(), 1))),
                panel.getY() + panel.getH() + getY() + 9,
                color
        );

        Helper2D.drawRoundedRectangle(
                panel.getX() + panel.getW() - sliderWidth - 20,
                panel.getY() + panel.getH() + getY() + 10,
                150, 5, 2, Style.getColorTheme(4).getRGB(),
                roundedCorners ? 0 : -1
        );

        posHelper.pre(sliderPos);

        double diff = Math.min(sliderWidth, Math.max(0, mouseX - (panel.getX() + panel.getW() - sliderWidth - 20)));

        if (drag) {
            sliderPos = mouseX - (panel.getX() + panel.getW() - sliderWidth - 20);
            if (sliderPos < 0) {
                sliderPos = 0;
            } else if (sliderPos > sliderWidth) {
                sliderPos = sliderWidth;
            }
            if (diff == 0) {
                chat.setCurrentNumber(chat.getMinNumber());
            } else {
                chat.setCurrentNumber(sliderPos * (chat.getMaxNumber() / sliderWidth));
            }
        }

        posHelper.post(sliderPos);
        posHelper.update();

        float xPos = (panel.getX() + panel.getW() - sliderWidth - 20) + sliderPos - 3;
        Helper2D.drawRoundedRectangle(
                panel.getX() + panel.getW() - sliderWidth - 20,
                panel.getY() + panel.getH() + getY() + 10,
                (int) sliderPos, 5, 2, Style.getColorTheme(7).getRGB(),
                roundedCorners ? 0 : -1
        );

        Helper2D.drawRoundedRectangle(
                (int) (posHelper.isDirection() ? xPos - posHelper.getDifference() - posHelper.getValue() : xPos - posHelper.getDifference() + posHelper.getValue()) - 2,
                panel.getY() + panel.getH() + getY() + 8,
                9, 9, 2, Style.getColorTheme(1).getRGB(),
                roundedCorners ? 0 : -1
        );
        Helper2D.drawRoundedRectangle(
                (int) (posHelper.isDirection() ? xPos - posHelper.getDifference() - posHelper.getValue() : xPos - posHelper.getDifference() + posHelper.getValue()) - 1,
                panel.getY() + panel.getH() + getY() + 9,
                7, 7, 2, Style.getColorTheme(19).getRGB(),
                roundedCorners ? 0 : -1
        );
    }

    /**
     * Changes the drag variable if the slider is pressed
     *
     * @param mouseX      The current X position of the mouse
     * @param mouseY      The current Y position of the mouse
     * @param mouseButton The current mouse button which is pressed
     */

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (MathHelper.withinBox(
                panel.getX() + panel.getW() - 170,
                panel.getY() + panel.getH() + getY() + 5,
                150, 16, mouseX, mouseY)
        ) {
            drag = true;
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        drag = false;
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {

    }
}
