package me.miki.shindo.gui.minecraft.ingame;

import me.miki.shindo.gui.minecraft.ingame.impl.GameScene;
import net.minecraft.client.gui.GuiScreen;

import java.awt.*;
import java.util.ArrayList;

public class GuiShindoGameMenu extends GuiScreen {

    private GameMenuScene currentScene;

    private final ArrayList<GameMenuScene> scenes = new ArrayList<GameMenuScene>();



    public GuiShindoGameMenu() {

        scenes.add(new GameScene(this));

        currentScene = getSceneByClass(GameScene.class);
    }

    @Override
    public void initGui() {
        currentScene.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        currentScene.drawScreen(mouseX, mouseY, partialTicks);
    }






    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
       currentScene.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        currentScene.keyTyped(typedChar, keyCode);
    }

    public GameMenuScene getCurrentScene() {
        return currentScene;
    }

    public void setCurrentScene(GameMenuScene currentScene) {

        if(this.currentScene != null) {
            this.currentScene.onSceneClosed();
        }

        this.currentScene = currentScene;

        if(this.currentScene != null) {
            this.currentScene.initScene();
        }
    }


    public GameMenuScene getSceneByClass(Class<? extends GameMenuScene > clazz) {

        for(GameMenuScene s : scenes) {
            if(s.getClass().equals(clazz)) {
                return s;
            }
        }

        return null;
    }

    public Color getBackgroundColor() {
        return new Color(230, 230, 230, 120);
    }
}