package com.minecrafttas.lotas.mixin.client.tickratechanger;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.minecrafttas.lotas.mods.TickrateChanger;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

//# 1.15.2
//$$import net.minecraft.client.renderer.RenderStateShard;
//$$
//$$/**
//$$ * This Mixin slows down the Foil renderer to the tickrate
//$$ * @author Pancake
//$$ */
//$$@Mixin(RenderStateShard.class)
//$$@Environment(EnvType.CLIENT)
//$$public class MixinItemRenderer {
//$$
//$$	/**
//$$	 * Slows down the getMillis call in the RenderStateShard class
//$$	 * @param ignored Ignored original value
//$$	 * @return Manipulated value
//$$	 */
//$$	@ModifyVariable(method = "setupGlintTexturing", at = @At(value = "STORE"), index = 1, ordinal = 0)
//$$	private static long modifyrenderEffect(long ignored) {
//$$		return TickrateChanger.instance.getMilliseconds() * 8L;
//$$	}
//$$
//$$}
//# def
//$$import net.minecraft.client.renderer.entity.ItemRenderer;
//$$
//$$/**
//$$ * This Mixin slows down the foil item layer to the tickrate
//$$ * @author Pancake
//$$ */
//$$@Mixin(ItemRenderer.class)
//$$@Environment(EnvType.CLIENT)
//$$public class MixinItemRenderer {
//$$
//$$	/**
//$$	 * Slows down the getMillis call in the ItemRenderer class
//$$	 * @param ignored Ignored original value
//$$	 * @return Manipulated value
//$$	 */
//$$	@ModifyVariable(method = "renderFoilLayer", at = @At("STORE"), index = 2, ordinal = 0)
//$$	private static float modifyrenderEffect1(float f) {
//$$		return (TickrateChanger.instance.getMilliseconds() % 3000L) / 3000.0F / 8F;
//$$	}
//$$
//$$	/**
//$$	 * Slows down the getMillis call in the ItemRenderer class
//$$	 * @param ignored Ignored original value
//$$	 * @return Manipulated value
//$$	 */
//$$	@ModifyVariable(method = "renderFoilLayer", at = @At("STORE"), index = 3, ordinal = 1)
//$$	private static float modifyrenderEffect2(float f) {
//$$		return (TickrateChanger.instance.getMilliseconds() % 4873L) / 4873.0F / 8F;
//$$	}
//$$
//$$}
//# end
