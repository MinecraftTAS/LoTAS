package de.pfannkuchen.lotas.mixin.client.tickratechanger;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import de.pfannkuchen.lotas.mods.TickrateChanger;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.util.Mth;

/**
 * This Mixin levels down the pitch to the tickrate
 * @author Pancake
 */
@Mixin(SoundEngine.class)
@Environment(EnvType.CLIENT)
public class MixinSoundEngine {

	/**
	 * Calculates a new pitch to play based on the tickrate
	 * @param soundInstance Sound to play
	 * @param ci Returnable
	 */
	@Inject(method = "calculatePitch", at = @At(value = "HEAD"), cancellable = true)
	public void redosetPitch(SoundInstance soundInstance, CallbackInfoReturnable<Float> ci) {
		ci.setReturnValue((float) (Mth.clamp(soundInstance.getPitch(), 0.5F, 2.0F) * TickrateChanger.instance.getGamespeed()));
		ci.cancel();
	}

}
