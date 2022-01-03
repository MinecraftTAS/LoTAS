package de.pfannkuchen.lotas.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.pfannkuchen.lotas.ClientLoTAS;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.KeyboardHandler;

/**
 * This Mixin hooks up a key press event
 * @author Pancake
 */
@Mixin(KeyboardHandler.class)
@Environment(EnvType.CLIENT)
public class MixinKeyboardHandler {

	/**
	 * Triggers an Event in {@link ClientLoTAS#onKeyPressed(Integer)} whenever a screen catches a key event
	 * FIXME: figure out all parameters
	 */
	@Inject(method = "keyPress", at = @At("HEAD"))
	public void onKeyPressed(long l, int key, int j, int k, int m, CallbackInfo ci) {
		ClientLoTAS.instance.onKeyPress(-key); // note: control characters shall be negative
	}
	
	/**
	 * Triggers an Event in {@link ClientLoTAS#onKeyPressed(Integer)} whenever a screen catches a key event
	 * FIXME: figure out all parameters
	 */
	@Inject(method = "charTyped", at = @At("HEAD"))
	public void onCharTyped(long l, int key, int j, CallbackInfo ci) {
		ClientLoTAS.instance.onKeyPress(key);
	}
	
}
