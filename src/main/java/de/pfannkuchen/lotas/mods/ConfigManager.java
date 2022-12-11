package de.pfannkuchen.lotas.mods;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import de.pfannkuchen.lotas.LoTAS;

/**
 * The Configuration can read and store keys in a file using java's configuration.
 * @author Pancake
 */
public class ConfigManager {

	private Properties props = new Properties();
	private File configuration;

	/**
	 * Loads the configuration to a file.
	 * @param configuration Configuration file
	 */
	public ConfigManager(File configuration) {
		try {
			this.configuration = configuration;
			if (!configuration.exists())
				configuration.createNewFile();
			this.props = new Properties();
			var reader = new FileReader(configuration);
			this.props.load(reader);
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Stores something in the configuration.
	 * @param key Name of the variable
	 * @param value Value of the variable
	 */
	private void setProperty(String key, String value) {
		this.props.setProperty(key, value);
	}

	/**
	 * Obtains something from the configuration.
	 * @param key Name of the variable
	 * @param def Default value
	 */
	private String getProperty(String key, String def) {
		return this.props.getProperty(key, def);
	}

	/**
	 * Verifies whether a property is present in the configuration or not.
	 * @param key Name of tghe variable
	 * @return Value present
	 */
	public boolean hasProperty(String key) {
		return this.props.containsKey(key);
	}

	/**
	 * Saves the configuration to a file
	 */
	public void save() {
		try {
			var writer = new FileWriter(this.configuration);
			this.props.store(writer, "LoTAS Configuration File");
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// generic set methods

	/**
	 * Stores a string in the configuration.
	 * @param key Name of the variable
	 * @param value Value of the variable
	 */
	public void setString(String key, String value) {
		this.setProperty(key, value);
	}

	/**
	 * Stores a char in the configuration.
	 * @param key Name of the variable
	 * @param value Value of the variable
	 */
	public void setChar(String key, char value) {
		this.setProperty(key, (short) value + "");
	}

	/**
	 * Stores a byte in the configuration.
	 * @param key Name of the variable
	 * @param value Value of the variable
	 */
	public void setByte(String key, byte value) {
		this.setProperty(key, value + "");
	}

	/**
	 * Stores a short in the configuration.
	 * @param key Name of the variable
	 * @param value Value of the variable
	 */
	public void setShort(String key, short value) {
		this.setProperty(key, value + "");
	}

	/**
	 * Stores an int in the configuration.
	 * @param key Name of the variable
	 * @param value Value of the variable
	 */
	public void setInt(String key, int value) {
		this.setProperty(key, value + "");
	}

	/**
	 * Stores a long in the configuration.
	 * @param key Name of the variable
	 * @param value Value of the variable
	 */
	public void setLong(String key, long value) {
		this.setProperty(key, value + "");
	}

	/**
	 * Stores a double in the configuration.
	 * @param key Name of the variable
	 * @param value Value of the variable
	 */
	public void setDouble(String key, double value) {
		this.setProperty(key, Double.doubleToLongBits(value) + "");
	}

	/**
	 * Stores a float in the configuration.
	 * @param key Name of the variable
	 * @param value Value of the variable
	 */
	public void setFloat(String key, float value) {
		this.setProperty(key, Float.floatToIntBits(value) + "");
	}

	/**
	 * Stores a boolean in the configuration.
	 * @param key Name of the variable
	 * @param value Value of the variable
	 */
	public void setBoolean(String key, boolean value) {
		this.setProperty(key, value + "");
	}

	// generic get methods

	/**
	 * Obtains a string from the configuration.
	 * @param key Name of the variable
	 * @param def Default value
	 * @return Value or default
	 */
	public String getString(String key, String def) {
		return this.getProperty(key, def);
	}

	/**
	 * Obtains a char from the configuration.
	 * @param key Name of the variable
	 * @param def Default value
	 * @return Value or default
	 */
	public char getChar(String key, char def) {
		var stringVal = this.getProperty(key, (short) def + "");
		try {
			return (char) Short.parseShort(stringVal);
		} catch (NumberFormatException e) {
			LoTAS.LOGGER.warn("Configuration file for key \"" + key + "\" contains invalid char value \"" + stringVal + "\".");
		}
		return def;
	}

	/**
	 * Obtains a byte from the configuration.
	 * @param key Name of the variable
	 * @param def Default value
	 * @return Value or default
	 */
	public byte getByte(String key, byte def) {
		var stringVal = this.getProperty(key, def + "");
		try {
			return Byte.parseByte(stringVal);
		} catch (NumberFormatException e) {
			LoTAS.LOGGER.warn("Configuration file for key \"" + key + "\" contains invalid byte value \"" + stringVal + "\".");
		}
		return def;
	}

	/**
	 * Obtains a short from the configuration.
	 * @param key Name of the variable
	 * @param def Default value
	 * @return Value or default
	 */
	public short getShort(String key, short def) {
		var stringVal = this.getProperty(key, def + "");
		try {
			return Short.parseShort(stringVal);
		} catch (NumberFormatException e) {
			LoTAS.LOGGER.warn("Configuration file for key \"" + key + "\" contains invalid short value \"" + stringVal + "\".");
		}
		return def;
	}

	/**
	 * Obtains an int from the configuration.
	 * @param key Name of the variable
	 * @param def Default value
	 * @return Value or default
	 */
	public int getInt(String key, int def) {
		var stringVal = this.getProperty(key, def + "");
		try {
			return Integer.parseInt(stringVal);
		} catch (NumberFormatException e) {
			LoTAS.LOGGER.warn("Configuration file for key \"" + key + "\" contains invalid int value \"" + stringVal + "\".");
		}
		return def;
	}

	/**
	 * Obtains a long from the configuration.
	 * @param key Name of the variable
	 * @param def Default value
	 * @return Value or default
	 */
	public long getLong(String key, long def) {
		var stringVal = this.getProperty(key, def + "");
		try {
			return Long.parseLong(stringVal);
		} catch (NumberFormatException e) {
			LoTAS.LOGGER.warn("Configuration file for key \"" + key + "\" contains invalid long value \"" + stringVal + "\".");
		}
		return def;
	}

	/**
	 * Obtains a double from the configuration.
	 * @param key Name of the variable
	 * @param def Default value
	 * @return Value or default
	 */
	public double getDouble(String key, double def) {
		var stringVal = this.getProperty(key, Double.doubleToLongBits(def) + "");
		try {
			return Double.longBitsToDouble(Long.parseLong(stringVal));
		} catch (NumberFormatException e) {
			LoTAS.LOGGER.warn("Configuration file for key \"" + key + "\" contains invalid double value \"" + stringVal + "\".");
		}
		return def;
	}

	/**
	 * Obtains a float from the configuration.
	 * @param key Name of the variable
	 * @param def Default value
	 * @return Value or default
	 */
	public float getFloat(String key, float def) {
		var stringVal = this.getProperty(key, Float.floatToIntBits(def) + "");
		try {
			return Float.intBitsToFloat(Integer.parseInt(stringVal));
		} catch (NumberFormatException e) {
			LoTAS.LOGGER.warn("Configuration file for key \"" + key + "\" contains invalid float value \"" + stringVal + "\".");
		}
		return def;
	}

	/**
	 * Obtains a boolean from the configuration.
	 * @param key Name of the variable
	 * @param def Default value
	 * @return Value or default
	 */
	public boolean getBoolean(String key, boolean def) {
		var stringVal = this.getProperty(key, def + "");
		try {
			return Boolean.parseBoolean(stringVal);
		} catch (NumberFormatException e) {
			LoTAS.LOGGER.warn("Configuration file for key \"" + key + "\" contains invalid boolean value \"" + stringVal + "\".");
		}
		return def;
	}

}
