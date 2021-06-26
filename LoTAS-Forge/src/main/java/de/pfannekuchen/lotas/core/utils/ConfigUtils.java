package de.pfannekuchen.lotas.core.utils;

import de.pfannekuchen.lotas.gui.GuiConfiguration;

/** 
 * The Configuration can read and store keys in a file using MinecraftForge's Configuration.
 * @author Pancake
 * @since v1.0
 * @version v1.0
 */
public class ConfigUtils {

	/** Configuration instance that handles the File IO and properties Object */
	public static net.minecraftforge.common.config.Configuration conf;
	
	/**
	 * Stores a string in the configuration object.
	 * @param category Category where the key is being store (tools, ui, etc.).
	 * @param key Name of the stored variable
	 * @param value Value of the variable that is being stored at category/key
	 * @see #save() for saving the configuartion.
	 */
	public static void setString(String category, String key, String value) {
		conf.get(category, key, "").set(value);
	}
	
	/**
	 * Stores an integer in the configuration object.
	 * @param category Category where the key is being store (tools, ui, etc.).
	 * @param key Name of the stored variable
	 * @param value Value of the variable that is being stored at category/key
	 * @see #save() for saving the configuartion.
	 */
	public static void setInt(String category, String key, int value) {
		conf.get(category, key, "").set(value);
	}
	
	/**
	 * Stores a boolean in the configuration object.
	 * @param category Category where the key is being store (tools, ui, etc.).
	 * @param key Name of the stored variable
	 * @param value Value of the variable that is being stored at category/key
	 * @see #save() for saving the configuartion.
	 */
	public static void setBoolean(String category, String key, boolean value) {
		conf.get(category, key, "").set(value);
	}
	
	/**
	 * Obtains a boolean from the configuration object from category::key.
	 * @param category Category where the key is stored (tools, ui, etc.).
	 * @param key Name of the stored variable to obtain
	 * @see #init() for loading the configuartion.
	 */
	public static boolean getBoolean(String category, String key) {
		return conf.get(category, key, false).getBoolean();
	}
	
	/**
	 * Obtains a string from the configuration object from category::key.
	 * @param category Category where the key is stored (tools, ui, etc.).
	 * @param key Name of the stored variable to obtain
	 * @see #init() for loading the configuartion.
	 */
	public static String getString(String category, String key) {
		return conf.get(category, key, "null").getString();
	}
	
	/**
	 * Obtains an integer from the configuration object from category::key.
	 * @param category Category where the key is stored (tools, ui, etc.).
	 * @param key Name of the stored variable to obtain
	 * @see #init() for loading the configuartion.
	 */
	public static int getInt(String category, String key) {
		return conf.get(category, key, -1).getInt();
	}
	
	/**
	 * Saves the configuration to the file that MinecraftForge requests
	 * @see #init() for loading the configuration.
	 */
	public static void save() {
		conf.save();
	}
	 
	/**
	 * Loads the configuration to a file requested by MinecraftForge.
	 * @param configuration Configuration instance requested by MinecraftForge.
	 * @see #save() for saving the configuration
	 */
	public static void init(net.minecraftforge.common.config.Configuration configuration) {
		conf = configuration;
		conf.load();
		
		/* Setup default values to avoid crashing or recommended options */
		if (ConfigUtils.getInt("hidden", "tickrate") <= 0) ConfigUtils.setInt("hidden", "tickrate", 6); // Set the saved tickrate to 6 if unset, to avoid tickrate 0 when launching the game
		if (ConfigUtils.getInt("hidden", "explosionoptimization") <= 0) ConfigUtils.setInt("hidden", "explosionoptimization", 100);	// Set the explosionoptimization to 100% if it was set or unset to lower than 0 to avoid feature being useless
		ConfigUtils.save(); // Save the configuration file with the new changes if any
		
		/* Fill the configuration gui with the values read from the instantiated configuration file, so that the ConfigurationGui does not display placeholder values */
		for (int i = 0; i < GuiConfiguration.optionsBoolean.length; i++) {
			String cat = GuiConfiguration.optionsBoolean[i].split(":")[1];
			String title = GuiConfiguration.optionsBoolean[i].split(":")[2];
			// Replace the 'INSERT' placeholder from the array entry with the actual configuration entry
			GuiConfiguration.optionsBoolean[i] = GuiConfiguration.optionsBoolean[i].replaceFirst("INSERT", getBoolean(cat, title) + "");
		}
		/* Do the same for all strings */
		for (int i = 0; i < GuiConfiguration.optionsString.length; i++) {
			String cat = GuiConfiguration.optionsString[i].split(":")[1];
			String title = GuiConfiguration.optionsString[i].split(":")[2];
			GuiConfiguration.optionsString[i] = GuiConfiguration.optionsString[i].replaceFirst("INSERT", getString(cat, title) + "");
		}
		/* And lastly for all the integers */
		for (int i = 0; i < GuiConfiguration.optionsInteger.length; i++) {
			String cat = GuiConfiguration.optionsInteger[i].split(":")[1];
			String title = GuiConfiguration.optionsInteger[i].split(":")[2];
			GuiConfiguration.optionsInteger[i] = GuiConfiguration.optionsInteger[i].replaceFirst("INSERT", getInt(cat, title) + "");
		}
	}
}