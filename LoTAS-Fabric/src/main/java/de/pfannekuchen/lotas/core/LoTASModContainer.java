package de.pfannekuchen.lotas.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import de.pfannekuchen.lotas.core.utils.ConfigUtils;
import de.pfannekuchen.lotas.core.utils.KeybindsUtils;
import de.pfannekuchen.lotas.core.utils.TextureYoinker;
import de.pfannekuchen.lotas.mods.TickrateChangerMod;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

public class LoTASModContainer implements ModInitializer {

	public static Identifier shield;
	
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
	
    public static void loadShields() {	
		String uuid = MinecraftClient.getInstance().getSession().getProfile().getId().toString();
		
		try {
			URL url = new URL("https://raw.githubusercontent.com/ScribbleLP/MC-TASTools/1.12.2/shields/shieldnames.txt");
			BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
			String line = reader.readLine();
			while (line != null) {
				if (line.split(":")[0].equalsIgnoreCase(uuid)) {
					LoTASModContainer.shield = TextureYoinker.downloadShield(uuid, new URL("https://raw.githubusercontent.com/ScribbleLP/MC-TASTools/1.12.2/shields/" + line.split(":")[1]).openStream());
					return;
				}
				line = reader.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    
}
