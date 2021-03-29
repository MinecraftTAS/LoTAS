package de.pfannekuchen.lotas.gui;

import java.io.IOException;

import de.pfannekuchen.lotas.config.ConfigManager;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class GuiAcceptance extends GuiScreen {
	
    private static final ResourceLocation DEMO_BACKGROUND_LOCATION = new ResourceLocation("textures/gui/demo_background.png");
    
    @Override
	public void initGui() {
		this.buttonList.clear();
		this.buttonList.add(new GuiButton(1, this.width / 2 - 116, this.height / 2 + 62 + -16, 114, 20,
				I18n.format("Accept")));
		this.buttonList.add(new GuiButton(2, this.width / 2 + 2, this.height / 2 + 62 + -16, 114, 20,
				I18n.format("Decline")));
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		switch (button.id) {
		case 1:
			ConfigManager.setBoolean("hidden", "acceptedDataSending", true);
			ConfigManager.save();
			this.mc.displayGuiScreen(new GuiMainMenu());
			break;
		case 2:
			FMLCommonHandler.instance().exitJava(29, true);
		}
	}
	
	@Override
	public void drawDefaultBackground() {
		super.drawDefaultBackground();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(DEMO_BACKGROUND_LOCATION);
		int i = (this.width - 248) / 2;
		int j = (this.height - 166) / 2;
		this.drawTexturedModalRect(i, j, 0, 0, 248, 166);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		int i = (this.width - 248) / 2 + 10;
		int j = (this.height - 166) / 2 + 8;
		this.fontRendererObj.drawString(I18n.format("LoTAS Cheat prevention"), i, j, 2039583);
		j = j + 12;
		this.fontRendererObj.drawString("This mod collects a bit of data", i, j, 5197647);
		this.fontRendererObj.drawString(I18n.format("to prevent cheating."), i, j + 12, 5197647);
		this.fontRendererObj.drawString(I18n.format("Your data will be hashed and encrypted."), i, j + 24, 5197647);
		this.fontRendererObj.drawString(I18n.format("\u00A7cYour Data is unreadable to anyone!"),
				i, j + 36, 5197647);
		this.fontRendererObj.drawSplitString(I18n.format("If you are confused or worried, pm me on discord: MCPfannkuchenYT#9745."), i, j + 68, 218, 2039583);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

}
