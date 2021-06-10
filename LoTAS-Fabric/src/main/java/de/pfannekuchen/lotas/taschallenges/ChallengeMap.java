package de.pfannekuchen.lotas.taschallenges;

import java.io.File;
import java.io.Serializable;
import java.net.URL;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import net.minecraft.world.GameMode;
import net.minecraft.world.level.LevelGeneratorType;
import net.minecraft.world.level.LevelInfo;
import net.minecraft.world.level.LevelProperties;
import net.minecraft.world.level.storage.LevelStorage;
import net.minecraft.world.level.storage.LevelSummary;

public class ChallengeMap implements Serializable {
	
	private static final long serialVersionUID = -2179267199923671549L;

	public URL map;
	public String displayName;
	public String name;
	public String description;
	public String[] leaderboard;
	public Identifier resourceLoc;
	
	public LevelSummary getSummary() {
		LevelInfo settings = new LevelInfo(0L, GameMode.ADVENTURE, false, true, LevelGeneratorType.FLAT);
		LevelProperties info = new LevelProperties(settings, displayName);
		LevelSummary sum = new LevelSummary(info, name, displayName, 0, false);
		return sum;
	}

	public LevelStorage getLevelStorage() {
		return new LevelStorage(new File(MinecraftClient.getInstance().runDirectory, "challenges").toPath(), new File(MinecraftClient.getInstance().runDirectory, "backups").toPath(), MinecraftClient.getInstance().getDataFixer());
	}

	public LevelInfo getInfo() {
		return new LevelInfo(0L, GameMode.ADVENTURE, false, true, LevelGeneratorType.FLAT);
	}
	
}
