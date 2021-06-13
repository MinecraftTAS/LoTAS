package de.pfannekuchen.lotas.core.utils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import de.pfannekuchen.lotas.gui.ConfigurationScreen;

public class ConfigUtils {

    public static Properties props = new Properties();
    private static File configuration;

    public static void setString(String category, String key, String value) {
        props.setProperty(category + ":" + key, value);
    }

    public static void setInt(String category, String key, int value) {
        props.setProperty(category + ":" + key, value + "");
    }

    public static void setBoolean(String category, String key, boolean value) {
        props.setProperty(category + ":" + key, Boolean.toString(value));
    }

    public static boolean getBoolean(String category, String key) {
        return Boolean.valueOf(props.getProperty(category + ":" + key, "false"));
    }

    public static String getString(String category, String key) {
        return props.getProperty(category + ":" + key, "null");
    }

    public static int getInt(String category, String key) {
        return Integer.valueOf(props.getProperty(category + ":" + key, "-1"));
    }

    public static void save() {
        try {
            FileWriter writer = new FileWriter(configuration);
            props.store(writer, "LoTAS Configuration File");
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void init(File configuration) throws IOException {
        ConfigUtils.configuration = configuration;
        if (!configuration.exists()) configuration.createNewFile();
        props = new Properties();
        FileReader reader = new FileReader(configuration);
        props.load(reader);
        reader.close();

        // Defaults
		// Defaults
		if (ConfigUtils.getInt("hidden", "tickrate") <= 0) ConfigUtils.setInt("hidden", "tickrate", 6);
		if (ConfigUtils.getInt("hidden", "explosionoptimization") <= 0) ConfigUtils.setInt("hidden", "explosionoptimization", 100);
		ConfigUtils.save();
		
		for (int i = 0; i < ConfigurationScreen.optionsBoolean.length; i++) {
			String cat = ConfigurationScreen.optionsBoolean[i].split(":")[1];
			String title = ConfigurationScreen.optionsBoolean[i].split(":")[2];
			ConfigurationScreen.optionsBoolean[i] = ConfigurationScreen.optionsBoolean[i].replaceFirst("INSERT", getBoolean(cat, title) + "");
		}
		for (int i = 0; i < ConfigurationScreen.optionsString.length; i++) {
			String cat = ConfigurationScreen.optionsString[i].split(":")[1];
			String title = ConfigurationScreen.optionsString[i].split(":")[2];
			ConfigurationScreen.optionsString[i] = ConfigurationScreen.optionsString[i].replaceFirst("INSERT", getString(cat, title) + "");
		}
		for (int i = 0; i < ConfigurationScreen.optionsInteger.length; i++) {
			String cat = ConfigurationScreen.optionsInteger[i].split(":")[1];
			String title = ConfigurationScreen.optionsInteger[i].split(":")[2];
			ConfigurationScreen.optionsInteger[i] = ConfigurationScreen.optionsInteger[i].replaceFirst("INSERT", getInt(cat, title) + "");
		}
    }
}