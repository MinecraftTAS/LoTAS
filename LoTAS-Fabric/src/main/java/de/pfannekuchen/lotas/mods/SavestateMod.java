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
import org.lwjgl.glfw.GLFW;

import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.core.utils.Timer;
import de.pfannekuchen.lotas.mixin.render.gui.MixinGuiIngameMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;

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
		final LocalPlayer p = Minecraft.getInstance().player;
		final Vec3 velocity = p.getDeltaMovement();
		motionX = velocity.x;
		motionY = velocity.y;
		motionZ = velocity.z;
		return velocity.x + ":" + velocity.y + ":" + velocity.z + ":" + Timer.ticks;
	}

	/**
	 * Closes the server and creates a savestate in .minecraft/savestates/
	 * @throws IOException
	 */
	public static void savestate(String name) {		
		final String data = generateSavestateFile();
		final Minecraft mc = Minecraft.getInstance();

		final MinecraftServer server = mc.getSingleplayerServer();
		
		server.getPlayerList().saveAll();
		
		final String worldName = MCVer.getCurrentWorldFolder();
		final File worldDir = new File(mc.gameDirectory, "saves/" + worldName);
		final File savestatesDir = new File(mc.gameDirectory, "saves/savestates/");
		
		mc.getSingleplayerServer().halt(true);
		
		if (!savestatesDir.exists())
			savestatesDir.mkdir();

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
		
		//#if MC>=11600
//$$ 		mc.loadLevel(worldName);
		//#else
		mc.selectLevel(worldName, worldName, null);
		//#endif
		
		applyVelocity=true;
		showSavestateDone = true;
		timeTitle = System.currentTimeMillis();
	}

	/**
	 * Closes the server and loads the latest savestate in .minecraft/savestates/
	 * @throws IOException
	 */
	public static void loadstate(int number) {
		if (!hasSavestate())
			return;

		final Minecraft mc = Minecraft.getInstance();

		double x = mc.mouseHandler.xpos();
		double y = mc.mouseHandler.ypos();

		isLoading = true;

		final IntegratedServer server = mc.getSingleplayerServer();

		for (ServerLevel worldserver : server.getAllLevels()) {
			worldserver.noSave = true;
		}

		mc.getSingleplayerServer().halt(true);

		final String worldName = MCVer.getCurrentWorldFolder();
		final File worldDir = new File(mc.gameDirectory, "saves/" + worldName);
		final File savestatesDir = new File(mc.gameDirectory, "saves/savestates/");

		int existingSavestates = savestatesDir.listFiles((d, s) -> {
			return s.startsWith(worldName + "-Savestate");
		}).length;

		if (number != -1)
			existingSavestates = number;

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
		mc.gui.getChat().clearMessages(true);

		GLFW.glfwSetCursorPos(Minecraft.getInstance().window.getWindow(), x, y);
		mc.mouseHandler.turnPlayer();

		//#if MC>=11600
//$$ 		mc.loadLevel(worldName);
		//#else
		mc.selectLevel(worldName, worldName, null);
		//#endif
		
		GLFW.glfwSetCursorPos(Minecraft.getInstance().window.getWindow(), x, y);
		mc.mouseHandler.turnPlayer();

	}

	/**
	 * Checks if a savestate, that can be loaded, is present in the savestate directory
	 * @return existingSavestates
	 * @see MixinGuiIngameMenu#injectinitGui(org.spongepowered.asm.mixin.injection.callback.CallbackInfo)
	 */
	public static boolean hasSavestate() {
		final String worldName = MCVer.getCurrentWorldFolder();
		File savestatesDir = new File(Minecraft.getInstance().gameDirectory, "saves/savestates/");
		if (!savestatesDir.exists())
			return false;
		int existingSavestates = savestatesDir.listFiles((d, s) -> {
			return s.startsWith(worldName + "-Savestate");
		}).length;
		return existingSavestates != 0;
	}

	public static class TrackerFile {

		// This is the worst Code I have ever written.
		public static int savestateCount = -1;
		public static int loadstateCount = -1;
		
		public static void increaseSavestates(final File savestateDir, final String worldName) throws IOException {
			final File infoFile = new File(savestateDir, worldName + "-info.txt");
			generateFile(infoFile, readSavestates(savestateDir, worldName) + 1, readLoadstates(savestateDir, worldName));
			savestateCount++;
		}

		public static void increaseLoadstates(final File savestateDir, final String worldName) throws IOException {
			final File infoFile = new File(savestateDir, worldName + "-info.txt");
			generateFile(infoFile, readSavestates(savestateDir, worldName), readLoadstates(savestateDir, worldName) + 1);
			loadstateCount++;
		}

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

	// Delete
	public static void yeet(int i) {
		final Minecraft mc = Minecraft.getInstance();
		final String worldName = MCVer.getCurrentWorldFolder();
		final File savestatesDir = new File(mc.gameDirectory, "saves/savestates/");

		final File savestateDir = new File(savestatesDir, worldName + "-Savestate" + (i));
		try {
			FileUtils.deleteDirectory(savestateDir);
		} catch (IOException e) {
			e.printStackTrace();
		}
		int existingSavestates = savestatesDir.listFiles((d, s) -> {
			return s.startsWith(worldName + "-Savestate");
		}).length;

		// Re Number all of the rest
		for (int j = i; j < existingSavestates + 1; j++) {
			new File(savestatesDir, worldName + "-Savestate" + (j + 1)).renameTo(new File(savestatesDir, worldName + "-Savestate" + (j)));
		}

	}

}
