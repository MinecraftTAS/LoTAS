package de.pfannekuchen.lotas.mixin.render;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.pfannekuchen.lotas.core.LoTASModContainer;
import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.core.utils.ConfigUtils;
import de.pfannekuchen.lotas.mods.TickrateChangerMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.resources.ResourceLocation;

/**
 * Renders an Overlay over the screen
 * @author Pancake
 */
@Mixin(Gui.class)
public class MixinOverlayEvent {

	//#if MC>=11600
//$$ 	@Inject(at = @At(value = "INVOKE", shift = At.Shift.AFTER, target = "Lnet/minecraft/client/gui/Gui;renderEffects(Lcom/mojang/blaze3d/vertex/PoseStack;)V"), method = "render")
//$$ 	public void injectrender(com.mojang.blaze3d.vertex.PoseStack stack, float tickDelta, CallbackInfo ci) {
//$$ 		MCVer.stack = stack;
	//#else
	@Inject(at = @At(value = "INVOKE", shift = At.Shift.AFTER, target = "Lnet/minecraft/client/gui/Gui;renderEffects()V"), method = "render")
	public void injectrender(float tickDelta, CallbackInfo ci) {
	//#endif
		LoTASModContainer.hud.drawHud();
		if (ConfigUtils.getBoolean("tools", "showTickIndicator") && TickrateChangerMod.tickrate <= 5F && TickrateChangerMod.show) {
			Minecraft.getInstance().getTextureManager().bind(streaming);
			MCVer.blit(Minecraft.getInstance().window.getGuiScaledWidth() - 17, 1, 0, 0, 16, 16, 16, 64);
		}
	}

	private ResourceLocation streaming = new ResourceLocation("textures/gui/stream_indicator.png");

}
