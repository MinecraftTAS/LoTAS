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
//#if MC>=11601
//$$ import net.minecraft.client.util.math.MatrixStack;
//#endif
import net.minecraft.util.Identifier;

@Mixin(InGameHud.class)
public class MixinOverlayEvent {

    @Inject(at = @At(value = "INVOKE", shift = At.Shift.AFTER, target = "Lnet/minecraft/client/gui/hud/InGameHud;renderStatusEffectOverlay()V"), method = "render")
    //#if MC>=11601
    //$$ public void injectrender(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
    //#else
    public void injectrender(float tickDelta, CallbackInfo ci) {
    //#endif
    	if (Timer.ticks != -1) {
    	    //#if MC>=11601
    //$$ 	    DrawableHelper.fill(matrices, 0, 0, 75, ConfigUtils.getBoolean("ui", "hideRTATimer") ? 13 : 24, new Color(0, 0, 0, 175).getRGB());
    	    //#else
    	    DrawableHelper.fill(0, 0, 75, ConfigUtils.getBoolean("ui", "hideRTATimer") ? 13 : 24, new Color(0, 0, 0, 175).getRGB());
    	    //#endif
			Duration dur = Duration.ofMillis(Timer.ticks * 50);
			if (Timer.running) TickrateChangerMod.rta = Duration.ofMillis(System.currentTimeMillis() - Timer.startTime.toMillis());
			//#if MC>=11601
//$$ 			MinecraftClient.getInstance().textRenderer.drawWithShadow(matrices, Timer.getDuration(dur), 1, 3, 0xFFFFFFFF);
//$$ 		    if (!ConfigUtils.getBoolean("ui", "hideRTATimer")) MinecraftClient.getInstance().textRenderer.drawWithShadow(matrices, "RTA: " + Timer.getDuration(TickrateChangerMod.rta), 1, 15, 0xFFFFFFFF);
			//#else
            MinecraftClient.getInstance().textRenderer.drawWithShadow(Timer.getDuration(dur), 1, 3, 0xFFFFFFFF);
		    if (!ConfigUtils.getBoolean("ui", "hideRTATimer")) MinecraftClient.getInstance().textRenderer.drawWithShadow("RTA: " + Timer.getDuration(TickrateChangerMod.rta), 1, 15, 0xFFFFFFFF);
            //#endif
		} 
		if (ConfigUtils.getBoolean("tools", "showTickIndicator") && TickrateChangerMod.tickrate <= 5F && TickrateChangerMod.show) {
			MinecraftClient.getInstance().getTextureManager().bindTexture(streaming);
			//#if MC>=11601
//$$ 			DrawableHelper.drawTexture(matrices, MinecraftClient.getInstance().window.getScaledWidth() - 17, 1, 0, 0, 16, 16, 16, 64);
            //#else
            DrawableHelper.blit(MinecraftClient.getInstance().window.getScaledWidth() - 17, 1, 0, 0, 16, 16, 16, 64);
            //#endif
		}
		if (ConfigUtils.getBoolean("tools", "showSpeedometer")) {
		    //#if MC>=11601
		    //$$ double distTraveledLastTickX = MinecraftClient.getInstance().player.getX() - MinecraftClient.getInstance().player.prevX;
            //$$ double distTraveledLastTickZ = MinecraftClient.getInstance().player.getZ() - MinecraftClient.getInstance().player.prevZ;
            //#else
            double distTraveledLastTickX = MinecraftClient.getInstance().player.x - MinecraftClient.getInstance().player.prevX;
            double distTraveledLastTickZ = MinecraftClient.getInstance().player.z - MinecraftClient.getInstance().player.prevZ;
            //#endif
			String message = String.format("%.2f", MCVer.sqrt((distTraveledLastTickX * distTraveledLastTickX + distTraveledLastTickZ * distTraveledLastTickZ)) / 0.05F) + " blocks/sec";
			//#if MC>=11601
//$$             int width = MinecraftClient.getInstance().textRenderer.getWidth(message);
//$$             DrawableHelper.fill(matrices, 4, 4, 4 + width + 2 * 2, 4 + MinecraftClient.getInstance().textRenderer.fontHeight + 2 + 2 - 1, 0xAA000000);
//$$             MinecraftClient.getInstance().textRenderer.drawWithShadow(matrices, message, 6, 6, 14737632);
            //#else
			int width = MinecraftClient.getInstance().textRenderer.getStringWidth(message);
            DrawableHelper.fill(4, 4, 4 + width + 2 * 2, 4 + MinecraftClient.getInstance().textRenderer.fontHeight + 2 + 2 - 1, 0xAA000000);
            MinecraftClient.getInstance().textRenderer.drawWithShadow(message, 6, 6, 14737632);
            //#endif
		}
    }
    
	private Identifier streaming = new Identifier("textures/gui/stream_indicator.png");
	
}
