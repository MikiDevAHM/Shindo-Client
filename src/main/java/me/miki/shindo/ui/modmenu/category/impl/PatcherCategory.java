package me.miki.shindo.ui.modmenu.category.impl;

import me.miki.shindo.helpers.hud.ScrollHelper;
import me.miki.shindo.ui.modmenu.category.Category;
import me.miki.shindo.ui.modmenu.impl.Panel;

public class PatcherCategory extends Category {

    private final ScrollHelper scrollHelperPatcher = new ScrollHelper(0, 300, 35, 300);

    public PatcherCategory(Panel panel) {
        super(panel);
        setName("Patcher");
        setScrollHelper(scrollHelperPatcher);
        setValue(3);
    }
}
