/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package me.miki.shindo.ui.modmenu.impl.sidebar.mods.impl.type;

import me.miki.shindo.Shindo;
import me.miki.shindo.events.EventTarget;
import me.miki.shindo.events.impl.EventTick;
import me.miki.shindo.features.settings.Setting;
import me.miki.shindo.helpers.MathHelper;
import me.miki.shindo.helpers.render.Helper2D;
import me.miki.shindo.ui.Style;
import me.miki.shindo.ui.modmenu.impl.sidebar.mods.Button;
import me.miki.shindo.ui.modmenu.impl.sidebar.mods.impl.Settings;
import org.lwjgl.input.Keyboard;

public class Keybinding extends Settings {

    private boolean active;

    public Keybinding(Setting setting, Button button, int y) {
        super(setting, button, y);
    }

    /**
     * Renders the current keybinding and background,
     * Keybinding Setting
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
                button.getPanel().getX() + 80,
                button.getPanel().getY() + button.getPanel().getH() + 6 + getY(),
                color
        );

        Helper2D.drawRoundedRectangle(
                button.getPanel().getX() + button.getPanel().getW() - 90,
                button.getPanel().getY() + button.getPanel().getH() + 2 + getY(),
                70, 20, 2,
                Style.getColorTheme(setting.isCheckToggled() ? 8 : 5).getRGB(),
                roundedCorners ? 0 : -1
        );
        Shindo.getInstance().getFontHelper().size20.drawString(
                active ? "?" : Keyboard.getKeyName(setting.getKey()),
                button.getPanel().getX() + button.getPanel().getW() - 55 -
                        Shindo.getInstance().getFontHelper().size20.getStringWidth(active ? "?" : Keyboard.getKeyName(setting.getKey())) / 2f,
                button.getPanel().getY() + button.getPanel().getH() + 8 + getY(),
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
        if (MathHelper.withinBox(button.getPanel().getX() + button.getPanel().getW() - 90,
                button.getPanel().getY() + button.getPanel().getH() + 2 + getY(),
                70, 20, mouseX, mouseY)
        ) {
            active = !active;
            if (active) {
                setting.setKey(Keyboard.KEY_NONE);
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
    public void onKey(EventTick e) {
        int key = Keyboard.getEventKey();
        if (active) {
            setting.setKey(key);
            if (Keyboard.isKeyDown(key)) {
                active = false;
            }
        }
    }
}
