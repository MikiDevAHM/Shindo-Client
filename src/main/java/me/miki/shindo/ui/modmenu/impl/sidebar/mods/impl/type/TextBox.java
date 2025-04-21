package me.miki.shindo.ui.modmenu.impl.sidebar.mods.impl.type;

import me.miki.shindo.Shindo;
import me.miki.shindo.features.settings.Setting;
import me.miki.shindo.helpers.MathHelper;
import me.miki.shindo.helpers.render.GLHelper;
import me.miki.shindo.helpers.render.Helper2D;
import me.miki.shindo.ui.Style;
import me.miki.shindo.ui.modmenu.impl.sidebar.mods.Button;
import me.miki.shindo.ui.modmenu.impl.sidebar.mods.impl.Settings;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatAllowedCharacters;
import org.lwjgl.input.Keyboard;

public class TextBox extends Settings {

    public TextBox(Setting setting, Button button, int y) {
        super(setting, button, y);
    }

    private boolean focused;
    private boolean allSelected;

    private int x, y, w, h;

    @Override
    public void renderSetting(int mouseX, int mouseY) {
        boolean roundedCorners = Shindo.getInstance().getOptionManager().getOptionByName("Rounded Corners").isCheckToggled();
        int color = Shindo.getInstance().getOptionManager().getOptionByName("Color").getColor().getRGB();

        x = button.getPanel().getX() + button.getPanel().getW() - 150 - 11;
        y = button.getPanel().getY() + button.getPanel().getH() + getY() + 2;
        w = 150;
        h = 20;

        Shindo.getInstance().getFontHelper().size30.drawString(
                setting.getName(),
                button.getPanel().getX() + 80,
                button.getPanel().getY() + button.getPanel().getH() + getY() + 6,
                color
        );

        int offset = getH() / 2 - 10;

        GLHelper.startScissor(x, y, w, h);
        Helper2D.drawRoundedRectangle(x, y, w, h, 2, Style.getColorTheme(isFocused() ? 5 : 3).getRGB(), roundedCorners ? 0 : -1);
        if (setting.getText().isEmpty()) {
            Shindo.getInstance().getFontHelper().size20.drawString(setting.getPlaceholderText(), x + offset + 5, y + offset + 4, 0x50ffffff);
        } else {

            Helper2D.drawRectangle(x + offset + 5 + Shindo.getInstance().getFontHelper().size20.getStringWidth(setting.getText().substring(0, setting.getCursorPosition())), y + offset + 3, 1, 10, Style.getColorTheme(10).getRGB());
            if (allSelected) {
                Helper2D.drawRectangle(x + offset + 2, y + offset + 1, Shindo.getInstance().getFontHelper().size20.getStringWidth(setting.getText()) + 4, 14, Style.getColorTheme(8).getRGB());
            }
            Shindo.getInstance().getFontHelper().size20.drawString(setting.getText(), x + offset + 5, y + offset + 4, -1);
        }
        GLHelper.endScissor();
    }

    public void keyTyped(char typedChar, int keyCode) {
        if (!isFocused())
            return;

        switch (keyCode) {
            case Keyboard.KEY_BACK:
                removeText();
                break;
            case Keyboard.KEY_RIGHT:
                moveCursor(1);
                break;
            case Keyboard.KEY_LEFT:
                moveCursor(-1);
                break;
            case Keyboard.KEY_A:
                if (isCtrlKeyDown()) {
                    allSelected = true;
                    setting.setCursorPosition(setting.getText().length());
                }

            default:
                if (ChatAllowedCharacters.isAllowedCharacter(typedChar)) {
                    typeText(Character.toString(typedChar));
                }
        }
    }

    public static boolean isCtrlKeyDown() {
        return Minecraft.isRunningOnMac ? Keyboard.isKeyDown(219) || Keyboard.isKeyDown(220) : Keyboard.isKeyDown(29) || Keyboard.isKeyDown(157);
    }

    public void typeText(String currentText) {
        if (allSelected) {
            setting.setCursorPosition(0);
            setting.setText("");
        }
        StringBuilder builder = new StringBuilder(setting.getText());
        builder.insert(setting.getCursorPosition(), currentText);
        setting.setText(builder.toString());
        moveCursor(1);
    }

    public void removeText() {
        if (!setting.getText().isEmpty()) {
            if (allSelected) {
                allSelected = false;
                setting.setCursorPosition(0);
                setting.setText("");
            } else {
                moveCursor(-1);
                StringBuilder builder = new StringBuilder(setting.getText());
                builder.deleteCharAt(setting.getCursorPosition());
                setting.setText(builder.toString());
            }
        }
    }

    public void moveCursor(int direction) {
        allSelected = false;
        if (direction < 0) {
            if (setting.getCursorPosition() > 0) {
                setting.setCursorPosition(setting.getCursorPosition() - 1);
            }
        } else if (direction > 0) {
            if (setting.getCursorPosition() < setting.getText().length()) {
                setting.setCursorPosition(setting.getCursorPosition() + 1);
            }
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if(isHovered(mouseX, mouseY)) {
            setFocused(true);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        if(!isHovered(mouseX, mouseY)) {
            setFocused(false);
        }
    }

    public boolean isHovered(int mouseX, int mouseY) {
        return MathHelper.withinBox(x, y, w, h, mouseX, mouseY);
    }

    public boolean isFocused() {
        return focused;
    }

    public void setFocused(boolean focused) {
        this.focused = focused;
    }

    public boolean isAllSelected() {
        return allSelected;
    }

    public void setAllSelected(boolean allSelected) {
        this.allSelected = allSelected;
    }
}
