package de.pfannekuchen.lotas.mixin.render.binds;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import de.pfannekuchen.lotas.mods.TickrateChangerMod;
import net.minecraft.client.gui.hud.SubtitlesHud;
@Mixin(SubtitlesHud.SubtitleEntry.class)
public class MixinTickrateChangerSubtitle {

	@Shadow
	private long startTime;

	private long offset=0;
	private long store=0;

	private boolean once=false;

	@Inject(method = "getTime", at = @At(value = "HEAD"), cancellable = true)
	public void redoGetStartTime(CallbackInfoReturnable<Long> ci) {
		if(TickrateChangerMod.tickrate==0) {
			if(!once) {
				once=true;
				store=System.currentTimeMillis()-offset;
			}
			offset=System.currentTimeMillis()-store;
		}else {
			if(once) {
				once=false;
			}
		}
		ci.setReturnValue(this.startTime+offset);
		ci.cancel();
	}

	@Inject(method = "reset", at = @At(value = "HEAD"))
	public void resetOnRefresh(CallbackInfo ci) {
		offset=0;
		store=0;
		once=false;
	}
}
