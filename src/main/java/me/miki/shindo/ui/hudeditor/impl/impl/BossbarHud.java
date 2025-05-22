/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package me.miki.shindo.ui.hudeditor.impl.impl;

import me.miki.shindo.Shindo;
import me.miki.shindo.events.EventTarget;
import me.miki.shindo.events.impl.EventRender2D;
import me.miki.shindo.helpers.render.GLHelper;
import me.miki.shindo.helpers.render.Helper2D;
import me.miki.shindo.ui.hudeditor.impl.HudMod;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.boss.BossStatus;

public class BossbarHud extends HudMod {

    public BossbarHud() {
        super("Bossbar", 120, 5);
    }

    @Override
    public void renderMod(int mouseX, int mouseY) {
        GLHelper.startScale(getX(), getY(), getSize());
        if (Shindo.getInstance().getModManager().getMod(getName()).isToggled()) {
            renderBossHealthPlaceHolder();
            super.renderMod(mouseX, mouseY);
        }
        GLHelper.endScale();
    }

    @EventTarget
    public void onRender2D(EventRender2D e) {
        GLHelper.startScale(getX(), getY(), getSize());
        if (Shindo.getInstance().getModManager().getMod(getName()).isToggled()) {
            if (BossStatus.bossName != null && BossStatus.statusBarTime > 0) {
                renderBossHealth();
            }
        }
        GLHelper.endScale();
    }

    private void renderBossHealth() {
        BossStatus.statusBarTime--;
        int x = getX();
        int y = getY();
        int width = 182;
        int health = (int) (BossStatus.healthScale * (float) (width + 1));

        mc.getTextureManager().bindTexture(Gui.icons);
        BossStatus.statusBarTime--;
        mc.getTextureManager().bindTexture(Gui.icons);
        Helper2D.drawTexturedModalRect(x, y + 14, 0, 74, width, 5);
        Helper2D.drawTexturedModalRect(x, y + 14, 0, 74, width, 5);

        if (health > 0) {
            Helper2D.drawTexturedModalRect(x, y + 14, 0, 79, health, 5);
        }

        String s = BossStatus.bossName;
        mc.fontRendererObj.drawStringWithShadow(s, x + width / 2f - mc.fontRendererObj.getStringWidth(s) / 2f, y, 16777215);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(Gui.icons);

        setW(width);
        setH(15);
    }

    private void renderBossHealthPlaceHolder() {
        int x = getX();
        int y = getY();
        int width = 182;
        int health = 100;

        mc.getTextureManager().bindTexture(Gui.icons);
        Helper2D.drawTexturedModalRect(x, y + 10, 0, 74, width, 5);
        Helper2D.drawTexturedModalRect(x, y + 10, 0, 74, width, 5);
        Helper2D.drawTexturedModalRect(x, y + 10, 0, 79, health, 5);

        String s = "BossBar";
        mc.fontRendererObj.drawStringWithShadow(s, x + width / 2f - mc.fontRendererObj.getStringWidth(s) / 2f, y, 16777215);

        setW(width);
        setH(15);
    }

    private boolean isModern() {
        return Shindo.getInstance().getSettingManager().getSettingByModAndName(getName(), "Mode").getCurrentMode().equalsIgnoreCase("Modern");
    }
}
