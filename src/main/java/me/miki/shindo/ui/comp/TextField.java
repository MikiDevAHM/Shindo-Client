package me.miki.shindo.ui.comp;

import me.miki.shindo.Shindo;
import me.miki.shindo.helpers.IOHelper;
import me.miki.shindo.helpers.MathHelper;
import me.miki.shindo.helpers.render.GLHelper;
import me.miki.shindo.helpers.render.Helper2D;
import me.miki.shindo.ui.Style;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatAllowedCharacters;
import org.lwjgl.input.Keyboard;

public class TextField {
    private final int x;
    private final int y;
    private final int width;
    private final int height;
    private String text = "";
    private final String hint;
    private boolean focused;
    private int blink;
    private int cursorPosition;
    private boolean allSelected;
    private boolean passwordMode = false;
    private char passwordChar = '*';
    private int backgroundColor = 0xffffffff;
    private int textOffsetX = 0;

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

    public static boolean isCtrlKeyDown() {
        return Minecraft.isRunningOnMac ? Keyboard.isKeyDown(219) || Keyboard.isKeyDown(220) : Keyboard.isKeyDown(29) || Keyboard.isKeyDown(157);
    }

    /**
     * call this method to render the text field
     *
     * @param mx mouse x position
     * @param my mouse y position
     */
    public void render(int mx, int my) {
        boolean hover = MathHelper.withinBox(x, y, width, height, mx, my);
        // Fundo com cor customizável
        Helper2D.drawRectangle(x, y, width, height, hover || focused ? backgroundColor : 0xff969696);

        // Ativa scissor para limitar a renderização do texto
        GLHelper.startScissor(x + 4, y + 2, width - 8, height - 4);

        // Texto visível
        boolean em = text.isEmpty();
        String displayText = em && !focused ? hint : (passwordMode ? repeat(passwordChar, text.length()) : text);

        // Ajuste automático da posição do texto se estiver maior que a caixa
        int textWidth = Shindo.getInstance().getFontHelper().size15.getStringWidth(displayText);
        if (textWidth > width - 8) {
            int cursorPixel = Shindo.getInstance().getFontHelper().size15.getStringWidth(passwordMode
                    ? repeat(passwordChar, cursorPosition)
                    : text.substring(0, cursorPosition));

            // Move o texto para manter o cursor visível dentro da caixa
            if (cursorPixel - textOffsetX > width - 12) {
                textOffsetX = cursorPixel - (width - 12);
            } else if (cursorPixel - textOffsetX < 0) {
                textOffsetX = cursorPixel;
            }
        } else {
            textOffsetX = 0;
        }

        // Seleção
        if (allSelected) {
            int selWidth = Shindo.getInstance().getFontHelper().size15.getStringWidth(displayText);
            Helper2D.drawRectangle(x + 4 - textOffsetX, y + 5, selWidth + 4, 10, Style.getColorTheme(8).getRGB());
        }

        // Desenhar texto
        Shindo.getInstance().getFontHelper().size15.drawString(
                displayText,
                x + 5 - textOffsetX,
                y + 5,
                em ? Style.getColorTheme(5).getRGB() : Style.getColorTheme(4).getRGB()
        );

        // Cursor
        if (isFocused()) {
            int cursorX = x + 3 - textOffsetX + Shindo.getInstance().getFontHelper().size15.getStringWidth(
                    passwordMode ? repeat(passwordChar, cursorPosition) : text.substring(0, cursorPosition)
            );
            Helper2D.drawRectangle(cursorX, y + 2, 2, height - 4, 0xff000000);
        }

        GLHelper.endScissor();
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
     * @param mx mouse x position
     * @param my mouse y position
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
                case Keyboard.KEY_V:
                    if (isCtrlKeyDown()) {
                        typeText(IOHelper.getStringFromClipboard());
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

    public void setBackgroundColor(int color) {
        this.backgroundColor = color;
    }

    public void setPasswordMode(boolean passwordMode) {
        this.passwordMode = passwordMode;
    }

    public void setPasswordChar(char passwordChar) {
        this.passwordChar = passwordChar;
    }

    private String repeat(char c, int count) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < count; i++) {
            builder.append(c);
        }
        return builder.toString();
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
