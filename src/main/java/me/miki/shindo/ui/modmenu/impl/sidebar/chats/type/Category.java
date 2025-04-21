/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package me.miki.shindo.ui.modmenu.impl.sidebar.chats.type;

import me.miki.shindo.Shindo;
import me.miki.shindo.features.chat.Chat;
import me.miki.shindo.helpers.render.Helper2D;
import me.miki.shindo.ui.Style;
import me.miki.shindo.ui.modmenu.impl.Panel;
import me.miki.shindo.ui.modmenu.impl.sidebar.chats.Chats;

public class Category extends Chats {

    public Category(Chat chat, Panel panel, int y) {
        super(chat, panel, y);
    }

    @Override
    public void renderChats(int mouseX, int mouseY) {
        Shindo.getInstance().getFontHelper().size20.drawString(
                chat.getName(),
                panel.getX() + 80,
                panel.getY() + panel.getH() + getY() + 6,
                0x90ffffff
        );
        Helper2D.drawRectangle(panel.getX() + 80, panel.getY() + panel.getH() + getY() + 20, panel.getW() - 40 - 60, 1, Style.getColorTheme(6).getRGB());
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {

    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {

    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {

    }
}
