package me.miki.shindo.ui.mods.sidebar.mods.impl.type;

import me.miki.shindo.Shindo;
import me.miki.shindo.feature.setting.Setting;
import me.miki.shindo.ui.Style;
import me.miki.shindo.ui.mods.sidebar.mods.Button;
import me.miki.shindo.ui.mods.sidebar.mods.impl.Settings;
import me.miki.shindo.util.MathHelper;
import me.miki.shindo.util.hud.PositionHelper;
import me.miki.shindo.util.render.Helper2D;

public class Slider extends Settings {

    private final PositionHelper posHelper = new PositionHelper(125);

    private boolean drag;
    private float sliderPos;
    private final int sliderWidth = 150;

    public Slider(Setting setting, Button button, int y) {
        super(setting, button, y);
        sliderPos = setting.getCurrentNumber() / (setting.getMaxNumber() / sliderWidth);
    }

    /**
     * Renders the Slider Setting
     *
     * @param mouseX The current X position of the mouse
     * @param mouseY The current Y position of the mouse
     */

    @Override
    public void renderSetting(int mouseX, int mouseY) {
        boolean roundedCorners = Shindo.getInstance().getOptionManager().getOptionByName("Rounded Corners").isCheckToggled();
        int color = Shindo.getInstance().getOptionManager().getOptionByName("Color").getColor().getRGB();

        Shindo.getInstance().getFontHelper().size30.drawString(
                setting.getName(),
                button.getPanel().getX() + 20,
                button.getPanel().getY() + button.getPanel().getH() + getY() + 6,
                color
        );
        Shindo.getInstance().getFontHelper().size20.drawString(
                String.valueOf(MathHelper.round(setting.getCurrentNumber(), 1)),
                button.getPanel().getX() + button.getPanel().getW() - 175 -
                Shindo.getInstance().getFontHelper().size20.getStringWidth(String.valueOf(MathHelper.round(setting.getCurrentNumber(), 1))),
                button.getPanel().getY() + button.getPanel().getH() + getY() + 9,
                color
        );

        Helper2D.drawRoundedRectangle(
                button.getPanel().getX() + button.getPanel().getW() - sliderWidth - 20,
                button.getPanel().getY() + button.getPanel().getH() + getY() + 10,
                150, 5, 2, Style.getColor(50).getRGB(),
                roundedCorners ? 0 : -1
        );

        posHelper.pre(sliderPos);

        if (drag) {
            sliderPos = mouseX - (button.getPanel().getX() + button.getPanel().getW() - sliderWidth - 20);
            if (sliderPos < 0) {
                sliderPos = 0;
            } else if (sliderPos > sliderWidth) {
                sliderPos = sliderWidth;
            }
            setting.setCurrentNumber(sliderPos * (setting.getMaxNumber() / sliderWidth));
        }

        posHelper.post(sliderPos);
        posHelper.update();

        float xPos = (button.getPanel().getX() + button.getPanel().getW() - sliderWidth - 20) + sliderPos - 3;
        Helper2D.drawRoundedRectangle(
                (int) (posHelper.isDirection() ? xPos - posHelper.getDifference() - posHelper.getValue() : xPos - posHelper.getDifference() + posHelper.getValue()),
                button.getPanel().getY() + button.getPanel().getH() + getY() + 5,
                7, 16, 1, Style.getColor(80).getRGB(),
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
                button.getPanel().getX() + button.getPanel().getW() - 170,
                button.getPanel().getY() + button.getPanel().getH() + getY() + 5,
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
