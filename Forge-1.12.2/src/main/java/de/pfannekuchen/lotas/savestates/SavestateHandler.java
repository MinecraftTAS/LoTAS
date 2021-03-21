package de.pfannekuchen.lotas.savestates;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;

import de.pfannekuchen.lotas.LoTASModContainer;
import de.pfannekuchen.lotas.savestates.chunkloading.SavestatesChunkControl;
import de.pfannekuchen.lotas.savestates.exceptions.LoadstateException;
import de.pfannekuchen.lotas.savestates.exceptions.SavestateException;
import de.pfannekuchen.lotas.savestates.playerloading.SavestatePlayerLoading;
import de.pfannekuchen.lotas.tickratechanger.TickrateChanger;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import net.minecraftforge.fml.common.FMLCommonHandler;

/**
 * Creates and loads savestates on both client and server without closing the world <br>
 * The old version that you may find in TASTools was heavily inspired by bspkrs' <a href="https://www.curseforge.com/minecraft/mc-mods/worldstatecheckpoints">WorldStateCheckpoints</a>,
 * but this new version is completely self written.
 * 
 * @author ScribbleLP
 *
 */
public class SavestateHandler {
	private static MinecraftServer server=FMLCommonHandler.instance().getMinecraftServerInstance();
	private static final File savestateDirectory=new File(server.getDataDirectory()+File.separator+"saves"+File.separator+"savestates"+File.separator);
	
	public static boolean isSaving=false;
	
	public static boolean isLoading=false;
	public static boolean wasLoading=false;
	
	/**
	 * Creates a copy of the currently played world and saves it in .minecraft/saves/savestates/worldname <br>
	 * Called in {@link SavestatePacketHandler}<br>
	 * <br>
	 * Side: Server
	 * @throws SavestateException
	 * @throws IOException
	 */
	public static void saveState() throws SavestateException, IOException {
		if(isSaving) {
			throw new SavestateException("A savestating operation is already being carried out");
		}
		if(isLoading) {
			throw new SavestateException("A loadstate operation is being carried out");
		}
		//Lock savestating and loadstating
		isSaving=true;
		
		//Create a directory just in case
		createSavestateDirectory();
		
		//Enable tickrate 0
		if(TickrateChanger.index!=0) {
			TickrateChanger.indexSave=TickrateChanger.index;
		}
		TickrateChanger.index=0;
		TickrateChanger.updateTickrate(0);
		
		
		//Update the server variable
//		server=ModLoader.getServerInstance();
		
		//Save the world!
		server.getPlayerList().saveAllPlayerData();
		server.saveAllWorlds(true);
		
		//Display the loading screen on the client
		LoTASModContainer.NETWORK.sendToAll(new SavestatePacket());
		
		//Get the current and target directory for copying
		String worldname=server.getFolderName();
		File currentfolder=new File(server.getDataDirectory(),"saves"+File.separator+worldname);
		File targetfolder=getNextSaveFolderLocation(worldname);
		
		//Send the name of the world to all players. This will make a savestate of the recording on the client with that name
		
		//Wait for the chunkloader to save the game
		for(WorldServer world:server.worlds) {
			AnvilChunkLoader chunkloader=(AnvilChunkLoader)world.getChunkProvider().chunkLoader;
			while(chunkloader.getPendingSaveCount()>0) {
			}
		}
		
		//Copy the directory
		FileUtils.copyDirectory(currentfolder, targetfolder);
		
		//Incrementing info file
		SavestateTrackerFile tracker = new SavestateTrackerFile(new File(savestateDirectory, worldname+"-info.txt"));
		tracker.increaseSavestates();
		tracker.saveFile();
		
		//Close the GuiSavestateScreen on the client
//		LoTASModContainer.NETWORK.sendToAll(new SavestatePacket());
		Minecraft.getMinecraft().displayGuiScreen(null);
		
		//Unlock savestating
		isSaving=false;
	}
	
	/**
	 * Searches through the savestate folder to look for the next possible savestate foldername <br>
	 * Savestate equivalent to {@link SavestateHandler#getLatestSavestateLocation(String)}
	 * @param worldname
	 * @return targetsavefolder
	 * @throws SavestateException if the found savestates count is greater or equal than 300
	 */
	private static File getNextSaveFolderLocation(String worldname) throws SavestateException {
		int i = 1;
		int limit=300;
		File targetsavefolder=null;
		isSaving=true;
		while (i <= limit) {
			if (i >= limit) {
				isSaving = false;
				throw new SavestateException("Savestatecount is greater or equal than "+limit);
			}
			targetsavefolder = new File(savestateDirectory,worldname + "-Savestate" + Integer.toString(i)+File.separator);
			
			if (!targetsavefolder.exists()) {
				break;
			}
			i++;
		}
		return targetsavefolder;
	}
	
	/**
	 * Loads the latest savestate it can find in .minecraft/saves/savestates/worldname-Savestate
	 * 
	 * @Side Server
	 * @throws LoadstateException
	 * @throws IOException
	 */
	public static void loadState() throws LoadstateException, IOException {
		if(isSaving) {
			throw new LoadstateException("A savestating operation is already being carried out");
		}
		if(isLoading) {
			throw new LoadstateException("A loadstate operation is being carried out");
		}
		//Lock savestating and loadstating
		isLoading=true;
		
		//Create a directory just in case
		createSavestateDirectory();
		
		//Enable tickrate 0
		if(TickrateChanger.index!=0) {
			TickrateChanger.indexSave=TickrateChanger.index;
		}
		TickrateChanger.index=0;
		TickrateChanger.updateTickrate(0);
		
		
		//Update the server instance
//		server=ModLoader.getServerInstance();
		
		//Get the current and target directory for copying
		String worldname=server.getFolderName();
		File currentfolder=new File(server.getDataDirectory(),"saves"+File.separator+worldname);
		File targetfolder=getLatestSavestateLocation(worldname);
		
		//Disabeling level saving for all worlds in case the auto save kicks in during world unload
		for(WorldServer world: server.worlds) {
			world.disableLevelSaving=true;		
		}
		
		//Unload chunks on the client
		LoTASModContainer.NETWORK.sendToAll(new LoadstatePacket());
		
		//Unload chunks on the server
		SavestatesChunkControl.disconnectPlayersFromChunkMap();
		SavestatesChunkControl.unloadAllServerChunks();
		SavestatesChunkControl.flushSaveHandler();
		
		
		//Delete and copy directories
		FileUtils.deleteDirectory(currentfolder);
		FileUtils.copyDirectory(targetfolder, currentfolder);
		
		
		//Update the player and the client
		SavestatePlayerLoading.loadAndSendMotionToPlayer();
		//Update the session.lock file so minecraft behaves and saves the world
		SavestatesChunkControl.updateSessionLock();
		//Load the chunks and send them to the client
		SavestatesChunkControl.addPlayersToChunkMap();
		
		
		//Enable level saving again
		for(WorldServer world: server.worlds) {
			world.disableLevelSaving=false;
		}
		
		//Incrementing info file
		SavestateTrackerFile tracker = new SavestateTrackerFile(new File(savestateDirectory, worldname+"-info.txt"));
		tracker.increaseRerecords();
		tracker.saveFile();
		
		//Send a notification that the savestate has been loaded
		server.getPlayerList().sendMessage(new TextComponentString(TextFormatting.GREEN+"Savestate loaded"));
		
		
		//Unlock loadstating
		isLoading=false;
		wasLoading=true;
	}
	
	/**
	 * Searches through the savestate folder to look for the latest savestate<br>
	 * Loadstate equivalent to {@link SavestateHandler#getNextSaveFolderLocation(String)}
	 * @param worldname
	 * @return targetsavefolder
	 * @throws LoadstateException if there is no savestate or more than 300 savestates
	 */
	private static File getLatestSavestateLocation(String worldname) throws LoadstateException {
		int i=1;
		int limit=300;
		
		File targetsavefolder=null;
		while(i<=limit) {
			targetsavefolder = new File(savestateDirectory,worldname+"-Savestate"+Integer.toString(i));
			if (!targetsavefolder.exists()) {
				if(i-1==0) {
					throw new LoadstateException("Couldn't find any savestates");
				}
				if(i>limit) {
					throw new LoadstateException("Savestatecount is greater or equal than "+limit);
				}
				targetsavefolder = new File(savestateDirectory,worldname+"-Savestate"+Integer.toString(i-1));
				break;
			}
			i++;
		}
		return targetsavefolder;
	}
	
	/**
	 * Creates the savestate directory in case the user deletes it between savestates
	 */
	private static void createSavestateDirectory() {
		if(!savestateDirectory.exists()) {
			savestateDirectory.mkdir();
		}
	}
	
	public static void playerLoadSavestateEvent() {
		PlayerList list= server.getPlayerList();
		List<EntityPlayerMP> players=list.getPlayers();
		for(EntityPlayerMP player : players) {
			NBTTagCompound nbttagcompound=list.getPlayerNBT(player);
			SavestatePlayerLoading.reattachEntityToPlayer(nbttagcompound, player.getServerWorld(), player);
		}
	}
	
	//#yoink
	public static boolean hasSavestate() {
		String worldName = Minecraft.getMinecraft().getIntegratedServer().getFolderName();
		File savestatesDir = new File(Minecraft.getMinecraft().mcDataDir, "saves/savestates/");
        if (!savestatesDir.exists()) return false;
		int existingSavestates = savestatesDir.listFiles((d, s) -> {
        	return s.startsWith(worldName);
        }).length;
        return existingSavestates != 0;
	}
	
	public static void savestateLegacy() {
		
	}
}