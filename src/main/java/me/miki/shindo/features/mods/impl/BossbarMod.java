package me.miki.shindo.features.mods.impl;


import me.miki.shindo.events.EventTarget;
import me.miki.shindo.events.impl.EventRender2D;
import me.miki.shindo.features.mods.HUDMod;
import me.miki.shindo.helpers.ServerHelper;
import me.miki.shindo.helpers.render.GLHelper;
import me.miki.shindo.helpers.render.RenderHelper;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.boss.BossStatus;

public class BossbarMod extends HUDMod {
    public BossbarMod() {
        super(
                "Bossbar",
                "Adds tweaks to the Bossbar"
        );
    }

    @EventTarget
    public void onRender2D(EventRender2D event) {

        int bossHealthWidth = 182;
        int scale = (int) (BossStatus.healthScale * (float) (bossHealthWidth + 1));

        if((ServerHelper.isJoinServer() && BossStatus.bossName != null && BossStatus.statusBarTime > 0) || this.isEditing()) {

            String title = this.isEditing() ? "Boss Health" : BossStatus.bossName;

            GLHelper.startScale(this.getX(), this.getY(), this.getScale());

            mc.getTextureManager().bindTexture(Gui.icons);
            BossStatus.statusBarTime--;
            mc.getTextureManager().bindTexture(Gui.icons);

            RenderHelper.drawTexturedModalRect(this.getX(), this.getY() + 14, 0, 74, bossHealthWidth, 5);
            RenderHelper.drawTexturedModalRect(this.getX(), this.getY() + 14, 0, 74, bossHealthWidth, 5);

            if(scale > 0) {
                RenderHelper.drawTexturedModalRect(this.getX(), this.getY() + 14, 0, 79, scale, 5);
            }

            fr.drawStringWithShadow(title, (((182 / 2) - (fr.getStringWidth(title) / 2)) + this.getX()), (this.getY() - 10) + 13, 16777215);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            mc.getTextureManager().bindTexture(Gui.icons);

            GLHelper.endScale();
        }

        this.setWidth(182);
        this.setHeight(20);
    }
}
