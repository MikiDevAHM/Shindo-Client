package me.miki.shindo.events.impl;

import me.miki.shindo.events.Event;

public class SendChatEvent extends Event {

    private String message;

    public SendChatEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
