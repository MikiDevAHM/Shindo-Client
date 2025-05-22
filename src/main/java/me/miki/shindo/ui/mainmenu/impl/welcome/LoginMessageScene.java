package me.miki.shindo.ui.mainmenu.impl.welcome;

import me.miki.shindo.Shindo;
import me.miki.shindo.helpers.TimerHelper;
import me.miki.shindo.helpers.render.BlurHelper;
import me.miki.shindo.ui.mainmenu.MainMenuScene;
import me.miki.shindo.ui.mainmenu.ShindoMainMenu;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;

import java.awt.*;

public class LoginMessageScene extends MainMenuScene {

    private final TimerHelper timer = new TimerHelper();

    public LoginMessageScene(ShindoMainMenu parent) {
        super(parent);

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        ScaledResolution sr = new ScaledResolution(mc);
        String message = "Finally, let's login to Minecraft!";

        BlurHelper.drawBlurScreen(14);

        if (timer.delay(1200)) {
            message = "Press SPACE to continue";
            timer.reset();
        }


        Shindo.getInstance().getFontHelper().size20.drawString(message, (float) sr.getScaledWidth() / 2,
                ((float) sr.getScaledHeight() / 2) - ((float) Shindo.getInstance().getFontHelper().size20.getFontHeight() / 2),
                new Color(255, 255, 255).getRGB());

    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {

        if (keyCode == Keyboard.KEY_SPACE) {
            if (Shindo.getInstance().getDownloadManager().isDownloaded()) {
                this.setCurrentScene(this.getSceneByClass(FirstLoginScene.class));
            } else {
                this.setCurrentScene(this.getSceneByClass(CheckingDataScene.class));
            }
        }

    }
}
