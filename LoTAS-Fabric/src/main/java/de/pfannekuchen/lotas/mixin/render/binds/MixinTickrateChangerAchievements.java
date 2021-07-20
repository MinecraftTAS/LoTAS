package de.pfannekuchen.lotas.mixin.render.binds;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import de.pfannekuchen.lotas.mods.TickrateChangerMod;

@Mixin(targets = "net/minecraft/client/gui/components/toasts/ToastComponent$ToastInstance")
public class MixinTickrateChangerAchievements {

	//#if MC>=11601
//$$ 		@ModifyVariable(method = "render", at = @At(value = "STORE"), ordinal = 0, index = 4)
//$$ 		public long modifyAnimationTime(long animationTimer) {
//$$ 			return TickrateChangerMod.getMilliseconds();
//$$ 		}
	//#else
	@ModifyVariable(method = "render", at = @At(value = "STORE"), ordinal = 0, index = 3)
	public long modifyAnimationTime(long animationTimer) {
		return TickrateChangerMod.getMilliseconds();
	}
	//#endif

}
