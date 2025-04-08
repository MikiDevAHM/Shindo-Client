package me.miki.shindo.features.security.impl;

import me.miki.shindo.Shindo;
import me.miki.shindo.events.EventTarget;
import me.miki.shindo.events.impl.PacketReceiveEvent;
import me.miki.shindo.features.security.Security;
import net.minecraft.network.play.server.S48PacketResourcePackSend;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class ResourcePackSecurity extends Security {

	@EventTarget
	public void onReceivePacket(PacketReceiveEvent event) {
        if (event.getPacket() instanceof S48PacketResourcePackSend) {
            S48PacketResourcePackSend pack = ((S48PacketResourcePackSend) event.getPacket());

            String url = pack.getURL();
            String hash = pack.getHash();

            if (url.toLowerCase().startsWith("level://")) {
                if(check(url, hash)) {
                	event.setCancelled(true);
                }
            }
        }
	}
	
    private boolean check(String url, String hash) {
        try {
            URI uri = new URI(url);

            String scheme = uri.getScheme();
            boolean isLevelProtocol = "level".equals(scheme);

            if (!("http".equals(scheme) || "https".equals(scheme) || isLevelProtocol)) {
                throw new URISyntaxException(url, "Wrong protocol");
            }

            url = URLDecoder.decode(url.substring("level://".length()), StandardCharsets.UTF_8.toString());

            if (isLevelProtocol && (url.contains("..") || !url.endsWith("/resources.zip"))) {
                throw new URISyntaxException(url, "Invalid levelstorage resource pack path");
            }

            return false;
        } catch (Exception e) {
            Shindo.getInstance().getMessageHelper().showMessage("Security", "Malicious Resource Pack File");
            return true;
        }
    }
}
