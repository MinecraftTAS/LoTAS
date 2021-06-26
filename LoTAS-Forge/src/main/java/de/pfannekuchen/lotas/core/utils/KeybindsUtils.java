package de.pfannekuchen.lotas.core.utils;

import org.lwjgl.input.Keyboard;

import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.mods.DupeMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.settings.KeyBinding;

/**
 * Contains all keybinds and handles key inputs
 * @author Pancake
 * @since v1.0
 * @version v1.1
 */
public class KeybindsUtils {

	/** Keybind that will save a state */
	public static final KeyBinding saveStateKeybind  = new KeyBinding("Savestate", Keyboard.KEY_J, "Stating");
	/** Keybind that will load a state */
	public static final KeyBinding loadStateKeybind  = new KeyBinding("Loadstate", Keyboard.KEY_K, "Stating");
	/** Keybind that will load the dupemod */
	public static final KeyBinding loadDupeKeybind  = new KeyBinding("Load Items/Chests", Keyboard.KEY_O, "Duping");
	/** Keybind that will save the dupemod */
	public static final KeyBinding saveDupeKeybind  = new KeyBinding("Save Items/Chests", Keyboard.KEY_P, "Duping");
	/** Keybind that will strafe when held */
	public static final KeyBinding holdStrafeKeybind = new KeyBinding("Auto-Strafe", Keyboard.KEY_H, "Moving");
	/** Keybind that will enable or disable freecam */
	public static final KeyBinding toggleFreecamKeybind = new KeyBinding("Freecam", Keyboard.KEY_I, "Moving");
	/** Keybind that will increase the Tickrate */
	public static final KeyBinding increaseTickrateKeybind = new KeyBinding("Faster Tickrate", Keyboard.KEY_PERIOD, "Tickrate Changer");
	/** Keybind that will decrease the Tickrate */
	public static final KeyBinding decreaseTickrateKeybind = new KeyBinding("Slower Tickrate", Keyboard.KEY_COMMA, "Tickrate Changer");
	/** Keybind that advance a single tick */
	public static final KeyBinding advanceTicksKeybind = new KeyBinding("Advance Tick", Keyboard.KEY_F9, "Tickrate Changer");
	/** Keybind that will enable or disable tick advance (tickrate 0) */
	public static final KeyBinding toggleAdvanceKeybind = new KeyBinding("Tickrate Zero Toggle", Keyboard.KEY_F8, "Tickrate Changer");
	/** Keybind that will toggle the timer */
	public static final KeyBinding toggleTimerKeybind = new KeyBinding("Start/Stop Timer", Keyboard.KEY_NUMPAD5, "Tickrate Changer");
	/** Keybind that will open the info hud editor */
	public static final KeyBinding openInfoHud = new KeyBinding("Open InfoGui Editor", Keyboard.KEY_F6, "Misc");
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
	 * @throws Exception Throws an whenever something is wrong
	 */
	public static void keyEvent() throws Exception {
		// Savestate and Loadstate handling
		if (saveStateKeybind.isPressed() && de.pfannekuchen.lotas.taschallenges.ChallengeMap.currentMap == null) {
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
			Minecraft.getMinecraft().displayGuiScreen(new de.pfannekuchen.lotas.gui.HudSettings());
		}
	}
	
	/**
	 * Static method to register all Keybinds to the game.
	 * Note: Not using Forge API to avoid a crash in 1.8.9.
	 */
	public static void registerKeybinds() {
		Minecraft.getMinecraft().gameSettings.keyBindings = org.apache.commons.lang3.ArrayUtils.addAll(Minecraft.getMinecraft().gameSettings.keyBindings, saveStateKeybind, loadStateKeybind, loadDupeKeybind, saveDupeKeybind, holdStrafeKeybind, toggleFreecamKeybind, increaseTickrateKeybind, decreaseTickrateKeybind, advanceTicksKeybind, toggleAdvanceKeybind, toggleTimerKeybind, openInfoHud);
	}
	
}
