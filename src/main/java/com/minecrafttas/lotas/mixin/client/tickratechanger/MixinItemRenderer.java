package com.minecrafttas.lotas.mixin.client.tickratechanger;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.RenderStateShard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

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
	 * @param ignored Ignored original value
	 * @return Manipulated value
	 */
	@ModifyVariable(method = "setupGlintTexturing", at = @At(value = "STORE"), index = 1, ordinal = 0)
	private static long modifyrenderEffect(long value) {
		return TICKRATE_CHANGER.getMilliseconds() * 8L;
	}

}
