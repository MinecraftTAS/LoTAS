package de.pfannekuchen.lotas.mixin.patches;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.pfannekuchen.lotas.mods.SavestateMod;
import de.pfannekuchen.lotas.mods.SavestateMod.State;
import net.minecraft.world.storage.SaveHandler;

@Mixin(SaveHandler.class)
/**
 * On rare occasions, savestating fails due to the level.dat being saved while saving the world creating a phantom file
 * 
 * @author Scribble
 *
 */
public class MixinSaveHandler {
	@Inject(method = "saveWorldInfoWithPlayer", at = @At(value = "INVOKE", target = "Ljava/io/File;exists()Z", ordinal = 0), cancellable = true)
	public void cancelLevelDataCreation(CallbackInfo ci) {
		if(SavestateMod.state == State.LOADSTATING) {
			ci.cancel();
		}
	}

}
