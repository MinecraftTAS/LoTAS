package de.pfannekuchen.lotas.gui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

/**
 * Draws a list of commonly used seeds in TASing and speedrunning.
 * @author Pancake
 * @see GuiSeedsMenu
 */
public class GuiSeedList extends GuiListExtended {

	public GuiSeedList(int widthIn, int heightIn, int topIn, int bottomIn, int slotHeightIn) {
		super(Minecraft.getMinecraft(), widthIn, heightIn, topIn, bottomIn, slotHeightIn);
	}

	public static int selectedSeed = 1234;
	public static int selectedIndex = -1;
	public static List<SeedEntry> seeds = new ArrayList<GuiSeedList.SeedEntry>();
	
	public static class SeedEntry implements Serializable, IGuiListEntry {

		private static final long serialVersionUID = 4428898479076411871L;
		public String name;
		public String description;
		public String seed;
		private int index;
		public ResourceLocation loc = null;
		
		public SeedEntry(String name, String description, String seed, int index) {
			this.name = name;
			this.description = description;
			this.seed = seed;
			this.index = index;
		}
		
		@Override
		public void updatePosition(int slotIndex, int x, int y, float partialTicks) {

		}
		
		@Override
		public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY,
				boolean isSelected, float partialTicks) {
			
			String s = name;
			String s1 = description;
			String s2 = seed;

			if (StringUtils.isEmpty(s)) {
				s = I18n.format("selectWorld.world") + " " + (slotIndex + 1);
			}

			s2 = "Seed: " + s2;

			Minecraft.getMinecraft().fontRenderer.drawString(s, x + 32 + 3, y + 1, 16777215);
			Minecraft.getMinecraft().fontRenderer.drawString(s1, x + 32 + 3,
					y + Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT + 3, 8421504);
			Minecraft.getMinecraft().fontRenderer.drawString(s2, x + 32 + 3,
					y + Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT
							+ Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT + 3,
					8421504);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			GlStateManager.enableBlend();
			Minecraft.getMinecraft().getTextureManager().bindTexture(loc);
			Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0F, 0.0F, 32, 32, 32.0F, 32.0F);
			GlStateManager.disableBlend();
		}

	    public boolean mousePressed(int slotIndex, int mouseX, int mouseY, int mouseEvent, int relativeX, int relativeY) {
			selectedIndex = index;
	        return false;
	    }

		@Override
		public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {

		}

	}
	
	@Override
	protected boolean isSelected(int slotIndex) {
		return selectedIndex == slotIndex;
	}
	
	@Override
	public boolean mouseClicked(int mouseX, int mouseY, int mouseEvent) {
		return super.mouseClicked(mouseX, mouseY, mouseEvent);
	}
	
	@Override
	public IGuiListEntry getListEntry(int index) {
		return seeds.get(index);
	}
	
	@Override
	protected int getSize() {
		return seeds.size();
	}
}
