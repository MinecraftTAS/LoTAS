package de.pfannekuchen.lotas.mixin;

import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import de.pfannekuchen.lotas.drops.blockdrops.LeaveDropManipulation;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

@Mixin(BlockLeaves.class)
public abstract class MixinLeaves extends Block {

	public MixinLeaves(Material materialIn) {
		super(materialIn);
	}

	@Overwrite
    public java.util.List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		java.util.List<ItemStack> drops = new java.util.ArrayList<ItemStack>();
		Random rand = world instanceof World ? ((World)world).rand : new Random();
        int chance = this.getSaplingDropChance(state);

        if (fortune > 0)
        {
            chance -= 2 << fortune;
            if (chance < 10) chance = 10;
        }

        if (rand.nextInt(chance) == 0 || LeaveDropManipulation.dropSapling.isToggled())
        {
            ItemStack drop = new ItemStack(getItemDropped(state, rand, fortune), 1, damageDropped(state));
            drops.add(drop);
        }

        chance = 200;
        if (fortune > 0)
        {
            chance -= 10 << fortune;
            if (chance < 40) chance = 40;
        }

        this.captureDrops(true);
        if (world instanceof World)
            this.dropApple((World)world, pos, state, !LeaveDropManipulation.dropApple.isToggled() ? chance : 1); // Dammet mojang
        drops.addAll(this.captureDrops(false));
        return drops;
    }

	@Shadow
	protected abstract void dropApple(World world, BlockPos pos, IBlockState state, int chance);
	@Shadow
	protected abstract int getSaplingDropChance(IBlockState state);
	
}
