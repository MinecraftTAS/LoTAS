package de.pfannkuchen.lotas.mixin.client.events;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.pfannkuchen.lotas.ClientLoTAS;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;

/**
 * This mixin is purely responsible for the hooking up the events in {@link ClientLoTAS}.
 * @author Pancake
 */
@Mixin(Options.class)
@Environment(EnvType.CLIENT)
public class HookOptions {

	/**
	 * List of Key Mappings that are being registered once loaded
	 */
	@Mutable
	@Final
	@Shadow
	public KeyMapping[] keyMappings;

	/**
	 * Triggers an Event in {@link ClientLoTAS#onShutdown(Minecraft)} before the keybinds are initializes and replaces them with a custom array.
	 * @param ci Callback Info
	 */
	@Inject(method = "load", at = @At("HEAD"))
	public void hookLoadEvent(CallbackInfo ci) {
		this.keyMappings = ClientLoTAS.instance.onKeybindInitialize(this.keyMappings);
	}

}
