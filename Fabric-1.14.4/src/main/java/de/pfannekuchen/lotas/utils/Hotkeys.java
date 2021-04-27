package de.pfannekuchen.lotas.utils;

import java.io.IOException;
import java.time.Duration;

import org.lwjgl.glfw.GLFW;

import de.pfannekuchen.lotas.dupemod.DupeMod;
import de.pfannekuchen.lotas.tickratechanger.TickrateChanger;
import de.pfannekuchen.lotas.tickratechanger.Timer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.options.KeyBinding;
import rlog.RLogAPI;

public class Hotkeys {

	private static final KeyBinding saveState  = new KeyBinding("Savestate", GLFW.GLFW_KEY_J, "Savestates");
	private static final KeyBinding loadState  = new KeyBinding("Loadstate", GLFW.GLFW_KEY_K, "Savestates");
	private static final KeyBinding loadDupe  = new KeyBinding("Load Items/Chests", GLFW.GLFW_KEY_O, "Duping");
	private static final KeyBinding saveDupe  = new KeyBinding("Save Items/Chests", GLFW.GLFW_KEY_P, "Duping");
	private static final KeyBinding strafe  = new KeyBinding("Strafe +45", GLFW.GLFW_KEY_H, "Movement");
	private static final KeyBinding unstrafe = new KeyBinding("Strafe -45", GLFW.GLFW_KEY_G, "Movement");
	public static final KeyBinding freecam = new KeyBinding("Freecam", GLFW.GLFW_KEY_I, "Movement");
	private static final KeyBinding slower = new KeyBinding("Faster Tickrate", GLFW.GLFW_KEY_PERIOD, "Tickrate Changer");
	private static final KeyBinding faster = new KeyBinding("Slower Tickrate", GLFW.GLFW_KEY_COMMA, "Tickrate Changer");
	public static final KeyBinding advance = new KeyBinding("Advance Tick", GLFW.GLFW_KEY_F9, "Tickrate Changer");
	public static final KeyBinding zero = new KeyBinding("Tickrate Zero Toggle", GLFW.GLFW_KEY_F8, "Tickrate Changer");
	private static final KeyBinding timer = new KeyBinding("Start/Stop Timer", GLFW.GLFW_KEY_Z, "Tickrate Changer");
	public static boolean shouldSavestate = false;
	public static boolean shouldLoadstate = false;
	public static boolean isFreecaming  = false;
	public static int savedTickrate;
	
	public static void keyEvent() throws IOException {
		if (saveState.wasPressed()) {
			RLogAPI.logDebug("[Hotkeys] Requesting Savestate");
			MinecraftClient.getInstance().openScreen(new GameMenuScreen(true));
			shouldSavestate = true;
		} else if (loadState.wasPressed()) {
			MinecraftClient.getInstance().openScreen(new GameMenuScreen(true));
			shouldLoadstate = true;
			RLogAPI.logDebug("[Hotkeys] Requesting Loadstate");
		} else if (loadDupe.wasPressed()) {
			DupeMod.load(MinecraftClient.getInstance());
			RLogAPI.logDebug("[Hotkeys] Loading Items and Chests");
		} else if (saveDupe.wasPressed()) {
			DupeMod.save(MinecraftClient.getInstance());
			RLogAPI.logDebug("[Hotkeys] Saving Items and Chests");
		} else if (strafe.wasPressed()) {
			MinecraftClient.getInstance().player.yaw += 45;
		} else if (unstrafe.wasPressed()) {
			MinecraftClient.getInstance().player.yaw -= 45;
		} else if (slower.wasPressed()) {
			TickrateChanger.index++;
			if (TickrateChanger.index >= 10) TickrateChanger.index = 10;
			TickrateChanger.updateTickrate(TickrateChanger.ticks[TickrateChanger.index]);
		} else if (faster.wasPressed()) {
			TickrateChanger.index--;
			if (TickrateChanger.index <= 0) TickrateChanger.index = 0;
			TickrateChanger.updateTickrate(TickrateChanger.ticks[TickrateChanger.index]);
		} else if (timer.wasPressed()) {
			if (Timer.ticks < 1 || Timer.startTime == null) {
				Timer.startTime = Duration.ofMillis(System.currentTimeMillis());
				Timer.ticks = 1;
			}
			Timer.running = !Timer.running;
		}
	}
	
	public static void registerKeybindings() {
		RLogAPI.logDebug("[Hotkeys] Registering Keybinds");
		KeyBindingHelper.registerKeyBinding(saveState);
		KeyBindingHelper.registerKeyBinding(loadState);
		KeyBindingHelper.registerKeyBinding(loadDupe);
		KeyBindingHelper.registerKeyBinding(saveDupe);
		KeyBindingHelper.registerKeyBinding(strafe);
		KeyBindingHelper.registerKeyBinding(unstrafe);
		KeyBindingHelper.registerKeyBinding(faster);
		KeyBindingHelper.registerKeyBinding(slower);
		KeyBindingHelper.registerKeyBinding(advance);
		KeyBindingHelper.registerKeyBinding(zero);
		KeyBindingHelper.registerKeyBinding(timer);
		KeyBindingHelper.registerKeyBinding(freecam);
	}
	
}
