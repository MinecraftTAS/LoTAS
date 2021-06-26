package de.pfannekuchen.lotas.mixin.patches;

import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import de.pfannekuchen.lotas.dropmanipulation.drops.blockdrops.LeavesDropManipulation;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.state.IBlockState;
//#if MC>=10900
import net.minecraft.util.math.BlockPos;
//#else
//$$ import net.minecraft.util.BlockPos;
//#endif
import net.minecraft.world.World;

/**
 * Extension to {@link MixinBlockPatch}
 * @author Scribble
 */
@Mixin(BlockLeaves.class)
public abstract class MixinLeavesPatch {
	
	@Redirect(method = "getDrops", at = @At(value = "INVOKE", target = "Ljava/util/Random;nextInt(I)I"), remap = false)
	public int redirectRandom(Random rand, int chance) {
		return LeavesDropManipulation.dropSapling.isToggled() ? 0:rand.nextInt(chance);
	}
	
	//#if MC>=10900
	@Redirect(method = "getDrops", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockLeaves;dropApple(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/state/IBlockState;I)V"))
	//#else
//$$ 	@Redirect(method = "getDrops", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockLeaves;dropApple(Lnet/minecraft/world/World;Lnet/minecraft/util/BlockPos;Lnet/minecraft/block/state/IBlockState;I)V"))
	//#endif
	public void redirectDropApple(BlockLeaves leaves, World world, BlockPos pos, IBlockState state, int chance) {
		this.dropApple((World)world, pos, state, !LeavesDropManipulation.dropApple.isToggled() ? chance : 1);
	}
	
	@Shadow
	protected abstract void dropApple(World world, BlockPos pos, IBlockState state, int chance);
	
}