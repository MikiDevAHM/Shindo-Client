/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package me.miki.shindo.ui.modmenu;

import me.miki.shindo.Shindo;
import me.miki.shindo.helpers.ResolutionHelper;
import me.miki.shindo.helpers.TimeHelper;
import me.miki.shindo.helpers.render.Helper2D;
import me.miki.shindo.ui.Style;
import me.miki.shindo.ui.modmenu.impl.Panel;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.util.Date;

public class ModMenu extends GuiScreen {

    private final Panel panel = new Panel();

    /**
     * Draws the panel of the modmenu used to toggle mods and change settings
     *
     * @param mouseX The current X position of the mouse
     * @param mouseY The current Y position of the mouse
     * @param partialTicks The partial ticks used for rendering
     */

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        Helper2D.drawRectangle(0, 0, width, height, 0x70000000);
        boolean roundedCorners = Shindo.getInstance().getOptionManager().getOptionByName("Rounded Corners").isCheckToggled();
        int color = Shindo.getInstance().getOptionManager().getOptionByName("Color").getColor().getRGB();

        panel.setY(ResolutionHelper.getHeight() / 2 - 150);
        panel.renderPanel(mouseX, mouseY);

        /*
        Draws the time at the top right
         */

        Helper2D.drawRoundedRectangle(ResolutionHelper.getWidth() / 4 + 60, ResolutionHelper.getHeight() / 4 - 27, 130, 20, 2, Style.getColorTheme(4).getRGB(), 0);
        Helper2D.drawPicture(ResolutionHelper.getWidth() / 4 + 10, ResolutionHelper.getHeight() / 4 - 30, 30, 30, color,  "logo.png");

        Shindo.getInstance().getFontHelper().size20.drawString(TimeHelper.getFormattedDate(), ResolutionHelper.getWidth() / 4f + 90f , ResolutionHelper.getHeight() / 4f - 20f, -1);
        Shindo.getInstance().getFontHelper().size20.drawString(TimeHelper.getFormattedTimeMinute(), ResolutionHelper.getWidth() / 4f + 150f , ResolutionHelper.getHeight() / 4f - 20f, -1);

        Date date = new Date();
        boolean isDay = date.getHours() < 19 && date.getHours() > 6 ;
        Helper2D.drawPicture(ResolutionHelper.getWidth() / 4 + 65, ResolutionHelper.getHeight() / 4 - 25, 15, 15, color,  isDay ? "icon/light.png" : "icon/dark.png" );
        /*
        Draws the dark and light mode button on the bottom left
         */


        // Helper2D.drawRoundedRectangle(10, height - 50, 40, 40, 2, Style.getColor(40).getRGB(), roundedCorners ? 0 : -1);
        // Helper2D.drawPicture(15, height - 45, 30, 30, color, Style.isDarkMode() ? "icon/dark.png" : "icon/light.png");
        // Helper2D.drawRoundedRectangle(60, height - 50 + animateSnapping.getValueI(), 40, 40, 2, Style.getColor(40).getRGB(), roundedCorners ? 0 : -1);
        // Helper2D.drawPicture(65, height - 45 + animateSnapping.getValueI(), 30, 30, color, Style.isSnapping() ? "icon/grid.png" : "icon/nogrid.png");
    }

    /**
     * Sets different values of the panel when any mouse button is clicked
     * Changes the darkMode boolean if the button in the bottom left is pressed
     *
     * @param mouseX The current X position of the mouse
     * @param mouseY The current Y position of the mouse
     * @param mouseButton The current mouse button which is pressed
     */

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        panel.mouseClicked(mouseX, mouseY, mouseButton);
        // if(mouseButton == 0) {
            // if (MathHelper.withinBox(
            //        panel.getX(), panel.getY(),
            //        panel.getW(), panel.getH(),
            //        mouseX, mouseY
            // )) {
            //    panel.setDragging(true);
            //    panel.setOffsetX(mouseX - panel.getX());
            //    panel.setOffsetY(mouseY - panel.getY());
            // }

            // if (MathHelper.withinBox(10, height - 50, 40, 40, mouseX, mouseY)) {
            //    Style.setDarkMode(!Style.isDarkMode());
            // }
        // }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        panel.setDragging(false);
        panel.mouseReleased(mouseX, mouseY, state);
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {
        panel.keyTyped(typedChar, keyCode);
        super.keyTyped(typedChar, keyCode);
    }

    /**
     * Loads a shader to blur the screen when the gui is opened
     */

    @Override
    public void initGui() {
        panel.initGui();
        mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
        super.initGui();
    }

    /**
     * Deleted all shaderGroups in order to remove the screen blur when the gui is closed
     */

    @Override
    public void onGuiClosed() {
        if (mc.entityRenderer.getShaderGroup() != null) {
            mc.entityRenderer.getShaderGroup().deleteShaderGroup();
        }
        super.onGuiClosed();
    }
}
