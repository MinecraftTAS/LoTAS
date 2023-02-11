package com.minecrafttas.lotas.mixin.accessors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
import net.minecraft.world.level.chunk.storage.RegionFile;
import net.minecraft.world.level.chunk.storage.RegionFileStorage;

@Mixin(RegionFileStorage.class)
public interface AccessorRegionFileStorage {

	@Accessor("regionCache")
	public Long2ObjectLinkedOpenHashMap<RegionFile> regionCache();
	
}
