package me.eldodebug.soar.discord;

import java.time.OffsetDateTime;

import me.eldodebug.soar.Shindo;
import me.eldodebug.soar.discord.ipc.IPCClient;
import me.eldodebug.soar.discord.ipc.IPCListener;
import me.eldodebug.soar.discord.ipc.entities.RichPresence;
import me.eldodebug.soar.discord.ipc.exceptions.NoDiscordClientException;

public class DiscordRPC {

	private IPCClient client;
	
	public void start() {
		
		client = new IPCClient(978250675576258610L);
		client.setListener(new IPCListener() {
			@Override
			public void onReady(IPCClient client) {
				
				RichPresence.Builder builder = new RichPresence.Builder();
				
				builder.setState("Playing Shindo Client v" + Shindo.getInstance().getVersion())
						.setStartTimestamp(OffsetDateTime.now())
						.setLargeImage("large");
				
				client.sendRichPresence(builder.build());
			}
		});
		
		try {
			client.connect();
		} catch (NoDiscordClientException e) {
			e.printStackTrace();
		}
	}
	
	public void stop() {
		client.close();
	}

	public IPCClient getClient() {
		return client;
	}
	
	public boolean isStarted() {
		return client != null;
	}
}
