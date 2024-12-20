package me.miki.shindo.ui.mods.sidebar.options.type;


import org.lwjgl.input.Keyboard;

import me.miki.shindo.Shindo;
import me.miki.shindo.event.EventTarget;
import me.miki.shindo.event.impl.tick.ClientTickEvent;
import me.miki.shindo.feature.option.Option;
import me.miki.shindo.ui.Style;
import me.miki.shindo.ui.mods.Panel;
import me.miki.shindo.ui.mods.sidebar.options.Options;
import me.miki.shindo.util.MathHelper;
import me.miki.shindo.util.render.Helper2D;

public class Keybinding extends Options {

    private boolean active;

    public Keybinding(Option option, Panel panel, int y) {
        super(option, panel, y);
    }

    /**
     * Renders the current keybinding and background,
     * Keybinding Setting
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
                panel.getY() + panel.getH() + 6 + getY(),
                color
        );

        Helper2D.drawRoundedRectangle(
                panel.getX() + panel.getW() - 90,
                panel.getY() + panel.getH() + 2 + getY(),
                70, 20, 2,
                Style.getColor(option.isCheckToggled() ? 80 : 50).getRGB(),
                roundedCorners ? 0 : -1
        );
        Shindo.getInstance().getFontHelper().size20.drawString(
                active ? "?" : Keyboard.getKeyName(option.getKey()),
                panel.getX() + panel.getW() - 55 -
                Shindo.getInstance().getFontHelper().size20.getStringWidth(active ? "?" : Keyboard.getKeyName(option.getKey())) / 2f,
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
        if (MathHelper.withinBox(panel.getX() + panel.getW() - 140,
                panel.getY() + panel.getH() + 2 + getY(),
                120, 21, mouseX, mouseY)
        ) {
            active = !active;
            if(active) {
                option.setKey(Keyboard.KEY_NONE);
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
    public void onKey(ClientTickEvent e) {
        int key = Keyboard.getEventKey();
        if (active) {
            option.setKey(key);
            if (Keyboard.isKeyDown(key)) {
                active = false;
            }
        }
    }
}