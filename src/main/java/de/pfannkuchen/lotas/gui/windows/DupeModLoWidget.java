package de.pfannkuchen.lotas.gui.windows;

import de.pfannkuchen.lotas.gui.widgets.ButtonLoWidget;
import de.pfannkuchen.lotas.gui.widgets.WindowLoWidget;
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
	private Runnable onSave;
	private Runnable onLoad;

	/**
	 * Initializes a Tickrate Changer Widget
	 */
	public DupeModLoWidget(Runnable onSave, Runnable onLoad) {
		super("dupemodwidget", new TextComponent("Dupe Mod"), .15, .135);
		this.onSave = onSave;
		this.onLoad = onLoad;
	}

	@Override
	protected void init() {
		this.addWidget(new ButtonLoWidget(true, 0.005, 0.035, .14, this.onSave, new TextComponent("Save Playerdata")));
		this.addWidget(new ButtonLoWidget(true, 0.005, 0.085, .14, this.onLoad, new TextComponent("Load Playerdata")));
		super.init();
	}

}
