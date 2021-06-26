package de.pfannekuchen.lotas.taschallenges;

import java.io.File;
import java.net.URL;

import net.minecraft.client.Minecraft;
//#if MC>=11000
import net.minecraft.world.GameType;
//#else
//$$ import net.minecraft.world.WorldSettings.GameType;
//#endif
import net.minecraft.world.chunk.storage.AnvilSaveConverter;
import net.minecraft.world.storage.ISaveFormat;

/**
 * TAS Challenge struct
 * @since v1.1
 * @version v1.1
 * @author Pancake
 */
public class ChallengeMap {
	
	/** Map that is currently loaded or null */
	public static ChallengeMap currentMap;
	
	public URL map;
	public String displayName;
	public String name;
	public String description;
	public String[] leaderboard;
	public String resourceLoc;
	
	//#if MC>=10900
	/**
	 * Get the world data from the entry
	 * @return Returns a summary of all data saved in the world (names, gamemodes, etc.)
	 */
	public net.minecraft.world.storage.WorldSummary getSummary() {
		net.minecraft.world.WorldSettings settings = new net.minecraft.world.WorldSettings(0L, GameType.ADVENTURE, false, true, net.minecraft.world.WorldType.FLAT);
		net.minecraft.world.storage.WorldInfo info = new net.minecraft.world.storage.WorldInfo(settings, displayName);
		net.minecraft.world.storage.WorldSummary sum = new net.minecraft.world.storage.WorldSummary(info, name, displayName, 0, false);
		return sum;
	}
	//#endif

	/** 
	 * Creates a Save Loader for the Challenge map
	 * @return Save Loader for the TAS Challenge map.
	 */
	public ISaveFormat getSaveLoader() {
		new File(Minecraft.getMinecraft().mcDataDir, "challenges").mkdir();
		//#if MC>=10900
		ISaveFormat format = new AnvilSaveConverter(new File(Minecraft.getMinecraft().mcDataDir, "challenges"), Minecraft.getMinecraft().getDataFixer());
		//#else
//$$ 		ISaveFormat format = new AnvilSaveConverter(new File(Minecraft.getMinecraft().mcDataDir, "challenges"));
		//#endif
		return format;
	}
	
}
