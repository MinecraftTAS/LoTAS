package de.pfannkuchen.lotas.mixin.accessors;

import java.util.Map;
import java.util.concurrent.Executor;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.progress.ChunkProgressListener;
import net.minecraft.server.level.progress.ChunkProgressListenerFactory;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.LevelStorageSource;


/**
 * This mixin accessor makes a storage source available
 * @author Pancake
 */
@Mixin(MinecraftServer.class)
public interface AccessorMinecraftServer {

	/**
	 * This Accessor opens the private field containing the storage source for the server
	 * @return Level Storage Access
	 */
	@Accessor
	public LevelStorageSource.LevelStorageAccess getStorageSource();
	
	/**
	 * This Accessor opens the private field containing the executor for the server
	 * @return Executor
	 */
	@Accessor
	public Executor getExecutor();
	
	@Accessor
	public Map<ResourceKey<Level>, ServerLevel> getLevels();
	
	@Accessor("progressListenerFactory")
	public ChunkProgressListenerFactory getFactory();
	
	@Invoker("createLevels")
	public void invokeCreateLevels(ChunkProgressListener listener);
	
}