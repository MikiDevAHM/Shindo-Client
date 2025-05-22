package me.miki.shindo.events.impl;

import me.eldodebug.soar.management.event.Event;

public class EventRenderTooltip extends Event {

	private float partialTicks;
	
	public EventRenderTooltip(float partialTicks) {
		this.partialTicks = partialTicks;
	}

	public float getPartialTicks() {
		return partialTicks;
	}
}
