package de.pfannekuchen.lotas.core.utils;

import java.time.Duration;

import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.glfw.GLFW;

import de.pfannekuchen.lotas.gui.HudSettings;
import de.pfannekuchen.lotas.mods.DupeMod;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.PauseScreen;

/**
 * Contains all keybinds and handles key inputs
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
	/* Toggles freecamming */
	public static final KeyMapping toggleFreecamKeybind = new KeyMapping("Freecam", GLFW.GLFW_KEY_I, "Moving");
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
	/** Variable that shows whether the player is currently freecamming */
	public static boolean isFreecaming;
	/** Temporary variable that will reset the tickrate when leaving freecam */
	public static int savedTickrate;
	/** Temporary variable that will state whether the hold strafe key was pressed a tick before */
	public static boolean wasPressed = false;

	/**
	 * Handles a new KeyInputEvent and ticks through all keybinds.
	 */
	public static void keyEvent() {
		// Savestate and Loadstate handling
		while (saveStateKeybind.consumeClick()) {
			shouldSavestate = true;
			Minecraft.getInstance().setScreen(new PauseScreen(true));
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
			Minecraft.getInstance().setScreen(new HudSettings());
		}
		// Autostrafe auto rotation handling
		if (wasPressed != holdStrafeKeybind.isDown() && wasPressed == true) {
			KeyMapping.set(Minecraft.getInstance().options.keyRight.getDefaultKey(), false);
		} else if (wasPressed != holdStrafeKeybind.isDown() && wasPressed == false) {
			Minecraft.getInstance().player.yRot -= 45;
		}
		wasPressed = holdStrafeKeybind.isDown();
		if(test.consumeClick()) {
//			SpawnManipMod manip=new SpawnManipMod();
//			manip.debugSpawn();
		}
	}

	/**
	 * Static method to register all Keybinds to the game.
	 * Note: Not using Fabric API to avoid a crash using Mojang Mappings
	 */
	public static void registerKeybinds() {
		ArrayUtils.addAll(Minecraft.getInstance().options.keyMappings, saveStateKeybind, loadStateKeybind, loadDupeKeybind, saveDupeKeybind, holdStrafeKeybind, toggleFreecamKeybind, increaseTickrateKeybind, decreaseTickrateKeybind, advanceTicksKeybind, toggleAdvanceKeybind, toggleTimerKeybind, openInfoHud);
	}
}