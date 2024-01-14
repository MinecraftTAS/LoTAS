package com.minecrafttas.lotas.mixin.client.tickratechanger;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static com.minecrafttas.lotas.LoTAS.TICKRATE_CHANGER;

/**
 * This mixin slows down the toast in the top right to the tickrate
 *
 * @author Pancake
 */
@Mixin(targets = "net/minecraft/client/gui/components/toasts/ToastComponent$ToastInstance")
@Environment(EnvType.CLIENT)
public class MixinToastInstance {

	/**
	 * Slow down toast timer
	 *
	 * @return Manipulated value
	 */
	@Redirect(method = "render(IILcom/mojang/blaze3d/vertex/PoseStack;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/Util;getMillis()J"))
	public long modifyAnimationTime() {
		return TICKRATE_CHANGER.getMilliseconds();
	}

}
