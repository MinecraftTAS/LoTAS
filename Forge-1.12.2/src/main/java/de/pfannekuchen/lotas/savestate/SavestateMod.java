package de.pfannekuchen.lotas.savestate;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.lwjgl.input.Mouse;

import com.google.common.io.Files;

import de.pfannekuchen.lotas.mixin.gui.RedoGuiIngameMenu;
import de.pfannekuchen.lotas.tickratechanger.Timer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import rlog.RLogAPI;

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
		final EntityPlayerSP p = Minecraft.getMinecraft().player;
		return p.motionX + ":" + p.motionY + ":" + p.motionZ + ":" + Timer.ticks;
	}
	
	/**
	 * Closes the server and creates a savestate in .minecraft/savestates/
	 * @throws IOException
	 */
	public static void savestate(String name) {
		RLogAPI.logDebug("[Savestate] Trying to create a Savestate");
		final String data = generateSavestateFile();
		final Minecraft mc = Minecraft.getMinecraft();
		
		// Hack 1
		final MinecraftServer server = mc.integratedServer;
		server.getPlayerList().saveAllPlayerData();
		server.saveAllWorlds(false);
		for (WorldServer world : server.worlds) {
			try {
				world.saveAllChunks(true, null);
			} catch (MinecraftException e) {
				e.printStackTrace();
			}
		}
        for (final WorldServer worldserver : mc.integratedServer.worlds) {
        	worldserver.disableLevelSaving = true;
        }
        
        // Orig Hack 1
		/*Minecraft.getMinecraft().world.sendQuittingDisconnectingPacket();
        Minecraft.getMinecraft().loadWorld((WorldClient)null);*/
        
        for(WorldServer world:server.worlds) {
    		AnvilChunkLoader chunkloader=(AnvilChunkLoader)world.getChunkProvider().chunkLoader;
    		while(chunkloader.getPendingSaveCount()>0) {
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
				Files.write(data.getBytes(), new File(savestateDir, "savestate.dat"));
				Files.write((name == null) ? new SimpleDateFormat("MM-dd-yyyy HH.mm.ss").format(new Date()).getBytes() : name.getBytes(), new File(savestateDir, "lotas.dat"));
			} catch (IOException e) {
				e.printStackTrace();
			}
            
            try {
				TrackerFile.increaseSavestates(savestatesDir, worldName);
			} catch (IOException e) {
				e.printStackTrace();
			}
            
            // Hack 2
            for (WorldServer worldserver : server.worlds) {
            	worldserver.disableLevelSaving = false;
            }
            RLogAPI.logDebug("[Savestate] Savestate created");
        }).start();
        
        
        // Orig Hack 2
        //applyVelocity = true;
        //Minecraft.getMinecraft().launchIntegratedServer(worldName, worldName, null);
        
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
		
		RLogAPI.logDebug("[Savestate] Trying to Loadstate");
		final Minecraft mc = Minecraft.getMinecraft();
		final IntegratedServer server = mc.integratedServer;
		
		// Imagine Saving the World when loading a state ._.
        for (WorldServer worldserver : server.worlds) {
        	worldserver.disableLevelSaving = true;
        }
		
        mc.world.sendQuittingDisconnectingPacket();
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
			final String data = new String(Files.toByteArray(new File(savestateDir, "savestate.dat")));
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
        mc.ingameGUI.getChatGUI().clearChatMessages(true);
        
        Mouse.setCursorPosition(x, y);
        Mouse.getDX();
        Mouse.getDY();
        
        mc.launchIntegratedServer(worldName, worldName, null);
        RLogAPI.logDebug("[Savestates] Loadstate Done");
        
        Mouse.setCursorPosition(x, y);
        Mouse.getDX();
        Mouse.getDY();
        
        
	}

	/**
	 * Checks if a savestate, that can be loaded, is present in the savestate directory
	 * @return existingSavestates
	 * @see RedoGuiIngameMenu#injectinitGui(org.spongepowered.asm.mixin.injection.callback.CallbackInfo)
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
	
}
