package me.miki.shindo.features.profile.mainmenu.impl;

import net.minecraft.util.ResourceLocation;

public class DefaultBackground extends Background {

	private String name;
	private ResourceLocation image;
	
	public DefaultBackground(int id, String name, ResourceLocation image) {
		super(id,name);
		this.name = name;
		this.image = image;
	}
	
	@Override
	public String getName() {
		return name;
	}

	public ResourceLocation getImage() {
		return image;
	}
}