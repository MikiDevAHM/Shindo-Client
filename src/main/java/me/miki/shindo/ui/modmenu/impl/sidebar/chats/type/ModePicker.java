/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package me.miki.shindo.ui.modmenu.impl.sidebar.chats.type;

import me.miki.shindo.Shindo;
import me.miki.shindo.features.chat.Chat;
import me.miki.shindo.helpers.MathHelper;
import me.miki.shindo.helpers.animation.Animate;
import me.miki.shindo.helpers.animation.Easing;
import me.miki.shindo.helpers.render.GLHelper;
import me.miki.shindo.helpers.render.Helper2D;
import me.miki.shindo.ui.Style;
import me.miki.shindo.ui.modmenu.impl.Panel;
import me.miki.shindo.ui.modmenu.impl.sidebar.chats.Chats;

public class ModePicker extends Chats {

    private final Animate animateSelect = new Animate();
    private int longestString;
    private boolean fontChanger = false;

    public ModePicker(Chat chat, Panel panel, int y) {
        super(chat, panel, y);
        setOptionHeight(chat.getOptions().length * 15 + 5);
        animateSelect.setEase(Easing.CUBIC_OUT).setMin(0).setMax(chat.getOptions().length * 15 + 2).setReversed(false);
        if (chat.getName().equalsIgnoreCase("font changer")) {
            fontChanger = true;
        }
    }

    /**
     * Renders the ModePicker Setting
     *
     * @param mouseX The current X position of the mouse
     * @param mouseY The current Y position of the mouse
     */

    @Override
    public void renderChats(int mouseX, int mouseY) {
        boolean roundedCorners = Shindo.getInstance().getOptionManager().getOptionByName("Rounded Corners").isCheckToggled();
        int color = Shindo.getInstance().getOptionManager().getOptionByName("Color").getColor().getRGB();

        animateSelect.setSpeed(getOptionHeight() * 2).update();

        for (int i = 0; i < chat.getOptions().length; i++) {
            if (longestString < Shindo.getInstance().getFontHelper().size20.getStringWidth(chat.getOptions()[i])) {
                longestString = Shindo.getInstance().getFontHelper().size20.getStringWidth(chat.getOptions()[i]);
            }
        }

        Shindo.getInstance().getFontHelper().size30.drawString(
                chat.getName(),
                panel.getX() + 80,
                panel.getY() + panel.getH() + getY() + 6,
                color
        );

        Helper2D.drawRoundedRectangle(
                panel.getX() + panel.getW() - 30 - longestString,
                panel.getY() + panel.getH() + getY() + 4,
                longestString + 10,
                18,
                2, Style.getColorTheme(5).getRGB(),
                1
        );

        Shindo.getInstance().getFontHelper().size20.drawString(
                chat.getCurrentMode(),
                panel.getX() + panel.getW() - 20 - longestString - 4,
                panel.getY() + panel.getH() + getY() + 9,
                color
        );

        if (isOpen()) {
            if (!animateSelect.hasFinished()) {
                GLHelper.startScissor(
                        panel.getX(),
                        panel.getY() + panel.getH() + getY() + 23,
                        panel.getW(),
                        chat.getOptions().length * 15 + 2
                );
            }
            Helper2D.drawRoundedRectangle(
                    panel.getX() + panel.getW() - 30 - longestString,
                    panel.getY() + panel.getH() + getY() - chat.getOptions().length * 15 + animateSelect.getValueI() + 20,
                    longestString + 10, chat.getOptions().length * 15 + 2,
                    2, Style.getColorTheme(5).getRGB(),
                    2
            );

            int offset = 0;
            for (int i = 0; i < chat.getOptions().length; i++) {
                boolean hovered = MathHelper.withinBox(
                        panel.getX() + panel.getW() - 30 - longestString,
                        panel.getY() + panel.getH() + getY() + offset * 15 + 25,
                        longestString + 10, 15, mouseX, mouseY
                );

                if (hovered) {
                    Helper2D.drawRoundedRectangle(
                            panel.getX() + panel.getW() - 30 - longestString,
                            panel.getY() + panel.getH() + getY() + offset * 15 - chat.getOptions().length * 15 + animateSelect.getValueI() + 20,
                            longestString + 10, 17, 2, Style.getColorTheme(7).getRGB(),
                            roundedCorners ? 0 : -1
                    );
                }

                Shindo.getInstance().getFontHelper().size20.drawString(
                        chat.getOptions()[i],
                        panel.getX() + panel.getW() - 24 - longestString,
                        panel.getY() + panel.getH() + getY() + offset * 15 - chat.getOptions().length * 15 + animateSelect.getValueI() + 25,
                        color
                );

                offset++;
            }
            if (!animateSelect.hasFinished()) {
                GLHelper.endScissor();
            }
        }
    }

    /**
     * Opens and closes the setting
     * Loops through every Mode in the Setting and checks if it is pressed
     *
     * @param mouseX      The current X position of the mouse
     * @param mouseY      The current Y position of the mouse
     * @param mouseButton The current mouse button which is pressed
     */

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (MathHelper.withinBox(
                panel.getX() + panel.getW() - 30 - longestString,
                panel.getY() + panel.getH() + getY() + 4,
                longestString + 10,
                18, mouseX, mouseY)
        ) {
            if (mouseButton == 0) {
                if (isOpen()) {
                    setH(25);
                    setOpen(false);
                } else {
                    animateSelect.reset();
                    setH(getOptionHeight() + 25);
                    setOpen(true);
                }
            }
        }
        if (isOpen()) {
            int offset = 0;
            for (int i = 0; i < chat.getOptions().length; i++) {
                boolean hovered = MathHelper.withinBox(
                        panel.getX() + panel.getW() - 30 - longestString,
                        panel.getY() + panel.getH() + getY() + offset * 15 + 25,
                        longestString + 10, 15, mouseX, mouseY
                );

                if (hovered) {
                    chat.setCurrentMode(chat.getOptions()[i]);
                    if (fontChanger) {
                        Shindo.getInstance().getFontHelper().setFont(chat.getCurrentMode());
                        Shindo.getInstance().getFontHelper().init();
                    }
                }
                offset++;
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {

    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {

    }
}
