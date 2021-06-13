package de.pfannekuchen.lotas.core;

import java.io.File;
import java.io.IOException;

import de.pfannekuchen.lotas.core.utils.ConfigUtils;
import de.pfannekuchen.lotas.core.utils.KeybindsUtils;
import de.pfannekuchen.lotas.mods.TickrateChangerMod;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;

public class LoTASModContainer implements ModInitializer {

	@Override
	public void onInitialize() {
		KeybindsUtils.registerKeybinds();
		try {
			ConfigUtils.init(new File(MinecraftClient.getInstance().runDirectory, "lotas.properties"));
			if (ConfigUtils.getBoolean("tools", "saveTickrate")) TickrateChangerMod.updateTickrate(ConfigUtils.getInt("hidden", "tickrate"));
		} catch (IOException e) {
			System.err.println("Couldn't load Configuration");
			e.printStackTrace();
		}
	}
}
