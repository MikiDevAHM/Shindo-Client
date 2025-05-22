package me.miki.shindo.events.impl;

import me.miki.shindo.events.Event;
import net.minecraft.client.shader.ShaderGroup;

import java.util.ArrayList;
import java.util.List;

public class EventShader extends Event {
	
	private List<ShaderGroup> groups = new ArrayList<ShaderGroup>();

	public List<ShaderGroup> getGroups() {
		return groups;
	}
}
