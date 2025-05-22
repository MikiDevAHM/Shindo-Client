/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package me.miki.shindo.features.mods.impl.crosshair;


import me.miki.shindo.Shindo;
import me.miki.shindo.events.EventTarget;
import me.miki.shindo.events.impl.EventRender2D;
import me.miki.shindo.features.mods.Mod;
import me.miki.shindo.features.mods.Type;
import me.miki.shindo.features.settings.Setting;
import me.miki.shindo.helpers.ResolutionHelper;
import me.miki.shindo.helpers.render.Helper2D;

import java.awt.*;

public class CrosshairMod extends Mod {
    public static final LayoutManager layoutManager = new LayoutManager();

    public CrosshairMod() {
        super(
                "Crosshair",
                "Makes Crosshair customizable.",
                Type.Hud
        );

        Shindo.getInstance().getSettingManager().addSetting(new Setting("Color", this, new Color(255, 255, 255), new Color(255, 0, 0), 0, new float[]{0, 0}));
        Shindo.getInstance().getSettingManager().addSetting(new Setting("Cells", this, layoutManager.getLayout(0)));
    }

    @EventTarget
    public void onRender(EventRender2D e) {
        for (int row = 0; row < 11; row++) {
            for (int col = 0; col < 11; col++) {
                if (getCells()[row][col] && isToggled()) {
                    Helper2D.drawRectangle(
                            ResolutionHelper.getWidth() / 2 - 5 + col,
                            ResolutionHelper.getHeight() / 2 - 5 + row,
                            1, 1, color()
                    );
                }
            }
        }
    }

    private int color() {
        return Shindo.getInstance().getSettingManager().getSettingByModAndName(getName(), "Color").getColor().getRGB();
    }

    private boolean[][] getCells() {
        return Shindo.getInstance().getSettingManager().getSettingByModAndName(getName(), "Cells").getCells();
    }
}
