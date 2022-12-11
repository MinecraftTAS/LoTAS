package de.pfannkuchen.lotas;

import de.pfannkuchen.lotas.mods.KeybindManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;

/**
 * LoTAS fabric mod core for the client only.
 * @author Pancake
 */
@Environment(EnvType.CLIENT)
public class ClientLoTAS implements ClientModInitializer {

	// Client-side LoTAS Singleton
	public static ClientLoTAS instance;
	// Keybind Manager Singleton
	public static KeybindManager keybindmanager;

	@Override
	public void onInitializeClient() {
		ClientLoTAS.instance = this;
		ClientLoTAS.keybindmanager = new KeybindManager(); // Also initializes this
	}

	/**
	 * Executed after the rendering engine launches.
	 * @param mc Instance of minecraft
	 */
	public void onRenderInitialize(Minecraft mc) {
		// Update Tickrate Changer Minecraft Instance
		LoTAS.tickratechanger.mc = mc;
		// Update Tick Advance Minecraft Instance
		LoTAS.tickadvance.mc = mc;
		// Update Dupe Mod Minecraft Instance
		LoTAS.dupemod.mc = mc;
		// Update Dragon Manipulation Mod Minecraft Instance
		LoTAS.dragonmanipulationmod.mc = mc;
	}

	/**
	 * Executed before the JVM stops.
	 * @param mc Instance of minecraft
	 */
	public void onShutdown(Minecraft mc) {

	}

	/**
	 * Executed every tick of the game.
	 * @param mc Instance of minecraft
	 */
	public void onTick(Minecraft mc) {
		// Tick Tick Advance
		LoTAS.tickadvance.onTick(mc);
	}

	/**
	 * Executed every time the game logic loops.
	 * @param mc Instance of minecraft
	 */
	public void onGameLoop(Minecraft mc) {
		// Update Key bindings
		ClientLoTAS.keybindmanager.onGameLoop(mc);
	}

	/**
	 * Executed after the options are being initialized.
	 * @param keyMappings Standard Key Mappings
	 * @return Modified Key Mappings
	 */
	public KeyMapping[] onKeybindInitialize(KeyMapping[] keyMappings) {
		return ClientLoTAS.keybindmanager.onKeybindInitialize(keyMappings);
	}

	/**
	 * Executed every time the client receives a custom payload packet.
	 * @param packet Packet In
	 */
	public void onClientPayload(ClientboundCustomPayloadPacket packet) {
		// Update Tickrate Changer Callback
		LoTAS.tickratechanger.onClientPacket(packet);
		// Update Tick Advance Callback
		LoTAS.tickadvance.onClientPacket(packet);
		// Update Dupe Mod Callback
		LoTAS.dupemod.onClientPacket(packet);
		// Update Dupe Mod Callback
		LoTAS.dragonmanipulationmod.onClientPacket(packet);
	}

	/**
	 * Executed if the client disconnects
	 */
	public void onClientDisconnect() {
		// Update Dupe Mod
		LoTAS.dupemod.onDisconnect();
		// Update Tick Advance
		LoTAS.tickadvance.onDisconnect();
		// Update Tickrate Changer
		LoTAS.tickratechanger.onDisconnect();
	}

}
