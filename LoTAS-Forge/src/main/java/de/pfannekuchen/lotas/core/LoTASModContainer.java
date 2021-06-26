package de.pfannekuchen.lotas.core;

import net.minecraft.client.renderer.IImageBuffer;
import java.io.IOException;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.Minecraft;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FileUtils;
import de.pfannekuchen.lotas.core.utils.ConfigUtils;
import de.pfannekuchen.lotas.core.utils.EventUtils;
import de.pfannekuchen.lotas.core.utils.KeybindsUtils;
import de.pfannekuchen.lotas.gui.GuiSeedList.SeedListExtended;
import de.pfannekuchen.lotas.gui.GuiSeedList.SeedListExtended.SeedEntry;
import de.pfannekuchen.lotas.gui.HudSettings;
import de.pfannekuchen.lotas.mods.TickrateChangerMod;
import de.pfannekuchen.lotas.taschallenges.ChallengeMap;
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
	
	/** Minecraft Version */
	public static String version = ForgeVersion.mcVersion;
	
	/**
	 * Called by MinecraftForge whenever our Mod gets initialized
	 * @param e FMLInitializationEvent provided by MinecraftForge
	 */
	@EventHandler public void onInit(FMLInitializationEvent e) {
		loadShields(); // load custom shields
		KeybindsUtils.registerKeybinds(); // register keybinds
		/* Load the Settings-Hud. */
		try {
			HudSettings.load(); // This goes first.. muhahahahaha
			// add a hook whenever the JVM stops to save the hud
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
			/* Defaults whenever the file wasn't found */
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
				HudSettings.save(); // save defaults to file
			} catch (IOException e420) {
				e420.printStackTrace();
			}
		}
	}
	
	/**
	 * Before the Mod Loader launches the Minecraft Gui
	 * @param e FMLPreInitializationEvent provided by MinecraftForge
	 */
	@EventHandler public void onPreInit(FMLPreInitializationEvent e) {
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
				BufferedReader stream = new BufferedReader(new InputStreamReader(new URL("http://mgnet.work/taschallenges/maps" + version + ".txt").openStream()));
				int maps = Integer.parseInt(stream.readLine().charAt(0) + ""); // First line contains number of maps
				for (int i = 0; i < maps; i++) {
					ChallengeMap map = new ChallengeMap();
					// read map from stream using it's format
					map.displayName = stream.readLine();
					map.name = stream.readLine();
					map.description = stream.readLine();
					map.map = new URL("http://mgnet.work/taschallenges/" + stream.readLine());
					int board = Integer.parseInt(stream.readLine().charAt(0) + "");
					map.leaderboard = new String[board];
					for (int j = 0; j < board; j++) {
						map.leaderboard[j] = stream.readLine();
					}
					
					// load the icon for the tas challenge
					ResourceLocation loc = new ResourceLocation("maps", map.name);
					ThreadDownloadImageData dw = new ThreadDownloadImageData((File) null, "http://mgnet.work/taschallenges/" + map.name + ".png", null, new ImageBufferDownload());
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
	 * Method that loads the custom shields for LoTAS
	 * @author Scribble
	 */
	public void loadShields() {	
		// load shields from server
		//#if MC>=10900
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
		LoTASModContainer.shield = net.minecraft.client.renderer.BannerTextures.SHIELD_BASE_TEXTURE;
		//#endif
	}

	/**
	 * Method that loads the seeds file from the server
	 * @throws Exception Throws whenever something fails horribly
	 */
	public void loadSeeds() throws Exception {
		// Load the file
		File file = new File("seeddata.txt");
		try {
			URL url = new URL("http://mgnet.work/seeds/seeds" + version + ".txt");
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
				ThreadDownloadImageData dw = new ThreadDownloadImageData((File) null, "http://mgnet.work/seeds/" + seed + ".png", null, new ImageBufferDownload());
				Minecraft.getMinecraft().getTextureManager().loadTexture(entry.loc, dw);
			}).start();
			SeedListExtended.seeds.add(entry);
			c++;
		}
	}
	
}
