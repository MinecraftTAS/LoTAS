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
	private String s = "";
	
	public LootManipulation() {
		super(new ResourceLocation("lotas", "lootmanipulation"));
	}

	@Override
	protected void onServerTick() {
		if (fetch)
			return;
		fetch = true;
		
		// Iterate through all loot tables
		s += "{";
		LootTables lootTables = this.mcserver.getLootTables();
		for (ResourceLocation loc : lootTables.getIds()) {
			LootTable table = lootTables.get(loc);
			// Analyze loot table
			s += "\"" + loc.getPath() + "\":{";
			analyzeTable(table);
			s += "},";
		}
		s += "}";
		System.out.println(s);
	}

	/**
	 * Analyzes a loot table
	 * @param table Loot table
	 */
	private void analyzeTable(LootTable table) {
		// Iterate through all loot pools
		s += "\"pools\":[";
		for (LootPool pool : ((AccessorLootTable) table).pools()) {
			// Analyze loot pool
			s += "{";
			analyzePool(pool);
			s += "},";
		}
		s += "]";
	}
	
	/**
	 * Analyzes a loot pool
	 * @param pool Loot pool
	 */
	private void analyzePool(LootPool pool) {
		// Iterate through all loot entries
		s += "\"entries\":[";
		for (LootPoolEntryContainer entry : ((AccessorLootPool) pool).entries()) {
			// Analyze loot entry
			analyzeEntry(entry);
		}
		s += "]";
	}
	
	/**
	 * Analyzes a loot entry
	 * @param entry Loot Entry
	 */
	private void analyzeEntry(LootPoolEntryContainer entry) {
		if (entry instanceof LootItem) {
			s += "{\"item\":\"" + ((AccessorLootItem) entry).item().toString() + "\"},";
		} else if (entry instanceof AlternativesEntry) {
			s += "{\"first\":[";
			for (LootPoolEntryContainer item : ((AccessorCompositeEntryBase) entry).children())
				analyzeEntry(item);
			s += "]},";
		} else if (entry instanceof TagEntry) {
			s += "{\"any\":[";
			// # 1.19.3
//$$ 			for (net.minecraft.core.Holder<Item> item : net.minecraft.core.registries.BuiltInRegistries.ITEM.getTagOrEmpty(((AccessorTagEntry) entry).tag()))
//$$ 				s += "\"" + item.value().toString() + "\",";
			// # 1.18.2
//$$			for (net.minecraft.core.Holder<Item> item : net.minecraft.core.Registry.ITEM.getTagOrEmpty(((AccessorTagEntry) entry).tag()))
//$$				s += "\"" + item.value().toString() + "\",";
			// # def
//$$ 			for (Item item : ((AccessorTagEntry) entry).tag().getValues())
//$$ 				s += "\"" + item.toString() + "\",";
			// # end
			s += "]},";
		} else if (entry instanceof LootTableReference) {
			s += "{\"reference\":\"" + ((AccessorLootTableReference) entry).name().getPath() + "\"},";
		} else if (entry instanceof EmptyLootItem) {
//			System.out.println("nothing");
		} else {
			throw new RuntimeException("Loot Entry not implemented: " + entry.getClass().getSimpleName());
		}
	}
	
}
