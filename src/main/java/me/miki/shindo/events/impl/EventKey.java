package me.miki.shindo.events.impl;

import me.miki.shindo.events.Event;

public class EventKey extends Event {

    private int keyCode;

    public EventKey(int keyCode) {
        this.keyCode = keyCode;
    }

    public int getKeyCode() {
        return keyCode;
    }

    public void setKeyCode(int keyCode) {
        this.keyCode = keyCode;
    }
}