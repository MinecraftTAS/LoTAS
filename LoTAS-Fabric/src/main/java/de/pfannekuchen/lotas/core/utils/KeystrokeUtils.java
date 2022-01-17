package de.pfannekuchen.lotas.core.utils;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;

public class KeystrokeUtils {
	
	public static String getKeystrokes() {
		String out = "";
		Options gamesettings = Minecraft.getInstance().options;
		/* Obtain every pressed key and add it to a string */
		for (int i = 0; i < gamesettings.keyMappings.length; i++) {
			KeyMapping binds = gamesettings.keyMappings[i];
			try {
				//#if MC>=11601
//$$ 				if (binds.isDown()) out += binds.getTranslatedKeyMessage().getString().toUpperCase() + " ";
				//#else
				if (binds.isDown()) out += binds.getTranslatedKeyMessage().toUpperCase() + " ";
				//#endif
			} catch (Exception e3) {
				
			}
		}
		if(!out.isEmpty()) {
			out=(String) out.subSequence(0, out.length()-1);
		}
		return out;
	}
}
