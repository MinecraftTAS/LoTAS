package de.pfannekuchen.lotas.mixin.tickratechanger;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import de.pfannekuchen.lotas.tickratechanger.TickrateChanger;
import net.minecraft.client.renderer.entity.RenderItem;

@Mixin(RenderItem.class)
public abstract class BindRenderItem {
	
	@ModifyVariable(method = "renderEffect", at = @At("STORE"), index = 2, ordinal = 0, remap = false)
	public float modifyrenderEffect1(float f) {
		return (TickrateChanger.getMilliseconds() % 3000L) / 3000.0F / 8F;
	}
	
	@ModifyVariable(method = "renderEffect", at = @At("STORE"), index = 3, ordinal = 1, remap = false)
	public float modifyrenderEffect2(float f) {
		return (TickrateChanger.getMilliseconds() % 4873L) / 4873.0F / 8F;
	}
	
}
