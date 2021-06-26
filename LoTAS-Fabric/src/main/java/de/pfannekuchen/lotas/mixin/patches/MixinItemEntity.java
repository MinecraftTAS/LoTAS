package de.pfannekuchen.lotas.mixin.patches;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.pfannekuchen.lotas.core.utils.ConfigUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

@Mixin(ItemEntity.class)
public abstract class MixinItemEntity extends Entity {

	public MixinItemEntity(EntityType<?> type, World world) {
		super(type, world);
	}
	//#if MC>=11700
//$$ 	@Inject(at = @At(value = "TAIL"), method = "Lnet/minecraft/entity/ItemEntity;<init>(Lnet/minecraft/world/World;DDDLnet/minecraft/item/ItemStack;DDD)V")
	//#else
	@Inject(at = @At("TAIL"), method = "Lnet/minecraft/entity/ItemEntity;<init>(Lnet/minecraft/world/World;DDD)V")
	//#endif
		public void hackVelocity(CallbackInfo ci) {
		try {
			//#if MC>=11601
//$$ 			           double pX = MinecraftClient.getInstance().player.getX() - getX();
//$$ 			           double pZ = MinecraftClient.getInstance().player.getZ() - getZ();
			//#else
			double pX = MinecraftClient.getInstance().player.x - x;
			double pZ = MinecraftClient.getInstance().player.z - z;
			//#endif
			if (pX > 0)
				pX = 1;
			if (pX < 0)
				pX = -1;
			if (pZ > 0)
				pZ = 1;
			if (pZ < 0)
				pZ = -1;
			if (ConfigUtils.getBoolean("tools", "manipulateVelocityTowards")) {
				setVelocity(pX * 0.1D, .2F, pZ * 0.1D);
			} else if (ConfigUtils.getBoolean("tools", "manipulateVelocityAway")) {
				setVelocity(pX * -0.1D, .2F, pZ * -0.1D);
			}
		} catch (Exception e) {
			// Ignore this Error
		}
	}

}
