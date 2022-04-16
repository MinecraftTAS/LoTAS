package de.pfannkuchen.lotas.worldhacking;

import java.io.IOException;

import de.pfannkuchen.lotas.mixin.accessors.AccessorEntity;
import de.pfannkuchen.lotas.mixin.accessors.AccessorMinecraftServer;
import de.pfannkuchen.lotas.mixin.accessors.AccessorServerLevel;
import it.unimi.dsi.fastutil.longs.LongIterator;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.TicketType;
import net.minecraft.server.level.progress.ChunkProgressListener;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ForcedChunksSavedData;
import net.minecraft.world.level.biome.BiomeManager;

public class WorldHacks {
	
	public static void unloadWorld() {
		for (ServerLevel world : Minecraft.getInstance().getSingleplayerServer().getAllLevels())
			try {
				world.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	public static void unloadPlayers(ServerLevel world) {
		MinecraftServer server=Minecraft.getInstance().getSingleplayerServer();
		server.getPlayerList().getPlayers().forEach(player -> {
			player.getLevel().removePlayerImmediately(player, RemovalReason.CHANGED_DIMENSION);
			((AccessorServerLevel)world).getChunkSource().removeEntity(player);
		});
	}
	
	public static void loadWorld() {
		MinecraftServer mcserver = Minecraft.getInstance().getSingleplayerServer();
		ChunkProgressListener chunkProgressListener = ((AccessorMinecraftServer)mcserver).getFactory().create(11);
		((AccessorMinecraftServer)mcserver).invokeCreateLevels(chunkProgressListener);
		
		ServerLevel serverLevel = mcserver.overworld();
        BlockPos blockPos = serverLevel.getSharedSpawnPos();
        chunkProgressListener.updateSpawnPos(new ChunkPos(blockPos));
        ServerChunkCache serverChunkCache = serverLevel.getChunkSource();
        serverChunkCache.getLightEngine().setTaskPerBatch(500);
        serverChunkCache.addRegionTicket(TicketType.START, new ChunkPos(blockPos), 11, Unit.INSTANCE);
        
		for (ServerLevel serverLevel2 :((AccessorMinecraftServer)mcserver).getLevels().values()) {
            ForcedChunksSavedData forcedChunksSavedData = serverLevel2.getDataStorage().get(ForcedChunksSavedData::load, "chunks");
            if (forcedChunksSavedData == null) continue;
            LongIterator longIterator = forcedChunksSavedData.getChunks().iterator();
            while (longIterator.hasNext()) {
                long l = longIterator.nextLong();
                ChunkPos chunkPos = new ChunkPos(l);
                serverLevel2.getChunkSource().updateChunkForced(chunkPos, true);
            }
        }
		
		serverChunkCache.getLightEngine().setTaskPerBatch(5);
	}

	public static void loadPlayer() {
		Minecraft.getInstance().getSingleplayerServer().getPlayerList().getPlayers().forEach(player -> {
			((AccessorEntity)player).invokeUnsetRemoved();
			MinecraftServer server =Minecraft.getInstance().getSingleplayerServer();
			ServerLevel serverLevel=server.overworld();
			((AccessorServerLevel)serverLevel).getChunkSource().addEntity(player);
			player.connection.send(new CustomRespawnPacket(serverLevel.dimensionType(), serverLevel.dimension(), BiomeManager.obfuscateSeed(serverLevel.getSeed()), player.gameMode.getGameModeForPlayer(), player.gameMode.getPreviousGameModeForPlayer(), serverLevel.isDebug(), serverLevel.isFlat(), true, true));
			player.changeDimension(serverLevel);
		});
	}

}