package de.pfannekuchen.lotas.core.utils;

import java.time.Duration;

import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.glfw.GLFW;

import de.pfannekuchen.lotas.core.utils.EventUtils.Timer;
import de.pfannekuchen.lotas.gui.HudSettings;
import de.pfannekuchen.lotas.mods.DupeMod;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.PauseScreen;

public class KeybindsUtils {

	public static KeyMapping saveStateKeybind = new KeyMapping("Savestate", GLFW.GLFW_KEY_J, "Stating");
	public static final KeyMapping loadStateKeybind = new KeyMapping("Loadstate", GLFW.GLFW_KEY_K, "Stating");
	public static final KeyMapping loadDupeKeybind = new KeyMapping("Load Items/Chests", GLFW.GLFW_KEY_O, "Duping");
	public static final KeyMapping saveDupeKeybind = new KeyMapping("Save Items/Chests", GLFW.GLFW_KEY_P, "Duping");
	public static final KeyMapping holdStrafeKeybind = new KeyMapping("Auto-Strafe", GLFW.GLFW_KEY_H, "Moving");
	public static final KeyMapping toggleFreecamKeybind = new KeyMapping("Freecam", GLFW.GLFW_KEY_I, "Moving");
	public static final KeyMapping increaseTickrateKeybind = new KeyMapping("Faster Tickrate", GLFW.GLFW_KEY_PERIOD, "Tickrate Changer");
	public static final KeyMapping decreaseTickrateKeybind = new KeyMapping("Slower Tickrate", GLFW.GLFW_KEY_COMMA, "Tickrate Changer");
	public static final KeyMapping advanceTicksKeybind = new KeyMapping("Advance Tick", GLFW.GLFW_KEY_F9, "Tickrate Changer");
	public static final KeyMapping toggleAdvanceKeybind = new KeyMapping("Tickrate Zero Toggle", GLFW.GLFW_KEY_F8, "Tickrate Changer");
	public static final KeyMapping toggleTimerKeybind = new KeyMapping("Start/Stop Timer", GLFW.GLFW_KEY_KP_5, "Tickrate Changer");
	public static final KeyMapping openInfoHud = new KeyMapping("Open InfoGui Editor", GLFW.GLFW_KEY_F6, "Misc");
	public static final KeyMapping test = new KeyMapping("Test", GLFW.GLFW_KEY_F12, "Misc");
	public static boolean shouldSavestate;
	public static boolean shouldLoadstate;
	public static boolean isFreecaming;
	public static int savedTickrate;

	public static boolean wasPressed = false;

	public static void keyEvent() {
		while (saveStateKeybind.consumeClick()) {
			shouldSavestate = true;
			Minecraft.getInstance().setScreen(new PauseScreen(true));
		}
		while (loadStateKeybind.consumeClick()) {
			Minecraft.getInstance().setScreen(new PauseScreen(true));
			shouldLoadstate = true;
		}
		while (loadDupeKeybind.consumeClick()) {
			DupeMod.load(Minecraft.getInstance());
		}
		while (saveDupeKeybind.consumeClick()) {
			DupeMod.save(Minecraft.getInstance());
		}
		while (toggleTimerKeybind.consumeClick()) {
			if (Timer.ticks < 1 || Timer.startTime == null) {
				Timer.startTime = Duration.ofMillis(System.currentTimeMillis());
				Timer.ticks = 1;
			}
			Timer.running = !Timer.running;
		}
		
		while (openInfoHud.consumeClick()) {
			Minecraft.getInstance().setScreen(new HudSettings());
		}

		if (wasPressed != holdStrafeKeybind.isDown() && wasPressed == true) {
			KeyMapping.set(Minecraft.getInstance().options.keyRight.getDefaultKey(), false);
		} else if (wasPressed != holdStrafeKeybind.isDown() && wasPressed == false) {
			Minecraft.getInstance().player.yRot -= 45;
		}
		if(test.consumeClick()) {
//			SpawnManipMod manip=new SpawnManipMod();
//			manip.debugSpawn();
		}
		wasPressed = holdStrafeKeybind.isDown();
	}

	public static void registerKeybinds() {
		ArrayUtils.addAll(Minecraft.getInstance().options.keyMappings, saveStateKeybind, loadStateKeybind, loadDupeKeybind, saveDupeKeybind, holdStrafeKeybind, toggleFreecamKeybind, increaseTickrateKeybind, decreaseTickrateKeybind, advanceTicksKeybind, toggleAdvanceKeybind, toggleTimerKeybind, openInfoHud);
	}
}