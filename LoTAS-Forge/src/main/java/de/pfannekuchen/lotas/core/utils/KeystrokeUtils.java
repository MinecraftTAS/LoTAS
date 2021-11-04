package de.pfannekuchen.lotas.core.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;

public class KeystrokeUtils {
	
	public static String getKeystrokes() {
		String out1 = "";
        GameSettings gamesettings=Minecraft.getMinecraft().gameSettings;
        /* Obtain every pressed key and add it to a string */
		for (net.minecraft.client.settings.KeyBinding binds : gamesettings.keyBindings) {
			try {
				if (binds.isKeyDown()) out1 += org.lwjgl.input.Keyboard.getKeyName(binds.getKeyCode()) + " ";
			} catch (Exception e3) {
				
			}
		}
		// Add left and right-click to the string if pressed.
		if (gamesettings.keyBindAttack.isKeyDown()) out1 += "LC ";
		if (gamesettings.keyBindUseItem.isKeyDown()) out1 += "RC ";
		return out1;
	}
}
