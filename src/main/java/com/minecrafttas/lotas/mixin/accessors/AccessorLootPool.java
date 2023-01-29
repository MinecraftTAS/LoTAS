package com.minecrafttas.lotas.mixin.accessors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;

/**
 * This mixin accessor makes the loot pool values accessible.
 * @author Pancake
 */
@Mixin(LootPool.class)
public interface AccessorLootPool {

	/**
	 * This Accessor opens the private field containing the list of loot entries
	 * @return Identifier
	 */
	@Accessor("entries")
	public LootPoolEntryContainer[] entries();

}
