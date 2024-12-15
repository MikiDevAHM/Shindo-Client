package me.miki.shindo.ui.components.buttons;

import java.awt.Color;

import me.miki.shindo.Shindo;
import me.miki.shindo.util.MathHelper;
import me.miki.shindo.util.animation.Animate;
import me.miki.shindo.util.animation.Easing;
import me.miki.shindo.util.render.Helper2D;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public class GuiIconButton extends GuiButton {
	
	private final Animate animate = new Animate();

    private int id;
    private String icon;
    public GuiIconButton(int id, int x, int y, String icon) {
    	super(id, x, y, icon);
    	this.icon = icon;
        this.xPosition = x;
        this.yPosition = y;
        this.width = 20;
        this.height = 20;
        animate.setEase(Easing.LINEAR).setMin(0).setMax(25).setSpeed(200);
    }
    

    /**
     * Renders the button with the position, width and height taken from the constructor
     *
     * @param mouseX The current X position of the mouse
     * @param mouseY The current Y position of the mouse
     */

    public void drawButton(Minecraft mc, int mouseX, int mouseY) {

        animate.update().setReversed(!isHovered(mouseX, mouseY));

        Helper2D.drawRoundedRectangle(xPosition, yPosition, width, height, 2,
                new Color(255, 255, 255, animate.getValueI() + 30).getRGB(), 
                Shindo.getInstance().getOptionManager().getOptionByName("Rounded Corners").isCheckToggled() ? 0 : -1
        );

        Helper2D.drawPicture(xPosition, yPosition, width, height, 0xffffffff, "icon/" + icon);
    }

    public boolean isHovered(int mouseX, int mouseY) {
        return MathHelper.withinBox(xPosition, yPosition, width, height, mouseX, mouseY);
    }

    public int getW() {
        return width;
    }

    public int getH() {
        return height;
    }

    public int getX() {
        return xPosition;
    }

    public int getY() {
        return yPosition;
    }

    public String getIcon() {
        return icon;
    }
}
