package com.minecrafttas.lotas.system;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import static com.minecrafttas.lotas.LoTAS.LOGGER;

/**
 * Configuration system that can read and store keys in a file using java properties.
 *
 * @author Pancake
 */
public class ConfigurationSystem {

	/** Configuration object */
	private static Properties props = new Properties();

	/** Configuration file */
	private static File configurationFile;

	/**
	 * Load configuration from file
	 *
	 * @param configuration Configuration file
	 */
	public static void load(File configuration) {
		try {
			configurationFile = configuration;

			if (!configuration.exists())
				configuration.createNewFile();

			FileReader reader = new FileReader(configuration);
			props = new Properties();
			props.load(reader);
			reader.close();
		} catch (IOException e) {
			LOGGER.error("Could not load configuration file!", e);
		}
	}

	/**
	 * Save configuration to file
	 */
	public static void save() {
		try {
			FileWriter writer = new FileWriter(configurationFile);
			props.store(writer, "LoTAS Configuration File");
			writer.close();
		} catch (Exception e) {
			LOGGER.error("Could not save configuration file!", e);
		}
	}

	/**
	 * Store something in configuration.
	 *
	 * @param key Name of the variable
	 * @param value Value of the variable
	 */
	private static void setProperty(String key, String value) {
		props.setProperty(key, value);
	}

	/**
	 * Obtain something from configuration
	 *
	 * @param key Name of the variable
	 * @param def Default value
	 */
	private static String getProperty(String key, String def) {
		return props.getProperty(key, def);
	}

	/**
	 * Verify property is present in configuration
	 *
	 * @param key Name of the variable
	 * @return Value present
	 */
	public static boolean hasProperty(String key) {
		return props.containsKey(key);
	}

	// generic set methods

	/**
	 * Store string in configuration
	 *
	 * @param key Name of the variable
	 * @param value Value of the variable
	 */
	public static void setString(String key, String value) {
		setProperty(key, value);
	}

	/**
	 * Store char in configuration
	 *
	 * @param key Name of the variable
	 * @param value Value of the variable
	 */
	public static void setChar(String key, char value) {
		setProperty(key, (short) value + "");
	}

	/**
	 * Store byte in configuration
	 *
	 * @param key Name of the variable
	 * @param value Value of the variable
	 */
	public static void setByte(String key, byte value) {
		setProperty(key, value + "");
	}

	/**
	 * Store short in configuration
	 *
	 * @param key Name of the variable
	 * @param value Value of the variable
	 */
	public static void setShort(String key, short value) {
		setProperty(key, value + "");
	}

	/**
	 * Store int in configuration
	 *
	 * @param key Name of the variable
	 * @param value Value of the variable
	 */
	public static void setInt(String key, int value) {
		setProperty(key, value + "");
	}

	/**
	 * Store long in configuration
	 *
	 * @param key Name of the variable
	 * @param value Value of the variable
	 */
	public static void setLong(String key, long value) {
		setProperty(key, value + "");
	}

	/**
	 * Store double in configuration
	 *
	 * @param key Name of the variable
	 * @param value Value of the variable
	 */
	public static void setDouble(String key, double value) {
		setProperty(key, Double.doubleToLongBits(value) + "");
	}

	/**
	 * Store float in configuration
	 *
	 * @param key Name of the variable
	 * @param value Value of the variable
	 */
	public static void setFloat(String key, float value) {
		setProperty(key, Float.floatToIntBits(value) + "");
	}

	/**
	 * Store boolean in configuration
	 *
	 * @param key Name of the variable
	 * @param value Value of the variable
	 */
	public static void setBoolean(String key, boolean value) {
		setProperty(key, value + "");
	}

	// generic get methods

	/**
	 * Obtain string from configuration
	 *
	 * @param key Name of the variable
	 * @param def Default value
	 * @return Value or default
	 */
	public static String getString(String key, String def) {
		return getProperty(key, def);
	}

	/**
	 * Obtain char from configuration
	 *
	 * @param key Name of the variable
	 * @param def Default value
	 * @return Value or default
	 */
	public static char getChar(String key, char def) {
		String stringVal = getProperty(key, (short) def + "");
		try {
			return (char) Short.parseShort(stringVal);
		} catch (NumberFormatException e) {
			LOGGER.warn("Configuration file for key \"{}\" contains invalid char value \"{}\".", key, stringVal);
		}
		return def;
	}

	/**
	 * Obtain byte from configuration
	 *
	 * @param key Name of the variable
	 * @param def Default value
	 * @return Value or default
	 */
	public static byte getByte(String key, byte def) {
		String stringVal = getProperty(key, def + "");
		try {
			return Byte.parseByte(stringVal);
		} catch (NumberFormatException e) {
			LOGGER.warn("Configuration file for key \"{}\" contains invalid byte value \"{}\".", key, stringVal);
		}
		return def;
	}

	/**
	 * Obtain short from configuration
	 *
	 * @param key Name of the variable
	 * @param def Default value
	 * @return Value or default
	 */
	public static short getShort(String key, short def) {
		String stringVal = getProperty(key, def + "");
		try {
			return Short.parseShort(stringVal);
		} catch (NumberFormatException e) {
			LOGGER.warn("Configuration file for key \"{}\" contains invalid short value \"{}\".", key, stringVal);
		}
		return def;
	}

	/**
	 * Obtain int from configuration
	 *
	 * @param key Name of the variable
	 * @param def Default value
	 * @return Value or default
	 */
	public static int getInt(String key, int def) {
		String stringVal = getProperty(key, def + "");
		try {
			return Integer.parseInt(stringVal);
		} catch (NumberFormatException e) {
			LOGGER.warn("Configuration file for key \"{}\" contains invalid int value \"{}\".", key, stringVal);
		}
		return def;
	}

	/**
	 * Obtain long from configuration
	 *
	 * @param key Name of the variable
	 * @param def Default value
	 * @return Value or default
	 */
	public static long getLong(String key, long def) {
		String stringVal = getProperty(key, def + "");
		try {
			return Long.parseLong(stringVal);
		} catch (NumberFormatException e) {
			LOGGER.warn("Configuration file for key \"{}\" contains invalid long value \"{}\".", key, stringVal);
		}
		return def;
	}

	/**
	 * Obtain double from configuration
	 *
	 * @param key Name of the variable
	 * @param def Default value
	 * @return Value or default
	 */
	public static double getDouble(String key, double def) {
		String stringVal = getProperty(key, Double.doubleToLongBits(def) + "");
		try {
			return Double.longBitsToDouble(Long.parseLong(stringVal));
		} catch (NumberFormatException e) {
			LOGGER.warn("Configuration file for key \"{}\" contains invalid double value \"{}\".", key, stringVal);
		}
		return def;
	}

	/**
	 * Obtain float from configuration
	 *
	 * @param key Name of the variable
	 * @param def Default value
	 * @return Value or default
	 */
	public static float getFloat(String key, float def) {
		String stringVal = getProperty(key, Float.floatToIntBits(def) + "");
		try {
			return Float.intBitsToFloat(Integer.parseInt(stringVal));
		} catch (NumberFormatException e) {
			LOGGER.warn("Configuration file for key \"{}\" contains invalid float value \"{}\".", key, stringVal);
		}
		return def;
	}

	/**
	 * Obtain boolean from configuration
	 *
	 * @param key Name of the variable
	 * @param def Default value
	 * @return Value or default
	 */
	public static boolean getBoolean(String key, boolean def) {
		String stringVal = getProperty(key, def + "");
		try {
			return Boolean.parseBoolean(stringVal);
		} catch (NumberFormatException e) {
			LOGGER.warn("Configuration file for key \"{}\" contains invalid boolean value \"{}\".", key, stringVal);
		}
		return def;
	}

}
