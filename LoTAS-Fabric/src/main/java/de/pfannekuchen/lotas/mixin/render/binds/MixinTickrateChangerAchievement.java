package de.pfannekuchen.lotas.mixin.render.binds;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import de.pfannekuchen.lotas.mods.TickrateChangerMod;

@Mixin(targets = "net/minecraft/client/toast/ToastManager$Entry")
public abstract class MixinTickrateChangerAchievement {

	//#if MC<=11502
	@ModifyVariable(method = "draw", at = @At(value = "STORE", ordinal = 0))
	public long modifyAnimationTime(long animationTimer) {
		return TickrateChangerMod.getMilliseconds();
	}
	//#endif

}
