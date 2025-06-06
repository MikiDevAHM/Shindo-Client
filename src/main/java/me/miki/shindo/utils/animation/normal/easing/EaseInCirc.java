package me.miki.shindo.utils.animation.normal.easing;

import me.miki.shindo.utils.animation.normal.Animation;

public class EaseInCirc extends Animation {

	public EaseInCirc(int ms, double endPoint) {
		super(ms, endPoint);
		this.reset();
	}

	@Override
	protected double getEquation(double x) {
	    double x1 = x / duration;
	    return -1 * (Math.sqrt(1 - x1 * x1) - 1);
	}
}
