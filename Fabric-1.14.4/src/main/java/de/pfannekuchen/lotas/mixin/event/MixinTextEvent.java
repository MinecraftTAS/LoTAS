package de.pfannekuchen.lotas.mixin.event;

import java.awt.Color;
import java.time.Duration;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.pfannekuchen.lotas.tickratechanger.TickrateChanger;
import de.pfannekuchen.lotas.tickratechanger.Timer;
import de.pfannekuchen.lotas.utils.ConfigManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.util.Identifier;

@Mixin(InGameHud.class)

public class MixinTextEvent {

    @Inject(at = @At(value = "INVOKE", shift = At.Shift.AFTER, target = "Lnet/minecraft/client/gui/hud/InGameHud;renderStatusEffectOverlay()V"), method = "Lnet/minecraft/client/gui/hud/InGameHud;render(F)V")
    public void injectrender(float tickDelta, CallbackInfo ci) {
    	if (Timer.ticks != -1) {
			DrawableHelper.fill(0, 0, 75, ConfigManager.getBoolean("ui", "hideRTATimer") ? 13 : 24, new Color(0, 0, 0, 175).getRGB());
			Duration dur = Duration.ofMillis(Timer.ticks * 50);
			if (Timer.running) TickrateChanger.rta = Duration.ofMillis(System.currentTimeMillis() - Timer.startTime.toMillis());
			MinecraftClient.getInstance().textRenderer.drawWithShadow(Timer.getDuration(dur), 1, 3, 0xFFFFFFFF);
			if (!ConfigManager.getBoolean("ui", "hideRTATimer")) MinecraftClient.getInstance().textRenderer.drawWithShadow("RTA: " + Timer.getDuration(TickrateChanger.rta), 1, 15, 0xFFFFFFFF);
		} 
		if (ConfigManager.getBoolean("tools", "showTickIndicator") && TickrateChanger.tickrate <= 5F && TickrateChanger.show) {
			MinecraftClient.getInstance().getTextureManager().bindTexture(streaming);
			DrawableHelper.blit(MinecraftClient.getInstance().window.getScaledWidth() - 17, 1, 0, 0, 16, 16, 16, 64);
		}
    }
    
	private Identifier streaming = new Identifier("textures/gui/stream_indicator.png");

}
