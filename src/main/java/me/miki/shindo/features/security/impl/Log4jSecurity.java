package me.miki.shindo.features.security.impl;

import me.miki.shindo.events.EventTarget;
import me.miki.shindo.events.impl.EventReceivePacket;
import me.miki.shindo.events.impl.EventSendChat;
import me.miki.shindo.features.security.Security;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S29PacketSoundEffect;
import net.minecraft.util.IChatComponent;

import java.util.regex.Pattern;

public class Log4jSecurity extends Security {

    private final Pattern pattern = Pattern.compile(".*\\$\\{[^}]*\\}.*");

    @EventTarget
    public void onReceivePacket(EventReceivePacket event) {

        if (event.getPacket() instanceof S29PacketSoundEffect) {

            S29PacketSoundEffect sound = (S29PacketSoundEffect) event.getPacket();
            String name = sound.getSoundName();

            if (pattern.matcher(name).matches()) {
                event.setCancelled(true);
            }
        }

        if (event.getPacket() instanceof S02PacketChat) {
            S02PacketChat chat = ((S02PacketChat) event.getPacket());
            IChatComponent component = chat.getChatComponent();

            if (pattern.matcher(component.getUnformattedText()).matches() || pattern.matcher(component.getFormattedText()).matches()) {
                event.setCancelled(true);
            }
        }
    }

    @EventTarget
    public void onChat(EventSendChat event) {
        if (pattern.matcher(event.getMessage()).matches()) {
            event.setCancelled(true);
        }
    }
}
