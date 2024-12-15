package me.miki.shindo;

import me.miki.shindo.api.logger.EcoLogManager;
import me.miki.shindo.api.logger.enums.LoggerCasualEnum;
import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;
import net.arikia.dev.drpc.DiscordUser;
import net.arikia.dev.drpc.callbacks.ReadyCallback;

public class DiscordIPC {
	
	private static final EcoLogManager logger = EcoLogManager.getLogger();
	
	private boolean running = true;
	private long created = 0;
	private String appID = "978250675576258610";
	
	
	public void start() {
		
		this.created = System.currentTimeMillis();
		DiscordEventHandlers handlers = new DiscordEventHandlers.Builder().setReadyEventHandler(new ReadyCallback() {
			
			@Override
			public void apply(DiscordUser user) {
				if (user.discriminator.equals("0")) {
					logger.log(LoggerCasualEnum.ProjectInfo, "Bem Vindo(a) " + user.username + "!");
				} else {
					logger.log(LoggerCasualEnum.ProjectInfo, "Bem Vindo(a) " + user.username + "#" + user.discriminator + "!");
				}
				
				update("Iniciando...", "");
			}
		}).build();
		
		DiscordRPC.discordInitialize(appID, handlers, true);
		
		new Thread("Discord RPC Callback") {
			
			@Override
			public void run() {
				
				while(running) {
					DiscordRPC.discordRunCallbacks();
				}
			}
		}.start();

	}
	public void shutdown() {
		running = false;
		DiscordRPC.discordShutdown();
	}
	
	public void update(String firstLine, String secondLine) {
		DiscordRichPresence.Builder b = new DiscordRichPresence.Builder(secondLine);
		b.setBigImage("large", "");
		b.setDetails(firstLine);
		b.setStartTimestamps(created);
		
		DiscordRPC.discordUpdatePresence(b.build());
	}
	


}
