package de.pfannekuchen.lotas.hotkeys;

import java.io.IOException;

import org.lwjgl.input.Keyboard;

import de.pfannekuchen.lotas.challenges.ChallengeLoader;
import de.pfannekuchen.lotas.dupemod.DupeMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import rlog.RLogAPI;

public class Hotkeys {

	public static final KeyBinding saveState  = new KeyBinding("Savestate", Keyboard.KEY_J, "Savestates");
	public static final KeyBinding loadState  = new KeyBinding("Loadstate", Keyboard.KEY_K, "Savestates");
	public static final KeyBinding loadDupe  = new KeyBinding("Load Items/Chests", Keyboard.KEY_O, "Duping");
	public static final KeyBinding saveDupe  = new KeyBinding("Save Items/Chests", Keyboard.KEY_P, "Duping");
	public static final KeyBinding strafe  = new KeyBinding("Strafe +45", Keyboard.KEY_H, "Movement");
	public static final KeyBinding unstrafe = new KeyBinding("Strafe -45", Keyboard.KEY_G, "Movement");
	public static final KeyBinding freecam = new KeyBinding("Freecam", Keyboard.KEY_I, "Movement");
	public static final KeyBinding slower = new KeyBinding("Faster Tickrate", Keyboard.KEY_PERIOD, "Tickrate Changer");
	public static final KeyBinding faster = new KeyBinding("Slower Tickrate", Keyboard.KEY_COMMA, "Tickrate Changer");
	public static final KeyBinding advance = new KeyBinding("Advance Tick", Keyboard.KEY_F9, "Tickrate Changer");
	public static final KeyBinding zero = new KeyBinding("Tickrate Zero Toggle", Keyboard.KEY_F8, "Tickrate Changer");
	public static final KeyBinding timer = new KeyBinding("Start/Stop Timer", Keyboard.KEY_NUMPAD5, "Tickrate Changer");
	public static boolean shouldSavestate = false;
	public static boolean shouldLoadstate = false;
	public static boolean isFreecaming  = false;
	public static int savedTickrate;
	
	public static void keyEvent() throws IOException {
		if (saveState.isPressed() && ChallengeLoader.map == null) {
			Minecraft.getMinecraft().displayGuiScreen(new GuiIngameMenu());
			RLogAPI.logDebug("[Hotkeys] Requesting Savestate");
			shouldSavestate = true;
		} else if (loadState.isPressed() && ChallengeLoader.map == null) {
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
		ClientRegistry.registerKeyBinding(saveState);
		ClientRegistry.registerKeyBinding(loadState);
		ClientRegistry.registerKeyBinding(loadDupe);
		ClientRegistry.registerKeyBinding(saveDupe);
		ClientRegistry.registerKeyBinding(strafe);
		ClientRegistry.registerKeyBinding(unstrafe);
		ClientRegistry.registerKeyBinding(faster);
		ClientRegistry.registerKeyBinding(slower);
		ClientRegistry.registerKeyBinding(advance);
		ClientRegistry.registerKeyBinding(zero);
		ClientRegistry.registerKeyBinding(timer);
		ClientRegistry.registerKeyBinding(freecam);
	}
	
}
