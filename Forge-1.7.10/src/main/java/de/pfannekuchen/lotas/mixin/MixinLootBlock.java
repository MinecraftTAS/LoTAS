package de.pfannekuchen.lotas.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import de.pfannekuchen.lotas.gui.GuiLootManipulation;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

@Mixin(Block.class)
public class MixinLootBlock {
	
	//(Lnet/minecraft/world/IBlockAccess;IIILnet/minecraft/block/Block;ILorg/spongepowered/asm/mixin/injection/callback/CallbackInfoReturnable;)V! Expected (Lnet/minecraft/world/World;IIIIILorg/spongepowered/asm/mixin/injection/callback/CallbackInfoReturnable;)V
	
	@Inject(at = @At("HEAD"), method = "getDrops", cancellable = true, remap = false)
	public void getDropsInject(World w, int x, int y, int z, int metadata, int fortune, CallbackInfoReturnable<List<ItemStack>> ci) {
        for (GuiLootManipulation.DropManipulation man : GuiLootManipulation.manipulations) {
            if (!man.enabled.isChecked()) continue;
            List<ItemStack> list = man.redirectDrops(w.getBlock(x, y, z));
            if (!list.isEmpty()) {
                ci.setReturnValue(list);
                ci.cancel();
            }
        }
	}
	
}
