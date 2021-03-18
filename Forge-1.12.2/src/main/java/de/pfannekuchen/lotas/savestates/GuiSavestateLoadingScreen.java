package de.pfannekuchen.lotas.savestates;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;

public class GuiSavestateLoadingScreen extends GuiScreen{

	private static boolean copying;
	private static boolean deleting;

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		
		ScaledResolution scaled = new ScaledResolution(Minecraft.getMinecraft());
		int width = scaled.getScaledWidth();
		int height = scaled.getScaledHeight();
		
		drawCenteredString(fontRenderer,I18n.format("Loading a savestate!"), width / 2, height / 4 + 50 + -16, 0xFFFFFF);	//Loading a savestate!

		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return true;
	}
}
