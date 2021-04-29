package de.pfannekuchen.lotas;

import static rlog.RLogAPI.instantiate;
import static rlog.RLogAPI.logDebug;
import static rlog.RLogAPI.logError;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import de.pfannekuchen.lotas.challenges.ChallengeMap;
import de.pfannekuchen.lotas.config.ConfigManager;
import de.pfannekuchen.lotas.gui.GuiSeedList;
import de.pfannekuchen.lotas.gui.GuiSeedList.SeedEntry;
import de.pfannekuchen.lotas.gui.InfoGui;
import de.pfannekuchen.lotas.manipulation.WorldManipulation;
import de.pfannekuchen.lotas.tickratechanger.TickrateChanger;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;

@Mod(acceptedMinecraftVersions = "1.8.9", canBeDeactivated = false, modid = "lotas", name = "LoTAS", version = "${version}")
public class LoTASModContainer {
	
	/*
	 *
	 * This is the Mod Container. It manages forge stuff like loading configs and registering events
	 *
	 */
	
	public static volatile boolean playSound = false;
	public static final List<ChallengeMap> maps = new ArrayList<ChallengeMap>();
	
	static {
		try {
			instantiate();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(29);
		}
	}
	
	
	@EventHandler
	public void init(FMLInitializationEvent e) {
		Binds.registerKeybindings();
	}
	
	@EventHandler
	public void preinit(final FMLPreInitializationEvent e) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				logDebug("[PreInit] Initializing Configuration");
				ConfigManager.init(new Configuration(e.getSuggestedConfigurationFile()));
			
				logDebug("[PreInit] Downloading Seeds");
				try {
					loadSeeds();
				} catch (Exception e1) {
					logDebug("[PreInit] Reading Seeds File failed");
					logError(e1, "Couldn't read Seeds File #0");
					e1.printStackTrace();
				}
				
				if (ConfigManager.getBoolean("tools", "saveTickrate")) {
					logDebug("[PreInit] Loading Tickrate");
					TickrateChanger.index = ConfigManager.getInt("hidden", "tickrate");
					TickrateChanger.updateTickrate(TickrateChanger.ticks[TickrateChanger.index]);
				}
				
				logDebug("[PreInit] Downloading TAS Challenge Maps");
				try {
					BufferedReader stream = new BufferedReader(new InputStreamReader(new URL("http://mgnet.work/taschallenges/maps1.7.10.txt").openStream()));
					int maps = Integer.parseInt(stream.readLine().charAt(0) + "");
					for (int i = 0; i < maps; i++) {
						ChallengeMap map = new ChallengeMap();
						
						map.displayName = stream.readLine();
						map.name = stream.readLine();
						map.description = stream.readLine();
						map.map = new URL("http://mgnet.work/taschallenges/" + stream.readLine());
						int board = Integer.parseInt(stream.readLine().charAt(0) + "");
						map.leaderboard = new String[board];
						for (int j = 0; j < board; j++) {
							map.leaderboard[j] = stream.readLine();
						}
						
						LoTASModContainer.maps.add(map);
						
						stream.readLine(); // Empty
					}
					stream.close();
				} catch (Exception e1) {
					logError(e1, "Couldn't download Challenge Maps #6");
				}
			}
		}).start();
		
		logDebug("[PreInit] Registering Events");
		MinecraftForge.EVENT_BUS.register(new TickrateChanger()); logDebug("[PreInit] Registered TickrateChanger Events");
		MinecraftForge.EVENT_BUS.register(new WorldManipulation()); logDebug("[PreInit] Registered World Manipulation Events");
		MinecraftForge.EVENT_BUS.register(new InfoGui()); logDebug("[PreInit] Registered Info Gui Events");
		 
		logDebug("[PreInit] Registering Keybindings");
		
	}
	
	/**
	 * Loads a list of seeds together with preview images from <a href="http://mgnet.work/seeds/">mgnet.work/seeds/</a> and creates a GuiSeedList SeedEntry
	 * @see GuiSeedList
	 * @throws IOException
	 */
	public void loadSeeds() throws Exception {
		File file = new File("seeddata.txt");
		try {
			logDebug("[PreInit] Trying to download seeds.txt from http://mgnet.work/seeds/seeds.txt");
			URL url = new URL("http://mgnet.work/seeds/seeds1.7.10.txt");
			URLConnection conn = url.openConnection();
			conn.setReadTimeout(5000);
			file.createNewFile();
			FileUtils.copyInputStreamToFile(conn.getInputStream(), file);
			logDebug("[PreInit] seeds.txt downloaded to seeddata.txt");
		} catch (Exception e) {
			logDebug("[PreInit] Downloading Seeds failed");
			logError(e, "Seeds.txt Download failed #1");
		}
		logDebug("[PreInit] Reading Local seeddata.txt file");
		List<String> strings = Files.readAllLines(file.toPath());
		int c = 0;
		for (String line : strings) {
			String seed = line.split(":")[0];
			String name = line.split(":")[1];
			String description = line.split(":")[2];
			SeedEntry entry = new SeedEntry(name, description, seed, c);
			GuiSeedList.seeds.add(entry);
			logDebug("[PreInit] Seed found: " + name + " (" + seed + ") ");
			c++;
		}
		logDebug("[PreInit] Finished with " + c + " Seeds found and added to GuiSeedList");
	}
	
}
