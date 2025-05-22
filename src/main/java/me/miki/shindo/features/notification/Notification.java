package me.miki.shindo.features.notification;

import me.miki.shindo.Shindo;
import me.miki.shindo.features.color.AccentColor;
import me.miki.shindo.features.nanovg.NanoVGManager;
import me.miki.shindo.features.nanovg.font.Fonts;
import me.miki.shindo.helpers.ColorHelper;
import me.miki.shindo.helpers.TimerHelper;
import me.miki.shindo.helpers.animation.normal.Animation;
import me.miki.shindo.helpers.animation.normal.Direction;
import me.miki.shindo.helpers.animation.normal.easing.EaseBackIn;
import me.miki.shindo.helpers.buffer.ScreenAlpha;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;

public class Notification {

    private Animation animation;
    private final String title;
    private final String message;
    private final NotificationType type;
    private final TimerHelper timer;

    private ScreenAlpha screenAlpha = new ScreenAlpha();

    public Notification(String title, String message, NotificationType type) {
        this.title = title;
        this.message = message;
        this.type = type;
        this.timer = new TimerHelper();
    }


    public void draw() {

        NanoVGManager nvg = Shindo.getInstance().getNanoVGManager();

        screenAlpha.wrap(() -> drawNanoVG(nvg), animation.getValueFloat());
    }

    private void drawNanoVG(NanoVGManager nvg) {

        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        Shindo instance = Shindo.getInstance();
        AccentColor currentColor = instance.getColorManager().getCurrentColor();

        float maxWidth;
        float titleWidth = nvg.getTextWidth(title, 9.6F, Fonts.MEDIUM);
        float messageWidth = nvg.getTextWidth(message, 7.6F, Fonts.REGULAR);

        maxWidth = Math.max(titleWidth, messageWidth);

        maxWidth = maxWidth + 31;

        int x = (int) (sr.getScaledWidth() - maxWidth) - 8;
        int y = sr.getScaledHeight() - 29 - 8;

        if(timer.delay(3000)) {
            animation.setDirection(Direction.BACKWARDS);
        }

        nvg.save();
        nvg.translate(160 - (animation.getValueFloat() * 160), 0);

        nvg.drawShadow(x, y, maxWidth, 29, 6);
        nvg.drawGradientRoundedRect(x, y, maxWidth, 29, 6, ColorHelper.applyAlpha(currentColor.getColor1(), 220), ColorHelper.applyAlpha(currentColor.getColor2(), 220));
        nvg.drawText(type.getIcon(), x + 5, y + 6F, Color.WHITE, 17, Fonts.ICON);
        nvg.drawText(title, x + 26, y + 6F, Color.white, 9.6F, Fonts.MEDIUM);
        nvg.drawText(message, x + 26, y + 17.5F, Color.WHITE, 7.5F, Fonts.REGULAR);

        nvg.restore();
    }

    public void show() {
        animation = new EaseBackIn(300, 1, 0);
        animation.setDirection(Direction.FORWARDS);
        animation.reset();
        timer.reset();
    }

    public boolean isShown() {
        return !animation.isDone(Direction.BACKWARDS);
    }

    public Animation getAnimation() {
        return animation;
    }

    public TimerHelper getTimer() {
        return timer;
    }
}
