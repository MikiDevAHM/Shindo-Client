package me.miki.shindo.events.impl;

import me.miki.shindo.events.Event;
import net.minecraft.network.Packet;

public class EventReceivePacket extends Event {

    private Packet<?> packet;

    public EventReceivePacket(Packet<?> packet) {
        this.packet = packet;
    }

    public Packet<?> getPacket() {
        return packet;
    }

    public void setPacket(Packet<?> packet) {
        this.packet = packet;
    }
}