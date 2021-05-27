package de.pfannekuchen.lotas.mixin.accessors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.render.RenderTickCounter;

@Mixin(RenderTickCounter.class)
public interface AccessorTimer {
	@Accessor("tickTime")
	public void setTickTime(float f);
}
