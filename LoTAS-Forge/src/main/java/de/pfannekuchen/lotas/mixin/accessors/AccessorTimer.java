package de.pfannekuchen.lotas.mixin.accessors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.util.Timer;

@Mixin(Timer.class)
public interface AccessorTimer {

	//#if MC>=11200
	@Accessor("tickLength")
	//#else
//$$ 	@Accessor("ticksPerSecond")
	//#endif
	public void tickLength(float timer);
	
}
