package me.miki.shindo.ui.modmenu.setting;

import me.miki.shindo.features.mods.Mod;
import me.miki.shindo.ui.modmenu.ClickGui;
import me.miki.shindo.ui.modmenu.comp.ModButton;

import java.util.ArrayList;


public class ModSettingManager {
	public ArrayList<ModSettingGui> modSettingRender = new ArrayList<>();
	
	public static Mod mod = null;
	
	
	public ModSettingManager(int centerW, int centerH) {
		//reset
		modSettingRender.removeAll(modSettingRender);
		
		//add
		System.out.println(centerW);
		for(ModButton modButton : ClickGui.modButtonToRender) {
			this.modSettingRender.add(new ModSettingGui(centerW+205, centerH-100, 200, 200, modButton.mod));
		}
	}
	
	public void render() {
		if(mod != null) {
			for(ModSettingGui modSettingGui : modSettingRender) {
				if(modSettingGui.mod == mod) {
					modSettingGui.render();
					continue;
				}
			}
		}
		
	}
	


}
