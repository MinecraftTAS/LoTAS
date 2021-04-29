package de.pfannekuchen.lotas.mixin;

import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import de.pfannekuchen.lotas.config.ConfigManager;
import net.minecraft.enchantment.EnchantmentDurability;
import net.minecraft.item.ItemStack;

@Mixin(EnchantmentDurability.class)
public class MixinEnchantmentDurability {

	@Inject(at = @At("HEAD"), method = "negateDamage", cancellable = true)
	private static void damageNO(ItemStack stack, int level, Random rand, CallbackInfoReturnable<Boolean> bool) {
		if (ConfigManager.getBoolean("tools", "noDamageUnbreaking") && level >= 1) {
			bool.setReturnValue(true);
			bool.cancel();
		}
	}
	
}
