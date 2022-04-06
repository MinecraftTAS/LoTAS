package de.pfannkuchen.lotas.mixin.accessors;

import java.util.concurrent.Executor;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.server.MinecraftServer;
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
	
}