package de.pfannekuchen.lotas.hotkeys;

import java.time.Duration;

import org.lwjgl.glfw.GLFW;

import de.pfannekuchen.lotas.dupemod.DupeMod;
import de.pfannekuchen.lotas.tickratechanger.Timer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;

public class Hotkeys {

    public static final KeyBinding loadDupe  = new KeyBinding("Load Inventory", GLFW.GLFW_KEY_O, "Duping");
    public static final KeyBinding saveDupe  = new KeyBinding("Save Inventory", GLFW.GLFW_KEY_P, "Duping");

    public static final KeyBinding slower = new KeyBinding("Faster Tickrate", GLFW.GLFW_KEY_PERIOD, "Tickrate Changer");
    public static final KeyBinding faster = new KeyBinding("Slower Tickrate", GLFW.GLFW_KEY_COMMA, "Tickrate Changer");
    public static final KeyBinding advance = new KeyBinding("Advance Tick", GLFW.GLFW_KEY_F9, "Tickrate Changer");

    public static final KeyBinding zero = new KeyBinding("Tickrate Zero Toggle", GLFW.GLFW_KEY_F8, "Tickrate Changer");

    public static final KeyBinding timer = new KeyBinding("Start/Stop Timer", InputUtil.fromName("key.keyboard.keypad.5").getKeyCode(), "Tickrate Changer");
    public static final KeyBinding freecam = new KeyBinding("Freecam", GLFW.GLFW_KEY_I, "Movement");

    public static boolean isFreecaming;
    public static int savedTickrate;

    public static void keyEvent() {
        while (saveDupe.wasPressed()) {
            DupeMod.save(MinecraftClient.getInstance());
        }
        while (loadDupe.wasPressed()) {
            DupeMod.load(MinecraftClient.getInstance());
        }
        while (timer.wasPressed()) {
            if (Timer.ticks < 1 || Timer.startTime == null) {
                Timer.startTime = Duration.ofMillis(System.currentTimeMillis());
                Timer.ticks = 1;
            }
            Timer.running = !Timer.running;
        }

    }

    public static void initKeybindings() {
        KeyBindingHelper.registerKeyBinding(advance);
        KeyBindingHelper.registerKeyBinding(faster);
        KeyBindingHelper.registerKeyBinding(slower);
        KeyBindingHelper.registerKeyBinding(zero);
        KeyBindingHelper.registerKeyBinding(loadDupe);
        KeyBindingHelper.registerKeyBinding(saveDupe);
        KeyBindingHelper.registerKeyBinding(timer);
        KeyBindingHelper.registerKeyBinding(freecam);
    }

}
