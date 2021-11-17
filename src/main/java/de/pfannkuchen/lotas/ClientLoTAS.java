package de.pfannkuchen.lotas;

import com.mojang.blaze3d.vertex.PoseStack;

import de.pfannkuchen.lotas.gui.EmptyScreen;
import de.pfannkuchen.lotas.gui.MainLoScreen;
import de.pfannkuchen.lotas.loscreen.LoScreenManager;
import de.pfannkuchen.lotas.mods.KeybindManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;

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
			loscreenmanager.setScreen(null);
			// remove the temporary mc screen
			if (mc.screen instanceof EmptyScreen) mc.setScreen(null);
		} else {
			loscreenmanager.setScreen(new MainLoScreen());
			if (mc.screen == null) mc.setScreen(new EmptyScreen());
		}
	}
	
}
