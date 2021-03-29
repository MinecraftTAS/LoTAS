package de.pfannekuchen.lotas.challenges;

import java.io.File;
import java.io.Serializable;
import java.net.URL;
import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.chunk.storage.AnvilSaveConverter;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.SaveFormatComparator;

public class ChallengeMap implements Serializable {
	
	private static final long serialVersionUID = -2179267199923671549L;

	public String username;
	public UUID uuid;
	public URL map;
	public String displayName;
	public String name;
	public String description;
	public String[] leaderboard;
	public String resourceLoc;
	
	public SaveFormatComparator getSummary() {
		SaveFormatComparator c = new SaveFormatComparator(name, displayName, 0L, 0L, WorldSettings.GameType.ADVENTURE, false, false, false);
		return c;
	}

	public ISaveFormat getSaveLoader() {
		new File(Minecraft.getMinecraft().mcDataDir, "challenges").mkdir();
		ISaveFormat format = new AnvilSaveConverter(new File(Minecraft.getMinecraft().mcDataDir, "challenges"));
		return format;
	}
	
}
