package de.pfannekuchen.lotas.mixin.tickratechanger;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.pfannekuchen.lotas.tickratechanger.TickrateChanger;
import paulscode.sound.Source;

@Mixin(Source.class)
public abstract class BindSource {
	
	/*
	 * This File Pitches the Audio to the Tickrate
	 */
	
	@Shadow(remap = false)
	public float pitch;
	
	@Inject(method = "setPitch", at = @At(value = "RETURN"), remap = false)
	public void redosetPitch(float value, CallbackInfo ci) {
		pitch = value * (TickrateChanger.tickrate / 20F);
	}
	
}
