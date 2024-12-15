package me.miki.shindo.ui.hudeditor.impl.impl;

import me.miki.shindo.Shindo;
import me.miki.shindo.event.EventTarget;
import me.miki.shindo.event.impl.render.Render2DEvent;
import me.miki.shindo.ui.hudeditor.HudEditor;
import me.miki.shindo.ui.hudeditor.impl.HudMod;
import me.miki.shindo.util.render.GLHelper;
import me.miki.shindo.util.render.Helper2D;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.boss.BossStatus;

public class BossbarHud extends HudMod {
	

    public BossbarHud() {
        super("Bossbar", 10, 10);
        setW(182);
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
    public void onRender2D(Render2DEvent e) {
        GLHelper.startScale(getX(), getY(), getSize());
        if (Shindo.getInstance().getModManager().getMod(getName()).isToggled() && !(mc.currentScreen instanceof HudEditor)) {
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
        Helper2D.drawTexturedModalRect(x, y + 10, 0, 74, width, 5);
        Helper2D.drawTexturedModalRect(x, y + 10, 0, 74, width, 5);

        if (health > 0) {
            Helper2D.drawTexturedModalRect(x, y + 10, 0, 79, health, 5);
        }

        String s = BossStatus.bossName;
        
        if (isModern()) {
        	Shindo.getInstance().getFontHelper().size20.drawString(s, x + width / 2f - mc.fontRendererObj.getStringWidth(s) / 2f, y, getColor());
        } else {
            mc.fontRendererObj.drawString(s, x + width / 2 - mc.fontRendererObj.getStringWidth(s) / 2, y, getColor());
        }

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
        if (isModern()) {
        	Shindo.getInstance().getFontHelper().size20.drawString(s, x + width / 2f - mc.fontRendererObj.getStringWidth(s) / 2f, y, getColor());
        } else {
            mc.fontRendererObj.drawString(s, x + width / 2 - mc.fontRendererObj.getStringWidth(s) / 2, y, getColor());
        }


        setW(width);
        setH(15);
    }
    
    private int getColor() {
        return Shindo.getInstance().getSettingsManager().getSettingByModAndName(getName(), "Font Color").getColor().getRGB();
    }

    private boolean isModern() {
        return Shindo.getInstance().getSettingsManager().getSettingByModAndName(getName(), "Mode").getCurrentMode().equalsIgnoreCase("Modern");
    }
}