package de.pfannkuchen.lotas;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.pfannkuchen.lotas.system.ConfigurationSystem;
import de.pfannkuchen.lotas.system.ModSystem;
import net.fabricmc.api.ModInitializer;

/**
 * LoTAS fabric mod core.
 * @author Pancake
 */
public class LoTAS implements ModInitializer {

	// LoTAS Logger for printing debug lines into the console.
	public static final Logger LOGGER = LogManager.getLogger("lotas");

	/**
	 * Executed after the game launches.
	 */
	@Override
	public void onInitialize() {
		ConfigurationSystem.load(new File("lotas_develop.properties"));
		ModSystem.onInitialize();
	}

}
