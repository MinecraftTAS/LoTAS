package com.minecrafttas.lotas.system;

import com.minecrafttas.lotas.mods.*;
import com.minecrafttas.lotas.mods.DragonManipulation.Phase;
import lombok.Getter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.glfw.GLFW;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * This class manages keybinds and their categories.
 * @author Pancake
 */
@Environment(EnvType.CLIENT)
public class KeybindSystem {

	/** List of keybinds */
	private static final Keybind[] KEYBINDS = {
		new Keybind("Advance a tick", "Tickrate Changer", GLFW.GLFW_KEY_F9, true, () -> {
			TickAdvance.instance.requestTickadvance();
		}),
		new Keybind("Toggle tick advance", "Tickrate Changer", GLFW.GLFW_KEY_F8, true, () -> {
			TickAdvance.instance.requestTickadvanceToggle();
		}),
		new Keybind("Decrease Tickrate", "Tickrate Changer", GLFW.GLFW_KEY_COMMA, true, () -> {
			TickrateChanger.instance.decreaseTickrate();
		}),
		new Keybind("Increase Tickrate", "Tickrate Changer", GLFW.GLFW_KEY_PERIOD, true, () -> {
			TickrateChanger.instance.increaseTickrate();
		}),
		new Keybind("Save playerdata", "Duplication", GLFW.GLFW_KEY_N, true, () -> {
			DupeMod.instance.requestDupe(true);
		}),
		new Keybind("Load playerdata", "Duplication", GLFW.GLFW_KEY_M, true, () -> {
			DupeMod.instance.requestDupe(false);
		}),
		new Keybind("Manipulate to holding pattern phase", "Dragon Manipulation", GLFW.GLFW_KEY_Y, true, () -> { // hotkey just for testing, will be removed with gui
			DragonManipulation.instance.requestPhaseChange(Phase.HOLDINGPATTERN);
		}),
		new Keybind("Manipulate to landing approach phase", "Dragon Manipulation", GLFW.GLFW_KEY_X, true, () -> { // hotkey just for testing, will be removed with gui
			DragonManipulation.instance.requestPhaseChange(Phase.LANDINGAPPROACH);
		}),
		new Keybind("Manipulate to strafing phase", "Dragon Manipulation", GLFW.GLFW_KEY_C, true, () -> { // hotkey just for testing, will be removed with gui
			DragonManipulation.instance.requestPhaseChange(Phase.STRAFING);
		}),
		new Keybind("Disable manipulation", "Dragon Manipulation", GLFW.GLFW_KEY_V, true, () -> { // hotkey just for testing, will be removed with gui
			DragonManipulation.instance.requestPhaseChange(Phase.OFF);
		}),
		new Keybind("Savestate", "Savestates", GLFW.GLFW_KEY_J, true, () -> {
			SavestateMod.instance.requestState(0, -1, "Test");
		}),
		new Keybind("Loadstate", "Savestates", GLFW.GLFW_KEY_K, true, () -> {
			SavestateMod.instance.requestState(1, 0, null);
		}),
		new Keybind("Deletestate", "Savestates", GLFW.GLFW_KEY_I, true, () -> {
			SavestateMod.instance.requestState(2, 0, null);
		}),
	};

	/**
	 * This class represents a keybind
	 * @author Pancake
	 */
	private static class Keybind {

		/** Minecraft key mapping */
		@Getter
		private KeyMapping keyMapping;

		/** Category of the keybind in the controls menu */
		private String category;

		/** Should the keybind only be available if mc.level is not null */
		private boolean isInGame;

		/** Ran when keybind is pressed */
		private Runnable onKeyDown;

		/**
		 * Initialize keybind
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

	}

	/**
	 * Initialize keybind system and register categories and key binds.
	 */
	public static KeyMapping[] onKeybindInitialize(KeyMapping[] keyMappings) {
		// initialize categories
		Map<String, Integer> categories = KeyMapping.CATEGORY_SORT_ORDER;
		for (int i = 0; i < KEYBINDS.length; i++)
			if (!categories.containsKey(KEYBINDS[i].category))
				categories.put(KEYBINDS[i].category, i + 8);

		// add keybinds
		return ArrayUtils.addAll(keyMappings, Arrays.asList(KEYBINDS).stream().map(Keybind::getKeyMapping).toArray(KeyMapping[]::new)); // convert Keybind array to KeyMapping on the fly
	}

	/**
	 * Trigger keybinds on key press
	 * @param mc Instance of minecraft
	 */
	public static void onGameLoop(Minecraft mc) {
		for (Keybind keybind : KEYBINDS) {
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
	 * Check whether key has been pressed this frame.
	 * @param mc Instance of minecraft
	 * @param map Key mapping to check
	 * @return Key has been pressed in this frame
	 */
	private static boolean isKeyDown(Minecraft mc, KeyMapping map) {
		// check if in a text field
		Screen screen = mc.screen;
		if (screen != null && ((screen.getFocused() instanceof EditBox && ((EditBox) screen.getFocused()).canConsumeInput()) || screen.getFocused() instanceof RecipeBookComponent))
			return false;

		// check if key is just pressed
		boolean wasPressed = keys.containsKey(map) ? keys.get(map) : false;
		boolean isPressed = GLFW.glfwGetKey(mc.getWindow().getWindow(), map.key.getValue()) == GLFW.GLFW_PRESS;
		keys.put(map, isPressed);
		return !wasPressed && isPressed;
	}

}
