package me.miki.shindo.ui.mods;

import java.util.ArrayList;

import me.miki.shindo.Shindo;
import me.miki.shindo.feature.mod.Mod;
import me.miki.shindo.feature.mod.Type;
import me.miki.shindo.feature.option.Option;
import me.miki.shindo.ui.Style;
import me.miki.shindo.ui.mods.sidebar.TextBox;
import me.miki.shindo.ui.mods.sidebar.mods.Button;
import me.miki.shindo.ui.mods.sidebar.options.Options;
import me.miki.shindo.ui.mods.sidebar.options.type.Category;
import me.miki.shindo.ui.mods.sidebar.options.type.CellGrid;
import me.miki.shindo.ui.mods.sidebar.options.type.CheckBox;
import me.miki.shindo.ui.mods.sidebar.options.type.ColorPicker;
import me.miki.shindo.ui.mods.sidebar.options.type.Keybinding;
import me.miki.shindo.ui.mods.sidebar.options.type.ModePicker;
import me.miki.shindo.ui.mods.sidebar.options.type.Slider;
import me.miki.shindo.util.MathHelper;
import me.miki.shindo.util.ResolutionHelper;
import me.miki.shindo.util.animation.Animate;
import me.miki.shindo.util.animation.Easing;
import me.miki.shindo.util.hud.ScrollHelper;
import me.miki.shindo.util.render.GLHelper;
import me.miki.shindo.util.render.Helper2D;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class Panel {
	
	private Minecraft mc = Minecraft.getMinecraft();
    private final ArrayList<Button> buttonList = new ArrayList<>();
    private final ArrayList<Options> optionsList = new ArrayList<>();
    private final String[] sideButtons = {"Mods", "Settings", "Cosmetics"};
    private final Animate animateSideBar = new Animate();
    private final Animate animateTransition = new Animate();
    private final ScrollHelper scrollHelperMods = new ScrollHelper(0, 270, 35, 300);
    private final ScrollHelper scrollHelperOptions = new ScrollHelper(0, 300, 35, 300);
    private final TextBox textBox = new TextBox("Search", 0, 0, 150, 20);
    private int x, y, w, h;
    private int offsetX, offsetY;
    private boolean dragging;
    private boolean anyButtonOpen;
    private int selected = 0;
    private Type selectedType = Type.All;

    public Panel() {
        this.x = ResolutionHelper.getWidth() / 2 - 250;
        this.y = ResolutionHelper.getHeight() / 2 - 150;
        this.w = 500;
        this.h = 30;
        this.offsetX = 0;
        this.offsetY = 0;
        this.dragging = false;

        initButtons();

        int addOptionY = 10;

        for (Option option : Shindo.getInstance().getOptionManager().getOptions()) {
            switch (option.getMode()) {
                case "CheckBox":
                    CheckBox checkBox = new CheckBox(option, this, addOptionY);
                    optionsList.add(checkBox);
                    addOptionY += checkBox.getH();
                    break;
                case "Slider":
                    Slider slider = new Slider(option, this, addOptionY);
                    optionsList.add(slider);
                    addOptionY += slider.getH();
                    break;
                case "ModePicker":
                    ModePicker modePicker = new ModePicker(option, this, addOptionY);
                    optionsList.add(modePicker);
                    addOptionY += modePicker.getH();
                    break;
                case "ColorPicker":
                    ColorPicker colorPicker = new ColorPicker(option, this, addOptionY);
                    optionsList.add(colorPicker);
                    addOptionY += colorPicker.getH();
                    break;
                case "CellGrid":
                    CellGrid cellGrid = new CellGrid(option, this, addOptionY);
                    optionsList.add(cellGrid);
                    addOptionY += cellGrid.getH();
                    break;
                case "Keybinding":
                    Keybinding keybinding = new Keybinding(option, this, addOptionY);
                    optionsList.add(keybinding);
                    addOptionY += keybinding.getH();
                    break;
                case "Category":
                    Category category = new Category(option, this, addOptionY);
                    optionsList.add(category);
                    addOptionY += category.getH();
                    break;
            }
        }

        animateSideBar.setEase(Easing.LINEAR).setMin(0).setMax(40).setSpeed(200);
        animateTransition.setEase(Easing.CUBIC_IN_OUT).setMin(0).setMax(300).setSpeed(500);
    }

    public void initButtons() {
        buttonList.clear();
        scrollHelperMods.setScrollStep(0);
        int addButtonX = 0;
        int addButtonY = 0;
        int buttonCounter = 0;
        for (Mod mod : Shindo.getInstance().getModManager().getMods()) {
            if (selectedType.equals(mod.getType()) || selectedType.equals(Type.All)) {
                if (textBox.getText().equals("") || mod.getName().toLowerCase().contains(textBox.getText().toLowerCase())) {
                    Button button = new Button(mod, this, addButtonX, addButtonY);
                    buttonList.add(button);
                    buttonCounter++;
                    addButtonX += button.getW() + 3;
                    if (buttonCounter % 4 == 0) {
                        addButtonX = 0;
                        addButtonY += button.getH() + 3;
                    }
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
        Helper2D.drawRoundedRectangle(x, y, w, h, 2, Style.getColor(80).getRGB(), roundedCorners ? 1 : -1);
        Helper2D.drawRoundedRectangle(x,
                selected == 1 ? y + 30 : y + 60, w,
                selected == 1 ? h + 270 : h + 240, 2,
                Style.getColor(50).getRGB(), roundedCorners ? 2 : -1
        );
        if (selected == 0 || selected == 2)
            Helper2D.drawRectangle(x, y + 30, w, 30, Style.getColor(70).getRGB());

        boolean hovered = MathHelper.withinBox(x + w - 25, y + 5, 20, 20, mouseX, mouseY);
        Helper2D.drawRoundedRectangle(x + w - 25, y + 5, 20, 20, 2, Style.getColor(hovered ? 70 : 50).getRGB(), roundedCorners ? 0 : -1);
        Helper2D.drawPicture(x + w - 25, y + 5, 20, 20, color, "icon/cross.png");

        Helper2D.drawPicture(x + 2, y - 1, 35, 35, 0, "logo.png");
        Shindo.getInstance().getFontHelper().size40.drawString(Shindo.NAME, x + 37, y + 6, color);

        /*
        Buttons are only drawn if the Sidebar is on the mods tab
         */

        animateTransition.update();
        scrollHelperMods.update();
        scrollHelperOptions.update();

        Shindo.getInstance().getMessageHelper().renderMessage();

        if (selected == 0) {
            int offset = 0;
            for (Type type : Type.values()) {
                String text = type.name();
                int length = Shindo.getInstance().getFontHelper().size20.getStringWidth(text);
                Helper2D.drawRoundedRectangle(
                        x + offset + 5,
                        y + h + 5,
                        length + 25,
                        20, 2,
                        Style.getColor(selectedType.equals(type) ? 120 : 50).getRGB(),
                        roundedCorners ? 0 : -1
                );
                Helper2D.drawPicture(x + offset + 8, y + h + 8, 15, 15, -1, "icon/" + type.getIcon());
                Shindo.getInstance().getFontHelper().size20.drawString(text, x + offset + 26, y + h + 11, -1);
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
                int index = 0;
                for (Button button : buttonList) {
                    if (index % 4 == 0) {
                        height += button.getH() + 3;
                    }
                    index++;
                }
                scrollHelperMods.updateScroll();
                scrollHelperMods.setHeight(height);

                index = 0;
                int count = 0;
                for (Button button : buttonList) {
                    float position = scrollHelperMods.getCalculatedScroll();
                    position += count * (button.getH() + 3);
                    button.setY((int) position);
                    index++;
                    if (index % 4 == 0) {
                        count++;
                    }
                }
            }
        } else if (selected == 1) {
            GLHelper.startScissor(x, y + 30, w, h + 270);
            for (Options option : optionsList) {
                option.renderOption(mouseX, mouseY);
            }
            GLHelper.endScissor();

            if (MathHelper.withinBox(x, y + 30, w, h + 270, mouseX, mouseY)) {
                int height = 0;
                for (Options options : optionsList) {
                    height += options.getH();
                }
                scrollHelperOptions.setHeight(height);
                scrollHelperOptions.updateScroll();

                height = 0;
                for (Options options : optionsList) {
                    float position = height;
                    position += scrollHelperOptions.getCalculatedScroll() + 10;
                    options.setY((int) position);
                    height += options.getH();
                }
            }
        } else if (selected == 2) {
        	GLHelper.startScissor(x, y + 60, w, h + 240);
        	
        	GLHelper.endScissor();
        }

        /*
        Draws the sidebar with the mods and settings tab
         */

        animateSideBar.update();

        
        Helper2D.drawRoundedRectangle(x - 50, y, 40, h + 300, 2, Style.getColor(50).getRGB(), roundedCorners ? 0 : -1);
        
        int value = 0;
        if (selected == 0) {
        	value = (40 - animateSideBar.getValueI());
        }
        if (selected == 1) {
        	value = (animateSideBar.getValueI());
        }
        if (selected == 2) {
        	value = (40 + animateSideBar.getValueI());
        }

        Helper2D.drawRoundedRectangle(x - 50, y + value, 40, 40, 2, Style.getColor(50).getRGB(), roundedCorners ? 0 : -1);


        int index = 0;
        for (String button : sideButtons) {
           Shindo.getInstance().getFontHelper().size15.drawString(button, x - 30 - Shindo.getInstance().getFontHelper().size15.getStringWidth(button) / 2f, y + 30 + index * 40, color);
           Helper2D.drawPicture(x - 40, y + 5 + index * 40, 20, 20, color, "icon/button/sidebar/" + button.toLowerCase() + ".png");
           

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
            for (String button : sideButtons) {
                if (MathHelper.withinBox(x - 50, y + index * 40, 40, 39, mouseX, mouseY)) {
                    if (selected != index) {
                    	
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
                if (MathHelper.withinBox(x + offset + 5, y + h + 5, length + 25, 20, mouseX, mouseY)) {
                    selectedType = type;
                    scrollHelperMods.setScrollStep(0);
                    initButtons();
                }
                offset += length + 30;
            }
            
            if (MathHelper.withinBox(x + w - 25, y + 5, 20, 20, mouseX, mouseY)) {
                mc.displayGuiScreen((GuiScreen)null);
            }


        }

        if (selected == 0) {
            for (Button button : buttonList) {
                button.mouseClicked(mouseX, mouseY, mouseButton);
            }
        } else if (selected == 1) {
            for (Options option : optionsList) {
                option.mouseClicked(mouseX, mouseY, mouseButton);
            }
        } else if (selected == 2) {
        	
        }
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
        if (selected == 0) {
            for (Button button : buttonList) {
                button.mouseReleased(mouseX, mouseY, state);
            }
        } else if (selected == 1){
            for (Options option : optionsList) {
                option.mouseReleased(mouseX, mouseY, state);
            }
        } else if (selected == 2) {
        	
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
    
}
