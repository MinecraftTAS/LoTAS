package de.pfannkuchen.lotas;

import com.mojang.blaze3d.vertex.PoseStack;

import de.pfannkuchen.lotas.gui.MainLoScreen;
import de.pfannkuchen.lotas.loscreen.LoScreenManager;
import de.pfannkuchen.lotas.mods.KeybindManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;

/**
 * LoTAS Fabric Mod Core for the Client only.
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
	
	@Override
	public void onInitializeClient() {
		ClientLoTAS.instance = this;
		ClientLoTAS.loscreenmanager = new LoScreenManager();
		ClientLoTAS.keybindmanager = new KeybindManager(); // Also initializes this
	}

	/**
	 * Executed after the rendering engine launches.
	 * @param mc Instance of Minecraft
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
	 * Executed every time the Minecraft Screen changes
	 * @param screen New Screen
	 * @param mc Instance of Minecraft
	 * @return Should cancel
	 */
	public boolean onGuiUpdate(Screen screen, Minecraft mc) {
		// Trigger LoScreen Event
		return loscreenmanager.onScreenUpdate(screen, mc);
	}
	
	/**
	 * Executed before the JVM stops.
	 * @param mc Instance of Minecraft
	 */
	public void onShutdown(Minecraft mc) {
		
	}
	
	/**
	 * Executed every tick of the game.
	 * @param mc Instance of Minecraft
	 */
	public void onTick(Minecraft mc) {
		// Tick Tick Advance
		LoTAS.tickadvance.onTick(mc);
	}
	
	/**
	 * Executed every time the game logic loops.
	 * @param mc Instance of Minecraft
	 */
	public void onGameLoop(Minecraft mc) {
		// Update LoScreens
		ClientLoTAS.loscreenmanager.onGameLoop(mc);
		// Update Keybindings
		ClientLoTAS.keybindmanager.onGameLoop(mc);
	}

	/**
	 * Executed after the gui screens are rendered. Only executed while in a world
	 * @param stack Pose Stack for rendering
	 * @param delta Render Partial Ticks
	 * @param mc Instance of Minecraft
	 */
	public void onRenderScreen(PoseStack stack, Minecraft mc) {
		// Render LoScreen
		loscreenmanager.onGuiRender(stack, mc);
	}
	
	/**
	 * Executed after the options are being initialized.
	 * @param keyMappings Standard Key Mappings
	 * @return Modified Key Mappings
	 */
	public KeyMapping[] onKeybindInitialize(KeyMapping[] keyMappings) {
		return keybindmanager.onKeybindInitialize(keyMappings);
	}

	/**
	 * Toggles on or off the LoTAS Menu and opens a Gui Screen in case there isn't one to regain the cursor.
	 * @param mc Instance of Minecraft
	 */
	public void toggleLoTASMenu(Minecraft mc) {
		if (mc.level == null) return;
		if (loscreenmanager.isScreenOpened()) {
			if (loscreenmanager.getScreen() instanceof MainLoScreen) 
				loscreenmanager.setScreen(null);
		} else {
			loscreenmanager.setScreen(new MainLoScreen());
		}
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
	
}
