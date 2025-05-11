package me.miki.shindo.events.impl;

import me.miki.shindo.events.Event;

public class MouseEvent extends Event {

    private final int button;

    public MouseEvent(int button) {
        this.button = button;
    }

    public int getButton() {
        return button;
    }
}