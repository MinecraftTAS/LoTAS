package com.minecrafttas.lotas.mixin.accessors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.entries.CompositeEntryBase;
import net.minecraft.world.level.storage.loot.entries.LootItem;

/**
 * This mixin accessor makes the children of a composite entry base loot entry accessible.
 * @author Pancake
 */
@Mixin(CompositeEntryBase.class)
public interface AccessorCompositeEntryBase {

	/**
	 * This Accessor opens the private field containing the children
	 * @return Children
	 */
	@Accessor("children")
	public LootPoolEntryContainer[] children();

}
