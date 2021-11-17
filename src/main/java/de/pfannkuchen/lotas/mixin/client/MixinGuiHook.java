package de.pfannkuchen.lotas.mixin.client;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;

import de.pfannkuchen.lotas.ClientLoTAS;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

/**
 * This mixin hooks up the screen render event to {@link ClientLoTAS}.
 * @author Pancake
 */
@Mixin(Gui.class)
@Environment(EnvType.CLIENT)
public class MixinGuiHook {

	// Shadow Field seen in Gui.class
	@Shadow @Final
	public Minecraft minecraft;
	
	/**
	 * Triggers an Event in {@link ClientLoTAS#onRenderScreen(Minecraft)} after the gui screens are being rendered.
	 * Only triggers while in a minecraft world
	 * @param ci Callback Info
	 */
	@Inject(method = "render", at = @At(value = "INVOKE", shift = At.Shift.AFTER, target = "Lnet/minecraft/client/gui/Gui;renderEffects(Lcom/mojang/blaze3d/vertex/PoseStack;)V"))
	public void renderScreenEvent(PoseStack stack, float f, CallbackInfo ci) {
		if (minecraft.level != null) ClientLoTAS.instance.onRenderScreen(stack, minecraft);
	}
	
}
