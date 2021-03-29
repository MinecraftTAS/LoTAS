package de.pfannekuchen.lotas.gui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;

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
		
		public SeedEntry(String name, String description, String seed, int index) {
			this.name = name;
			this.description = description;
			this.seed = seed;
			this.index = index;
		}
		
		public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected) {
			
			String s = name;
			String s1 = description;
			String s2 = seed;

			if (StringUtils.isEmpty(s)) {
				s = I18n.format("selectWorld.world") + " " + (slotIndex + 1);
			}

			s2 = "Seed: " + s2;

			Minecraft.getMinecraft().fontRendererObj.drawString(s, x + 2, y + 1, 16777215);
			Minecraft.getMinecraft().fontRendererObj.drawString(s1, x + 2,
					y + Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT + 3, 8421504);
			Minecraft.getMinecraft().fontRendererObj.drawString(s2, x + 2,
					y + Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT
							+ Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT + 3,
					8421504);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		}
		
		@Override
		public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {

		}

		@Override
		public void setSelected(int p_178011_1_, int p_178011_2_, int p_178011_3_) {
			selectedIndex = index;
		}

		@Override
		public boolean mousePressed(int slotIndex, int p_148278_2_, int p_148278_3_, int p_148278_4_, int p_148278_5_,
				int p_148278_6_) {
			selectedIndex = index;
			return false;
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
