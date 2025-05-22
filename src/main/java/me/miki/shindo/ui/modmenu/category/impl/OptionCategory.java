package me.miki.shindo.ui.modmenu.category.impl;

import me.miki.shindo.Shindo;
import me.miki.shindo.features.options.Option;
import me.miki.shindo.helpers.MathHelper;
import me.miki.shindo.helpers.hud.ScrollHelper;
import me.miki.shindo.helpers.render.GLHelper;
import me.miki.shindo.ui.modmenu.category.Category;
import me.miki.shindo.ui.modmenu.impl.Panel;
import me.miki.shindo.ui.modmenu.impl.sidebar.options.Options;
import me.miki.shindo.ui.modmenu.impl.sidebar.options.type.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OptionCategory extends Category {

    private final ArrayList<Options> optionsList = new ArrayList<>();
    private final ScrollHelper scrollHelperOptions = new ScrollHelper(0, 300, 35, 300);

    public OptionCategory(Panel panel) {
        super(panel);
        setName("Settings");
        setIcon("icon/button/sidebar/settings.png");
        setScrollHelper(scrollHelperOptions);
        setValue(2);
    }

    @Override
    public void initGui() {
        // 1. Preserva o estado de expansão
        Map<String, Boolean> expandedStates = new HashMap<>();
        for (Options o : optionsList) {
            if (o instanceof me.miki.shindo.ui.modmenu.impl.sidebar.options.type.Category) {
                expandedStates.put(o.getOption().getName(), ((me.miki.shindo.ui.modmenu.impl.sidebar.options.type.Category) o).isExpanded());
            }
        }

        optionsList.clear();
        int addOptionY = 10;
        me.miki.shindo.ui.modmenu.impl.sidebar.options.type.Category currentCategory = null;

        for (Option option : Shindo.getInstance().getOptionManager().getOptions()) {
            if (option.getMode().equals("Category")) {
                currentCategory = new me.miki.shindo.ui.modmenu.impl.sidebar.options.type.Category(option, getPanel(), addOptionY);

                // Restaurar o estado expandido anterior, se existir
                if (expandedStates.containsKey(option.getName())) {
                    currentCategory.setExpanded(expandedStates.get(option.getName()));
                }

                optionsList.add(currentCategory);
                addOptionY += currentCategory.getH();
                continue;
            }

            if (currentCategory != null && !currentCategory.isExpanded()) {
                continue;
            }
            Options opt = null;
            switch (option.getMode()) {
                case "CheckBox":
                    opt = new CheckBox(option, getPanel(), addOptionY);
                    break;
                case "Slider":
                    opt = new Slider(option, getPanel(), addOptionY);
                    break;
                case "ModePicker":
                    opt = new ModePicker(option, getPanel(), addOptionY);
                    break;
                case "ColorPicker":
                    opt = new ColorPicker(option, getPanel(), addOptionY);
                    break;
                case "Keybinding":
                    opt = new Keybinding(option, getPanel(), addOptionY);
                    break;
                default:
                    opt = null;
                    break;
            }

            if (opt != null) {
                optionsList.add(opt);
                addOptionY += opt.getH();
            }
        }
    }

    @Override
    public void initCategory() {
        // 1. Preserva o estado de expansão
        Map<String, Boolean> expandedStates = new HashMap<>();
        for (Options o : optionsList) {
            if (o instanceof me.miki.shindo.ui.modmenu.impl.sidebar.options.type.Category) {
                expandedStates.put(o.getOption().getName(), ((me.miki.shindo.ui.modmenu.impl.sidebar.options.type.Category) o).isExpanded());
            }
        }

        optionsList.clear();
        int addOptionY = 10;
        me.miki.shindo.ui.modmenu.impl.sidebar.options.type.Category currentCategory = null;

        for (Option option : Shindo.getInstance().getOptionManager().getOptions()) {
            if (option.getMode().equals("Category")) {
                currentCategory = new me.miki.shindo.ui.modmenu.impl.sidebar.options.type.Category(option, getPanel(), addOptionY);

                // Restaurar o estado expandido anterior, se existir
                if (expandedStates.containsKey(option.getName())) {
                    currentCategory.setExpanded(expandedStates.get(option.getName()));
                }

                optionsList.add(currentCategory);
                addOptionY += currentCategory.getH();
                continue;
            }

            if (currentCategory != null && !currentCategory.isExpanded()) {
                continue;
            }
            Options opt = null;
            switch (option.getMode()) {
                case "CheckBox":
                    opt = new CheckBox(option, getPanel(), addOptionY);
                    break;
                case "Slider":
                    opt = new Slider(option, getPanel(), addOptionY);
                    break;
                case "ModePicker":
                    opt = new ModePicker(option, getPanel(), addOptionY);
                    break;
                case "ColorPicker":
                    opt = new ColorPicker(option, getPanel(), addOptionY);
                    break;
                case "Keybinding":
                    opt = new Keybinding(option, getPanel(), addOptionY);
                    break;
                default:
                    opt = null;
                    break;
            }

            if (opt != null) {
                optionsList.add(opt);
                addOptionY += opt.getH();
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY) {
        GLHelper.startScissor(getPanel().getX(), getPanel().getY() + 60, getPanel().getW(), getPanel().getH() + 240);
        for (Options option : optionsList) {
            option.renderOption(mouseX, mouseY);
        }
        GLHelper.endScissor();

        if (MathHelper.withinBox(getPanel().getX(), getPanel().getY() + 60, getPanel().getW(), getPanel().getH() + 240, mouseX, mouseY)) {
            int height = 0;
            for (Options options : optionsList) {
                height += options.getH();
            }
            scrollHelperOptions.updateScroll();
            scrollHelperOptions.setHeight(height + 35);


            height = 0;
            for (Options options : optionsList) {
                float position = height;
                position += scrollHelperOptions.getCalculatedScroll() + 45;
                options.setY((int) position);
                height += options.getH();
            }
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        for (Options option : optionsList) {
            option.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        for (Options option : optionsList) {
            option.mouseReleased(mouseX, mouseY, state);
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        for (Options option : optionsList) {
            option.keyTyped(typedChar, keyCode);
        }
    }

}
