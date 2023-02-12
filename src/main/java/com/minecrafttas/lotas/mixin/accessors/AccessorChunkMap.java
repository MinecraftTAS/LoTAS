package com.minecrafttas.lotas.mixin.accessors;

import java.util.function.BooleanSupplier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.progress.ChunkProgressListener;

@Mixin(ChunkMap.class)
public interface AccessorChunkMap {

	@Invoker("processUnloads")
	public void runProcessUnloads(BooleanSupplier booleanSupplier);
	
	@Accessor("updatingChunkMap")
    public Long2ObjectLinkedOpenHashMap<ChunkHolder> updatingChunkMap();
	
	@Accessor("visibleChunkMap")
	public Long2ObjectLinkedOpenHashMap<ChunkHolder> visibleChunkMap();
	
	@Accessor("pendingUnloads")
	public Long2ObjectLinkedOpenHashMap<ChunkHolder> pendingUnloads();
	
	@Accessor("progressListener")
	public ChunkProgressListener progressListener();

	@Accessor("toDrop")
	public LongSet toDrop();

	@Invoker("getUpdatingChunkIfPresent")
	public ChunkHolder runGetUpdatingChunkIfPresent(long l);

	@Invoker("updateChunkScheduling")
	public ChunkHolder runUpdateChunkScheduling(long l, int i, ChunkHolder chunkHolder, int j);
	
}
