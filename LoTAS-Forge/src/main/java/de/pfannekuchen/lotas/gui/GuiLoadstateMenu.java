package de.pfannekuchen.lotas.gui;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import com.google.common.io.Files;

import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.gui.widgets.ButtonWidget;
import de.pfannekuchen.lotas.mods.SavestateMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;

public class GuiLoadstateMenu extends GuiScreen {

	GuiLoadstateList list;
	
	@Override
	public void initGui() {
		try {
			list = new GuiLoadstateList(width, height, 32, height - 64, 36);
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}
		this.buttonList.add(new GuiButton(0, width / 2 - 102, height - 55, 120, 20, I18n.format("loadstategui.lotas.loadstate")));
		//#if MC>10900
		this.buttonList.add(new GuiButton(1, width / 2 + 22, height - 55, 80, 20, net.minecraft.util.text.TextFormatting.RED+I18n.format("loadstategui.lotas.deletestate")));
		//#else
//$$ 		this.buttonList.add(new GuiButton(1, width / 2 + 22, height - 55, 80, 20, com.mojang.realmsclient.gui.ChatFormatting.RED+I18n.format("loadstategui.lotas.deletestate")));
		//#endif
		this.buttonList.add(new ButtonWidget(width / 2 - 102, height - 31, 204, 20, I18n.format("loadstategui.lotas.back"), btn->{
			Minecraft.getMinecraft().displayGuiScreen(new GuiIngameMenu());
		}));
		super.initGui();
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 0) {
			SavestateMod.loadstate(list.selectedIndex + 1);
		} else if (button.id == 1) {
			SavestateMod.yeet(list.selectedIndex + 1);
			Minecraft.getMinecraft().displayGuiScreen(new GuiLoadstateMenu());
		}
		super.actionPerformed(button);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		list.drawScreen(mouseX, mouseY, partialTicks);
		drawCenteredString(MCVer.getFontRenderer(mc), I18n.format("loadstategui.lotas.info"), width / 2, 16, 0xFFFFFF);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	public void handleMouseInput() throws IOException {
		list.handleMouseInput();
		super.handleMouseInput();
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		list.mouseClicked(mouseX, mouseY, mouseButton);
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		list.mouseReleased(mouseX, mouseY, state);
		super.mouseReleased(mouseX, mouseY, state);
	}
	
	public static class GuiLoadstateList extends GuiListExtended {

		public GuiLoadstateList(int widthIn, int heightIn, int topIn, int bottomIn, int slotHeightIn) throws NumberFormatException, IOException {
			super(Minecraft.getMinecraft(), widthIn, heightIn, topIn, bottomIn, slotHeightIn);
			
			File[] f = new File(Minecraft.getMinecraft().mcDataDir, "saves/savestates/").listFiles(new FilenameFilter() {
				
				@Override
				public boolean accept(File dir, String name) {	
//					System.out.println(name);
					return name.startsWith(Minecraft.getMinecraft().getIntegratedServer().getFolderName() + "-Savestate");
				}
			});
			Arrays.sort(f, new Comparator<File>() {

				@Override
				public int compare(File o1, File o2) {
					Integer o1N = Integer.parseInt(o1.getName().split("-Savestate")[1]);
					Integer o2N = Integer.parseInt(o2.getName().split("-Savestate")[1]);
					return o1N - o2N;
				}
			});
			for (File file : f) {
				try {
				states.add(new StateEntry(Files.readLines(new File(file, "lotas.dat"), StandardCharsets.UTF_8).get(0), "Savestate " + file.getName().split("-Savestate")[1]));
				} catch(Exception e) {
					states.add(new StateEntry(I18n.format("loadstategui.lotas.error.1"), I18n.format("loadstategui.lotas.error.2")));
					e.printStackTrace();
				}
			}
			
		}

		public int selectedIndex = -1;
		public List<StateEntry> states = new ArrayList<GuiLoadstateList.StateEntry>();
		
		public class StateEntry implements Serializable, IGuiListEntry {

			private static final long serialVersionUID = 4428898479076411871L;
			public String name;
			public String description;
			
			public StateEntry(String name, String description) {
				this.name = name;
				this.description = description;
			}
			
			//#if MC>=11200
			@Override
			public void updatePosition(int slotIndex, int x, int y, float partialTicks) {

			}
			@Override
			public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected, float partialTicks) {
			//#else
//$$ 			@Override
//$$ 			public void setSelected(int p_178011_1_, int p_178011_2_, int p_178011_3_) {
//$$
//$$ 			}
//$$ 			@Override
//$$ 			public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected) {
			//#endif
				String s = name;
				String s1 = description;
				
				MCVer.getFontRenderer(Minecraft.getMinecraft()).drawString(s, x + 32 + 3, y + 1, 16777215);
				MCVer.getFontRenderer(Minecraft.getMinecraft()).drawString(s1, x + 32 + 3, y + MCVer.getFontRenderer(Minecraft.getMinecraft()).FONT_HEIGHT + 3, 8421504);
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			}

		    public boolean mousePressed(int slotIndex, int mouseX, int mouseY, int mouseEvent, int relativeX, int relativeY) {
				GuiLoadstateList.this.selectedIndex = slotIndex;
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
			return states.get(index);
		}
		
		@Override
		protected int getSize() {
			return states.size();
		}
	}
	
}
