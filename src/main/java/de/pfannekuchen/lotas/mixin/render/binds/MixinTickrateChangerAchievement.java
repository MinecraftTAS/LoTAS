package de.pfannekuchen.lotas.mixin.render.binds;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import de.pfannekuchen.lotas.mods.TickrateChangerMod;
//#if MC<=11102
//$$ import net.minecraft.client.gui.achievement.GuiAchievement;
//$$ import net.minecraft.stats.Achievement;
//$$ import org.spongepowered.asm.mixin.injection.Redirect;
//#endif

//#if MC>=11200
@Mixin(targets = "net/minecraft/client/gui/toasts/GuiToast$ToastInstance")
public abstract class MixinTickrateChangerAchievement {

	@ModifyVariable(method = "render(II)Z", at = @At(value = "STORE", ordinal=0))
	public long modifyAnimationTime(long animationTimer) {
		return TickrateChangerMod.getMilliseconds();
	}

}
//#else
//$$ @Mixin(GuiAchievement.class)
//$$ public abstract class MixinTickrateChangerAchievement {
//$$
//$$ 	@Redirect(method = "displayAchievement", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;getSystemTime()J"))
//$$ 	public long redirectIt(Achievement ach) {
//$$ 		return TickrateChangerMod.getMilliseconds();
//$$ }
//$$
//$$ @Redirect(method = "displayUnformattedAchievement", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;getSystemTime()J"))
//$$ public long redirectIt2(Achievement achievementIn) {
//$$ 		return TickrateChangerMod.getMilliseconds();
//$$ }
//$$
//$$ @Redirect(method = "updateAchievementWindow", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;getSystemTime()J"))
//$$ public long redirectIt3() {
//$$ 		return TickrateChangerMod.getMilliseconds();
//$$ }
//$$
//$$ }
//#endif