package de.pfannkuchen.lotas.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

/**
 * An empty gui screen.
 * Note: This is NOT a LoScreen
 * @author Pancake
 */
@Environment(EnvType.CLIENT)
public class EmptyScreen extends Screen {

	public EmptyScreen() {
		super(Component.literal("Empty Screen"));
	}

	/**
	 * This will cause the server to be controlled by tickrate zero, instead of the screen window. Breaks server support a bit less
	 */
	@Override
	public boolean isPauseScreen() {
		return false;
	}

}
