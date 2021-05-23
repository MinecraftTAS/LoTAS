package de.pfannekuchen.lotas.mixin.accessors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.entity.item.EntityItem;

@Mixin(EntityItem.class)
public interface AccessorEntityItem {
	
	//#if MC>=11200
	@Accessor("pickupDelay")
	//#else
//$$ 	@Accessor("delayBeforeCanPickup")
	//#endif
	public int pickupDelay();
	
}
