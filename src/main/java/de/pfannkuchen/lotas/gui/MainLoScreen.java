package de.pfannkuchen.lotas.gui;

import de.pfannkuchen.lotas.gui.api.LoScreen;
import de.pfannkuchen.lotas.gui.widgets.MainLoWidget;

/*
 * WARNING: EXAMPLE CODE
 */

/**
 * This is the Main LoScreen that opens on the LoTAS Edit button. Currently it is only an example though
 * @author Pancake
 */
public class MainLoScreen extends LoScreen {
	
	@Override
	protected void init() {
		addWidget(new MainLoWidget(0.25, 0.005, 0.075, 0xFF1c1a1e, 0xFF025a5f, 0xFF038386, 0xFFFFFFFF, "LoTAS Menu"));
	}
	
}
