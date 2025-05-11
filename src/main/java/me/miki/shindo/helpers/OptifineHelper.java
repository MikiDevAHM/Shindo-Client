package me.miki.shindo.helpers;

import me.miki.shindo.helpers.logger.ShindoLogger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;

public class OptifineHelper {

    private static final Minecraft mc = Minecraft.getMinecraft();


    public static void disableFastRender() {
        mc.gameSettings.ofFastRender = false;
        mc.gameSettings.useVbo = true;
        mc.gameSettings.fboEnable = true;
    }

    public static void removeOptifineZoom() {
        try {
            unregisterKeybind((KeyBinding) GameSettings.class.getField("ofKeyBindZoom").get(mc.gameSettings));
        } catch (Exception e) {
            ShindoLogger.error("Failed to unregister zoom key", e);
        }
    }

    private static void unregisterKeybind(KeyBinding key) {
        if (Arrays.asList(mc.gameSettings.keyBindings).contains(key)) {
            mc.gameSettings.keyBindings = ArrayUtils.remove(mc.gameSettings.keyBindings, Arrays.asList(mc.gameSettings.keyBindings).indexOf(key));
            key.setKeyCode(0);
        }
    }
}
