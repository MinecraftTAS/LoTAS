package de.pfannekuchen.lotas.mixin.patches;

import java.util.List;
import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import de.pfannekuchen.lotas.gui.GuiDropManipulation;
import de.pfannekuchen.lotas.gui.GuiDropManipulation.DropManipulation;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
//#if MC>=10900
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;
@Mixin(EntityLiving.class)
public class MixinEntityLivingPatch {

	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/storage/loot/LootTable;generateLootForPools(Ljava/util/Random;Lnet/minecraft/world/storage/loot/LootContext;)Ljava/util/List;"), method = "dropLoot")
    public List<ItemStack> generateLootForPools(LootTable input, Random rng, LootContext context, boolean wasRecentlyHit, int lootingModifier, DamageSource source) {
        for (DropManipulation man : GuiDropManipulation.manipulations) {
           if (!man.enabled.isChecked()) continue;
           List<ItemStack> list = man.redirectDrops((EntityLiving) (Object) this, lootingModifier);
            if (!list.isEmpty()) {
                return list;
            }
        }
        return input.generateLootForPools(rng, context);
	}
}
//#else
//$$ import net.minecraft.world.World;
//$$ import net.minecraft.entity.Entity;
//$$ import net.minecraft.entity.EntityLivingBase;
//$$ import org.spongepowered.asm.mixin.Shadow;
//$$ @Mixin(EntityLivingBase.class)
//$$ public abstract class MixinEntityLivingPatch extends Entity {
//$$
//$$ 	public MixinEntityLivingPatch(World worldIn) {
//$$ 		super(worldIn);
//$$ 	}
//$$
//$$ 	public List<ItemStack> generateLootForPools(int i) {
//$$         for (DropManipulation man : GuiDropManipulation.manipulations) {
//$$             if (!man.enabled.isChecked()) continue;
//$$             List<ItemStack> list = man.redirectDrops((EntityLivingBase) (Object) this, i);
//$$             if (!list.isEmpty()) {
//$$                 return list;
//$$             }
//$$         }
//$$         return null;
//$$ 	}
//$$
//$$ 	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLivingBase;dropFewItems(ZI)V"), method = "onDeath")
//$$     protected void injectdropFewItems(EntityLivingBase e, boolean b, int i) {
//$$ 		List<ItemStack> loot = generateLootForPools(i);
//$$ 		if (loot != null) 
//$$ 			if (!loot.isEmpty()) {
//$$ 				for (ItemStack itemStack : loot) 
//$$ 					entityDropItem(itemStack, 0.0F);
//$$ 				return;
//$$ 			}
//$$
//$$
//$$ 		dropFewItems(b, i);
//$$
//$$ 	}
//$$
//$$ 	@Shadow
//$$ 	protected abstract void dropFewItems(boolean b, int i);
//$$
//$$ }
//#endif