package de.pfannekuchen.lotas.mixin.render.binds;

import org.spongepowered.asm.mixin.Mixin;

@Mixin(targets = "net/minecraft/client/toast/ToastManager$Entry")
public abstract class MixinTickrateChangerAchievement {
	//#if MC>=11601
//$$ 	@ModifyVariable(method = "Lnet/minecraft/client/toast/ToastManager;draw(IILnet/minecraft/client/util/math/MatrixStack;)Z", at = @At(value = "STORE"), ordinal = 0, index = 4)
//$$ 	public long modifyAnimationTime(long animationTimer) {
//$$ 		return TickrateChangerMod.getMilliseconds();
//$$ 	}
	//#else
	@ModifyVariable(method = "Lnet/minecraft/client/toast/ToastManager;draw(II)Z", at = @At(value = "STORE"), ordinal = 0, index = 3)
	public long modifyAnimationTime(long animationTimer) {
		return TickrateChangerMod.getMilliseconds();
	}
	//#endif
}
