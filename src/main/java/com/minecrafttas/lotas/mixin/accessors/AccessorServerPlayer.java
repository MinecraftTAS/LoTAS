package com.minecrafttas.lotas.mixin.accessors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.ServerStatsCounter;

@Mixin(ServerPlayer.class)
public interface AccessorServerPlayer {

	@Accessor("stats") @Mutable
	public void stats(ServerStatsCounter stats);
	
}
