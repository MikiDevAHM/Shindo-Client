package me.miki.shindo.ui.mods.sidebar.mods.impl.type;

import me.miki.shindo.Shindo;
import me.miki.shindo.feature.setting.Setting;
import me.miki.shindo.ui.Style;
import me.miki.shindo.ui.mods.sidebar.mods.Button;
import me.miki.shindo.ui.mods.sidebar.mods.impl.Settings;
import me.miki.shindo.util.MathHelper;
import me.miki.shindo.util.render.Helper2D;

public class CellGrid extends Settings {

    public CellGrid(Setting setting, Button button, int y) {
        super(setting, button, y);
        setH(160);
    }

    /**
     * Renders the CellGrid Setting
     *
     * @param mouseX The current X position of the mouse
     * @param mouseY The current Y position of the mouse
     */

    @Override
    public void renderSetting(int mouseX, int mouseY) {

        Shindo.getInstance().getFontHelper().size30.drawString(
                setting.getName(),
                button.getPanel().getX() + 20,
                button.getPanel().getY() + button.getPanel().getH() + 6 + getY(),
                Shindo.getInstance().getOptionManager().getOptionByName("Color").getColor().getRGB()
        );

        Helper2D.drawRoundedRectangle(
                button.getPanel().getX() + button.getPanel().getW() - 140,
                button.getPanel().getY() + button.getPanel().getH() + getY() + 5,
                121, 121, 2, Style.getColor(50).getRGB(), 0
        );

        for (int row = 0; row < 11; row++) {
            for (int col = 0; col < 11; col++) {
                int x = button.getPanel().getX() + button.getPanel().getW() - 140 + col * 11;
                int y = button.getPanel().getY() + button.getPanel().getH() + getY() + 5 + row * 11;
                Helper2D.drawRectangle(x, y, 11, 11,
                        setting.getCells()[row][col] ?
                                MathHelper.withinBox(x, y, 11, 11, mouseX, mouseY) ? 0x70ffffff : 0x50ffffff :
                                MathHelper.withinBox(x, y, 11, 11, mouseX, mouseY) ? 0x20ffffff : 0x00ffffff

                );
            }
        }

        int index = 0;
        int counter = 0;

        String text = "Erase all";
        int width = Shindo.getInstance().getFontHelper().size20.getStringWidth(text);
        int x = button.getPanel().getX() + button.getPanel().getW() - 20 - width;
        int y = button.getPanel().getY() + button.getPanel().getH() + getY() + 135;

        Shindo.getInstance().getFontHelper().size20.drawString(text, x - 3, y + 6, -1);
        Helper2D.drawRoundedRectangle(x - 9, y, width + 10, 20, 2, Style.getColor(
                MathHelper.withinBox(x - 9, y, width + 10, 20, mouseX, mouseY) ? 50 : 30
        ).getRGB(), 0);
    }

    /**
     * Toggles the state of a cell if it is pressed
     *
     * @param mouseX      The current X position of the mouse
     * @param mouseY      The current Y position of the mouse
     * @param mouseButton The current mouse button which is pressed
     */

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        for (int row = 0; row < 11; row++) {
            for (int col = 0; col < 11; col++) {
                if (MathHelper.withinBox(
                        button.getPanel().getX() + button.getPanel().getW() - 140 + col * 11,
                        button.getPanel().getY() + button.getPanel().getH() + getY() + 5 + row * 11,
                        11, 11, mouseX, mouseY
                )) {
                    setting.getCells()[row][col] = !setting.getCells()[row][col];
                }
            }
        }

        int index = 0;
        int counter = 0;

        String text = "Erase all";
        int width = Shindo.getInstance().getFontHelper().size20.getStringWidth(text);
        int x = button.getPanel().getX() + button.getPanel().getW() - 20 - width;
        int y = button.getPanel().getY() + button.getPanel().getH() + getY() + 135;

        if (MathHelper.withinBox(x - 9, y, width + 10, 20, mouseX, mouseY)) {
            setting.setCells(new boolean[11][11]);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {

    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {

    }
}
