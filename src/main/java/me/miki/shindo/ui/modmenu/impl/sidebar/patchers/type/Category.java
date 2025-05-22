/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package me.miki.shindo.ui.modmenu.impl.sidebar.patchers.type;

import me.miki.shindo.Shindo;
import me.miki.shindo.features.patcher.Patcher;
import me.miki.shindo.helpers.MathHelper;
import me.miki.shindo.helpers.animation.Animate;
import me.miki.shindo.helpers.animation.Easing;
import me.miki.shindo.helpers.render.Helper2D;
import me.miki.shindo.ui.Style;
import me.miki.shindo.ui.modmenu.impl.Panel;
import me.miki.shindo.ui.modmenu.impl.sidebar.patchers.Patchers;

public class Category extends Patchers {

    private final Animate animation = new Animate();
    private final boolean expanded = false; // Começa expandida

    public Category(Patcher patcher, Panel panel, int y) {
        super(patcher, panel, y);
        animation.setEase(Easing.CUBIC_IN_OUT).setMin(0).setMax(1).setSpeed(300);
        animation.setReversed(!patcher.isExpanded());
        if (patcher.isExpanded()) {
            animation.setValue(1);
        } else {
            animation.setValue(0);
        }
    }

    @Override
    public void renderPatchers(int mouseX, int mouseY) {
        int baseX = panel.getX() + 80;
        int baseY = panel.getY() + panel.getH() + getY();

        animation.update();

        // Desenhar o nome da categoria
        Shindo.getInstance().getFontHelper().size20.drawString(
                patcher.getName(),
                baseX,
                baseY + 6,
                0x90ffffff
        );

        // Linha decorativa
        Helper2D.drawRectangle(baseX, baseY + 20, panel.getW() - 40 - 60, 1, Style.getColorTheme(6).getRGB());
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        int baseX = panel.getX() + 80;
        int baseY = panel.getY() + panel.getH() + getY();

        if (MathHelper.withinBox(baseX, baseY + 6, 150, 15, mouseX, mouseY)) {
            setExpanded(!isExpanded());
            panel.setNeedsRefreshOptions(true); // Marca para atualizar no próximo frame
        }
    }

    public boolean isExpanded() {
        return patcher.isExpanded();
    }

    public void setExpanded(boolean expanded) {
        patcher.setExpanded(expanded);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {

    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {

    }
}