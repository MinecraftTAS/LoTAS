package com.minecrafttas.lotas.mixin.client.tickratechanger;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Timer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.minecrafttas.lotas.LoTAS.TICKRATE_CHANGER;
import static com.minecrafttas.lotas.LoTAS.TICK_ADVANCE;

/**
 * This mixin slows down the integrated Timer making the game run slower
 *
 * @author Pancake
 */
@Mixin(Timer.class)
@Environment(EnvType.CLIENT)
public class MixinTimer {

	@Shadow @Final @Mutable
	private float msPerTick;

	/**
	 * Slow down the timer
	 *
	 * @param cir Returnable
	 */
	@Inject(method = "advanceTime", at = @At("HEAD"))
	public void onAdvanceTime(CallbackInfoReturnable<Integer> cir) {
		this.msPerTick = (float) (TICK_ADVANCE.isTickadvance() && !TICK_ADVANCE.shouldTickClient ? Float.MAX_VALUE : TICKRATE_CHANGER.getMsPerTick());
	}

}
