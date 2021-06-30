package de.pfannekuchen.lotas.mixin.render;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;

//#if MC<=11002
//$$ @Mixin(ItemPotion.class)
//$$ public class MixinItemPotion {
//$$ 	@Inject(method="hasEffect", at = @At("HEAD"), cancellable = true)
//$$ 	public void cancelEffect(ItemStack stack, CallbackInfoReturnable<Boolean> ci) {
//$$ 		if(stack!=null&&stack.getTagCompound()!=null) {
//$$ 			if(stack.getTagCompound().getBoolean("enchoff")) {
//$$ 				ci.setReturnValue(false);
//$$ 				ci.cancel();
//$$ 			}
//$$ 		}
//$$ 	}
//$$ }
//#endif