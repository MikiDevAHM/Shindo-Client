package me.miki.shindo.management.cape.impl;

import me.miki.shindo.management.cape.CapeCategory;
import me.miki.shindo.utils.animation.simple.SimpleAnimation;
import net.minecraft.util.ResourceLocation;

public class Cape {
	
	private String name;
	private ResourceLocation cape;
	private CapeCategory category;
	
	private SimpleAnimation animation = new SimpleAnimation();
	
	public Cape(String name, ResourceLocation cape, CapeCategory category) {
		this.name = name;
		this.category = category;
		this.cape = cape;
	}

	public String getName() {
		return name;
	}

	public CapeCategory getCategory() {
		return category;
	}

	public SimpleAnimation getAnimation() {
		return animation;
	}

	public ResourceLocation getCape() {
		return cape;
	}
}
