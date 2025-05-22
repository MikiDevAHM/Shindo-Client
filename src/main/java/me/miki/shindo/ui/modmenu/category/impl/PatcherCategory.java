package me.miki.shindo.ui.modmenu.category.impl;

import me.miki.shindo.Shindo;
import me.miki.shindo.features.patcher.Patcher;
import me.miki.shindo.helpers.MathHelper;
import me.miki.shindo.helpers.hud.ScrollHelper;
import me.miki.shindo.helpers.render.GLHelper;
import me.miki.shindo.ui.modmenu.category.Category;
import me.miki.shindo.ui.modmenu.impl.Panel;
import me.miki.shindo.ui.modmenu.impl.sidebar.patchers.Patchers;
import me.miki.shindo.ui.modmenu.impl.sidebar.patchers.type.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PatcherCategory extends Category {

    private final ScrollHelper scrollHelperPatcher = new ScrollHelper(0, 300, 35, 300);
    private final ArrayList<Patchers> patchersList = new ArrayList<>();

    public PatcherCategory(Panel panel) {
        super(panel);
        setName("Patcher");
        setIcon("icon/button/sidebar/patcher.png");
        setScrollHelper(scrollHelperPatcher);
        setValue(4);
    }


    @Override
    public void initGui() {
        // 1. Preserva o estado de expansão
        Map<String, Boolean> expandedStates = new HashMap<>();
        for (Patchers o : patchersList) {
            if (o instanceof me.miki.shindo.ui.modmenu.impl.sidebar.patchers.type.Category) {
                expandedStates.put(o.getPatcher().getName(), ((me.miki.shindo.ui.modmenu.impl.sidebar.patchers.type.Category) o).isExpanded());
            }
        }

        patchersList.clear();
        int addOptionY = 10;
        me.miki.shindo.ui.modmenu.impl.sidebar.patchers.type.Category currentCategory = null;

        for (Patcher patcher : Shindo.getInstance().getPatcherManager().getPatcher()) {
            if (patcher.getMode().equals("Category")) {
                currentCategory = new me.miki.shindo.ui.modmenu.impl.sidebar.patchers.type.Category(patcher, getPanel(), addOptionY);

                // Restaurar o estado expandido anterior, se existir
                if (expandedStates.containsKey(patcher.getName())) {
                    currentCategory.setExpanded(expandedStates.get(patcher.getName()));
                }

                patchersList.add(currentCategory);
                addOptionY += currentCategory.getH();
                continue;
            }

            if (currentCategory != null && !currentCategory.isExpanded()) {
                continue;
            }
            Patchers opt = null;
            switch (patcher.getMode()) {
                case "CheckBox":
                    opt = new CheckBox(patcher, getPanel(), addOptionY);
                    break;
                case "Slider":
                    opt = new Slider(patcher, getPanel(), addOptionY);
                    break;
                case "ModePicker":
                    opt = new ModePicker(patcher, getPanel(), addOptionY);
                    break;
                case "ColorPicker":
                    opt = new ColorPicker(patcher, getPanel(), addOptionY);
                    break;
                case "Keybinding":
                    opt = new Keybinding(patcher, getPanel(), addOptionY);
                    break;
                default:
                    opt = null;
                    break;
            }

            if (opt != null) {
                patchersList.add(opt);
                addOptionY += opt.getH();
            }
        }
    }

    @Override
    public void initCategory() {
        // 1. Preserva o estado de expansão
        Map<String, Boolean> expandedStates = new HashMap<>();
        for (Patchers o : patchersList) {
            if (o instanceof me.miki.shindo.ui.modmenu.impl.sidebar.patchers.type.Category) {
                expandedStates.put(o.getPatcher().getName(), ((me.miki.shindo.ui.modmenu.impl.sidebar.patchers.type.Category) o).isExpanded());
            }
        }

        patchersList.clear();
        int addOptionY = 10;
        me.miki.shindo.ui.modmenu.impl.sidebar.patchers.type.Category currentCategory = null;

        for (Patcher patcher : Shindo.getInstance().getPatcherManager().getPatcher()) {
            if (patcher.getMode().equals("Category")) {
                currentCategory = new me.miki.shindo.ui.modmenu.impl.sidebar.patchers.type.Category(patcher, getPanel(), addOptionY);

                // Restaurar o estado expandido anterior, se existir
                if (expandedStates.containsKey(patcher.getName())) {
                    currentCategory.setExpanded(expandedStates.get(patcher.getName()));
                }

                patchersList.add(currentCategory);
                addOptionY += currentCategory.getH();
                continue;
            }

            if (currentCategory != null && !currentCategory.isExpanded()) {
                continue;
            }
            Patchers opt = null;
            switch (patcher.getMode()) {
                case "CheckBox":
                    opt = new CheckBox(patcher, getPanel(), addOptionY);
                    break;
                case "Slider":
                    opt = new Slider(patcher, getPanel(), addOptionY);
                    break;
                case "ModePicker":
                    opt = new ModePicker(patcher, getPanel(), addOptionY);
                    break;
                case "ColorPicker":
                    opt = new ColorPicker(patcher, getPanel(), addOptionY);
                    break;
                case "Keybinding":
                    opt = new Keybinding(patcher, getPanel(), addOptionY);
                    break;
                default:
                    opt = null;
                    break;
            }

            if (opt != null) {
                patchersList.add(opt);
                addOptionY += opt.getH();
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY) {
        GLHelper.startScissor(getPanel().getX(), getPanel().getY() + 60, getPanel().getW(), getPanel().getH() + 240);
        for (Patchers patcher : patchersList) {
            patcher.renderPatchers(mouseX, mouseY);
        }
        GLHelper.endScissor();

        if (MathHelper.withinBox(getPanel().getX(), getPanel().getY() + 60, getPanel().getW(), getPanel().getH() + 240, mouseX, mouseY)) {
            int height = 0;
            for (Patchers patcher : patchersList) {
                height += patcher.getH();
            }
            scrollHelperPatcher.updateScroll();
            scrollHelperPatcher.setHeight(height + 35);


            height = 0;
            for (Patchers patchers : patchersList) {
                float position = height;
                position += scrollHelperPatcher.getCalculatedScroll() + 45;
                patchers.setY((int) position);
                height += patchers.getH();
            }
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        for (Patchers patchers : patchersList) {
            patchers.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        for (Patchers patchers : patchersList) {
            patchers.mouseReleased(mouseX, mouseY, state);
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        for (Patchers patchers : patchersList) {
            patchers.keyTyped(typedChar, keyCode);
        }
    }
}