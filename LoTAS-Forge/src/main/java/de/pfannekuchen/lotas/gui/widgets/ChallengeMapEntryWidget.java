package de.pfannekuchen.lotas.gui.widgets;

import java.io.IOException;
import java.time.Duration;

import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.core.utils.EventUtils.Timer;
import de.pfannekuchen.lotas.gui.GuiChallengeLeaderboard;
import de.pfannekuchen.lotas.taschallenges.ChallengeLoader;
import de.pfannekuchen.lotas.taschallenges.ChallengeMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

//#if MC>=10900
public class ChallengeMapEntryWidget extends net.minecraft.client.gui.GuiListWorldSelectionEntry {
//#else
//$$ public class ChallengeMapEntryWidget extends net.minecraft.client.gui.GuiSelectWorld.List {
//#endif
	
	
	public ResourceLocation loc = null;
	int width;
	
	// Different to 1.8, instead of having a single instance per element, this is just a full list.
	//#if MC>=10900
	public ChallengeMap map;
	public ChallengeMapEntryWidget(net.minecraft.client.gui.GuiListWorldSelection listWorldSelIn, ChallengeMap map, int width) {
		super(listWorldSelIn, map.getSummary(), map.getSaveLoader());
		this.map = map;
		this.width = width;
	//#else
//$$ 	public ChallengeMapEntryWidget(net.minecraft.client.gui.GuiSelectWorld guiSelectWorld, int width) {
//$$ 		guiSelectWorld.super(Minecraft.getMinecraft());
//$$ 		this.width = width;
	//#endif
	}

	public void deleteWorld() {}
	public void editWorld() {}
	public void recreateWorld() {}
	
	public void joinWorld() {
		//#if MC>=10900
		ChallengeMap.currentMap = map;
		//#else
//$$ 		ChallengeMap.currentMap = de.pfannekuchen.lotas.core.LoTASModContainer.maps.get(selectedElement);
		//#endif
		try {
			ChallengeLoader.load(true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//#if MC<=10809
//$$ 	@Override
//$$ 	protected int getSize() {
//$$ 		return de.pfannekuchen.lotas.core.LoTASModContainer.maps.size();
//$$ 	}
//$$
//$$ 	@Override
//$$ 	protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY) {
//$$ 		selectedElement = slotIndex;
//$$ 		if (isDoubleClick) {
//$$             joinWorld();
//$$         }
//$$ 	}
	//#else
	@Override
	public boolean mousePressed(int slotIndex, int mouseX, int mouseY, int mouseEvent, int relativeX, int relativeY) {
        int posX = width - x - 80;
        int posY = y + MCVer.getFontRenderer(Minecraft.getMinecraft()).FONT_HEIGHT + MCVer.getFontRenderer(Minecraft.getMinecraft()).FONT_HEIGHT + 3;

        if (mouseX > posX && mouseX < (posX + 80) && mouseY > posY && mouseY < (posY + MCVer.getFontRenderer(Minecraft.getMinecraft()).FONT_HEIGHT)) {
        	Minecraft.getMinecraft().displayGuiScreen(new GuiChallengeLeaderboard(map));
        }
		return super.mousePressed(slotIndex, mouseX, mouseY, mouseEvent, relativeX, relativeY);
	}
	//#endif
	
	int x;
	int y;
	
	@Override
	//#if MC>=11200
	public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected, float partialTicks) {
	//#else
	//#if MC>=10900
//$$ 		public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected) {
	//#else
//$$ 		public void drawSlot(int slotIndex, int x, int y, int p_180791_4_, int mouseXIn, int mouseYIn) {
	//#endif
	//#endif
		
		//#if MC>=10900
		this.x = x;
		this.y = y;
		String s = "\u00A76TAS Challenge Map - \u00A7f" + map.displayName;
    	String s1 = map.description;
        String s2 = "WR: " + map.leaderboard[0].split(";")[0] + " - " + Timer.getDuration(Duration.ofMillis(Integer.parseInt(map.leaderboard[0].split(";")[1])));
        //#else
        //$$ String s = "\u00A76TAS Challenge Map - \u00A7f" + de.pfannekuchen.lotas.core.LoTASModContainer.maps.get(selectedElement).displayName;
        //$$ String s1 = de.pfannekuchen.lotas.core.LoTASModContainer.maps.get(selectedElement).description;
        //$$ String s2 = "WR: " + de.pfannekuchen.lotas.core.LoTASModContainer.maps.get(selectedElement).leaderboard[0].split(";")[0] + " - " + Timer.getDuration(Duration.ofMillis(Integer.parseInt(de.pfannekuchen.lotas.core.LoTASModContainer.maps.get(selectedElement).leaderboard[0].split(";")[1])));
        //$$
        //#endif
        
        MCVer.getFontRenderer(Minecraft.getMinecraft()).drawString(s, x + 32 + 3, y + 1, 16777215);
        MCVer.getFontRenderer(Minecraft.getMinecraft()).drawString(s1, x + 32 + 3, y + MCVer.getFontRenderer(Minecraft.getMinecraft()).FONT_HEIGHT + 3, 8421504);
        MCVer.getFontRenderer(Minecraft.getMinecraft()).drawString(s2, x + 32 + 3, y + MCVer.getFontRenderer(Minecraft.getMinecraft()).FONT_HEIGHT + MCVer.getFontRenderer(Minecraft.getMinecraft()).FONT_HEIGHT + 3, 8421504);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        
        //#if MC>=10900
        GlStateManager.enableBlend();
		Minecraft.getMinecraft().getTextureManager().bindTexture(loc);
		Gui.drawModalRectWithCustomSizedTexture(x, y, 0, 0, 32, 32, 32.0F, 32.0F);
		GlStateManager.disableBlend();
        if (Minecraft.getMinecraft().gameSettings.touchscreen || isSelected) {
        	Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("textures/gui/world_selection.png"));
        	Gui.drawRect(x, y, x + 32, y + 32, -1601138544);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            int j = mouseX - x;
            int i = j < 32 ? 32 : 0;

            Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0F, (float)i, 32, 32, 256.0F, 256.0F);
        }
        //#else
        //$$ if (Minecraft.getMinecraft().gameSettings.touchscreen || isSelected(slotIndex)) {
        //$$     Gui.drawRect(x, y, x + 32, y + 32, -1601138544);
        //$$ }
		//#endif
        
        boolean hover = false;
        int posX = width - x - 80;
        int posY = y + MCVer.getFontRenderer(Minecraft.getMinecraft()).FONT_HEIGHT + MCVer.getFontRenderer(Minecraft.getMinecraft()).FONT_HEIGHT + 3;
        
        if (mouseX > posX && mouseX < (posX + 80) && mouseY > posY && mouseY < (posY + MCVer.getFontRenderer(Minecraft.getMinecraft()).FONT_HEIGHT)) {
        	hover = true;
        }
        
        MCVer.getFontRenderer(Minecraft.getMinecraft()).drawString((hover ? "\u00A7l" : "") + "Leaderboard >", posX, posY, 0xadadad);
        
    }
	
}