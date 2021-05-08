package de.pfannekuchen.lotas.mixin.patches;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.core.utils.ConfigUtils;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

@Mixin(Block.class)
public class MixinBlockPatch {

	@Redirect(method = "spawnAsEntity", at = @At(value = "NEW", target = "Lnet/minecraft/entity/item/EntityItem;<init>(Lnet/minecraft/world/World;DDDLnet/minecraft/item/ItemStack;)Lnet/minecraft/entity/item/EntityItem;"))
	private static EntityItem moveItem(World w, double x, double y, double z, ItemStack stack) {
		EntityItem it = new EntityItem(w, x, y, z, stack);
		try {
			if (ConfigUtils.getBoolean("tools", "manipulateVelocityTowards")) {
				double pX = MCVer.player(Minecraft.getMinecraft()).posX - x;
				double pZ = MCVer.player(Minecraft.getMinecraft()).posZ - z;
				it.motionX = Math.min(Math.max((int) pX, 1), -1) * 0.03f;
				it.motionZ = Math.min(Math.max((int) pZ, 1), -1) * 0.03f;
			} else if (ConfigUtils.getBoolean("tools", "manipulateVelocityAway")) {
				double pX = MCVer.player(Minecraft.getMinecraft()).posX - x;
				double pZ = MCVer.player(Minecraft.getMinecraft()).posZ - z;
				it.motionX = Math.min(Math.max((int) pX, 1), -1) * -0.03f;
				it.motionZ = Math.min(Math.max((int) pZ, 1), -1) * -0.03f;
			}
		} catch (Exception e) {
			// When called in loading screen
		}
		return it;
	}
	
}
