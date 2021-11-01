package de.pfannekuchen.lotas.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.io.FileUtils;

import de.pfannekuchen.lotas.core.utils.ConfigUtils;
import de.pfannekuchen.lotas.core.utils.EventUtils;
import de.pfannekuchen.lotas.core.utils.KeybindsUtils;
import de.pfannekuchen.lotas.gui.GuiSeedList.SeedListExtended;
import de.pfannekuchen.lotas.gui.GuiSeedList.SeedListExtended.SeedEntry;
import de.pfannekuchen.lotas.gui.InfoHud;
import de.pfannekuchen.lotas.mods.TickrateChangerMod;
import de.pfannekuchen.lotas.taschallenges.ChallengeMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * Forge Mod Entry Point, initializes stuff here
 * 
 * @author Pancake
 * @since v1.0
 * @version v2.0
 */
@Mod(acceptedMinecraftVersions = "@MC_VERSION@", version = "@MOD_VERSION@", modid = "lotas", clientSideOnly = true, canBeDeactivated = false, name = "LoTAS")
public class LoTASModContainer {

	/** List of TAS Challenges */
	public static final List<ChallengeMap> maps = new ArrayList<>();
	/** Custom shield */
	public static ResourceLocation shield;
	/** World spawn offset X */
	public static int offsetX = 0;
	/** World spawn offset Z */
	public static int offsetZ = 0;
	/** Only ever InfoHud instance */
	public static InfoHud hud;

	/** Minecraft Version */
	public static String version = ForgeVersion.mcVersion;
	
	/**
	 * Called by MinecraftForge whenever our Mod gets initialized
	 * 
	 * @param e FMLInitializationEvent provided by MinecraftForge
	 */
	@EventHandler
	public void onInit(FMLInitializationEvent e) {
		loadShieldsMCTAS(); // load custom shields
		KeybindsUtils.registerKeybinds(); // register keybinds
		hud = new InfoHud(); // load info gui
	}

	/**
	 * Before the Mod Loader launches the Minecraft Gui
	 * 
	 * @param e FMLPreInitializationEvent provided by MinecraftForge
	 */
	@EventHandler
	public void onPreInit(FMLPreInitializationEvent e) {
		new Thread(() -> {
			// Load configuration with suggested config file
			ConfigUtils.init(new Configuration(e.getSuggestedConfigurationFile()));

			// Load all seeds for the seeds gui
			try {
				loadSeeds();
			} catch (Exception e1) {
				e1.printStackTrace();
			}

			// Load the tickrate if it's supposed to be saved
			if (ConfigUtils.getBoolean("tools", "saveTickrate")) {
				TickrateChangerMod.index = ConfigUtils.getInt("hidden", "tickrate");
				TickrateChangerMod.updateTickrate(TickrateChangerMod.ticks[TickrateChangerMod.index]);
			}

			// Try to download all tas challenge maps
			try {
				BufferedReader stream = new BufferedReader(new InputStreamReader(new URL("https://data.mgnet.work/lotas/taschallenges/" + version + ".txt").openStream()));
				int maps = Integer.parseInt(stream.readLine().charAt(0) + ""); // First line contains number of maps
				for (int i = 0; i < maps; i++) {
					ChallengeMap map = new ChallengeMap();
					// read map from stream using it's format
					map.displayName = stream.readLine();
					map.name = stream.readLine();
					map.description = stream.readLine();
					map.map = new URL("https://data.mgnet.work/lotas/taschallenges/maps/" + stream.readLine());
					int board = Integer.parseInt(stream.readLine().charAt(0) + "");
					map.leaderboard = new String[board];
					for (int j = 0; j < board; j++) {
						map.leaderboard[j] = stream.readLine();
					}

					// load the icon for the tas challenge
					ResourceLocation loc = new ResourceLocation("maps", map.name);
					ThreadDownloadImageData dw = new ThreadDownloadImageData((File) null, "https://data.mgnet.work/lotas/taschallenges/images/" + map.name + ".png", null, new ImageBufferDownload());
					Minecraft.getMinecraft().getTextureManager().loadTexture(loc, dw);
					map.resourceLoc = loc.getResourcePath();

					LoTASModContainer.maps.add(map);

					// one empty line for commenters purposes
					stream.readLine(); // Empty
				}
				stream.close();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}).start();

		// register forge events
		MinecraftForge.EVENT_BUS.register(new EventUtils());
	}

	/**
	 * Method that loads the custom shields for LoTAS from the <a href=
	 * "https://github.com/ScribbleLP/MC-TASTools/tree/1.12.2/shields">TASTools<a/>
	 * repository
	 * 
	 * @author Scribble
	 */
	public void loadShieldsTASTools() {
		// load shields from server
		// #if MC>=10900
		String uuid = Minecraft.getMinecraft().getSession().getProfile().getId().toString();

		try {
			// read file that contains all uuids for shields
			URL url = new URL("https://raw.githubusercontent.com/ScribbleLP/MC-TASTools/1.12.2/shields/shieldnames.txt");
			BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
			String line = reader.readLine();
			while (line != null) {
				if (line.split(":")[0].equalsIgnoreCase(uuid)) {
					// load shield texture from uuid and return
					ThreadDownloadImageData thread = new ThreadDownloadImageData(null, "https://raw.githubusercontent.com/ScribbleLP/MC-TASTools/1.12.2/shields/" + line.split(":")[1], null, new IImageBuffer() {

						@Override
						public void skinAvailable() {

						}

						@Override
						public java.awt.image.BufferedImage parseUserSkin(java.awt.image.BufferedImage image) {
							return image;
						}
					});
					LoTASModContainer.shield = new ResourceLocation("shield.png");
					Minecraft.getMinecraft().getTextureManager().loadTexture(LoTASModContainer.shield, thread);
					return;
				}
				line = reader.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		// when there is no custom shield, set the texture to the normal one
		LoTASModContainer.shield = new ResourceLocation("lotas", "misc/bottleshield.png");
		// #endif
	}

	/**
	 * Method that loads the custom shields for LoTAS from
	 * <a href="https://minecrafttas.com">minecrafttas.com</a>
	 * 
	 * @author Scribble
	 */
	public void loadShieldsMCTAS() {
		// load shields from server
		// #if MC>=10900
		String uuid = Minecraft.getMinecraft().getSession().getProfile().getId().toString();

		String urlname = "https://minecrafttas.com/" + uuid;
		URL url = null;

		try {
			url = new URL(urlname);
			HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
			connection.setReadTimeout(5000);
			connection.setInstanceFollowRedirects(false);
			if (connection.getResponseCode() == HttpsURLConnection.HTTP_MOVED_PERM) {
				urlname = connection.getHeaderField("Location");
				// load shield texture from uuid and return
				ThreadDownloadImageData thread = new ThreadDownloadImageData(null, urlname, null, new IImageBuffer() {

					@Override
					public void skinAvailable() {

					}

					@Override
					public java.awt.image.BufferedImage parseUserSkin(java.awt.image.BufferedImage image) {
						return image;
					}
				});
				LoTASModContainer.shield = new ResourceLocation("shield.png");
				Minecraft.getMinecraft().getTextureManager().loadTexture(LoTASModContainer.shield, thread);
				return;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		// when there is no custom shield, set the texture to the normal one
		LoTASModContainer.shield = new ResourceLocation("lotas", "misc/bottleshield.png");
		// #endif
	}

	/**
	 * Method that loads the seeds file from the server
	 * 
	 * @throws Exception Throws whenever something fails horribly
	 */
	public void loadSeeds() throws Exception {
		// Load the file
		File file = new File("seeddata.txt");
		try {
			URL url = new URL("https://data.mgnet.work/lotas/seeds/" + version + ".txt");
			URLConnection conn = url.openConnection();
			conn.setReadTimeout(5000);
			file.createNewFile();
			FileUtils.copyInputStreamToFile(conn.getInputStream(), file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Parse the file
		List<String> strings = Files.readAllLines(file.toPath());
		int c = 0;
		for (String line : strings) {
			String seed = line.split(":")[0]; // seed is before first :
			String name = line.split(":")[1]; // etc...
			String description = line.split(":")[2];
			SeedEntry entry = new SeedEntry(name, description, seed, c);
			// load icon and add seed to list
			new Thread(() -> {
				entry.loc = new ResourceLocation("seeds", seed);
				ThreadDownloadImageData dw = new ThreadDownloadImageData((File) null, "https://data.mgnet.work/lotas/seeds/images/" + seed + ".png", null, new ImageBufferDownload());
				Minecraft.getMinecraft().getTextureManager().loadTexture(entry.loc, dw);
			}).start();
			SeedListExtended.seeds.add(entry);
			c++;
		}
	}
}
