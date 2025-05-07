package net.minecraft.client.gui;

import me.miki.shindo.Shindo;
import me.miki.shindo.helpers.render.Helper2D;
import me.miki.shindo.ui.Style;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;

public class GuiLockIconButton extends GuiButton
{
    private boolean field_175231_o = false;

    public GuiLockIconButton(int p_i45538_1_, int p_i45538_2_, int p_i45538_3_)
    {
        super(p_i45538_1_, p_i45538_2_, p_i45538_3_, 20, 20, "");
    }

    public boolean func_175230_c()
    {
        return this.field_175231_o;
    }

    public void func_175229_b(boolean p_175229_1_)
    {
        this.field_175231_o = p_175229_1_;
    }

    /**
     * Draws this button to the screen.
     */
    public void drawButton(Minecraft mc, int mouseX, int mouseY)
    {
        if (this.visible)
        {
            //mc.getTextureManager().bindTexture(GuiButton.buttonTextures);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            boolean flag = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;

            Helper2D.drawRoundedRectangle(xPosition + 1, yPosition + 1, width -2, height - 2, 3,
                    Style.getColorTheme(8).getRGB(),
                    Shindo.getInstance().getOptionManager().getOptionByName("Rounded Corners").isCheckToggled() ? 0 : -1
            );


            String icon = "";

            if (this.field_175231_o)
            {
                if (!this.enabled)
                {
                    Helper2D.drawRoundedRectangle(xPosition + 2, yPosition + 2, width - 4, height - 4, 3,
                            Style.getColorTheme(3).getRGB(),
                            Shindo.getInstance().getOptionManager().getOptionByName("Rounded Corners").isCheckToggled() ? 0 : -1
                    );
                    icon = "icon/lock.png";
                }
                else if (flag)
                {
                    Helper2D.drawRoundedRectangle(xPosition + 2, yPosition + 2, width - 4, height - 4, 3,
                            Style.getColorTheme(7).getRGB(),
                            Shindo.getInstance().getOptionManager().getOptionByName("Rounded Corners").isCheckToggled() ? 0 : -1
                    );
                    icon = "icon/lock.png";
                }
                else
                {
                    Helper2D.drawRoundedRectangle(xPosition + 2, yPosition + 2, width - 4, height - 4, 3,
                            Style.getColorTheme(5).getRGB(),
                            Shindo.getInstance().getOptionManager().getOptionByName("Rounded Corners").isCheckToggled() ? 0 : -1
                    );
                    icon = "icon/lock.png";
                }
            }
            else if (!this.enabled)
            {
                Helper2D.drawRoundedRectangle(xPosition + 2, yPosition + 2, width - 4, height - 4, 3,
                        Style.getColorTheme(3).getRGB(),
                        Shindo.getInstance().getOptionManager().getOptionByName("Rounded Corners").isCheckToggled() ? 0 : -1
                );
                icon = "icon/unlock.png";
            }
            else if (flag)
            {
                Helper2D.drawRoundedRectangle(xPosition + 2, yPosition + 2, width - 4, height - 4, 3,
                        Style.getColorTheme(7).getRGB(),
                        Shindo.getInstance().getOptionManager().getOptionByName("Rounded Corners").isCheckToggled() ? 0 : -1
                );
                icon = "icon/unlock.png";
            }
            else
            {
                Helper2D.drawRoundedRectangle(xPosition + 2, yPosition + 2, width - 4, height - 4, 3,
                        Style.getColorTheme(5).getRGB(),
                        Shindo.getInstance().getOptionManager().getOptionByName("Rounded Corners").isCheckToggled() ? 0 : -1
                );
                icon = "icon/unlock.png";
            }

            Helper2D.drawPicture(xPosition + 4, yPosition + 4, width - 8, height - 8, 0, icon);
            //this.drawTexturedModalRect(this.xPosition, this.yPosition, guilockiconbutton$icon.func_178910_a(), guilockiconbutton$icon.func_178912_b(), this.width, this.height);
        }
    }

    static enum Icon
    {
        LOCKED(0, 146),
        LOCKED_HOVER(0, 166),
        LOCKED_DISABLED(0, 186),
        UNLOCKED(20, 146),
        UNLOCKED_HOVER(20, 166),
        UNLOCKED_DISABLED(20, 186);

        private final int field_178914_g;
        private final int field_178920_h;

        private Icon(int p_i45537_3_, int p_i45537_4_)
        {
            this.field_178914_g = p_i45537_3_;
            this.field_178920_h = p_i45537_4_;
        }

        public int func_178910_a()
        {
            return this.field_178914_g;
        }

        public int func_178912_b()
        {
            return this.field_178920_h;
        }
    }
}
