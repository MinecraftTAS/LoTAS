package de.pfannekuchen.lotas.mixin.patches;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;

import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.core.utils.ConfigUtils;
import de.pfannekuchen.lotas.gui.GuiDropChanceManipulation;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
//#if MC>=10900
import net.minecraft.util.math.BlockPos;
//#else
//$$ import net.minecraft.util.BlockPos;
//#endif
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * This mixin alters the behaviour whenever a block is being destroyed
 * @author Pancake
 * @since v1.0
 * @version v1.1
 */
@Mixin(Block.class)
public class MixinBlockPatch {
	
	/**
	 * Changes the velocity of a dropped item when wanted
	 * @return A Hijacked EntityItem
	 */
	@Redirect(method = "spawnAsEntity", at = @At(value = "NEW", target = "Lnet/minecraft/entity/item/EntityItem;<init>(Lnet/minecraft/world/World;DDDLnet/minecraft/item/ItemStack;)Lnet/minecraft/entity/item/EntityItem;"))
	private static EntityItem moveItem(World w, double x, double y, double z, ItemStack stack) {
		EntityItem it = new EntityItem(w, x, y, z, stack);
		try {
			double pX = MCVer.player(Minecraft.getMinecraft()).posX - x;
			double pZ = MCVer.player(Minecraft.getMinecraft()).posZ - z;
			if (pX > 0) pX = 1;
			if (pX < 0) pX = -1;
			if (pZ > 0) pZ = 1;
			if (pZ < 0) pZ = -1;
			if (ConfigUtils.getBoolean("tools", "manipulateVelocityTowards")) {
				it.motionX = pX * 0.1f;
				it.motionZ = pZ * 0.1f;
			} else if (ConfigUtils.getBoolean("tools", "manipulateVelocityAway")) {
				it.motionX = pX * -0.1f;
				it.motionZ = pZ * -0.1f;
			}
		} catch (Exception e) {
			// When called in loading screen
		}
		return it;
	}

	/**
	 * Changes the items dropped when breaking a block to the best possible ones if wanted.
	 */
	//#if MC>=11202
	@Inject(at = @At("HEAD"), remap = false, method = "Lnet/minecraft/block/Block;getDrops(Lnet/minecraft/util/NonNullList;Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/state/IBlockState;I)V", cancellable = true)
	public void getDropsInject(net.minecraft.util.NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune, org.spongepowered.asm.mixin.injection.callback.CallbackInfo ci) {
	//#else
	//#if MC>=10900
//$$ 	@Inject(at = @At("HEAD"), remap = false, method = "Lnet/minecraft/block/Block;getDrops(Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/state/IBlockState;I)Ljava/util/List;", cancellable = true)
//$$ 	public void getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune, org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable<List<ItemStack>> ci) {
	//#else
//$$ 	@Inject(at = @At("HEAD"), method = "getDrops", cancellable = true, remap = false)
//$$ 	public void getDropsInject(IBlockAccess world, BlockPos pos, IBlockState state, int fortune, org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable<List<ItemStack>> ci) {
	//#endif
	//#endif
		for (GuiDropChanceManipulation.DropManipulation man : GuiDropChanceManipulation .manipulations) {
			if (!man.enabled.isChecked()) continue;
			List<ItemStack> list = man.redirectDrops(state);
            if (!list.isEmpty()) {
            	//#if MC>=11202
                drops.clear();
                drops.addAll(list);
                //#else
                //$$ ci.setReturnValue(list);
                //#endif
                ci.cancel();
            }
        }
	}
	
}
