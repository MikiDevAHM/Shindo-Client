/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package me.miki.shindo.ui.modmenu.impl.sidebar.chats;

import me.miki.shindo.events.EventManager;
import me.miki.shindo.features.chat.Chat;
import me.miki.shindo.ui.modmenu.impl.Panel;

public abstract class Chats {

    public Chat chat;
    public Panel panel;
    private int h, y;
    private boolean open;
    private int settingHeight;
    private boolean updated;

    public Chats(Chat chat, Panel panel, int y) {
        EventManager.register(this);
        this.chat = chat;
        this.panel = panel;
        this.open = false;
        this.h = 25;
        this.y = y;
    }

    public abstract void renderChats(int mouseX, int mouseY);

    public abstract void mouseClicked(int mouseX, int mouseY, int mouseButton);

    public abstract void mouseReleased(int mouseX, int mouseY, int state);

    public abstract void keyTyped(char typedChar, int keyCode);

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public Panel getPanel() {
        return panel;
    }

    public void setPanel(Panel panel) {
        this.panel = panel;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public boolean isUpdated() {
        return updated;
    }

    public void setUpdated(boolean updated) {
        this.updated = updated;
    }

    public int getOptionHeight() {
        return settingHeight;
    }

    public void setOptionHeight(int settingHeight) {
        this.settingHeight = settingHeight;
    }
}
