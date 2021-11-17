package de.pfannkuchen.lotas.mods;

import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.glfw.GLFW;

import de.pfannkuchen.lotas.mixin.client.MixinKeyMappingsAccessor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.KeyMapping;

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
	 * Categories for all keybinds used
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
		return ArrayUtils.addAll(keyMappings, openLoTASMenuKeybind);
	}
	
}
