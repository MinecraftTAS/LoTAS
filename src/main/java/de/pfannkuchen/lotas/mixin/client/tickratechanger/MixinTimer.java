package de.pfannkuchen.lotas.mixin.client.tickratechanger;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import de.pfannkuchen.lotas.LoTAS;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Timer;

/**
 * This Mixin slows down the integrated Timer making the game run slower
 * @author Pancake
 */
@Mixin(Timer.class)
@Environment(EnvType.CLIENT)
public class MixinTimer {

	@Shadow @Final @Mutable
	public float msPerTick;

	@Inject(method = "advanceTime", at = @At("HEAD"))
	public void onAdvanceTime(CallbackInfoReturnable<Integer> cir) {
		this.msPerTick = (float) (LoTAS.tickadvance.isTickadvanceEnabled() && !LoTAS.tickadvance.shouldTickClient ? Float.MAX_VALUE : LoTAS.tickratechanger.getMsPerTick());
	}

}
