package me.miki.shindo.events.impl;

import me.miki.shindo.events.Event;

public class EventHurtCamera extends Event {

	private float intensity;
	
	public EventHurtCamera() {
		this.intensity = 1.0F;
	}

	public float getIntensity() {
		return intensity;
	}

	public void setIntensity(float intensity) {
		this.intensity = intensity;
	}
}
