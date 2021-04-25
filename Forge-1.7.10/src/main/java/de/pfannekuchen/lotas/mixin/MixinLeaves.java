package de.pfannekuchen.lotas.mixin;

import java.util.ArrayList;
import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import de.pfannekuchen.lotas.drops.blockdrops.LeaveDropManipulation;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

@Mixin(BlockLeaves.class)
public abstract class MixinLeaves extends Block {

	public MixinLeaves(Material materialIn) {
		super(materialIn);
	}

	@Overwrite
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
		ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
		Random rand = world instanceof World ? ((World)world).rand : new Random();
        int chance = this.func_150123_b(metadata);

        if (fortune > 0)
        {
            chance -= 2 << fortune;
            if (chance < 10) chance = 10;
        }

        if (rand.nextInt(chance) == 0 || LeaveDropManipulation.dropSapling.isToggled())
        {
            ItemStack drop = new ItemStack(getItemDropped(metadata, rand, fortune), 1, damageDropped(metadata));
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
            this.func_150124_c((World)world, x, y, z, metadata, !LeaveDropManipulation.dropApple.isToggled() ? chance : 1); // Dammet mojang
        drops.addAll(this.captureDrops(false));
        return drops;
    }

	@Shadow
	protected abstract void func_150124_c(World p_150124_1_, int p_150124_2_, int p_150124_3_, int p_150124_4_, int p_150124_5_, int p_150124_6_);
	@Shadow
	protected abstract int func_150123_b(int state);
	
}
