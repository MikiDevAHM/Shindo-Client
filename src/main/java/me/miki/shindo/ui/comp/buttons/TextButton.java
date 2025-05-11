/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package me.miki.shindo.ui.comp.buttons;

import me.miki.shindo.Shindo;
import me.miki.shindo.helpers.MathHelper;
import me.miki.shindo.helpers.render.Helper2D;
import me.miki.shindo.ui.Style;

public class TextButton {


    private final String text;
    private final int w, h;
    private int x, y;

    public TextButton(String text, int x, int y) {
        this.text = text;
        this.x = x;
        this.y = y;
        this.w = 120;
        this.h = 20;
    }

    /**
     * Renders the button with the position, width and height taken from the constructor
     *
     * @param mouseX The current X position of the mouse
     * @param mouseY The current Y position of the mouse
     */

    public void renderButton(int x, int y, int mouseX, int mouseY) {
        this.x = x;
        this.y = y;

        Helper2D.drawRoundedRectangle(x, y, w, h, 3,
                Style.getColorTheme(8).getRGB(),
                Shindo.getInstance().getOptionManager().getOptionByName("Rounded Corners").isCheckToggled() ? 0 : -1
        );
        Helper2D.drawRoundedRectangle(x + 1, y + 1, w - 2, h - 2, 3,
                Style.getColorTheme(isHovered(mouseX, mouseY) ? 7 : 5).getRGB(),
                Shindo.getInstance().getOptionManager().getOptionByName("Rounded Corners").isCheckToggled() ? 0 : -1
        );

        Helper2D.drawCircle(x + 10, y + 10, 3, 0, 360, Style.getColorTheme(10).getRGB());
        Helper2D.drawCircle(x + 10, y + 10, 2, 0, 360, Style.getColorTheme(9).getRGB());


        Shindo.getInstance().getFontHelper().size20.drawString(
                text,
                x + w / 2f - Shindo.getInstance().getFontHelper().size20.getStringWidth(text) / 2f,
                y + h / 2f - 4,
                -1
        );
    }

    public boolean isHovered(int mouseX, int mouseY) {
        return MathHelper.withinBox(x, y, w, h, mouseX, mouseY);
    }

    public int getW() {
        return w;
    }

    public int getH() {
        return h;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getText() {
        return text;
    }
}
