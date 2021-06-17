package de.pfannekuchen.lotas.mixin.accessors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.server.network.ServerPlayerEntity;

@Mixin(ServerPlayerEntity.class)
public interface AccessorServerPlayerEntity {

	//#if MC>=11601
//$$ 	@Accessor("joinInvulnerabilityTicks")
	//#else
	@Accessor("field_13998")
	//#endif
	int getField_13998();

	//#if MC>=11601
//$$ 	@Accessor("joinInvulnerabilityTicks")
	//#else
	@Accessor("field_13998")
	//#endif
	void setField_13998(int field_13998);

}