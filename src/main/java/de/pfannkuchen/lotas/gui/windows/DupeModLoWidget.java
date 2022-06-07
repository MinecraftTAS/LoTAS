package de.pfannkuchen.lotas.gui.windows;

import de.pfannkuchen.lotas.gui.widgets.ButtonLoWidget;
import de.pfannkuchen.lotas.gui.widgets.WindowLoWidget;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.Component;

/**
 * Dupe mod window lowidget
 * @author Pancake
 */
@Environment(EnvType.CLIENT)
public class DupeModLoWidget extends WindowLoWidget {

	// Update Event
	private Runnable onSave;
	private Runnable onLoad;

	/**
	 * Initializes a dupe mod widget
	 */
	public DupeModLoWidget(Runnable onSave, Runnable onLoad) {
		super("dupemodwidget", Component.literal("Dupe Mod"), .15, .135);
		this.onSave = onSave;
		this.onLoad = onLoad;
	}

	@Override
	protected void init() {
		this.addWidget(new ButtonLoWidget(true, 0.005, 0.035, .14, this.onSave, Component.literal("Save Playerdata")));
		this.addWidget(new ButtonLoWidget(true, 0.005, 0.085, .14, this.onLoad, Component.literal("Load Playerdata")));
		super.init();
	}

}
