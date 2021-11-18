package de.pfannkuchen.lotas.gui;

import de.pfannkuchen.lotas.gui.widgets.MainLoWidget;
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
		addWidget(new MainLoWidget((a, b) -> {
			if (a.getString().startsWith("\u00A7\u00A7\u00A7l")) a = new TextComponent(a.getString().replaceAll("\u00A7\u00A7\u00A7l", ""));
			else a = new TextComponent("\u00A7\u00A7\u00A7l" + a.getString());
			return a;
		}));
	}
	
}
