package de.pfannkuchen.lotas;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.pfannkuchen.lotas.mods.ConfigManager;
import de.pfannkuchen.lotas.mods.TickAdvance;
import de.pfannkuchen.lotas.mods.TickrateChanger;
import net.fabricmc.api.ModInitializer;
import net.minecraft.network.protocol.game.ServerboundCustomPayloadPacket;
import net.minecraft.server.MinecraftServer;

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
	// Tick Advance Singleton
	public static TickAdvance tickadvance;
	// Config Manager Singleton
	public static ConfigManager configmanager;
	
	/**
	 * Executed after the game launches.
	 */
	@Override
	public void onInitialize() {
		LoTAS.instance = this; // Prepare the singleton
		LoTAS.tickratechanger = new TickrateChanger();
		LoTAS.tickadvance = new TickAdvance();
		LoTAS.configmanager = new ConfigManager(new File("lotas_develop.properties"));
	}
	
	/**
	 * Executed after the server launches.
	 * @param server New Server
	 */
	public void onServerLoad(MinecraftServer server) {
		// Update Tickrate Changer Instance
		LoTAS.tickratechanger.mcserver = server;
		// Update Tick Advance Instance
		LoTAS.tickadvance.mcserver = server;
	}
	
	/**
	 * Executed every time the server receives a custom payload packet.
	 * @param packet Packet In
	 */
	public void onServerPayload(ServerboundCustomPayloadPacket packet) {
		// Update Tickrate Changer Handler
		LoTAS.tickratechanger.onServerPacket(packet);
		// Update Tick Advance Handler
		LoTAS.tickadvance.onServerPacket(packet);
	}
	
}