package me.miki.shindo.ui.modmenu.category.impl;

import me.miki.shindo.helpers.hud.ScrollHelper;
import me.miki.shindo.ui.modmenu.category.Category;
import me.miki.shindo.ui.modmenu.impl.Panel;

public class HomeCategory extends Category {

    private final ScrollHelper scrollHelperHome = new ScrollHelper(0, 300, 35, 300);
    public HomeCategory(Panel panel) {
        super(panel);
        setName("Home");
        setScrollHelper(scrollHelperHome);
        setValue(0);
    }
}
