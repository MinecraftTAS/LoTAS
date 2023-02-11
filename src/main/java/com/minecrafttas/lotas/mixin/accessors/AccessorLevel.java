package com.minecrafttas.lotas.mixin.accessors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.world.level.Level;

@Mixin(Level.class)
public interface AccessorLevel {

	@Accessor("chunkSource") @Mutable
	public void chunkSource(ChunkSource newChunkCache);
	
}
