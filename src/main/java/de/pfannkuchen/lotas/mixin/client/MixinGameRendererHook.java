package de.pfannkuchen.lotas.mixin.client;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.pfannkuchen.lotas.ClientLoTAS;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;

/**
 * This mixin hooks up the screen render event to {@link ClientLoTAS}.
 * @author Pancake
 */
@Mixin(GameRenderer.class)
public class MixinGameRendererHook {

	// Shadow Field seen in GameRenderer.class
	@Shadow @Final
	public Minecraft minecraft;
	
	/**
	 * Triggers an Event in {@link ClientLoTAS#onRenderScreen(Minecraft)} after the gui screens are being rendered.
	 * Only triggers while in a minecraft world
	 * @param ci Callback Info
	 */
	@Inject(method = "render", at = @At("TAIL"))
	public void renderScreenEvent(CallbackInfo ci) {
		if (minecraft.level != null) ClientLoTAS.instance.onRenderScreen(minecraft);
	}
	
}
