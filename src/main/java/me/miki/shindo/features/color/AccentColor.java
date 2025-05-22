package me.miki.shindo.features.color;

import me.miki.shindo.helpers.ColorHelper;
import me.miki.shindo.helpers.animation.simple.SimpleAnimation;

import java.awt.*;

public class AccentColor {

	private SimpleAnimation animation = new SimpleAnimation();
	
	private String name;
	private Color color1, color2;
	
	public AccentColor(String name, Color color1, Color color2) {
		this.name = name;
		this.color1 = color1;
		this.color2 = color2;
	}

	public String getName() {
		return name;
	}

	public Color getColor1() {
		return color1;
	}

	public Color getColor2() {
		return color2;
	}
	
	public Color getInterpolateColor() {
		return ColorHelper.interpolateColors(15, 0, color1, color2);
	}
	
	public Color getInterpolateColor(int index) {
		return ColorHelper.interpolateColors(15, index, color1, color2);
	}

	public SimpleAnimation getAnimation() {
		return animation;
	}
}
