package de.pfannkuchen.lotas.util;

import java.io.IOException;

import de.pfannkuchen.lotas.mixin.accessors.AccessorEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.game.ClientboundRespawnPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.level.biome.BiomeManager;

public class ChunkControl {
	
	public static void unloadWorld() {
		for (ServerLevel world : Minecraft.getInstance().getSingleplayerServer().getAllLevels())
			try {
				world.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	public static void unloadPlayer() {
		Minecraft.getInstance().getSingleplayerServer().getPlayerList().getPlayers().forEach(player -> {
			player.getLevel().removePlayerImmediately(player, RemovalReason.CHANGED_DIMENSION);
		});
	}

	public static void loadPlayer() {
		Minecraft.getInstance().getSingleplayerServer().getPlayerList().getPlayers().forEach(player -> {
			((AccessorEntity)player).invokeUnsetRemoved();
			ServerLevel serverLevel=player.getLevel();
			player.connection.send(new ClientboundRespawnPacket(serverLevel.dimensionType(), serverLevel.dimension(), BiomeManager.obfuscateSeed(serverLevel.getSeed()), player.gameMode.getGameModeForPlayer(), player.gameMode.getPreviousGameModeForPlayer(), serverLevel.isDebug(), serverLevel.isFlat(), true));
		});
	}
}
