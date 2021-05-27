package de.pfannekuchen.lotas.mixin.accessors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderTickCounter;

@Mixin(MinecraftClient.class)
public interface AccessorMinecraftClient {
	@Accessor
	RenderTickCounter getRenderTickCounter();
}
