package de.pfannekuchen.lotas.mixin.render.binds;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import de.pfannekuchen.lotas.mods.TickrateChangerMod;

//#if MC<=11404
import  net.minecraft.client.render.item.ItemRenderer;
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
//#else
//$$ import net.minecraft.client.render.RenderPhase;
//$$ @Mixin(RenderPhase.class)
//$$ public abstract class MixinTickrateChangerEnchantmentGlimm {
//$$ 		@ModifyVariable(method = "setupGlintTexturing", at = @At(value = "STORE"), index = 1, ordinal = 0)
//$$ 		private static long modifyrenderEffect(long ignored) {
//$$ 			return TickrateChangerMod.getMilliseconds()*8L;
//$$ 		}
//$$ }
//#endif