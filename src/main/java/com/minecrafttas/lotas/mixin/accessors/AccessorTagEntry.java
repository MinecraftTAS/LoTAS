package com.minecrafttas.lotas.mixin.accessors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.tags.TagKey; // @LootImportTag
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.entries.TagEntry;

/**
 * This mixin accessor makes the loot item in a tag entry accessible.
 * @author Pancake
 */
@Mixin(TagEntry.class)
public interface AccessorTagEntry {

	/**
	 * This Accessor opens the private field containing the list of tags
	 * @return Tag
	 */
	@Accessor("tag")
	public TagKey<Item> tag(); // @LootTag

}
