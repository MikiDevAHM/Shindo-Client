package me.miki.shindo.events.impl;


import me.miki.shindo.events.Event;

public class EventRender3D extends Event {

	private float partialTicks;
	
	public EventRender3D(float partialTicks) {
		this.partialTicks = partialTicks;
	}

	public float getPartialTicks() {
		return partialTicks;
	}
}