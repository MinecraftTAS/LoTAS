package de.pfannekuchen.lotas.mixin.render.binds;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import de.pfannekuchen.lotas.mods.TickrateChangerMod;


@Mixin(targets = "net/minecraft/client/gui/components/toasts/ToastComponent$ToastInstance")
public class MixinTickrateChangerAchievements {

	//#if MC>=11601
//$$ 		@ModifyVariable(
				//#if MC>=11901
				//#if MC>=12000
//$$ 				method = "Lnet/minecraft/client/gui/components/toasts/ToastComponent$ToastInstance;render(ILnet/minecraft/client/gui/GuiGraphics;)Z",
				//#else
//$$ 				method = "Lnet/minecraft/client/gui/components/toasts/ToastComponent$ToastInstance;render(ILcom/mojang/blaze3d/vertex/PoseStack;)Z",
				//#endif
				//#else
//$$ 				method = "Lnet/minecraft/client/gui/components/toasts/ToastComponent$ToastInstance;render(IILcom/mojang/blaze3d/vertex/PoseStack;)Z",
				//#endif
//$$
//$$ 				at = @At(value = "STORE"), ordinal = 0, 
//$$
				//#if MC>=11901
//$$ 				index = 3)
				//#else
//$$ 				index = 4)
				//#endif
//$$ 		public long modifyAnimationTime(long animationTimer) {
//$$ 			return TickrateChangerMod.getMilliseconds();
//$$ 		}
	//#else
	@ModifyVariable(method = "Lnet/minecraft/client/gui/components/toasts/ToastComponent$ToastInstance;render(II)Z", at = @At(value = "STORE"), ordinal = 0, index = 3)
	public long modifyAnimationTime(long animationTimer) {
		return TickrateChangerMod.getMilliseconds();
	}
	//#endif

}
