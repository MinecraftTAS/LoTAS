package de.pfannekuchen.lotas.core.utils;

import java.io.IOException;

import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.input.Keyboard;

import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.mods.DupeMod;
import de.pfannekuchen.lotas.taschallenges.ChallengeMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.settings.KeyBinding;

public class KeybindsUtils {

	public static final KeyBinding saveStateKeybind  = new KeyBinding("Savestate", Keyboard.KEY_J, "Stating");
	public static final KeyBinding loadStateKeybind  = new KeyBinding("Loadstate", Keyboard.KEY_K, "Stating");
	public static final KeyBinding loadDupeKeybind  = new KeyBinding("Load Items/Chests", Keyboard.KEY_O, "Duping");
	public static final KeyBinding saveDupeKeybind  = new KeyBinding("Save Items/Chests", Keyboard.KEY_P, "Duping");
	public static final KeyBinding strafeRightKeybind = new KeyBinding("Strafe +45", Keyboard.KEY_H, "Moving");
	public static final KeyBinding strafeLeftKeybind = new KeyBinding("Strafe -45", Keyboard.KEY_G, "Moving");
	public static final KeyBinding toggleFreecamKeybind = new KeyBinding("Freecam", Keyboard.KEY_I, "Moving");
	public static final KeyBinding increaseTickrateKeybind = new KeyBinding("Faster Tickrate", Keyboard.KEY_PERIOD, "Tickrate Changer");
	public static final KeyBinding decreaseTickrateKeybind = new KeyBinding("Slower Tickrate", Keyboard.KEY_COMMA, "Tickrate Changer");
	public static final KeyBinding advanceTicksKeybind = new KeyBinding("Advance Tick", Keyboard.KEY_F9, "Tickrate Changer");
	public static final KeyBinding toggleAdvanceKeybind = new KeyBinding("Tickrate Zero Toggle", Keyboard.KEY_F8, "Tickrate Changer");
	public static final KeyBinding toggleTimerKeybind = new KeyBinding("Start/Stop Timer", Keyboard.KEY_NUMPAD5, "Tickrate Changer");
	public static boolean shouldSavestate;
	public static boolean shouldLoadstate;
	public static boolean isFreecaming;
	public static int savedTickrate;
	
	public static void keyEvent() throws IOException {
		if (saveStateKeybind.isPressed() && ChallengeMap.currentMap == null) {
			Minecraft.getMinecraft().displayGuiScreen(new GuiIngameMenu());
			shouldSavestate = true;
		} else if (loadStateKeybind.isPressed()) {
			Minecraft.getMinecraft().displayGuiScreen(new GuiIngameMenu());
			shouldLoadstate = true;
		} else if (loadDupeKeybind.isPressed()) {
			DupeMod.loadChests();
			DupeMod.loadItems();
		} else if (saveDupeKeybind.isPressed()) {
			DupeMod.saveChests();
			DupeMod.saveItems();
		} else if (strafeRightKeybind.isPressed()) {
			MCVer.player(Minecraft.getMinecraft()).rotationYaw += 45;
		} else if (strafeLeftKeybind.isPressed()) {
			MCVer.player(Minecraft.getMinecraft()).rotationYaw -= 45;
		}
		
	}
	
	public static void registerKeybinds() {
		Minecraft.getMinecraft().gameSettings.keyBindings = ArrayUtils.addAll(Minecraft.getMinecraft().gameSettings.keyBindings, saveStateKeybind, loadStateKeybind, loadDupeKeybind, saveDupeKeybind, strafeRightKeybind, strafeLeftKeybind, toggleFreecamKeybind, increaseTickrateKeybind, decreaseTickrateKeybind, advanceTicksKeybind, toggleAdvanceKeybind, toggleTimerKeybind);
	}
	
}
