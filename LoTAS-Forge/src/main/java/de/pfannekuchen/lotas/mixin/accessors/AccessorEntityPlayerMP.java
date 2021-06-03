package de.pfannekuchen.lotas.mixin.accessors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.entity.player.EntityPlayerMP;

@Mixin(EntityPlayerMP.class)
public interface AccessorEntityPlayerMP {

	@Accessor("respawnInvulnerabilityTicks")
	public int respawnInvulnerabilityTicks();
	
	@Accessor("respawnInvulnerabilityTicks")
	public void respawnInvulnerabilityTicks(int value);
	
}
