/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package me.miki.shindo.features.mods.impl;

import me.miki.shindo.features.mods.Mod;
import me.miki.shindo.features.mods.Type;

public class NoHurtCamMod extends Mod {

    public NoHurtCamMod() {
        super(
                "NoHurtCam",
                "Removes the camera shake effect when you take damage.",
                Type.Visual
        );
    }
}
