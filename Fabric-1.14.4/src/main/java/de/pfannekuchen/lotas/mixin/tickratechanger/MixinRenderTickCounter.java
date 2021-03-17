package de.pfannekuchen.lotas.mixin.tickratechanger;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import de.pfannekuchen.lotas.duck.TickDuck;
import net.minecraft.client.render.RenderTickCounter;

@Mixin(RenderTickCounter.class)
public class MixinRenderTickCounter implements TickDuck{
	@Shadow
	private float tickTime;
	
	@Override
	public float getTickTime() {
		return this.tickTime;
	}

	@Override
	public void setTickTime(float ticksPerSecond) {
		tickTime=ticksPerSecond;
	}

}
