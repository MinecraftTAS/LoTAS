package de.pfannekuchen.lotas.mixin.patches;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//#if MC<=11200
//$$ import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//#endif

import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.core.utils.ConfigUtils;
import de.pfannekuchen.lotas.gui.GuiDropChanceManipulation;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
//#if MC>=11200
import net.minecraft.util.NonNullList;
//#endif
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

@Mixin(Block.class)
public class MixinBlockPatch {

	@Redirect(method = "spawnAsEntity", at = @At(value = "NEW", target = "Lnet/minecraft/entity/item/EntityItem;<init>(Lnet/minecraft/world/World;DDDLnet/minecraft/item/ItemStack;)Lnet/minecraft/entity/item/EntityItem;"))
	private static EntityItem moveItem(World w, double x, double y, double z, ItemStack stack) {
		EntityItem it = new EntityItem(w, x, y, z, stack);
		try {
			if (ConfigUtils.getBoolean("tools", "manipulateVelocityTowards")) {
				double pX = MCVer.player(Minecraft.getMinecraft()).posX - x;
				double pZ = MCVer.player(Minecraft.getMinecraft()).posZ - z;
				it.motionX = Math.min(Math.max((int) pX, 1), -1) * 0.03f;
				it.motionZ = Math.min(Math.max((int) pZ, 1), -1) * 0.03f;
			} else if (ConfigUtils.getBoolean("tools", "manipulateVelocityAway")) {
				double pX = MCVer.player(Minecraft.getMinecraft()).posX - x;
				double pZ = MCVer.player(Minecraft.getMinecraft()).posZ - z;
				it.motionX = Math.min(Math.max((int) pX, 1), -1) * -0.03f;
				it.motionZ = Math.min(Math.max((int) pZ, 1), -1) * -0.03f;
			}
		} catch (Exception e) {
			// When called in loading screen
		}
		return it;
	}
	
	//#if MC>=11202
	@Inject(at = @At("HEAD"), remap = false, method = "Lnet/minecraft/block/Block;getDrops(Lnet/minecraft/util/NonNullList;Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/state/IBlockState;I)V", cancellable = true)
	public void getDropsInject(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune, CallbackInfo ci) {
    //#else
	//$$ @Inject(at = @At("HEAD"), remap = false, method = "Lnet/minecraft/block/Block;getDrops(Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/state/IBlockState;I)Ljava/util/List;", cancellable = true)
	//$$ public void getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune, CallbackInfoReturnable<List<ItemStack>> ci) {
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
