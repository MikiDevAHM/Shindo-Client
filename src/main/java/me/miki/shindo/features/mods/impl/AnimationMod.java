/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package me.miki.shindo.features.mods.impl;

import me.miki.shindo.Shindo;
import me.miki.shindo.events.EventTarget;
import me.miki.shindo.events.impl.EventTick;
import me.miki.shindo.features.mods.Mod;
import me.miki.shindo.features.mods.Type;
import me.miki.shindo.features.settings.Setting;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.MovingObjectPosition;


public class AnimationMod extends Mod {

    public AnimationMod() {
        super(
                "Animation",
                "1.7 Animations in 1.8.",
                Type.Visual
        );

        Shindo.getInstance().getSettingManager().addSetting(new Setting("Block Animation", this, true));
        Shindo.getInstance().getSettingManager().addSetting(new Setting("Pushing Animation", this, true));
        Shindo.getInstance().getSettingManager().addSetting(new Setting("Pushing Particles", this, true));
        Shindo.getInstance().getSettingManager().addSetting(new Setting("Sneak Animation", this, true));
        Shindo.getInstance().getSettingManager().addSetting(new Setting("Health Animation", this, true));
        Shindo.getInstance().getSettingManager().addSetting(new Setting("Armor Damage Animation", this, true));
        Shindo.getInstance().getSettingManager().addSetting(new Setting("Item Switch Animation", this, true));
        Shindo.getInstance().getSettingManager().addSetting(new Setting("Fishing Rod Animation", this, true));
    }
}
