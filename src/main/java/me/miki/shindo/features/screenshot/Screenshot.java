package me.miki.shindo.features.screenshot;

import java.io.File;

public class Screenshot {
	
	private String name;
	private File image;
	
	public Screenshot(File image) {
		this.image = image;
		this.name = image.getName().replace(".png", "");
	}

	public String getName() {
		return name;
	}

	public File getImage() {
		return image;
	}
}
