package net.minecraft.client.gui;

import me.miki.shindo.Shindo;
import me.miki.shindo.helpers.render.Helper2D;
import me.miki.shindo.ui.Style;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;

public class GuiListButton extends GuiButton
{
    private boolean field_175216_o;

    /** The localization string used by this control. */
    private String localizationStr;

    /** The GuiResponder Object reference. */
    private final GuiPageButtonList.GuiResponder guiResponder;

    public GuiListButton(GuiPageButtonList.GuiResponder responder, int p_i45539_2_, int p_i45539_3_, int p_i45539_4_, String p_i45539_5_, boolean p_i45539_6_)
    {
        super(p_i45539_2_, p_i45539_3_, p_i45539_4_, 150, 20, "");
        this.localizationStr = p_i45539_5_;
        this.field_175216_o = p_i45539_6_;
        this.displayString = this.buildDisplayString();
        this.guiResponder = responder;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (this.visible) {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
            int i = this.getHoverState(this.hovered);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.blendFunc(770, 771);
            //this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, 46 + i * 20, this.width / 2, this.height);
            //this.drawTexturedModalRect(this.xPosition + this.width / 2, this.yPosition, 200 - this.width / 2, 46 + i * 20, this.width / 2, this.height);

            Helper2D.drawRoundedRectangle(xPosition, yPosition, width, height, 3,
                    Style.getColorTheme(8).getRGB(),
                    Shindo.getInstance().getOptionManager().getOptionByName("Rounded Corners").isCheckToggled() ? 0 : -1
            );
            Helper2D.drawRoundedRectangle(xPosition + 1, yPosition + 1, width - 2, height - 2, 3,
                    Style.getColorTheme(hovered ? 7 : 5).getRGB(),
                    Shindo.getInstance().getOptionManager().getOptionByName("Rounded Corners").isCheckToggled() ? 0 : -1
            );

            //Helper2D.drawCircle(xPosition + 10, yPosition + 10, 3, 0, 360, Style.getColorTheme(10).getRGB());
            //Helper2D.drawCircle(xPosition + 10, yPosition + 10, 2, 0, 360, Style.getColorTheme(9).getRGB());

            Shindo.getInstance().getFontHelper().size20.drawString(
                    displayString,
                    xPosition + width / 2f - Shindo.getInstance().getFontHelper().size20.getStringWidth(displayString) / 2f,
                    yPosition + height / 2f - 4,
                    -1
            );
        }
    }

    /**
     * Builds the localized display string for this GuiListButton
     */
    private String buildDisplayString()
    {
        return I18n.format(this.localizationStr, new Object[0]) + ": " + (this.field_175216_o ? I18n.format("gui.yes", new Object[0]) : I18n.format("gui.no", new Object[0]));
    }

    public void func_175212_b(boolean p_175212_1_)
    {
        this.field_175216_o = p_175212_1_;
        this.displayString = this.buildDisplayString();
        this.guiResponder.func_175321_a(this.id, p_175212_1_);
    }

    /**
     * Returns true if the mouse has been pressed on this control. Equivalent of MouseListener.mousePressed(MouseEvent
     * e).
     */
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
    {
        if (super.mousePressed(mc, mouseX, mouseY))
        {
            this.field_175216_o = !this.field_175216_o;
            this.displayString = this.buildDisplayString();
            this.guiResponder.func_175321_a(this.id, this.field_175216_o);
            return true;
        }
        else
        {
            return false;
        }
    }
}
