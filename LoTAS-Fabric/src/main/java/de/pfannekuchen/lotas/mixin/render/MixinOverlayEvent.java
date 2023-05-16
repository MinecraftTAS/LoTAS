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

	//#if MC>=11601
	//#if MC>=12000
//$$ 	@Inject(at = @At(value = "INVOKE", shift = At.Shift.AFTER, target = "Lnet/minecraft/client/gui/Gui;renderEffects(Lnet/minecraft/client/gui/GuiGraphics;)V"), method = "render")
//$$ 	public void injectrender(net.minecraft.client.gui.GuiGraphics stack, float tickDelta, CallbackInfo ci) {
	//#else
//$$ 	@Inject(at = @At(value = "INVOKE", shift = At.Shift.AFTER, target = "Lnet/minecraft/client/gui/Gui;renderEffects(Lcom/mojang/blaze3d/vertex/PoseStack;)V"), method = "render")
//$$ 	public void injectrender(com.mojang.blaze3d.vertex.PoseStack stack, float tickDelta, CallbackInfo ci) {
	//#endif
//$$ 		MCVer.stack = stack;
	//#else
	@Inject(at = @At(value = "INVOKE", shift = At.Shift.AFTER, target = "Lnet/minecraft/client/gui/Gui;renderEffects()V"), method = "render")
	public void injectrender(float tickDelta, CallbackInfo ci) {
	//#endif
		LoTASModContainer.hud.drawHud();
		if (ConfigUtils.getBoolean("tools", "showTickIndicator") && TickrateChangerMod.tickrate <= 5F && TickrateChangerMod.show) {
			MCVer.bind(Minecraft.getInstance().getTextureManager(),streaming);
			MCVer.blit(MCVer.getGLWindow().getGuiScaledWidth() - 17, 1, 0, 0, 16, 16, 16, 64);
		}
	}

	private ResourceLocation streaming = new ResourceLocation("textures/gui/stream_indicator.png");

}
