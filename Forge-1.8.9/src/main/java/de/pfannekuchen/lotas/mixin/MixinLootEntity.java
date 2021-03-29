package de.pfannekuchen.lotas.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import de.pfannekuchen.lotas.gui.GuiLootManipulation;
import de.pfannekuchen.lotas.gui.GuiLootManipulation.DropManipulation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

@Mixin(EntityLivingBase.class)
public abstract class MixinLootEntity extends Entity {
	
	public MixinLootEntity(World worldIn) {
		super(worldIn);
	}

	public List<ItemStack> generateLootForPools() {
        for (DropManipulation man : GuiLootManipulation.manipulations) {
            if (!man.enabled.isChecked()) continue;
            List<ItemStack> list = man.redirectDrops((EntityLivingBase) (Object) this);
            if (!list.isEmpty()) {
                return list;
            }
        }
        return null;
	}
	
	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLivingBase;dropFewItems(ZI)V"), method = "onDeath")
    protected void injectdropFewItems(EntityLivingBase e, boolean b, int i) {
		List<ItemStack> loot = generateLootForPools();
		if (loot != null) 
			if (!loot.isEmpty()) {
				for (ItemStack itemStack : loot) 
					entityDropItem(itemStack, 0.0F);
				return;
			}

		
		dropFewItems(b, i);
		
	}

	@Shadow
	protected abstract void dropFewItems(boolean b, int i);
	
}
