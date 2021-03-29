package de.pfannekuchen.lotas.mixin;

import java.util.List;
import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import de.pfannekuchen.lotas.gui.GuiLootManipulation;
import de.pfannekuchen.lotas.gui.GuiLootManipulation.DropManipulation;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;

@Mixin(EntityLiving.class)
public class MixinLootEntity {
	
	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/storage/loot/LootTable;generateLootForPools(Ljava/util/Random;Lnet/minecraft/loot/context/LootContext;)Ljava/util/List;"), method = "dropLoot")
    public List<ItemStack> generateLootForPools(LootTable input, Random rng, LootContext context) {
        for (DropManipulation man : GuiLootManipulation.manipulations) {
            if (!man.enabled.isChecked()) continue;
            List<ItemStack> list = man.redirectDrops((EntityLiving) (Object) this);
            if (!list.isEmpty()) {
                return list;
            }
        }
        return input.generateLootForPools(rng, context);
	}
	
}
