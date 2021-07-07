package de.pfannekuchen.lotas.mixin.patches;

import java.util.Random;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.DigDurabilityEnchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import de.pfannekuchen.lotas.core.utils.ConfigUtils;

@Mixin(DigDurabilityEnchantment.class)
public class MixinUnbreakingPatch {

	@Inject(at = @At("HEAD"), method = "shouldIgnoreDurabilityDrop", cancellable = true)
	private static void damageNO(ItemStack stack, int level, Random rand, CallbackInfoReturnable<Boolean> bool) {
		if (ConfigUtils.getBoolean("tools", "noDamageUnbreaking") && level >= 1) {
			bool.setReturnValue(true);
			bool.cancel();
		}
	}

}
