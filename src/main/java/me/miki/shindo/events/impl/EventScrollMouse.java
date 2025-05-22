package me.miki.shindo.events.impl;


import me.miki.shindo.events.Event;

public class EventScrollMouse extends Event {

	private int amount;
	
	public EventScrollMouse(int amount) {
		this.amount = amount;
	}

	public int getAmount() {
		return amount;
	}
}