package com.minecrafttas.lotas.mixin.accessors;

import java.util.Set;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.DistanceManager;
import net.minecraft.server.level.Ticket;

@Mixin(DistanceManager.class)
public interface AccessorDistanceManager {

	@Accessor("tickets")
	public Long2ObjectOpenHashMap<ObjectSortedSet<Ticket<?>>> tickets();

	@Accessor("chunksToUpdateFutures")
	public Set<ChunkHolder> chunksToUpdateFutures();

	@Accessor("ticketsToRelease")
	public LongSet ticketsToRelease();


}
