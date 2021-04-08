package de.pfannekuchen.lotas.gui.parts;

import java.io.IOException;
import java.time.Duration;

import de.pfannekuchen.lotas.LoTASModContainer;
import de.pfannekuchen.lotas.challenges.ChallengeLoader;
import de.pfannekuchen.lotas.challenges.ChallengeMap;
import de.pfannekuchen.lotas.tickratechanger.Timer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.gui.GuiSelectWorld.List;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiCustomMapEntry extends List {
	
	public GuiCustomMapEntry(GuiSelectWorld guiSelectWorld, Minecraft mcIn) {
		guiSelectWorld.super(mcIn);
	}

	public ResourceLocation loc = null;

	@Override
	protected int getSize() {
		return LoTASModContainer.maps.size();
	}
	
	@Override
	protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY) {
		selectedElement = slotIndex;
		if (isDoubleClick) {
            joinWorld();
        }
	}
	
	
	
	public void joinWorld() {
		ChallengeLoader.map = LoTASModContainer.maps.get(selectedElement);
		try {
			ChallengeLoader.load(true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected boolean isSelected(int slotIndex) {
		return slotIndex == selectedElement;
	}
	
	@Override
    public void drawSlot(int entryID, int x, int y, int p_180791_4_, int mouseXIn, int mouseYIn) {
		ChallengeMap map = LoTASModContainer.maps.get(entryID);
        String s = "\u00A76TAS Challenge Map - \u00A7f" + map.displayName;
        String s1 = map.description;
        String s2 = "WR: " + map.leaderboard[0].split(";")[0] + " - " + Timer.getDuration(Duration.ofMillis(Integer.parseInt(map.leaderboard[0].split(";")[1])));
        
        Minecraft.getMinecraft().fontRendererObj.drawString(s, x + 32 + 3, y + 1, 16777215);
        Minecraft.getMinecraft().fontRendererObj.drawString(s1, x + 32 + 3, y + Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT + 3, 8421504);
        Minecraft.getMinecraft().fontRendererObj.drawString(s2, x + 32 + 3, y + Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT + Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT + 3, 8421504);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        if (Minecraft.getMinecraft().gameSettings.touchscreen || isSelected(entryID)) {
            Gui.drawRect(x, y, x + 32, y + 32, -1601138544);
        }
    }
	
}