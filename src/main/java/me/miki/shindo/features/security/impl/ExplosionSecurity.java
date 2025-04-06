package me.miki.shindo.features.security.impl;

import me.miki.shindo.events.EventTarget;
import me.miki.shindo.events.impl.PacketReceiveEvent;
import me.miki.shindo.features.security.Security;
import net.minecraft.network.play.server.S27PacketExplosion;

public class ExplosionSecurity extends Security {

	@EventTarget
	public void onReceivePacket(PacketReceiveEvent event) {
		if(event.getPacket() instanceof S27PacketExplosion) {
			
			S27PacketExplosion explosion = ((S27PacketExplosion) event.getPacket());
			
			if(explosion.func_149149_c() >= Byte.MAX_VALUE || explosion.func_149144_d() >= Byte.MAX_VALUE || explosion.func_149149_c() >= Byte.MAX_VALUE) {
				event.setCancelled(true);
			}
		}
	}
}
