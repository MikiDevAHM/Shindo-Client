/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package me.miki.shindo.helpers;

import me.miki.shindo.events.EventTarget;
import me.miki.shindo.events.impl.MouseEvent;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.List;

public class CpsHelper {

    private final List<Long> leftClicks = new ArrayList<>();
    private final List<Long> rightClicks = new ArrayList<>();

    @EventTarget
    public void onClick(MouseEvent event) {
        if (Minecraft.getMinecraft().currentScreen != null) return; // don't register cps in GUIs
        long time = System.currentTimeMillis();


        if (event.getButton() == 0) leftClicks.add(time);
        else if (event.getButton() == 1) rightClicks.add(time);

        removeOldClicks(time);
    }

    public int getCPS(int mouseButton) {
        removeOldClicks(System.currentTimeMillis());
        return mouseButton == 0 ? leftClicks.size() : rightClicks.size();
    }

    public void removeOldClicks(long currentTime) {
        leftClicks.removeIf(e -> e + 1000 < currentTime);
        rightClicks.removeIf(e -> e + 1000 < currentTime);
    }
}
