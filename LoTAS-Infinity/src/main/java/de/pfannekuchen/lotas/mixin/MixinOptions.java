package de.pfannekuchen.lotas.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.pfannekuchen.lotas.core.utils.KeybindsUtils;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Options;

@Mixin(Options.class)
public class MixinOptions {
	@Mutable
	@Final
	@Shadow
	public KeyMapping[] keyMappings;
	@Inject(at = @At("HEAD"), method = "load()V")
	public void loadHook(CallbackInfo info) {
		keyMappings=KeybindsUtils.registerKeybinds(keyMappings);
	}
}
