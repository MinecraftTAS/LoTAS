package de.pfannekuchen.lotas.mixin.render.binds;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.pfannekuchen.lotas.mods.TickrateChangerMod;
import paulscode.sound.Source;

@Mixin(Source.class)
public abstract class MixinTickrateChangerAudioPitch {
	
	@Shadow(remap = false)
	public float pitch;
	
	@Inject(method = "setPitch", at = @At(value = "RETURN"), remap = false)
	public void redosetPitch(float value, CallbackInfo ci) {
		pitch = value * (TickrateChangerMod.tickrate / 20F);
	}
	
}
