package me.miki.shindo.gui.minecraft.ingame;

import me.miki.shindo.utils.animation.simple.SimpleAnimation;
import net.minecraft.client.Minecraft;

import java.awt.*;

public class GameMenuScene {
    public Minecraft mc = Minecraft.getMinecraft();
    private GuiShindoGameMenu parent;

    private SimpleAnimation animation = new SimpleAnimation();

    public GameMenuScene(GuiShindoGameMenu parent) {
        this.parent = parent;
    }

    public void initScene() {}

    public void initGui() {}

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {}

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {}

    public void keyTyped(char typedChar, int keyCode) {}

    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {}

    public void handleInput() {}

    public void onGuiClosed() {}

    public void onSceneClosed() {}

    public GuiShindoGameMenu getParent() {
        return parent;
    }

    public void setCurrentScene(GameMenuScene scene) {
        parent.setCurrentScene(scene);
    }

    public Color getBackgroundColor() {
        return parent.getBackgroundColor();
    }

    public SimpleAnimation getAnimation() {
        return animation;
    }

    public GameMenuScene getSceneByClass(Class<? extends GameMenuScene> clazz) {
        return parent.getSceneByClass(clazz);
    }
}
