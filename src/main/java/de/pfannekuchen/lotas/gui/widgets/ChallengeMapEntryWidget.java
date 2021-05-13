package de.pfannekuchen.lotas.gui.widgets;

import java.io.IOException;
import java.time.Duration;

import de.pfannekuchen.lotas.core.LoTASModContainer;
import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.core.utils.EventUtils.Timer;
import de.pfannekuchen.lotas.taschallenges.ChallengeLoader;
import de.pfannekuchen.lotas.taschallenges.ChallengeMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
//#if MC>=10900
import net.minecraft.client.gui.GuiListWorldSelection;
import net.minecraft.client.gui.GuiListWorldSelectionEntry;
//#else
//$$ import net.minecraft.client.gui.GuiSelectWorld;
//$$ import net.minecraft.client.gui.GuiSelectWorld.List;
//#endif
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

//#if MC>=10900
public class ChallengeMapEntryWidget extends GuiListWorldSelectionEntry {
//#else
//$$ public class ChallengeMapEntryWidget extends List {
//#endif
	
	
	public ResourceLocation loc = null;
	
	//#if MC>=10900
	public ChallengeMap map;
	public ChallengeMapEntryWidget(GuiListWorldSelection listWorldSelIn, ChallengeMap map) {
		super(listWorldSelIn, map.getSummary(), map.getSaveLoader());
		this.map = map;
	//#else
//$$ 	public ChallengeMapEntryWidget(GuiSelectWorld guiSelectWorld) {
//$$ 		guiSelectWorld.super(Minecraft.getMinecraft());
	//#endif
	}

	//#if MC>=10900
	private static final ResourceLocation ICON_OVERLAY_LOCATION = new ResourceLocation("textures/gui/world_selection.png");
	//#endif
	
	public void deleteWorld() {}
	public void editWorld() {}
	public void recreateWorld() {}
	
	public void joinWorld() {
		//#if MC>=10900
		ChallengeMap.currentMap = map;
		//#else
//$$ 		ChallengeMap.currentMap = LoTASModContainer.maps.get(selectedElement);
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
//$$ 		return LoTASModContainer.maps.size();
//$$ 	}
//$$
//$$ 	@Override
//$$ 	protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY) {
//$$ 		selectedElement = slotIndex;
//$$ 		if (isDoubleClick) {
//$$             joinWorld();
//$$         }
//$$ 	}
	//#endif
	
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
		String s = "\u00A76TAS Challenge Map - \u00A7f" + map.displayName;
    	String s1 = map.description;
        String s2 = "WR: " + map.leaderboard[0].split(";")[0] + " - " + Timer.getDuration(Duration.ofMillis(Integer.parseInt(map.leaderboard[0].split(";")[1])));
        //#else
        //$$ String s = "\u00A76TAS Challenge Map - \u00A7f" + LoTASModContainer.maps.get(selectedElement).displayName;
        //$$ String s1 = LoTASModContainer.maps.get(selectedElement).description;
        //$$ String s2 = "WR: " + LoTASModContainer.maps.get(selectedElement).leaderboard[0].split(";")[0] + " - " + Timer.getDuration(Duration.ofMillis(Integer.parseInt(LoTASModContainer.maps.get(selectedElement).leaderboard[0].split(";")[1])));
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
        	Minecraft.getMinecraft().getTextureManager().bindTexture(ICON_OVERLAY_LOCATION);
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
		

    }
	
}