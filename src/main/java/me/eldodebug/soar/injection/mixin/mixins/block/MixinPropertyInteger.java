package me.eldodebug.soar.injection.mixin.mixins.block;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import me.eldodebug.soar.injection.interfaces.ICachedHashcode;
import net.minecraft.block.properties.PropertyInteger;

@Mixin(PropertyInteger.class)
public class MixinPropertyInteger {

    @Overwrite
    public int hashCode() {
        return ((ICachedHashcode)((Object)this)).getCachedHashcode();
    }
}