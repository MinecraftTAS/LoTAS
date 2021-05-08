package de.pfannekuchen.lotas.mixin.patches;

import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import de.pfannekuchen.lotas.core.utils.ConfigUtils;
import net.minecraft.enchantment.EnchantmentDurability;
import net.minecraft.item.ItemStack;

@Mixin(EnchantmentDurability.class)
public class MixinPatchDurabilityEnch {

	@Inject(at = @At("HEAD"), method = "negateDamage", cancellable = true)
	private static void disableDamage(ItemStack stack, int level, Random rand, CallbackInfoReturnable<Boolean> bool) {
		if (ConfigUtils.getBoolean("tools", "noDamageUnbreaking") && level >= 1) {
			bool.setReturnValue(true);
			bool.cancel();
		}
	}
	
	// TODO: Patch Worldborder, but correctly
	
}
