package de.pfannekuchen.lotas.mixin.render.binds;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
//#if MC>=10900
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import de.pfannekuchen.lotas.mods.TickrateChangerMod;
import net.minecraft.client.gui.GuiSubtitleOverlay;
@Mixin(GuiSubtitleOverlay.Subtitle.class)
public class MixinTickrateChangerSubtitle {

	@Shadow
	private long startTime;

	private long offset=0;
	private long store=0;

	private boolean once=false;

	@Inject(method = "getStartTime", at = @At(value = "HEAD"), cancellable = true)
	public void redoGetStartTime(CallbackInfoReturnable<Long> ci) {
		if(TickrateChangerMod.tickrate==0) {
			if(!once) {
				once=true;
				store=Minecraft.getSystemTime()-offset;
			}
			offset=Minecraft.getSystemTime()-store;
		}else {
			if(once) {
				once=false;
			}
		}
		ci.setReturnValue(this.startTime+offset);
		ci.cancel();
	}

	@Inject(method = "refresh", at = @At(value = "HEAD"))
	public void resetOnRefresh(CallbackInfo ci) {
		offset=0;
		store=0;
		once=false;
	}
}
//#else
//$$ @Mixin(Minecraft.class)
//$$ public class MixinTickrateChangerSubtitle {
//$$
//$$ }
//#endif