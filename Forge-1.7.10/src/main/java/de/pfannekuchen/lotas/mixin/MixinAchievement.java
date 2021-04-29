package de.pfannekuchen.lotas.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import de.pfannekuchen.lotas.tickratechanger.TickrateChanger;
import net.minecraft.client.gui.achievement.GuiAchievement;
import net.minecraft.stats.Achievement;

@Mixin(GuiAchievement.class)
public abstract class MixinAchievement {

	@Redirect(method = "displayAchievement", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;getSystemTime()J"))
	public long redirectIt(Achievement ach) {
		return TickrateChanger.getMilliseconds();
	}
	
	@Redirect(method = "displayUnformattedAchievement", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;getSystemTime()J"))
	public long redirectIt2(Achievement achievementIn) {
		return TickrateChanger.getMilliseconds();
	}
	
	@Redirect(method = "updateAchievementWindow", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;getSystemTime()J"))
	public long redirectIt3() {
		return TickrateChanger.getMilliseconds();
	}
	
}
	