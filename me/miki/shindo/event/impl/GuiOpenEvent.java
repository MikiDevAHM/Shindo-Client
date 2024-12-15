package me.miki.shindo.event.impl;

import me.miki.shindo.event.Event;
import net.minecraft.client.gui.GuiScreen;

public class GuiOpenEvent extends Event
{
    public GuiScreen gui;
    public GuiOpenEvent(GuiScreen gui)
    {
        this.gui = gui;
    }
}
