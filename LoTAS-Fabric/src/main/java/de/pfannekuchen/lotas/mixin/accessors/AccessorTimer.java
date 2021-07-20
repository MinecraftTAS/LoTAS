package de.pfannekuchen.lotas.mixin.accessors;

import net.minecraft.client.Timer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Timer.class)
public interface AccessorTimer {
	@Accessor("msPerTick")
	public void setTickTime(float f);
}
