package me.miki.shindo.helpers.animation.normal.easing;

import me.miki.shindo.helpers.animation.normal.Animation;

public class EaseInSine extends Animation {

	public EaseInSine(int ms, double endPoint) {
		super(ms, endPoint);
		this.reset();
	}

	@Override
	protected double getEquation(double x) {
	    return -1 * Math.cos(x / duration * (Math.PI / 2)) + 1;
	}
}
