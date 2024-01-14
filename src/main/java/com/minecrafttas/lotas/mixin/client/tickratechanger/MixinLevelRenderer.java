package com.minecrafttas.lotas.mixin.client.tickratechanger;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.LevelRenderer;

import static com.minecrafttas.lotas.LoTAS.TICKRATE_CHANGER;

/**
 * This mixin slows down the world border renderer to the tickrate
 * @author Pancake
 */
@Mixin(LevelRenderer.class)
@Environment(EnvType.CLIENT)
public class MixinLevelRenderer {

	/**
	 * Slow down getMillis
	 * @param f Ignored original value
	 * @return Manipulated value
	 */
	@ModifyVariable(method = "renderWorldBounds", at = @At(value = "STORE"), index = 19, ordinal = 3)
	public float injectf3(float f) {
		return TICKRATE_CHANGER.getMilliseconds() % 3000L / 3000.0F;
	}

}
