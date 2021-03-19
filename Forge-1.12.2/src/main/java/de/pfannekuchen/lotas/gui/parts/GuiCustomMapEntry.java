package de.pfannekuchen.lotas.gui.parts;

import java.io.IOException;

import de.pfannekuchen.lotas.challenges.ChallengeLoader;
import de.pfannekuchen.lotas.challenges.ChallengeMap;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiListWorldSelection;
import net.minecraft.client.gui.GuiListWorldSelectionEntry;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiCustomMapEntry extends GuiListWorldSelectionEntry {
	
	public ChallengeMap map;
	
	public GuiCustomMapEntry(GuiListWorldSelection listWorldSelIn, ChallengeMap map) {
		super(listWorldSelIn, map.getSummary(), map.getSaveLoader());
		this.map = map;
	}

	private static final ResourceLocation ICON_OVERLAY_LOCATION = new ResourceLocation("textures/gui/world_selection.png");
	
	@Override
	public void deleteWorld() {

	}
	
	@Override
	public void editWorld() {
		
	}
	
	@Override
	public void recreateWorld() {
		
	}
	
	@Override
	public void joinWorld() {
		ChallengeLoader.map = map;
		try {
			ChallengeLoader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
    public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected, float partialTicks) {
        String s = "\u00A76TAS Challenge Map - \u00A7f" + map.displayName;
        String s1 = map.description;
        String s2 = "WR: " + map.leaderboard[0].split(";")[0] + " - " + map.leaderboard[0].split(";")[1];
        
        this.client.fontRenderer.drawString(s, x + 32 + 3, y + 1, 16777215);
        this.client.fontRenderer.drawString(s1, x + 32 + 3, y + this.client.fontRenderer.FONT_HEIGHT + 3, 8421504);
        this.client.fontRenderer.drawString(s2, x + 32 + 3, y + this.client.fontRenderer.FONT_HEIGHT + this.client.fontRenderer.FONT_HEIGHT + 3, 8421504);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        
        // DRAW ICON
        /*this.client.getTextureManager().bindTexture(this.icon != null ? this.iconLocation : ICON_MISSING);
        GlStateManager.enableBlend();
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0F, 0.0F, 32, 32, 32.0F, 32.0F);
        GlStateManager.disableBlend();*/

        if (this.client.gameSettings.touchscreen || isSelected)
        {
            this.client.getTextureManager().bindTexture(ICON_OVERLAY_LOCATION);
            Gui.drawRect(x, y, x + 32, y + 32, -1601138544);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            int j = mouseX - x;
            int i = j < 32 ? 32 : 0;

            Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0F, (float)i, 32, 32, 256.0F, 256.0F);
        }
    }
	
}