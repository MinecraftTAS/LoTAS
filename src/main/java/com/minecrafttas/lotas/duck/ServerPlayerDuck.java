package com.minecrafttas.lotas.duck;


import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.dimension.DimensionType;

public interface ServerPlayerDuck {
	public Entity changeDimensionNoPortal(DimensionType dimensionType);
}
