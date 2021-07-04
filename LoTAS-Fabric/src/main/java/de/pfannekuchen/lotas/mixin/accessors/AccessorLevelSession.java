package de.pfannekuchen.lotas.mixin.accessors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.server.MinecraftServer;
//#if MC>=11601
//$$ import net.minecraft.world.level.storage.LevelStorage;
//#endif

@Mixin(MinecraftServer.class)
public interface AccessorLevelSession {
	//#if MC>=11601
//$$ 	@Accessor
//$$ 	public LevelStorage.Session getSession();
	//#endif
}
