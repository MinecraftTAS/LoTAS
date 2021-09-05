package de.pfannekuchen.lotas.mixin.accessors;

import net.minecraft.client.Minecraft;
import net.minecraft.client.Timer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Minecraft.class)
public interface AccessorMinecraftClient {
	@Accessor
	Timer getTimer();
}
