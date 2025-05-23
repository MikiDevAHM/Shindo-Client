package me.miki.shindo.ui.modmenu.impl.sidebar;

import me.miki.shindo.Shindo;
import me.miki.shindo.helpers.MathHelper;
import me.miki.shindo.helpers.render.GLHelper;
import me.miki.shindo.helpers.render.Helper2D;
import me.miki.shindo.ui.Style;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatAllowedCharacters;
import org.lwjgl.input.Keyboard;

public class TextBox {

    private String text;
    private String placeHolderText;
    private int x, y;
    private final int w;
    private final int h;
    private boolean focused;
    private int cursorPosition;
    private boolean allSelected;

    public TextBox(String placeHolderText, int x, int y, int w, int h) {
        this.text = "";
        this.placeHolderText = placeHolderText;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.focused = true;
    }

    public static boolean isCtrlKeyDown() {
        return Minecraft.isRunningOnMac ? Keyboard.isKeyDown(219) || Keyboard.isKeyDown(220) : Keyboard.isKeyDown(29) || Keyboard.isKeyDown(157);
    }

    public void renderTextBox(int x, int y, int mouseX, int mouseY) {
        this.x = x;
        this.y = y;

        int offset = h / 2 - 10;

        GLHelper.startScissor(x, y, w, h);
        Helper2D.drawRoundedRectangle(x, y, w, h, 2, Style.getColorTheme(5).getRGB(), 0);
        Helper2D.drawPicture(x + offset + 2, y + offset + 3, 15, 15, 0x50ffffff, "icon/search.png");
        if (text.isEmpty()) {
            Shindo.getInstance().getFontHelper().size20.drawString(placeHolderText, x + offset + 20, y + offset + 6, 0x50ffffff);
        } else {

            Helper2D.drawRectangle(x + offset + Shindo.getInstance().getFontHelper().size20.getStringWidth(text.substring(0, cursorPosition)) + 20, y + offset + 5, 1, 10, Style.getColorTheme(10).getRGB());
            if (allSelected) {
                Helper2D.drawRectangle(x + offset + 17, y + offset + 3, Shindo.getInstance().getFontHelper().size20.getStringWidth(text) + 4, 14, Style.getColorTheme(8).getRGB());
            }
            Shindo.getInstance().getFontHelper().size20.drawString(text, x + offset + 20, y + offset + 6, -1);
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
                    cursorPosition = text.length();
                }

            default:
                if (ChatAllowedCharacters.isAllowedCharacter(typedChar)) {
                    typeText(Character.toString(typedChar));
                }
        }
    }

    public void typeText(String currentText) {
        if (allSelected) {
            cursorPosition = 0;
            text = "";
        }
        StringBuilder builder = new StringBuilder(text);
        builder.insert(cursorPosition, currentText);
        text = builder.toString();
        moveCursor(1);
    }

    public void removeText() {
        if (!text.isEmpty()) {
            if (allSelected) {
                allSelected = false;
                cursorPosition = 0;
                text = "";
            } else {
                moveCursor(-1);
                StringBuilder builder = new StringBuilder(text);
                builder.deleteCharAt(cursorPosition);
                text = builder.toString();
            }
        }
    }

    public void moveCursor(int direction) {
        allSelected = false;
        if (direction < 0) {
            if (cursorPosition > 0) {
                cursorPosition--;
            }
        } else if (direction > 0) {
            if (cursorPosition < text.length()) {
                cursorPosition++;
            }
        }
    }

    public boolean isHovered(int mouseX, int mouseY) {
        return MathHelper.withinBox(x, y, w, h, mouseX, mouseY);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPlaceHolderText() {
        return placeHolderText;
    }

    public void setPlaceHolderText(String placeHolderText) {
        this.placeHolderText = placeHolderText;
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

    public int getH() {
        return h;
    }

    public boolean isFocused() {
        return focused;
    }

    public void setFocused(boolean focused) {
        this.focused = focused;
    }
}
