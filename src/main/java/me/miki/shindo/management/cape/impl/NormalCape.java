package me.miki.shindo.management.cape.impl;

import me.miki.shindo.management.cape.CapeCategory;
import net.minecraft.util.ResourceLocation;

public class NormalCape extends Cape {

	private ResourceLocation sample;
	
	public NormalCape(String name, ResourceLocation sample, ResourceLocation cape, CapeCategory category) {
		super(name, cape, category);
		this.sample = sample;
	}

	public ResourceLocation getSample() {
		return sample;
	}
}
