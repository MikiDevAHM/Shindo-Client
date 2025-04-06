package me.miki.shindo.features.security.impl;

import java.util.regex.Pattern;

import me.miki.shindo.events.EventTarget;
import me.miki.shindo.events.impl.PacketReceiveEvent;
import me.miki.shindo.events.impl.SendChatEvent;
import me.miki.shindo.features.security.Security;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S29PacketSoundEffect;
import net.minecraft.util.IChatComponent;

public class Log4jSecurity extends Security {

	private Pattern pattern = Pattern.compile(".*\\$\\{[^}]*\\}.*");
	
	@EventTarget
	public void onReceivePacket(PacketReceiveEvent event) {
		
		 if (event.getPacket() instanceof S29PacketSoundEffect) {
			 
			 S29PacketSoundEffect sound = (S29PacketSoundEffect) event.getPacket();
			 String name = sound.getSoundName();
	            
			 if(pattern.matcher(name).matches()) {
				 event.setCancelled(true);
			 }
		 }
		 
		 if (event.getPacket() instanceof S02PacketChat) {
			 S02PacketChat chat = ((S02PacketChat) event.getPacket());
			 IChatComponent component = chat.getChatComponent();
			 
			 if(pattern.matcher(component.getUnformattedText()).matches() || pattern.matcher(component.getFormattedText()).matches()) {
				 event.setCancelled(true);
			 }
		 }
	}
	
	@EventTarget
	public void onChat(SendChatEvent event) {
		if(pattern.matcher(event.getMessage()).matches()) {
			event.setCancelled(true);
		}
	}
}
