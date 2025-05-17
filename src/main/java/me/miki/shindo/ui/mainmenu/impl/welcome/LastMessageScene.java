package me.miki.shindo.ui.mainmenu.impl.welcome;

import me.miki.shindo.Shindo;
import me.miki.shindo.helpers.TimerHelper;
import me.miki.shindo.helpers.animation.Animate;
import me.miki.shindo.helpers.render.BlurHelper;
import me.miki.shindo.ui.mainmenu.MainMenuScene;
import me.miki.shindo.ui.mainmenu.ShindoMainMenu;
import me.miki.shindo.ui.mainmenu.impl.MainScene;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;

import java.awt.*;

public class LastMessageScene extends MainMenuScene {

    String compMessage = "Setup is complete!";
    String welcomeMessage = "Welcome to Shindo Client!";
    String continueMessage = "Press SPACE To Continue";
    private Animate fadeAnimation, blurAnimation;
    private int step;
    private String message;
    private final TimerHelper timer = new TimerHelper();

    public LastMessageScene(ShindoMainMenu parent) {
        super(parent);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        ScaledResolution sr = new ScaledResolution(mc);

        BlurHelper.drawBlurScreen(1 + blurAnimation.getValueI());


        if (timer.delay(1200) && message == null) {
            message = compMessage;
        }

        if (timer.delay(1200) && message.equals(compMessage)) {
            message = welcomeMessage;
        }

        if (timer.delay(1200) && message.equals(welcomeMessage)) {
            message = continueMessage;
        }

        Shindo.getInstance().getFontHelper().size20.drawString(message, (float) sr.getScaledWidth() / 2,
                ((float) sr.getScaledHeight() / 2) - ((float) Shindo.getInstance().getFontHelper().size20.getFontHeight() / 2),
                new Color(255, 255, 255).getRGB());

    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        if (keyCode == Keyboard.KEY_SPACE) {
            Shindo.getInstance().getShindoAPI().createFirstLoginFile();
            this.setCurrentScene(this.getSceneByClass(MainScene.class));
        }
    }
}