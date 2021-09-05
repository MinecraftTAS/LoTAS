package de.pfannekuchen.lotas.mixin.accessors;

import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ServerPlayer.class)
public interface AccessorServerPlayerEntity {

	@Accessor("spawnInvulnerableTime")
	int getSpawnInvulnerableTime();

	@Accessor("spawnInvulnerableTime")
	void setSpawnInvulnerableTime(int spawnInvulnerableTime);

}