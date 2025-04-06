/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package me.miki.shindo.ui.hudeditor.impl.impl;

import me.miki.shindo.Shindo;
import me.miki.shindo.events.EventTarget;
import me.miki.shindo.events.impl.RenderEvent;
import me.miki.shindo.helpers.render.GLHelper;
import me.miki.shindo.helpers.render.Helper2D;
import me.miki.shindo.ui.Style;
import me.miki.shindo.ui.hudeditor.impl.HudMod;

public class CpsHud extends HudMod {

    public CpsHud() {
        super("CPS", 10, 10);
        setW(60);
        setH(20);
    }

    @Override
    public void renderMod(int mouseX, int mouseY) {
        GLHelper.startScale(getX(), getY(), getSize());
        if (Shindo.getInstance().getModManager().getMod(getName()).isToggled()) {
            if (isModern()) {
                if (isBackground()) {
                    Helper2D.drawRoundedRectangle(getX(), getY(), getW(), getH(), 2, Style.getColor(50).getRGB(), 0);
                }
                Shindo.getInstance().getFontHelper().size20.drawString(
                        isRightClick() ? "CPS: " + getLeftCPS() + " | " + getRightCPS() : "CPS: " + getLeftCPS(),
                        getX() + getW() / 2f - Shindo.getInstance().getFontHelper().size20.getStringWidth(isRightClick() ? "CPS: " + getLeftCPS() + " | " + getRightCPS() : "CPS: " + getLeftCPS()) / 2f,
                        getY() + 6,
                        getColor()
                );
            } else {
                if (isBackground()) {
                    Helper2D.drawRectangle(getX(), getY(), getW(), getH(), Style.getColor(50).getRGB());
                }
                mc.fontRendererObj.drawString(
                        isRightClick() ? "CPS: " + getLeftCPS() + " | " + getRightCPS() : "CPS: " + getLeftCPS(),
                        getX() + getW() / 2 - mc.fontRendererObj.getStringWidth(isRightClick() ? "CPS: " + getLeftCPS() + " | " + getRightCPS() : "CPS: " + getLeftCPS()) / 2,
                        getY() + 6,
                        getColor()
                );
            }
            super.renderMod(mouseX, mouseY);
        }
        GLHelper.endScale();
    }

    @EventTarget
    public void onRender2D(RenderEvent e) {
        GLHelper.startScale(getX(), getY(), getSize());
        if (Shindo.getInstance().getModManager().getMod(getName()).isToggled()) {
            if (isModern()) {
                if (isBackground()) {
                    Helper2D.drawRoundedRectangle(getX(), getY(), getW(), getH(), 2, 0x50000000, 0);
                }
                Shindo.getInstance().getFontHelper().size20.drawString(
                        isRightClick() ? "CPS: " + getLeftCPS() + " | " + getRightCPS() : "CPS: " + getLeftCPS(),
                        getX() + getW() / 2f - Shindo.getInstance().getFontHelper().size20.getStringWidth(isRightClick() ? "CPS: " + getLeftCPS() + " | " + getRightCPS() : "CPS: " + getLeftCPS()) / 2f,
                        getY() + 6,
                        getColor()
                );
            } else {
                if (isBackground()) {
                    Helper2D.drawRectangle(getX(), getY(), getW(), getH(), 0x50000000);
                }
                mc.fontRendererObj.drawString(
                        isRightClick() ? "CPS: " + getLeftCPS() + " | " + getRightCPS() : "CPS: " + getLeftCPS(),
                        getX() + getW() / 2 - mc.fontRendererObj.getStringWidth(isRightClick() ? "CPS: " + getLeftCPS() + " | " + getRightCPS() : "CPS: " + getLeftCPS()) / 2,
                        getY() + 6,
                        getColor()
                );
            }
        }
        GLHelper.endScale();
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

    private boolean isRightClick() {
        return Shindo.getInstance().getSettingManager().getSettingByModAndName(getName(), "Right click").isCheckToggled();
    }

    private int getLeftCPS() {
        return Shindo.getInstance().getCpsHelper().getCPS(0);
    }

    private int getRightCPS() {
        return Shindo.getInstance().getCpsHelper().getCPS(1);
    }
}
