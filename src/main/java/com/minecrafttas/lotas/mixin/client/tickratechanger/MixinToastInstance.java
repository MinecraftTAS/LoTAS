package com.minecrafttas.lotas.mixin.client.tickratechanger;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.minecrafttas.lotas.mods.TickrateChanger;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/**
 * This mixin slows down the toast in the top right to the tickrate
 * @author Pancake
 */
@Mixin(targets = "net/minecraft/client/gui/components/toasts/ToastComponent$ToastInstance")
@Environment(EnvType.CLIENT)
public class MixinToastInstance {

	/**
	 * Slow down toast timer
	 * @param animationTimer Original value
	 * @return Manipulated value
	 */
	@ModifyVariable(method = "render(IILcom/mojang/blaze3d/vertex/PoseStack;)Z", at = @At(value = "STORE"), ordinal = 0, index = 4)
	public long modifyAnimationTime(long animationTimer) {
		return TickrateChanger.instance.getMilliseconds();
	}

}
