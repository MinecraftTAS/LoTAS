package de.pfannekuchen.lotas.mixin;

import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import de.pfannekuchen.lotas.drops.blockdrops.LeaveDropManipulation;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

@Mixin(BlockLeaves.class)
public abstract class MixinLeaves {

	@Redirect(method = "getDrops", at = @At(value = "INVOKE", target = "Ljava/util/Random;nextInt(I)I", remap = false), remap = false)
	public int redirectRandom(Random rand, int chance) {
		return LeaveDropManipulation.dropSapling.isToggled() ? 0:rand.nextInt(chance);
	}
	
	@Redirect(method = "getDrops", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockLeaves;dropApple(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/state/IBlockState;I)V"))
	public void redirectDropApple(BlockLeaves leaves, World world, BlockPos pos, IBlockState state, int chance) {
		this.dropApple((World)world, pos, state, !LeaveDropManipulation.dropApple.isToggled() ? chance : 1);
	}
	@Shadow
	protected abstract void dropApple(World world, BlockPos pos, IBlockState state, int chance);
	
}
