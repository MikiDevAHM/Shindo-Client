package me.miki.shindo.ui.comp;

import me.miki.shindo.Shindo;
import me.miki.shindo.helpers.MathHelper;
import me.miki.shindo.helpers.render.Helper2D;
import me.miki.shindo.ui.Style;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatAllowedCharacters;
import org.lwjgl.input.Keyboard;

import java.awt.*;

public class TextField {
    private int x, y, width, height;
    private String text = "", hint;
    private boolean focused;
    private int blink;
    private int cursorPosition;
    private boolean allSelected;

    /**
     * @param x      x position
     * @param y      y position
     * @param width  width of text field
     * @param height height of text field
     * @param hint   hint to display when the text is empty
     */
    public TextField(int x, int y, int width, int height, String hint) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.hint = hint;
    }

    /**
     * call this method to render the text field
     *
     * @param mx mouse x position
     * @param my mouse y position
     */
    public void render(int mx, int my) {

        boolean hover = MathHelper.withinBox(x, y, width, height, mx, my);
        Helper2D.drawRectangle(x, y, width, height, hover || focused ? 0xffffffff : 0xff969696);

        // Se seleção total, desenhe antes do texto
        if (allSelected) {
            Helper2D.drawRectangle(
                    x + 2,
                    y + 2,
                    Shindo.getInstance().getFontHelper().size20.getStringWidth(text) + 4,
                    10,
                    Style.getColorTheme(8).getRGB()
            );
        }

        boolean em = text.isEmpty();
        Shindo.getInstance().getFontHelper().size20.drawString(
                em && !focused ? hint : text,
                x + 2,
                y + 3,
                em ? Style.getColorTheme(5).getRGB() : Style.getColorTheme(4).getRGB()
        );

        // cursor
        if (isFocused()) {
            if (cursorPosition >= 0 && cursorPosition <= text.length()) {
                int cursorX = (int) (this.x + 3 + Shindo.getInstance().getFontHelper().size20.getStringWidth(
                        text.substring(0, cursorPosition)
                ));
                Helper2D.drawRectangle(cursorX, y + 2, 2, height - 4, Color.BLACK.getRGB());
            }
        }

    }

    /**
     * Called on tick
     */
    public void update() {
        blink++;
        if (blink >= 20) blink = -1;
    }

    /**
     * call this method when user clicked anywhere on the screen
     *
     * @param mx  mouse x position
     * @param my  mouse y position
     * @param mb mouse button
     */
    public void onClick(int mx, int my, int mb) {
        if (mb == 0) focused = isHovered(mx, my);
    }

    public void keyTyped(char typedChar, int keyCode) {
        if (focused) {
            switch (keyCode) {
                case Keyboard.KEY_ESCAPE:
                    focused = false;
                    break;
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
    }



    public void typeText(String currentText) {
        if (allSelected) {
            cursorPosition = 0;
            text = "";
            allSelected = false; // <- adicione isso aqui
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
            } else if (cursorPosition > 0) { // <- adicione essa verificação
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

    public static boolean isCtrlKeyDown() {
        return Minecraft.isRunningOnMac ? Keyboard.isKeyDown(219) || Keyboard.isKeyDown(220) : Keyboard.isKeyDown(29) || Keyboard.isKeyDown(157);
    }

    public boolean isHovered(int mouseX, int mouseY) {
        return MathHelper.withinBox(x, y, width, height, mouseX, mouseY);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isFocused() {
        return focused;
    }

    public void setFocused(boolean focused) {
        this.focused = focused;
    }
}
