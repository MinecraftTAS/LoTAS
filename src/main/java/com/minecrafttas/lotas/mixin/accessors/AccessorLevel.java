package com.minecrafttas.lotas.mixin.accessors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkSource;
import net.minecraft.world.level.storage.LevelData;

@Mixin(Level.class)
public interface AccessorLevel {

	@Accessor("chunkSource") @Mutable
	public void chunkSource(ChunkSource newChunkCache);
	
	@Accessor("levelData") @Mutable
	public void levelData(LevelData newLevelData);
	
}
