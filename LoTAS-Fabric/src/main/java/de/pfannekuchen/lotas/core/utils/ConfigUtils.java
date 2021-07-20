package de.pfannekuchen.lotas.core.utils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import de.pfannekuchen.lotas.gui.ConfigurationScreen;

/** 
 * The Configuration can read and store keys in a file using MinecraftForge's Configuration.
 * @author Pancake
 * @since v1.0
 * @version v1.0
 */
public class ConfigUtils {

	public static Properties props = new Properties();
	private static File configuration;

	/**
	 * Stores a string in the configuration object.
	 * @param category Category where the key is being store (tools, ui, etc.).
	 * @param key Name of the stored variable
	 * @param value Value of the variable that is being stored at category/key
	 * @see #save() for saving the configuartion.
	 */
	public static void setString(String category, String key, String value) {
		props.setProperty(category + ":" + key, value);
	}

	/**
	 * Stores an integer in the configuration object.
	 * @param category Category where the key is being store (tools, ui, etc.).
	 * @param key Name of the stored variable
	 * @param value Value of the variable that is being stored at category/key
	 * @see #save() for saving the configuartion.
	 */
	public static void setInt(String category, String key, int value) {
		props.setProperty(category + ":" + key, value + "");
	}

	/**
	 * Stores a boolean in the configuration object.
	 * @param category Category where the key is being store (tools, ui, etc.).
	 * @param key Name of the stored variable
	 * @param value Value of the variable that is being stored at category/key
	 * @see #save() for saving the configuartion.
	 */
	public static void setBoolean(String category, String key, boolean value) {
		props.setProperty(category + ":" + key, Boolean.toString(value));
	}

	/**
	 * Obtains a boolean from the configuration object from category::key.
	 * @param category Category where the key is stored (tools, ui, etc.).
	 * @param key Name of the stored variable to obtain
	 * @see #init() for loading the configuartion.
	 */
	public static boolean getBoolean(String category, String key) {
		return Boolean.valueOf(props.getProperty(category + ":" + key, "false"));
	}

	/**
	 * Obtains a string from the configuration object from category::key.
	 * @param category Category where the key is stored (tools, ui, etc.).
	 * @param key Name of the stored variable to obtain
	 * @see #init() for loading the configuartion.
	 */
	public static String getString(String category, String key) {
		return props.getProperty(category + ":" + key, "null");
	}

	/**
	 * Obtains an integer from the configuration object from category::key.
	 * @param category Category where the key is stored (tools, ui, etc.).
	 * @param key Name of the stored variable to obtain
	 * @see #init() for loading the configuartion.
	 */
	public static int getInt(String category, String key) {
		return Integer.valueOf(props.getProperty(category + ":" + key, "-1"));
	}
	
	/**
	 * Saves the configuration to the file that MinecraftForge requests
	 * @see #init() for loading the configuration.
	 */
	public static void save() {
		try {
			FileWriter writer = new FileWriter(configuration);
			props.store(writer, "LoTAS Configuration File");
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Loads the configuration to a file requested by MinecraftForge.
	 * @param configuration Configuration instance requested by MinecraftForge.
	 * @see #save() for saving the configuration
	 */
	public static void init(File configuration) throws IOException {
		ConfigUtils.configuration = configuration;
		if (!configuration.exists())
			configuration.createNewFile();
		props = new Properties();
		FileReader reader = new FileReader(configuration);
		props.load(reader);
		reader.close();

		/* Setup default values to avoid crashing or recommended options */
		if (ConfigUtils.getInt("hidden", "tickrate") <= 0)
			ConfigUtils.setInt("hidden", "tickrate", 6);
		if (ConfigUtils.getInt("hidden", "explosionoptimization") <= 0)
			ConfigUtils.setInt("hidden", "explosionoptimization", 100);
		ConfigUtils.save();

		/* Fill the configuration gui with the values read from the instantiated configuration file, so that the ConfigurationGui does not display placeholder values */
		for (int i = 0; i < ConfigurationScreen.optionsBoolean.length; i++) {
			String cat = ConfigurationScreen.optionsBoolean[i].split(":")[1];
			String title = ConfigurationScreen.optionsBoolean[i].split(":")[2];
			// Replace the 'INSERT' placeholder from the array entry with the actual configuration entry
			ConfigurationScreen.optionsBoolean[i] = ConfigurationScreen.optionsBoolean[i].replaceFirst("INSERT", getBoolean(cat, title) + "");
		}
		/* Do the same for all strings */
		for (int i = 0; i < ConfigurationScreen.optionsString.length; i++) {
			String cat = ConfigurationScreen.optionsString[i].split(":")[1];
			String title = ConfigurationScreen.optionsString[i].split(":")[2];
			ConfigurationScreen.optionsString[i] = ConfigurationScreen.optionsString[i].replaceFirst("INSERT", getString(cat, title) + "");
		}
		/* And lastly for all the integers */
		for (int i = 0; i < ConfigurationScreen.optionsInteger.length; i++) {
			String cat = ConfigurationScreen.optionsInteger[i].split(":")[1];
			String title = ConfigurationScreen.optionsInteger[i].split(":")[2];
			ConfigurationScreen.optionsInteger[i] = ConfigurationScreen.optionsInteger[i].replaceFirst("INSERT", getInt(cat, title) + "");
		}
	}
}