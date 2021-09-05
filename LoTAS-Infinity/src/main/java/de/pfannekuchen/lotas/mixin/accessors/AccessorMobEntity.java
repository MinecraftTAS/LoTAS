package de.pfannekuchen.lotas.mixin.accessors;

import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Mob.class)
public interface AccessorMobEntity {
	@Accessor
	public void setArmorDropChances(float[] armor);
	
	@Accessor
	public void setHandDropChances(float[] hand);
}
