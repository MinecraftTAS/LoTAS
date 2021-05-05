package de.pfannekuchen.lotas.mixin.server;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.pfannekuchen.lotas.gui.LootManipulationScreen;
import de.pfannekuchen.lotas.utils.ConfigManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

@Mixin(LivingEntity.class)
public class MixinDropEntity {

    @Inject(method = "dropLoot", at = @At("HEAD"), cancellable = true)
    public void redodrop(DamageSource source, boolean causedByPlayer, CallbackInfo ci) {
        for (LootManipulationScreen.DropManipulation man : LootManipulationScreen.manipulations) {
            if (!man.enabled.isChecked()) continue;
            List<ItemStack> list = man.redirectDrops((LivingEntity) (Object) this);
            if (!list.isEmpty()) {
                for (ItemStack itemStack : list) {
                    ((LivingEntity) (Object) this).dropStack(itemStack);
                }
                ci.cancel();
            }
        }
    }

    @Redirect(method = "onDeath", at = @At(value = "NEW", target = "Lnet/minecraft/entity/ItemEntity;<init>(Lnet/minecraft/world/World;DDDLnet/minecraft/item/ItemStack;)Lnet/minecraft/entity/ItemEntity;"))
	private static ItemEntity haxInit(World w, double x, double y, double z, ItemStack stack) {
    	ItemEntity it = new ItemEntity(w, x, y, z, stack);
		try {
			if (ConfigManager.getBoolean("tools", "manipulateVelocityTowards")) {
				double pX = MinecraftClient.getInstance().player.x - x;
				double pZ = MinecraftClient.getInstance().player.z - z;
				it.setVelocity(Math.min(Math.max(Math.round(pX), 1), -1) * 0.03f, it.getVelocity().y, Math.max(Math.round(pZ), 1) * 0.03f);
			} else if (ConfigManager.getBoolean("tools", "manipulateVelocityAway")) {
				double pX = MinecraftClient.getInstance().player.x - x;
				double pZ = MinecraftClient.getInstance().player.z - z;
				it.setVelocity(Math.min(Math.max(Math.round(pX), 1), -1) * -0.03f, it.getVelocity().y, Math.max(Math.round(pZ), 1) * -0.03f);
			}
		} catch (Exception e) {
			// Ignore this Error
		}
		return it;
	}
    
}
