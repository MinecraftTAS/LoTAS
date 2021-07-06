package de.pfannekuchen.lotas.mixin.accessors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.entity.mob.MobEntity;

@Mixin(MobEntity.class)
public interface AccessorMobEntity {
	@Accessor
	public void setArmorDropChances(float[] armor);
	
	@Accessor
	public void setHandDropChances(float[] hand);
}
