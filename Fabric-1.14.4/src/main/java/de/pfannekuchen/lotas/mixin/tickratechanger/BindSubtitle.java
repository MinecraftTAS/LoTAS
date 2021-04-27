package de.pfannekuchen.lotas.mixin.tickratechanger;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import de.pfannekuchen.lotas.tickratechanger.TickrateChanger;
import net.minecraft.client.gui.hud.SubtitlesHud;
import net.minecraft.util.Util;

@Mixin(SubtitlesHud.SubtitleEntry.class)
public class BindSubtitle {
	
	@Shadow
	private long time;
	
	private long offset=0;
	private long store=0;
	
	private boolean once=false;
	
	@Inject(method = "getTime", at = @At(value = "HEAD"), cancellable = true)
	public void redoGetTime(CallbackInfoReturnable<Long> ci) {
		if(TickrateChanger.tickrate==0) {
			if(!once) {
				once=true;
				store=Util.getMeasuringTimeMs()-offset;
			}
			offset=Util.getMeasuringTimeMs()-store;
		}else {
			if(once) {
				once=false;
			}
		}
		ci.setReturnValue(this.time+offset);
		ci.cancel();
	}
	
	@Inject(method = "reset", at = @At(value = "HEAD"))
	public void resetOnReset(CallbackInfo ci) {
		offset=0;
		store=0;
		once=false;
	}
}
