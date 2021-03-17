package de.pfannekuchen.lotas.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.world.end.DragonFightManager;

@Mixin(DragonFightManager.class)
public abstract class MixinDragonFightManager {
	
	@Shadow
    public boolean scanForLegacyFight;
	@Shadow
    public boolean previouslyKilled;
	private int ticks = 20;
	
	@Inject(at = @At("RETURN"), method = "<init>")
	public void endInit(CallbackInfo ci) {
		scanForLegacyFight = false;
	}
	
	@Inject(at = @At("HEAD"), method = "tick")
	public void injecttick(CallbackInfo ci) {
		ticks--;
		if (ticks == 0 && previouslyKilled) {
			scanForLegacyFight = true;
		}
	}
	
}