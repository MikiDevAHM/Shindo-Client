package me.miki.shindo.feature.mod.impl.crosshair;

import java.awt.Color;

import me.miki.shindo.Shindo;
import me.miki.shindo.event.EventTarget;
import me.miki.shindo.event.impl.render.Render2DEvent;
import me.miki.shindo.feature.mod.Mod;
import me.miki.shindo.feature.mod.Type;
import me.miki.shindo.feature.setting.Setting;
import me.miki.shindo.util.ResolutionHelper;
import me.miki.shindo.util.render.Helper2D;

public class CrosshairMod extends Mod {

    public static final LayoutManager layoutManager = new LayoutManager();

    public CrosshairMod() {
        super(
                "Crosshair",
                "Makes Crosshair customizable.",
                Type.Hud
        );

        Shindo.getInstance().getSettingsManager().addSetting(new Setting("Color", this, new Color(255, 255, 255), new Color(255, 0, 0), 0, new float[]{0, 0}));
        Shindo.getInstance().getSettingsManager().addSetting(new Setting("Cells", this, layoutManager.getLayout(0)));
    }

    @EventTarget
    public void onRender(Render2DEvent e) {
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
        return Shindo.getInstance().getSettingsManager().getSettingByModAndName(getName(), "Color").getColor().getRGB();
    }

    private boolean[][] getCells() {
        return Shindo.getInstance().getSettingsManager().getSettingByModAndName(getName(), "Cells").getCells();
    }
}
