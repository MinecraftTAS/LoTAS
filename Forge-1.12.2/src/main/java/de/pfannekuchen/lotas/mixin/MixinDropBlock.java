package de.pfannekuchen.lotas.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import de.pfannekuchen.lotas.config.ConfigManager;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

@Mixin(Block.class)
public abstract class MixinDropBlock {

	@Redirect(method = "spawnAsEntity", at = @At(value = "NEW", target = "Lnet/minecraft/entity/item/EntityItem;<init>(Lnet/minecraft/world/World;DDDLnet/minecraft/item/ItemStack;)Lnet/minecraft/entity/item/EntityItem;"))
	private static EntityItem haxInit(World w, double x, double y, double z, ItemStack stack) {
		EntityItem it = new EntityItem(w, x, y, z, stack);
		try {
			if (ConfigManager.getBoolean("tools", "manipulateVelocityTowards")) {
				double pX = Minecraft.getMinecraft().player.posX - x;
				double pZ = Minecraft.getMinecraft().player.posZ - z;
				it.motionX = Math.min(Math.max(Math.round(pX), 1), -1) * 0.03f;
				it.motionZ = Math.max(Math.round(pZ), 1) * 0.03f;
			} else if (ConfigManager.getBoolean("tools", "manipulateVelocityAway")) {
				double pX = Minecraft.getMinecraft().player.posX - x;
				double pZ = Minecraft.getMinecraft().player.posZ - z;
				it.motionX = Math.min(Math.max(Math.round(pX), 1), -1) * -0.03f;
				it.motionZ = Math.max(Math.round(pZ), 1) * -0.03f;
			}
		} catch (Exception e) {
			// Ignore this Error
		}
		return it;
	}
	

}
