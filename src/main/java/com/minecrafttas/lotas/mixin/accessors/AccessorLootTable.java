package com.minecrafttas.lotas.mixin.accessors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;

/**
 * This mixin accessor makes the loot pools accessible.
 * @author Pancake
 */
@Mixin(LootTable.class)
public interface AccessorLootTable {

	/**
	 * This Accessor opens the private field containing the list of loot pools
	 * @return Loot Pools
	 */
	@Accessor("pools")
	public LootPool[] pools();
	
}
