package me.miki.shindo.features.mods.impl;

import me.miki.shindo.discord.DiscordRPC;
import me.miki.shindo.features.mods.Mod;
import me.miki.shindo.features.mods.Type;

public class DiscordRPCMod extends Mod {

    private DiscordRPC discord = new DiscordRPC();

    public DiscordRPCMod() {
        super(
                "DiscordRPC",
                "Discord Rich Presence.",
                Type.Tweaks
        );
    }
    @Override
    public void onEnable() {
        super.onEnable();
        //discord.start();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        if(discord.isStarted()) {
            discord.stop();
        }
    }
}
