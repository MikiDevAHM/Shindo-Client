package me.miki.shindo.events.impl;

import me.eldodebug.soar.management.event.Event;

public class EventRenderVisualizer extends Event {

	private float partialTicks;
	
	public EventRenderVisualizer(float partialTicks) {
		this.partialTicks = partialTicks;
	}

	public float getPartialTicks() {
		return partialTicks;
	}
}
