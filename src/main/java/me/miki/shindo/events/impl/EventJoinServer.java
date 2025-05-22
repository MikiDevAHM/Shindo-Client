package me.miki.shindo.events.impl;

import me.miki.shindo.events.Event;

public class EventJoinServer extends Event {

	private String ip;
	
	public EventJoinServer(String ip) {
		this.ip = ip;
	}

	public String getIp() {
		return ip;
	}
}
