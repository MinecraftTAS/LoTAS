package de.pfannekuchen.lotas.tickratechanger;

import java.time.Duration;
import java.util.List;

import com.google.common.collect.ImmutableList;

/**
 * Keeps track of the timer diplayed in the gui
 * @author Pancake
 *
 */
public class Timer {

	public static Duration startTime;
	public static int ticks = -1;
	public static boolean running;
	
	public static final List<String> allowed = ImmutableList.of("guichest", "guibeacon", "guibrewingstand", "guichat", "guinewchat", "guicommandblock", "guidispenser",
			"guienchantment", "guifurnace", "guihopper", "guiinventory", "guirecipebook", "guigameover", "guirecipeoverlay", "guimerchant", "guicontainercreative", "guishulkerbox", "guirepair", "guicrafting");
    
    public static String getDuration(Duration d) {
        return String.format("%02d", d.toMinutes()) + ":" + String.format("%02d", d.getSeconds() % 60) + ":" + String.format("%02d", (int) ((d.toMillis() % 1000)));
    }
	
	public static String getCurrentTimerFormatted() {
		Duration d = Duration.ofMillis(ticks * 50);
		return String.format("%02d", d.toMinutes()) + ":" + String.format("%02d", d.getSeconds() % 60) + "." + String.format("%02d", (int) ((d.toMillis() % 1000)));
	}
	
}