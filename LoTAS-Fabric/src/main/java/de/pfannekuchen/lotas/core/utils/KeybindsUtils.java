package de.pfannekuchen.lotas.core.utils;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.lwjgl.glfw.GLFW;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import de.pfannekuchen.lotas.core.LoTASModContainer;
import de.pfannekuchen.lotas.mixin.accessors.AccessorKeyMapping;
import de.pfannekuchen.lotas.mods.DupeMod;
import de.pfannekuchen.lotas.mods.TickrateChangerMod;
import net.minecraft.Util;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.controls.ControlsScreen;
import net.minecraft.client.gui.screens.inventory.SignEditScreen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.util.Mth;

/**
 * Contains all keybinds and handles key inputs
 * 
 * @author Pancake
 * @since v1.0
 * @version v1.1
 */
public class KeybindsUtils {

	/* Keybind that will savestate */
	public static final KeyMapping saveStateKeybind = new KeyMapping("keybind.lotas.savestate", GLFW.GLFW_KEY_J, "keybind.category.lotas.stating"); //"Savestate" "Stating"
	/* Keybind that will load a state */
	public static final KeyMapping loadStateKeybind = new KeyMapping("keybind.lotas.loadstate", GLFW.GLFW_KEY_K, "keybind.category.lotas.stating"); //"Loadstate"
	/* Keybind that will load your Inventory */
	public static final KeyMapping loadDupeKeybind = new KeyMapping("keybind.lotas.dupe.load", GLFW.GLFW_KEY_O, "keybind.category.lotas.duping"); //"Load Items/Chests" "Duping"
	/* Keybind that will save your inventory */
	public static final KeyMapping saveDupeKeybind = new KeyMapping("keybind.lotas.dupe.save", GLFW.GLFW_KEY_P, "keybind.category.lotas.duping"); //"Save Items/Chests"
	/* Keybind used for automated strafing */
	public static final KeyMapping holdStrafeKeybind = new KeyMapping("keybind.lotas.autostrafe", GLFW.GLFW_KEY_H, "keybind.category.lotas.moving"); //"Auto-Strafe" "Moving"
	/* Increases the Tickrate */
	public static final KeyMapping increaseTickrateKeybind = new KeyMapping("keybind.lotas.tickrate.faster", GLFW.GLFW_KEY_PERIOD, "keybind.category.lotas.tickratechanger"); //"Faster Tickrate" "Tickrate Changer"
	/* Decreases the Tickrate */
	public static final KeyMapping decreaseTickrateKeybind = new KeyMapping("keybind.lotas.tickrate.slower", GLFW.GLFW_KEY_COMMA, "keybind.category.lotas.tickratechanger");//"Slower Tickrate"
	/* Advances a single tick while in Tick Advance Mode */
	public static final KeyMapping advanceTicksKeybind = new KeyMapping("keybind.lotas.tickrate.tickadvance", GLFW.GLFW_KEY_F9, "keybind.category.lotas.tickratechanger");//"Advance Tick"
	/* Toggles Tickrate Zero (aka Tick Advance Mode) */
	public static final KeyMapping toggleAdvanceKeybind = new KeyMapping("keybind.lotas.tickrate.tick0", GLFW.GLFW_KEY_F8, "keybind.category.lotas.tickratechanger"); //"Tickrate Zero Toggle"
	/* Starts or stops the Timer */
	public static final KeyMapping toggleTimerKeybind = new KeyMapping("keybind.lotas.tickrate.timer", GLFW.GLFW_KEY_KP_5, "keybind.category.lotas.tickratechanger"); //"Start/Stop Timer"
	/* Opens the Info Hud Editor */
	public static final KeyMapping openInfoHud = new KeyMapping("keybind.lotas.infogui", GLFW.GLFW_KEY_F6, "keybind.category.lotas.misc");//"Open InfoGui Editor" "Misc"
	/* ^_____^ */
	public static final KeyMapping test = new KeyMapping("keybind.lotas.test", GLFW.GLFW_KEY_F12, "keybind.category.lotas.misc"); //"Test"
	/** Temporary variable used to request a savestate */
	public static boolean shouldSavestate;
	/** Temporary variable used to request a loadstate */
	public static boolean shouldLoadstate;

	public static int save;

	/**
	 * Temporary variable that will state whether the hold strafe key was pressed a
	 * tick before
	 */
	public static boolean wasPressed = false;
	
	private static HashMap<Integer, Long> cooldownHashMap = new HashMap<Integer, Long>();

	/**
	 * Handles a new KeyInputEvent and ticks through all keybinds.
	 */
	public static void tickKeyEvent() {
		// Savestate and Loadstate handling
		while (saveStateKeybind.consumeClick()) {
			Minecraft.getInstance().setScreen(new PauseScreen(true));
			shouldSavestate = true;
		}
		while (loadStateKeybind.consumeClick()) {
			Minecraft.getInstance().setScreen(new PauseScreen(true));
			shouldLoadstate = true;
		}
		// Dupemod save and load handling.
		while (loadDupeKeybind.consumeClick()) {
			DupeMod.load(Minecraft.getInstance());
		}
		while (saveDupeKeybind.consumeClick()) {
			DupeMod.save(Minecraft.getInstance());
		}
		// Timer
		while (toggleTimerKeybind.consumeClick()) {
			if (Timer.ticks < 1 || Timer.startTime == null) {
				Timer.startTime = Duration.ofMillis(System.currentTimeMillis());
				Timer.ticks = 1;
			}
			Timer.running = !Timer.running;
		}
		// Info Hud handling
		while (openInfoHud.consumeClick()) {
			Minecraft.getInstance().setScreen(LoTASModContainer.hud);
		}
		// Autostrafe auto rotation handling
		if (wasPressed != holdStrafeKeybind.isDown() && wasPressed == true) {
			Minecraft mc = Minecraft.getInstance();
			KeyMapping.set(mc.options.keyRight.getDefaultKey(), false);
		}
		wasPressed = holdStrafeKeybind.isDown();
		if (test.consumeClick()) {
		}
	}

	/**
	 * Handles keybinds executed every frame
	 */
	public static void frameKeyEvent() {
		/* Tickrate Zero Toggle */
		if (KeybindsUtils.toggleAdvanceKeybind.consumeClick() && TickrateChangerMod.advanceClient == false && Minecraft.getInstance().screen == null) {
			if (TickrateChangerMod.tickrate > 0) {
				save = TickrateChangerMod.index;
				TickrateChangerMod.updateTickrate(0);
				TickrateChangerMod.index = 0;
			} else {
				TickrateChangerMod.updateTickrate(TickrateChangerMod.ticks[save]);
				TickrateChangerMod.index = save;
			}
		}

		/* Tickrate Zero */
		if (TickrateChangerMod.tickrate == 0 && KeybindsUtils.advanceTicksKeybind.consumeClick()) {
			TickrateChangerMod.advanceTick();
		}
		
		/* Tickrate Changer */
		boolean flag = false;
		if (KeybindsUtils.increaseTickrateKeybind.consumeClick()) {
			flag = true;
			TickrateChangerMod.index++;
		} else if (KeybindsUtils.decreaseTickrateKeybind.consumeClick()) {
			flag = true;
			TickrateChangerMod.index--;
		}
		if (flag) {
			TickrateChangerMod.index = Mth.clamp(TickrateChangerMod.index, 0, 11);
			TickrateChangerMod.updateTickrate(TickrateChangerMod.ticks[TickrateChangerMod.index]);
		}
	}

	/**
	 * Checks whether the keycode is pressed, regardless of any gui screens
	 * 
	 * @param keybind
	 * @return
	 */
	public static boolean isKeyDown(KeyMapping keybind) {
		return isKeyDown(getKeyCodeFromKeybind(keybind));
	}

	public static boolean isKeyDownExceptTextField(KeyMapping keybind) {
		Minecraft mc = Minecraft.getInstance();
		Screen screen=mc.screen;
		
		if(screen instanceof ChatScreen || screen instanceof SignEditScreen) {
			return false;
		}
		return isKeyDown(keybind);
	}
	
	public static boolean isKeyDown(int keycode) {
		boolean down = false;
		Minecraft mc = Minecraft.getInstance();
		Screen screen=mc.screen;
		
		if (screen instanceof ControlsScreen) {
			return false;
		}

		down = keycode > 0 ? Keyboard.isKeyDown(keycode) : Mouse.isKeyDown(keycode); // Check if it's keyboard key or mouse button

		if (down) {
			if (cooldownHashMap.containsKey(keycode)) {
				if (50 * 3 <= Util.getMillis() - cooldownHashMap.get(keycode)) {
					cooldownHashMap.put(keycode, Util.getMillis());
					return true;
				}
				return false;
			} else {
				cooldownHashMap.put(keycode, Util.getMillis());
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Static method to register all Keybinds to the game. Note: Not using Fabric
	 * API to avoid a crash using Mojang Mappings
	 * 
	 * @return
	 */
	public static KeyMapping[] registerKeybinds(KeyMapping[] keyMappings) {
		List<KeyMapping> modded = new ArrayList<KeyMapping>(ImmutableList.of(
				saveStateKeybind,
				loadStateKeybind,
				loadDupeKeybind,
				saveDupeKeybind,
				holdStrafeKeybind,
				increaseTickrateKeybind,
				decreaseTickrateKeybind,
				advanceTicksKeybind,
				toggleAdvanceKeybind,
				toggleTimerKeybind,
				openInfoHud/*,
				test*/));

		addCategories(modded);

		List<KeyMapping> newKeyMappings = Lists.newArrayList(keyMappings);
		newKeyMappings.removeAll(modded);
		newKeyMappings.addAll(modded);

		return newKeyMappings.toArray(new KeyMapping[0]);
	}

	public static int getKeyCodeFromKeybind(KeyMapping keybind) {
		return ((AccessorKeyMapping)keybind).getKey().getValue();
	}
	
	private static void addCategories(List<KeyMapping> modded) {
		modded.forEach(key -> {
			Map<String, Integer> map = AccessorKeyMapping.getCategorySorting();
			String categoryName = key.getCategory();
			if (map.containsKey(categoryName)) {
				return;
			}

			Optional<Integer> largest = map.values().stream().max(Integer::compareTo);
			int largestInt = largest.orElse(0);
			map.put(categoryName, largestInt + 1);
		});
	}
}