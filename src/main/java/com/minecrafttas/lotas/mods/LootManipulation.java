package com.minecrafttas.lotas.mods;

import com.minecrafttas.lotas.mixin.accessors.AccessorCompositeEntryBase;
import com.minecrafttas.lotas.mixin.accessors.AccessorLootItem;
import com.minecrafttas.lotas.mixin.accessors.AccessorLootPool;
import com.minecrafttas.lotas.mixin.accessors.AccessorLootTable;
import com.minecrafttas.lotas.mixin.accessors.AccessorLootTableReference;
import com.minecrafttas.lotas.mixin.accessors.AccessorTagEntry;
import com.minecrafttas.lotas.system.ModSystem.Mod;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraft.world.level.storage.loot.entries.TagEntry;

public class LootManipulation extends Mod {		

	private boolean fetch;
	
	public LootManipulation() {
		super(new ResourceLocation("lotas", "lootmanipulation"));
	}

	@Override
	protected void onServerTick() {
		if (fetch)
			return;
		fetch = true;
		
		// Iterate through all loot tables
		LootTables lootTables = this.mcserver.getLootTables();
		for (ResourceLocation loc : lootTables.getIds()) {
			LootTable table = lootTables.get(loc);
			// Analyze loot table
			System.out.println("Loot Table: " + loc.getPath());
			analyzeTable(table);
		}
	}

	/**
	 * Analyzes a loot table
	 * @param table Loot table
	 */
	private void analyzeTable(LootTable table) {
		// Iterate through all loot pools
		for (LootPool pool : ((AccessorLootTable) table).pools()) {
			// Analyze loot pool
			System.out.println("Pool:");
			analyzePool(pool);
		}
	}
	
	/**
	 * Analyzes a loot pool
	 * @param pool Loot pool
	 */
	private void analyzePool(LootPool pool) {
		// Iterate through all loot entries
		for (LootPoolEntryContainer entry : ((AccessorLootPool) pool).entries()) {
			// Analyze loot entry
			analyzeEntry(entry);
		}
	}
	
	/**
	 * Analyzes a loot entry
	 * @param entry Loot Entry
	 */
	private void analyzeEntry(LootPoolEntryContainer entry) {
		if (entry instanceof LootItem) {
			System.out.println(((AccessorLootItem) entry).item().toString());
		} else if (entry instanceof AlternativesEntry) {
			System.out.println("First Entry:");
			for (LootPoolEntryContainer item : ((AccessorCompositeEntryBase) entry).children())
				analyzeEntry(item);
		} else if (entry instanceof TagEntry) {
			System.out.println("One Item:");
			for (Item item : ((AccessorTagEntry) entry).tag().getValues())
				System.out.println(item.toString());
		} else if (entry instanceof LootTableReference) {
			System.out.println("\u2937 " + ((AccessorLootTableReference) entry).name().getPath());
		} else if (entry instanceof EmptyLootItem) {
			System.out.println("nothing");
		} else {
			throw new RuntimeException("Loot Entry not implemented: " + entry.getClass().getSimpleName());
		}
	}
}
