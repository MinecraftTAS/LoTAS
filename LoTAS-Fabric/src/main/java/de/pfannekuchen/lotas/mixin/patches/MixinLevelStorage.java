package de.pfannekuchen.lotas.mixin.patches;

//#if MC<11601
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.pfannekuchen.lotas.mods.SavestateMod;
import de.pfannekuchen.lotas.mods.SavestateMod.State;

@Mixin(net.minecraft.world.level.storage.LevelStorage.class)
public class MixinLevelStorage {
	@Inject(method = "saveLevelData", at = @At(value = "INVOKE", target = "Ljava/io/File;exists()Z", ordinal = 0), cancellable = true)
	public void cancelLevelDataCreation(CallbackInfo ci) {
		if (SavestateMod.state == State.LOADSTATING) {
			ci.cancel();
		}
	}
}
//#endif

//@Mixin(net.minecraft.world.level.storage.LevelStorageSource.LevelStorageAccess.class)
//@Inject(method = "Lnet/minecraft/world/level/storage/LevelStorageSource$LevelStorageAccess;saveDataTag(Lnet/minecraft/core/RegistryAccess;)V", at = @At(value = "INVOKE", target = "Ljava/io/File;exists()Z", ordinal = 0), cancellable = true)
//public void cancelLevelDataCreation(CallbackInfo ci) {
//	if(SavestateMod.isLoading) {
//		ci.cancel();
//	}
//}