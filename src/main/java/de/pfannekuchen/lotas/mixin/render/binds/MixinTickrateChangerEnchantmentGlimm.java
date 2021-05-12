package de.pfannekuchen.lotas.mixin.render.binds;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import de.pfannekuchen.lotas.mods.TickrateChangerMod;
import net.minecraft.client.renderer.RenderItem;

@Mixin(RenderItem.class)
public abstract class MixinTickrateChangerEnchantmentGlimm {
	
	@ModifyVariable(method = "renderEffect", at = @At("STORE"), index = 2, ordinal = 0)
	public float modifyrenderEffect1(float f) {
		return (TickrateChangerMod.getMilliseconds() % 3000L) / 3000.0F / 8F;
	}
	
	@ModifyVariable(method = "renderEffect", at = @At("STORE"), index = 3, ordinal = 1)
	public float modifyrenderEffect2(float f) {
		return (TickrateChangerMod.getMilliseconds() % 4873L) / 4873.0F / 8F;
	}
	
}
