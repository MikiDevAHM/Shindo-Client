package me.miki.shindo.feature.mod.impl;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import me.miki.shindo.Shindo;
import me.miki.shindo.event.EventTarget;
import me.miki.shindo.event.impl.KeyEvent;
import me.miki.shindo.event.impl.render.EntityViewRenderEvent;
import me.miki.shindo.event.impl.tick.ClientTickEvent;
import me.miki.shindo.feature.mod.Mod;
import me.miki.shindo.feature.mod.Type;
import me.miki.shindo.feature.setting.Setting;

public class FreelookMod extends Mod {

    public static boolean cameraToggled = false;
	public static float cameraYaw;
    public static float cameraPitch;

    public FreelookMod() {
        super(
                "Freelook",
                "Allows you to see a 360 view around your Player.",
                Type.Mechanic
        );

        Shindo.getInstance().getSettingsManager().addSetting(new Setting("Keybinding", this, Keyboard.KEY_R));
    }

    @EventTarget
    public void onKey(KeyEvent e) {
        if(Keyboard.isKeyDown(getKey()) && !cameraToggled){
            cameraYaw = mc.thePlayer.rotationYaw + 180;
            cameraPitch = mc.thePlayer.rotationPitch;
            cameraToggled = true;
            mc.gameSettings.thirdPersonView = 1;
        }
    }

    @EventTarget
    public void onTick(ClientTickEvent e){
        if(!Keyboard.isKeyDown(getKey()) && cameraToggled) {
            cameraToggled = false;
            mc.gameSettings.thirdPersonView = 0;
        }
    }



    private int getKey(){
        return Shindo.getInstance().getSettingsManager().getSettingByModAndName(getName(), "Keybinding").getKey();
    }
}
