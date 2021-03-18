package de.pfannekuchen.lotas.savestate;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.google.common.io.Files;

import de.pfannekuchen.lotas.mixin.gui.RedoGuiIngameMenu;
import de.pfannekuchen.lotas.tickratechanger.Timer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
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
	
	/**
	 * Returns the motion of the player, and the current time of the timer as a string
	 * @return Data as String
	 */
	public static String generateSavestateFile() {
		EntityPlayerSP p = Minecraft.getMinecraft().player;
		return p.motionX + ":" + p.motionY + ":" + p.motionZ + ":" + Timer.ticks;
	}
	
	/**
	 * Closes the server and creates a savestate in .minecraft/savestates/
	 * @throws IOException
	 */
	public static void savestate() throws IOException {
		RLogAPI.logDebug("[Savestate] Trying to create a Savestate");
		
		//Genrate data of the player
		String data = generateSavestateFile();
		
		//Get the necessary files
		String worldName = Minecraft.getMinecraft().getIntegratedServer().getFolderName();
		File worldDir = new File(Minecraft.getMinecraft().mcDataDir, "saves/" + worldName);
		File savestatesDir = new File(Minecraft.getMinecraft().mcDataDir, "savestates/");
		
		//Make the directory if it doesn't exist
		if(!savestatesDir.exists()) savestatesDir.mkdir();
		
		//Kill the server
        Minecraft.getMinecraft().world.sendQuittingDisconnectingPacket();
        Minecraft.getMinecraft().loadWorld((WorldClient)null);
        
        //Get the current savestate count
        int existingSavestates = savestatesDir.listFiles((d, s) -> {
        	return s.startsWith(worldName);
        }).length;
        
        //Change the target directory
        File savestateDir = new File(savestatesDir, worldName + (existingSavestates+1));
        
        //Read the motion of the data again
        double x = Double.parseDouble(data.split(":")[0]);
        double y = Double.parseDouble(data.split(":")[1]);
        double z = Double.parseDouble(data.split(":")[2]);
        
        //Do the actual copying
        FileUtils.copyDirectory(worldDir, savestateDir);
        Files.write(data.getBytes(), new File(savestateDir, "savestate.dat"));
        motionX = x;
        motionY = y;
        motionZ = z;
        applyVelocity = true;
        Minecraft.getMinecraft().launchIntegratedServer(worldName, worldName, null);
        RLogAPI.logDebug("[Savestate] Savestate created");
	}
	
	/**
	 * Closes the server and loads the latest savestate in .minecraft/savestates/
	 * @throws IOException
	 */
	public static void loadstate() throws IOException {
		RLogAPI.logDebug("[Savestate] Trying to Loadstate");
		String worldName = Minecraft.getMinecraft().getIntegratedServer().getFolderName();
		File worldDir = new File(Minecraft.getMinecraft().mcDataDir, "saves/" + worldName);
		File savestatesDir = new File(Minecraft.getMinecraft().mcDataDir, "savestates/");
		
        //Minecraft.getMinecraft().world.sendQuittingDisconnectingPacket();
        Minecraft.getMinecraft().getIntegratedServer().stopServer();
        //Minecraft.getMinecraft().loadWorld((WorldClient)null);
        
        int existingSavestates = savestatesDir.listFiles((d, s) -> {
        	return s.startsWith(worldName);
        }).length;
        
        File savestateDir = new File(savestatesDir, worldName + (existingSavestates));
        String data = new String(Files.toByteArray(new File(savestateDir, "savestate.dat")));
     
        motionX = Double.parseDouble(data.split(":")[0]);
        motionY = Double.parseDouble(data.split(":")[1]);
        motionZ = Double.parseDouble(data.split(":")[2]);
        Timer.ticks = (int) Double.parseDouble(data.split(":")[3]);
        applyVelocity = true;
        
        FileUtils.deleteDirectory(worldDir);
        FileUtils.copyDirectory(savestateDir, worldDir);
        Minecraft.getMinecraft().launchIntegratedServer(worldName, worldName, null);
        Minecraft.getMinecraft().ingameGUI.getChatGUI().clearChatMessages(true);
        RLogAPI.logDebug("[Savestates] Loadstate Done");
	}

	/**
	 * Checks if a savestate, that can be loaded, is present in the savestate directory
	 * @return existingSavestates
	 * @see RedoGuiIngameMenu#injectinitGui(org.spongepowered.asm.mixin.injection.callback.CallbackInfo)
	 */
	public static boolean hasSavestate() {
		String worldName = Minecraft.getMinecraft().getIntegratedServer().getFolderName();
		File savestatesDir = new File(Minecraft.getMinecraft().mcDataDir, "savestates/");
        if (!savestatesDir.exists()) return false;
		int existingSavestates = savestatesDir.listFiles((d, s) -> {
        	return s.startsWith(worldName);
        }).length;
        return existingSavestates != 0;
	}
	
}
