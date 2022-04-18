package de.pfannkuchen.lotas.worldhacking;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;

public class WorldHacks {
	
	public static void unloadPlayers(ServerLevel world) {
		MinecraftServer server=Minecraft.getInstance().getSingleplayerServer();
		server.getPlayerList().getPlayers().forEach(player -> {
			world.getChunkSource().removeEntity(player);
		});
	}
	
	public static void unloadWorld() {
		for (ServerLevel world : Minecraft.getInstance().getSingleplayerServer().getAllLevels()) {
			ServerChunkCache source=world.getChunkSource();
			source.chunkMap.flushWorker();
		}
	}
	
	public static void loadWorld() {
		MinecraftServer mcserver = Minecraft.getInstance().getSingleplayerServer();
//		ChunkProgressListener chunkProgressListener = ((AccessorMinecraftServer)mcserver).getFactory().create(11);
//		((AccessorMinecraftServer)mcserver).invokeCreateLevels(chunkProgressListener);
//		
//		ServerLevel serverLevel = mcserver.overworld();
//        BlockPos blockPos = serverLevel.getSharedSpawnPos();
//        chunkProgressListener.updateSpawnPos(new ChunkPos(blockPos));
//        ServerChunkCache serverChunkCache = serverLevel.getChunkSource();
//        serverChunkCache.getLightEngine().setTaskPerBatch(500);
//        serverChunkCache.addRegionTicket(TicketType.START, new ChunkPos(blockPos), 11, Unit.INSTANCE);
//        
//		for (ServerLevel serverLevel2 :((AccessorMinecraftServer)mcserver).getLevels().values()) {
//            ForcedChunksSavedData forcedChunksSavedData = serverLevel2.getDataStorage().get(ForcedChunksSavedData::load, "chunks");
//            if (forcedChunksSavedData == null) continue;
//            LongIterator longIterator = forcedChunksSavedData.getChunks().iterator();
//            while (longIterator.hasNext()) {
//                long l = longIterator.nextLong();
//                ChunkPos chunkPos = new ChunkPos(l);
//                serverLevel2.getChunkSource().updateChunkForced(chunkPos, true);
//            }
//        }
//		
//		serverChunkCache.getLightEngine().setTaskPerBatch(5);
		Minecraft.getInstance().getSingleplayerServer().getPlayerList().getPlayers().forEach(player -> {
			MinecraftServer server =Minecraft.getInstance().getSingleplayerServer();
			ServerLevel serverLevel=server.overworld();
			for(int i = 0; i <= (int)player.position().x >> 4; i++) {
				for(int j = 0; j <= (int)player.position().y >> 4; j++) {
					serverLevel.getChunkSource().getChunkNow(i , j);
				}
			}
		});
	}
	
	public static void loadPlayer() {
		Minecraft.getInstance().getSingleplayerServer().getPlayerList().getPlayers().forEach(player -> {
//			((AccessorEntity)player).invokeUnsetRemoved();
			MinecraftServer server =Minecraft.getInstance().getSingleplayerServer();
			ServerLevel serverLevel=server.overworld();
			serverLevel.getChunkSource().addEntity(player);
//			player.connection.send(new CustomRespawnPacket(serverLevel.dimensionType(), serverLevel.dimension(), BiomeManager.obfuscateSeed(serverLevel.getSeed()), player.gameMode.getGameModeForPlayer(), player.gameMode.getPreviousGameModeForPlayer(), serverLevel.isDebug(), serverLevel.isFlat(), true, true));
//			player.changeDimension(serverLevel);
		});
	}

}