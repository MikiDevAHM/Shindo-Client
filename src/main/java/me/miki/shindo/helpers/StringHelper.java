package me.miki.shindo.helpers;

import me.miki.shindo.helpers.render.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;

public class StringHelper {

    private static final Minecraft mc = Minecraft.getMinecraft();

    public static int drawStringWithHead(ChatLine line, String text, float x, float y, int color) {

        float actualX = x;


        NetworkPlayerInfo networkPlayerInfo = line.getPlayerInfo();

        actualX += networkPlayerInfo != null ? 10f : 0;

        if (networkPlayerInfo != null) {

            GlStateManager.enableBlend();
            GlStateManager.enableAlpha();
            GlStateManager.enableTexture2D();

            mc.getTextureManager().bindTexture(networkPlayerInfo.getLocationSkin());

            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

            RenderHelper.drawScaledCustomSizeModalRect(x, y - 0.5, 8.0f, 8.0f, 8, 8, 8, 8, 64.0f, 64.0f);
            RenderHelper.drawScaledCustomSizeModalRect(x, y - 0.5, 40.0f, 8.0f, 8, 8, 8, 8, 64.0f, 64.0f);

            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        }

        return mc.fontRendererObj.drawStringWithShadow(text, actualX, y, color);
    }
}
