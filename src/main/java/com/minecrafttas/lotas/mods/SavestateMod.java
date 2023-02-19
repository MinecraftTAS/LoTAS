/**
 * Here is the logic of the savestate mod..
 *
 * The class 'state' represents a savestate. It contains some data such as name, timestamp and more.
 * There is a list of states, which is being synchronized between the client and the server whenever a state action occurs (save load delete)
 *
 * in onClientsidePayload the client reacts to the server, once it sends a packet. The packet contains all states of a server.
 *
 * The client can request and action in requestState().
 */
package com.minecrafttas.lotas.mods;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

import com.minecrafttas.lotas.LoTAS;
import com.minecrafttas.lotas.mods.savestatemod.StateData;
import com.minecrafttas.lotas.mods.savestatemod.StateData.State;
import com.minecrafttas.lotas.system.ModSystem.Mod;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundChangeDifficultyPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerAbilitiesPacket;
import net.minecraft.network.protocol.game.ClientboundRespawnPacket;
import net.minecraft.network.protocol.game.ClientboundSetExperiencePacket;
import net.minecraft.network.protocol.game.ClientboundSetHealthPacket;
import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.DerivedServerLevel;
import net.minecraft.server.level.DistanceManager;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.stats.ServerStatsCounter;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.chunk.storage.RegionFile;
import net.minecraft.world.level.storage.DerivedLevelData;
import net.minecraft.world.level.storage.LevelData;
import net.minecraft.world.level.storage.LevelStorageSource;

/**
 * Main savestate mod
 * @author Pancake
 */
public class SavestateMod extends Mod {

	public static SavestateMod instance;
	
	/**
	 * Initializes the savestate mod
	 * @param id
	 */
	public SavestateMod() {
		super(new ResourceLocation("lotas", "savestatemod"));
		instance = this;
	}

	// Server-side Todo list
	private String doSavestate = null;
	private int doLoadstate = -1;
	private int doDeletestate = -1;

	private StateData data = new StateData();
	
	/**
	 * Client-Side only state request. Sends a packet to the server contains a save or load int and an index to load
	 * @param state state 0 is save, state 1 is load, state 2 is delete
	 * @param index Index of Load/Deletestate
	 * @param name Name of the Savestate
	 */
	@Environment(EnvType.CLIENT)
	public void requestState(int state, int index, String name) {
		FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
		buf.writeInt(state);
		if (state == 0)
			buf.writeUtf(name);
		else
			buf.writeInt(index);
		this.sendPacketToServer(buf);
	}
	
	/**
	 * Triggers a Load/delete/savestate when a client packet is incoming and then resends the packet to all clients.
	 * @param buf Packet Data
	 */
	@Override
	protected void onServerPayload(FriendlyByteBuf buf) {
		// The Server has to be in tickrate zero AFTER THE ACTION for this to work
		TickAdvance.instance.lock = true;
		TickAdvance.instance.updateTickadvanceStatus(false);
		switch (buf.readInt()) {
			case 0:
				this.doSavestate = buf.readUtf(Short.MAX_VALUE);
				break;
			case 1:
				this.doLoadstate = buf.readInt();
				break;
			case 2:
				this.doDeletestate = buf.readInt();
				break;
		}
	}

	/**
	 * Savestates/loadstates/deletestates after the tick
	 */
	@Override
	protected void onServerTick() {
		
		// Savestate
		if (this.doSavestate != null) {
			this.savestate();
			this.doSavestate = null;
			TickAdvance.instance.lock = false;
		}
		// Loadstate
		if (this.doLoadstate != -1) {
			this.loadstate(this.doLoadstate);
			this.doLoadstate = -1;
			TickAdvance.instance.lock = false;
		}
		// Deletestate
		if (this.doDeletestate != -1) {
			this.deletestate(this.doDeletestate);
			this.doDeletestate = -1;
			TickAdvance.instance.lock = false;
		}
		
	}

	/**
	 * Sends all states to the client
	 */
	public void sendStates() {
		byte[] serialized = this.data.serializeData();
		this.mcserver.getPlayerList().getPlayers().forEach(player -> {
			FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
			buf.writeByteArray(serialized);
			this.sendPacketToClient(player, buf);
		});
	}

	/**
	 * Saves a new state of the world
	 */
	private void savestate() {
		// Enable tickrate zero
		TickAdvance.instance.updateTickadvanceStatus(true);
		
		// Save world
		this.mcserver.getPlayerList().saveAll();
		this.mcserver.saveAllChunks(false, true, false);
		
		try {
			// Load data
			this.data.loadData();
			
			// Make state
			int latestStateIndex = this.data.getStateCount() - 1;
			int index = latestStateIndex == -1 ? 0 : this.data.getState(latestStateIndex).getIndex() + 1;
			File stateDir = new File(data.getWorldSavestateDir(), index + "");
			FileUtils.copyDirectory(this.data.getWorldDir(), stateDir);
			this.data.addState(new State(this.doSavestate, Instant.now().getEpochSecond(), index));
			
			// Save data and send to client
			this.data.saveData();
			this.sendStates();
		} catch (IOException e) {
			e.printStackTrace(); // TODO: proper error
		}
		
		// Disable tickrate zero FIXME
		TickAdvance.instance.updateTickadvanceStatus(false);
	}

	/**
	 * Loads a state of the world
	 * @param i Index to load
	 */
	private void loadstate(int i) {
		if (!this.data.isValid(i)) {
			LoTAS.LOGGER.warn("Trying to load a nonexistant state: " + i);
			return;
		}
		
		// Enable tickrate zero
		TickAdvance.instance.updateTickadvanceStatus(true);
		
		// serverside: 
		// fetch and remove entities from ServerLevel (globalEntities, entitiesById, toAddAfterTick)
		// remove entities from ChunkAccess/LevelChunk
		// remove entities from ChunkMap
		
		for (ServerLevel level : this.mcserver.getAllLevels()) {
			level.toAddAfterTick.clear(); // entities in this queue will be added next tick
			level.globalEntities.clear(); // global entities are entities such as lighting bolts, they are ticked but not registered the the chunk map or the chunk access
		
			for (Entity entity : new ArrayList<>(level.entitiesById.values())) {
				if (entity != null)
					level.despawn(entity);
			}
		}
		
		// serverside:
		// remove chunk tickets from distance manager (tickets, chunksToUpdateFutures, ticketsToRelease)
		// unload chunks (updatingChunkMap, visibleChunkMap, pendingUnloads)
		// clear chunk cache
		// unlock files
		
		for (ServerLevel level : this.mcserver.getAllLevels()) {
			try {
				ServerChunkCache chunkCache = level.getChunkSource();

				// remove tickets
				DistanceManager distanceManager = chunkCache.distanceManager;
				distanceManager.tickets.clear();
				distanceManager.chunksToUpdateFutures.clear();
				distanceManager.ticketsToRelease.clear();

				// unload chunks
				ChunkMap map = chunkCache.chunkMap;
				map.pendingUnloads.clear();
				map.updatingChunkMap.clear();
				map.visibleChunkMap.clear();
				map.entitiesInLevel.clear();
				map.processUnloads(() -> true);
				
				// unload portals
				level.getPortalForcer().cachedPortals.clear();
				
				// clear cache
				chunkCache.clearCache();

				// unlock files
				for (RegionFile file : map.poiManager.regionCache.values())
					file.close();
				map.poiManager.regionCache.clear();
				
				for (RegionFile file : map.regionCache.values())
					file.close();
				map.regionCache.clear();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
		// serverside:
		// load state

		File worldDir = null;
		try {
			// Load data
			this.data.loadData();
			
			worldDir = this.data.getWorldDir();

			// Save session.lock
			Path sessionLockFile = new File(worldDir, "session.lock").toPath();
			byte[] sessionLock = Files.readAllBytes(sessionLockFile);
			
			// Delete world
			FileUtils.deleteDirectory(worldDir);
			
			// Copy state
			File worldSavestateDir = new File(this.data.getWorldSavestateDir(), this.data.getState(i).getIndex() + "");
			FileUtils.copyDirectory(worldSavestateDir, this.data.getWorldDir());
			
			// Load session.lock
			Files.write(sessionLockFile, sessionLock, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
			
		} catch (IOException e) {
			e.printStackTrace(); // FIXME proper error
		}
		
		LevelData data = LevelStorageSource.getLevelData(new File(worldDir, "level.dat"), this.mcserver.getFixerUpper());
		for (ServerLevel level : this.mcserver.getAllLevels())
			if (level instanceof DerivedServerLevel)
				level.levelData = new DerivedLevelData(data);
			else
				level.levelData = data;
		
		for (ServerPlayer player : new ArrayList<>(this.mcserver.getPlayerList().getPlayers())) {
			this.mcserver.getPlayerList().load(player);
			ServerLevel newLevel = this.mcserver.getLevel(player.dimension);
			
	        // Pre update Player
	        LevelData levelData = player.level.getLevelData();
	        player.connection.send(new ClientboundRespawnPacket(player.dimension, levelData.getGeneratorType(), player.gameMode.getGameModeForPlayer()));
	        player.connection.send(new ClientboundChangeDifficultyPacket(levelData.getDifficulty(), levelData.isDifficultyLocked()));
	        player.server.getPlayerList().sendPlayerPermissionLevel(player);
	        
	        // Update level
	        player.moveTo(player.x, player.y, player.z, player.yRot, player.xRot);
	        player.setLevel(newLevel);
	        newLevel.addDuringPortalTeleport(player);
	        
	        
	        // Update player
	        player.connection.teleport(player.x, player.y, player.z, player.yRot, player.xRot);
	        player.gameMode.setLevel(newLevel);
	        player.connection.send(new ClientboundPlayerAbilitiesPacket(player.abilities));
	        player.server.getPlayerList().sendLevelInfo(player, newLevel);
	        player.server.getPlayerList().sendAllPlayerInfo(player);
	        player.connection.send(new ClientboundSetHealthPacket(player.getHealth(), player.getFoodData().getFoodLevel(), player.getFoodData().getSaturationLevel()));
	        player.connection.send(new ClientboundSetExperiencePacket(player.experienceProgress, player.totalExperience, player.experienceLevel));
	        for (MobEffectInstance mobEffectInstance : player.getActiveEffects())
	        	player.connection.send(new ClientboundUpdateMobEffectPacket(player.getId(), mobEffectInstance));
		}
		
		PlayerList playerList = this.mcserver.getPlayerList();
		for (ServerPlayer player : playerList.getPlayers()) {
			PlayerAdvancements adv = player.getAdvancements();
            adv.reload();
            adv.flushDirty(player);
            
            playerList.stats.remove(player.getUUID());
            ServerStatsCounter stats = playerList.getPlayerStats(player);
            player.stats = stats;
            stats.sendStats(player);
		}
		
		// Disable tickrate zero FIXME
		TickAdvance.instance.updateTickadvanceStatus(false);
	}
	
	/**
	 * Deletes a state of the world
	 * @param i Index to delete
	 */
	private void deletestate(int i) {
		if (!this.data.isValid(i)) {
			LoTAS.LOGGER.warn("Trying to delete a nonexistant state: " + i);
			return;
		}
		
		// Enable tickrate zero
		TickAdvance.instance.updateTickadvanceStatus(true);

		try {
			// Load data
			this.data.loadData();
			
			// Delete State
			FileUtils.deleteDirectory(new File(this.data.getWorldSavestateDir(), this.data.getState(i).getIndex() + ""));
			this.data.removeState(i);

			// Save data and send to client
			this.data.saveData();
			this.sendStates();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Disable tickrate zero FIXME
		TickAdvance.instance.updateTickadvanceStatus(false);
	}
	
	
	/**
	 * Updates the state list on incoming packet
	 * @param buf Packet Data
	 */
	@Override
	@Environment(EnvType.CLIENT)
	protected void onClientsidePayload(FriendlyByteBuf buf) {
		this.data.deserializeData(buf.readByteArray());
	}
	
	/**
	 * Updates client data on connect
	 */
	public void onConnect(ServerPlayer c) {
		try {
			this.data.loadData();
			this.sendStates();
		} catch (IOException e) {
			e.printStackTrace(); // TODO: proper error
		}
	}
	
	/**
	 * Load save data on initialize
	 */
	@Override
	protected void onServerLoad() {
		this.data.onServerInitialize(this.mcserver);
	}
}
