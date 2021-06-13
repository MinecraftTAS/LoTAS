package de.pfannekuchen.lotas.mixin.patches;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import de.pfannekuchen.lotas.core.utils.ConfigUtils;
import de.pfannekuchen.lotas.gui.DropManipulationScreen;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.world.World;

@Mixin(Block.class)
public class MixinBlockPatch {

    @Inject(method = "getDroppedStacks(Lnet/minecraft/block/BlockState;Lnet/minecraft/loot/context/LootContext$Builder;)Ljava/util/List;", at = @At("HEAD"), cancellable = true)
    public void redodrop(BlockState state, LootContext.Builder builder, CallbackInfoReturnable<List<ItemStack>> drops) {
        for (DropManipulationScreen.DropManipulation man : DropManipulationScreen.manipulations) {
            if (!man.enabled.isChecked()) continue;
            List<ItemStack> list = man.redirectDrops(state);
            if (!list.isEmpty()) {
                drops.setReturnValue(list);
                drops.cancel();
            }
        }
    }
    
    @Redirect(method = "dropStack", at = @At(value = "NEW", target = "Lnet/minecraft/entity/ItemEntity;<init>(Lnet/minecraft/world/World;DDDLnet/minecraft/item/ItemStack;)Lnet/minecraft/entity/ItemEntity;"))
	private static ItemEntity haxInit(World w, double x, double y, double z, ItemStack stack) {
    	ItemEntity it = new ItemEntity(w, x, y, z, stack);
		try {
			double pX = MinecraftClient.getInstance().player.x - x;
			double pZ = MinecraftClient.getInstance().player.z - z;
			if (pX > 0) pX = 1;
			if (pX < 0) pX = -1;
			if (pZ > 0) pZ = 1;
			if (pZ < 0) pZ = -1;
			if (ConfigUtils.getBoolean("tools", "manipulateVelocityTowards")) {
				it.setVelocity(pX * 0.1f, 0.2F, pZ * 0.1f);
			} else if (ConfigUtils.getBoolean("tools", "manipulateVelocityAway")) {
				it.setVelocity(pX * -0.1f, 0.2F, pZ * -0.1f);
			}
		} catch (Exception e) {
			// Ignore this Error when loading in the Loading Screen
		}
		return it;
	}
	

}
