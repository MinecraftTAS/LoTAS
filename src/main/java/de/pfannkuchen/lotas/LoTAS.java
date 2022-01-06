package de.pfannkuchen.lotas;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.pfannkuchen.lotas.mods.ConfigManager;
import de.pfannkuchen.lotas.mods.DupeMod;
import de.pfannkuchen.lotas.mods.SavestateMod;
import de.pfannkuchen.lotas.mods.TickAdvance;
import de.pfannkuchen.lotas.mods.TickrateChanger;
import net.fabricmc.api.ModInitializer;
import net.minecraft.network.protocol.game.ServerboundCustomPayloadPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

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
	// Dupe Mod Singleton
	public static DupeMod dupemod;
	// Savestate Mod Singleton
	public static SavestateMod savestatemod;
	
	/**
	 * Executed after the game launches.
	 */
	@Override
	public void onInitialize() {
		LoTAS.instance = this; // Prepare the singleton
		LoTAS.tickratechanger = new TickrateChanger();
		LoTAS.tickadvance = new TickAdvance();
		LoTAS.configmanager = new ConfigManager(new File("lotas_develop.properties"));
		LoTAS.dupemod = new DupeMod();
		LoTAS.savestatemod = new SavestateMod();
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
		// Update Dupe Mod Handler
		LoTAS.dupemod.mcserver = server;
		// Update Savestate Mod Handler
		LoTAS.savestatemod.mcserver = server;
	}
	
	/**
	 * Executed after/before every server tick
	 * @param server Server Instance
	 */
	public void onServerTick(MinecraftServer server) {
		// Tick Savestate Mod
		LoTAS.savestatemod.afterServerTick(server);
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
		// Update Dupe Mod Handler
		LoTAS.dupemod.onServerPacket(packet);
		// Update Savestate Mod Handler
		LoTAS.savestatemod.onServerPacket(packet);
	}

	/**
	 * Executed if a client connects
	 */
	public void onClientConnect(ServerPlayer c) {
		// Update Dupe Mod
		LoTAS.dupemod.onConnect(c);
		// Update Tick Advance
		LoTAS.tickadvance.onConnect(c);
		// Update Tickrate Changer
		LoTAS.tickratechanger.onConnect(c);
		// Update Savestate Mod
		LoTAS.savestatemod.onConnect(c);
	}
	
}