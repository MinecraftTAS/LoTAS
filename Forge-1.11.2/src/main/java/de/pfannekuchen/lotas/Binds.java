package de.pfannekuchen.lotas;

import java.io.IOException;

import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.input.Keyboard;

import de.pfannekuchen.lotas.challenges.ChallengeLoader;
import de.pfannekuchen.lotas.dupemod.DupeMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.settings.KeyBinding;
import rlog.RLogAPI;

public class Binds {

	public static KeyBinding saveState  = new KeyBinding("Savestate", Keyboard.KEY_J, "States");
	public static KeyBinding loadState  = new KeyBinding("Loadstate", Keyboard.KEY_K, "States");
	public static KeyBinding loadDupe  = new KeyBinding("Load Items/Chests", Keyboard.KEY_O, "Duping");
	public static KeyBinding saveDupe  = new KeyBinding("Save Items/Chests", Keyboard.KEY_P, "Duping");
	public static KeyBinding strafe  = new KeyBinding("Strafe +45", Keyboard.KEY_H, "Moving");
	public static KeyBinding unstrafe = new KeyBinding("Strafe -45", Keyboard.KEY_G, "Moving");
	public static KeyBinding freecam = new KeyBinding("Freecam", Keyboard.KEY_I, "Moving");
	public static KeyBinding slower = new KeyBinding("Faster Tickrate", Keyboard.KEY_PERIOD, "Tickrate Changer");
	public static KeyBinding faster = new KeyBinding("Slower Tickrate", Keyboard.KEY_COMMA, "Tickrate Changer");
	public static KeyBinding advance = new KeyBinding("Advance Tick", Keyboard.KEY_F9, "Tickrate Changer");
	public static KeyBinding zero = new KeyBinding("Tickrate Zero Toggle", Keyboard.KEY_F8, "Tickrate Changer");
	public static KeyBinding timer = new KeyBinding("Start/Stop Timer", Keyboard.KEY_NUMPAD5, "Tickrate Changer");
	public static boolean shouldSavestate = false;
	public static boolean shouldLoadstate = false;
	public static boolean isFreecaming  = false;
	public static int savedTickrate;
	
	public static void keyEvent() throws IOException {
		
		if (saveState.isPressed() && ChallengeLoader.map == null) {
			RLogAPI.logDebug("[Hotkeys] Requesting Savestate");
			Minecraft.getMinecraft().displayGuiScreen(new GuiIngameMenu());
			shouldSavestate = true;
		} else if (loadState.isPressed()) {
			Minecraft.getMinecraft().displayGuiScreen(new GuiIngameMenu());
			shouldLoadstate = true;
			RLogAPI.logDebug("[Hotkeys] Requesting Loadstate");
		} else if (loadDupe.isPressed()) {
			DupeMod.loadChests();
			DupeMod.loadItems();
			RLogAPI.logDebug("[Hotkeys] Loading Items and Chests");
		} else if (saveDupe.isPressed()) {
			DupeMod.saveChests();
			DupeMod.saveItems();
			RLogAPI.logDebug("[Hotkeys] Saving Items and Chests");
		} else if (strafe.isPressed()) {
			Minecraft.getMinecraft().player.rotationYaw += 45;
		} else if (unstrafe.isPressed()) {
			Minecraft.getMinecraft().player.rotationYaw -= 45;
		}
		
	}
	
	public static void registerKeybindings() {
		RLogAPI.logDebug("[Hotkeys] Registering Keybinds");
		Minecraft.getMinecraft().gameSettings.keyBindings = ArrayUtils.addAll(Minecraft.getMinecraft().gameSettings.keyBindings, saveState, loadState, loadDupe, saveDupe, freecam, strafe, unstrafe, faster, slower, advance, zero, timer);
	}
	
}
