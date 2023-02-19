package com.minecrafttas.lotas.mixin.accessors;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.server.level.ColumnPos;
import net.minecraft.world.level.PortalForcer;

@Mixin(PortalForcer.class)
public interface AccessorPortalForcer {

	@Accessor("cachedPortals")
	public Map<?, ?> cachedPortals();
	
}
