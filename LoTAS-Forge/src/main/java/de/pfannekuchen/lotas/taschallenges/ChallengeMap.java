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
//#if MC>=10900
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;
import net.minecraft.world.storage.WorldInfo;
import net.minecraft.world.storage.WorldSummary;
//#endif

public class ChallengeMap {
	
	public static ChallengeMap currentMap;
	
	public URL map;
	public String displayName;
	public String name;
	public String description;
	public String[] leaderboard;
	public String resourceLoc;
	
	//#if MC>=10900
	public WorldSummary getSummary() {
		WorldSettings settings = new WorldSettings(0L, GameType.ADVENTURE, false, true, WorldType.FLAT);
		WorldInfo info = new WorldInfo(settings, displayName);
		WorldSummary sum = new WorldSummary(info, name, displayName, 0, false);
		return sum;
	}
	//#endif

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
