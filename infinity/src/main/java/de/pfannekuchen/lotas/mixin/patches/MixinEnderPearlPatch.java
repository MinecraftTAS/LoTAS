package de.pfannekuchen.lotas.mixin.patches;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import de.pfannekuchen.lotas.core.utils.ConfigUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.EnderpearlItem;

/**
 * This Mixin can remove the ender pearl delay by reconnecting
 * @author Pancake
 */
@Mixin(EnderpearlItem.class)
public class MixinEnderPearlPatch {

	/**
	 * Change the amount of ticks the pearl will be delayed to
	 */
	@ModifyArg(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemCooldowns;addCooldown(Lnet/minecraft/world/item/Item;I)V"), method = "use", index = 1)
	public int removePearlCooldown(int ticksIn) {
		if (ConfigUtils.getBoolean("tools", "removePearlDelay")) {
			Minecraft.getInstance().player.setDeltaMovement(0, 0, 0);
			return 0;
		} else {
			return ticksIn;
		}
	}

}
