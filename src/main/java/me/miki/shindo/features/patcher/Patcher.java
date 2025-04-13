package me.miki.shindo.features.patcher;

import me.miki.shindo.events.EventManager;

public class Patcher {

    public Patcher() {
        EventManager.register(this);
    }
}
