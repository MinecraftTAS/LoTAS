package de.pfannkuchen.lotas.mods;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.glfw.GLFW;

import de.pfannkuchen.lotas.ClientLoTAS;
import de.pfannkuchen.lotas.LoTAS;
import de.pfannkuchen.lotas.mixin.client.MixinKeyMappingsAccessor;
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
	 * Keybinding for opening and closing the LoTAS Menu LoScreen.
	 */
	private KeyMapping openLoTASMenuKeybind = new KeyMapping("Open LoTAS Menu", GLFW.GLFW_KEY_R, "LoTAS Keybinds");
	
	/**
	 * Keybinding for advancing a single tick while being in Tick Advance.
	 */
	private KeyMapping tickadvanceKeybind = new KeyMapping("Advance a tick", GLFW.GLFW_KEY_F9, "LoTAS Keybinds");
	
	/**
	 * Keybinding for advancing a single tick while being in Tick Advance.
	 */
	private KeyMapping toggleTickadvance = new KeyMapping("Toggle tick advance", GLFW.GLFW_KEY_F8, "LoTAS Keybinds");
	
	/**
	 * Categories for all keybinds used.
	 */
	private String[] keybindCategories = new String[] {"LoTAS Keybinds"};
	
	/**
	 * Initializes the Keybind Manager, registers categories and keybinds.
	 */
	public KeyMapping[] onKeybindInitialize(KeyMapping[] keyMappings) {
		// Initialize Categories first
		final Map<String, Integer> categories = MixinKeyMappingsAccessor.getCategorySorting();
		for (int i = 0; i < keybindCategories.length; i++) categories.put(keybindCategories[i], i+8);
		// Finish by adding Keybinds
		return ArrayUtils.addAll(keyMappings, openLoTASMenuKeybind, tickadvanceKeybind, toggleTickadvance);
	}
	
	/**
	 * Watches out for key presses and triggers sub events.
	 * @param mc Instance of Minecraft
	 */
	public void onGameLoop(Minecraft mc) {
		if (isKeyDown(mc, openLoTASMenuKeybind) && mc.level != null)
			ClientLoTAS.loscreenmanager.toggleLoTASMenu(mc);
		else if (isKeyDown(mc, tickadvanceKeybind) && mc.level != null)
			LoTAS.tickadvance.requestTickadvance();
		else if (isKeyDown(mc, toggleTickadvance) && mc.level != null)
			LoTAS.tickadvance.requestTickadvanceToggle();
	}
	
	/**
	 * Map of pressed/unpressed Keys.
	 */
	private Map<KeyMapping, Boolean> keys = new HashMap<>();
	
	/**
	 * Checks whether a key has been pressed recently.
	 * @param mc Instance of Minecraft
	 * @param map Key Mappings to check
	 * @return Key has been pressed recently
	 */
	private boolean isKeyDown(Minecraft mc, KeyMapping map) {
		boolean wasPressed = keys.containsKey(map) ? keys.get(map) : false;
		boolean isPressed = GLFW.glfwGetKey(mc.getWindow().getWindow(), ((MixinKeyMappingsAccessor) map).getKey().getValue()) == GLFW.GLFW_PRESS;
		keys.put(map, isPressed);
		return !wasPressed && isPressed;
	}
	
}
