package me.miki.shindo.event.impl.render;

import me.miki.shindo.event.Event;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.Entity;

public class EntityViewRenderEvent  extends Event {
	
	public final EntityRenderer renderer;
    public final Entity entity;
    public final Block block;
    public final double renderPartialTicks;

    public EntityViewRenderEvent(EntityRenderer renderer, Entity entity, Block block, double renderPartialTicks)
    {
        this.renderer = renderer;
        this.entity = entity;
        this.block = block;
        this.renderPartialTicks = renderPartialTicks;
    }
    
    public static class CameraSetup extends EntityViewRenderEvent
    {
        public float yaw;
        public float pitch;
        public float roll;

        public CameraSetup(EntityRenderer renderer, Entity entity, Block block, double renderPartialTicks, float yaw, float pitch, float roll)
        {
            super(renderer, entity, block, renderPartialTicks);
            this.yaw = yaw;
            this.pitch = pitch;
            this.roll = roll;
        }
    }
	
}
