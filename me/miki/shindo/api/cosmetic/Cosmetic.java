package me.miki.shindo.api.cosmetic;

import net.minecraft.util.ResourceLocation;

public class Cosmetic {
	
	 public String name;
	 public String location;
	 public boolean state;
	 public float prog = 0.0F;
	 public float anim = 22.0F;
	 public float dragY;
	 public float dragX;

	 public Cosmetic(String name, String location) {
	    this.name = name;
	    this.location = location;
	 }

	 public void render(float ticks) {
	 }

	 public void toggle() {
	    this.state = !this.state;
	 }
	 
	 public String getName() {
		 return name;
	 }

	 public void setLocation(String in) {
	    this.location = in;
	 }

	 public ResourceLocation getLocation() {
	    return new ResourceLocation(this.location);
	 }
}

