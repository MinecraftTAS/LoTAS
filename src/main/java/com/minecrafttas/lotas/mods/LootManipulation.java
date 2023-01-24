package com.minecrafttas.lotas.mods;

import com.minecrafttas.lotas.mixin.accessors.AccessorLootTable;
import com.minecrafttas.lotas.system.ModSystem.Mod;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;

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
			System.out.println("Loot Table " + loc.getPath());
			for (LootPool pool : ((AccessorLootTable) table).pools()) {
				System.out.println("\tTODO: Pool");
			}
		}
	}

}
