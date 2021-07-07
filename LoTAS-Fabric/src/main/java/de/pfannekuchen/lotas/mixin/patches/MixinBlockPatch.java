package de.pfannekuchen.lotas.mixin.patches;

import java.util.List;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import de.pfannekuchen.lotas.gui.DropManipulationScreen;

@Mixin(net.minecraft.world.level.block.Block.class)
public class MixinBlockPatch {
	@Inject(method = "getDrops(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/storage/loot/LootContext$Builder;)Ljava/util/List;", at = @At("HEAD"), cancellable = true)
	public void redodrop(BlockState state, LootContext.Builder builder, CallbackInfoReturnable<List<ItemStack>> drops) {
		for (DropManipulationScreen.DropManipulation man : DropManipulationScreen.manipulations) {
			if (!man.enabled.selected())
				continue;
			List<ItemStack> list = man.redirectDrops(state);
			if (!list.isEmpty()) {
				drops.setReturnValue(list);
				drops.cancel();
			}
		}
	}

}
