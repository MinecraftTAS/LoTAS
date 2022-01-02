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
	private KeyMapping openLoTASMenuKeybind = new KeyMapping("key.openlotasmenu", GLFW.GLFW_KEY_R, "key.categories.lotas");
	
	/**
	 * Keybinding for advancing a single tick while being in Tick Advance.
	 */
	private KeyMapping tickadvanceKeybind = new KeyMapping("key.tickadvance", GLFW.GLFW_KEY_TAB, "key.categories.lotas");
	
	/**
	 * Keybinding for advancing a single tick while being in Tick Advance.
	 */
	private KeyMapping toggleTickadvance = new KeyMapping("key.toggletickadvance", GLFW.GLFW_KEY_F8, "key.categories.lotas");
	
	/**
	 * Categories for all keybinds used.
	 */
	private String[] keybindCategories = new String[] {"key.categories.lotas"};
	
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
		if (isKeyDown(mc, openLoTASMenuKeybind))
			ClientLoTAS.loscreenmanager.toggleLoTASMenu(mc);
		else if (isKeyDown(mc, tickadvanceKeybind))
			LoTAS.tickadvance.requestTickadvance();
		else if (isKeyDown(mc, toggleTickadvance))
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
