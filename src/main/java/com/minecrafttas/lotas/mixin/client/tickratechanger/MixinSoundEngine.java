package com.minecrafttas.lotas.mixin.client.tickratechanger;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.util.Mth;

import static com.minecrafttas.lotas.LoTAS.TICKRATE_CHANGER;

/**
 * This mixin adjusts the pitch of audio to the tickrate
 *
 * @author Pancake
 */
@Mixin(SoundEngine.class)
@Environment(EnvType.CLIENT)
public class MixinSoundEngine {

	/**
	 * Calculate new pitch to play based on tickrate
	 *
	 * @param soundInstance Sound to play
	 * @param ci Returnable
	 */
	@Inject(method = "calculatePitch", at = @At(value = "HEAD"), cancellable = true)
	public void redosetPitch(SoundInstance soundInstance, CallbackInfoReturnable<Float> ci) {
		ci.setReturnValue((float) (Mth.clamp(soundInstance.getPitch(), 0.5F, 2.0F) * TICKRATE_CHANGER.getGamespeed()));
		ci.cancel();
	}

}
