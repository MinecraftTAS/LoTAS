package de.pfannkuchen.lotas.gui;

import de.pfannkuchen.lotas.gui.widgets.MainLoWidget;
import de.pfannkuchen.lotas.loscreen.LoScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/**
 * This is the Main LoScreen that opens on the LoTAS Edit button.
 * @author Pancake
 */
@Environment(EnvType.CLIENT)
public class MainLoScreen extends LoScreen {
	
	@Override
	protected void init() {
		addWidget(new MainLoWidget(0.25, 0.004, 0.075, 0xFF1c1a1e, 0xFF025a5f, 0xFF038386, 0xFFFFFFFF, "LoTAS Menu"));
	}
	
}
