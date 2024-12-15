package me.miki.shindo.ui.hudeditor.impl.impl;

import me.miki.shindo.Shindo;
import me.miki.shindo.event.EventTarget;
import me.miki.shindo.event.impl.render.Render2DEvent;
import me.miki.shindo.ui.Style;
import me.miki.shindo.ui.hudeditor.HudEditor;
import me.miki.shindo.ui.hudeditor.impl.HudMod;
import me.miki.shindo.util.TimeHelper;
import me.miki.shindo.util.render.GLHelper;
import me.miki.shindo.util.render.Helper2D;

public class TimeHud extends HudMod {

    public TimeHud() {
        super("Time", 10, 10);
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
                        TimeHelper.getFormattedTimeSecond(),
                        getX() + getW() / 2f - (Shindo.getInstance().getFontHelper().size20.getStringWidth(TimeHelper.getFormattedTimeSecond())) / 2f,
                        getY() + 6,
                        getColor()
                );
            } else {
                if (isBackground()) {
                    Helper2D.drawRectangle(getX(), getY(), getW(), getH(), Style.getColor(50).getRGB());
                }
                mc.fontRendererObj.drawString(
                        TimeHelper.getFormattedTimeSecond(),
                        getX() + getW() / 2 - (mc.fontRendererObj.getStringWidth(TimeHelper.getFormattedTimeSecond())) / 2,
                        getY() + 6, getColor()
                );
            }
            super.renderMod(mouseX, mouseY);
        }
        GLHelper.endScale();
    }

    @EventTarget
    public void onRender2D(Render2DEvent e) {
        GLHelper.startScale(getX(), getY(), getSize());
        if (Shindo.getInstance().getModManager().getMod(getName()).isToggled() && !(mc.currentScreen instanceof HudEditor)) {
            if (isModern()) {
                if (isBackground()) {
                    Helper2D.drawRoundedRectangle(getX(), getY(), getW(), getH(), 2, 0x50000000, 0);
                }
                Shindo.getInstance().getFontHelper().size20.drawString(
                        TimeHelper.getFormattedTimeSecond(),
                        getX() + getW() / 2f - (Shindo.getInstance().getFontHelper().size20.getStringWidth(TimeHelper.getFormattedTimeSecond())) / 2f,
                        getY() + 6,
                        getColor()
                );
            } else {
                if (isBackground()) {
                    Helper2D.drawRectangle(getX(), getY(), getW(), getH(), 0x50000000);
                }
                mc.fontRendererObj.drawString(
                        TimeHelper.getFormattedTimeSecond(),
                        getX() + getW() / 2 - (mc.fontRendererObj.getStringWidth(TimeHelper.getFormattedTimeSecond())) / 2,
                        getY() + 6, getColor()
                );
            }
        }
        GLHelper.endScale();
    }

    public int getColor() {
        return Shindo.getInstance().getSettingsManager().getSettingByModAndName(getName(), "Font Color").getColor().getRGB();
    }

    private boolean isModern() {
        return Shindo.getInstance().getSettingsManager().getSettingByModAndName(getName(), "Mode").getCurrentMode().equalsIgnoreCase("Modern");
    }

    private boolean isBackground() {
        return Shindo.getInstance().getSettingsManager().getSettingByModAndName(getName(), "Background").isCheckToggled();
    }
}
