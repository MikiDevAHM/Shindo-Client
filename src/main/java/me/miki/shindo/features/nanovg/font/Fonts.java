package me.miki.shindo.features.nanovg.font;

import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.Arrays;

public class Fonts {
	
	private static final String PATH = "shindo/fonts/";
	
	public static final Font REGULAR = new Font("regular", new ResourceLocation(PATH + "MiSans-Regular.ttf"));
	public static final Font MEDIUM = new Font("medium", new ResourceLocation(PATH + "MiSans-Medium.ttf"));
	public static final Font DEMIBOLD = new Font("demi-bold", new ResourceLocation(PATH + "MiSans-Demibold.ttf"));
	public static final Font ICON = new Font("icon", new ResourceLocation(PATH + "Icon.ttf"));

	public static ArrayList<Font> getFonts() {
		return new ArrayList<Font>(Arrays.asList(MEDIUM, DEMIBOLD, REGULAR, ICON));
	}
}
