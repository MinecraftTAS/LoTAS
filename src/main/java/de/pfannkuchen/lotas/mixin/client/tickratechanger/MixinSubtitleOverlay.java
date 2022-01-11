package de.pfannkuchen.lotas.mixin.client.tickratechanger;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import de.pfannkuchen.lotas.LoTAS;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.SubtitleOverlay;

/**
 * This Mixin slows down the subtitle overlay to the tickrate
 * @author Scribble
 */
@Mixin(SubtitleOverlay.class)
@Environment(EnvType.CLIENT)
public class MixinSubtitleOverlay {

	/**
	 * Slows down the render speed by multiplying with the Gamespeed
	 * @param threethousand 3000, well
	 * @return Slowed down 3000
	 */
	@ModifyConstant(method = "render", constant = @Constant(longValue = 3000L))
	public long applyTickrate(long threethousand) {
		return (long) (threethousand * (20.0 / LoTAS.tickratechanger.getTickrate()));
	}

	/**
	 * Slows down the render speed by multiplying with the Gamespeed
	 * @param threethousand 3000, well
	 * @return Slowed down 3000
	 */
	@ModifyConstant(method = "render", constant = @Constant(floatValue = 3000F))
	public float applyTickrate2(float threethousand) {
		return (float) (threethousand * (20.0 / LoTAS.tickratechanger.getTickrate()));
	}

}
