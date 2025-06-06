package me.miki.shindo.injection.mixin.mixins.lwjgl;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "org.lwjgl.opengl.WindowsDisplay")
public abstract class MixinWindowsDisplay {

    @Shadow
    protected abstract void handleMouseButton(int button, int state, long millis);
    
    @Inject(method = "doHandleMessage", at = @At("HEAD"), cancellable = true, remap = false)
    private void doHandleMessage(long hwnd, int msg, long wParam, long lParam, long millis,
            CallbackInfoReturnable<Long> cir) {
        if (msg == 0x020B) {
            if ((wParam >> 16) == 1L) {
                handleMouseButton(3, 1, millis);
            } else {
                handleMouseButton(4, 1, millis);
            }
            cir.setReturnValue(1L);
        }
    }
}