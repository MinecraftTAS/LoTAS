package com.minecrafttas.lotas.mixin.client.tickratechanger;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.minecrafttas.lotas.mods.TickrateChanger;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.LevelRenderer;

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
		return TickrateChanger.instance.getMilliseconds() % 3000L / 3000.0F;
	}

}
