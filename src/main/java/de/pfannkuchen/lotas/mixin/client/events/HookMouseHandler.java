package de.pfannkuchen.lotas.mixin.client.events;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.pfannkuchen.lotas.ClientLoTAS;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MouseHandler;

/**
 * This Mixin hooks up a scroll and possibly mouse click event
 * @author Pancake
 */
@Mixin(MouseHandler.class)
@Environment(EnvType.CLIENT)
public class HookMouseHandler {

	/**
	 * Triggers an event in {@link ClientLoTAS#onKeyPressed(Integer)} whenever a screen catches a key event
	 * @param l The game window
	 * @param d The action
	 * @param e The scroll amount
	 */
	@Inject(method = "onScroll", at = @At("HEAD"))
	private void onScroll(long l, double d, double e, CallbackInfo ci) {
		ClientLoTAS.instance.onMouseScroll(e);
	}
	
}