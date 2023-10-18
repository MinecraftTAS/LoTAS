package com.minecrafttas.lotas;

import com.minecrafttas.lotas.system.ConfigurationSystem;
import com.minecrafttas.lotas.system.ModSystem;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

/**
 * LoTAS fabric mod core.
 * @author Pancake
 */
public class LoTAS implements ModInitializer {

	/** Logger instance */
	public static final Logger LOGGER = LogManager.getLogger("lotas");

	@Override
	public void onInitialize() {
		ConfigurationSystem.load(new File("lotas_develop.properties"));
		ModSystem.onInitialize();
	}

}
