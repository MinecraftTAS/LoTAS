package de.pfannkuchen.lotas;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.pfannkuchen.lotas.mods.TickrateChanger;
import net.fabricmc.api.ModInitializer;

/**
 * LoTAS Fabric Mod Core.
 * @author Pancake
 */
public class LoTAS implements ModInitializer {
	
	// LoTAS Logger for printing debug lines into the console.
	public static final Logger LOGGER = LogManager.getLogger("lotas");
	// LoTAS Singleton
	public static LoTAS instance;
	// Tickrate Changer Singleton
	public static TickrateChanger tickratechanger;
	
	/**
	 * Executed after the game launches.
	 */
	@Override
	public void onInitialize() {
		LoTAS.instance = this; // Prepare the singleton
		LoTAS.tickratechanger = new TickrateChanger();
	}
	
}