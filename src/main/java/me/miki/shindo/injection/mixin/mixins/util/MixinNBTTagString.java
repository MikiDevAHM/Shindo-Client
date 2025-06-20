package me.miki.shindo.injection.mixin.mixins.util;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.nbt.NBTTagString;

@Mixin(NBTTagString.class)
public class MixinNBTTagString {
	
    @Shadow 
    private String data;
    
    @Unique 
    private String dataCache;

    @Inject(method = "read", at = @At("HEAD"))
    private void emptyDataCache(CallbackInfo ci) {
        this.dataCache = null;
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public String toString() {
        if (this.dataCache == null) {
            this.dataCache = "\"" + this.data.replace("\"", "\\\"") + "\"";
        }

        return this.dataCache;
    }
}
