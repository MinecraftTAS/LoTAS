package de.pfannekuchen.lotas.mixin.accessors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.server.MinecraftServer;

@Mixin(MinecraftServer.class)
public interface AccessorLevelStorage {
	//#if MC>=11600
//$$ 	@Accessor
//$$ 	public net.minecraft.world.level.storage.LevelStorageSource.LevelStorageAccess getStorageSource();
	//#endif
}
