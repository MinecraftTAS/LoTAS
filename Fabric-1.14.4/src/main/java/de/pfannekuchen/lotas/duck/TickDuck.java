package de.pfannekuchen.lotas.duck;

import de.pfannekuchen.lotas.mixin.tickratechanger.InjectMinecraftClient;
import de.pfannekuchen.lotas.mixin.tickratechanger.MixinRenderTickCounter;

/**
 * Adds methods to RenderTickCounter and to access tickTime.
 * 
 * Used in {@link MixinRenderTickCounter} and {@link InjectMinecraftClient}
 * 
 * @author ScribbleLP
 * 
 */
public interface TickDuck {
	public float getTickTime();
	
	public void setTickTime(float ticksPerSecond);
}
