package me.miki.shindo.ui.mods.sidebar.options.type;

import me.miki.shindo.Shindo;
import me.miki.shindo.feature.option.Option;
import me.miki.shindo.ui.mods.Panel;
import me.miki.shindo.ui.mods.sidebar.options.Options;
import me.miki.shindo.util.render.Helper2D;

public class Category extends Options {

    public Category(Option option, Panel panel, int y) {
        super(option, panel, y);
    }

    @Override
    public void renderOption(int mouseX, int mouseY) {
        Shindo.getInstance().getFontHelper().size20.drawString(
                option.getName(),
                panel.getX() + 20,
                panel.getY() + panel.getH() + getY() + 6,
                0x90ffffff
        );
        Helper2D.drawRectangle(panel.getX() + 20, panel.getY() + panel.getH() + getY() + 20, panel.getW() - 40, 1, 0x40ffffff);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {

    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {

    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {

    }
}