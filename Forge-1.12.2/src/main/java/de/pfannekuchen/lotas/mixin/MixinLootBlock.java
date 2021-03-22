package de.pfannekuchen.lotas.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.pfannekuchen.lotas.gui.GuiLootManipulation;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

@Mixin(Block.class)
public class MixinLootBlock {
	
	@Inject(at = @At("HEAD"), method = "Lnet/minecraft/block/Block;getDrops(Lnet/minecraft/util/NonNullList;Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/state/IBlockState;I)V", cancellable = true)
	public void getDropsInject(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune, CallbackInfo ci) {
        for (GuiLootManipulation.DropManipulation man : GuiLootManipulation.manipulations) {
            if (!man.enabled.isChecked()) continue;
            List<ItemStack> list = man.redirectDrops(state);
            if (!list.isEmpty()) {
                drops.clear();
                drops.addAll(list);
                ci.cancel();
            }
        }
	}
	
}
