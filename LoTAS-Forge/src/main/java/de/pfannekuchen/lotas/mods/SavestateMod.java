package de.pfannekuchen.lotas.mods;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.lwjgl.input.Mouse;

import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.core.utils.EventUtils.Timer;
import de.pfannekuchen.lotas.mixin.accessors.AccessorAnvilChunkLoader;
import de.pfannekuchen.lotas.mixin.render.gui.MixinGuiIngameMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import net.minecraft.world.gen.ChunkProviderServer;

/**
 * Saves the player velocity and makes a copy of the world in a seperate folder.
 * That world can also be loaded again
 * @since v1.0
 * @version v1.1 Added loadless savestates and multithreading
 * @author Pancake
 */
public class SavestateMod {

	/** Temporary Variable to request loading the velocity */
	public static boolean applyVelocity;
	public static double motionX;
	public static double motionY;
	public static double motionZ;

	/** Temporary variable to indicate that the "Savestate Done" Label should show up */
	public static boolean showSavestateDone;
	/** Temporary variable to indicate that the "Loadstate Done" Label should show up */
	public static boolean showLoadstateDone;
	/** Temporary variable to indicate that the Labels above should fade out */
	public static long timeTitle;

	/** Turns off rendering to fake loadless savestates */
	public static boolean isLoading;
	
	public static boolean isSaving;

	/**
	 * Returns the motion of the player, and the current time of the timer as a string
	 * @return Data as String
	 */
	public static final String generateSavestateFile() {
		final EntityPlayerSP p = MCVer.player(Minecraft.getMinecraft());
		return p.motionX + ":" + p.motionY + ":" + p.motionZ + ":" + Timer.ticks;
	}

	/**
	 * Creates a savestate in .minecraft/savestates/
	 * @throws IOException Throws when the World was locked
	 */
	public static void savestate(String name) {
		final String data = generateSavestateFile();
		final Minecraft mc = Minecraft.getMinecraft();
		
		isSaving = true;

		final MinecraftServer server = mc.getIntegratedServer();
		
		float tickratesaved=TickrateChangerMod.tickrateServer;
		TickrateChangerMod.updateTickrate(20);
		
		// save the world
		//#if MC>=10900
		server.getPlayerList().saveAllPlayerData();
		server.saveAllWorlds(false);
		AIManipMod.save();
		//#else
//$$ 		server.getConfigurationManager().saveAllPlayerData();
		//#endif
		for (WorldServer world : MCVer.getWorlds(server)) {
			try {
				world.saveAllChunks(true, null);
			} catch (MinecraftException e) {
				e.printStackTrace();
			}
		}
		
		// don't save the world
		for (final WorldServer worldserver : MCVer.getWorlds(server)) {
			worldserver.disableLevelSaving = true;
		}
		for(WorldServer world:MCVer.getWorlds(server)) {
			AnvilChunkLoader chunkloader=(AnvilChunkLoader) ((ChunkProviderServer) world.getChunkProvider()).chunkLoader;
			while(((AccessorAnvilChunkLoader) chunkloader).chunksToSave().size() > 0) {

			}
		}

		// create a new thread that copies the world
		new Thread(() -> {
			// find next savestate directory
			String worldName = server.getFolderName();
			File worldDir = new File(mc.mcDataDir, "saves/" + worldName);
			File savestatesDir = new File(mc.mcDataDir, "saves/savestates/");

			if (!savestatesDir.exists()) savestatesDir.mkdir();

			int existingSavestates = savestatesDir.listFiles((d, s) -> {
				return s.startsWith(worldName + "-Savestate");
			}).length;

			File savestateDir = new File(savestatesDir, worldName + "-Savestate" + (existingSavestates + 1));

			// copy the world
			try {
				FileUtils.copyDirectory(worldDir, savestateDir);
				com.google.common.io.Files.write(data.getBytes(), new File(savestateDir, "savestate.dat"));
				com.google.common.io.Files.write((name == null) ? new SimpleDateFormat("MM-dd-yyyy HH.mm.ss").format(new Date()).getBytes() : name.getBytes(), new File(savestateDir, "lotas.dat"));
			} catch (IOException e) {
				e.printStackTrace();
			}

			// update the tracking file
			try {
				TrackerFile.increaseSavestates(savestatesDir, worldName);
			} catch (IOException e) {
				e.printStackTrace();
			}

			// save worlds again
			for (WorldServer worldserver : MCVer.getWorlds(server)) {
				worldserver.disableLevelSaving = false;
			}
		}).start();

		// show the label, that the savestates is done.
		showSavestateDone = true;
		timeTitle = System.currentTimeMillis();
		isSaving = false;
		System.gc();
		
		TickrateChangerMod.updateTickrate(tickratesaved);
	}

	/**
	 * Closes the server and loads the latest savestate in .minecraft/savestates/
	 * @throws IOException Throws when the World was locked
	 */
	public static void loadstate(int number) {
		if (!hasSavestate()) 
			return; // check for a savestates

		// save mouse coordinates to avoid mouse madness when not in fullscreen
		int x = Mouse.getX();
		int y = Mouse.getY();

		isLoading = true; // turn off rendering
		
		// Store the tickrate before the loadstate
		float tickratesaved=TickrateChangerMod.tickrateServer;
		TickrateChangerMod.updateTickrate(20);
		
		// stop the server without saving the world
		Minecraft mc = Minecraft.getMinecraft();
		IntegratedServer server = mc.getIntegratedServer();
		for (WorldServer worldserver : MCVer.getWorlds(server)) {
			worldserver.disableLevelSaving = true;
		}
		Minecraft.stopIntegratedServer();

		// load the world
		String worldName = server.getFolderName();
		File worldDir = new File(mc.mcDataDir, "saves/" + worldName);
		File savestatesDir = new File(mc.mcDataDir, "saves/savestates/");

		int existingSavestates = savestatesDir.listFiles((d, s) -> {
			return s.startsWith(worldName + "-Savestate");
		}).length;

		if (number != -1) existingSavestates = number;

		// Deleting the savestate dir
		File savestateDir = new File(savestatesDir, worldName + "-Savestate" + (existingSavestates));
		try {
			FileUtils.deleteDirectory(worldDir);
		} catch (IOException e) {
			System.out.println("Failed to delete the current world. Trying to salvage it...");
			e.printStackTrace();
			try {
				Thread.sleep(20L);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			try {
				FileUtils.copyDirectory(savestateDir, worldDir);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		
		// Copying savestates
		try {
			FileUtils.copyDirectory(savestateDir, worldDir);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		File savestateFile = new File(savestateDir, "savestate.dat");
		
		// Load savestate data
		
		String data= "";
		try {
			if(savestateFile.exists())
				data = new String(com.google.common.io.Files.toByteArray(savestateFile));
			
			if(!data.isEmpty()) {
				// re apply the velocity to make a seamless transition
				motionX = Double.parseDouble(data.split(":")[0]);
				motionY = Double.parseDouble(data.split(":")[1]);
				motionZ = Double.parseDouble(data.split(":")[2]);
				Timer.ticks = Integer.parseInt(data.split(":")[3]);
				applyVelocity = true;
				TrackerFile.increaseLoadstates(savestatesDir, worldName);
			}
		} catch(Exception e){
			System.out.println("Failed to read savestate data:");
			e.printStackTrace();
		}
		
		// clear chat messages because of leftover messages
		//#if MC>=11100
		mc.ingameGUI.getChatGUI().clearChatMessages(true);
		//#else
//$$ 		mc.ingameGUI.getChatGUI().clearChatMessages();
		//#endif

		// reset the mouse position
		Mouse.setCursorPosition(x, y);
		Mouse.getDX();
		Mouse.getDY();

		// start the server
		mc.launchIntegratedServer(worldName, worldName, null);

		// reset the mouse position again
		Mouse.setCursorPosition(x, y);
		Mouse.getDX();
		Mouse.getDY();
		
		TickrateChangerMod.updateTickrate(tickratesaved);
		
		System.gc();
	}

	/**
	 * Checks if a savestate, that can be loaded, is present in the savestate directory
	 * @return existingSavestates
	 * @see MixinGuiIngameMenu#injectinitGui(org.spongepowered.asm.mixin.injection.callback.CallbackInfo)
	 */
	public static boolean hasSavestate() {
		String worldName = Minecraft.getMinecraft().getIntegratedServer().getFolderName();
		File savestatesDir = new File(Minecraft.getMinecraft().mcDataDir, "saves/savestates/");
		if (!savestatesDir.exists()) return false;
		int existingSavestates = savestatesDir.listFiles((d, s) -> {
			return s.startsWith(worldName + "-Savestate");
		}).length;
		return existingSavestates != 0;
	}

	public static class TrackerFile {

		// This is the worst Code I have ever written.
		public static int savestateCount = -1;
		public static int loadstateCount = -1;
		
		/**
		 * Increases the amount of savestates in the File
		 * @param savestateDir Savestate Directory
		 * @param worldName World Name
		 * @throws IOException File not found
		 */
		public static void increaseSavestates(final File savestateDir, final String worldName) throws IOException {
			final File infoFile = new File(savestateDir, worldName + "-info.txt");
			generateFile(infoFile, readSavestates(savestateDir, worldName) + 1, readLoadstates(savestateDir, worldName));
			savestateCount++;
		}

		/**
		 * Increases the amount of loadstates in the File
		 * @param savestateDir Savestate Directory
		 * @param worldName World Name
		 * @throws IOException File not found
		 */
		public static void increaseLoadstates(final File savestateDir, final String worldName) throws IOException {
			final File infoFile = new File(savestateDir, worldName + "-info.txt");
			generateFile(infoFile, readSavestates(savestateDir, worldName), readLoadstates(savestateDir, worldName) + 1);
			loadstateCount++;
		}

		/**
		 * Reads the amount of savestates in the File
		 * @param savestateDir Savestate Directory
		 * @param worldName World Name
		 * @throws IOException File not found
		 */
		public static int readSavestates(final File savestateDir, final String worldName) throws IOException {
			final File infoFile = new File(savestateDir, worldName + "-info.txt");
			if (!infoFile.exists()) {
				infoFile.createNewFile();
				generateFile(infoFile, 0, 0);
				return -1;
			}
			List<String> lines = Files.readAllLines(infoFile.toPath());
			return savestateCount = Integer.parseInt(lines.get(3).split("=")[1]);
		}

		/**
		 * Reads the amount of loadstates in the File
		 * @param savestateDir Savestate Directory
		 * @param worldName World Name
		 * @throws IOException File not found
		 */
		public static int readLoadstates(final File savestateDir, final String worldName) throws IOException {
			final File infoFile = new File(savestateDir, worldName + "-info.txt");
			if (!infoFile.exists()) {
				infoFile.createNewFile();
				generateFile(infoFile, 0, 0);
				return -1;
			}
			List<String> lines = Files.readAllLines(infoFile.toPath());
			return loadstateCount = Integer.parseInt(lines.get(4).split("=")[1]);
		}

		/**
		 * Creates a new file for tracking savestates and loadstates
		 * @param file Tracker File
		 * @param savestates Amount of savestates
		 * @param loadstates Amount of loadstate
		 */
		private static void generateFile(final File file, final int savestates, final int loadstates) throws FileNotFoundException {
			PrintWriter writer = new PrintWriter(file); // Empty File
			writer.println("");
			writer.close();

			PrintWriter hwga = new PrintWriter(file);
			hwga.print("# This file was generated by TASmod/LoTAS and diplays info about the usage of savestates\r\n\r\n\r\n");
			hwga.println("Total Savestates=" + savestates);
			hwga.println("Total Rerecords=" + loadstates);
			hwga.close();
		}

	}

	/**
	 * Delete a savestates and re-enumerate the rest
	 * @param i Index of savestate to delete
	 */
	public static void yeet(int i) {
		final Minecraft mc = Minecraft.getMinecraft();
		final IntegratedServer server = mc.getIntegratedServer();
		
		final String worldName = server.getFolderName();
		final File savestatesDir = new File(mc.mcDataDir, "saves/savestates/");
		
		final File savestateDir = new File(savestatesDir, worldName + "-Savestate" + (i));
		try {
			FileUtils.deleteDirectory(savestateDir);
		} catch (IOException e) {
			e.printStackTrace();
		}
		int existingSavestates = savestatesDir.listFiles((d, s) -> {
			return s.startsWith(worldName + "-Savestate");
		}).length;
		
		// Re-enumerate all of the savestates
		for (int j = i; j < existingSavestates + 1; j++) {
			new File(savestatesDir, worldName + "-Savestate" + (j + 1)).renameTo(new File(savestatesDir, worldName + "-Savestate" + (j)));
		}
		
	}

}
