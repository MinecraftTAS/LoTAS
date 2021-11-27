package de.pfannkuchen.lotas.gui.widgets;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.TextComponent;

/**
 * Dupe Mod Widget
 * @author Pancake
 */
@Environment(EnvType.CLIENT)
public class DupeModLoWidget extends WindowLoWidget {
	
	// Update Event
	private static Runnable onSave;
	private static Runnable onLoad;
	
	/**
	 * Initializes a Tickrate Changer Widget
	 */
	public DupeModLoWidget(Runnable onSave, Runnable onLoad) {
		super("dupemodwidget", new TextComponent("Dupe Mod"), .15, .135);
		DupeModLoWidget.onSave = onSave;
		DupeModLoWidget.onLoad = onLoad;
	}

	@Override
	protected void init() {
		addWidget(new ButtonLoWidget(true, 0.005, 0.035, .14, onSave, new TextComponent("Save Playerdata")));
		addWidget(new ButtonLoWidget(true, 0.005, 0.085, .14, onLoad, new TextComponent("Load Playerdata")));
		super.init();
	}
	
}
