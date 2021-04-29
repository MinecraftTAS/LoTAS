package de.pfannekuchen.lotas.mixin.accessors;

import org.spongepowered.asm.mixin.gen.Accessor;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderTickCounter;

@Mixin(MinecraftClient.class)
public interface MinecraftClientAccessor {

	@Accessor("renderTickCounter")
	public RenderTickCounter getRenderTickCounter();
	
}
