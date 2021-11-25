package de.pfannkuchen.lotas.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;

/**
 * An empty gui screen.
 * Note: This is NOT a LoScreen
 * @author Pancake
 */
@Environment(EnvType.CLIENT)
public class EmptyScreen extends Screen {

	public EmptyScreen() {
		super(new TextComponent("Empty Screen"));
	}
	
}
