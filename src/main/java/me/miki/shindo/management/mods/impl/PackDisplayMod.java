package me.miki.shindo.management.mods.impl;

import java.io.IOException;
import java.util.List;

import me.miki.shindo.Shindo;
import me.miki.shindo.injection.interfaces.IMixinMinecraft;
import me.miki.shindo.management.event.EventTarget;
import me.miki.shindo.management.event.impl.EventRender2D;
import me.miki.shindo.management.event.impl.EventSwitchTexture;
import me.miki.shindo.management.language.TranslateText;
import me.miki.shindo.management.mods.HUDMod;
import me.miki.shindo.management.mods.settings.impl.BooleanSetting;
import me.miki.shindo.management.nanovg.NanoVGManager;
import me.miki.shindo.utils.ColorUtils;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.ResourcePackRepository;
import net.minecraft.util.ResourceLocation;

public class PackDisplayMod extends HUDMod {

	private IResourcePack pack;
	private ResourceLocation currentPack;
	private final ResourcePackRepository resourcePackRepository = mc.getResourcePackRepository();
	private List<ResourcePackRepository.Entry> packs = resourcePackRepository.getRepositoryEntries();
	
	private BooleanSetting compactSetting = new BooleanSetting(TranslateText.COMPACT, this, false);
	
	public PackDisplayMod() {
		super(TranslateText.PACK_DISPLAY, TranslateText.PACK_DISPLAY_DESCRIPTION);
	}

	@Override
	public void onEnable() {
		super.onEnable();
		this.loadTexture();
	}
	
	@EventTarget
	public void onRender2D(EventRender2D event) {
		
		Shindo instance = Shindo.getInstance();
		NanoVGManager nvg = instance.getNanoVGManager();
		
		if(pack == null) {
			pack = this.getCurrentPack();
		}
		
		nvg.setupAndDraw(() -> drawNanoVG());
	}
	
	private void drawNanoVG() {
		
		String name = ColorUtils.removeColorCode(pack.getPackName()).replace(".zip", "");
		
		float stringWidth = this.getTextWidth(name, 9, getHudFont(1));
		boolean compact = compactSetting.isToggled();
		int imgSize = compact ? 12 : 30;
		float imgX = compact ? 5F : 4.5F;
		float imgY = compact ? 3F : 4.5F;
		float textX = compact ? 20.5F : 38F;
		float textY = compact ? 5.5F : 7F;
		
		this.drawBackground((compact ? 24 : 44) + stringWidth, compact ? 18 : 39);
		
		this.drawRoundedImage(mc.getTextureManager().getTexture(currentPack).getGlTextureId(), imgX, imgY, imgSize, imgSize, compact ? 2 : 4);
		this.drawText(name, textX, textY, 9, getHudFont(1));
		
		this.setWidth((int) ((compact ? 24 : 44) + stringWidth));
		this.setHeight(compact ? 18 : 39);
	}
	
	@EventTarget
	public void onSwitchTexture(EventSwitchTexture event) {
		packs = resourcePackRepository.getRepositoryEntries();
		pack = this.getCurrentPack();
		this.loadTexture();
	}
	
	private void loadTexture() {
		DynamicTexture dynamicTexture;
		try {
			dynamicTexture = new DynamicTexture(getCurrentPack().getPackImage());
		} catch (Exception e) {
			try {
				dynamicTexture = new DynamicTexture(((IMixinMinecraft)mc).getMcDefaultResourcePack().getPackImage());
			} catch (IOException e1) {
				dynamicTexture = TextureUtil.missingTexture;
			}
		}
		
		this.currentPack = mc.getTextureManager().getDynamicTextureLocation("texturepackicon", dynamicTexture);
	}
	
	private IResourcePack getCurrentPack() {
		if (packs.size() > 0) {
			final IResourcePack last = packs.get(packs.size() - 1).getResourcePack();
			return last;
		}
		return ((IMixinMinecraft)mc).getMcDefaultResourcePack();
	}
}
