/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package me.miki.shindo.features.mods.impl;


import me.miki.shindo.Shindo;
import me.miki.shindo.events.EventTarget;
import me.miki.shindo.events.impl.EventRender2D;
import me.miki.shindo.features.mods.SimpleHUDMod;
import me.miki.shindo.features.nanovg.NanoVGManager;
import me.miki.shindo.features.nanovg.font.Fonts;
import me.miki.shindo.features.nanovg.font.Icon;
import me.miki.shindo.features.settings.Setting;
import net.minecraft.util.BlockPos;
import net.minecraft.world.chunk.Chunk;

public class CoordinatesMod extends SimpleHUDMod {

    public CoordinatesMod() {
        super(
                "Coordinates",
                "Shows your Coordinates on the HUD."
        );

        String[] mode = {"Simple", "Fancy"};
        Shindo.getInstance().getSettingManager().addSetting(new Setting("Design", this, "Simple", 0, mode));
        Shindo.getInstance().getSettingManager().addSetting(new Setting("Icon", this, true));
    }

    @EventTarget
    public void onRender2D(EventRender2D event) {

        NanoVGManager nvg = Shindo.getInstance().getNanoVGManager();

        if(isSimple()) {
            this.draw();
        }else {
            nvg.setupAndDraw(() -> drawNanoVG());
        }
    }

    private void drawNanoVG() {

        String biome = "";
        Chunk chunk = mc.theWorld.getChunkFromBlockCoords(new BlockPos(mc.thePlayer));
        int maxWidth = 100;
        biome = chunk.getBiome(new BlockPos(mc.thePlayer), this.mc.theWorld.getWorldChunkManager()).biomeName;

        if(maxWidth < (this.getTextWidth("Biome: " + biome, 9, Fonts.REGULAR))) {
            maxWidth = (int) (this.getTextWidth("Biome: " + biome, 9, Fonts.REGULAR) + 12);
        }else {
            maxWidth = 107;
        }

        this.drawBackground(maxWidth, 48);
        this.drawText("X: " + (int) mc.thePlayer.posX, 5.5F, 5.5F, 9, Fonts.REGULAR);
        this.drawText("Y: " + (int) mc.thePlayer.posY, 5.5F, 15.5F, 9, Fonts.REGULAR);
        this.drawText("Z: " + (int) mc.thePlayer.posZ, 5.5F, 25.5F, 9, Fonts.REGULAR);
        this.drawText("Biome: " + biome, 5.5F, 35.5F, 9, Fonts.REGULAR);

        this.setWidth(maxWidth);
        this.setHeight(48);
    }

    @Override
    public String getText() {
        return "X: " + (int) mc.thePlayer.posX + " Y: " + (int) mc.thePlayer.posY + " Z: " + (int) mc.thePlayer.posZ;
    }

    @Override
    public String getIcon() {
        return isIcon() ? Icon.MAP_PIN : null;
    }

    private boolean isSimple() {
        return  Shindo.getInstance().getSettingManager().getSettingByModAndName(getName(), "Design").getCurrentMode().equalsIgnoreCase("Simple");
    }

    private boolean isIcon() {
        return Shindo.getInstance().getSettingManager().getSettingByModAndName(getName(), "Icon").isCheckToggled();
    }
}
