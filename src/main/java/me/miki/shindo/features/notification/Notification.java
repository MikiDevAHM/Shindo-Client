package me.miki.shindo.features.notification;

import me.miki.shindo.Shindo;
import me.miki.shindo.helpers.ResolutionHelper;
import me.miki.shindo.helpers.animation.Animate;
import me.miki.shindo.helpers.animation.Easing;
import me.miki.shindo.helpers.font.GlyphPageFontRenderer;
import me.miki.shindo.helpers.render.Helper2D;
import me.miki.shindo.ui.Style;

import java.awt.*;

public class Notification {

	private Animate animate = new Animate();
	private String title, message;
	private NotificationType type;
	private double time;
	private int timeLength;

	
	public Notification(String title, String message, NotificationType type) {
		this.title = title;
		this.message = message;
		this.type = type;
		timeLength = 2500;
		time = 0;
		animate.setEase(Easing.CUBIC_OUT).setMin(0).setMax(200).setSpeed(200);
	}


	public void draw() {

		Shindo instance = Shindo.getInstance();
		GlyphPageFontRenderer font = instance.getFontHelper().size15;
		GlyphPageFontRenderer font1 = instance.getFontHelper().size20;

		int maxWidth;
		int titleWidth = font1.getStringWidth(title);
		int messageWidth = font.getStringWidth(message);
		
		if(titleWidth > messageWidth) {
			maxWidth = titleWidth;
		}else {
			maxWidth = messageWidth;
		}
		
		maxWidth = maxWidth + 31;
		
		int x = (int) (ResolutionHelper.getWidth() - maxWidth) - 8;
		int y = ResolutionHelper.getHeight() - 29 - 8;

		animate.update();
		animate.setMax(messageWidth);

		if (!(time > timeLength)) {
			if (title != null && message != null) {
				Helper2D.drawRectangle(x - 2, y - 2, maxWidth + 4, 33, Style.getColorTheme(1).getRGB());
				Helper2D.drawRectangle(x, y, maxWidth, 29, Style.getColorTheme(7).getRGB());
				Helper2D.drawPicture(x + 5, y + 6, 17, 17, Color.WHITE.getRGB(), type.getIcon());
				font1.drawStringWidthShadow(title, x  + 26, y + 6, Style.getColorTheme(19).getRGB());
				font.drawStringWidthShadow(message, x  + 26, y + 18, Style.getColorTheme(19).getRGB());
			}
		}

	}
	
	public void show() {
		animate.reset();
	}
	
	public boolean isShown() {
		return !animate.hasFinished();
	}
	
	public Animate getAnimation() {
		return animate;
	}
}
