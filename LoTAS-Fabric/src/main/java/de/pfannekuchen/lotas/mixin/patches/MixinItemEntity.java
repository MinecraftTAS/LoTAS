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
import net.minecraft.world.World;

@Mixin(ItemEntity.class)
public abstract class MixinItemEntity extends Entity {

	public MixinItemEntity(EntityType<?> type, World world) { super(type, world); }

	//#if MC>=11502
//$$ 	//ItemEntity(World world, double x, double y, double z)
//$$ 	@Inject(at = @At("TAIL"), method = "Lnet/minecraft/entity/ItemEntity;<init>(Lnet/minecraft/world/World;DDD)V")
//$$ 	public void hackVelocity(CallbackInfo ci) {
//$$ 		try {
//$$ 			if (ConfigUtils.getBoolean("tools", "manipulateVelocityTowards")) {
//$$ 				double pX = MinecraftClient.getInstance().player.x - x;
//$$ 				double pZ = MinecraftClient.getInstance().player.z - z;
//$$ 				setVelocity(Math.min(Math.max(Math.round(pX), 1), -1) * 0.03f, getVelocity().y, Math.max(Math.round(pZ), 1) * 0.03f);
//$$ 			} else if (ConfigUtils.getBoolean("tools", "manipulateVelocityAway")) {
//$$ 				double pX = MinecraftClient.getInstance().player.x - x;
//$$ 				double pZ = MinecraftClient.getInstance().player.z - z;
//$$ 				setVelocity(Math.min(Math.max(Math.round(pX), 1), -1) * -0.03f, getVelocity().y, Math.max(Math.round(pZ), 1) * -0.03f);
//$$ 			}
//$$ 		} catch (Exception e) {
//$$ 			// Ignore this Error
//$$ 		}
//$$ 	}
	//#endif
	
}
