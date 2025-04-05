package me.miki.shindo.events.impl;

import me.miki.shindo.events.Event;

public class RenderEvent extends Event {

    private float partialTicks;

    public RenderEvent(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks() {
        return partialTicks;
    }
}
