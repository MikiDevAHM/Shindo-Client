package me.miki.shindo.features.security.impl;

import me.miki.shindo.events.EventTarget;
import me.miki.shindo.events.impl.EventReceivePacket;
import me.miki.shindo.features.security.Security;
import net.minecraft.network.play.server.S2BPacketChangeGameState;

public class DemoSecurity extends Security {

    @EventTarget
    public void onReceivePacket(EventReceivePacket event) {
        if (event.getPacket() instanceof S2BPacketChangeGameState) {

            S2BPacketChangeGameState state = ((S2BPacketChangeGameState) event.getPacket());

            if (state.getGameState() == 5 && state.func_149137_d() == 0) {
                event.setCancelled(true);
            }
        }
    }
}