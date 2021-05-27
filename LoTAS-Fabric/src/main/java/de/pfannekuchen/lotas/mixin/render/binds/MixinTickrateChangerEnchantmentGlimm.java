package de.pfannekuchen.lotas.mixin.render.binds;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import de.pfannekuchen.lotas.mods.TickrateChangerMod;
import net.minecraft.client.render.item.ItemRenderer;

@Mixin(ItemRenderer.class)
public abstract class MixinTickrateChangerEnchantmentGlimm {
	
	@ModifyVariable(method = "renderGlint", at = @At("STORE"), index = 2, ordinal = 0)
	private static float modifyrenderEffect1(float f) {
		return (TickrateChangerMod.getMilliseconds() % 3000L) / 3000.0F / 8F;
	}
	
	@ModifyVariable(method = "renderGlint", at = @At("STORE"), index = 3, ordinal = 1)
	private static float modifyrenderEffect2(float f) {
		return (TickrateChangerMod.getMilliseconds() % 4873L) / 4873.0F / 8F;
	}
	
}
