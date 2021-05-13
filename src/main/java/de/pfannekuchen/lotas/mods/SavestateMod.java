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
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.WorldServer;
//#if MC>=10900
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
//#endif

/**
 * Saves the player velocity and makes a copy of the world in a seperate folder.
 * That world can also be loaded again
 * 
 * @author Pancake
 */
public class SavestateMod {

	public static boolean applyVelocity;
	public static double motionX;
	public static double motionY;
	public static double motionZ;

	public static boolean showSavestateDone;
	public static boolean showLoadstateDone;
	public static long timeTitle;

	public static boolean isLoading;

	/**
	 * Returns the motion of the player, and the current time of the timer as a string
	 * @return Data as String
	 */
	public static final String generateSavestateFile() {
		final EntityPlayerSP p = MCVer.player(Minecraft.getMinecraft());
		return p.motionX + ":" + p.motionY + ":" + p.motionZ + ":" + Timer.ticks;
	}

	/**
	 * Closes the server and creates a savestate in .minecraft/savestates/
	 * @throws IOException
	 */
	public static void savestate(String name) {
		final String data = generateSavestateFile();
		final Minecraft mc = Minecraft.getMinecraft();

		final MinecraftServer server = mc.getIntegratedServer();
		//#if MC>=10900
		server.getPlayerList().saveAllPlayerData();
		//#else
		//$$ server.getConfigurationManager().saveAllPlayerData();
		//#endif
		server.saveAllWorlds(false);
		for (WorldServer world : MCVer.getWorlds(server)) {
			try {
				world.saveAllChunks(true, null);
			} catch (MinecraftException e) {
				e.printStackTrace();
			}
		}
		
		for (final WorldServer worldserver : MCVer.getWorlds(server)) {
			worldserver.disableLevelSaving = true;
		}

		for(WorldServer world:MCVer.getWorlds(server)) {
			AnvilChunkLoader chunkloader=(AnvilChunkLoader)world.getChunkProvider().chunkLoader;
			while(((AccessorAnvilChunkLoader) chunkloader).chunksToSave().size() > 0) {
				
			}
		}

		new Thread(() -> {
			final String worldName = server.getFolderName();
			final File worldDir = new File(mc.mcDataDir, "saves/" + worldName);
			final File savestatesDir = new File(mc.mcDataDir, "saves/savestates/");

			if (!savestatesDir.exists()) savestatesDir.mkdir();

			final int existingSavestates = savestatesDir.listFiles((d, s) -> {
				return s.startsWith(worldName + "-Savestate");
			}).length;

			File savestateDir = new File(savestatesDir, worldName + "-Savestate" + (existingSavestates + 1));

			try {
				FileUtils.copyDirectory(worldDir, savestateDir);
				com.google.common.io.Files.write(data.getBytes(), new File(savestateDir, "savestate.dat"));
				com.google.common.io.Files.write((name == null) ? new SimpleDateFormat("MM-dd-yyyy HH.mm.ss").format(new Date()).getBytes() : name.getBytes(), new File(savestateDir, "lotas.dat"));
			} catch (IOException e) {
				e.printStackTrace();
			}

			try {
				TrackerFile.increaseSavestates(savestatesDir, worldName);
			} catch (IOException e) {
				e.printStackTrace();
			}

			for (WorldServer worldserver : MCVer.getWorlds(server)) {
				worldserver.disableLevelSaving = false;
			}
		}).start();

		showSavestateDone = true;
		timeTitle = System.currentTimeMillis();
	}

	/**
	 * Closes the server and loads the latest savestate in .minecraft/savestates/
	 * @throws IOException
	 */
	public static void loadstate(int number) {
		if (!hasSavestate()) return;

		int x = Mouse.getX();
		int y = Mouse.getY();

		isLoading = true;

		final Minecraft mc = Minecraft.getMinecraft();
		final IntegratedServer server = mc.getIntegratedServer();

		for (WorldServer worldserver : MCVer.getWorlds(server)) {
			worldserver.disableLevelSaving = true;
		}

		Minecraft.stopIntegratedServer();

		final String worldName = server.getFolderName();
		final File worldDir = new File(mc.mcDataDir, "saves/" + worldName);
		final File savestatesDir = new File(mc.mcDataDir, "saves/savestates/");

		int existingSavestates = savestatesDir.listFiles((d, s) -> {
			return s.startsWith(worldName + "-Savestate");
		}).length;

		if (number != -1) existingSavestates = number;

		final File savestateDir = new File(savestatesDir, worldName + "-Savestate" + (existingSavestates));
		try {
			final String data = new String(com.google.common.io.Files.toByteArray(new File(savestateDir, "savestate.dat")));
			FileUtils.deleteDirectory(worldDir);
			FileUtils.copyDirectory(savestateDir, worldDir);

			motionX = Double.parseDouble(data.split(":")[0]);
			motionY = Double.parseDouble(data.split(":")[1]);
			motionZ = Double.parseDouble(data.split(":")[2]);
			Timer.ticks = Integer.parseInt(data.split(":")[3]);
			applyVelocity = true;
			TrackerFile.increaseLoadstates(savestatesDir, worldName);
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}
		//#if MC>=11100
		mc.ingameGUI.getChatGUI().clearChatMessages(true);
		//#else
//$$ 		mc.ingameGUI.getChatGUI().clearChatMessages();
		//#endif

		Mouse.setCursorPosition(x, y);
		Mouse.getDX();
		Mouse.getDY();

		mc.launchIntegratedServer(worldName, worldName, null);

		Mouse.setCursorPosition(x, y);
		Mouse.getDX();
		Mouse.getDY();


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

		public static void increaseSavestates(final File savestateDir, final String worldName) throws IOException {
			final File infoFile = new File(savestateDir, worldName + "-info.txt");
			generateFile(infoFile, readSavestates(savestateDir, worldName) + 1, readLoadstates(savestateDir, worldName));
		}

		public static void increaseLoadstates(final File savestateDir, final String worldName) throws IOException {
			final File infoFile = new File(savestateDir, worldName + "-info.txt");
			generateFile(infoFile, readSavestates(savestateDir, worldName), readLoadstates(savestateDir, worldName) + 1);
		}

		public static int readSavestates(final File savestateDir, final String worldName) throws IOException {
			final File infoFile = new File(savestateDir, worldName + "-info.txt");
			if (!infoFile.exists()) {
				infoFile.createNewFile();
				generateFile(infoFile, 0, 0);
				return -1;
			}
			List<String> lines = Files.readAllLines(infoFile.toPath());
			return Integer.parseInt(lines.get(3).split("=")[1]);
		}

		public static int readLoadstates(final File savestateDir, final String worldName) throws IOException {
			final File infoFile = new File(savestateDir, worldName + "-info.txt");
			if (!infoFile.exists()) {
				infoFile.createNewFile();
				generateFile(infoFile, 0, 0);
				return -1;
			}
			List<String> lines = Files.readAllLines(infoFile.toPath());
			return Integer.parseInt(lines.get(4).split("=")[1]);
		}

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

}
