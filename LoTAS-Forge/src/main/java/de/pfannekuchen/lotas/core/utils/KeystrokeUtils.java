package de.pfannekuchen.lotas.core.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;

public class KeystrokeUtils {
	
	public static String getKeystrokes() {
		String out = "";
        GameSettings gamesettings=Minecraft.getMinecraft().gameSettings;
        /* Obtain every pressed key and add it to a string */
		for (net.minecraft.client.settings.KeyBinding binds : gamesettings.keyBindings) {
			try {
				if (binds.isKeyDown()) out += org.lwjgl.input.Keyboard.getKeyName(binds.getKeyCode()) + " ";
			} catch (Exception e3) {
				
			}
		}
		// Add left and right-click to the string if pressed.
		if (gamesettings.keyBindAttack.isKeyDown()) out += "LC ";
		if (gamesettings.keyBindUseItem.isKeyDown()) out += "RC ";
		
		if(!out.isEmpty()) {
			out=(String) out.subSequence(0, out.length()-1);
		}
		return out;
	}
}
