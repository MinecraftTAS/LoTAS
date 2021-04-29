package de.pfannekuchen.lotas.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import de.pfannekuchen.lotas.config.ConfigManager;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemEnderPearl;

@Mixin(ItemEnderPearl.class)
public class MixinPearlDelay {

	@ModifyArg(at = @At(value = "INVOKE", target = "Lnet/minecraft/util/CooldownTracker;setCooldown(Lnet/minecraft/item/Item;I)V"), method = "onItemRightClick", index = 1)
	public int removePearlCooldown(int ticksIn) {
		if (ConfigManager.getBoolean("tools", "removePearlDelay")) {
			Minecraft.getMinecraft().player.motionX = 0;
			Minecraft.getMinecraft().player.motionY = 0;
			Minecraft.getMinecraft().player.motionZ = 0;
			return 0;
		} else {
			return ticksIn;
		}
	}
	
}
