package me.miki.shindo.features.security.impl;

import me.miki.shindo.events.EventTarget;
import me.miki.shindo.events.impl.PacketReciveEvent;
import me.miki.shindo.features.security.Security;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

public class TeleportSecurity extends Security {

	@EventTarget
	public void onReceivePacket(PacketReciveEvent event) {
		if (event.getPacket() instanceof S08PacketPlayerPosLook) {
			
			S08PacketPlayerPosLook pos = ((S08PacketPlayerPosLook) event.getPacket());
			
			if(Math.abs(pos.getX()) > 1E+9 || Math.abs(pos.getY()) > 1E+9 || Math.abs(pos.getZ()) > 1E+9) {
				event.setCancelled(true);
			}
		}
	}
}
