package com.minecrafttas.lotas.mixin.accessors;

import java.util.Map;
import java.util.UUID;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.players.PlayerList;
import net.minecraft.stats.ServerStatsCounter;

@Mixin(PlayerList.class)
public interface AccessorPlayerList {

	@Accessor("stats")
    public Map<UUID, ServerStatsCounter> stats();
	
	@Accessor("advancements")
    public Map<UUID, PlayerAdvancements> advancements();
	
}
