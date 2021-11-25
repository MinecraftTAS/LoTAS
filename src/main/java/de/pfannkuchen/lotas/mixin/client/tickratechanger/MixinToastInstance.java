package de.pfannkuchen.lotas.mixin.client.tickratechanger;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import de.pfannkuchen.lotas.LoTAS;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/**
 * This Mixin slows down the toast in the top right to the tickrate
 * @author Pancake
 */
@Mixin(targets = "net/minecraft/client/gui/components/toasts/ToastComponent$ToastInstance")
@Environment(EnvType.CLIENT)
public class MixinToastInstance {

	/**
	 * Slows down the Toaster™ Timer
	 * @param animationTimer Original value
	 * @return Manipulated value
	 */
 	@ModifyVariable(method = "Lnet/minecraft/client/gui/components/toasts/ToastComponent$ToastInstance;render(IILcom/mojang/blaze3d/vertex/PoseStack;)Z", at = @At(value = "STORE"), ordinal = 0, index = 4)
 	public long modifyAnimationTime(long animationTimer) {
 		return LoTAS.tickratechanger.getMilliseconds();
 	}
	
}
