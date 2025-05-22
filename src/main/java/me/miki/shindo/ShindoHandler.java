package me.miki.shindo;

import me.miki.shindo.events.EventTarget;
import me.miki.shindo.events.impl.EventKey;
import me.miki.shindo.events.impl.EventReceivePacket;
import me.miki.shindo.events.impl.EventTick;
import me.miki.shindo.helpers.OptifineHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.S2EPacketCloseWindow;
import org.lwjgl.input.Keyboard;

public class ShindoHandler {

    private final Minecraft mc = Minecraft.getMinecraft();

    private final Shindo INSTANCE;

    public ShindoHandler() {
        INSTANCE = Shindo.getInstance();
    }

    public void init() {
    }

    @EventTarget
    public void onTick(EventTick e) {
        OptifineHelper.disableFastRender();
    }

    @EventTarget
    public void onKey(EventKey e) {
        if (Keyboard.isKeyDown(Shindo.getInstance().getOptionManager().getOptionByName("ModMenu Keybinding").getKey())) {
            mc.displayGuiScreen(INSTANCE.getHudEditor());
        }
    }

    @EventTarget
    public void onReceivePacket(EventReceivePacket event) {
        if (event.getPacket() instanceof S2EPacketCloseWindow && mc.currentScreen instanceof ModMenu) {
            event.setCancelled(true);
        }
    }
}
