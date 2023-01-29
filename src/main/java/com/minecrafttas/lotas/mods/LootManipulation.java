package com.minecrafttas.lotas.mods;

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
		System.out.println("TEST?");
		LootTables lootTables = this.mcserver.getLootTables();
		for (ResourceLocation loc : lootTables.getIds()) {
			LootTable table = lootTables.get(loc);
			System.out.println("Table " + loc.getPath());
			for (LootPool pool : ((AccessorLootTable) table).pools()) {
				System.out.println("\tPool:");
				for (LootPoolEntryContainer entry : ((AccessorLootPool) pool).entries()) {
					if (entry instanceof LootItem) {
						System.out.println("\t\t" + ((AccessorLootItem) entry).item().toString());
					} else if (entry instanceof AlternativesEntry) {
						System.out.println("\t\tdrops too complicated");
					} else if (entry instanceof TagEntry) {
						System.out.println("\t\tTag:");
						for (Item item : ((AccessorTagEntry) entry).tag().getValues()) {
							System.out.println("\t\t\t" + item.toString());
						}
					} else if (entry instanceof LootTableReference) {
						System.out.println("\t\t\u2937 " + ((AccessorLootTableReference) entry).name().getPath());
					} else if (entry instanceof EmptyLootItem) {
						System.out.println("\t\t");
					} else {
						throw new RuntimeException("Loot Entry not implemented: " + entry.getClass().getSimpleName());
					}
				}
			}
		}
	}

}
