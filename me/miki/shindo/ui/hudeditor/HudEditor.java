package me.miki.shindo.ui.hudeditor;

import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.input.Mouse;

import me.miki.shindo.Shindo;
import me.miki.shindo.ui.Style;
import me.miki.shindo.ui.hudeditor.impl.HudMod;
import me.miki.shindo.ui.hudeditor.impl.impl.ArmorHud;
import me.miki.shindo.ui.hudeditor.impl.impl.BlockinfoHud;
import me.miki.shindo.ui.hudeditor.impl.impl.BossbarHud;
import me.miki.shindo.ui.hudeditor.impl.impl.CoordinatesHud;
import me.miki.shindo.ui.hudeditor.impl.impl.CpsHud;
import me.miki.shindo.ui.hudeditor.impl.impl.FpsHud;
import me.miki.shindo.ui.hudeditor.impl.impl.MemoryHud;
import me.miki.shindo.ui.hudeditor.impl.impl.PingHud;
import me.miki.shindo.ui.hudeditor.impl.impl.PotionHud;
import me.miki.shindo.ui.hudeditor.impl.impl.ScoreboardHud;
import me.miki.shindo.ui.hudeditor.impl.impl.ServerAddressHud;
import me.miki.shindo.ui.hudeditor.impl.impl.SpeedIndicatorHud;
import me.miki.shindo.ui.hudeditor.impl.impl.SprintHud;
import me.miki.shindo.ui.hudeditor.impl.impl.TimeHud;
import me.miki.shindo.ui.hudeditor.impl.impl.keystrokes.KeystrokesHud;
import me.miki.shindo.ui.mods.ModsUI;
import me.miki.shindo.util.MathHelper;
import me.miki.shindo.util.ResolutionHelper;
import me.miki.shindo.util.animation.Animate;
import me.miki.shindo.util.animation.Easing;
import me.miki.shindo.util.render.GLHelper;
import me.miki.shindo.util.render.Helper2D;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

public class HudEditor extends GuiScreen {

    private final ArrayList<HudMod> hudModList = new ArrayList<>();

    private final Animate animateLogo = new Animate();
    private final Animate animateSnapping = new Animate();
    private final Animate animate = new Animate();



    public HudEditor() {
    	
    	init();
        animateLogo.setEase(Easing.CUBIC_OUT).setMin(0).setMax(70).setSpeed(100).setReversed(false);
        animateSnapping.setEase(Easing.CUBIC_OUT).setMin(0).setMax(50).setSpeed(100).setReversed(false);
        animate.setEase(Easing.LINEAR).setMin(0).setMax(25).setSpeed(200);
    }
    
    

    /**
     * Initialize every hud mod
     * Ex: addHudMod(new Exemple());
     */
    public void init() {

        addHudMod(new ArmorHud());
        addHudMod(new BlockinfoHud());
        addHudMod(new BossbarHud());
        addHudMod(new CoordinatesHud());
        addHudMod(new CpsHud());
        addHudMod(new FpsHud());
        addHudMod(new KeystrokesHud());
        addHudMod(new MemoryHud());
        addHudMod(new PingHud());
        addHudMod(new PotionHud());
        addHudMod(new ScoreboardHud());
        addHudMod(new ServerAddressHud());
        addHudMod(new SpeedIndicatorHud());
        addHudMod(new SprintHud());
        addHudMod(new TimeHud());
	}



    /**
     * Draws the Screen with the button to show the modmenu and all the hudMods
     *
     * @param mouseX       The current X position of the mouse
     * @param mouseY       The current Y position of the mouse
     * @param partialTicks The partial ticks used for rendering
     */

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        boolean roundedCorners = Shindo.getInstance().getOptionManager().getOptionByName("Rounded Corners").isCheckToggled();
        int color = Shindo.getInstance().getOptionManager().getOptionByName("Color").getColor().getRGB();

        Helper2D.drawRectangle(0, 0, width, height, 0x70000000);

        animateLogo.update();
        GLHelper.startScissor(0, height / 2 - 78, width, 73);
        Shindo.getInstance().getFontHelper().size40.drawString(
                Shindo.NAME,
                width / 2f - Shindo.getInstance().getFontHelper().size40.getStringWidth(Shindo.NAME) / 2f,
                height / 2f + 36 - animateLogo.getValueI(),
                color
        );
        Helper2D.drawPicture(
                width / 2 - 25,
                height / 2 - 8 - animateLogo.getValueI(),
                50, 50, Style.getColor(70).getRGB(), "logo.png"
        );
        GLHelper.endScissor();

        animate.update().setReversed(!MathHelper.withinBox(width / 2 - 50, height / 2 - 6, 100, 20, mouseX, mouseY));

        Helper2D.drawRoundedRectangle(
                width / 2 - 50,
                height / 2 - 6,
                100, 20, 2,
                Style.getColor(animate.getValueI() + 30).getRGB(),
                roundedCorners ? 0 : -1
        );
        Shindo.getInstance().getFontHelper().size20.drawString(
                "Open Mods",
                width / 2f - Shindo.getInstance().getFontHelper().size20.getStringWidth("Open Mods") / 2f,
                height / 2f,
                color
        );

        for (HudMod hudMod : hudModList) {
            hudMod.renderMod(mouseX, mouseY);
            hudMod.updatePosition(mouseX, mouseY);
            if (hudMod.withinMod(mouseX, mouseY)) {
                int scroll = Mouse.getDWheel();
                if (scroll > 0 && hudMod.getSize() < 2) {
                    hudMod.setSize(hudMod.getSize() + 0.1f);
                } else if (scroll < 0 && hudMod.getSize() > 0.5f) {
                    hudMod.setSize(hudMod.getSize() - 0.1f);
                }
            }

            if (hudMod.getX() < 0) {
                hudMod.setX(0);
            } else if (hudMod.getX() + hudMod.getW() * hudMod.getSize() > ResolutionHelper.getWidth()) {
                hudMod.setX((int) (ResolutionHelper.getWidth() - hudMod.getW() * hudMod.getSize()));
            }

            if (hudMod.getY() < 0) {
                hudMod.setY(0);
            } else if (hudMod.getY() + hudMod.getH() * hudMod.getSize() > ResolutionHelper.getHeight()) {
                hudMod.setY((int) (ResolutionHelper.getHeight() - hudMod.getH() * hudMod.getSize()));
            }

            for (HudMod sHudMod : hudModList) {
                if (
                        Shindo.getInstance().getModManager().getMod(sHudMod.getName()).isToggled() &&
                                hudMod.isDragging() &&
                                !sHudMod.equals(hudMod) &&
                                !sHudMod.equals(hudMod) &&
                                Style.isSnapping()
                ) {
                    SnapPosition snap = new SnapPosition();
                    snap.setSnapping(true);
                    int snapRange = 5;
                    if (MathHelper.withinBoundsRange(hudMod.getX(), sHudMod.getX(), snapRange))
                        snap.setAll(sHudMod.getX(), sHudMod.getX(), false);
                    else if (MathHelper.withinBoundsRange(hudMod.getX() + hudMod.getW() * hudMod.getSize(), sHudMod.getX() + sHudMod.getW() * sHudMod.getSize(), snapRange))
                        snap.setAll(sHudMod.getX() + sHudMod.getW() * sHudMod.getSize(), sHudMod.getX() + sHudMod.getW() * sHudMod.getSize() - hudMod.getW() * hudMod.getSize(), false);
                    else if (MathHelper.withinBoundsRange(hudMod.getX() + hudMod.getW() * hudMod.getSize(), sHudMod.getX(), snapRange))
                        snap.setAll(sHudMod.getX(), sHudMod.getX() - hudMod.getW() * hudMod.getSize(), false);
                    else if (MathHelper.withinBoundsRange(hudMod.getX(), sHudMod.getX() + sHudMod.getW() * sHudMod.getSize(), snapRange))
                        snap.setAll(sHudMod.getX() + sHudMod.getW() * sHudMod.getSize(), sHudMod.getX() + sHudMod.getW() * sHudMod.getSize(), false);
                    else if (MathHelper.withinBoundsRange(hudMod.getY(), sHudMod.getY(), snapRange))
                        snap.setAll(sHudMod.getY(), sHudMod.getY(), true);
                    else if (MathHelper.withinBoundsRange(hudMod.getY() + hudMod.getH() * hudMod.getSize(), sHudMod.getY() + sHudMod.getH() * sHudMod.getSize(), snapRange))
                        snap.setAll(sHudMod.getY() + sHudMod.getH() * sHudMod.getSize(), sHudMod.getY() + sHudMod.getH() * sHudMod.getSize() - hudMod.getH() * hudMod.getSize(), true);
                    else if (MathHelper.withinBoundsRange(hudMod.getY() + hudMod.getH() * hudMod.getSize(), sHudMod.getY(), snapRange))
                        snap.setAll(sHudMod.getY(), sHudMod.getY() - hudMod.getH() * hudMod.getSize(), true);
                    else if (MathHelper.withinBoundsRange(hudMod.getY(), sHudMod.getY() + sHudMod.getH() * sHudMod.getSize(), snapRange))
                        snap.setAll(sHudMod.getY() + sHudMod.getH() * sHudMod.getSize(), sHudMod.getY() + sHudMod.getH() * sHudMod.getSize(), true);
                    else
                        snap.setSnapping(false);

                    if (snap.isSnapping()) {
                        if (!snap.isHorizontal()) {
                            Helper2D.drawRectangle((int) snap.getsPos(), 0, 1, ResolutionHelper.getHeight(), 0x60ffffff);
                            hudMod.setX((int) snap.getPos());
                        } else {
                            Helper2D.drawRectangle(0, (int) snap.getsPos(), ResolutionHelper.getWidth(), 1, 0x60ffffff);
                            hudMod.setY((int) snap.getPos());
                        }
                    }
                }
            }
        }

        animateSnapping.update();
        Helper2D.drawRoundedRectangle(10, height - 50, 40, 40, 2, Style.getColor(40).getRGB(), roundedCorners ? 0 : -1);
        Helper2D.drawPicture(15, height - 45, 30, 30, color, Style.isDarkMode() ? "icon/dark.png" : "icon/light.png");
        Helper2D.drawRoundedRectangle(60, height - animateSnapping.getValueI(), 40, 40, 2, Style.getColor(40).getRGB(), roundedCorners ? 0 : -1);
        Helper2D.drawPicture(65, height + 5 - animateSnapping.getValueI(), 30, 30, color, Style.isSnapping() ? "icon/grid.png" : "icon/nogrid.png");
    }

    /**
     * Sets the gui screen to the modmenu when the middle button is clicked
     * Toggles the Dark mode if the bottom left button is pressed
     *
     * @param mouseX      The current X position of the mouse
     * @param mouseY      The current Y position of the mouse
     * @param mouseButton The current mouse button which is pressed
     */

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        for (HudMod hudMod : hudModList) {
            if (hudMod.withinMod(mouseX, mouseY) && mouseButton == 0) {
                hudMod.setDragging(true);
                hudMod.setOffsetX(mouseX - hudMod.getX());
                hudMod.setOffsetY(mouseY - hudMod.getY());
            }
        }

        if (mouseButton == 0) {
            if (MathHelper.withinBox(width / 2 - 50, height / 2 - 6, 100, 20, mouseX, mouseY)) {
                mc.displayGuiScreen(new ModsUI());
            }

            if (MathHelper.withinBox(10, height - 50, 40, 40, mouseX, mouseY)) {
                Style.setDarkMode(!Style.isDarkMode());
            } else if (MathHelper.withinBox(60, height - 50, 40, 40, mouseX, mouseY)) {
                Style.setSnapping(!Style.isSnapping());
            }
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        for (HudMod hudMod : hudModList) {
            hudMod.setDragging(false);
        }
        super.mouseReleased(mouseX, mouseY, state);
    }

    /**
     * Loads a shader to blur the screen when the gui is opened
     */

    @Override
    public void initGui() {
        mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
        animateLogo.reset();
        animateSnapping.reset();
        super.initGui();
    }

    /**
     * Deletes all shaderGroups in order to remove the screen blur when the gui is closed
     */

    @Override
    public void onGuiClosed() {
        if (mc.entityRenderer.getShaderGroup() != null) {
            mc.entityRenderer.getShaderGroup().deleteShaderGroup();
        }
        super.onGuiClosed();
    }
    
    /**
     * @return Returns an Arraylist of hud mods
     */

    public ArrayList<HudMod> getHudMods() {
        return hudModList;
    }

    /**
     * Adds a hudMod to the list
     *
     * @param hudMod The hudMod which should be added
     */

    public void addHudMod(HudMod hudMod) {
        hudModList.add(hudMod);
    }

    /**
     * Returns a given hudMod using its name
     *
     * @param name The name of the hudMod
     * @return The returned hudMod
     */

    public HudMod getHudMod(String name) {
        for (HudMod hudMod : hudModList) {
            if (hudMod.getName().equalsIgnoreCase(name)) {
                return hudMod;
            }
        }
        return null;
    }
    



}
