package me.miki.shindo.features.nanovg.font;

import me.miki.shindo.helpers.IOHelper;
import me.miki.shindo.helpers.logger.ShindoLogger;
import org.lwjgl.nanovg.NanoVG;

import java.nio.ByteBuffer;


public class FontManager {

	public void init(long nvg) {
		for(Font f : Fonts.getFonts()) {
			loadFont(nvg, f);
		}
	}
	
	private void loadFont(long nvg, Font font) {
		
		if(font.isLoaded()) {
			return;
		}
		
		int loaded = -1;
		
		try {
			ByteBuffer buffer = IOHelper.resourceToByteBuffer(font.getResourceLocation());
			loaded = NanoVG.nvgCreateFontMem(nvg, font.getName(), buffer, false);
			font.setBuffer(buffer);
		} catch (Exception e) {
			ShindoLogger.error("Failed to load font", e);
		}
		
		if(loaded == -1) {
			throw new RuntimeException("Failed to init font " + font.getName());
		}else {
			font.setLoaded(true);
		}
	}
}
