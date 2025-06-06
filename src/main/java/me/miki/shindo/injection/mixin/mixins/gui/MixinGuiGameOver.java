package me.miki.shindo.injection.mixin.mixins.gui;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.GuiGameOver;

@Mixin(GuiGameOver.class)
public class MixinGuiGameOver {

    @Shadow 
    private int enableButtonsTimer;

    @Inject(method = "initGui", at = @At("HEAD"))
    private void allowClickable(CallbackInfo ci) {
        this.enableButtonsTimer = 0;
    }
}
