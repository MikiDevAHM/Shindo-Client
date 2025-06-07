package me.miki.shindo.injection.mixin.mixins.gui;

import me.miki.shindo.management.mods.impl.TabEditorMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.NetworkManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.awt.*;
import java.util.UUID;

@Mixin(GuiPlayerTabOverlay.class)
public class MixinGuiPlayerTabOverlay {
	
	@Redirect(method = "renderPlayerlist", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;drawStringWithShadow(Ljava/lang/String;FFI)I", ordinal = 2))
	public int renderShindoIcon(FontRenderer instance, String text, float x, float y, int color) {

		/*
		Minecraft mc = Minecraft.getMinecraft();

		NetworkPlayerInfo playerInfo = mc.getNetHandler().getPlayerInfoMap().stream()
				.filter(info -> info.getGameProfile().getName().equalsIgnoreCase(text))
				.findFirst()
				.orElse(null);

		// Tamanho do ícone
		int iconSize = 8;
		int iconOffset = 2; // espaço entre ícone e texto

		if (playerInfo != null) {
			String uuid = Shindo.getInstance().getShindoAPI().isUUIDBad()
					? playerInfo.getGameProfile().getName()
					: playerInfo.getGameProfile().getId().toString();

			if (Shindo.getInstance().getShindoAPI().isOnline(uuid)) {
				String texture = "shindo/logo.png";
				ShindoAPI api = Shindo.getInstance().getShindoAPI();

				if (api.hasPrivilege(uuid, "Staff")) {
					texture = "shindo/logo-staff.png";
				} else if (api.hasPrivilege(uuid, "Diamond")) {
					texture = "shindo/logo-diamond.png";
				} else if (api.hasPrivilege(uuid, "Gold")) {
					texture = "shindo/logo-gold.png";
				}

				mc.getTextureManager().bindTexture(new ResourceLocation(texture));
				RenderUtils.drawModalRectWithCustomSizedTexture((int) x, (int) y, 0, 0, iconSize, iconSize, iconSize, iconSize);

				x += iconSize + iconOffset;
			}
		}
		*/
		// Renderiza o texto (com X ajustado se necessário)
		return instance.drawStringWithShadow(text, x, y, color);
	}
	
	@Redirect(method = "renderPlayerlist", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/WorldClient;getPlayerEntityByUUID(Ljava/util/UUID;)Lnet/minecraft/entity/player/EntityPlayer;"))
	public EntityPlayer removePlayerHead(WorldClient instance, UUID uuid) {
		
		if(TabEditorMod.getInstance().isToggled() && !TabEditorMod.getInstance().getHeadSetting().isToggled()) {
			return null;
		}

		return instance.getPlayerEntityByUUID(uuid);
	}

	@Redirect(method = "renderPlayerlist", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;isIntegratedServerRunning()Z"))
	public boolean removePlayerHead(Minecraft instance) {
		return instance.isIntegratedServerRunning() && showHeads();
	}

	@Redirect(method = "renderPlayerlist", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/NetworkManager;getIsencrypted()Z"))
	public boolean removePlayerHead(NetworkManager instance) {
		return instance.getIsencrypted() && showHeads();
	}
	
	@ModifyConstant(method = "renderPlayerlist", constant = @Constant(intValue = Integer.MIN_VALUE))
	public int removeBackground(int original) {
		
		if(TabEditorMod.getInstance().isToggled() && !TabEditorMod.getInstance().getBackgroundSetting().isToggled()) {
			return new Color(0, 0, 0, 0).getRGB();
		}

		return original;
	}

	@ModifyConstant(method = "renderPlayerlist", constant = @Constant(intValue = 553648127))
	public int removeBackground2(int original) {
		
		if(TabEditorMod.getInstance().isToggled() && !TabEditorMod.getInstance().getBackgroundSetting().isToggled()) {
			return new Color(0, 0, 0, 0).getRGB();
		}

		return original;
	}
	
	private boolean showHeads() {
		return !(TabEditorMod.getInstance().isToggled() && !TabEditorMod.getInstance().getHeadSetting().isToggled());
	}
}