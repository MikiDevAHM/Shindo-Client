package me.miki.shindo.ui.mainmenu;

import net.minecraft.client.Minecraft;

import java.awt.*;

public class MainMenuScene {
    public Minecraft mc = Minecraft.getMinecraft();
    private ShindoMainMenu parent;

    public MainMenuScene(ShindoMainMenu parent) {
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

    public ShindoMainMenu getParent() {
        return parent;
    }

    public void setCurrentScene(MainMenuScene scene) {
        parent.setCurrentScene(scene);
    }

    public Color getBackgroundColor() {
        return parent.getBackgroundColor();
    }

    public MainMenuScene getSceneByClass(Class<? extends MainMenuScene> clazz) {
        return parent.getSceneByClass(clazz);
    }
}