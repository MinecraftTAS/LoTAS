package com.minecrafttas.lotas.mixin.accessors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.entries.LootItem;

/**
 * This mixin accessor makes the loot item in the lootitem class accessible.
 * @author Pancake
 */
@Mixin(LootItem.class)
public interface AccessorLootItem {

	/**
	 * This Accessor opens the private field containing the item
	 * @return Item
	 */
	@Accessor("item")
	public Item item();

}
