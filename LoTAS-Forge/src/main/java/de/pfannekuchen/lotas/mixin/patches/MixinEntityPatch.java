package de.pfannekuchen.lotas.mixin.patches;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.core.utils.ConfigUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * This Mixin alters the drops of an entity when it dies
 * @author Pancake
 * @since v1.0
 * @version v1.0
 */
@Mixin(Entity.class)
public class MixinEntityPatch {

	@Shadow
	private double posX;
	@Shadow
	private double posY;
	@Shadow
	private double posZ;
	
	/**
	 * Changes the velocity of a item dropped from an entity
	 * @return Returns a new modified EntityItem with a custom velocity
	 */
	@Redirect(method = "entityDropItem", at = @At(value = "NEW", target = "Lnet/minecraft/entity/item/EntityItem;<init>(Lnet/minecraft/world/World;DDDLnet/minecraft/item/ItemStack;)Lnet/minecraft/entity/item/EntityItem;"))
	public EntityItem moveItem(World w, double x, double y, double z, ItemStack stack) {
		EntityItem it = new EntityItem(w, posX, posY, posZ, stack);
		try {
			double pX = MCVer.player(Minecraft.getMinecraft()).posX - posX;
			double pZ = MCVer.player(Minecraft.getMinecraft()).posZ - posZ;
			if (pX > 0) pX = 1;
			if (pX < 0) pX = -1;
			if (pZ > 0) pZ = 1;
			if (pZ < 0) pZ = -1;
			if (ConfigUtils.getBoolean("tools", "manipulateVelocityTowards")) {
				it.motionX = pX * 0.1f;
				it.motionZ = pZ * 0.1f;
			} else if (ConfigUtils.getBoolean("tools", "manipulateVelocityAway")) {
				it.motionX = pX * -0.1f;
				it.motionZ = pZ * -0.1f;
			}
		} catch (Exception e) {
			// When called in loading screen
		}
		return it;
	}
	
}
