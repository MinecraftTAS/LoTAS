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
	 * Key binding for opening and closing the LoTAS menu LoScreen.
	 */
	private KeyMapping openLoTASMenuKeybind = new KeyMapping("Open LoTAS Menu", GLFW.GLFW_KEY_R, "LoTAS Keybinds");

	/**
	 * Key binding for advancing a single tick while being in tick advance.
	 */
	private KeyMapping tickadvanceKeybind = new KeyMapping("Advance a tick", GLFW.GLFW_KEY_F9, "LoTAS Keybinds");

	/**
	 * Key binding for advancing a single tick while being in tick advance.
	 */
	private KeyMapping toggleTickadvance = new KeyMapping("Toggle tick advance", GLFW.GLFW_KEY_F8, "LoTAS Keybinds");

	/**
	 * Categories for all key binds used.
	 */
	private String[] keybindCategories = {"LoTAS Keybinds"};

	/**
	 * Initializes the key bind Manager, registers categories and key binds.
	 */
	public KeyMapping[] onKeybindInitialize(KeyMapping[] keyMappings) {
		// Initialize Categories first
		final Map<String, Integer> categories = MixinKeyMappingsAccessor.getCategorySorting();
		for (int i = 0; i < this.keybindCategories.length; i++) categories.put(this.keybindCategories[i], i+8);
		// Finish by adding Keybinds
		return ArrayUtils.addAll(keyMappings, this.openLoTASMenuKeybind, this.tickadvanceKeybind, this.toggleTickadvance);
	}

	/**
	 * Watches out for key presses and triggers sub events.
	 * @param mc Instance of minecraft
	 */
	public void onGameLoop(Minecraft mc) {
		if (this.isKeyDown(mc, this.openLoTASMenuKeybind) && mc.level != null)
			ClientLoTAS.loscreenmanager.toggleLoTASMenu(mc);
		else if (this.isKeyDown(mc, this.tickadvanceKeybind) && mc.level != null)
			LoTAS.tickadvance.requestTickadvance();
		else if (this.isKeyDown(mc, this.toggleTickadvance) && mc.level != null)
			LoTAS.tickadvance.requestTickadvanceToggle();
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
		boolean isPressed = GLFW.glfwGetKey(mc.getWindow().getWindow(), ((MixinKeyMappingsAccessor) map).getKey().getValue()) == GLFW.GLFW_PRESS;
		this.keys.put(map, isPressed);
		return !wasPressed && isPressed;
	}

}
