package de.pfannekuchen.lotas.mixin.accessors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.pfannekuchen.lotas.core.utils.KeybindsUtils;
import net.minecraft.client.gui.GuiTextField;

@Mixin(GuiTextField.class)
public class AccessFocused {

	@Inject(at = @At("HEAD"), method = "setFocused", cancellable = true)
	public void injectsetFocused(boolean focused, CallbackInfo ci) {
		KeybindsUtils.focused = focused;
	}

}
