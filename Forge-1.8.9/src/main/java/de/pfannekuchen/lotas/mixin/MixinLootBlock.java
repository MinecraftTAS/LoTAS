package de.pfannekuchen.lotas.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import de.pfannekuchen.lotas.gui.GuiLootManipulation;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;

@Mixin(Block.class)
public class MixinLootBlock {
	
	@Inject(at = @At("HEAD"), method = "getDrops", cancellable = true, remap = false)
	public void getDropsInject(IBlockAccess world, BlockPos pos, IBlockState state, int fortune, CallbackInfoReturnable<List<ItemStack>> ci) {
        for (GuiLootManipulation.DropManipulation man : GuiLootManipulation.manipulations) {
            if (!man.enabled.isChecked()) continue;
            List<ItemStack> list = man.redirectDrops(state);
            if (!list.isEmpty()) {
                ci.setReturnValue(list);
                ci.cancel();
            }
        }
	}
	
}
