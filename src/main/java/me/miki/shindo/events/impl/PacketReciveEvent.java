package me.miki.shindo.events.impl;

import me.miki.shindo.events.Event;
import net.minecraft.network.Packet;

public class PacketReciveEvent extends Event {

    private Packet<?> packet;

    public PacketReciveEvent(Packet<?> packet) {
        this.packet = packet;
    }

    public Packet<?> getPacket() {
        return packet;
    }

    public void setPacket(Packet<?> packet) {
        this.packet = packet;
    }
}