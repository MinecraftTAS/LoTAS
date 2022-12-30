package com.minecrafttas.lotas.mixin.client.tickratechanger;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.minecrafttas.lotas.mods.TickrateChanger;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.RenderStateShard;

/**
 * This Mixin slows down the Foil renderer to the tickrate
 * @author Pancake
 */
@Mixin(RenderStateShard.class)
@Environment(EnvType.CLIENT)
public class MixinItemRenderer {

	/**
	 * Slows down the getMillis call in the RenderStateShard class
	 * @param ignored Ignored original value
	 * @return Manipulated value
	 */
	@ModifyVariable(method = "setupGlintTexturing", at = @At(value = "STORE"), index = 1, ordinal = 0)
	private static long modifyrenderEffect(long ignored) {
		return TickrateChanger.instance.getMilliseconds() * 8L;
	}

}
