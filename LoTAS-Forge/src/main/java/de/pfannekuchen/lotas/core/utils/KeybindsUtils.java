package de.pfannekuchen.lotas.core.utils;

import java.time.Duration;
import java.util.HashMap;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import de.pfannekuchen.lotas.core.LoTASModContainer;
import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.core.utils.EventUtils.Timer;
import de.pfannekuchen.lotas.mods.DupeMod;
import de.pfannekuchen.lotas.mods.TickrateChangerMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiControls;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.client.settings.KeyBinding;

/**
 * Contains all keybinds and handles key inputs
 * 
 * @author Pancake
 * @since v1.0
 * @version v1.1
 */
public class KeybindsUtils {

	/** Keybind that will save a state */
	public static final KeyBinding saveStateKeybind = new KeyBinding("keybind.lotas.savestate", Keyboard.KEY_J, "keybind.category.lotas.stating");
	/** Keybind that will load a state */
	public static final KeyBinding loadStateKeybind = new KeyBinding("keybind.lotas.loadstate", Keyboard.KEY_K, "keybind.category.lotas.stating");
	/** Keybind that will load the dupemod */
	public static final KeyBinding loadDupeKeybind = new KeyBinding("keybind.lotas.dupe.load", Keyboard.KEY_O, "keybind.category.lotas.duping");
	/** Keybind that will save the dupemod */
	public static final KeyBinding saveDupeKeybind = new KeyBinding("keybind.lotas.dupe.save", Keyboard.KEY_P, "keybind.category.lotas.duping");
	/** Keybind that will strafe when held */
	public static final KeyBinding holdStrafeKeybind = new KeyBinding("keybind.lotas.autostrafe", Keyboard.KEY_H, "keybind.category.lotas.moving");
	/** Keybind that will increase the Tickrate */
	public static final KeyBinding increaseTickrateKeybind = new KeyBinding("keybind.lotas.tickrate.faster", Keyboard.KEY_PERIOD, "keybind.category.lotas.tickratechanger");
	/** Keybind that will decrease the Tickrate */
	public static final KeyBinding decreaseTickrateKeybind = new KeyBinding("keybind.lotas.tickrate.slower", Keyboard.KEY_COMMA, "keybind.category.lotas.tickratechanger");
	/** Keybind that advance a single tick */
	public static final KeyBinding advanceTicksKeybind = new KeyBinding("keybind.lotas.tickrate.tickadvance", Keyboard.KEY_F9, "keybind.category.lotas.tickratechanger");
	/** Keybind that will enable or disable tick advance (tickrate 0) */
	public static final KeyBinding toggleAdvanceKeybind = new KeyBinding("keybind.lotas.tickrate.tick0", Keyboard.KEY_F8, "keybind.category.lotas.tickratechanger");
	/** Keybind that will toggle the timer */
	public static final KeyBinding toggleTimerKeybind = new KeyBinding("keybind.lotas.tickrate.timer", Keyboard.KEY_NUMPAD5, "keybind.category.lotas.tickratechanger");
	/** Keybind that will open the info hud editor */
	public static final KeyBinding openInfoHud = new KeyBinding("keybind.lotas.infogui", Keyboard.KEY_F6, "keybind.category.lotas.misc");
	/** Temporary variable used to request a savestate */
	public static boolean shouldSavestate;
	/** Temporary variable used to request a loadstate */
	public static boolean shouldLoadstate;

	private static int save;
	/**
	 * Temporary variable that will state whether the hold strafe key was pressed a
	 * tick before
	 */
	public static boolean wasPressed = false;

	private static HashMap<Integer, Long> cooldownHashMap = new HashMap<Integer, Long>();
	
	public static boolean focused;

	/**
	 * Handles a new KeyInputEvent and ticks through all keybinds.
	 * 
	 * @throws Exception Throws an whenever something is wrong
	 */
	public static void tickKeyEvent() throws Exception {
		// Savestate and Loadstate handling
		if (saveStateKeybind.isPressed()) {
			Minecraft.getMinecraft().displayGuiScreen(new GuiIngameMenu());
			shouldSavestate = true; // request a savestate next tick
		} else if (loadStateKeybind.isPressed()) {
			Minecraft.getMinecraft().displayGuiScreen(new GuiIngameMenu());
			shouldLoadstate = true; // request a loadstate next tick
		}

		// Dupemod save and load handling.
		else if (loadDupeKeybind.isPressed()) {
			DupeMod.loadChests();
			DupeMod.loadItems();
		} else if (saveDupeKeybind.isPressed()) {
			DupeMod.saveChests();
			DupeMod.saveItems();
		}

		// Autostrafe auto rotation handling
		if (wasPressed != holdStrafeKeybind.isKeyDown() && wasPressed == true) {
			KeyBinding.setKeyBindState(32, false); // let go of D when releasing the strafe keybind
		} else if (wasPressed != holdStrafeKeybind.isKeyDown() && wasPressed == false) {
			MCVer.player(Minecraft.getMinecraft()).rotationYaw -= 45; // rotate the player before starting to strafe
		}
		wasPressed = holdStrafeKeybind.isKeyDown(); // update state for next tick
		
		// Info Hud handling
		if (openInfoHud.isPressed()) {
			Minecraft.getMinecraft().displayGuiScreen(LoTASModContainer.hud);
		}

		// Timer keybinds
		if (KeybindsUtils.toggleTimerKeybind.isPressed()) { // Start/Stop the timer if there are no tas challenges running
			if (Timer.ticks < 0 || Timer.startTime == null) { // Start the timer
				Timer.startTime = Duration.ofMillis(System.currentTimeMillis());
				Timer.ticks = 0;
			}
			Timer.running = !Timer.running; // Start/stop the timers state
		}
	}

	/**
	 * Handles keybinds executed every frame
	 */
	public static void frameKeyEvent() {

		if (isKeyDown(toggleAdvanceKeybind) && TickrateChangerMod.advanceClient == false) {
			if (TickrateChangerMod.tickrate > 0) {
				save = TickrateChangerMod.index;
				TickrateChangerMod.updateTickrate(0);
				TickrateChangerMod.index = 0;
			} else {
				TickrateChangerMod.updateTickrate(TickrateChangerMod.ticks[save]);
				TickrateChangerMod.index = save;
			}
		}

		else if (TickrateChangerMod.tickrate == 0 && isKeyDown(KeybindsUtils.advanceTicksKeybind)) {
			TickrateChangerMod.advanceTick();
		}

		// Tickrate keybinds
		boolean flag = false;
		if (isKeyDownExceptTextField(KeybindsUtils.increaseTickrateKeybind)) {
			TickrateChangerMod.index++;
			flag = true;
		} else if (isKeyDownExceptTextField(KeybindsUtils.decreaseTickrateKeybind)) {
			TickrateChangerMod.index--;
			flag = true;
		}
		if (flag) {
			TickrateChangerMod.index = MCVer.clamp(TickrateChangerMod.index, 0, 11); // Update the index of the recommended Tickrates array
			TickrateChangerMod.updateTickrate(TickrateChangerMod.ticks[TickrateChangerMod.index]); // Update the Tickrate
		}
	}

	/**
	 * Checks whether the keycode is pressed, regardless of any gui screens
	 * 
	 * @param keybind
	 * @return
	 */
	public static boolean isKeyDown(KeyBinding keybind) {
		return isKeyDown(keybind.getKeyCode());
	}

	public static boolean isKeyDownExceptTextField(KeyBinding keybind) {
		GuiScreen screen=Minecraft.getMinecraft().currentScreen;
		
		if(screen instanceof GuiChat || screen instanceof GuiEditSign || (focused && screen != null)) {
			return false;
		}
		return isKeyDown(keybind);
	}
	
	public static boolean isKeyDown(int keycode) {
		boolean down = false;

		GuiScreen screen=Minecraft.getMinecraft().currentScreen;
		
		if (screen instanceof GuiControls) {
			return false;
		}

		down = keycode >= 0 ? Keyboard.isKeyDown(keycode) : Mouse.isButtonDown(keycode + 100); // Check if it's keyboard key or mouse button

		if (down) {
			if (cooldownHashMap.containsKey(keycode)) {
				if (50 * 3 <= Minecraft.getSystemTime() - cooldownHashMap.get(keycode)) {
					cooldownHashMap.put(keycode, Minecraft.getSystemTime());
					return true;
				}
				return false;
			} else {
				cooldownHashMap.put(keycode, Minecraft.getSystemTime());
				return true;
			}
		}
		return false;
	}

	/**
	 * Static method to register all Keybinds to the game. Note: Not using Forge API
	 * to avoid a crash in 1.8.9.
	 */
	public static void registerKeybinds() {
		Minecraft.getMinecraft().gameSettings.keyBindings = org.apache.commons.lang3.ArrayUtils.addAll(Minecraft.getMinecraft().gameSettings.keyBindings, saveStateKeybind, loadStateKeybind, loadDupeKeybind, saveDupeKeybind, holdStrafeKeybind, increaseTickrateKeybind, decreaseTickrateKeybind, advanceTicksKeybind, toggleAdvanceKeybind, toggleTimerKeybind, openInfoHud);
	}

}
