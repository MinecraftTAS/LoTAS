package de.pfannekuchen.lotas.mixin.patches;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.core.utils.ConfigUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemEnderPearl;

@Mixin(ItemEnderPearl.class)
public class MixinPearlPatch {

	// TODO: Double check this too
	
	@ModifyArg(at = @At(value = "INVOKE", target = "Lnet/minecraft/util/CooldownTracker;setCooldown(Lnet/minecraft/item/Item;I)V"), method = "onItemRightClick", index = 1)
	public int removePearlCooldown(int ticksIn) {
		if (ConfigUtils.getBoolean("tools", "removePearlDelay")) {
			MCVer.player(Minecraft.getMinecraft()).motionX = 0;
			MCVer.player(Minecraft.getMinecraft()).motionY = 0;
			MCVer.player(Minecraft.getMinecraft()).motionZ = 0;
			return 0;
		} else {
			return ticksIn;
		}
	}
	
}
