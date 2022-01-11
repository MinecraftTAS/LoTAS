package de.pfannkuchen.lotas.gui;

import de.pfannkuchen.lotas.LoTAS;
import de.pfannkuchen.lotas.gui.widgets.MainLoWidget;
import de.pfannkuchen.lotas.gui.windows.DupeModLoWidget;
import de.pfannkuchen.lotas.gui.windows.SavestatesLoWidget;
import de.pfannkuchen.lotas.gui.windows.TickrateChangerLoWidget;
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
		TickrateChangerLoWidget tickratechangerwidget = new TickrateChangerLoWidget(c -> LoTAS.tickratechanger.requestTickrateUpdate(c));
		this.addWidget(tickratechangerwidget);
		TickrateChangerLoWidget.updateTickrate(LoTAS.tickratechanger.getTickrate());

		// Add the Dupe Mod Widget
		DupeModLoWidget dupemodwidget = new DupeModLoWidget(() -> LoTAS.dupemod.requestDupe(true), () -> LoTAS.dupemod.requestDupe(false));
		this.addWidget(dupemodwidget);

		// Add the Savestate Widget
		SavestatesLoWidget savestateswidget = new SavestatesLoWidget();
		this.addWidget(savestateswidget);

		this.addWidget(new MainLoWidget((a, b) -> {
			boolean enable; // Whether the widget should be enabled or disabled
			String widgetname;
			if (a.getString().startsWith("\u00A7\u00A7\u00A7l")) {
				a = new TextComponent(a.getString().replace("\u00A7\u00A7\u00A7l", ""));
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
				TickrateChangerLoWidget.updateTickrate(LoTAS.tickratechanger.getTickrate());
				break;
			case "Duping":
				dupemodwidget.changeVisibility(enable);
				break;
			case "Savestating":
				savestateswidget.changeVisibility(enable);
				break;
			default:
				throw new AssertionError();
			}
			// Enable/disable widget
			return a;
		}));
		super.init();
	}

}
