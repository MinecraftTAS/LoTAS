package de.pfannekuchen.lotas.mixin.patches;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import de.pfannekuchen.lotas.core.utils.ConfigUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.EnderPearlItem;

@Mixin(EnderPearlItem.class)
public class MixinEnderPearlPatch {

	@ModifyArg(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/ItemCooldownManager;set(Lnet/minecraft/item/Item;I)V"), method = "use", index = 1)
	public int removePearlCooldown(int ticksIn) {
		if (ConfigUtils.getBoolean("tools", "removePearlDelay")) {
			MinecraftClient.getInstance().player.setVelocity(0, 0, 0);
			;
			return 0;
		} else {
			return ticksIn;
		}
	}

}
