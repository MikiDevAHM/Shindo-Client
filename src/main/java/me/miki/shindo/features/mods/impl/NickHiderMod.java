/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package me.miki.shindo.features.mods.impl;


import me.miki.shindo.Shindo;
import me.miki.shindo.features.mods.Mod;
import me.miki.shindo.features.mods.Type;
import me.miki.shindo.features.settings.Setting;

public class NickHiderMod extends Mod {

    public NickHiderMod() {
        super(
                "NickHider",
                "Hides your nickname in game by replacing it.",
                Type.Visual
        );

        Shindo.getInstance().getSettingManager().addSetting(new Setting("Nickname", this, "Name", "You", 3));
    }

    public static String replaceNickname(String nick) {
        if (Shindo.getInstance() != null) {
            if (Shindo.getInstance().getModManager() != null) {
                if (Shindo.getInstance().getModManager().getMod("NickHider").isToggled()) {
                    return nick.replace(
                            mc.getSession().getUsername(),
                            Shindo.getInstance().getSettingManager().getSettingByModAndName("NickHider", "Nickname").getText()
                    );
                }
            }
        }
        return nick;
    }
}
