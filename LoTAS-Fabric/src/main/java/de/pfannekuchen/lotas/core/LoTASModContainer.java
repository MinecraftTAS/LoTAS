package de.pfannekuchen.lotas.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.List;

import org.apache.commons.io.FileUtils;

import de.pfannekuchen.lotas.core.utils.ConfigUtils;
import de.pfannekuchen.lotas.core.utils.KeybindsUtils;
import de.pfannekuchen.lotas.core.utils.TextureYoinker;
import de.pfannekuchen.lotas.gui.HudSettings;
import de.pfannekuchen.lotas.gui.SeedListScreen;
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
			HudSettings.load(); // This goes first.. muhahahahaha
			Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {
						HudSettings.save();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}));
		} catch (IOException e3) {
			HudSettings.p = new java.util.Properties();
			HudSettings.p.setProperty("XYZ_visible", "true");
			HudSettings.p.setProperty("XYZPRECISE_visible", "false");
			HudSettings.p.setProperty("CXZ_visible", "false");
			HudSettings.p.setProperty("WORLDSEED_visible", "false");
			HudSettings.p.setProperty("RNGSEEDS_visible", "false");
			HudSettings.p.setProperty("FACING_visible", "false");
			HudSettings.p.setProperty("TICKS_visible", "false");
			HudSettings.p.setProperty("TICKRATE_visible", "false");
			HudSettings.p.setProperty("SAVESTATECOUNT_visible", "false");
			HudSettings.p.setProperty("TRAJECTORIES_visible", "false");
			
			HudSettings.p.setProperty("XYZ_x", "0");
			HudSettings.p.setProperty("XYZPRECISE_x", "0");
			HudSettings.p.setProperty("CXZ_x", "0");
			HudSettings.p.setProperty("WORLDSEED_x", "0");
			HudSettings.p.setProperty("RNGSEEDS_x", "0");
			HudSettings.p.setProperty("FACING_x", "0");
			HudSettings.p.setProperty("TICKS_x", "0");
			HudSettings.p.setProperty("TICKRATE_x", "0");
			HudSettings.p.setProperty("SAVESTATECOUNT_x", "0");
			HudSettings.p.setProperty("TRAJECTORIES_x", "0");
			
			HudSettings.p.setProperty("XYZ_y", "0");
			HudSettings.p.setProperty("XYZPRECISE_y", "0");
			HudSettings.p.setProperty("CXZ_y", "0");
			HudSettings.p.setProperty("WORLDSEED_y", "0");
			HudSettings.p.setProperty("RNGSEEDS_y", "0");
			HudSettings.p.setProperty("FACING_y", "0");
			HudSettings.p.setProperty("TICKS_y", "0");
			HudSettings.p.setProperty("TICKRATE_y", "0");
			HudSettings.p.setProperty("SAVESTATECOUNT_y", "0");
			HudSettings.p.setProperty("TRAJECTORIES_y", "0");
			
			HudSettings.p.setProperty("XYZ_hideRect", "false");
			HudSettings.p.setProperty("XYZPRECISE_hideRect", "false");
			HudSettings.p.setProperty("CXZ_hideRect", "false");
			HudSettings.p.setProperty("WORLDSEED_hideRect", "false");
			HudSettings.p.setProperty("RNGSEEDS_hideRect", "false");
			HudSettings.p.setProperty("FACING_hideRect", "false");
			HudSettings.p.setProperty("TICKS_hideRect", "false");
			HudSettings.p.setProperty("TICKRATE_hideRect", "false");
			HudSettings.p.setProperty("SAVESTATECOUNT_hideRect", "false");
			HudSettings.p.setProperty("TRAJECTORIES_hideRect", "false");
			
			try {
				HudSettings.save();
			} catch (IOException e420) {
				e420.printStackTrace();
			}
		}
		try {
			loadSeeds();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		try {
			ConfigUtils.init(new File(MinecraftClient.getInstance().runDirectory, "lotas.properties"));
			if (ConfigUtils.getBoolean("tools", "saveTickrate"))
				TickrateChangerMod.updateTickrate(ConfigUtils.getInt("hidden", "tickrate"));
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
					break;
				}
				line = reader.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		//		AccessorModelLoader.setShieldBase(new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEX, new Identifier("lotas","pan_cake")));
	}

	/**
	 * Loads a list of seeds together with preview images from <a href="http://mgnet.work/seeds/">mgnet.work/seeds/seedsX.XX.X.txt</a> and creates a List
	 * @throws IOException
	 */
	public void loadSeeds() throws Exception {
		File file = new File("seeddata.txt");
		try {
			URL url = new URL("http://mgnet.work/seeds/seeds1.14.4.txt");
			URLConnection conn = url.openConnection();
			conn.setReadTimeout(5000);
			file.createNewFile();
			FileUtils.copyInputStreamToFile(conn.getInputStream(), file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<String> strings = Files.readAllLines(file.toPath());
		for (String line : strings) {
			String seed = line.split(":")[0];
			String name = line.split(":")[1];
			String description = line.split(":")[2];

			SeedListScreen.seeds.add(new SeedListScreen.Seed(seed, name, description));
		}
	}

}
