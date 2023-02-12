package com.minecrafttas.lotas.mixin.accessors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.server.level.DistanceManager;
import net.minecraft.server.level.ServerChunkCache;

@Mixin(ServerChunkCache.class)
public interface AccessorServerChunkCache {

	@Invoker("clearCache")
	public void runClearCache();
	
	@Accessor("distanceManager")
	public DistanceManager distanceManager();
	
	@Accessor("distanceManager") @Mutable
	public void distanceManager(DistanceManager distanceManager);
	
}
