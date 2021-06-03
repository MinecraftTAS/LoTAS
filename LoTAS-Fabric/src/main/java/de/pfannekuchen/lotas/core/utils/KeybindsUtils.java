package de.pfannekuchen.lotas.core.utils;

import java.io.IOException;

import org.lwjgl.glfw.GLFW;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.options.KeyBinding;

public class KeybindsUtils {

	public static KeyBinding saveStateKeybind = new KeyBinding("Savestate", GLFW.GLFW_KEY_J, "Stating");
	public static final KeyBinding loadStateKeybind = new KeyBinding("Loadstate", GLFW.GLFW_KEY_K, "Stating");
	public static final KeyBinding loadDupeKeybind = new KeyBinding("Load Items/Chests", GLFW.GLFW_KEY_O, "Duping");
	public static final KeyBinding saveDupeKeybind = new KeyBinding("Save Items/Chests", GLFW.GLFW_KEY_P, "Duping");
	public static final KeyBinding holdStrafeKeybind = new KeyBinding("Auto-Strafe", GLFW.GLFW_KEY_H, "Moving");
	public static final KeyBinding toggleFreecamKeybind = new KeyBinding("Freecam", GLFW.GLFW_KEY_I, "Moving");
	public static final KeyBinding increaseTickrateKeybind = new KeyBinding("Faster Tickrate", GLFW.GLFW_KEY_PERIOD, "Tickrate Changer");
	public static final KeyBinding decreaseTickrateKeybind = new KeyBinding("Slower Tickrate", GLFW.GLFW_KEY_COMMA, "Tickrate Changer");
	public static final KeyBinding advanceTicksKeybind = new KeyBinding("Advance Tick", GLFW.GLFW_KEY_F9, "Tickrate Changer");
	public static final KeyBinding toggleAdvanceKeybind = new KeyBinding("Tickrate Zero Toggle", GLFW.GLFW_KEY_F8, "Tickrate Changer");
	public static final KeyBinding toggleTimerKeybind = new KeyBinding("Start/Stop Timer", GLFW.GLFW_KEY_KP_5, "Tickrate Changer");
	public static boolean shouldSavestate;
	public static boolean shouldLoadstate;
	public static boolean isFreecaming;
	public static int savedTickrate;

	public static void keyEvent() {
		while (saveStateKeybind.wasPressed() /*&& ChallengeMap.currentMap == null*/) {
			MinecraftClient.getInstance().openScreen(new GameMenuScreen(true));
			shouldSavestate = true;
		} 
		while(loadStateKeybind.wasPressed()) {
			MinecraftClient.getInstance().openScreen(new GameMenuScreen(true));
			shouldLoadstate = true;
		}
//		} else if (loadDupeKeybind.isPressed()) {
//			DupeMod.loadChests();
//			DupeMod.loadItems();
//		} else if (saveDupeKeybind.isPressed()) {
//			DupeMod.saveChests();
//			DupeMod.saveItems();
//		}
	}

	public static void registerKeybinds() {
		KeyBindingHelper.registerKeyBinding(saveStateKeybind);
		KeyBindingHelper.registerKeyBinding(loadStateKeybind);
//		KeyBindingHelper.registerKeyBinding(loadDupeKeybind);
//		KeyBindingHelper.registerKeyBinding(saveDupeKeybind);
//		KeyBindingHelper.registerKeyBinding(holdStrafeKeybind);
//		KeyBindingHelper.registerKeyBinding(toggleFreecamKeybind);
		KeyBindingHelper.registerKeyBinding(increaseTickrateKeybind);
		KeyBindingHelper.registerKeyBinding(decreaseTickrateKeybind);
		KeyBindingHelper.registerKeyBinding(advanceTicksKeybind);
		KeyBindingHelper.registerKeyBinding(toggleAdvanceKeybind);
//		KeyBindingHelper.registerKeyBinding(toggleTimerKeybind);
	}
}