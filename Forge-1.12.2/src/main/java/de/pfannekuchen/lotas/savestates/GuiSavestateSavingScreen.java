package de.pfannekuchen.lotas.savestates;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;

public class GuiSavestateSavingScreen extends GuiScreen{


	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		
		ScaledResolution scaled = new ScaledResolution(Minecraft.getMinecraft());
		int width = scaled.getScaledWidth();
		int height = scaled.getScaledHeight();
		
		drawCenteredString(fontRenderer,I18n.format("Making a savestate, please wait!"),width / 2,height / 4 + 34 + -16, 0xFFFFFF); //Making a savestate, please wait!
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return true;
	}
}
