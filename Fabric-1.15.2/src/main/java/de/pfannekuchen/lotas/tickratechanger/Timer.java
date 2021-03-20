package de.pfannekuchen.lotas.tickratechanger;

import java.awt.Color;
import java.time.Duration;
import java.util.List;

import com.google.common.collect.ImmutableList;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;

public class Timer {

    private static Duration rta = Duration.ZERO;

    public static Duration startTime;
    public static int ticks = -1;
    public static boolean running;

    public static final List<String> allowed = ImmutableList.of("containerscreen", "beaconscreen", "brewingstandscreen", "chatscreen", "commandblockscreen", "enchantingscreen", "furnacescreen", "hopperscreen", "inventoryscreen", "merchantscreen", "creativecontainer", "shulkerboxscreen", "anvilscreen", "craftingcontainer");

    /**
     * Converting the duration to a string that will be displayed in the gui
     * @param d
     * @return Timer as a string
     */
    public static String getDuration(Duration d) {
        return String.format("%02d", d.toMinutes()) + ":" + String.format("%02d", d.getSeconds() % 60) + ":" + (int) ((d.toMillis() % 1000) / 100) + "0";
    }

    /**
     * Drawing timer and RTA timer
     */
    public static void onDraw() {
        if (Timer.ticks != -1) {
            Screen.fill(0, 0, 75, 24, new Color(0, 0, 0, 175).getRGB());
            Duration dur = Duration.ofMillis(Timer.ticks * 50);
            if (Timer.running) rta = Duration.ofMillis(System.currentTimeMillis() - Timer.startTime.toMillis());
            MinecraftClient.getInstance().textRenderer.drawWithShadow(getDuration(dur), 0, 02, 0xFFFFFFFF);
            MinecraftClient.getInstance().textRenderer.drawWithShadow("RTA: " + getDuration(rta), 0, 14, 0xFFFFFFFF);
        }
    }


}
