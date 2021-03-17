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
	
}
