package me.miki.shindo.ui.components.buttons;

import java.awt.Color;

import me.miki.shindo.Shindo;
import me.miki.shindo.util.MathHelper;
import me.miki.shindo.util.animation.Animate;
import me.miki.shindo.util.animation.Easing;
import me.miki.shindo.util.render.Helper2D;

public class TextButton{
	
	
	private final Animate animate = new Animate(); 
	
	private final String text;
	private int x,y;
	private final int w, h;
	
	
	public TextButton(String text, int x, int y) {
		
		this.text = text;
		this.x = x;
		this.y = y;
		this.w = 150;
		this.h = 20;
		
		animate.setEase(Easing.LINEAR).setMin(0).setMax(25).setSpeed(200);
	}
	
	public void renderButton(int x, int y, int mouseX, int mouseY) {
        this.x = x;
        this.y = y;
        
        animate.update().setReversed(!isHovered(mouseX, mouseY));
        
        Helper2D.drawRoundedRectangle(x, y, w, h, 2, 
        		new Color(255, 255, 255, animate.getValueI() + 30).getRGB(), 
        		Shindo.getInstance().getOptionManager().getOptionByName("Rounded Corners").isCheckToggled() ? 0 : -1
        );
        
        Shindo.getInstance().getFontHelper().size20.drawString(text, x + w / 2f - Shindo.getInstance().getFontHelper().size20.getStringWidth(text) / 2f, y + h / 2f - 4, -1);
        
        
		
	}
	
    public boolean isHovered(int mouseX, int mouseY) {
        return MathHelper.withinBox(x, y, w, h, mouseX, mouseY);
    }

    public int getW() {
        return w;
    }

    public int getH() {
        return h;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getText() {
        return text;
    }
}
