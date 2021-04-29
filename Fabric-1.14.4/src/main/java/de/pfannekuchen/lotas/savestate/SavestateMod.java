package de.pfannekuchen.lotas.savestate;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;

import com.google.common.io.Files;

import de.pfannekuchen.lotas.tickratechanger.Timer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.server.world.ServerWorld;
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
		final ClientPlayerEntity p = MinecraftClient.getInstance().player;
		return p.getVelocity().x + ":" + p.getVelocity().y + ":" + p.getVelocity().z + ":" + Timer.ticks;
	}
	
	/**
	 * Closes the server and creates a savestate in .minecraft/savestates/
	 * @throws IOException
	 */
	public static void savestate(String name) {
		RLogAPI.logDebug("[Savestate] Trying to create a Savestate");
		final String data = generateSavestateFile();
		final MinecraftClient mc = MinecraftClient.getInstance();
		
		// Hack 1
		final MinecraftServer server = mc.getServer();
		server.save(true, true, true);
		
        for (final ServerWorld worldserver : mc.getServer().getWorlds()) {
        	worldserver.savingDisabled = true;
        }
        
        // Orig Hack 1
		/*MinecraftClient.getInstance().world.sendQuittingDisconnectingPacket();
        MinecraftClient.getInstance().loadWorld((WorldClient)null);*/
        
//        for(ServerWorld world:server.getWorlds()) {
//    		AnvilChunkLoader chunkloader=(AnvilChunkLoader)world.getChunkManager().getCh.chunkLoader;
//    		while(chunkloader.getPendingSaveCount()>0) {
//    		}
//    	}
        
        new Thread(() -> {
    		final String worldName = server.getLevelName();
    		final File worldDir = new File(mc.runDirectory, "saves/" + worldName);
    		final File savestatesDir = new File(mc.runDirectory, "saves/savestates/");
    		
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
            for (ServerWorld worldserver : server.getWorlds()) {
            	worldserver.savingDisabled = false;
            }
            RLogAPI.logDebug("[Savestate] Savestate created");
        }).start();
        
        
        // Orig Hack 2
        //applyVelocity = true;
        //MinecraftClient.getInstance().launchgetServer()(worldName, worldName, null);
        
        showSavestateDone = true;
        timeTitle = System.currentTimeMillis();
	}
	
	/**
	 * Closes the server and loads the latest savestate in .minecraft/savestates/
	 * @throws IOException
	 */
	public static void loadstate(int number) {
		if (!hasSavestate()) return;
		
		isLoading = true;
		
		RLogAPI.logDebug("[Savestate] Trying to Loadstate");
		final MinecraftClient mc = MinecraftClient.getInstance();
		final IntegratedServer server = mc.getServer();
		
		// Imagine Saving the World when loading a state ._.
        for (ServerWorld worldserver : server.getWorlds()) {
        	worldserver.savingDisabled = true;
        }
		
        mc.getServer().stop(true);
        
    	final String worldName = server.getLevelName();
		final File worldDir = new File(mc.runDirectory, "saves/" + worldName);
		final File savestatesDir = new File(mc.runDirectory, "saves/savestates/");
        
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
        mc.inGameHud.getChatHud().clear(true);
        
        mc.startIntegratedServer(worldName, worldName, null);
        RLogAPI.logDebug("[Savestates] Loadstate Done");
        
        
	}

	/**
	 * Checks if a savestate, that can be loaded, is present in the savestate directory
	 * @return existingSavestates
	 * @see RedoGuiIngameMenu#injectinitGui(org.spongepowered.asm.mixin.injection.callback.CallbackInfo)
	 */
	public static boolean hasSavestate() {
		String worldName = MinecraftClient.getInstance().getServer().getLevelName();
		File savestatesDir = new File(MinecraftClient.getInstance().runDirectory, "saves/savestates/");
        if (!savestatesDir.exists()) return false;
		int existingSavestates = savestatesDir.listFiles((d, s) -> {
        	return s.startsWith(worldName + "-Savestate");
        }).length;
        return existingSavestates != 0;
	}
	
}
