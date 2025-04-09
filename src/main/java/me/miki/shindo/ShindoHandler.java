package me.miki.shindo;

import me.miki.shindo.events.EventTarget;
import me.miki.shindo.events.impl.KeyEvent;
import me.miki.shindo.events.impl.PacketReceiveEvent;
import me.miki.shindo.events.impl.TickEvent;
import me.miki.shindo.events.impl.UpdateEvent;
import me.miki.shindo.helpers.OptifineHelper;
import me.miki.shindo.helpers.TargetHelper;
import me.miki.shindo.ui.modmenu.ModMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.S2EPacketCloseWindow;
import org.lwjgl.input.Keyboard;

public class ShindoHandler
{

    private final Minecraft mc = Minecraft.getMinecraft();

    private Shindo INSTANCE;

    public ShindoHandler() {
        INSTANCE = Shindo.getInstance();
    }

    public void init() {
        OptifineHelper.removeOptifineZoom();
    }

    @EventTarget
    public void onTick(TickEvent e) {
        OptifineHelper.disableFastRender();
    }

    @EventTarget
    public void onKey(KeyEvent e) {
        if (Keyboard.isKeyDown(Shindo.getInstance().getOptionManager().getOptionByName("ModMenu Keybinding").getKey())) {
            mc.displayGuiScreen(INSTANCE.getHudEditor());
        }
    }

    @EventTarget
    public void onUpdate(UpdateEvent e) {
        TargetHelper.onUpdate();
    }

    @EventTarget
    public void onReceivePacket(PacketReceiveEvent event) {
        if (event.getPacket() instanceof S2EPacketCloseWindow && mc.currentScreen instanceof ModMenu) {
            event.setCancelled(true);
        }
    }
}
