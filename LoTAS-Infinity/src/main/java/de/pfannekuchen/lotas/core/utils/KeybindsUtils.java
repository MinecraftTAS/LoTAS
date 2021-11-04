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
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.PauseScreen;
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
	public static final KeyMapping saveStateKeybind = new KeyMapping("Savestate", GLFW.GLFW_KEY_J, "Stating");
	/* Keybind that will load a state */
	public static final KeyMapping loadStateKeybind = new KeyMapping("Loadstate", GLFW.GLFW_KEY_K, "Stating");
	/* Keybind that will load your Inventory */
	public static final KeyMapping loadDupeKeybind = new KeyMapping("Load Items/Chests", GLFW.GLFW_KEY_O, "Duping");
	/* Keybind that will save your inventory */
	public static final KeyMapping saveDupeKeybind = new KeyMapping("Save Items/Chests", GLFW.GLFW_KEY_P, "Duping");
	/* Keybind used for automated strafing */
	public static final KeyMapping holdStrafeKeybind = new KeyMapping("Auto-Strafe", GLFW.GLFW_KEY_H, "Moving");
	/* Increases the Tickrate */
	public static final KeyMapping increaseTickrateKeybind = new KeyMapping("Faster Tickrate", GLFW.GLFW_KEY_PERIOD, "Tickrate Changer");
	/* Decreases the Tickrate */
	public static final KeyMapping decreaseTickrateKeybind = new KeyMapping("Slower Tickrate", GLFW.GLFW_KEY_COMMA, "Tickrate Changer");
	/* Advances a single tick while in Tick Advance Mode */
	public static final KeyMapping advanceTicksKeybind = new KeyMapping("Advance Tick", GLFW.GLFW_KEY_F9, "Tickrate Changer");
	/* Toggles Tickrate Zero (aka Tick Advance Mode) */
	public static final KeyMapping toggleAdvanceKeybind = new KeyMapping("Tickrate Zero Toggle", GLFW.GLFW_KEY_F8, "Tickrate Changer");
	/* Starts or stops the Timer */
	public static final KeyMapping toggleTimerKeybind = new KeyMapping("Start/Stop Timer", GLFW.GLFW_KEY_KP_5, "Tickrate Changer");
	/* Opens the Info Hud Editor */
	public static final KeyMapping openInfoHud = new KeyMapping("Open InfoGui Editor", GLFW.GLFW_KEY_F6, "Misc");
	/* ^_____^ */
	public static final KeyMapping test = new KeyMapping("Test", GLFW.GLFW_KEY_F12, "Misc");
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
			KeyMapping.set(Minecraft.getInstance().options.keyRight.getDefaultKey(), false);
		}
		wasPressed = holdStrafeKeybind.isDown();
		if (test.consumeClick()) {
//			SpawnManipMod manip=new SpawnManipMod();
//			manip.debugSpawn();
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
	 * Static method to register all Keybinds to the game. Note: Not using Fabric
	 * API to avoid a crash using Mojang Mappings
	 * 
	 * @return
	 */
	public static KeyMapping[] registerKeybinds(KeyMapping[] keyMappings) {
		Minecraft mc = Minecraft.getInstance();
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
				openInfoHud));

		addCategories(modded);

		List<KeyMapping> newKeyMappings = Lists.newArrayList(keyMappings);
		newKeyMappings.removeAll(modded);
		newKeyMappings.addAll(modded);

		return newKeyMappings.toArray(new KeyMapping[0]);
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