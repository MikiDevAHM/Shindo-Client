package me.miki.shindo.injection.mixin.mixins.item;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.item.ItemStack;

@Mixin(ItemStack.class)
public class MixinItemStack {

    private String cachedDisplayName;

    @Inject(method = "getDisplayName", at = @At("HEAD"), cancellable = true)
    private void returnCachedDisplayName(CallbackInfoReturnable<String> cir) {
        if (cachedDisplayName != null) {
            cir.setReturnValue(cachedDisplayName);
        }
    }

    @Inject(method = "getDisplayName", at = @At("RETURN"))
    private void cacheDisplayName(CallbackInfoReturnable<String> cir) {
        cachedDisplayName = cir.getReturnValue();
    }

    @Inject(method = "setStackDisplayName", at = @At("HEAD"))
    private void resetCachedDisplayName(String displayName, CallbackInfoReturnable<ItemStack> cir) {
        cachedDisplayName = null;
    }
}
