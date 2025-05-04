package me.miki.shindo.ui.modmenu.category;

import me.miki.shindo.helpers.hud.ScrollHelper;
import me.miki.shindo.ui.modmenu.impl.Panel;
import net.minecraft.client.Minecraft;

public class Category {

    public Minecraft mc = Minecraft.getMinecraft();
    private Panel panel;
    private ScrollHelper scrollHelper;

    private int value;
    private String name;
    private boolean initialized;

    public Category(Panel panel) {
        this.panel = panel;
        initialized = false;
    }

    public void initGui() {}

    public void initCategory() {}

    public void drawScreen(int mouseX, int mouseY) {}

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {}

    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {}

    public void keyTyped(char typedChar, int keyCode) {}

    public Panel getPanel() { return panel; }

    public boolean isInitialized() {
        return initialized;
    }

    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }

    public boolean isCanClose() {
        return panel.isCanClose();
    }

    public void setCanClose(boolean canClose) {
        panel.setCanClose(canClose);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setScrollHelper(ScrollHelper scrollHelper) {
        this.scrollHelper = scrollHelper;
    }
    public ScrollHelper getScrollHelper() {
        return scrollHelper;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

}
