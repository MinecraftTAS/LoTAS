package com.minecrafttas.lotas.mixin.client.tickratechanger;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.RenderStateShard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static com.minecrafttas.lotas.LoTAS.TICKRATE_CHANGER;

/**
 * This mixin slows down the Foil renderer to the tickrate
 * @author Pancake
 */
@Mixin(RenderStateShard.class)
@Environment(EnvType.CLIENT)
public class MixinItemRenderer {

	/**
	 * Slow down the getMillis
	 * @return Manipulated value
	 */
	@Redirect(method = "setupGlintTexturing", at = @At(value = "INVOKE", target = "Lnet/minecraft/Util;getMillis()J"))
	private static long modifyrenderEffect() {
		return TICKRATE_CHANGER.getMilliseconds();
	}

}
