package de.pfannkuchen.lotas.mixin.accessors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.world.entity.Entity;

@Mixin(Entity.class)
public interface AccessorEntity {
	@Invoker("unsetRemoved")
	public void invokeUnsetRemoved();
}
