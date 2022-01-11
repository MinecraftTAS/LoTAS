package de.pfannkuchen.lotas.mods;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

/**
 * The Configuration can read and store keys in a file using java's configuration.
 * @author Pancake
 */
public class ConfigManager {

	private Properties props = new Properties();
	private File configuration;

	/**
	 * Stores a string in the configuration object.
	 * @param category Category where the key is being store (tools, ui, etc.).
	 * @param key Name of the stored variable
	 * @param value Value of the variable that is being stored at category/key
	 * @see #save() for saving the configuartion.
	 */
	public void setString(String category, String key, String value) {
		this.props.setProperty(category + ":" + key, value);
	}

	/**
	 * Stores an integer in the configuration object.
	 * @param category Category where the key is being store (tools, ui, etc.).
	 * @param key Name of the stored variable
	 * @param value Value of the variable that is being stored at category/key
	 * @see #save() for saving the configuartion.
	 */
	public void setInt(String category, String key, int value) {
		this.props.setProperty(category + ":" + key, value + "");
	}

	/**
	 * Stores a double in the configuration object.
	 * @param category Category where the key is being store (tools, ui, etc.).
	 * @param key Name of the stored variable
	 * @param value Value of the variable that is being stored at category/key
	 * @see #save() for saving the configuartion.
	 */
	public void setDouble(String category, String key, double value) {
		this.props.setProperty(category + ":" + key, value + "");
	}

	/**
	 * Stores a boolean in the configuration object.
	 * @param category Category where the key is being store (tools, ui, etc.).
	 * @param key Name of the stored variable
	 * @param value Value of the variable that is being stored at category/key
	 * @see #save() for saving the configuartion.
	 */
	public void setBoolean(String category, String key, boolean value) {
		this.props.setProperty(category + ":" + key, Boolean.toString(value));
	}

	/**
	 * Obtains a boolean from the configuration object from category::key.
	 * @param category Category where the key is stored (tools, ui, etc.).
	 * @param key Name of the stored variable to obtain
	 * @see #init() for loading the configuartion.
	 */
	public boolean getBoolean(String category, String key) {
		return Boolean.parseBoolean(this.props.getProperty(category + ":" + key, "false"));
	}

	/**
	 * Obtains a string from the configuration object from category::key.
	 * @param category Category where the key is stored (tools, ui, etc.).
	 * @param key Name of the stored variable to obtain
	 * @see #init() for loading the configuartion.
	 */
	public String getString(String category, String key) {
		return this.props.getProperty(category + ":" + key, "null");
	}

	/**
	 * Obtains a double from the configuration object from category::key.
	 * @param category Category where the key is stored (tools, ui, etc.).
	 * @param key Name of the stored variable to obtain
	 * @see #init() for loading the configuartion.
	 */
	public double getDouble(String category, String key) {
		return Double.parseDouble(this.props.getProperty(category + ":" + key, "-1"));
	}

	/**
	 * Obtains an integer from the configuration object from category::key.
	 * @param category Category where the key is stored (tools, ui, etc.).
	 * @param key Name of the stored variable to obtain
	 * @see #init() for loading the configuartion.
	 */
	public int getInt(String category, String key) {
		return Integer.parseInt(this.props.getProperty(category + ":" + key, "-1"));
	}

	/**
	 * Saves the configuration to a file
	 * @see #init() for loading the configuration.
	 */
	public void save() {
		try {
			FileWriter writer = new FileWriter(this.configuration);
			this.props.store(writer, "LoTAS Configuration File");
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Loads the configuration to a file.
	 * @param configuration Configuration file
	 * @see #save() for saving the configuration
	 */
	public ConfigManager(File configuration) {
		try {
			this.configuration = configuration;
			if (!configuration.exists())
				configuration.createNewFile();
			this.props = new Properties();
			FileReader reader = new FileReader(configuration);
			this.props.load(reader);
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}