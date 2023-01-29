package com.minecrafttas.lotas.mixin.accessors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;

/**
 * This mixin accessor makes the loot table in the loot table reference class accessible.
 * @author Pancake
 */
@Mixin(LootTableReference.class)
public interface AccessorLootTableReference {

	/**
	 * This Accessor opens the private field containing the item
	 * @return Item
	 */
	@Accessor("name")
	public ResourceLocation name();

}
