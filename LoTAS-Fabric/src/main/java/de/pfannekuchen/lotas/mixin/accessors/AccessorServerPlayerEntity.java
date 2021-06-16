package de.pfannekuchen.lotas.mixin.accessors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.server.network.ServerPlayerEntity;

@Mixin(ServerPlayerEntity.class)
public interface AccessorServerPlayerEntity {

	@Accessor("field_13998")
	int getField_13998();

	@Accessor("field_13998")
	void setField_13998(int field_13998);

}
