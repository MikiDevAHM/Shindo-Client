package me.miki.shindo.util;

import java.util.ArrayList;
import java.util.List;

import me.miki.shindo.event.EventTarget;
import me.miki.shindo.event.impl.MouseEvent;
import net.minecraft.client.Minecraft;

public class CpsHelper {

    private final List<Long> leftClicks = new ArrayList<>();
    private final List<Long> rightClicks = new ArrayList<>();

    @EventTarget
    public void onClick(MouseEvent event) {
        if (Minecraft.getMinecraft().currentScreen != null) return; // don't register cps in GUIs
        long time = System.currentTimeMillis();

        if (!event.buttonstate) return;

        if (event.button == 0) leftClicks.add(time);
        else if (event.button == 1) rightClicks.add(time);

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
