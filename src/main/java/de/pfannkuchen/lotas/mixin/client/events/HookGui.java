package de.pfannkuchen.lotas.mixin.client.events;

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
import net.minecraft.client.renderer.GameRenderer;

/**
 * This mixin hooks up the screen render event to {@link ClientLoTAS}.
 * @author Pancake
 */
@Mixin(GameRenderer.class)
@Environment(EnvType.CLIENT)
public class HookGui {

	// Shadow Field seen in GameRenderer.class
	@Shadow @Final
	public Minecraft minecraft;

	/**
	 * Triggers an Event in {@link ClientLoTAS#onRenderScreen(Minecraft)} after the gui screens are being rendered.
	 * Only triggers while in a minecraft world
	 * @param in Original Screen
	 * @param stack Pose Stack
	 * @param i Width
	 * @param j Height
	 * @param f Partial Ticks
	 */
	/*@Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;render(Lcom/mojang/blaze3d/vertex/PoseStack;IIF)V"))
	public void renderScreenEvent(Screen in, PoseStack stack, int i, int j, float f) {
		in.render(stack, i, j, f);
		ClientLoTAS.instance.onRenderScreen(stack, this.minecraft);
	}*/

	@Inject(method = "render", at = @At(value = "TAIL"))
	public void renderScreenEvent(CallbackInfo ci) {
		ClientLoTAS.instance.onRenderScreen(new PoseStack(), this.minecraft);
	}
	
}
