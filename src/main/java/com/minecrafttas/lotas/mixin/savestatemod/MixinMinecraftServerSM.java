package com.minecrafttas.lotas.mixin.savestatemod;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.minecrafttas.lotas.mods.SavestateMod;
import com.minecrafttas.lotas.mods.SavestateMod.SavestateState;

import net.minecraft.server.MinecraftServer;

@Mixin(MinecraftServer.class)
public class MixinMinecraftServerSM {
	
	@Redirect(method = "tickServer", at = @At(value = "INVOKE", 
			//#1.18.2
//$$			target = "Lnet/minecraft/server/MinecraftServer;saveEverything(ZZZ)Z"))
			//#def
//$$			target = "Lnet/minecraft/server/MinecraftServer;saveAllChunks(ZZZ)Z"))
			//#end
	public boolean redirect_tickServer(MinecraftServer server, boolean bl1, boolean bl2, boolean bl3) {
		if(SavestateMod.state == SavestateState.NONE) {
			return server.saveAllChunks(bl1, bl2, bl3);
		}
		return true;
	}
}
