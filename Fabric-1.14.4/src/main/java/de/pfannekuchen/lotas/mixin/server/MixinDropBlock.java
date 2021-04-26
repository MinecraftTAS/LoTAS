package de.pfannekuchen.lotas.mixin.server;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import de.pfannekuchen.lotas.gui.LootManipulationScreen;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;

@Mixin(Block.class)
public class MixinDropBlock {

    @Inject(method = "getDroppedStacks(Lnet/minecraft/block/BlockState;Lnet/minecraft/loot/context/LootContext$Builder;)Ljava/util/List;", at = @At("HEAD"), cancellable = true)
    public void redodrop(BlockState state, LootContext.Builder builder, CallbackInfoReturnable<List<ItemStack>> drops) {
        for (LootManipulationScreen.DropManipulation man : LootManipulationScreen.manipulations) {
            if (!man.enabled.isChecked()) continue;
            List<ItemStack> list = man.redirectDrops(state);
            if (!list.isEmpty()) {
                drops.setReturnValue(list);
                drops.cancel();
            }
        }
    }

}
