package me.miki.shindo.feature.mod.impl;

import me.miki.shindo.Shindo;
import me.miki.shindo.feature.mod.Mod;
import me.miki.shindo.feature.mod.Type;
import me.miki.shindo.feature.setting.Setting;

public class TablistMod extends Mod {
	
	public TablistMod() {
		super(
				"Tablist",
				"Modify the Tablist",
				Type.Tweaks
		);
		
		String[] mode = { "Modern", "Legacy" };
		Shindo.getInstance().getSettingsManager().addSetting(new Setting("Mode", this, "Modern", 0, mode));
		Shindo.getInstance().getSettingsManager().addSetting(new Setting("Show Ping", this, false));
	}

}
