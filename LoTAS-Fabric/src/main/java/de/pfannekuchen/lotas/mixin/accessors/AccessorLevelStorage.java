package de.pfannekuchen.lotas.mixin.accessors;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.server.MinecraftServer;

@Mixin(MinecraftServer.class)
public interface AccessorLevelStorage {
	//#if MC>=11601
//$$ 	@org.spongepowered.asm.mixin.gen.Accessor
//$$ 	public net.minecraft.world.level.storage.LevelStorageSource.LevelStorageAccess getStorageSource();
	//#endif
}
