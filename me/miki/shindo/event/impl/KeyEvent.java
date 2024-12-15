package me.miki.shindo.event.impl;

import me.miki.shindo.event.Event;

public class KeyEvent extends Event {
	
	private final int key;
	
	public KeyEvent(int key) {
		this.key = key;
	}
	
	public int getKey() {
		return key;
	}

}
