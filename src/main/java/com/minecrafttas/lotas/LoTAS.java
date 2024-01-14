package com.minecrafttas.lotas;

import com.minecrafttas.lotas.mods.*;
import com.minecrafttas.lotas.system.ConfigurationSystem;
import com.minecrafttas.lotas.system.ModSystem;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

/**
 * LoTAS fabric mod core.
 *
 * @author Pancake
 */
public class LoTAS implements ModInitializer {

	/** Logger instance */
	public static final Logger LOGGER = LogManager.getLogger("lotas");

	/** Dupe mod instance */
	public static final DupeMod DUPE_MOD = new DupeMod();
	/** Tick advance mod instance */
	public static final TickAdvance TICK_ADVANCE = new TickAdvance();
	/** Tickrate changer mod instance */
	public static final TickrateChanger TICKRATE_CHANGER = new TickrateChanger();
	/** Dragon manipulation mod instance */
	public static final DragonManipulation DRAGON_MANIPULATION = new DragonManipulation();
	/** Savestate mod instance */
	public static final SavestateMod SAVESTATE_MOD = new SavestateMod();

	static {
		ModSystem.registerMods(DUPE_MOD, TICK_ADVANCE, TICKRATE_CHANGER, DRAGON_MANIPULATION, SAVESTATE_MOD);
	}

	@Override
	public void onInitialize() {
		ConfigurationSystem.load(new File("lotas_dev.properties"));
		ModSystem.onInitialize();
	}

}
