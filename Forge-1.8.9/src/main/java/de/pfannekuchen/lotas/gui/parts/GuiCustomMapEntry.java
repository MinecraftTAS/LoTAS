package de.pfannekuchen.lotas.gui.parts;

import java.io.IOException;
import java.time.Duration;

import de.pfannekuchen.lotas.challenges.ChallengeLoader;
import de.pfannekuchen.lotas.challenges.ChallengeMap;
import de.pfannekuchen.lotas.tickratechanger.Timer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiCustomMapEntry extends GuiSelectWorld {
	
	public ChallengeMap map;
	public ResourceLocation loc = null;
	
	public GuiCustomMapEntry(GuiSelectWorld listWorldSelIn, ChallengeMap map) {
		super(listWorldSelIn);
		this.map = map;
	}

	private static final ResourceLocation ICON_OVERLAY_LOCATION = new ResourceLocation("textures/gui/world_selection.png");
	
	@Override
	public void func_146615_e(int p_146615_1_) {
		ChallengeLoader.map = map;
		try {
			ChallengeLoader.load(true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
    public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected, float partialTicks) {
        String s = "\u00A76TAS Challenge Map - \u00A7f" + map.displayName;
        String s1 = map.description;
        String s2 = "WR: " + map.leaderboard[0].split(";")[0] + " - " + Timer.getDuration(Duration.ofMillis(Integer.parseInt(map.leaderboard[0].split(";")[1])));
        
        this.mc.fontRendererObj.drawString(s, x + 32 + 3, y + 1, 16777215);
        this.mc.fontRendererObj.drawString(s1, x + 32 + 3, y + this.mc.fontRendererObj.FONT_HEIGHT + 3, 8421504);
        this.mc.fontRendererObj.drawString(s2, x + 32 + 3, y + this.mc.fontRendererObj.FONT_HEIGHT + this.mc.fontRendererObj.FONT_HEIGHT + 3, 8421504);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        
        // DRAW ICON
		GlStateManager.enableBlend();
		Minecraft.getMinecraft().getTextureManager().bindTexture(loc);
		Gui.drawModalRectWithCustomSizedTexture(x, y, 0, 0, 32, 32, 32.0F, 32.0F);
		GlStateManager.disableBlend();

        if (this.mc.gameSettings.touchscreen || isSelected)
        {
            this.mc.getTextureManager().bindTexture(ICON_OVERLAY_LOCATION);
            Gui.drawRect(x, y, x + 32, y + 32, -1601138544);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            int j = mouseX - x;
            int i = j < 32 ? 32 : 0;

            Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0F, (float)i, 32, 32, 256.0F, 256.0F);
        }
    }
	
}