package me.miki.shindo.management.event.impl;

import me.miki.shindo.management.event.Event;
import net.minecraft.client.entity.AbstractClientPlayer;

public class EventFovUpdate extends Event {

	private AbstractClientPlayer entity;
	private float fov;
	
	public EventFovUpdate(AbstractClientPlayer entity, float fov) {
		this.entity = entity;
		this.fov = fov;
	}

	public float getFov() {
		return fov;
	}

	public void setFov(float fov) {
		this.fov = fov;
	}

	public AbstractClientPlayer getEntity() {
		return entity;
	}
}