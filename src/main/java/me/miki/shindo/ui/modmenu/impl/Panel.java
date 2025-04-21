/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package me.miki.shindo.ui.modmenu.impl;

import me.miki.shindo.Shindo;
import me.miki.shindo.features.chat.Chat;
import me.miki.shindo.features.mods.Mod;
import me.miki.shindo.features.mods.Type;
import me.miki.shindo.features.options.Option;
import me.miki.shindo.helpers.MathHelper;
import me.miki.shindo.helpers.ResolutionHelper;
import me.miki.shindo.helpers.animation.Animate;
import me.miki.shindo.helpers.animation.Easing;
import me.miki.shindo.helpers.hud.ScrollHelper;
import me.miki.shindo.helpers.render.GLHelper;
import me.miki.shindo.helpers.render.Helper2D;
import me.miki.shindo.ui.Style;
import me.miki.shindo.ui.modmenu.impl.sidebar.TextBox;
import me.miki.shindo.ui.modmenu.impl.sidebar.chats.Chats;
import me.miki.shindo.ui.modmenu.impl.sidebar.mods.Button;
import me.miki.shindo.ui.modmenu.impl.sidebar.options.Options;
import me.miki.shindo.ui.modmenu.impl.sidebar.options.type.*;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Panel {

    private static final Minecraft mc = Minecraft.getMinecraft();
    private final ArrayList<Button> buttonList = new ArrayList<>();
    private final ArrayList<Options> optionsList = new ArrayList<>();
    private final ArrayList<Chats> chatsList = new ArrayList<>();
    private final String[] sideButtons = {"Mods", "Settings", "Chat", "Patcher", "Graphics", "Performance", "Tweaker"};
    private final Animate animateSideBar = new Animate();
    private final Animate animateTransition = new Animate();
    private final ScrollHelper scrollHelperMods = new ScrollHelper(0, 270, 35, 300);
    private final ScrollHelper scrollHelperOptions = new ScrollHelper(0, 300, 35, 300);
    private final ScrollHelper scrollHelperChats = new ScrollHelper(0, 300, 35, 300);
    private final ScrollHelper scrollHelperTweakers = new ScrollHelper(0, 300, 35, 300);
    private final ScrollHelper scrollHelperPatchers = new ScrollHelper(0, 300, 35, 300);

    private final TextBox textBox = new TextBox("Search", 0, 0, 80, 20);
    private int x, y, w, h;
    private int offsetX, offsetY;
    private boolean dragging;
    private boolean anyButtonOpen;
    private int selected = 0;
    private Type selectedType = Type.All;

    //
    boolean needsRefreshOptions = false;

    public Panel() {
        this.x = ResolutionHelper.getWidth() / 2 - 250;
        this.y = ResolutionHelper.getHeight() / 2 - 150;
        this.w = 530;
        this.h = 30;
        this.offsetX = 0;
        this.offsetY = 0;
        this.dragging = false;

        initButtons();

        initOptions();

        int addChatY = 10;

        for (Chat chat : Shindo.getInstance().getChatManager().getChat()) {
            switch (chat.getMode()) {
                case "CheckBox":
                    me.miki.shindo.ui.modmenu.impl.sidebar.chats.type.CheckBox checkBox = new me.miki.shindo.ui.modmenu.impl.sidebar.chats.type.CheckBox(chat, this, addChatY);
                    chatsList.add(checkBox);
                    addChatY += checkBox.getH();
                    break;
                case "Slider":
                    me.miki.shindo.ui.modmenu.impl.sidebar.chats.type.Slider slider = new me.miki.shindo.ui.modmenu.impl.sidebar.chats.type.Slider(chat, this, addChatY);
                    chatsList.add(slider);
                    addChatY += slider.getH();
                    break;
                case "ModePicker":
                    me.miki.shindo.ui.modmenu.impl.sidebar.chats.type.ModePicker modePicker = new me.miki.shindo.ui.modmenu.impl.sidebar.chats.type.ModePicker(chat, this, addChatY);
                    chatsList.add(modePicker);
                    addChatY += modePicker.getH();
                    break;
                case "ColorPicker":
                    me.miki.shindo.ui.modmenu.impl.sidebar.chats.type.ColorPicker colorPicker = new me.miki.shindo.ui.modmenu.impl.sidebar.chats.type.ColorPicker(chat, this, addChatY);
                    chatsList.add(colorPicker);
                    addChatY += colorPicker.getH();
                    break;
                case "Keybinding":
                    me.miki.shindo.ui.modmenu.impl.sidebar.chats.type.Keybinding keybinding = new me.miki.shindo.ui.modmenu.impl.sidebar.chats.type.Keybinding(chat, this, addChatY);
                    chatsList.add(keybinding);
                    addChatY += keybinding.getH();
                    break;
                case "Category":
                    me.miki.shindo.ui.modmenu.impl.sidebar.chats.type.Category category = new me.miki.shindo.ui.modmenu.impl.sidebar.chats.type.Category(chat, this, addChatY);
                    chatsList.add(category);
                    addChatY += category.getH();
                    break;
            }
        }

        animateSideBar.setEase(Easing.CUBIC_IN_OUT).setMin(0).setMax(40).setSpeed(200);
        animateTransition.setEase(Easing.CUBIC_IN_OUT).setMin(0).setMax(300).setSpeed(500);
    }

    public void initOptions() {
        // 1. Preserva o estado de expansão
        Map<String, Boolean> expandedStates = new HashMap<>();
        for (Options o : optionsList) {
            if (o instanceof Category) {
                expandedStates.put(o.getOption().getName(), ((Category) o).isExpanded());
            }
        }

        optionsList.clear();
        int addOptionY = 10;
        Category currentCategory = null;

        for (Option option : Shindo.getInstance().getOptionManager().getOptions()) {
            if (option.getMode().equals("Category")) {
                currentCategory = new Category(option, this, addOptionY);

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
                case "CheckBox" :
                    opt = new CheckBox(option, this, addOptionY);
                    break;
                case "Slider":
                    opt = new Slider(option, this, addOptionY);
                    break;
                case "ModePicker":
                    opt = new ModePicker(option, this, addOptionY);
                    break;
                case "ColorPicker":
                    opt = new ColorPicker(option, this, addOptionY);
                    break;
                case "CellGrid":
                    opt = new CellGrid(option, this, addOptionY);
                    break;
                case "Keybinding":
                    opt = new Keybinding(option, this, addOptionY);
                    break;
                default: opt = null;
                    break;
            };

            if (opt != null) {
                optionsList.add(opt);
                addOptionY += opt.getH();
            }
        }
    }

    public void initButtons() {
        buttonList.clear();
        scrollHelperMods.setScrollStep(0);
        int addButtonX = 70;
        int addButtonY = 0;
        for (Mod mod : Shindo.getInstance().getModManager().getMods()) {
            if (selectedType.equals(mod.getType()) || selectedType.equals(Type.All)) {
                if (textBox.getText().isEmpty() || mod.getName().toLowerCase().contains(textBox.getText().toLowerCase())) {
                    Button button = new Button(mod, this, addButtonX, addButtonY);
                    buttonList.add(button);
                    addButtonY += button.getH() + 3;
                }
            }
        }
    }

    /**
     * Renders the panel background, the sidebar and all the buttons
     *
     * @param mouseX The current X position of the mouse
     * @param mouseY The current Y position of the mouse
     */

    public void renderPanel(int mouseX, int mouseY) {
        boolean roundedCorners = Shindo.getInstance().getOptionManager().getOptionByName("Rounded Corners").isCheckToggled();
        int color = Shindo.getInstance().getOptionManager().getOptionByName("Color").getColor().getRGB();
        Helper2D.drawRoundedRectangle(x, y + 30, w, h - 28, 2, Style.getColorTheme(2).getRGB(), 0);
        Helper2D.drawRoundedRectangle(x,
                selected == 0 ? y + 60 : y + 30, w,
                selected == 0 ? h + 240 : h + 270, 2,
                Style.getColorTheme(2).getRGB(), 0
        );
        Helper2D.drawRectangle(x, y + 30, w, 30, Style.getColorTheme(3).getRGB());
        Helper2D.drawRoundedRectangle(x, y + 30, 70, h + 270, 2, Style.getColorTheme(4).getRGB(), 0);

        boolean hovered = MathHelper.withinBox(x + w - 30, y - 6, 25, 25, mouseX, mouseY);
        Helper2D.drawRoundedRectangle(x + w - 30, y - 6, 25, 25, 2, Style.getColorTheme(hovered ? 7 : 5).getRGB(), roundedCorners ? 0 : -1);
        Helper2D.drawPicture(x + w - 30, y - 6, 25, 25, color, "icon/cross.png");

        // Helper2D.drawPicture(x + 2, y - 1, 35, 35, color, "cloudlogo.png");
        // Shindo.getInstance().getFontHelper().size40.drawString(Shindo.NAME + " Client", x + 37, y + 6, color);

        /*
        Buttons are only drawn if the Sidebar is on the mods tab
         */

        animateTransition.update();
        scrollHelperMods.update();
        scrollHelperOptions.update();
        scrollHelperChats.update();

        Shindo.getInstance().getMessageHelper().renderMessage();

        switch (selected) {
            case 0:
                int offset = 0;
                for (Type type : Type.values()) {
                    String text = type.name();
                    int length = Shindo.getInstance().getFontHelper().size20.getStringWidth(text);
                    Helper2D.drawRoundedRectangle(
                            x + offset + 5 + 75,
                            y + h + 5,
                            length + 25,
                            20, 2,
                            Style.getColorTheme(selectedType.equals(type) ? 5 : 4).getRGB(),
                            roundedCorners ? 0 : -1
                    );
                    Helper2D.drawPicture(x + offset + 8 + 75, y + h + 8, 15, 15, -1, "icon/" + type.getIcon());
                    Shindo.getInstance().getFontHelper().size20.drawString(text, x + offset + 26 + 75, y + h + 11, -1);
                    offset += length + 30;
                }

                textBox.renderTextBox(x + w - textBox.getW() - 5, y + h + 5, mouseX, mouseY);

                GLHelper.startScissor(x, y + 60, w, h + 240);
                for (Button button : buttonList) {
                    button.renderButton(mouseX, mouseY);
                }
                GLHelper.endScissor();

                if (MathHelper.withinBox(x, y + 30, w, h + 270, mouseX, mouseY)) {
                    int height = 0;
                    for (Button button : buttonList) {
                        height += button.getH() + 3;
                    }
                    scrollHelperMods.updateScroll();
                    scrollHelperMods.setHeight(height);

                    int count = 0;
                    for (Button button : buttonList) {
                        float position = scrollHelperMods.getCalculatedScroll();
                        position += count * (button.getH() + 3);
                        button.setY((int) position);
                        count++;
                    }
                }
                break;
            case 1:
                GLHelper.startScissor(x, y + 60, w, h + 240);
                for (Options option : optionsList) {
                    option.renderOption(mouseX, mouseY);
                }
                GLHelper.endScissor();

                if (MathHelper.withinBox(x, y + 60, w, h + 240, mouseX, mouseY)) {
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
                break;
            case 2:
                GLHelper.startScissor(x, y + 60, w, h + 240);
                for (Chats chat : chatsList) {
                    chat.renderChats(mouseX, mouseY);
                }
                GLHelper.endScissor();

                if (MathHelper.withinBox(x, y + 60, w, h + 240, mouseX, mouseY)) {
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
                break;
            case 3:

                break;
            case 4:

                break;
            case 5:

                break;
            case 6:

                break;
        }
        /*
        Draws the sidebar with the mods and settings tab
         */

        animateSideBar.update();
        drawSideBar();

        if (needsRefreshOptions) {
            initOptions();
            needsRefreshOptions = false;
        }
    }

    private void drawSideBar() {
        boolean roundedCorners = Shindo.getInstance().getOptionManager().getOptionByName("Rounded Corners").isCheckToggled();
        int color = Shindo.getInstance().getOptionManager().getOptionByName("Color").getColor().getRGB();

        int space = 0;
        int value = 0;
        for (int i = 0 ; i <= 6; i++) {
            Helper2D.drawRoundedRectangle(x + 20, y  + 45 + i * 40 + MathHelper.addIntToInt(space, 1, i), 30, 30, 2, Style.getColorTheme(6).getRGB(), roundedCorners ? 0 : -1);
        }

        Helper2D.drawRoundedRectangle(x + 20 , y + 45 + MathHelper.addIntToInt(value, 1, selected)  * 40 + MathHelper.addIntToInt(space, 1, selected) , 30 , 30, 2, Style.getColorTheme(8).getRGB(), roundedCorners ? 0 : -1);

        int index = 0;
        int space1 = 0;
        for (String button : sideButtons) {

            //Shindo.getInstance().getFontHelper().size15.drawString(button, x + 25 - Shindo.getInstance().getFontHelper().size15.getStringWidth(button) / 2f + index * 40 + MathHelper.addIntToInt(space1, 5, index), y + 18 , color);
            Helper2D.drawPicture(x + 28 , y + 52 + index * 40 + MathHelper.addIntToInt(space1, 1, index), 15, 15, color, "icon/button/sidebar/" + button.toLowerCase() + ".png");

            index++;
        }
    }

    /**
     * Closes the modmenu and opens the editor if the close button is pressed
     * Sets the "selected" variable to whatever tab is pressed in the sidebar
     *
     * @param mouseX      The current X position of the mouse
     * @param mouseY      The current Y position of the mouse
     * @param mouseButton The current mouse button which is pressed
     */

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0) {
            int index = 0;
            int space = 0;
            for (String button : sideButtons) {
                if (MathHelper.withinBox(x + 20 , y + 45 + index * 40 + MathHelper.addIntToInt(space, 1, index) , 35 , 35, mouseX, mouseY)) {
                    if (selected != index) {
                        //animateSideBar.reset();
                        animateTransition.reset();
                    }
                    selected = index;
                }
                index++;
            }

            int offset = 0;
            for (Type type : Type.values()) {
                String text = type.name();
                int length = Shindo.getInstance().getFontHelper().size20.getStringWidth(text);
                if (MathHelper.withinBox(x + offset + 5 + 75, y + h + 5, length + 25, 20, mouseX, mouseY)) {
                    selectedType = type;
                    scrollHelperMods.setScrollStep(0);
                    initButtons();
                }
                offset += length + 30;
            }


            if (MathHelper.withinBox(x + w - 30, y - 6, 25, 25, mouseX, mouseY)) {
                mc.displayGuiScreen(Shindo.getInstance().getHudEditor());
            }
        }

        switch (selected) {
            case 0:
                for (Button button : buttonList) {
                    button.mouseClicked(mouseX, mouseY, mouseButton);
                }
            break;
            case 1:
                for (Options option : optionsList) {
                    option.mouseClicked(mouseX, mouseY, mouseButton);
                }
            break;
            case 2:
                for (Chats chats : chatsList) {
                    chats.mouseClicked(mouseX, mouseY, mouseButton);
                }
                break;
            case 3:

                break;
            case 4:

                break;
        }
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
       switch (selected) {
           case 0:
                for (Button button : buttonList) {
                    button.mouseReleased(mouseX, mouseY, state);
                }
                break;
           case 1:
                for (Options option : optionsList) {
                    option.mouseReleased(mouseX, mouseY, state);
                }
                break;
           case 2:
               for (Chats chats : chatsList) {
                   chats.mouseReleased(mouseX, mouseY, state);
               }
               break;
           case 3:

               break;
           case 4:

               break;
        }
    }

    public void keyTyped(char typedChar, int keyCode) {
        if(isAnyButtonOpen()) {
            for (Button button : buttonList) {
                button.keyTyped(typedChar, keyCode);
            }
            for (Options option : optionsList) {
                option.keyTyped(typedChar, keyCode);
            }
        } else {
            textBox.keyTyped(typedChar, keyCode);
            initButtons();
        }
    }

    public void initGui() {
        setX(ResolutionHelper.getWidth() / 2 - 250);
    }

    /**
     * Updates the position of the panel
     *
     * @param mouseX The current X position of the mouse
     * @param mouseY The current Y position of the mouse
     */

    public void updatePosition(int mouseX, int mouseY) {
        if (isDragging()) {
            setX(mouseX - offsetX);
            setY(mouseY - offsetY);
        }
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(int offsetX) {
        this.offsetX = offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(int offsetY) {
        this.offsetY = offsetY;
    }

    public boolean isDragging() {
        return dragging;
    }

    public void setDragging(boolean dragging) {
        this.dragging = dragging;
    }

    public boolean isAnyButtonOpen() {
        return anyButtonOpen;
    }

    public void setAnyButtonOpen(boolean anyButtonOpen) {
        this.anyButtonOpen = anyButtonOpen;
    }

    public void setNeedsRefreshOptions(boolean needsRefreshOptions) {
        this.needsRefreshOptions = needsRefreshOptions;
    }
}
