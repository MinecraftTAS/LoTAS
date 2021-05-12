package de.pfannekuchen.lotas.gui.widgets;

import java.io.IOException;
import java.time.Duration;

import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.core.utils.EventUtils.Timer;
import de.pfannekuchen.lotas.taschallenges.ChallengeLoader;
import de.pfannekuchen.lotas.taschallenges.ChallengeMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiListWorldSelection;
import net.minecraft.client.gui.GuiListWorldSelectionEntry;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class ChallengeMapEntryWidget extends GuiListWorldSelectionEntry {
	
	public ChallengeMap map;
	public ResourceLocation loc = null;
	
	public ChallengeMapEntryWidget(GuiListWorldSelection listWorldSelIn, ChallengeMap map) {
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
		ChallengeMap.currentMap = map;
		try {
			ChallengeLoader.load(true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//#if MC>=11200
    public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected, float partialTicks) {
    //#else
    //$$ public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected, float partialTicks) {
    //#endif
    	String s = "\u00A76TAS Challenge Map - \u00A7f" + map.displayName;
        String s1 = map.description;
        String s2 = "WR: " + map.leaderboard[0].split(";")[0] + " - " + Timer.getDuration(Duration.ofMillis(Integer.parseInt(map.leaderboard[0].split(";")[1])));
        
        MCVer.getFontRenderer(Minecraft.getMinecraft()).drawString(s, x + 32 + 3, y + 1, 16777215);
        MCVer.getFontRenderer(Minecraft.getMinecraft()).drawString(s1, x + 32 + 3, y + MCVer.getFontRenderer(Minecraft.getMinecraft()).FONT_HEIGHT + 3, 8421504);
        MCVer.getFontRenderer(Minecraft.getMinecraft()).drawString(s2, x + 32 + 3, y + MCVer.getFontRenderer(Minecraft.getMinecraft()).FONT_HEIGHT + MCVer.getFontRenderer(Minecraft.getMinecraft()).FONT_HEIGHT + 3, 8421504);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        
        // DRAW ICON
		GlStateManager.enableBlend();
		Minecraft.getMinecraft().getTextureManager().bindTexture(loc);
		Gui.drawModalRectWithCustomSizedTexture(x, y, 0, 0, 32, 32, 32.0F, 32.0F);
		GlStateManager.disableBlend();

        if (Minecraft.getMinecraft().gameSettings.touchscreen || isSelected)
        {
        	Minecraft.getMinecraft().getTextureManager().bindTexture(ICON_OVERLAY_LOCATION);
            Gui.drawRect(x, y, x + 32, y + 32, -1601138544);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            int j = mouseX - x;
            int i = j < 32 ? 32 : 0;

            Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0F, (float)i, 32, 32, 256.0F, 256.0F);
        }
    }
	
}