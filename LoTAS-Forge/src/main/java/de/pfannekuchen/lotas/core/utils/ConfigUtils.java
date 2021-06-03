package de.pfannekuchen.lotas.core.utils;

import de.pfannekuchen.lotas.gui.GuiConfiguration;
import net.minecraftforge.common.config.Configuration;

public class ConfigUtils {

	public static Configuration conf;
	
	public static void setString(String category, String key, String value) {
		conf.get(category, key, "").set(value);
	}
	
	public static void setInt(String category, String key, int value) {
		conf.get(category, key, "").set(value);
	}
	
	public static void setBoolean(String category, String key, boolean value) {
		conf.get(category, key, "").set(value);
	}
	
	public static boolean getBoolean(String category, String key) {
		return conf.get(category, key, false).getBoolean();
	}
	
	public static String getString(String category, String key) {
		return conf.get(category, key, "null").getString();
	}
	public static int getInt(String category, String key) {
		return conf.get(category, key, -1).getInt();
	}
	
	public static void save() {
		conf.save();
	}
	
	public static void init(Configuration configuration) {
		conf = configuration;
		conf.load();
		
		// Defaults
		if (ConfigUtils.getInt("hidden", "tickrate") <= 0) ConfigUtils.setInt("hidden", "tickrate", 6);
		if (ConfigUtils.getInt("hidden", "explosionoptimization") <= 0) ConfigUtils.setInt("hidden", "explosionoptimization", 100);		
		ConfigUtils.save();
		
		for (int i = 0; i < GuiConfiguration.optionsBoolean.length; i++) {
			String cat = GuiConfiguration.optionsBoolean[i].split(":")[1];
			String title = GuiConfiguration.optionsBoolean[i].split(":")[2];
			GuiConfiguration.optionsBoolean[i] = GuiConfiguration.optionsBoolean[i].replaceFirst("INSERT", getBoolean(cat, title) + "");
		}
		for (int i = 0; i < GuiConfiguration.optionsString.length; i++) {
			String cat = GuiConfiguration.optionsString[i].split(":")[1];
			String title = GuiConfiguration.optionsString[i].split(":")[2];
			GuiConfiguration.optionsString[i] = GuiConfiguration.optionsString[i].replaceFirst("INSERT", getString(cat, title) + "");
		}
		for (int i = 0; i < GuiConfiguration.optionsInteger.length; i++) {
			String cat = GuiConfiguration.optionsInteger[i].split(":")[1];
			String title = GuiConfiguration.optionsInteger[i].split(":")[2];
			GuiConfiguration.optionsInteger[i] = GuiConfiguration.optionsInteger[i].replaceFirst("INSERT", getInt(cat, title) + "");
		}
	}
}