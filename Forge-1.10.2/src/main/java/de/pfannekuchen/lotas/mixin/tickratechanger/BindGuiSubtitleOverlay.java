package de.pfannekuchen.lotas.mixin.tickratechanger;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import de.pfannekuchen.lotas.tickratechanger.TickrateChanger;
import net.minecraft.client.gui.GuiSubtitleOverlay;

/**
 * 
 * @author ScribbleLP
 */
@Mixin(GuiSubtitleOverlay.class)
public abstract class BindGuiSubtitleOverlay {
	@ModifyConstant(method = "renderSubtitles", constant = @Constant(longValue = 3000L))
	public long applyTickrate(long threethousand) {
		float multiplier=TickrateChanger.tickrate==0 ? 20F/TickrateChanger.tickrateSaved : 20F/TickrateChanger.tickrate;
		return (long) (threethousand*multiplier);
	}
	@ModifyConstant(method = "renderSubtitles", constant = @Constant(floatValue = 3000F))
	public float applyTickrate2(float threethousand) {
		float multiplier=TickrateChanger.tickrate==0 ? 20F/TickrateChanger.tickrateSaved : 20F/TickrateChanger.tickrate;
		return threethousand*multiplier;
	}
}
