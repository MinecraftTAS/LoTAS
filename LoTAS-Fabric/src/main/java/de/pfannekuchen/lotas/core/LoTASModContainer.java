package de.pfannekuchen.lotas.core;

import de.pfannekuchen.lotas.core.utils.KeybindsUtils;
import net.fabricmc.api.ModInitializer;

public class LoTASModContainer implements ModInitializer {

	@Override
	public void onInitialize() {
		KeybindsUtils.registerKeybinds();
	}
}
