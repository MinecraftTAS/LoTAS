package de.pfannekuchen.lotas.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import de.pfannekuchen.lotas.config.ConfigManager;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

@Mixin(Entity.class)
public class MixinDropItem {

	@Shadow
	private double posX;
	@Shadow
	private double posY;
	@Shadow
	private double posZ;

	@Redirect(method = "entityDropItem", at = @At(value = "NEW", target = "Lnet/minecraft/entity/item/EntityItem;<init>(Lnet/minecraft/world/World;DDDLnet/minecraft/item/ItemStack;)Lnet/minecraft/entity/item/EntityItem;"))
	public EntityItem haxInit(World w, double x, double y, double z, ItemStack stack) {
		EntityItem it = new EntityItem(w, x, y, z, stack);
		try {
			if (ConfigManager.getBoolean("tools", "manipulateVelocityTowards")) {
				double pX = Minecraft.getMinecraft().thePlayer.posX - posX;
				double pZ = Minecraft.getMinecraft().thePlayer.posZ - posZ;
				it.motionX = Math.round(pX) * 0.03f;
				it.motionZ = Math.round(pZ) * 0.03f;
			} else if (ConfigManager.getBoolean("tools", "manipulateVelocityAway")) {
				double pX = Minecraft.getMinecraft().thePlayer.posX - posX;
				double pZ = Minecraft.getMinecraft().thePlayer.posZ - posZ;
				it.motionX = Math.round(pX) * -0.03f;
				it.motionZ = Math.round(pZ) * -0.03f;
			}
		} catch (Exception e) {
			
		}
		return it;
	}

}
