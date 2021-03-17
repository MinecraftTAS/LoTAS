package de.pfannekuchen.lotas.mixin.tickratechanger;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import de.pfannekuchen.lotas.tickratechanger.TickrateChanger;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.util.Util;

@Mixin(ItemRenderer.class)
public abstract class BindRenderItem {
	
	@ModifyVariable(method = "renderGlint", at = @At("STORE"), index = 2, ordinal = 0)
	private static float modifyrenderEffect1(float f) {
		return (getTime() % 3000L) / 3000.0F / 8F;
	}
	
	@ModifyVariable(method = "renderGlint", at = @At("STORE"), index = 3, ordinal = 1)
	private static float modifyrenderEffect2(float f) {
		return (getTime() % 4873L) / 4873.0F / 8F;
	}
	
	private static long getTime() {
		if(TickrateChanger.tickrate == 0 || TickrateChanger.advanceClient) {
			return TickrateChanger.ticksPassed * 50L;
		}else {
			return (TickrateChanger.ticksPassed * 50L) + ((Util.getMeasuringTimeMs() - TickrateChanger.timeOffset) * ((int)TickrateChanger.tickrate / 20L));
		}
	}
	
}
