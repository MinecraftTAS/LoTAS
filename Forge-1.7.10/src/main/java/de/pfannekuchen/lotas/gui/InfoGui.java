package de.pfannekuchen.lotas.gui;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;

public class InfoGui {

	@SubscribeEvent
	public void render(RenderGameOverlayEvent e) {
		if (e.type != ElementType.TEXT) return;
    	FontRenderer renderer = Minecraft.getMinecraft().fontRendererObj;
    	int height = Minecraft.getMinecraft().displayHeight;
		String out1 = "";
        GameSettings gs = Minecraft.getMinecraft().gameSettings;
		for (KeyBinding binds : gs.keyBindings) {
			try {
				if (binds.getIsKeyPressed()) out1 += Keyboard.getKeyName(binds.getKeyCode()) + " ";
			} catch (Exception e3) {
				
			}
		}
		if (gs.keyBindAttack.getIsKeyPressed()) out1 += "LC ";
		if (gs.keyBindUseItem.getIsKeyPressed()) out1 += "RC ";
        renderer.drawStringWithShadow(out1, 5, height - 11, 0xFFFFFF);
	}
	
}
