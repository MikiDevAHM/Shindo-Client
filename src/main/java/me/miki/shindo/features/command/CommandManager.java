package me.miki.shindo.features.command;

import me.miki.shindo.events.EventManager;
import me.miki.shindo.events.EventTarget;
import me.miki.shindo.events.impl.SendChatEvent;
import me.miki.shindo.features.command.impl.ScreenshotCommand;

import java.util.ArrayList;

public class CommandManager {

	private ArrayList<Command> commands = new ArrayList<Command>();
	
	public CommandManager() {
		
		commands.add(new ScreenshotCommand());
		EventManager.register(this);
	}
	
	@EventTarget
	public void onSendChat(SendChatEvent event) {
		
		if(event.getMessage().startsWith(".scmd")) {
			
			event.setCancelled(true);
			
			String[] args = event.getMessage().split(" ");
			
			if(args.length > 1) {
				for(Command c : commands) {
					if(args[1].equals(c.getPrefix())) {
						c.onCommand(event.getMessage().replace(".scmd ", "").replace(args[1] + " ", ""));
					}
				}
			}
		}
	}

	public ArrayList<Command> getCommands() {
		return commands;
	}
}
