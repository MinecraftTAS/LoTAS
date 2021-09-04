package de.pfannekuchen.lotas.mixin.patches;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.core.utils.ConfigUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;

/**
 * Alters the velocity of a newly spawned Item
 * @author Pancake
 */
@Mixin(ItemEntity.class)
public abstract class MixinItemEntity extends Entity {

	public MixinItemEntity(EntityType<?> type, Level world) {
		super(type, world);
	}
	
	/**
	 * Modify the Velocity of any Item
	 */
	//#if MC>=11700
//$$ 	@Inject(at = @At("TAIL"), method = "Lnet/minecraft/world/entity/item/ItemEntity;<init>(Lnet/minecraft/world/level/Level;DDDLnet/minecraft/world/item/ItemStack;)V")
	//#else
	@Inject(at = @At("TAIL"), method = "Lnet/minecraft/world/entity/item/ItemEntity;<init>(Lnet/minecraft/world/level/Level;DDD)V")
	//#endif
	public void hackVelocity(CallbackInfo ci) {
		try {
			double pX = MCVer.getX(Minecraft.getInstance().player) - MCVer.getX(this);
			double pZ = MCVer.getZ(Minecraft.getInstance().player) - MCVer.getZ(this);
			if (pX > 0)
				pX = 1;
			if (pX < 0)
				pX = -1;
			if (pZ > 0)
				pZ = 1;
			if (pZ < 0)
				pZ = -1;
			if (ConfigUtils.getBoolean("tools", "manipulateVelocityTowards")) {
				setDeltaMovement(pX * 0.1D, .2F, pZ * 0.1D);
			} else if (ConfigUtils.getBoolean("tools", "manipulateVelocityAway")) {
				setDeltaMovement(pX * -0.1D, .2F, pZ * -0.1D);
			}
		} catch (Exception e) {
			// Ignore this Error
		}
	}

}
