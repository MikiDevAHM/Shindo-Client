package me.miki.shindo.features.lang;

import net.minecraft.util.ResourceLocation;

public enum Language {
    PORTUGUESE("pt-br",TranslateText.PT_BR, new ResourceLocation("shindo/flag/pt_br.png")),
    ENGLISH("en", TranslateText.ENGLISH_US, new ResourceLocation("soar/flag/us.png"));

    private String id;
    private TranslateText nameTranslate;
    private ResourceLocation flag;

    private Language(String id, TranslateText nameTranslate, ResourceLocation flag) {
        this.id = id;
        this.nameTranslate = nameTranslate;
        this.flag = flag;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return nameTranslate.getText();
    }

    public ResourceLocation getFlag() {
        return flag;
    }


    public TranslateText getNameTranslate() {
        return nameTranslate;
    }

    public static Language getLanguageById(String id) {

        for(Language lang : Language.values()) {
            if(lang.getId().equals(id)) {
                return lang;
            }
        }

        return Language.ENGLISH;
    }
}
