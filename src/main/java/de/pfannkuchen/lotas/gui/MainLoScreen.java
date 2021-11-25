package de.pfannkuchen.lotas.gui;

import de.pfannkuchen.lotas.LoTAS;
import de.pfannkuchen.lotas.gui.widgets.MainLoWidget;
import de.pfannkuchen.lotas.gui.widgets.TickrateChangerLoWidget;
import de.pfannkuchen.lotas.loscreen.LoScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.TextComponent;

/**
 * This is the Main LoScreen that opens on the LoTAS Edit button.
 * @author Pancake
 */
@Environment(EnvType.CLIENT)
public class MainLoScreen extends LoScreen {
	
	@Override
	protected void init() {
		// Add the Tickrate Changer Widget
		TickrateChangerLoWidget tickratechangerwidget = new TickrateChangerLoWidget(c -> {
			LoTAS.tickratechanger.requestTickrateUpdate(c);
		});
		addWidget(tickratechangerwidget);
		tickratechangerwidget.updateTickrate(LoTAS.tickratechanger.getTickrate());
		
		addWidget(new MainLoWidget((a, b) -> {
			boolean enable; // Whether the widget should be enabled or disabled
			String widgetname;
			if (a.getString().startsWith("\u00A7\u00A7\u00A7l")) {
				a = new TextComponent(a.getString().replaceAll("\u00A7\u00A7\u00A7l", ""));
				enable = false;
				widgetname = a.getString();
			} else {
				widgetname = a.getString();
				a = new TextComponent("\u00A7\u00A7\u00A7l" + a.getString());
				enable = true;
			}
			// Find the widget
			switch (widgetname) {
				case "Tickrate Changing":
					tickratechangerwidget.changeVisibility(enable);
					break;
				default:
					throw new AssertionError();
			}
			// Enable/disable widget
			return a;
		}));
	}
	
}
