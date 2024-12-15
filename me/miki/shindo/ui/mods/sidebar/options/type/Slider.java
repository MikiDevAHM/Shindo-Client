package me.miki.shindo.ui.mods.sidebar.options.type;

import me.miki.shindo.Shindo;
import me.miki.shindo.feature.option.Option;
import me.miki.shindo.ui.Style;
import me.miki.shindo.ui.mods.Panel;
import me.miki.shindo.ui.mods.sidebar.options.Options;
import me.miki.shindo.util.MathHelper;
import me.miki.shindo.util.hud.PositionHelper;
import me.miki.shindo.util.render.Helper2D;

public class Slider extends Options {

    private final PositionHelper posHelper = new PositionHelper(125);

    private boolean drag;
    private float sliderPos;
    private final int sliderWidth = 150;

    public Slider(Option option, Panel panel, int y) {
        super(option, panel, y);
        sliderPos = option.getCurrentNumber() / (option.getMaxNumber() / sliderWidth);
    }

    /**
     * Renders the Slider Setting
     *
     * @param mouseX The current X position of the mouse
     * @param mouseY The current Y position of the mouse
     */

    @Override
    public void renderOption(int mouseX, int mouseY) {
        boolean roundedCorners = Shindo.getInstance().getOptionManager().getOptionByName("Rounded Corners").isCheckToggled();
        int color = Shindo.getInstance().getOptionManager().getOptionByName("Color").getColor().getRGB();

        Shindo.getInstance().getFontHelper().size30.drawString(
                option.getName(),
                panel.getX() + 20,
                panel.getY() + panel.getH() + getY() + 6,
                color
        );
        Shindo.getInstance().getFontHelper().size20.drawString(
                String.valueOf(MathHelper.round(option.getCurrentNumber(), 1)),
                panel.getX() + panel.getW() - 175 -
                Shindo.getInstance().getFontHelper().size20.getStringWidth(String.valueOf(MathHelper.round(option.getCurrentNumber(), 1))),
                panel.getY() + panel.getH() + getY() + 9,
                color
        );

        Helper2D.drawRoundedRectangle(
                panel.getX() + panel.getW() - sliderWidth - 20,
                panel.getY() + panel.getH() + getY() + 10,
                150, 5, 2, Style.getColor(50).getRGB(),
                roundedCorners ? 0 : -1
        );

        posHelper.pre(sliderPos);

        if (drag) {
            sliderPos = mouseX - (panel.getX() + panel.getW() - sliderWidth - 20);
            if (sliderPos < 0) {
                sliderPos = 0;
            } else if (sliderPos > sliderWidth) {
                sliderPos = sliderWidth;
            }
            option.setCurrentNumber(sliderPos * (option.getMaxNumber() / sliderWidth));
        }

        posHelper.post(sliderPos);
        posHelper.update();

        float xPos = (panel.getX() + panel.getW() - sliderWidth - 20) + sliderPos - 3;
        Helper2D.drawRoundedRectangle(
                (int) (posHelper.isDirection() ? xPos - posHelper.getDifference() - posHelper.getValue() : xPos - posHelper.getDifference() + posHelper.getValue()),
                panel.getY() + panel.getH() + getY() + 5,
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
