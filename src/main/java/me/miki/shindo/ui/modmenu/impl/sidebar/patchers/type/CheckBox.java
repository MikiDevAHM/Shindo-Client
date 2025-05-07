/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package me.miki.shindo.ui.modmenu.impl.sidebar.patchers.type;

import me.miki.shindo.Shindo;
import me.miki.shindo.features.patcher.Patcher;
import me.miki.shindo.helpers.MathHelper;
import me.miki.shindo.helpers.animation.Animate;
import me.miki.shindo.helpers.animation.Easing;
import me.miki.shindo.helpers.render.Helper2D;
import me.miki.shindo.ui.Style;
import me.miki.shindo.ui.modmenu.impl.Panel;
import me.miki.shindo.ui.modmenu.impl.sidebar.patchers.Patchers;

public class CheckBox extends Patchers {

    private final Animate animCheckBox = new Animate();

    public CheckBox(Patcher patcher, Panel panel, int y) {
        super(patcher, panel, y);
        animCheckBox.setEase(Easing.CUBIC_IN_OUT).setMin(0).setMax(10).setSpeed(100);
    }

    /**
     * Renders the Checkbox Setting
     *
     * @param mouseX The current X position of the mouse
     * @param mouseY The current Y position of the mouse
     */

    @Override
    public void renderPatchers(int mouseX, int mouseY) {
        boolean roundedCorners = Shindo.getInstance().getOptionManager().getOptionByName("Rounded Corners").isCheckToggled();
        int color = Shindo.getInstance().getOptionManager().getOptionByName("Color").getColor().getRGB();

        animCheckBox.setReversed(!patcher.isCheckToggled()).update();

        Shindo.getInstance().getFontHelper().size30.drawString(
                patcher.getName(),
                panel.getX() + 80,
                panel.getY() + panel.getH() + getY() + 6,
                color
        );

        Helper2D.drawRoundedRectangle(
                panel.getX() + panel.getW() - 40,
                panel.getY() + panel.getH() + getY() + 2,
                20, 20, 2,
                Style.getColorTheme(patcher.isCheckToggled() ? 8 : 5).getRGB(),
                roundedCorners ? 0 : -1
        );

        Helper2D.drawPicture(
                panel.getX() + panel.getW() - 30 - animCheckBox.getValueI(),
                panel.getY() + panel.getH() + 12 + getY() - animCheckBox.getValueI(),
                animCheckBox.getValueI() * 2,
                animCheckBox.getValueI() * 2,
                color,
                "icon/check.png"
        );
    }

    /**
     * Toggles the state of the setting if it is pressed
     *
     * @param mouseX The current X position of the mouse
     * @param mouseY The current Y position of the mouse
     * @param mouseButton The current mouse button which is pressed
     */

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if(mouseButton == 0){
            if (MathHelper.withinBox(
                    panel.getX() + panel.getW() - 40,
                    panel.getY() + panel.getH() + 2 + getY(),
                    20, 20, mouseX, mouseY)
            ) {
                patcher.setCheckToggled(!patcher.isCheckToggled());
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
