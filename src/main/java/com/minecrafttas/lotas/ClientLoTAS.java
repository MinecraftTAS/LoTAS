package com.minecrafttas.lotas;

import com.minecrafttas.lotas.system.ModSystem;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/**
 * LoTAS fabric mod core for the client
 *
 * @author Pancake
 */
@Environment(EnvType.CLIENT)
public class ClientLoTAS implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ModSystem.onClientsideInitialize();
	}

}
