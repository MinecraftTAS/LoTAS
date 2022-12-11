package de.pfannkuchen.lotas;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.pfannkuchen.lotas.mods.TickrateChanger;
import de.pfannkuchen.lotas.system.ConfigurationSystem;
import de.pfannkuchen.lotas.system.ModSystem;
import net.fabricmc.api.ModInitializer;
import net.minecraft.network.protocol.game.ServerboundCustomPayloadPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

/**
 * LoTAS fabric mod core.
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
		ConfigurationSystem.load(new File("lotas_develop.properties"));
		ModSystem.onInitialize();
		LoTAS.tickratechanger = new TickrateChanger();
	}

	/**
	 * Executed after the server launches.
	 * @param server New Server
	 */
	public void onServerLoad(MinecraftServer server) {
		// Update Tickrate Changer Instance
		LoTAS.tickratechanger.mcserver = server;
	}

	/**
	 * Executed inbetween every server tick
	 * @param server Server Instance
	 */
	public void onServerTick(MinecraftServer server) {

	}

	/**
	 * Executed every time the server receives a custom payload packet.
	 * @param packet Packet In
	 */
	public void onServerPayload(ServerboundCustomPayloadPacket packet) {
		// Update Tickrate Changer Handler
		LoTAS.tickratechanger.onServerPacket(packet);
	}

	/**
	 * Executed if a client connects
	 */
	public void onClientConnect(ServerPlayer c) {
		// Update Tickrate Changer
		LoTAS.tickratechanger.onConnect(c);
	}

}
