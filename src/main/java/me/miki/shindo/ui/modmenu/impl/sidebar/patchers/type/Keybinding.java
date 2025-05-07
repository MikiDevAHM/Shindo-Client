/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package me.miki.shindo.ui.modmenu.impl.sidebar.patchers.type;

import me.miki.shindo.Shindo;
import me.miki.shindo.events.EventTarget;
import me.miki.shindo.events.impl.TickEvent;
import me.miki.shindo.features.patcher.Patcher;
import me.miki.shindo.helpers.MathHelper;
import me.miki.shindo.helpers.render.Helper2D;
import me.miki.shindo.ui.Style;
import me.miki.shindo.ui.modmenu.impl.Panel;
import me.miki.shindo.ui.modmenu.impl.sidebar.patchers.Patchers;
import org.lwjgl.input.Keyboard;

public class Keybinding extends Patchers {

    private boolean active;

    public Keybinding(Patcher patcher, Panel panel, int y) {
        super(patcher, panel, y);
    }

    /**
     * Renders the current keybinding and background,
     * Keybinding Setting
     *
     * @param mouseX The current X position of the mouse
     * @param mouseY The current Y position of the mouse
     */

    @Override
    public void renderPatchers(int mouseX, int mouseY) {
        boolean roundedCorners = Shindo.getInstance().getOptionManager().getOptionByName("Rounded Corners").isCheckToggled();
        int color = Shindo.getInstance().getOptionManager().getOptionByName("Color").getColor().getRGB();

        Shindo.getInstance().getFontHelper().size30.drawString(
                patcher.getName(),
                panel.getX() + 80,
                panel.getY() + panel.getH() + 6 + getY(),
                color
        );

        Helper2D.drawRoundedRectangle(
                panel.getX() + panel.getW() - 90,
                panel.getY() + panel.getH() + 2 + getY(),
                70, 20, 2,
                Style.getColorTheme(patcher.isCheckToggled() ? 8 : 5).getRGB(),
                roundedCorners ? 0 : -1
        );
        Shindo.getInstance().getFontHelper().size20.drawString(
                active ? "?" : Keyboard.getKeyName(patcher.getKey()),
                panel.getX() + panel.getW() - 55 -
                        Shindo.getInstance().getFontHelper().size20.getStringWidth(active ? "?" : Keyboard.getKeyName(patcher.getKey())) / 2f,
                panel.getY() + panel.getH() + 8 + getY(),
                color
        );
    }

    /**
     * Changes the active variable if the background of the keybinding is pressed
     *
     * @param mouseX      The current X position of the mouse
     * @param mouseY      The current Y position of the mouse
     * @param mouseButton The current mouse button which is pressed
     */

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (MathHelper.withinBox(
                panel.getX() + panel.getW() - 90,
                panel.getY() + panel.getH() + 2 + getY(),
                70, 20, mouseX, mouseY)
        ) {
            active = !active;
            if(active) {
                patcher.setKey(Keyboard.KEY_NONE);
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {

    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {

    }

    /**
     * Checks and sets the button which is pressed if it is active
     */

    @EventTarget
    public void onKey(TickEvent e) {
        int key = Keyboard.getEventKey();
        if (active) {
            patcher.setKey(key);
            if (Keyboard.isKeyDown(key)) {
                active = false;
            }
        }
    }
}
