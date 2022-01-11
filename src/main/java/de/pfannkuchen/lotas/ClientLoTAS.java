package de.pfannkuchen.lotas;

import com.mojang.blaze3d.vertex.PoseStack;

import de.pfannkuchen.lotas.loscreen.LoScreenManager;
import de.pfannkuchen.lotas.mods.KeybindManager;
import de.pfannkuchen.lotas.mods.RecorderMod;
import de.pfannkuchen.lotas.util.InternalTimer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.Util;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;

/**
 * LoTAS fabric mod core for the client only.
 * @author Pancake
 */
@Environment(EnvType.CLIENT)
public class ClientLoTAS implements ClientModInitializer {

	// Client-side LoTAS Singleton
	public static ClientLoTAS instance;
	// LoScreen Manager Singleton
	public static LoScreenManager loscreenmanager;
	// Keybind Manager Singleton
	public static KeybindManager keybindmanager;
	// Internal Timer Singleton
	public static InternalTimer internaltimer;
	// Recorder Mod Singleton
	public static RecorderMod recordermod;

	@Override
	public void onInitializeClient() {
		ClientLoTAS.instance = this;
		ClientLoTAS.loscreenmanager = new LoScreenManager();
		ClientLoTAS.keybindmanager = new KeybindManager(); // Also initializes this
		ClientLoTAS.internaltimer = new InternalTimer();
		ClientLoTAS.recordermod = new RecorderMod();
	}

	/**
	 * Executed after the rendering engine launches.
	 * @param mc Instance of minecraft
	 */
	public void onRenderInitialize(Minecraft mc) {
		// Initialize LoScreens
		ClientLoTAS.loscreenmanager.onGameInitialize(mc);
		// Update Tickrate Changer Minecraft Instance
		LoTAS.tickratechanger.mc = mc;
		// Update Tick Advance Minecraft Instance
		LoTAS.tickadvance.mc = mc;
		// Update Dupe Mod Minecraft Instance
		LoTAS.dupemod.mc = mc;
		// Update Savestate Mod Minecraft Instance
		LoTAS.savestatemod.mc = mc;
	}

	/**
	 * Executed every time the minecraft screen changes
	 * @param screen New Screen
	 * @param mc Instance of minecraft
	 * @return Should cancel
	 */
	public boolean onGuiUpdate(Screen screen, Minecraft mc) {
		// Trigger LoScreen Event
		return ClientLoTAS.loscreenmanager.onScreenUpdate(screen, mc);
	}

	/**
	 * Executed after every screen caught key press
	 * @param key Key that was pressed
	 */
	public void onKeyPress(int key) {
		// Trigger a key press event for LoScreens
		ClientLoTAS.loscreenmanager.onKeyPress(key);
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
		// Update LoScreens
		ClientLoTAS.loscreenmanager.onGameLoop(mc);
		// Update Key bindings
		ClientLoTAS.keybindmanager.onGameLoop(mc);
		// Update internal timer
		ClientLoTAS.internaltimer.advanceTime(Util.getMillis());
		// Update Recorder
		ClientLoTAS.recordermod.onRender(mc);
	}

	/**
	 * Executed after the gui screens are rendered.
	 * @param stack Pose Stack for rendering
	 * @param mc Instance of minecraft
	 */
	public void onRenderScreen(PoseStack stack, Minecraft mc) {
		// Render LoScreen
		ClientLoTAS.loscreenmanager.onGuiRender(stack, mc);
		// Render Savestate
		LoTAS.savestatemod.onRender();
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
		// Update Savestate Mod Callback
		LoTAS.savestatemod.onClientPacket(packet);
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
		// Update Savestate Mod
		LoTAS.savestatemod.onDisconnect();
	}

}
