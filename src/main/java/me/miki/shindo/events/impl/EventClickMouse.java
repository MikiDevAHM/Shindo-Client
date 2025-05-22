package me.miki.shindo.events.impl;

import me.miki.shindo.events.Event;

public class EventClickMouse extends Event {

    private final int button;

    public EventClickMouse(int button) {
        this.button = button;
    }

    public int getButton() {
        return button;
    }
}