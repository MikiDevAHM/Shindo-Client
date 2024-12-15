package me.miki.shindo.util;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import me.miki.shindo.Shindo;
import me.miki.shindo.event.EventTarget;
import me.miki.shindo.event.impl.tick.ClientTickEvent;
import net.minecraft.client.Minecraft;

public class ScreenHelper {
	private static Minecraft mc = Minecraft.getMinecraft();
	   boolean lastFullscreen = false;
	
	public void fix(boolean fullscreen) {
		try {
			if (fullscreen) {
				System.setProperty("org.lwjgl.opengl.Window.undecorated", "true");
		        Display.setDisplayMode(Display.getDesktopDisplayMode());
		        Display.setLocation(0, 0);
		        Display.setFullscreen(false);
		        Display.setResizable(false);
		    } else {
		        System.setProperty("org.lwjgl.opengl.Window.undecorated", "false");
		        Display.setDisplayMode(new DisplayMode(mc.displayWidth, mc.displayHeight));
		        Display.setResizable(true);
		    }
		} catch (LWJGLException var3) {
		         var3.printStackTrace();
		}
	}
	
	@EventTarget
	public void tick(ClientTickEvent event) {
		if(Shindo.getInstance().getOptionManager().getOptionByName("Bordeless FullScreen").isCheckToggled()) {
			boolean fullScreenNow = mc.isFullScreen();
		    if (this.lastFullscreen != fullScreenNow) {
		    	this.fix(fullScreenNow);
		        this.lastFullscreen = fullScreenNow;
		    }
		}
	}
}
