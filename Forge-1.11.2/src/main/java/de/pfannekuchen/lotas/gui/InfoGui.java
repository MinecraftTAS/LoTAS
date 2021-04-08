package de.pfannekuchen.lotas.gui;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class InfoGui {

	@SubscribeEvent
	public void render(RenderGameOverlayEvent e) {
		if (e.getType() != ElementType.TEXT) return;
    	FontRenderer renderer = Minecraft.getMinecraft().fontRendererObj;
    	int height = new ScaledResolution(Minecraft.getMinecraft()).getScaledHeight();
		String out1 = "";
        GameSettings gs = Minecraft.getMinecraft().gameSettings;
		for (KeyBinding binds : gs.keyBindings) {
			try {
				if (binds.isKeyDown()) out1 += Keyboard.getKeyName(binds.getKeyCode()) + " ";
			} catch (Exception e3) {
				
			}
		}
		if (gs.keyBindAttack.isKeyDown()) out1 += "LC ";
		if (gs.keyBindUseItem.isKeyDown()) out1 += "RC ";
        renderer.drawStringWithShadow(out1, 5, height - 11, 0xFFFFFF);
	}
	
}
