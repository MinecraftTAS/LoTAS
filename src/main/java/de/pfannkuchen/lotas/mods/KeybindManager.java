package de.pfannkuchen.lotas.mods;

import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.glfw.GLFW;

import net.minecraft.client.KeyMapping;

/**
 * Manages keybinds.
 * @author Pancake
 */
public class KeybindManager {

	/**
	 * Keybinding for opening and closing the LoTAS Menu LoScreen.
	 */
	private KeyMapping openLoTASMenuKeybind = new KeyMapping("Open LoTAS Menu", GLFW.GLFW_KEY_R, "LoTAS");
	
	/**
	 * Initializes the Keybind Manager, registers keybinds.
	 */
	public KeyMapping[] onKeybindInitialize(KeyMapping[] keyMappings) {
		return ArrayUtils.addAll(keyMappings, openLoTASMenuKeybind);
	}
	
}
