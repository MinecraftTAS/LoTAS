package de.pfannekuchen.lotas.gui;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.google.common.io.Files;

import de.pfannekuchen.lotas.savestate.SavestateMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;

public class GuiLoadstate extends GuiScreen {

	GuiLoadstateList list;
	
	@Override
	public void initGui() {
		try {
			list = new GuiLoadstateList(width, height, 32, height - 64, 36);
		} catch (Exception e) {
			e.printStackTrace();
		}
		buttonList.add(new GuiButton(0, width / 2 - 102, height - 52, 204, 20, "Loadstate"));
		super.initGui();
	}
	
	@Override
	protected void actionPerformed(GuiButton button)  {
		if (button.id == 0) {
			SavestateMod.loadstate(list.selectedIndex + 1);
		}
		super.actionPerformed(button);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		list.drawScreen(mouseX, mouseY, partialTicks);
		drawCenteredString(mc.fontRendererObj, "Select State to load", width / 2, 16, 0xFFFFFF);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton)  {
		list.func_148179_a(mouseX, mouseY, mouseButton);
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	public static class GuiLoadstateList extends GuiListExtended {

		public GuiLoadstateList(int widthIn, int heightIn, int topIn, int bottomIn, int slotHeightIn) throws NumberFormatException, IOException {
			super(Minecraft.getMinecraft(), widthIn, heightIn, topIn, bottomIn, slotHeightIn);
			
			File[] f = new File(Minecraft.getMinecraft().mcDataDir, "saves/savestates/").listFiles(new FilenameFilter() {
				
				@Override
				public boolean accept(File dir, String name) {	
					return name.startsWith(Minecraft.getMinecraft().theIntegratedServer.getFolderName() + "-Savestate");
				}
			});
			for (File file : f) {
				states.add(new StateEntry(Files.readLines(new File(file, "lotas.dat"), StandardCharsets.UTF_8).get(0), "Savestate " + file.getName().split("-Savestate")[1], Integer.parseInt(file.getName().split("-Savestate")[1]) - 1));
			}
			
		}

		public int selectedIndex = -1;
		public List<StateEntry> states = new ArrayList<GuiLoadstateList.StateEntry>();
		
		public class StateEntry implements Serializable, IGuiListEntry {

			private static final long serialVersionUID = 4428898479076411871L;
			public String name;
			public String description;
			private int index;
			
			public StateEntry(String name, String description, int index) {
				this.name = name;
				this.description = description;
				this.index = index;
			}
			
			@Override
			public void drawEntry(int e, int x, int y, int var4, int var5, Tessellator var6, int var7,
					int var8, boolean var9) {
				
				String s = name;
				String s1 = description;
				
				Minecraft.getMinecraft().fontRendererObj.drawString(s, x + 32 + 3, y + 1, 16777215);
				Minecraft.getMinecraft().fontRendererObj.drawString(s1, x + 32 + 3,
						y + Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT + 3, 8421504);
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			}
			
		    public boolean mousePressed(int slotIndex, int mouseX, int mouseY, int mouseEvent, int relativeX, int relativeY) {
				GuiLoadstateList.this.selectedIndex = index;
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
		public IGuiListEntry getListEntry(int index) {
			return states.get(index);
		}
		
		@Override
		protected int getSize() {
			return states.size();
		}
	}
	
}
