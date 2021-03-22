package de.pfannekuchen.lotas.savestates;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.google.common.io.Files;

import de.pfannekuchen.lotas.config.ConfigManager;
import de.pfannekuchen.lotas.mixin.gui.RedoGuiIngameMenu;
import de.pfannekuchen.lotas.tickratechanger.TickrateChanger;
import de.pfannekuchen.lotas.tickratechanger.Timer;
import de.pfannekuchen.lotas.util.ChunkUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import net.minecraftforge.fml.common.FMLCommonHandler;
import rlog.RLogAPI;

/**
 * Saves the player velocity and makes a copy of the world in a seperate folder.
 * That world can also be loaded again
 * 
 * @author Pancake
 */
public class SavestateMod {
	
	private static MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
	private static final File savestateDirectory = new File(server.getDataDirectory() + File.separator + "saves" + File.separator + "savestates" + File.separator);
	
	public static boolean applyVelocity;
	public static double motionX;
	public static double motionY;
	public static double motionZ;
	
	/**
	 * Returns the motion of the player, and the current time of the timer as a string
	 * @return Data as String
	 */
	private static String generateSavestateFile() {
		EntityPlayerSP p = Minecraft.getMinecraft().player;
		return p.motionX + ":" + p.motionY + ":" + p.motionZ + ":" + Timer.ticks;
	}
	
	/**
	 * Closes the server and creates a savestate in .minecraft/savestates/
	 * @throws IOException
	 */
	public static void savestate() throws IOException {
		RLogAPI.logDebug("[Savestate] Trying to create a Savestate");
		
		if(TickrateChanger.index != 0) {
			TickrateChanger.indexSave = TickrateChanger.index;
		}
		TickrateChanger.index = 0;
		TickrateChanger.updateTickrate(0);
		
		server.getPlayerList().saveAllPlayerData();
		server.saveAllWorlds(true);
		
		String worldname = server.getWorldName();
		File currentfolder = new File(server.getDataDirectory(), "saves" + File.separator + worldname);
		File targetfolder = getNextSaveFolderLocation(worldname);
		
		for(WorldServer world : server.worlds) {
			AnvilChunkLoader chunkloader = (AnvilChunkLoader) world.getChunkProvider().chunkLoader;
			while (chunkloader.getPendingSaveCount() > 0) {
				
			}
		}
		
		FileUtils.copyDirectory(currentfolder, targetfolder);
		
		Files.write(generateSavestateFile().getBytes(), new File(targetfolder, "savestate.dat"));
		
		Minecraft.getMinecraft().displayGuiScreen(null);
		
		TickrateChanger.updateTickrate(TickrateChanger.ticks[TickrateChanger.indexSave]);
		TickrateChanger.index = TickrateChanger.indexSave;
        RLogAPI.logDebug("[Savestate] Savestate created");
	}
	
	/**
	 * Closes the server and loads the latest savestate in .minecraft/savestates/
	 * @throws IOException
	 */
	public static void loadstate() throws IOException {
		boolean experimental = ConfigManager.getBoolean("savestates", "useExperimentalSavestates");
		RLogAPI.logDebug("[Savestate] Trying to Loadstate using " + (experimental ? "Experimental Loadstates" : "Normal Savestates"));
		String worldName = server.getWorldName();
		File worldDir = new File(Minecraft.getMinecraft().mcDataDir, "saves/" + worldName);
		File savestatesDir = new File(Minecraft.getMinecraft().mcDataDir, "saves/savestates/");
		File targetfolder = getLatestSavestateLocation(worldName);
		
        int existingSavestates = savestatesDir.listFiles((d, s) -> {
        	return s.startsWith(worldName + "-Savestate");
        }).length;
        File savestateDir = new File(savestatesDir, worldName + "-Savestate" + existingSavestates);
		
		if (experimental) {
			createSavestateDirectory();
			
			if(TickrateChanger.index != 0) {
				TickrateChanger.indexSave = TickrateChanger.index;
			}
			TickrateChanger.index = 0;
			TickrateChanger.updateTickrate(0);
			
			for (WorldServer world: server.worlds) {
				world.disableLevelSaving=true;		
			}
			
			Minecraft.getMinecraft().addScheduledTask(()->{
				ChunkUtil.unloadAllClientChunks();
			});
			
			ChunkUtil.disconnectPlayersFromChunkMap();
			ChunkUtil.unloadAllServerChunks();
			ChunkUtil.flushSaveHandler();
			
			FileUtils.deleteDirectory(worldDir);
			FileUtils.copyDirectory(targetfolder, worldDir);
			
			ChunkUtil.loadAndSendMotionToPlayer();
			ChunkUtil.updateSessionLock();
			ChunkUtil.addPlayersToChunkMap();
			
			for(WorldServer world : server.worlds) {
				world.disableLevelSaving = false;
			}
		
			/*PlayerList list = server.getPlayerList();
			for (EntityPlayerMP player : list.getPlayers()) {
				NBTTagCompound tag = list.getPlayerNBT(player);
				ChunkUtil.reattachEntityToPlayer(tag, player.getServerWorld(), player);
			}*/
			
		} else {
			Minecraft.getMinecraft().displayGuiScreen(new GuiIngameMenu());
	        Minecraft.getMinecraft().getIntegratedServer().stopServer();
	        FileUtils.deleteDirectory(worldDir);
	        FileUtils.copyDirectory(savestateDir, worldDir);
	        Minecraft.getMinecraft().launchIntegratedServer(worldName, worldName, null);
		}

        String data = new String(Files.toByteArray(new File(savestateDir, "savestate.dat")));
        
        motionX = Double.parseDouble(data.split(":")[0]);
        motionY = Double.parseDouble(data.split(":")[1]);
        motionZ = Double.parseDouble(data.split(":")[2]);
        Timer.ticks = (int) Double.parseDouble(data.split(":")[3]);
        applyVelocity = true;
        
        Minecraft.getMinecraft().ingameGUI.getChatGUI().clearChatMessages(true);
        RLogAPI.logDebug("[Savestates] Loadstate Done");
	}

	/**
	 * Checks if a savestate, that can be loaded, is present in the savestate directory
	 * @return existingSavestates
	 * @see RedoGuiIngameMenu#injectinitGui(org.spongepowered.asm.mixin.injection.callback.CallbackInfo)
	 */
	public static boolean hasSavestate() {
		String worldName = Minecraft.getMinecraft().getIntegratedServer().getWorldName();
		File savestatesDir = new File(Minecraft.getMinecraft().mcDataDir, "savestates/");
        if (!savestatesDir.exists()) return false;
		int existingSavestates = savestatesDir.listFiles((d, s) -> {
        	return s.startsWith(worldName);
        }).length;
        return existingSavestates != 0;
	}
	
	/**
	 * Creates the savestate directory in case the user deletes it between savestates
	 */
	private static void createSavestateDirectory() {
		if(!savestateDirectory.exists()) {
			savestateDirectory.mkdir();
		}
	}
	
	/**
	 * Searches through the savestate folder to look for the latest savestate<br>
	 * Loadstate equivalent to {@link SavestateHandler#getNextSaveFolderLocation(String)}
	 * @param worldname
	 * @return targetsavefolder
	 * @throws LoadstateException if there is no savestate or more than 300 savestates
	 */
	private static File getLatestSavestateLocation(String worldname) throws IOException {
		int i = 1;
		int limit = 300;
		
		File targetsavefolder = null;
		while(i <= limit) {
			targetsavefolder = new File(savestateDirectory,worldname + "-Savestate" + Integer.toString(i));
			if (!targetsavefolder.exists()) {
				if(i - 1 == 0) {
					throw new IOException("Couldn't find any savestates");
				}
				if(i > limit) {
					throw new IOException("Savestatecount is greater or equal than " + limit);
				}
				targetsavefolder = new File(savestateDirectory, worldname + "-Savestate" + Integer.toString(i - 1));
				break;
			}
			i++;
		}
		return targetsavefolder;
	}
	
	/**
	 * Searches through the savestate folder to look for the next possible savestate foldername <br>
	 * Savestate equivalent to {@link SavestateHandler#getLatestSavestateLocation(String)}
	 * @param worldname
	 * @return targetsavefolder
	 * @throws SavestateException if the found savestates count is greater or equal than 300
	 */
	private static File getNextSaveFolderLocation(String worldname) {
		int i = 1;
		int limit = 300;
		File targetsavefolder = null;
		while (i <= limit) {
			File savestatesDir = new File(Minecraft.getMinecraft().mcDataDir, "saves/savestates/");
			targetsavefolder = new File(savestatesDir, worldname + "-Savestate" + Integer.toString(i) + File.separator);
			
			if (!targetsavefolder.exists()) {
				break;
			}
			i++;
		}
		return targetsavefolder;
	}
	
	
}
