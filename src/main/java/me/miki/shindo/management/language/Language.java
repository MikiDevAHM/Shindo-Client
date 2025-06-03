package me.miki.shindo.management.language;

import me.miki.shindo.utils.animation.simple.SimpleAnimation;
import net.minecraft.util.ResourceLocation;

public enum Language {
	JAPANESE("ja-jp", "日本語 (日本)", new ResourceLocation("shindo/flag/ja.png")),
	CHINESE("zh-cn", "中文 (中國)", new ResourceLocation("shindo/flag/cn.png")),
	ENGLISHGB("en-gb", "English (United Kingdom)", new ResourceLocation("shindo/flag/gb.png")),
	ENGLISH("en-us", "English (United States)", new ResourceLocation("shindo/flag/us.png")),
	FRENCH("fr-fr", "Français (France)", new ResourceLocation("shindo/flag/fr.png")),
	SPANISH("es-es", "Español (España)", new ResourceLocation("shindo/flag/es.png")),
	VIETNAMESE("vi-vn", "Tiếng Việt (Việt Nam)", new ResourceLocation("shindo/flag/vn.png")),
	RUSSIAN("ru-ru", "русский (россия)", new ResourceLocation("shindo/flag/ru.png")),
	PORTUGESE("pt-pt", "Português (Portugal)", new ResourceLocation("shindo/flag/pt.png")),
	LOLCAT("lc-koc", "LOLCAT (Kinduim ov catos)", new ResourceLocation("shindo/flag/koc.png")),
	WOOFLANG("wo-yap", "Wooflang (Yappington)", new ResourceLocation("shindo/flag/wo.png"));
	
	private SimpleAnimation animation = new SimpleAnimation();
	
	private String id;
	private String nameTranslate;
	private ResourceLocation flag;
	
	private Language(String id, String nameTranslate, ResourceLocation flag) {
		this.id = id;
		this.nameTranslate = nameTranslate;
		this.flag = flag;
	}

	public String getId() {
		return id;
	}
	
	public String getName() {
		return nameTranslate;
	}
	
	public ResourceLocation getFlag() {
		return flag;
	}

	public SimpleAnimation getAnimation() {
		return animation;
	}

	public String getNameTranslate() {
		return nameTranslate;
	}

	public static Language getLanguageById(String id) {
		
		for(Language lang : Language.values()) {
			if(lang.getId().equals(id)) {
				return lang;
			}
		}
		
		return Language.ENGLISHGB;
	}
}
