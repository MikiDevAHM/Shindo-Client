/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package me.miki.shindo.ui.hudeditor.impl.impl.keystrokes;

import me.miki.shindo.Shindo;
import me.miki.shindo.events.EventTarget;
import me.miki.shindo.events.impl.EventRender2D;
import me.miki.shindo.events.impl.EventTick;
import me.miki.shindo.helpers.render.GLHelper;
import me.miki.shindo.ui.Style;
import me.miki.shindo.ui.hudeditor.impl.HudMod;
import me.miki.shindo.ui.hudeditor.impl.impl.keystrokes.keys.KeyboardKey;
import me.miki.shindo.ui.hudeditor.impl.impl.keystrokes.keys.MouseKey;

public class KeystrokesHud extends HudMod {

    KeyboardKey keyUp = new KeyboardKey();
    KeyboardKey keyDown = new KeyboardKey();
    KeyboardKey keyLeft = new KeyboardKey();
    KeyboardKey keyRight = new KeyboardKey();
    KeyboardKey keyJump = new KeyboardKey();
    MouseKey mouseKey0 = new MouseKey();
    MouseKey mouseKey1 = new MouseKey();

    public KeystrokesHud() {
        super("Keystrokes", 50, 10);
        setW(80);
        setH(80);
    }

    @Override
    public void renderMod(int mouseX, int mouseY) {
        GLHelper.startScale(getX(), getY(), getSize());
        if (Shindo.getInstance().getModManager().getMod(getName()).isToggled()) {
            keyUp.renderKey(getX() + 28, getY() + 2, 24, 24, isModern(), mc.gameSettings.keyBindForward, Style.getColor(50).getRGB(), getColor(), isBackground());
            keyDown.renderKey(getX() + 28, getY() + 28, 24, 24, isModern(), mc.gameSettings.keyBindBack, Style.getColor(50).getRGB(), getColor(), isBackground());
            keyLeft.renderKey(getX() + 2, getY() + 28, 24, 24, isModern(), mc.gameSettings.keyBindLeft, Style.getColor(50).getRGB(), getColor(), isBackground());
            keyRight.renderKey(getX() + getW() - 26, getY() + 28, 24, 24, isModern(), mc.gameSettings.keyBindRight, Style.getColor(50).getRGB(), getColor(), isBackground());
            keyJump.renderKey(getX() + 2, isClicks() ? getY() + 54 + 26 : getY() + 54, 76, 24, isModern(), mc.gameSettings.keyBindJump, Style.getColor(50).getRGB(), getColor(), isBackground());
            if (isClicks()) {
                mouseKey0.renderKey(getX() + 2, getY() + 54, 38, 24, isModern(), 0, Style.getColor(50).getRGB(), getColor(), isBackground(), isCPS());
                mouseKey1.renderKey(getX() + 2 + 40, getY() + 54, 36, 24, isModern(), 1, Style.getColor(50).getRGB(), getColor(), isBackground(), isCPS());
            }
            super.renderMod(mouseX, mouseY);
        }
        GLHelper.endScale();
    }

    @EventTarget
    public void onRender2D(EventRender2D e) {
        GLHelper.startScale(getX(), getY(), getSize());
        if (Shindo.getInstance().getModManager().getMod(getName()).isToggled()) {
            keyUp.renderKey(getX() + 28, getY() + 2, 24, 24, isModern(), mc.gameSettings.keyBindForward, 0x50000000, getColor(), isBackground());
            keyDown.renderKey(getX() + 28, getY() + 28, 24, 24, isModern(), mc.gameSettings.keyBindBack, 0x50000000, getColor(), isBackground());
            keyLeft.renderKey(getX() + 2, getY() + 28, 24, 24, isModern(), mc.gameSettings.keyBindLeft, 0x50000000, getColor(), isBackground());
            keyRight.renderKey(getX() + getW() - 26, getY() + 28, 24, 24, isModern(), mc.gameSettings.keyBindRight, 0x50000000, getColor(), isBackground());
            keyJump.renderKey(getX() + 2, isClicks() ? getY() + 54 + 26 : getY() + 54, 76, 24, isModern(), mc.gameSettings.keyBindJump, 0x50000000, getColor(), isBackground());
            if (isClicks()) {
                mouseKey0.renderKey(getX() + 2, getY() + 54, 38, 24, isModern(), 0, 0x50000000, getColor(), isBackground(), isCPS());
                mouseKey1.renderKey(getX() + 2 + 40, getY() + 54, 36, 24, isModern(), 1, 0x50000000, getColor(), isBackground(), isCPS());
            }
        }
        GLHelper.endScale();
    }

    @EventTarget
    public void onTick(EventTick e) {
        if (isClicks()) {
            setH(106);
        } else {
            setH(80);
        }
    }

    private int getColor() {
        return Shindo.getInstance().getSettingManager().getSettingByModAndName(getName(), "Font Color").getColor().getRGB();
    }

    private boolean isModern() {
        return Shindo.getInstance().getSettingManager().getSettingByModAndName(getName(), "Mode").getCurrentMode().equalsIgnoreCase("Modern");
    }

    private boolean isBackground() {
        return Shindo.getInstance().getSettingManager().getSettingByModAndName(getName(), "Background").isCheckToggled();
    }

    private boolean isClicks() {
        return Shindo.getInstance().getSettingManager().getSettingByModAndName(getName(), "Clicks").isCheckToggled();
    }

    private boolean isCPS() {
        return Shindo.getInstance().getSettingManager().getSettingByModAndName(getName(), "CPS").isCheckToggled();
    }
}