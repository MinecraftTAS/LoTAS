package de.pfannekuchen.lotas;

import static rlog.RLogAPI.instantiate;
import static rlog.RLogAPI.logDebug;
import static rlog.RLogAPI.logError;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.List;

import org.apache.commons.io.FileUtils;

import de.pfannekuchen.lotas.config.ConfigManager;
import de.pfannekuchen.lotas.gui.GuiSeedList;
import de.pfannekuchen.lotas.gui.GuiSeedList.SeedEntry;
import de.pfannekuchen.lotas.gui.InfoGui;
import de.pfannekuchen.lotas.hotkeys.Hotkeys;
import de.pfannekuchen.lotas.manipulation.WorldManipulation;
import de.pfannekuchen.lotas.savestates.LoadstatePacket;
import de.pfannekuchen.lotas.savestates.LoadstatePacketHandler;
import de.pfannekuchen.lotas.savestates.SavestatePacket;
import de.pfannekuchen.lotas.savestates.SavestatePacketHandler;
import de.pfannekuchen.lotas.savestates.motion.MotionPacket;
import de.pfannekuchen.lotas.savestates.motion.MotionPacketHandler;
import de.pfannekuchen.lotas.savestates.playerloading.SavestatePlayerLoadingPacket;
import de.pfannekuchen.lotas.savestates.playerloading.SavestatePlayerLoadingPacketHandler;
import de.pfannekuchen.lotas.tickratechanger.TickrateChanger;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

@Mod(acceptedMinecraftVersions = "1.12.2", canBeDeactivated = false, clientSideOnly = true, modid = "lotas", name = "LoTAS", version = "${version}")
public class LoTASModContainer {

	/*
	 *
	 * This is the Mod Container. It manages forge stuff like loading configs and registering events
	 *
	 */
	
	public static int tutorialState = -1;
	public static int blazePearlState = 0;
	public static volatile boolean playSound = false;
	
	public static SimpleNetworkWrapper NETWORK;
	
	static {
		try {
			instantiate();
		} catch (Exception e) {
			e.printStackTrace();
			FMLCommonHandler.instance().exitJava(29, true);
		}
	}
	
	@EventHandler
	public void preinit(FMLPreInitializationEvent e) {
		logDebug("[PreInit] Initializing Configuration");
		ConfigManager.init(new Configuration(e.getSuggestedConfigurationFile()));
		
		logDebug("[PreInit] Registering Events");
		MinecraftForge.EVENT_BUS.register(new TickrateChanger()); logDebug("[PreInit] Registered TickrateChanger Events");
		MinecraftForge.EVENT_BUS.register(new WorldManipulation()); logDebug("[PreInit] Registered World Manipulation Events");
		MinecraftForge.EVENT_BUS.register(new InfoGui()); logDebug("[PreInit] Registered Info Gui Events");
		
		logDebug("[PreInit] Setting up Networking");
		NETWORK= NetworkRegistry.INSTANCE.newSimpleChannel("lotas");
		int i=-1;
		NETWORK.registerMessage(SavestatePlayerLoadingPacketHandler.class, SavestatePlayerLoadingPacket.class, i++, Side.CLIENT);
		NETWORK.registerMessage(MotionPacketHandler.class, MotionPacket.class, i++, Side.SERVER);
		NETWORK.registerMessage(SavestatePacketHandler.class, SavestatePacket.class, i++, Side.SERVER);
		NETWORK.registerMessage(SavestatePacketHandler.class, SavestatePacket.class, i++, Side.CLIENT);
		NETWORK.registerMessage(LoadstatePacketHandler.class, LoadstatePacket.class, i++, Side.SERVER);
		NETWORK.registerMessage(LoadstatePacketHandler.class, LoadstatePacket.class, i++, Side.CLIENT);
		
		logDebug("[PreInit] Registering Keybindings");
		Hotkeys.registerKeybindings();
		
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
			URL url = new URL("http://mgnet.work/seeds/seeds.txt");
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
			
			GuiSeedList.seeds.add(new SeedEntry(name, description, seed, c));
			logDebug("[PreInit] Seed found: " + name + " (" + seed + ") ");
			c++;
		}
		logDebug("[PreInit] Finished with " + c + " Seeds found and added to GuiSeedList");
	}
	
}
