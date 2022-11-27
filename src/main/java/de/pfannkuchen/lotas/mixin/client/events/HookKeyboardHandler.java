package de.pfannkuchen.lotas.mixin.client.events;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.pfannkuchen.lotas.ClientLoTAS;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;

/**
 * This Mixin hooks up key press events
 * @author Pancake
 */
@Mixin(KeyboardHandler.class)
@Environment(EnvType.CLIENT)
public class HookKeyboardHandler {

	@Shadow
	@Final
	public Minecraft minecraft;

	/**
	 * Triggers an Event in {@link ClientLoTAS#onKeyPressed(Integer)} whenever a screen catches a key event
	 * @param l The Game Window
	 * @param key The Pressed Key
	 * @param j Key Code
	 * @param k Key Action
	 * @param m Mods
	 */
	@Inject(method = "keyPress", at = @At("HEAD"))
	public void onKeyPressed(long l, int key, int j, int k, int m, CallbackInfo ci) {
		Screen screen = this.minecraft.screen;
		if (screen == null || !(screen.getFocused() instanceof EditBox) || !((EditBox) screen.getFocused()).canConsumeInput())
			ClientLoTAS.instance.onKeyPress(-key); // note: control characters shall be negative
	}

	/**
	 * Triggers an Event in {@link ClientLoTAS#onKeyPressed(Integer)} whenever a screen catches a key event
	 * @param l The Game Window
	 * @param key The Pressed Key
	 * @param j Key Code
	 */
	@Inject(method = "charTyped", at = @At("HEAD"))
	public void onCharTyped(long l, int key, int j, CallbackInfo ci) {
		Screen screen = this.minecraft.screen;
		if (screen == null || !(screen.getFocused() instanceof EditBox) || !((EditBox) screen.getFocused()).canConsumeInput())
			ClientLoTAS.instance.onKeyPress(key);
	}

}
