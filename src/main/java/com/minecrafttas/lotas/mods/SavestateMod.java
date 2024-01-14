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

import com.minecrafttas.lotas.mods.savestatemod.StateData;
import com.minecrafttas.lotas.mods.savestatemod.StateData.State;
import com.minecrafttas.lotas.system.ModSystem.Mod;
import com.mojang.serialization.Dynamic;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.Util;
import net.minecraft.commands.Commands;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.*;
import net.minecraft.resources.RegistryReadOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.ServerResources;
import net.minecraft.server.level.*;
import net.minecraft.server.packs.repository.*;
import net.minecraft.server.players.PlayerList;
import net.minecraft.stats.ServerStatsCounter;
import net.minecraft.util.DirectoryLock;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.level.DataPackConfig;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.chunk.storage.RegionFile;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.end.EndDragonFight;
import net.minecraft.world.level.lighting.BlockLightEngine;
import net.minecraft.world.level.lighting.SkyLightEngine;
import net.minecraft.world.level.storage.*;
import net.minecraft.world.level.storage.LevelStorageSource.LevelStorageAccess;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.SerializationUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import static com.minecrafttas.lotas.LoTAS.*;

/**
 * Savestate mod
 *
 * @author Pancake
 */
public class SavestateMod extends Mod {
	public SavestateMod() {
		super(new ResourceLocation("lotas", "savestatemod"));
	}

	/** Mirrored state data */
	private final StateData data = new StateData();
	
	/** Task to execute next tick */
	private Task task;

	/**
	 * Load save data on initialize
	 */
	@Override
	protected void onServerLoad() {
		this.data.onServerInitialize(this.mcserver);
	}

	/**
	 * Request state action by sending a packet to the server
	 *
	 * @param state State action (state 0 is save, state 1 is load, state 2 is delete)
	 * @param index Index of state to load/delete (only used for state 1 and 2)
	 * @param name Name of the state (only used for state 0)
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
	 * Trigger load/delete/savestate when client packet is incoming and then inform every client
	 *
	 * @param buf Packet
	 */
	@Override
	protected void onServerPayload(FriendlyByteBuf buf) {
		// prepare task for next tick
		switch (buf.readInt()) {
			case 0:
				String s = buf.readUtf(Short.MAX_VALUE);
				this.task = () -> this.savestate(s);
				break;
			case 1:
				int i = buf.readInt();
				this.task = () -> this.loadstate(i);
				break;
			case 2:
				int j = buf.readInt();
				this.task = () -> this.deletestate(j);
				break;
		}
		
		// tick the server
		if (TICK_ADVANCE.isTickadvance())
			TICK_ADVANCE.updateTickadvance();
	}

	/**
	 * Execute task on next tick
	 */
	@Override
	protected void onServerTick() {

		if (this.task != null) {
			boolean tickAdvanceState = TICK_ADVANCE.isTickadvance();
			TICK_ADVANCE.updateTickadvanceStatus(true); // enable tick advance to freeze clients

			try {
				// load state data and execute task
				this.data.loadData();
				this.task.run();
			} catch (IOException e) {
				LOGGER.error("State task failed!", e);
			}

			TICK_ADVANCE.updateTickadvanceStatus(tickAdvanceState); // restore tick advance state
			this.task = null;
		}

	}

	/**
	 * Send all states to client
	 */
	public void sendStates() {
		byte[] serialized = SerializationUtils.serialize(this.data.getStates());
		this.mcserver.getPlayerList().getPlayers().forEach(player -> {
			FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
			buf.writeInt(0);
			buf.writeByteArray(serialized);
			this.sendPacketToClient(player, buf);
		});
	}

	/**
	 * Save new state of the world
	 *
	 * @param name Savestate Name
	 * @throws IOException If an exception occurs while saving
	 */
	private void savestate(String name) throws IOException {
		for (ServerPlayer player : this.mcserver.getPlayerList().getPlayers()) {
			if (!player.isAlive()) {
				LOGGER.warn("Unable to create savestate because {} is not alive.", player.getName().getString());
				return;
			}
		}
		
		// save world
		this.mcserver.getPlayerList().saveAll();
		this.mcserver.saveAllChunks(false, true, false);
		
		// disable Session Lock
		Path levelPath = this.mcserver.storageSource.levelPath;
		this.mcserver.storageSource.lock.close();

		// make state
		State[] states = this.data.getStates();
		int latestStateIndex = states.length - 1;
		int index = latestStateIndex == -1 ? 0 : states[latestStateIndex].getIndex() + 1;
		File stateDir = new File(this.data.getWorldSavestateDir(), index + "");
		FileUtils.copyDirectory(this.data.getWorldDir(), stateDir);
		this.data.addState(new State(name == null ? "Untitled State" : name, Instant.now().getEpochSecond(), index));
		
		// re-enable session lock
		this.mcserver.storageSource.lock = DirectoryLock.create(levelPath);

		// save data and send to client
		this.data.saveData();
		this.sendStates();
	}

	/**
	 * Load state of the world
	 *
	 * @param i Index to load
	 * @throws IOException If an exception occurs while loading
	 */
	private void loadstate(int i) throws IOException {
		if (!this.data.isValid(i)) {
			LOGGER.warn("Trying to load a state that does not exist: " + i);
			return;
		}
		
		// revive every player to prevent a freeze
		for (ServerPlayer player : this.mcserver.getPlayerList().getPlayers())
			if (!player.isAlive())
				player.setHealth(20.0f);
		
		// unload level
		this.unloadServerLevel();
		
		// unlock session.lock
		File worldDir = this.data.getWorldDir();
		Path levelPath = this.unlockSessionLock();

		// delete world
		FileUtils.deleteDirectory(worldDir);

		// copy state
		File worldSavestateDir = new File(this.data.getWorldSavestateDir(), this.data.getStates()[i].getIndex() + "");
		FileUtils.copyDirectory(worldSavestateDir, worldDir);

		// lock session.lock
		this.lockSessionLock(levelPath);

		// reload level
		this.loadWorldData();
		this.loadWorldLighting();
		this.loadPlayers();
	}

	/**
	 * Unload all levels
	 *
	 * @throws IOException If an exception occurs while unloading
	 */
	private void unloadServerLevel() throws IOException {
		for (ServerLevel level : this.mcserver.getAllLevels()) {
			ServerChunkCache chunkCache = level.getChunkSource();
			DistanceManager distanceManager = chunkCache.distanceManager;
			ChunkMap map = chunkCache.chunkMap;

			// clear future entities
			level.toAddAfterTick.clear();

			// kill ender dragons (only mob with sub entities)
			if (level.dragonFight != null)
				for (EnderDragon dragon : level.getEntitiesOfClass(EnderDragon.class, new AABB(-256, 0, -256, 256, 256, 256)))
					dragon.kill();

			// despawn existing entities
			for (Entity entity : new ArrayList<>(level.entitiesById.values()))
				if (entity != null)
					level.despawn(entity);

			// remove chunk loading requests
			distanceManager.tickets.clear();
			distanceManager.chunksToUpdateFutures.clear();
			distanceManager.ticketsToRelease.clear();
			
			// unload chunks
			map.pendingUnloads.clear();
			map.updatingChunkMap.clear();
			map.visibleChunkMap.clear();
			map.entitiesInLevel.clear();
			map.processUnloads(() -> true);

			// clear lighting engine
			chunkCache.getLightEngine().blockEngine = null;
			chunkCache.getLightEngine().skyEngine = null;

			// clear chunk cache
			chunkCache.clearCache();

			// close files
			for (RegionFile file : map.poiManager.worker.storage.regionCache.values())
				file.close();

			map.poiManager.loadedChunks.clear();
			map.poiManager.storage.clear();
			map.poiManager.worker.storage.regionCache.clear();
			map.poiManager.worker.pendingWrites.clear();

			for (RegionFile file : map.worker.storage.regionCache.values())
				file.close();

			map.worker.storage.regionCache.clear();
			map.worker.pendingWrites.clear();
		}
	}

	/**
	 * Unlock session lock
	 *
	 * @return Path to level
	 * @throws IOException If an exception occurs while unlocking
	 */
	private Path unlockSessionLock() throws IOException {
		Path levelPath = this.mcserver.storageSource.levelPath;
		this.mcserver.storageSource.lock.close();
		return levelPath;
	}

	/**
	 * Create session lock
	 *
	 * @param levelPath Path to level
	 * @throws IOException If an exception occurs while locking
	 */
	private void lockSessionLock(Path levelPath) throws IOException {
		this.mcserver.storageSource.lock = DirectoryLock.create(levelPath);
	}

	/**
	 * Load world lighting
	 */
	private void loadWorldLighting() {
		for (ServerLevel level : this.mcserver.getAllLevels()) {
			ServerChunkCache chunkCache = level.getChunkSource();
			chunkCache.getLightEngine().blockEngine = new BlockLightEngine(chunkCache);
			chunkCache.getLightEngine().skyEngine = level.dimensionType().hasSkyLight() ? new SkyLightEngine(chunkCache) : null;
		}
	}

	/**
	 * Load all players
	 */
	private void loadPlayers() {
		PlayerList playerList = this.mcserver.getPlayerList();
		for (ServerPlayer player : new ArrayList<>(playerList.getPlayers())) {
			
			// load player data
			CompoundTag compoundTag = playerList.load(player);
			@SuppressWarnings("deprecation")
			ServerLevel newLevel = this.mcserver.getLevel(compoundTag != null ? DimensionType.parseLegacy(new Dynamic<>(NbtOps.INSTANCE, compoundTag.get("Dimension"))).result().orElse(Level.OVERWORLD) : Level.OVERWORLD);

	        // update client before spawning
			LevelData levelData = newLevel.getLevelData();
	        player.connection.send(new ClientboundRespawnPacket(newLevel.dimensionTypeKey(), newLevel.dimension(), BiomeManager.obfuscateSeed(newLevel.getSeed()), player.gameMode.getGameModeForPlayer(), player.gameMode.getPreviousGameModeForPlayer(), newLevel.isDebug(), newLevel.isFlat(), true));
	        player.connection.send(new ClientboundChangeDifficultyPacket(levelData.getDifficulty(), levelData.isDifficultyLocked()));
	        player.server.getPlayerList().sendPlayerPermissionLevel(player);
	        
	        // add player to level
	        Vec3 pos = player.position();
	        player.moveTo(pos.x(), pos.y(), pos.z(), player.yRot, player.xRot);
	        player.setLevel(newLevel);
	        newLevel.addDuringPortalTeleport(player);
	        
	        // update client level
	        player.connection.teleport(pos.x(), pos.y(), pos.z(), player.yRot, player.xRot);
	        player.gameMode.setLevel(newLevel);
	        player.connection.send(new ClientboundPlayerAbilitiesPacket(player.abilities));
	        player.server.getPlayerList().sendLevelInfo(player, newLevel);
	        player.server.getPlayerList().sendAllPlayerInfo(player);
	        player.connection.send(new ClientboundSetHealthPacket(player.getHealth(), player.getFoodData().getFoodLevel(), player.getFoodData().getSaturationLevel()));
	        player.connection.send(new ClientboundSetExperiencePacket(player.experienceProgress, player.totalExperience, player.experienceLevel));
	        for (MobEffectInstance mobEffectInstance : player.getActiveEffects())
	        	player.connection.send(new ClientboundUpdateMobEffectPacket(player.getId(), mobEffectInstance));
		
	        // update player advancements
			PlayerAdvancements adv = player.getAdvancements();
			adv.reload(this.mcserver.getAdvancements());
            adv.flushDirty(player);
            
            // update player stats
            playerList.stats.remove(player.getUUID());
			ServerStatsCounter stats = playerList.getPlayerStats(player);
            player.stats = stats;
            stats.sendStats(player);
            
            // update dupe mod data
			FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
    		buf.writeBoolean(true);
            DUPE_MOD.onServerPayload(buf);
            
            // run quality of life stuff
            player.getCooldowns().cooldowns.clear();
            
            // run quality of life stuff on the client
    		FriendlyByteBuf buf2 = new FriendlyByteBuf(Unpooled.buffer());
    		buf2.writeInt(-1);
            this.sendPacketToClient(player, buf2);
		}
	}

	/**
	 * Load world data
	 */
	private void loadWorldData() {
		WorldData worldData = this.loadWorldData(this.mcserver.storageSource);
		this.mcserver.worldData = worldData;
		for (ServerLevel level : this.mcserver.getAllLevels()) {
			ServerLevelData data = worldData.overworldData();
			if (level.dimension() != Level.OVERWORLD)
				data = new DerivedLevelData(worldData, data);

			level.levelData = data;
			level.serverLevelData = data;

			if (level.dragonFight != null)
				level.dragonFight = new EndDragonFight(level, this.mcserver.getWorldData().worldGenSettings().seed(), this.mcserver.getWorldData().endDragonFightData());
		}
	}

	/**
	 * Load world data from level storage access
	 *
	 * @param levelStorageAccess Level Storage Access
	 * @return World Data
	 */
	private WorldData loadWorldData(LevelStorageAccess levelStorageAccess) {
        ServerResources serverResources;
		DataPackConfig dataPackConfig = levelStorageAccess.getDataPacks();
        PackRepository<Pack> packRepository = new PackRepository<>(Pack::new, new ServerPacksSource(), new FolderRepositorySource(levelStorageAccess.getLevelPath(LevelResource.DATAPACK_DIR).toFile(), PackSource.WORLD));
        DataPackConfig dataPackConfig2 = MinecraftServer.configurePackRepository(packRepository, dataPackConfig == null ? DataPackConfig.DEFAULT : dataPackConfig, false);
        CompletableFuture<ServerResources> completableFuture = ServerResources.loadResources(packRepository.openAllSelected(), Commands.CommandSelection.DEDICATED, 2, Util.backgroundExecutor(), Runnable::run);
        try {
			serverResources = completableFuture.get();
        } catch (Exception exception) {
            packRepository.close();
            return null;
        }
        serverResources.updateGlobals();
		RegistryReadOps<Tag> registryReadOps = RegistryReadOps.create(NbtOps.INSTANCE, serverResources.getResourceManager(), RegistryAccess.builtin());
        return levelStorageAccess.getDataTag(registryReadOps, dataPackConfig2);
	}
	
	/**
	 * Delete state of the world
	 *
	 * @param i Index to delete
	 * @throws IOException If an exception occurs while deleting
	 */
	private void deletestate(int i) throws IOException {
		if (!this.data.isValid(i)) {
			LOGGER.warn("Trying to delete a state that does not exist: " + i);
			return;
		}
		
		// delete State
		FileUtils.deleteDirectory(new File(this.data.getWorldSavestateDir(), this.data.getStates()[i].getIndex() + ""));
		this.data.removeState(i);

		// save data and send to client
		this.data.saveData();
		this.sendStates();
	}
	
	
	/**
	 * Update state list on incoming packet
	 *
	 * @param buf Packet
	 */
	@Override
	@Environment(EnvType.CLIENT)
	protected void onClientsidePayload(FriendlyByteBuf buf) {
		switch (buf.readInt()) {
			case -1: // update after loadstate
				this.mc.gui.getChat().clearMessages(false);
				this.mc.getToasts().clear();
				this.mc.particleEngine.particles.clear();
				this.mc.player.getCooldowns().cooldowns.clear();
				this.mc.player.attackStrengthTicker = 100;
				this.mc.player.lastItemInMainHand = this.mc.player.getItemBySlot(EquipmentSlot.MAINHAND);
				break;
			case 1: // load states
				this.data.deserializeData(buf.readByteArray());
				break;
		}
	}

	/**
	 * Update client data on connect
	 */
	public void onClientConnect(ServerPlayer c) {
		try {
			this.data.loadData();
			this.sendStates();
		} catch (IOException e) {
			LOGGER.warn("Unable to send states to client.", e);
		}
	}

	public interface Task {
		void run() throws IOException;
	}
}
