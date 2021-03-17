package de.pfannekuchen.lotas.mixin.events;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.pfannekuchen.lotas.LoTAS;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;

@Mixin(GameRenderer.class)
public class RenderHandEvent {
	
	@Inject(at = @At("HEAD"), method = "renderHand")
	public void injecttrenderHand(CallbackInfo ci) {
		LoTAS.renderEvent(MinecraftClient.getInstance().currentScreen);
	}
	
}
