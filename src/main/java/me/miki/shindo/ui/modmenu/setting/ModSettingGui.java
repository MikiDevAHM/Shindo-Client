package me.miki.shindo.ui.modmenu.setting;

import java.awt.Color;

import me.miki.shindo.features.mods.Mod;
import me.miki.shindo.helpers.render.RoundedHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

public class ModSettingGui {
	public Mod mod;
	public int x,y,w,h;
	
	public ModSettingGui(int x, int y, int w, int h, Mod mod) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.mod = mod;
	}
	
	
	public void render() {
		RoundedHelper.drawRoundedRect(x , y , x+w , y+h, 8, new Color(245, 242, 233,255).getRGB());
		RoundedHelper.drawRoundedRect(x , y , x+w , y+10, 8, new Color(138, 66, 88,255).getRGB());
		Minecraft.getMinecraft().fontRendererObj.drawString(mod.getName() + " : " + mod.isToggled(), x + 3, y + 13, new Color(0,0,0,255).getRGB());
		Minecraft.getMinecraft().fontRendererObj.drawString(mod.getDescription(), x + 3, y + 23, new Color(0,0,0,255).getRGB());
	}
	
	
	
	
	
}
