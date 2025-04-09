package me.miki.shindo.features.command.impl;

import me.miki.shindo.Shindo;
import me.miki.shindo.features.command.Command;
import me.miki.shindo.helpers.file.FileManager;
import me.miki.shindo.helpers.logger.ShindoLogger;
import me.miki.shindo.helpers.transferable.FileTransferable;
import net.minecraft.util.ChatComponentText;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class ScreenshotCommand extends Command {

	public ScreenshotCommand() {
		super("screenshot");
	}

	@Override
	public void onCommand(String message) {
		
		FileManager fileManager = Shindo.getInstance().getFileManager();
		String[] args = message.split(" ");

		File file = new File(fileManager.getScreenshotDir(),args.length == 0 ? args[1] : "");

		if(args[0].equals("list")) {
			mc.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText("Files:"));
			for(File f : file.listFiles()) {
				mc.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(" - " + f.getName()));
			}
		}

		if(args.length < 1) {
			return;
		}
		
		if(args[0].equals("open")) {
			try {
				Desktop.getDesktop().open(file);
			} catch (IOException e) {
				ShindoLogger.error("Could not open screenshot.", e);
			}
		}
		
		if(args[0].equals("copy")) {
            FileTransferable selection = new FileTransferable(file);
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
		}
		
		if(args[0].equals("del")) {
			file.delete();
			mc.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(args[1] + " has been deleted"));
		}


	}
}
