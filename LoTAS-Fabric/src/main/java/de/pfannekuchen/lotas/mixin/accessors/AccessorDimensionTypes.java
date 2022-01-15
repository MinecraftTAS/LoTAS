package de.pfannekuchen.lotas.mixin.accessors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.world.level.dimension.DimensionType;

@Mixin(DimensionType.class)
public interface AccessorDimensionTypes {
	//#if MC>=11601
//$$ 	@Accessor("DEFAULT_OVERWORLD")
//$$ 	public static DimensionType getOverworld() {
//$$ 		throw new AssertionError();
//$$ 	}
//$$
//$$ 	@Accessor("DEFAULT_NETHER")
//$$ 	public static DimensionType getNether() {
//$$ 		throw new AssertionError();
//$$ 	}
//$$
//$$ 	@Accessor("DEFAULT_END")
//$$ 	public static DimensionType getEnd() {
//$$ 		throw new AssertionError();
//$$ 	}
	//#else
	@Accessor("OVERWORLD")
	public static DimensionType getOverworld() {
		throw new AssertionError();
	}

	@Accessor("NETHER")
	public static DimensionType getNether() {
		throw new AssertionError();
	}

	@Accessor("THE_END")
	public static DimensionType getEnd() {
		throw new AssertionError();
	}
	//#endif
}
