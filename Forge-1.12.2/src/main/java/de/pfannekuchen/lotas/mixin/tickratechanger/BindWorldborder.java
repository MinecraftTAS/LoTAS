package de.pfannekuchen.lotas.mixin.tickratechanger;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import de.pfannekuchen.lotas.tickratechanger.TickrateChanger;
import net.minecraft.client.renderer.RenderGlobal;

@Mixin(RenderGlobal.class)
public class BindWorldborder {
	
	@ModifyVariable(method = "renderWorldBorder", at = @At(value = "STORE"), index = 20, ordinal = 4)
	public float injectf3(float f) {
		return (getTime() % 3000L) / 3000.0F;
	}
	
	public long getTime() {
		return TickrateChanger.tickrate == 0 || TickrateChanger.advanceClient ?
				(TickrateChanger.ticksPassed * 50L) :
				(TickrateChanger.ticksPassed * 50L) + ((System.currentTimeMillis() - TickrateChanger.timeOffset) * ((int)TickrateChanger.tickrate / 20L));
	}
	
	
}
