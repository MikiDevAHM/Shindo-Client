package me.miki.shindo.helpers.animation.normal.easing;

import me.miki.shindo.helpers.animation.normal.Animation;

public class EaseOutExpo extends Animation {

	public EaseOutExpo(int ms, double endPoint) {
		super(ms, endPoint);
		this.reset();
	}

	@Override
	protected double getEquation(double x) {
	    return (x == duration) ? 1 : (-Math.pow(2, -10 * x / duration) + 1);
	}
}
