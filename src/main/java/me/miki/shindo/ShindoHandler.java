package me.miki.shindo;

import me.miki.shindo.events.EventTarget;
import me.miki.shindo.events.impl.KeyEvent;
import me.miki.shindo.events.impl.TickEvent;
import me.miki.shindo.events.impl.UpdateEvent;
import net.minecraft.client.Minecraft;

public class ShindoHandler
{

    private Minecraft mc =Minecraft.getMinecraft();

    private Shindo INSTANCE;

    public ShindoHandler() {
        INSTANCE = Shindo.getInstance();
    }

    public void init() {

    }

    @EventTarget
    public void onTick(TickEvent e) {

    }

    @EventTarget
    public void onKey(KeyEvent e) {

    }

    @EventTarget
    public void onUpdate(UpdateEvent e) {

    }
}
