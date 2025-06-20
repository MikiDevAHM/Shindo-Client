package me.miki.shindo.injection.mixin.mixins.gui;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import me.miki.shindo.gui.GuiFixConnecting;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.ServerData;

@Mixin(GuiMultiplayer.class)
public class MixinGuiMultiplayer extends GuiScreen {
	
	/**
     * @author
     * @reason
     */
    @Overwrite
    private void connectToServer(ServerData server){
        mc.displayGuiScreen(new GuiFixConnecting(this, mc, server));
    }
}
