package me.miki.shindo.feature.mod.impl;

import me.miki.shindo.feature.mod.Mod;
import me.miki.shindo.feature.mod.Type;

public class NoHurtCamMod extends Mod {

    public NoHurtCamMod() {
        super(
                "NoHurtCam",
                "Removes the camera shake effect when you take damage.",
                Type.Tweaks
        );
    }
}
