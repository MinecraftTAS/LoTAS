package de.pfannekuchen.lotas.mixin.accessors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.world.dimension.DimensionType;

@Mixin(DimensionType.class)
public interface AccessorDimensionTypes {
	@Accessor("OVERWORLD")
	public static DimensionType getOverworld() {
		throw new AssertionError();
	}
	
	@Accessor("THE_NETHER")
	public static DimensionType getNether() {
		throw new AssertionError();
	}
	
	@Accessor("THE_END")
	public static DimensionType getEnd() {
		throw new AssertionError();
	}
}
