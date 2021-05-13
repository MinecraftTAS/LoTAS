package de.pfannekuchen.lotas.mixin.accessors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.entity.EntityLiving;

@Mixin(EntityLiving.class)
public interface AccessorEntityLiving {

	//#if MC>=10900
	@Accessor("inventoryArmorDropChances") public void inventoryArmorDropChances(float[] value);	
	@Accessor("inventoryHandsDropChances") public void inventoryHandsDropChances(float[] value);
	//#else
//$$ 	@Accessor("equipmentDropChances") public void equipmentDropChances(float[] value);
	//#endif
	
}
