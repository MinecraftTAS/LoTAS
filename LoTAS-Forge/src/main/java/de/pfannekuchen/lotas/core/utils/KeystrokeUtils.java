package de.pfannekuchen.lotas.core.utils;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;

public class KeystrokeUtils {
	
	public static String getKeystrokes() {
		String out = "";
        GameSettings gamesettings=Minecraft.getMinecraft().gameSettings;
        /* Obtain every pressed key and add it to a string */
		for (net.minecraft.client.settings.KeyBinding binds : gamesettings.keyBindings) {
			try {
				if (Keyboard.isKeyDown(binds.getKeyCode())) out += org.lwjgl.input.Keyboard.getKeyName(binds.getKeyCode()) + " ";
			} catch (Exception e3) {
				
			}
		}
		// Add left and right-click to the string if pressed.
		if (Mouse.isButtonDown(gamesettings.keyBindAttack.getKeyCode()+100)) out += "LC ";
		if (Mouse.isButtonDown(gamesettings.keyBindUseItem.getKeyCode()+100)) out += "RC ";
		
		if(!out.isEmpty()) {
			out=(String) out.subSequence(0, out.length()-1);
		}
		return out;
	}
}
