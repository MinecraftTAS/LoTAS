package de.pfannekuchen.lotas.mixin.patches;

import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.item.ItemEnderPearl;

/**
 * This mixin removes the Ender-Pearl delay because no one likes it anyways, by reconnecting without reconnecting
 * @author Pancake
 * @since v1.1
 * @version v1.1
 */
@Mixin(ItemEnderPearl.class)
public class MixinPearlPatch {
	
	//#if MC>=10900
	@org.spongepowered.asm.mixin.injection.ModifyArg(at = @org.spongepowered.asm.mixin.injection.At(value = "INVOKE", target = "Lnet/minecraft/util/CooldownTracker;setCooldown(Lnet/minecraft/item/Item;I)V"), method = "onItemRightClick", index = 1)
	public int removePearlCooldown(int ticksIn) {
		if (de.pfannekuchen.lotas.core.utils.ConfigUtils.getBoolean("tools", "removePearlDelay")) {
			de.pfannekuchen.lotas.core.MCVer.player(net.minecraft.client.Minecraft.getMinecraft()).motionX = 0;
			de.pfannekuchen.lotas.core.MCVer.player(net.minecraft.client.Minecraft.getMinecraft()).motionY = 0;
			de.pfannekuchen.lotas.core.MCVer.player(net.minecraft.client.Minecraft.getMinecraft()).motionZ = 0;
			return 0;
		} else {
			return ticksIn;
		}
	}
	//#endif
	
}
