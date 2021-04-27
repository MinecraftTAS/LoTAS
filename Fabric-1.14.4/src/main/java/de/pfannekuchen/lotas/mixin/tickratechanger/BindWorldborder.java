package de.pfannekuchen.lotas.mixin.tickratechanger;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import de.pfannekuchen.lotas.tickratechanger.TickrateChanger;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.util.Util;

@Mixin(WorldRenderer.class)
public class BindWorldborder {
	
	@ModifyVariable(method = "renderWorldBorder", at = @At(value = "STORE"), index = 20, ordinal = 4)
	public float injectf3(float f) {
		return (getTime() % 3000L) / 3000.0F;
	}
	
	private static long getTime() {
		if(TickrateChanger.tickrate == 0 || TickrateChanger.advanceClient) {
			return TickrateChanger.ticksPassed * 50L;
		}else {
			return (TickrateChanger.ticksPassed * 50L) + ((Util.getMeasuringTimeMs() - TickrateChanger.timeOffset) * ((int)TickrateChanger.tickrate / 20L));
		}
	}
	
	
}
