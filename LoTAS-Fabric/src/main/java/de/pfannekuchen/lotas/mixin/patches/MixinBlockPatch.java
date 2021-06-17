package de.pfannekuchen.lotas.mixin.patches;

import java.util.List;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.At;

import de.pfannekuchen.lotas.gui.DropManipulationScreen;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.server.world.ServerWorld;
//#if MC>=11601
//$$ import net.minecraft.loot.context.LootContextParameters;
//#endif
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

@Mixin(Block.class)
public class MixinBlockPatch {

	//#if MC>=11601
//$$ 	@Overwrite
//$$ 	public static List<ItemStack> getDroppedStacks(BlockState state, ServerWorld world, BlockPos pos, @Nullable BlockEntity blockEntity) {
//$$  		for (DropManipulationScreen.DropManipulation man : DropManipulationScreen.manipulations) {
//$$  			if (!man.enabled.isChecked())
//$$  				continue;
//$$  			List<ItemStack> list = man.redirectDrops(state);
//$$  			if (!list.isEmpty()) 
//$$  				return list;
//$$
//$$  		}
 		//#if MC>=11605
//$$  		net.minecraft.loot.context.LootContext.Builder builder = (new net.minecraft.loot.context.LootContext.Builder(world)).random(world.random).parameter(LootContextParameters.ORIGIN, Vec3d.ofCenter(pos)).parameter(LootContextParameters.TOOL, ItemStack.EMPTY).optionalParameter(LootContextParameters.BLOCK_ENTITY, blockEntity);
		//#else
//$$ 		net.minecraft.loot.context.LootContext.Builder builder = (new net.minecraft.loot.context.LootContext.Builder(world)).random(world.random).parameter(LootContextParameters.POSITION, pos).parameter(LootContextParameters.TOOL, ItemStack.EMPTY).optionalParameter(LootContextParameters.BLOCK_ENTITY, blockEntity);
		//#endif
//$$ 		return state.getDroppedStacks(builder);
//$$ 	}
//$$ 	@Overwrite
//$$ 	public static List<ItemStack> getDroppedStacks(BlockState state, ServerWorld world, BlockPos pos, @Nullable BlockEntity blockEntity, @Nullable Entity entity, ItemStack stack) {
//$$  		for (DropManipulationScreen.DropManipulation man : DropManipulationScreen.manipulations) {
//$$  			if (!man.enabled.isChecked())
//$$  				continue;
//$$  			List<ItemStack> list = man.redirectDrops(state);
//$$  			if (!list.isEmpty()) 
//$$  				return list;
//$$  		}
 		//#if MC>=11605
//$$  		net.minecraft.loot.context.LootContext.Builder builder = (new net.minecraft.loot.context.LootContext.Builder(world)).random(world.random).parameter(LootContextParameters.ORIGIN, Vec3d.ofCenter(pos)).parameter(LootContextParameters.TOOL, ItemStack.EMPTY).optionalParameter(LootContextParameters.BLOCK_ENTITY, blockEntity);
		//#else
//$$ 		net.minecraft.loot.context.LootContext.Builder builder = (new net.minecraft.loot.context.LootContext.Builder(world)).random(world.random).parameter(LootContextParameters.POSITION, pos).parameter(LootContextParameters.TOOL, ItemStack.EMPTY).optionalParameter(LootContextParameters.BLOCK_ENTITY, blockEntity);
		//#endif
//$$ 		return state.getDroppedStacks(builder);
//$$ 	}
	//#else
	@Inject(method = "getDroppedStacks(Lnet/minecraft/block/BlockState;Lnet/minecraft/loot/context/LootContext$Builder;)Ljava/util/List;", at = @At("HEAD"), cancellable = true)
	public void redodrop(BlockState state, LootContext.Builder builder, CallbackInfoReturnable<List<ItemStack>> drops) {
		for (DropManipulationScreen.DropManipulation man : DropManipulationScreen.manipulations) {
			if (!man.enabled.isChecked())
				continue;
			List<ItemStack> list = man.redirectDrops(state);
			if (!list.isEmpty()) {
				drops.setReturnValue(list);
				drops.cancel();
			}
		}
	}
	//#endif

}
