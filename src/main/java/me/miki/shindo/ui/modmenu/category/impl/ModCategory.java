package me.miki.shindo.ui.modmenu.category.impl;

import me.miki.shindo.Shindo;
import me.miki.shindo.features.mods.Mod;
import me.miki.shindo.features.mods.Type;
import me.miki.shindo.helpers.MathHelper;
import me.miki.shindo.helpers.hud.ScrollHelper;
import me.miki.shindo.helpers.render.GLHelper;
import me.miki.shindo.helpers.render.Helper2D;
import me.miki.shindo.ui.Style;
import me.miki.shindo.ui.modmenu.category.Category;
import me.miki.shindo.ui.modmenu.impl.Panel;
import me.miki.shindo.ui.modmenu.impl.sidebar.TextBox;
import me.miki.shindo.ui.modmenu.impl.sidebar.mods.Button;

import java.util.ArrayList;

public class ModCategory extends Category {

    private final ArrayList<Button> buttonList = new ArrayList<>();
    private final ScrollHelper scrollHelperMods = new ScrollHelper(0, 270, 35, 300);

    private final TextBox textBox = new TextBox("Search", 0, 0, 80, 20);
    private Type selectedType = Type.All;

    public ModCategory(Panel panel) {
        super(panel);
        setName("Mods");
        setScrollHelper(scrollHelperMods);
        setValue(0);
    }

    @Override
    public void initGui() {
        buttonList.clear();
        scrollHelperMods.setScrollStep(0);
        int addButtonX = 70;
        int addButtonY = 0;
        for (Mod mod : Shindo.getInstance().getModManager().getMods()) {
            if (selectedType.equals(mod.getType()) || selectedType.equals(Type.All) && mod.getType() != Type.Hidden) {
                if (textBox.getText().isEmpty() || mod.getName().toLowerCase().contains(textBox.getText().toLowerCase())) {
                    Button button = new Button(mod, getPanel(), addButtonX, addButtonY);
                    buttonList.add(button);
                    addButtonY += button.getH() + 3;
                }
            }
        }
    }

    @Override
    public void initCategory() {
        buttonList.clear();
        scrollHelperMods.setScrollStep(0);
        int addButtonX = 70;
        int addButtonY = 0;
        for (Mod mod : Shindo.getInstance().getModManager().getMods()) {
            if (selectedType.equals(mod.getType()) || selectedType.equals(Type.All) && mod.getType() != Type.Hidden) {
                if (textBox.getText().isEmpty() || mod.getName().toLowerCase().contains(textBox.getText().toLowerCase())) {
                    Button button = new Button(mod, getPanel(), addButtonX, addButtonY);
                    buttonList.add(button);
                    addButtonY += button.getH() + 3;
                }
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY) {

        boolean roundedCorners = Shindo.getInstance().getOptionManager().getOptionByName("Rounded Corners").isCheckToggled();
        int color = Shindo.getInstance().getOptionManager().getOptionByName("Color").getColor().getRGB();

        int offset = 0;
        for (Type type : Type.values()) {
            if(!type.equals(Type.Hidden)) {
                String text = type.name();
                int length = Shindo.getInstance().getFontHelper().size20.getStringWidth(text);
                Helper2D.drawRoundedRectangle(
                        getPanel().getX() + offset + 5 + 75,
                        getPanel().getY() + getPanel().getH() + 5,
                        length + 25,
                        20, 2,
                        Style.getColorTheme(selectedType.equals(type) ? 5 : 4).getRGB(),
                        roundedCorners ? 0 : -1
                );
                Helper2D.drawPicture(getPanel().getX() + offset + 8 + 75, getPanel().getY() + getPanel().getH() + 8, 15, 15, -1, "icon/" + type.getIcon());
                Shindo.getInstance().getFontHelper().size20.drawString(text, getPanel().getX() + offset + 26 + 75, getPanel().getY() + getPanel().getH() + 11, -1);
                offset += length + 30;
            }
        }

        textBox.renderTextBox(getPanel().getX() + getPanel().getW() - textBox.getW() - 5, getPanel().getY() + getPanel().getH() + 5, mouseX, mouseY);

        GLHelper.startScissor(getPanel().getX(), getPanel().getY()+ 60, getPanel().getW(), getPanel().getH() + 240);
        for (Button button : buttonList) {
            button.renderButton(mouseX, mouseY);
        }
        GLHelper.endScissor();

        if (MathHelper.withinBox(getPanel().getX(), getPanel().getY() + 30, getPanel().getW(), getPanel().getH() + 270, mouseX, mouseY)) {
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
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {

        int offset = 0;
        for (Type type : Type.values()) {
            if (!type.equals(Type.Hidden)) {
                String text = type.name();
                int length = Shindo.getInstance().getFontHelper().size20.getStringWidth(text);
                if (MathHelper.withinBox(getPanel().getX() + offset + 5 + 75, getPanel().getY() + getPanel().getH() + 5, length + 25, 20, mouseX, mouseY)) {
                    selectedType = type;
                    scrollHelperMods.setScrollStep(0);
                    initGui();
                }
                offset += length + 30;
            }
        }

        for (Button button : buttonList) {
            button.mouseClicked(mouseX, mouseY, mouseButton);
        }

    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        for (Button button : buttonList) {
            button.mouseReleased(mouseX, mouseY, state);
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {

        if(getPanel().isAnyButtonOpen()) {
            for (Button button : buttonList) {
                button.keyTyped(typedChar, keyCode);
            }
        } else {
            textBox.keyTyped(typedChar, keyCode);
            initGui();
        }
    }


}
