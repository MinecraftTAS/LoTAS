package de.pfannkuchen.lotas.mods;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.glfw.GLFW;

import de.pfannkuchen.lotas.ClientLoTAS;
import de.pfannkuchen.lotas.LoTAS;
import de.pfannkuchen.lotas.gui.RecorderLoScreen;
import de.pfannkuchen.lotas.mixin.client.accessors.AccessorKeyMapping;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;

/**
 * Manages keybinds and their categories.
 * @author Pancake
 */
@Environment(EnvType.CLIENT)
public class KeybindManager {

	/**
	 * Key binding for opening and closing the LoTAS menu LoScreen.
	 */
	private KeyMapping openLoTASMenuKeybind = new KeyMapping("Open LoTAS Menu", GLFW.GLFW_KEY_R, "LoTAS Keybinds");

	/**
	 * Key binding for opening the recording menu
	 */
	private KeyMapping openRecordingMenuKeybind = new KeyMapping("Open Recording Menu", GLFW.GLFW_KEY_F6, "LoTAS Keybinds");
	
	/**
	 * Key binding for advancing a single tick while being in tick advance.
	 */
	private KeyMapping tickadvanceKeybind = new KeyMapping("Advance a tick", GLFW.GLFW_KEY_F9, "LoTAS Keybinds");

	/**
	 * Key binding for advancing a single tick while being in tick advance.
	 */
	private KeyMapping toggleTickadvance = new KeyMapping("Toggle tick advance", GLFW.GLFW_KEY_F8, "LoTAS Keybinds");
	
	/**
	 * Key binding for decreasing the tickrate
	 */
	private KeyMapping decreaseTickrate = new KeyMapping("Decrease Tickrate", GLFW.GLFW_KEY_COMMA, "LoTAS Keybinds");
	
	/**
	 * Key binding for increasing the tickrate
	 */
	private KeyMapping increaseTickrate = new KeyMapping("Increase Tickrate", GLFW.GLFW_KEY_PERIOD, "LoTAS Keybinds");
	
	/**
	 * Key binding for increasing the tickrate
	 */
	private KeyMapping savePlayerdata = new KeyMapping("Save playerdata", GLFW.GLFW_KEY_N, "LoTAS Keybinds");
	
	/**
	 * Key binding for decreasing the tickrate
	 */
	private KeyMapping loadPlayerdata = new KeyMapping("Load playerdata", GLFW.GLFW_KEY_M, "LoTAS Keybinds");

	/**
	 * Categories for all key binds used.
	 */
	private String[] keybindCategories = {"LoTAS Keybinds"};

	/**
	 * Initializes the key bind Manager, registers categories and key binds.
	 */
	public KeyMapping[] onKeybindInitialize(KeyMapping[] keyMappings) {
		// Initialize Categories first
		final Map<String, Integer> categories = AccessorKeyMapping.getCategorySorting();
		for (int i = 0; i < this.keybindCategories.length; i++) categories.put(this.keybindCategories[i], i+8);
		// Finish by adding Keybinds
		return ArrayUtils.addAll(keyMappings, this.openLoTASMenuKeybind, this.tickadvanceKeybind, this.toggleTickadvance, this.decreaseTickrate, this.increaseTickrate, this.savePlayerdata, this.loadPlayerdata);
	}

	/**
	 * Watches out for key presses and triggers sub events.
	 * @param mc Instance of minecraft
	 */
	public void onGameLoop(Minecraft mc) {
		if (this.isKeyDown(mc, this.openLoTASMenuKeybind) && mc.level != null)
			ClientLoTAS.loscreenmanager.toggleLoTASMenu(mc);
		if (this.isKeyDown(mc, this.openRecordingMenuKeybind) && mc.level != null)
			ClientLoTAS.loscreenmanager.setScreen(new RecorderLoScreen());
		else if (this.isKeyDown(mc, this.tickadvanceKeybind) && mc.level != null)
			LoTAS.tickadvance.requestTickadvance();
		else if (this.isKeyDown(mc, this.toggleTickadvance) && mc.level != null)
			LoTAS.tickadvance.requestTickadvanceToggle();
		else if (this.isKeyDown(mc, this.decreaseTickrate) && mc.level != null)
			LoTAS.tickratechanger.decreaseTickrate();
		else if (this.isKeyDown(mc, this.increaseTickrate) && mc.level != null)
			LoTAS.tickratechanger.increaseTickrate();
		else if (this.isKeyDown(mc, this.savePlayerdata) && mc.level != null)
			LoTAS.dupemod.requestDupe(true);
		else if (this.isKeyDown(mc, this.loadPlayerdata) && mc.level != null)
			LoTAS.dupemod.requestDupe(false);
		
	}

	/**
	 * Map of pressed/non-pressed Keys.
	 */
	private Map<KeyMapping, Boolean> keys = new HashMap<>();

	/**
	 * Checks whether a key has been pressed recently.
	 * @param mc Instance of minecraft
	 * @param map Key mappings to check
	 * @return Key has been pressed recently
	 */
	private boolean isKeyDown(Minecraft mc, KeyMapping map) {
		boolean wasPressed = this.keys.containsKey(map) ? this.keys.get(map) : false;
		boolean isPressed = GLFW.glfwGetKey(mc.getWindow().getWindow(), ((AccessorKeyMapping) map).getKey().getValue()) == GLFW.GLFW_PRESS;
		this.keys.put(map, isPressed);
		return !wasPressed && isPressed;
	}

}
