package de.pfannekuchen.lotas.mixin.render.binds;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import de.pfannekuchen.lotas.mods.TickrateChangerMod;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.util.Mth;

/**
 * Slows down Audio
 * @author ScribbleLP
 */
@Mixin(SoundEngine.class)
public abstract class MixinTickrateChangerAudioPitch {

	@Inject(method = "calculatePitch", at = @At(value = "HEAD"), cancellable = true)
	public void redosetPitch(SoundInstance soundInstance, CallbackInfoReturnable<Float> ci) {
		ci.setReturnValue(Mth.clamp(soundInstance.getPitch(), 0.5F, 2.0F) * (TickrateChangerMod.tickrate / 20F));
		ci.cancel();
	}

}
