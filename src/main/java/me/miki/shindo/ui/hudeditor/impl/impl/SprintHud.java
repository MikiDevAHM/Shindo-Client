/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package me.miki.shindo.ui.hudeditor.impl.impl;

import me.miki.shindo.Shindo;
import me.miki.shindo.events.EventTarget;
import me.miki.shindo.events.impl.RenderEvent;
import me.miki.shindo.features.mods.impl.ToggleSprintMod;
import me.miki.shindo.helpers.render.GLHelper;
import me.miki.shindo.helpers.render.Helper2D;
import me.miki.shindo.ui.Style;
import me.miki.shindo.ui.hudeditor.impl.HudMod;

public class SprintHud extends HudMod {

    public SprintHud() {
        super("ToggleSprint", 10, 10);
        setW(90);
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
                        "Sprint: " + (isSprinting() ? "Toggled" : "Vanilla"),
                        getX() + getW() / 2f - Shindo.getInstance().getFontHelper().size20.getStringWidth("Sprint: " + (isSprinting() ? "Toggled" : "Vanilla")) / 2f,
                        getY() + 6,
                        getColor()
                );
            } else {
                if (isBackground()) {
                    Helper2D.drawRectangle(getX(), getY(), getW(), getH(), Style.getColor(50).getRGB());
                }
                mc.fontRendererObj.drawString(
                        "Sprint: " + (isSprinting() ? "Toggled" : "Vanilla"),
                        getX() + getW() / 2 - mc.fontRendererObj.getStringWidth("Sprint: " + (isSprinting() ? "Toggled" : "Vanilla")) / 2,
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
                        "Sprint: " + (isSprinting() ? "Toggled" : "Vanilla"),
                        getX() + getW() / 2f - Shindo.getInstance().getFontHelper().size20.getStringWidth("Sprint: " + (isSprinting() ? "Toggled" : "Vanilla")) / 2f,
                        getY() + 6,
                        getColor()
                );
            } else {
                if (isBackground()) {
                    Helper2D.drawRectangle(getX(), getY(), getW(), getH(), 0x50000000);
                }
                mc.fontRendererObj.drawString(
                        "Sprint: " + (isSprinting() ? "Toggled" : "Vanilla"),
                        getX() + getW() / 2 - mc.fontRendererObj.getStringWidth("Sprint: " + (isSprinting() ? "Toggled" : "Vanilla")) / 2,
                        getY() + 6,
                        getColor()
                );
            }
        }
        GLHelper.endScale();
    }

    public int getColor() {
        return Shindo.getInstance().getSettingManager().getSettingByModAndName(getName(), "Font Color").getColor().getRGB();
    }

    private boolean isModern() {
        return Shindo.getInstance().getSettingManager().getSettingByModAndName(getName(), "Mode").getCurrentMode().equalsIgnoreCase("Modern");
    }

    private boolean isBackground() {
        return Shindo.getInstance().getSettingManager().getSettingByModAndName(getName(), "Background").isCheckToggled();
    }

    private boolean isSprinting() {
        return ToggleSprintMod.isSprinting();
    }
}
