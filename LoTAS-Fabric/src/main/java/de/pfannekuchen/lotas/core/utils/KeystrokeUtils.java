package de.pfannekuchen.lotas.core.utils;

import org.lwjgl.glfw.GLFW;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;

public class KeystrokeUtils {
	
	public static String getKeystrokes() {
		String out1 = "";
        Options gamesettings=Minecraft.getInstance().options;
        /* Obtain every pressed key and add it to a string */
		for (KeyMapping binds : gamesettings.keyMappings) {
			try {
				if (binds.isDown()) out1 += binds.getName() + " ";
			} catch (Exception e3) {
				
			}
		}
		// Add left and right-click to the string if pressed.
		if (gamesettings.keyAttack.isDown()) out1 += "LC ";
		if (gamesettings.keyUse.isDown()) out1 += "RC ";
		return out1;
	}
}
