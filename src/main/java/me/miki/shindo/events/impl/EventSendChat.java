package me.miki.shindo.events.impl;

import me.miki.shindo.events.Event;

public class EventSendChat extends Event {

    private final String message;

    public EventSendChat(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
