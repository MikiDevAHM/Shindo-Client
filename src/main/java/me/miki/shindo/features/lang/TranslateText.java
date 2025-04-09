package me.miki.shindo.features.lang;

public enum TranslateText {

    // MODS

    // OPTIONS

    //SETTINGS

    //OTHER
    PORTUGUESE("text.portuguese"),
    ENGLISH("text.english"),

    PT_BR("text.pt_br"),
    ENGLISH_US("text.english_us");
    private String key, text;

    private TranslateText(String key) {
        this.key = key;
    }

    public String getText() {
        return text == null ? "null" : text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getKey() {
        return key;
    }
}