package me.miki.shindo.ui.modmenu.category.impl;

import me.miki.shindo.Shindo;
import me.miki.shindo.features.chat.Chat;
import me.miki.shindo.helpers.MathHelper;
import me.miki.shindo.helpers.hud.ScrollHelper;
import me.miki.shindo.helpers.render.GLHelper;
import me.miki.shindo.ui.modmenu.category.Category;
import me.miki.shindo.ui.modmenu.impl.Panel;
import me.miki.shindo.ui.modmenu.impl.sidebar.chats.Chats;
import me.miki.shindo.ui.modmenu.impl.sidebar.chats.type.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChatCategory extends Category {

    private final ArrayList<Chats> chatsList = new ArrayList<>();
    private final ScrollHelper scrollHelperChats = new ScrollHelper(0, 300, 35, 300);

    public ChatCategory(Panel panel) {
        super(panel);
        setName("Chat Options");
        setIcon("icon/button/sidebar/chat.png");
        setScrollHelper(scrollHelperChats);
        setValue(3);
    }

    @Override
    public void initGui() {
        // 1. Preserva o estado de expansão
        Map<String, Boolean> expandedStates = new HashMap<>();
        for (Chats o : chatsList) {
            if (o instanceof me.miki.shindo.ui.modmenu.impl.sidebar.chats.type.Category) {
                expandedStates.put(o.getChat().getName(), ((me.miki.shindo.ui.modmenu.impl.sidebar.chats.type.Category) o).isExpanded());
            }
        }

        chatsList.clear();
        int addOptionY = 10;
        me.miki.shindo.ui.modmenu.impl.sidebar.chats.type.Category currentCategory = null;

        for (Chat chat : Shindo.getInstance().getChatManager().getChat()) {
            if (chat.getMode().equals("Category")) {
                currentCategory = new me.miki.shindo.ui.modmenu.impl.sidebar.chats.type.Category(chat, getPanel(), addOptionY);

                // Restaurar o estado expandido anterior, se existir
                if (expandedStates.containsKey(chat.getName())) {
                    currentCategory.setExpanded(expandedStates.get(chat.getName()));
                }

                chatsList.add(currentCategory);
                addOptionY += currentCategory.getH();
                continue;
            }

            if (currentCategory != null && !currentCategory.isExpanded()) {
                continue;
            }
            Chats opt = null;
            switch (chat.getMode()) {
                case "CheckBox":
                    opt = new CheckBox(chat, getPanel(), addOptionY);
                    break;
                case "Slider":
                    opt = new Slider(chat, getPanel(), addOptionY);
                    break;
                case "ModePicker":
                    opt = new ModePicker(chat, getPanel(), addOptionY);
                    break;
                case "ColorPicker":
                    opt = new ColorPicker(chat, getPanel(), addOptionY);
                    break;
                case "Keybinding":
                    opt = new Keybinding(chat, getPanel(), addOptionY);
                    break;
                default:
                    opt = null;
                    break;
            }

            if (opt != null) {
                chatsList.add(opt);
                addOptionY += opt.getH();
            }
        }
    }

    @Override
    public void initCategory() {
        // 1. Preserva o estado de expansão
        Map<String, Boolean> expandedStates = new HashMap<>();
        for (Chats o : chatsList) {
            if (o instanceof me.miki.shindo.ui.modmenu.impl.sidebar.chats.type.Category) {
                expandedStates.put(o.getChat().getName(), ((me.miki.shindo.ui.modmenu.impl.sidebar.chats.type.Category) o).isExpanded());
            }
        }

        chatsList.clear();
        int addOptionY = 10;
        me.miki.shindo.ui.modmenu.impl.sidebar.chats.type.Category currentCategory = null;

        for (Chat chat : Shindo.getInstance().getChatManager().getChat()) {
            if (chat.getMode().equals("Category")) {
                currentCategory = new me.miki.shindo.ui.modmenu.impl.sidebar.chats.type.Category(chat, getPanel(), addOptionY);

                // Restaurar o estado expandido anterior, se existir
                if (expandedStates.containsKey(chat.getName())) {
                    currentCategory.setExpanded(expandedStates.get(chat.getName()));
                }

                chatsList.add(currentCategory);
                addOptionY += currentCategory.getH();
                continue;
            }

            if (currentCategory != null && !currentCategory.isExpanded()) {
                continue;
            }
            Chats opt = null;
            switch (chat.getMode()) {
                case "CheckBox":
                    opt = new CheckBox(chat, getPanel(), addOptionY);
                    break;
                case "Slider":
                    opt = new Slider(chat, getPanel(), addOptionY);
                    break;
                case "ModePicker":
                    opt = new ModePicker(chat, getPanel(), addOptionY);
                    break;
                case "ColorPicker":
                    opt = new ColorPicker(chat, getPanel(), addOptionY);
                    break;
                case "Keybinding":
                    opt = new Keybinding(chat, getPanel(), addOptionY);
                    break;
                default:
                    opt = null;
                    break;
            }

            if (opt != null) {
                chatsList.add(opt);
                addOptionY += opt.getH();
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY) {
        GLHelper.startScissor(getPanel().getX(), getPanel().getY() + 60, getPanel().getW(), getPanel().getH() + 240);
        for (Chats chat : chatsList) {
            chat.renderChats(mouseX, mouseY);
        }
        GLHelper.endScissor();

        if (MathHelper.withinBox(getPanel().getX(), getPanel().getY() + 60, getPanel().getW(), getPanel().getH() + 240, mouseX, mouseY)) {
            int height = 0;
            for (Chats chats : chatsList) {
                height += chats.getH();
            }
            scrollHelperChats.updateScroll();
            scrollHelperChats.setHeight(height + 35);


            height = 0;
            for (Chats chats : chatsList) {
                float position = height;
                position += scrollHelperChats.getCalculatedScroll() + 45;
                chats.setY((int) position);
                height += chats.getH();
            }
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        for (Chats chats : chatsList) {
            chats.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        for (Chats chats : chatsList) {
            chats.mouseReleased(mouseX, mouseY, state);
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        for (Chats chats : chatsList) {
            chats.keyTyped(typedChar, keyCode);
        }
    }
}
