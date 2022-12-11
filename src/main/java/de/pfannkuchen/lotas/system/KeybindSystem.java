package de.pfannkuchen.lotas.system;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.glfw.GLFW;

import de.pfannkuchen.lotas.LoTAS;
import de.pfannkuchen.lotas.mixin.client.accessors.AccessorKeyMapping;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;

/**
 * Manages keybinds and their categories.
 * @author Pancake
 */
@Environment(EnvType.CLIENT)
public class KeybindSystem {

	/**
	 * List of keybinds
	 */
	// @formatter:off
	private static Keybind[] keybinds = {
		new Keybind("Advance a tick", "Tickrate Changer", GLFW.GLFW_KEY_F9, true, () -> {
			LoTAS.tickadvance.requestTickadvance();
		}),
		new Keybind("Toggle tick advance", "Tickrate Changer", GLFW.GLFW_KEY_F8, true, () -> {
			LoTAS.tickadvance.requestTickadvanceToggle();
		}),
		new Keybind("Decrease Tickrate", "Tickrate Changer", GLFW.GLFW_KEY_COMMA, true, () -> {
			LoTAS.tickratechanger.decreaseTickrate();
		}),
		new Keybind("Increase Tickrate", "Tickrate Changer", GLFW.GLFW_KEY_PERIOD, true, () -> {
			LoTAS.tickratechanger.increaseTickrate();
		}),
		new Keybind("Save playerdata", "Duplication", GLFW.GLFW_KEY_N, true, () -> {
			LoTAS.dupemod.requestDupe(true);
		}),
		new Keybind("Load playerdata", "Duplication", GLFW.GLFW_KEY_M, true, () -> {
			LoTAS.dupemod.requestDupe(false);
		}),
	};
	// @formatter:on

	/**
	 * Represents a keybind
	 * @author Pancake
	 */
	private static class Keybind {

		/**
		 * Minecraft key mapping
		 */
		private KeyMapping keyMapping;

		/**
		 * Category of the keybind in the controls menu
		 */
		private String category;

		/**
		 * Should the keybind only be available if mc.level is not null
		 */
		private boolean isInGame;

		/**
		 * Will be run when the keybind is pressed
		 */
		private Runnable onKeyDown;

		/**
		 * Initializes a keybind
		 * @param name Name of the keybind
		 * @param category Category of the keybind
		 * @param defaultKey Default key of the keybind
		 * @param isInGame Should the keybind only be available if mc.level is not null
		 * @param onKeyDown Will be run when the keybind is pressed
		 */
		public Keybind(String name, String category, int defaultKey, boolean isInGame, Runnable onKeyDown) {
			this.keyMapping = new KeyMapping(name, defaultKey, category);
			this.category = category;
			this.isInGame = isInGame;
			this.onKeyDown = onKeyDown;
		}

		/**
		 * Returns the minecraft key mapping
		 * @return Minecraft key mapping
		 */
		public KeyMapping getKeyMapping() {
			return this.keyMapping;
		}

	}

	/**
	 * Initializes the keybind Manager, registers categories and key binds.
	 */
	public static KeyMapping[] onKeybindInitialize(KeyMapping[] keyMappings) {
		// Initialize categories
		var categories = AccessorKeyMapping.getCategorySorting();
		for (var i = 0; i < keybinds.length; i++)
			if (!categories.containsKey(keybinds[i].category))
				categories.put(keybinds[i].category, i + 8);
		// Add keybinds
		return ArrayUtils.addAll(keyMappings, Arrays.asList(keybinds).stream().map(Keybind::getKeyMapping).toArray(KeyMapping[]::new)); // convert Keybind array to KeyMapping on the fly
	}

	/**
	 * Watches out for key presses and triggers sub events.
	 * @param mc Instance of minecraft
	 */
	public static void onGameLoop(Minecraft mc) {
		for (Keybind keybind : keybinds) {
			if (keybind.isInGame && mc.level == null || !isKeyDown(mc, keybind.getKeyMapping()))
				continue;
			keybind.onKeyDown.run();
		}
	}

	/**
	 * Map of pressed/non-pressed keys.
	 */
	private static Map<KeyMapping, Boolean> keys = new HashMap<>();

	/**
	 * Checks whether a key has been pressed recently.
	 * @param mc Instance of minecraft
	 * @param map Key mappings to check
	 * @return Key has been pressed recently
	 */
	private static boolean isKeyDown(Minecraft mc, KeyMapping map) {
		// Check if in a text field
		var screen = mc.screen;
		if (screen != null && screen.getFocused() instanceof EditBox && ((EditBox) screen.getFocused()).canConsumeInput())
			return false;

		var wasPressed = keys.containsKey(map) ? keys.get(map) : false;
		var isPressed = GLFW.glfwGetKey(mc.getWindow().getWindow(), ((AccessorKeyMapping) map).getKey().getValue()) == GLFW.GLFW_PRESS;
		keys.put(map, isPressed);
		return !wasPressed && isPressed;
	}

}
