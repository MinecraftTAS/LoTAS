package de.pfannekuchen.lotas.mixin.tickratechanger;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import de.pfannekuchen.lotas.tickratechanger.TickrateChanger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiSubtitleOverlay;

@Mixin(GuiSubtitleOverlay.Subtitle.class)
public class MixinSubtitle {
	
	@Shadow
	private long startTime;
	
	private long offset=0;
	private long store=0;
	
	private boolean once=false;
	
	@Inject(method = "getStartTime", at = @At(value = "HEAD"), cancellable = true)
	public void redoGetStartTime(CallbackInfoReturnable<Long> ci) {
		if(TickrateChanger.tickrate==0) {
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
