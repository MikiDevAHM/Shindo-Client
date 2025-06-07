package me.miki.shindo.injection.mixin.mixins.render;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Render.class)
public abstract class MixinRender <T extends Entity>  {

    @Shadow
    protected RenderManager renderManager;

    @Shadow
    public abstract FontRenderer getFontRendererFromRenderManager();

    /**
     * @author MikiDevAHM
     * @reason Client Logo Rendering
     */
    @Overwrite
    protected void renderLivingLabel(T entityIn, String str, double x, double y, double z, int maxDistance) {

        double d0 = entityIn.getDistanceSqToEntity(this.renderManager.livingPlayer);

        if (d0 <= (double)(maxDistance * maxDistance)) {
            FontRenderer fontrenderer = this.getFontRendererFromRenderManager();
            float f = 1.6F;
            float f1 = 0.016666668F * f;
            GlStateManager.pushMatrix();
            GlStateManager.translate((float)x + 0.0F, (float)y + entityIn.height + 0.5F, (float)z);
            GL11.glNormal3f(0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
            GlStateManager.scale(-f1, -f1, f1);
            GlStateManager.disableLighting();
            GlStateManager.depthMask(false);
            GlStateManager.disableDepth();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            int i = 0;

            int j = fontrenderer.getStringWidth(str) / 2;

            /*
            if (entityIn instanceof AbstractClientPlayer) {
                String uuid = Shindo.getInstance().getShindoAPI().isUUIDBad() ? ((AbstractClientPlayer) entityIn).getName() : ((AbstractClientPlayer) entityIn).getGameProfile().getId().toString();
                if (Shindo.getInstance().getShindoAPI().isOnline(uuid)) {
                    GlStateManager.disableTexture2D();
                    worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
                    worldrenderer.pos((double)(-j - 11), (double)(-1 + i), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
                    worldrenderer.pos((double)(-j - 11), (double)(8 + i), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
                    worldrenderer.pos((double)(j + 1), (double)(8 + i), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
                    worldrenderer.pos((double)(j + 1), (double)(-1 + i), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
                    tessellator.draw();
                    GlStateManager.enableTexture2D();

                    String texture = "shindo/logo.png";
                    ShindoAPI api = Shindo.getInstance().getShindoAPI();

                    if (api.isStaff(uuid)) {
                        texture = "shindo/logo-staff.png";
                    } else if (api.isDiamond(uuid)) {
                        texture = "shindo/logo-diamond.png";
                    } else if (api.isGold(uuid)) {
                        texture = "shindo/logo-gold.png";
                    }

                    Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(texture));
                    RenderUtils.drawModalRectWithCustomSizedTexture(-fontrenderer.getStringWidth(entityIn.getDisplayName().getFormattedText()) / 2F - 10, -1,  9, 9, 9, 9, 9, 9);
                } else {

             */
                    GlStateManager.disableTexture2D();
                    worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
                    worldrenderer.pos((double)(-j - 8), (double)(-1 + i), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
                    worldrenderer.pos((double)(-j - 8), (double)(8 + i), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
                    worldrenderer.pos((double)(j + 1), (double)(8 + i), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
                    worldrenderer.pos((double)(j + 1), (double)(-1 + i), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
                    tessellator.draw();
                    GlStateManager.enableTexture2D();

                    /*
                }
            }
             */
            if (str.equals("deadmau5")) {
                i = -10;
            }

            fontrenderer.drawString(str, -fontrenderer.getStringWidth(str) / 2, i, 553648127);
            GlStateManager.enableDepth();
            GlStateManager.depthMask(true);

            fontrenderer.drawString(str, -fontrenderer.getStringWidth(str) / 2, i, -1);
            GlStateManager.enableLighting();
            GlStateManager.disableBlend();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.popMatrix();
        }
    }
}
