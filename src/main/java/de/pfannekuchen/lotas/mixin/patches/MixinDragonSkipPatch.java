package de.pfannekuchen.lotas.mixin.patches;

import org.spongepowered.asm.mixin.Mixin;

//#if MC>=10900
import net.minecraft.world.end.DragonFightManager;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Mixin(DragonFightManager.class)
public abstract class MixinDragonSkipPatch {

	@Shadow
    public boolean scanForLegacyFight;
	@Shadow
    public boolean previouslyKilled;
	private int ticks = 20;

	@Inject(at = @At("RETURN"), method = "<init>")
	public void disableCheck(CallbackInfo ci) {
		scanForLegacyFight = false;
	}

	@Inject(at = @At("HEAD"), method = "tick")
	public void reenableCheck(CallbackInfo ci) {
		ticks--;
		if (ticks == 0 && previouslyKilled) {
			scanForLegacyFight = true;
		}
	}

}
//#else
//$$ import net.minecraft.client.Minecraft;
//$$ @Mixin(Minecraft.class)
//$$ public class MixinDragonSkipPatch {
//$$
//$$ }
//#endif