package de.pfannekuchen.lotas.mixin.render;

import java.awt.Color;
import java.time.Duration;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.core.utils.ConfigUtils;
import de.pfannekuchen.lotas.core.utils.EventUtils.Timer;
import de.pfannekuchen.lotas.mods.TickrateChangerMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.util.Identifier;

@Mixin(InGameHud.class)
public class MixinOverlayEvent {

    @Inject(at = @At(value = "INVOKE", shift = At.Shift.AFTER, target = "Lnet/minecraft/client/gui/hud/InGameHud;renderStatusEffectOverlay()V"), method = "Lnet/minecraft/client/gui/hud/InGameHud;render(F)V")
    public void injectrender(float tickDelta, CallbackInfo ci) {
    	if (Timer.ticks != -1) {
			DrawableHelper.fill(0, 0, 75, ConfigUtils.getBoolean("ui", "hideRTATimer") ? 13 : 24, new Color(0, 0, 0, 175).getRGB());
			Duration dur = Duration.ofMillis(Timer.ticks * 50);
			if (Timer.running) TickrateChangerMod.rta = Duration.ofMillis(System.currentTimeMillis() - Timer.startTime.toMillis());
			MinecraftClient.getInstance().textRenderer.drawWithShadow(Timer.getDuration(dur), 1, 3, 0xFFFFFFFF);
			if (!ConfigUtils.getBoolean("ui", "hideRTATimer")) MinecraftClient.getInstance().textRenderer.drawWithShadow("RTA: " + Timer.getDuration(TickrateChangerMod.rta), 1, 15, 0xFFFFFFFF);
		} 
		if (ConfigUtils.getBoolean("tools", "showTickIndicator") && TickrateChangerMod.tickrate <= 5F && TickrateChangerMod.show) {
			MinecraftClient.getInstance().getTextureManager().bindTexture(streaming);
			DrawableHelper.blit(MinecraftClient.getInstance().window.getScaledWidth() - 17, 1, 0, 0, 16, 16, 16, 64);
		}
		if (ConfigUtils.getBoolean("tools", "showSpeedometer")) {
			double distTraveledLastTickX = MinecraftClient.getInstance().player.x - MinecraftClient.getInstance().player.prevX;
			double distTraveledLastTickZ = MinecraftClient.getInstance().player.z - MinecraftClient.getInstance().player.prevZ;
			
			String message = String.format("%.2f", MCVer.sqrt((distTraveledLastTickX * distTraveledLastTickX + distTraveledLastTickZ * distTraveledLastTickZ)) / 0.05F) + " blocks/sec";
			int width = MinecraftClient.getInstance().textRenderer.getStringWidth(message);
			DrawableHelper.fill(4, 4, 4 + width + 2 * 2, 4 + MinecraftClient.getInstance().textRenderer.fontHeight + 2 + 2 - 1, 0xAA000000);
			MinecraftClient.getInstance().textRenderer.drawWithShadow(message, 6, 6, 14737632);
		}
    }
    
	private Identifier streaming = new Identifier("textures/gui/stream_indicator.png");
	
}
