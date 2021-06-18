package de.pfannekuchen.lotas.mixin.patches;

import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.item.ItemEnderPearl;
//#if MC>=10900
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.core.utils.ConfigUtils;
import net.minecraft.client.Minecraft;
//#endif

/**
 * This mixin removes the Ender-Pearl delay because no one likes it anyways, by reconnecting without reconnecting
 * @author Pancake
 * @since v1.1
 * @version v1.1
 */
@Mixin(ItemEnderPearl.class)
public class MixinPearlPatch {
	
	//#if MC>=10900
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
	//#endif
	
}
