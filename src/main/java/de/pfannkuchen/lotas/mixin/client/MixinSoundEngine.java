package de.pfannkuchen.lotas.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.pfannkuchen.lotas.ClientLoTAS;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEngine;

/**
 * This mixin is purely responsible for the hooking up the events in {@link ClientLoTAS}.
 * @author Pancake
 */
@Mixin(SoundEngine.class)
@Environment(EnvType.CLIENT)
public class MixinSoundEngine {

	/**
	 * Triggers an Event in {@link ClientLoTAS#onSoundPlay(SoundInstance)} before the game enters the game loop
	 * @param sound Sound being played
	 * @param ci Callback Info
	 */
	@Inject(method = "play", at = @At("HEAD"))
	public void hookPlayEvent(SoundInstance sound, CallbackInfo ci) {
		ClientLoTAS.instance.onSoundPlay(sound);
	}
	
}
