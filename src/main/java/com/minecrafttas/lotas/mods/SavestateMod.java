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
import java.nio.file.Path;
// # 1.19.3
//$$import java.nio.file.Paths;
// # end
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
import net.minecraft.server.level.DistanceManager;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.stats.ServerStatsCounter;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.level.chunk.storage.RegionFile;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.end.EndDragonFight;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.storage.DerivedLevelData;
import net.minecraft.world.level.storage.LevelData;
import net.minecraft.world.level.lighting.BlockLightEngine;
import net.minecraft.world.level.lighting.SkyLightEngine;

// # 1.19.3
//$$import net.minecraft.server.RegistryLayer;
//$$import net.minecraft.server.WorldLoader;
//$$import net.minecraft.server.WorldLoader.DataLoadContext;
//$$import net.minecraft.server.WorldLoader.InitConfig;
//$$import net.minecraft.server.dedicated.DedicatedServerProperties;
//$$import net.minecraft.server.dedicated.DedicatedServerSettings;
//$$import net.minecraft.resources.RegistryDataLoader;
//$$import net.minecraft.world.level.WorldDataConfiguration;
//$$import net.minecraft.core.LayeredRegistryAccess;
//$$import net.minecraft.core.registries.Registries;
//$$import net.minecraft.world.level.storage.LevelResource;
//$$import net.minecraft.world.level.storage.LevelStorageSource;
//$$import net.minecraft.server.packs.repository.PackRepository;
//$$import net.minecraft.server.packs.repository.ServerPacksSource;
//$$import net.minecraft.server.packs.resources.CloseableResourceManager;
//$$import net.minecraft.world.level.dimension.LevelStem;
//$$import net.minecraft.commands.Commands;
//$$import net.minecraft.core.Registry;
//$$import com.mojang.datafixers.util.Pair;
// # end

// # 1.17.1
// ## 1.19.3
//$$import net.minecraft.world.flag.FeatureFlags;
// ## end
//$$import net.minecraft.core.BlockPos;
//$$import net.minecraft.world.level.ChunkPos;
//$$import net.minecraft.util.Unit;
//$$import net.minecraft.server.level.TicketType;
//$$import net.minecraft.server.level.Ticket;
//$$import net.minecraft.world.level.entity.EntityTickList;
//$$import net.minecraft.world.level.entity.PersistentEntitySectionManager;
//$$import net.minecraft.world.entity.Entity.RemovalReason;
//$$import net.minecraft.world.level.chunk.ChunkStatus;
//$$import net.minecraft.world.level.chunk.storage.EntityStorage;
// # end

// # 1.18.2
//$$import net.minecraft.resources.RegistryOps;
// # 1.16.1
//$$import net.minecraft.resources.RegistryReadOps;
//$$import net.minecraft.server.ServerResources;
//$$import java.util.concurrent.CompletableFuture;
//$$
//$$import net.minecraft.Util;
//$$import net.minecraft.commands.Commands;
//$$import net.minecraft.server.MinecraftServer;
//$$import net.minecraft.server.packs.repository.FolderRepositorySource;
//$$import net.minecraft.server.packs.repository.PackRepository;
//$$import net.minecraft.server.packs.repository.ServerPacksSource;
//$$import net.minecraft.server.packs.repository.PackSource;
//$$import net.minecraft.world.level.storage.LevelResource;
//$$import net.minecraft.world.level.DataPackConfig;
// # end

// # 1.16.1
//$$import net.minecraft.world.level.Level;
//$$import net.minecraft.util.DirectoryLock;
//$$import net.minecraft.nbt.CompoundTag;
//$$import net.minecraft.nbt.NbtOps;
//$$import net.minecraft.nbt.Tag;
//$$import net.minecraft.core.RegistryAccess;
//$$import net.minecraft.world.level.biome.BiomeManager;
//$$import net.minecraft.world.level.storage.LevelStorageSource.LevelStorageAccess;
//$$import net.minecraft.world.level.storage.ServerLevelData;
//$$import net.minecraft.world.level.storage.WorldData;
//$$
//$$import com.mojang.serialization.Dynamic;
// # def
//$$import net.minecraft.world.level.dimension.NormalDimension;
//$$import net.minecraft.world.level.dimension.end.TheEndDimension;
//$$import net.minecraft.world.level.storage.LevelStorageSource;
//$$import net.minecraft.server.level.DerivedServerLevel;
//$$import java.nio.file.StandardOpenOption;
//$$import java.nio.file.Files;
// # end

/**
 * Main savestate mod
 * @author Pancake
 */
public class SavestateMod extends Mod {

	public static SavestateMod instance;
	
	/**
	 * Initializes the savestate mod
	 */
	public SavestateMod() {
		super(new ResourceLocation("lotas", "savestatemod"));
		instance = this;
	}

	/**
	 * Mirrored state data
	 */
	private StateData data = new StateData();
	
	/**
	 * Task for next tick
	 */
	private Task task;
	
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
		// Prepare task for next tick
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
		
		// Tick the server
		if (TickAdvance.instance.isTickadvanceEnabled())
			TickAdvance.instance.updateTickadvance();
	}

	/**
	 * Savestates/loadstates/deletestates after the tick
	 */
	@Override
	protected void onServerTick() {
		// Run task
		if (this.task != null) {
			boolean tickAdvanceState = TickAdvance.instance.isTickadvanceEnabled();
			TickAdvance.instance.updateTickadvanceStatus(true);
			try {
				this.data.loadData();
				this.task.run();
			} catch (IOException e) {
				LoTAS.LOGGER.error("State task failed!", e);
			}
			TickAdvance.instance.updateTickadvanceStatus(tickAdvanceState);
			this.task = null;
		}
	}

	/**
	 * Sends all states to the client
	 */
	public void sendStates() {
		byte[] serialized = this.data.serializeData();
		this.mcserver.getPlayerList().getPlayers().forEach(player -> {
			FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
			buf.writeInt(0);
			buf.writeByteArray(serialized);
			this.sendPacketToClient(player, buf);
		});
	}

	/**
	 * Saves a new state of the world
	 * @param name Savestate Name
	 * @throws IOException Filesystem Exception 
	 */
	private void savestate(String name) throws IOException {
		for (ServerPlayer player : this.mcserver.getPlayerList().getPlayers()) {
			if (!player.isAlive()) {
				LoTAS.LOGGER.warn("Unable to create savestate because {} is not alive.", player.getName().getString());
				return;
			}
		}
		
		// Save world
		this.mcserver.getPlayerList().saveAll();
		this.mcserver.saveAllChunks(false, true, false);
		
		// Disable Session Lock
		// # 1.16.1
		// ## 1.19.3
//$$		Path levelPath = this.mcserver.storageSource.getLevelPath(LevelResource.ROOT);
		// ## def
//$$		Path levelPath = this.mcserver.storageSource.levelPath;
		// ## end
//$$		this.mcserver.storageSource.lock.close();
		// # end
		
		// Make state
		int latestStateIndex = this.data.getStateCount() - 1;
		int index = latestStateIndex == -1 ? 0 : this.data.getState(latestStateIndex).getIndex() + 1;
		File stateDir = new File(this.data.getWorldSavestateDir(), index + "");
		FileUtils.copyDirectory(this.data.getWorldDir(), stateDir);
		this.data.addState(new State(name == null ? "Untitled State" : name, Instant.now().getEpochSecond(), index));
		
		// Reenable Session Lock
		// # 1.16.1
//$$		this.mcserver.storageSource.lock = DirectoryLock.create(levelPath);
		// # end
		
		// Save data and send to client
		this.data.saveData();
		this.sendStates();
	}

	/**
	 * Loads a state of the world
	 * @param i Index to load
	 * @throws IOException Filesystem Excepion
	 */
	private void loadstate(int i) throws IOException {
		if (!this.data.isValid(i)) {
			LoTAS.LOGGER.warn("Trying to load a state that does not exist: " + i);
			return;
		}
		
		// Revive every player to prevent a freeze
		for (ServerPlayer player : this.mcserver.getPlayerList().getPlayers())
			if (!player.isAlive())
				player.setHealth(20.0f);
		
		// Unload level
		this.unloadServerLevel();
		
		// Unlock session.lock
		File worldDir = this.data.getWorldDir();
		// # 1.16.1
//$$		Path levelPath = this.unlockSessionLock();
		// # def
//$$		byte[] sessionLock = this.unlockSessionLock(worldDir);
		// # end
		
		// Delete world
		FileUtils.deleteDirectory(worldDir);

		// Copy state
		File worldSavestateDir = new File(this.data.getWorldSavestateDir(), this.data.getState(i).getIndex() + "");
		FileUtils.copyDirectory(worldSavestateDir, this.data.getWorldDir());

		// Lock session.lock
		// # 1.16.1
//$$		this.lockSessionLock(levelPath);
		// # def
//$$		this.lockSessionLock(sessionLock, worldDir);
		// # end

		// Reload level
		this.loadWorldData(worldDir);
		this.loadWorld();
		this.loadPlayers();
	}
	
	private void unloadServerLevel() throws IOException {
		for (ServerLevel level : this.mcserver.getAllLevels()) {
			ServerChunkCache chunkCache = level.getChunkSource();
			DistanceManager distanceManager = chunkCache.distanceManager;
			ChunkMap map = chunkCache.chunkMap;
			
			// Clear global and future entities
			// # 1.17.1
//$$			
			// # 1.16.1
//$$			level.toAddAfterTick.clear();
			// # def
//$$			level.globalEntities.clear();
			// # end
			
			// # 1.16.1
//$$			if (level.dragonFight != null)
//$$				for (EnderDragon dragon : level.getEntitiesOfClass(EnderDragon.class, new AABB(-256, 0, -256, 256, 256, 256)))
//$$					dragon.kill();
			// # def
//$$			if (level.dimension instanceof TheEndDimension)
//$$				for (EnderDragon dragon : level.getEntitiesOfClass(EnderDragon.class, new AABB(-256, 0, -256, 256, 256, 256)))
//$$					dragon.kill();
			// # end
			
			// # 1.17.1
//$$			level.entityTickList = new EntityTickList();
//$$			
//$$			ArrayList<Entity> temp = new ArrayList<>();
//$$			level.getAllEntities().forEach(c -> {
//$$				temp.add(c);
//$$			});
//$$			for (Entity entity : temp) {
//$$				if (entity == null)
//$$					continue;
//$$				entity.remove(RemovalReason.UNLOADED_WITH_PLAYER);
//$$			}
//$$			temp.clear();
//$$			
//$$			PersistentEntitySectionManager<Entity> entityManager = level.entityManager;
//$$			EntityStorage entityStorage = (EntityStorage) entityManager.permanentStorage;
//$$			entityStorage.worker.pendingWrites.clear();
//$$			entityManager.knownUuids.clear();
//$$			entityManager.sectionStorage.sectionIds.clear();
//$$			entityManager.sectionStorage.sections.clear();
//$$			entityManager.chunkVisibility.clear();
//$$			entityManager.chunkLoadStatuses.clear();
//$$			entityManager.loadingInbox.clear();
//$$			entityManager.chunksToUnload.clear();
//$$			
//$$			for (Entity entity : entityManager.visibleEntityStorage.getAllEntities())
//$$				entityManager.visibleEntityStorage.remove(entity);
//$$			
//$$			for (RegionFile file : entityStorage.worker.storage.regionCache.values())
//$$				file.close();
//$$			entityStorage.worker.storage.regionCache.clear();
			// # def
//$$			// Despawn existing entities
//$$			for (Entity entity : new ArrayList<>(level.entitiesById.values()))
//$$				if (entity != null)
//$$					level.despawn(entity);
			// # end
			
			// Remove chunk loading requests
			distanceManager.tickets.clear();
			distanceManager.chunksToUpdateFutures.clear();
			distanceManager.ticketsToRelease.clear();
			
			// Unload chunks
			map.pendingUnloads.clear();
			map.updatingChunkMap.clear();
			map.visibleChunkMap.clear();
			map.entitiesInLevel.clear();
			map.processUnloads(() -> true);
			
			chunkCache.getLightEngine().blockEngine = null;
			chunkCache.getLightEngine().skyEngine = null;
			
			// # 1.15.2
//$$
			// # def
//$$			// Unload nether portals
//$$			level.getPortalForcer().cachedPortals.clear();
			// # end
			
			// Clear chunk cache
			chunkCache.clearCache();

			// Close file
			// # 1.15.2
//$$			for (RegionFile file : map.poiManager.worker.storage.regionCache.values())
//$$				file.close();
//$$			map.poiManager.loadedChunks.clear();
//$$			map.poiManager.storage.clear();
//$$			map.poiManager.worker.storage.regionCache.clear();
//$$			map.poiManager.worker.pendingWrites.clear();
//$$			
//$$			for (RegionFile file : map.worker.storage.regionCache.values())
//$$				file.close();
//$$			map.worker.storage.regionCache.clear();
//$$			map.worker.pendingWrites.clear();
			// # def
//$$			for (RegionFile file : map.poiManager.regionCache.values())
//$$				file.close();
//$$			map.poiManager.regionCache.clear();
//$$			
//$$			for (RegionFile file : map.regionCache.values())
//$$				file.close();
//$$			map.regionCache.clear();
			// # end
		}
	}
	
	// # 1.16.1
//$$	private Path unlockSessionLock() throws IOException {
	// # def
//$$	private byte[] unlockSessionLock(File worldDir) throws IOException {
	// # end
		// # 1.16.1
		// ## 1.19.3
//$$		Path levelPath = this.mcserver.storageSource.getLevelPath(LevelResource.ROOT);
		// ## def
//$$		Path levelPath = this.mcserver.storageSource.levelPath;
		// ## end
//$$		this.mcserver.storageSource.lock.close();
//$$		return levelPath;
		// # def
//$$		Path sessionLockFile = new File(worldDir, "session.lock").toPath();
//$$		return Files.readAllBytes(sessionLockFile);
		// # end
	}
	
	// # 1.16.1
//$$	private void lockSessionLock(Path levelPath) throws IOException {
	// # def
//$$	private void lockSessionLock(byte[] sessionLock, File worldDir) throws IOException {
	// # end
		// # 1.16.1
//$$		this.mcserver.storageSource.lock = DirectoryLock.create(levelPath);
		// # def
//$$		Path sessionLockFile = new File(worldDir, "session.lock").toPath();
//$$ 		Files.write(sessionLockFile, sessionLock, StandardOpenOption.WRITE, StandardOpenOption.CREATE);
		// # end
	}
	
	private void loadWorld() {
		for (ServerLevel level : this.mcserver.getAllLevels()) {
			ServerChunkCache chunkCache = level.getChunkSource();
			// # 1.16.1
//$$			chunkCache.getLightEngine().blockEngine = new BlockLightEngine(chunkCache);
//$$			chunkCache.getLightEngine().skyEngine = level.dimensionType().hasSkyLight() ? new SkyLightEngine(chunkCache) : null;
			// # def
//$$			chunkCache.getLightEngine().blockEngine = new BlockLightEngine(chunkCache);
//$$			chunkCache.getLightEngine().skyEngine = level.dimension instanceof NormalDimension ? new SkyLightEngine(chunkCache) : null;
			// # end
			
			// # 1.17.1
//$$			BlockPos blockPos = level.getSharedSpawnPos();
//$$			chunkCache.addRegionTicket(TicketType.START,new ChunkPos(blockPos), 11, Unit.INSTANCE);
			// # end
		}
	}
	
	// # 1.18.2
//$$	@SuppressWarnings("deprecation")
	// # end
	private void loadPlayers() {
		PlayerList playerList = this.mcserver.getPlayerList();
		for (ServerPlayer player : new ArrayList<>(playerList.getPlayers())) {
			
			// Load player data
			// # 1.16.1
//$$			CompoundTag compoundTag = playerList.load(player);
//$$			ServerLevel newLevel = this.mcserver.getLevel(compoundTag != null ? DimensionType.parseLegacy(new Dynamic<Tag>(NbtOps.INSTANCE, compoundTag.get("Dimension"))).result().orElse(Level.OVERWORLD) : Level.OVERWORLD);
			// # def
//$$			playerList.load(player);
//$$			ServerLevel newLevel = this.mcserver.getLevel(player.dimension);
			// # end

	        // Update client pre-level
	        LevelData levelData = newLevel.getLevelData();
	        // # 1.20.1
//$$	        player.connection.send(new ClientboundRespawnPacket(newLevel.dimensionTypeId(), newLevel.dimension(), BiomeManager.obfuscateSeed(newLevel.getSeed()), player.gameMode.getGameModeForPlayer(), player.gameMode.getPreviousGameModeForPlayer(), newLevel.isDebug(), newLevel.isFlat(), (byte) 1, player.getLastDeathLocation(), player.getPortalCooldown()));        
	        // # 1.19.3
//$$	        player.connection.send(new ClientboundRespawnPacket(newLevel.dimensionTypeId(), newLevel.dimension(), BiomeManager.obfuscateSeed(newLevel.getSeed()), player.gameMode.getGameModeForPlayer(), player.gameMode.getPreviousGameModeForPlayer(), newLevel.isDebug(), newLevel.isFlat(), (byte) 1, player.getLastDeathLocation()));        
	        // # 1.18.2
//$$	        player.connection.send(new ClientboundRespawnPacket(newLevel.dimensionTypeRegistration(), newLevel.dimension(), BiomeManager.obfuscateSeed(newLevel.getSeed()), player.gameMode.getGameModeForPlayer(), player.gameMode.getPreviousGameModeForPlayer(), newLevel.isDebug(), newLevel.isFlat(), true));        
	        // # 1.16.5
//$$	        player.connection.send(new ClientboundRespawnPacket(newLevel.dimensionType(), newLevel.dimension(), BiomeManager.obfuscateSeed(newLevel.getSeed()), player.gameMode.getGameModeForPlayer(), player.gameMode.getPreviousGameModeForPlayer(), newLevel.isDebug(), newLevel.isFlat(), true));        
	        // # 1.16.1
//$$	        player.connection.send(new ClientboundRespawnPacket(newLevel.dimensionTypeKey(), newLevel.dimension(), BiomeManager.obfuscateSeed(newLevel.getSeed()), player.gameMode.getGameModeForPlayer(), player.gameMode.getPreviousGameModeForPlayer(), newLevel.isDebug(), newLevel.isFlat(), true));        
	        // # 1.15.2
//$$	        player.connection.send(new ClientboundRespawnPacket(player.dimension, LevelData.obfuscateSeed(levelData.getSeed()), levelData.getGeneratorType(), player.gameMode.getGameModeForPlayer()));
	        // # def
//$$	        player.connection.send(new ClientboundRespawnPacket(player.dimension, levelData.getGeneratorType(), player.gameMode.getGameModeForPlayer()));
	        // # end
	        player.connection.send(new ClientboundChangeDifficultyPacket(levelData.getDifficulty(), levelData.isDifficultyLocked()));
	        player.server.getPlayerList().sendPlayerPermissionLevel(player);
	        
	        // Add player to level
	        Vec3 pos = player.position();
	        player.moveTo(pos.x(), pos.y(), pos.z(), player.yRot, player.xRot);
	        player.setLevel(newLevel);
	        newLevel.addDuringPortalTeleport(player); // FIXME: player not added 1.17+
	        
	        // # 1.17.1
//$$	        ChunkPos chunkPos = player.chunkPosition();
//$$	        @SuppressWarnings({ "unchecked", "rawtypes" })
//$$			Ticket<?> ticket = new Ticket(TicketType.PLAYER, 33 + ChunkStatus.getDistance(ChunkStatus.FULL) - 2, chunkPos);
//$$	        ServerChunkCache chunkSource = newLevel.getChunkSource();
//$$	        chunkSource.distanceManager.addTicket(chunkPos.toLong(), ticket);
	        // # end
	        
	        // Update client level
	        player.connection.teleport(pos.x(), pos.y(), pos.z(), player.yRot, player.xRot);
	        player.gameMode.setLevel(newLevel);
	        player.connection.send(new ClientboundPlayerAbilitiesPacket(player.abilities));
	        player.server.getPlayerList().sendLevelInfo(player, newLevel);
	        player.server.getPlayerList().sendAllPlayerInfo(player);
	        player.connection.send(new ClientboundSetHealthPacket(player.getHealth(), player.getFoodData().getFoodLevel(), player.getFoodData().getSaturationLevel()));
	        player.connection.send(new ClientboundSetExperiencePacket(player.experienceProgress, player.totalExperience, player.experienceLevel));
	        for (MobEffectInstance mobEffectInstance : player.getActiveEffects())
	        	player.connection.send(new ClientboundUpdateMobEffectPacket(player.getId(), mobEffectInstance));
		
	        // Update player advancements
			PlayerAdvancements adv = player.getAdvancements();
			// # 1.16.1
//$$			adv.reload(this.mcserver.getAdvancements());
			// # def
//$$			adv.reload();
			// # end
            adv.flushDirty(player);
            
            // Update player stats
            playerList.stats.remove(player.getUUID());
            ServerStatsCounter stats = playerList.getPlayerStats(player);
            player.stats = stats;
            stats.sendStats(player);
            
            // Update dupe mod data
    		FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
    		buf.writeBoolean(true);
            DupeMod.instance.onServerPayload(buf);
            
            // Run quality of life stuff
            player.getCooldowns().cooldowns.clear();
            
            // Run quality of life stuff on the client
    		FriendlyByteBuf buf2 = new FriendlyByteBuf(Unpooled.buffer());
    		buf2.writeInt(-1);
            this.sendPacketToClient(player, buf2);
		}
	}

	private void loadWorldData(File worldDir) throws IOException {
		// # 1.16.1
//$$		WorldData worldData = this.loadWorldData(this.mcserver.storageSource);
//$$		this.mcserver.worldData = worldData;
//$$		for (ServerLevel level : this.mcserver.getAllLevels()) {
//$$			ServerLevelData data = worldData.overworldData();
//$$			if (level.dimension() != Level.OVERWORLD) {
//$$				data = new DerivedLevelData(worldData, data);
//$$			}
//$$			level.levelData = data;
//$$			level.serverLevelData = data;
//$$	
//$$				if (level.dragonFight != null)
				// ## 1.19.3
//$$					level.dragonFight = new EndDragonFight(level, this.mcserver.getWorldData().worldGenOptions().seed(), this.mcserver.getWorldData().endDragonFightData());
				// ## def
//$$					level.dragonFight = new EndDragonFight(level, this.mcserver.getWorldData().worldGenSettings().seed(), this.mcserver.getWorldData().endDragonFightData());
				// ## end
//$$		}
		// # def
//$$		LevelData data = LevelStorageSource.getLevelData(new File(worldDir, "level.dat"), this.mcserver.getFixerUpper());
//$$		for (ServerLevel level : this.mcserver.getAllLevels()) {
//$$			// Load level data
//$$			if (level instanceof DerivedServerLevel) {
//$$				level.levelData = new DerivedLevelData(data);
//$$			} else {
//$$				level.levelData = data;
//$$			}
//$$
//$$			// Load end fight
//$$			if (level.dimension instanceof TheEndDimension)
//$$				((TheEndDimension) level.dimension).dragonFight = new EndDragonFight(level, level.getLevelData().getDimensionData(DimensionType.THE_END).getCompound("DragonFight")); 
//$$		}
		// # end
			
	}

	// # 1.19.3
//$$	private WorldData loadWorldData(LevelStorageAccess levelStorageAccess) throws IOException {
//$$		InitConfig initConfig = loadOrCreateConfig();
//$$        Pair<WorldDataConfiguration, CloseableResourceManager> pair = initConfig.packConfig.createResourceManager();
//$$        CloseableResourceManager closeableResourceManager = pair.getSecond();
//$$        LayeredRegistryAccess<RegistryLayer> layeredRegistryAccess = RegistryLayer.createRegistryAccess();
//$$        LayeredRegistryAccess<RegistryLayer> layeredRegistryAccess2 = WorldLoader.loadAndReplaceLayer(closeableResourceManager, layeredRegistryAccess, RegistryLayer.WORLDGEN, RegistryDataLoader.WORLDGEN_REGISTRIES);
//$$        RegistryAccess.Frozen frozen = layeredRegistryAccess2.getAccessForLoading(RegistryLayer.DIMENSIONS);
//$$        RegistryAccess.Frozen frozen2 = RegistryDataLoader.load(closeableResourceManager, frozen, RegistryDataLoader.DIMENSION_REGISTRIES);
//$$        WorldDataConfiguration worldDataConfiguration = pair.getFirst();
//$$		
//$$		DataLoadContext context = new DataLoadContext(closeableResourceManager, worldDataConfiguration, frozen, frozen2);
//$$		Registry<LevelStem> registry = context.datapackDimensions().registryOrThrow(Registries.LEVEL_STEM);
//$$		
//$$        RegistryOps<Tag> dynamicOps = RegistryOps.create(NbtOps.INSTANCE, context.datapackWorldgen());
//$$        return levelStorageAccess.getDataTag(dynamicOps, levelStorageAccess.getDataConfiguration(), registry, context.datapackWorldgen().allRegistriesLifecycle()).getFirst();
//$$	}
	// # 1.18.2
//$$	private WorldData loadWorldData(LevelStorageAccess levelStorageAccess) {
//$$        RegistryAccess.Writable writable = RegistryAccess.builtinCopy();
//$$        RegistryOps<Tag> dynamicOps = RegistryOps.createAndLoad(NbtOps.INSTANCE, writable, this.mcserver.getResourceManager());
//$$        return levelStorageAccess.getDataTag(dynamicOps, levelStorageAccess.getDataPacks(), writable.allElementsLifecycle());
//$$	}
	// # 1.16.1
//$$	private WorldData loadWorldData(LevelStorageAccess levelStorageAccess) {
//$$        ServerResources serverResources;
//$$		DataPackConfig dataPackConfig = levelStorageAccess.getDataPacks();
	// # end
		// # 1.18.2
//$$		
		// # 1.17.1
//$$		PackRepository packRepository = new PackRepository(net.minecraft.server.packs.PackType.SERVER_DATA, new ServerPacksSource(), new FolderRepositorySource(levelStorageAccess.getLevelPath(LevelResource.DATAPACK_DIR).toFile(), PackSource.WORLD));
//$$        DataPackConfig dataPackConfig2 = MinecraftServer.configurePackRepository(packRepository, dataPackConfig == null ? DataPackConfig.DEFAULT : dataPackConfig, false);
//$$        CompletableFuture<ServerResources> completableFuture = ServerResources.loadResources(packRepository.openAllSelected(), RegistryAccess.builtin(), Commands.CommandSelection.DEDICATED, 2, Util.backgroundExecutor(), Runnable::run);
		// # 1.16.5
//$$		PackRepository packRepository = new PackRepository(new ServerPacksSource(), new FolderRepositorySource(levelStorageAccess.getLevelPath(LevelResource.DATAPACK_DIR).toFile(), PackSource.WORLD));
//$$        DataPackConfig dataPackConfig2 = MinecraftServer.configurePackRepository(packRepository, dataPackConfig == null ? DataPackConfig.DEFAULT : dataPackConfig, false);
//$$        CompletableFuture<ServerResources> completableFuture = ServerResources.loadResources(packRepository.openAllSelected(), Commands.CommandSelection.DEDICATED, 2, Util.backgroundExecutor(), Runnable::run);
		// # 1.16.1
//$$        PackRepository<net.minecraft.server.packs.repository.Pack> packRepository = new PackRepository<net.minecraft.server.packs.repository.Pack>(net.minecraft.server.packs.repository.Pack::new, new ServerPacksSource(), new FolderRepositorySource(levelStorageAccess.getLevelPath(LevelResource.DATAPACK_DIR).toFile(), PackSource.WORLD));
//$$        DataPackConfig dataPackConfig2 = MinecraftServer.configurePackRepository(packRepository, dataPackConfig == null ? DataPackConfig.DEFAULT : dataPackConfig, false);
//$$        CompletableFuture<ServerResources> completableFuture = ServerResources.loadResources(packRepository.openAllSelected(), Commands.CommandSelection.DEDICATED, 2, Util.backgroundExecutor(), Runnable::run);
        // # end
    // # 1.18.2
//$$        
	// # 1.16.1
//$$        try {
//$$            serverResources = completableFuture.get();
//$$        }
//$$        catch (Exception exception) {
//$$            packRepository.close();
//$$            return null;
//$$        }
//$$        serverResources.updateGlobals();
//$$        RegistryAccess.RegistryHolder exception = RegistryAccess.builtin();
//$$        RegistryReadOps<net.minecraft.nbt.Tag> registryReadOps = RegistryReadOps.create(NbtOps.INSTANCE, serverResources.getResourceManager(), exception);
//$$        WorldData worldData = levelStorageAccess.getDataTag(registryReadOps, dataPackConfig2);
//$$        return worldData;
//$$	}
	// # end
	
	
	// # 1.19.3
//$$    private static WorldLoader.InitConfig loadOrCreateConfig() throws IOException {
//$$    	Path path = Paths.get("server.properties", new String[0]);
//$$        DedicatedServerSettings dedicatedServerSettings = new DedicatedServerSettings(path);
//$$        dedicatedServerSettings.forceSave();
//$$        
//$$        File file = new File(".");
//$$        String string = dedicatedServerSettings.getProperties().levelName;
//$$        
//$$        LevelStorageSource levelStorageSource = LevelStorageSource.createDefault(file.toPath());
//$$        LevelStorageSource.LevelStorageAccess levelStorageAccess = levelStorageSource.createAccess(string);
//$$        PackRepository packRepository = ServerPacksSource.createPackRepository(levelStorageAccess.getLevelPath(LevelResource.DATAPACK_DIR));
//$$        
//$$        DedicatedServerProperties dedicatedServerProperties = dedicatedServerSettings.getProperties();
//$$        
//$$        WorldDataConfiguration worldDataConfiguration2;
//$$        boolean bl2;
//$$        WorldDataConfiguration worldDataConfiguration = levelStorageAccess.getDataConfiguration();
//$$        if (worldDataConfiguration != null) {
//$$            bl2 = false;
//$$            worldDataConfiguration2 = worldDataConfiguration;
//$$        } else {
//$$            bl2 = true;
//$$            worldDataConfiguration2 = new WorldDataConfiguration(dedicatedServerProperties.initialDataPackConfiguration, FeatureFlags.DEFAULT_FLAGS);
//$$        }
//$$        WorldLoader.PackConfig packConfig = new WorldLoader.PackConfig(packRepository, worldDataConfiguration2, false, bl2);
//$$        return new WorldLoader.InitConfig(packConfig, Commands.CommandSelection.DEDICATED, dedicatedServerProperties.functionPermissionLevel);
//$$    }
    // # end
	
	/**
	 * Deletes a state of the world
	 * @param i Index to delete
	 * @throws IOException Filesystem Exception
	 */
	private void deletestate(int i) throws IOException {
		if (!this.data.isValid(i)) {
			LoTAS.LOGGER.warn("Trying to delete a state that does not exist: " + i);
			return;
		}
		
		// Delete State
		FileUtils.deleteDirectory(new File(this.data.getWorldSavestateDir(), this.data.getState(i).getIndex() + ""));
		this.data.removeState(i);

		// Save data and send to client
		this.data.saveData();
		this.sendStates();
	}
	
	
	/**
	 * Updates the state list on incoming packet
	 * @param buf Packet Data
	 */
	@Override
	@Environment(EnvType.CLIENT)
	protected void onClientsidePayload(FriendlyByteBuf buf) {
		switch (buf.readInt()) {
			case -1: // Update after loadstate
				this.mc.gui.getChat().clearMessages(false);
				this.mc.getToasts().clear();
				this.mc.particleEngine.particles.clear();
				this.mc.player.getCooldowns().cooldowns.clear();
				this.mc.player.attackStrengthTicker = 100;
				this.mc.player.lastItemInMainHand = this.mc.player.getItemBySlot(EquipmentSlot.MAINHAND);
				break;
			case 1: // Load states
				this.data.deserializeData(buf.readByteArray());
				break;
		}
	}
	
	/**
	 * Updates client data on connect
	 */
	public void onConnect(ServerPlayer c) {
		try {
			this.data.loadData();
			this.sendStates();
		} catch (IOException e) {
			LoTAS.LOGGER.warn("Unable to send states to client.", e);
		}
	}
	
	/**
	 * Load save data on initialize
	 */
	@Override
	protected void onServerLoad() {
		this.data.onServerInitialize(this.mcserver);
	}
	
	public interface Task {
		public void run() throws IOException;
	}
}
