package com.minecrafttas.lotas.mixin.client.accessors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.Minecraft;
import net.minecraft.client.Timer;

/**
 * This mixin accessor makes the timer available.
 * @author Pancake
 */
@Mixin(Minecraft.class)
public interface AccessorMinecraft {

	/**
	 * This Accessor opens the private field containing the timer
	 * @return timer
	 */
	@Accessor("timer")
	public Timer timer();
	
	
}
