package me.miki.shindo.ui.hudeditor.impl.impl;

import me.miki.shindo.Shindo;
import me.miki.shindo.event.EventTarget;
import me.miki.shindo.event.impl.render.Render2DEvent;
import me.miki.shindo.ui.Style;
import me.miki.shindo.ui.hudeditor.HudEditor;
import me.miki.shindo.ui.hudeditor.impl.HudMod;
import me.miki.shindo.util.MathHelper;
import me.miki.shindo.util.render.GLHelper;
import me.miki.shindo.util.render.Helper2D;

public class SpeedIndicatorHud extends HudMod {

    public SpeedIndicatorHud() {
        super("Speed Indicator", 10, 10);
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
                        getMeterPerSecond() + " m/s",
                        getX() + getW() / 2f - (Shindo.getInstance().getFontHelper().size20.getStringWidth((getMeterPerSecond() + " m/s")) / 2f),
                        getY() + 6,
                        getColor()
                );
            } else {
                if (isBackground()) {
                    Helper2D.drawRectangle(getX(), getY(), getW(), getH(), Style.getColor(50).getRGB());
                }
                mc.fontRendererObj.drawString(
                        getMeterPerSecond() + " m/s",
                        getX() + getW() / 2 - (mc.fontRendererObj.getStringWidth(getMeterPerSecond() + " m/s")) / 2,
                        getY() + 6,
                        getColor()
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
                        getMeterPerSecond() + " m/s",
                        getX() + getW() / 2f - (Shindo.getInstance().getFontHelper().size20.getStringWidth((getMeterPerSecond() + " m/s")) / 2f),
                        getY() + 6,
                        getColor()
                );
            } else {
                if (isBackground()) {
                    Helper2D.drawRectangle(getX(), getY(), getW(), getH(), 0x50000000);
                }
                mc.fontRendererObj.drawString(
                        getMeterPerSecond() + " m/s",
                        getX() + getW() / 2 - (mc.fontRendererObj.getStringWidth(getMeterPerSecond() + " m/s")) / 2,
                        getY() + 6,
                        getColor()
                );
            }
        }
        GLHelper.endScale();
    }

    private double getMeterPerSecond() {
        double x = mc.thePlayer.posX - mc.thePlayer.prevPosX;
        double z = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
        double speed = net.minecraft.util.MathHelper.sqrt_double(x * x + z * z) * 20;
        return MathHelper.round(speed, 1);
    }

    private int getColor() {
        return Shindo.getInstance().getSettingsManager().getSettingByModAndName(getName(), "Font Color").getColor().getRGB();
    }

    private boolean isModern() {
        return Shindo.getInstance().getSettingsManager().getSettingByModAndName(getName(), "Mode").getCurrentMode().equalsIgnoreCase("Modern");
    }

    private boolean isBackground() {
        return Shindo.getInstance().getSettingsManager().getSettingByModAndName(getName(), "Background").isCheckToggled();
    }
}