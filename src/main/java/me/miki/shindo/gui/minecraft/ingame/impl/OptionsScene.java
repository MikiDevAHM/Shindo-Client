package me.miki.shindo.gui.minecraft.ingame.impl;

import me.miki.shindo.gui.minecraft.ingame.GameMenuScene;
import me.miki.shindo.gui.minecraft.ingame.GuiShindoGameMenu;
import me.miki.shindo.management.nanovg.NanoVGManager;
import me.miki.shindo.management.nanovg.font.Fonts;

import java.awt.*;

public class OptionsScene extends GameMenuScene {


    public OptionsScene(GuiShindoGameMenu parent) {
        super(parent);
    }

    @Override
    public void initGui() {

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

    }

    private void drawNanoVG(NanoVGManager nvg) {


    }

    private void drawSlider(NanoVGManager nvg, String str, Float x, Float y, Float width, Float height, Float value) {

    }


    private void drawButton(NanoVGManager nvg, String str, String icon, Float x, Float y, Float width, Float height){
        nvg.drawRoundedRect(x, y, width , height, 6, new Color(230, 230, 230, 80));
        float startX = x + width / 2 - (nvg.getTextWidth(str, 9.5F, Fonts.MEDIUM) + 14) /2;
        nvg.drawText(icon, x - startX, y + 6.5F, Color.WHITE, 9.5F, Fonts.LEGACYICON);
        nvg.drawText(str, x - startX + 14, y + 7F, Color.WHITE, 9.5F, Fonts.MEDIUM);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {

    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {

    }
}
