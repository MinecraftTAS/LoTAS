package de.pfannekuchen.lotas.gui;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import com.google.common.io.Files;
import com.mojang.blaze3d.platform.GlStateManager;

import de.pfannekuchen.lotas.savestate.SavestateMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.LiteralText;

public class LoadstateScreen extends Screen {

	public LoadstateScreen() {
		super(new LiteralText("Loadstate Screen"));
	}

	GuiLoadstateList list;
	
	@Override
	public void init() {
		try {
			list = new GuiLoadstateList(width, height, 32, height - 64, 36);
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}
		addButton(new ButtonWidget(width / 2 - 102, height - 52, 204, 20, "Loadstate", btn -> {
			SavestateMod.loadstate(list.selectedIndex + 1);
		}));
		super.init();
	}
	
	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		list.render(mouseX, mouseY, partialTicks);
		drawCenteredString(minecraft.textRenderer, "Select State to load", width / 2, 16, 0xFFFFFF);
		super.render(mouseX, mouseY, partialTicks);
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
		list.mouseClicked((int) mouseX, (int) mouseY, mouseButton);
		return super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	public static class GuiLoadstateList extends AlwaysSelectedEntryListWidget<GuiLoadstateList.StateEntry> {

		public GuiLoadstateList(int widthIn, int heightIn, int topIn, int bottomIn, int slotHeightIn) throws NumberFormatException, IOException {
			super(MinecraftClient.getInstance(), widthIn, heightIn, topIn, bottomIn, slotHeightIn);
			
			File[] f = new File(MinecraftClient.getInstance().runDirectory, "saves/savestates/").listFiles(new FilenameFilter() {
				
				@Override
				public boolean accept(File dir, String name) {	
					return name.startsWith(MinecraftClient.getInstance().getServer().getLevelName() + "-Savestate");
				}
			});
			for (File file : f) {
				states.add(new StateEntry(Files.readLines(new File(file, "lotas.dat"), StandardCharsets.UTF_8).get(0), "Savestate " + file.getName().split("-Savestate")[1], Integer.parseInt(file.getName().split("-Savestate")[1]) - 1));
			}
			
		}

		public final class StateEntry extends AlwaysSelectedEntryListWidget.Entry<GuiLoadstateList.StateEntry> {

			public String name;
			public String description;
			private int index;
			
			public StateEntry(String name, String description, int index) {
				this.name = name;
				this.description = description;
				this.index = index;
			}
			
			@Override
			public void render(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected, float partialTicks) {
				
				String s = name;
				String s1 = description;
				
				MinecraftClient.getInstance().textRenderer.draw(s, x + 32 + 3, y + 1, 16777215);
				MinecraftClient.getInstance().textRenderer.draw(s1, x + 32 + 3,
						y + MinecraftClient.getInstance().textRenderer.fontHeight + 3, 8421504);
				GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
			}

		    public boolean mousePressed(int slotIndex, int mouseX, int mouseY, int mouseEvent, int relativeX, int relativeY) {
		    	GuiLoadstateList.this.selectedIndex = index;
		        return false;
		    }

		}
		
		public int selectedIndex = -1;
		public List<StateEntry> states = new ArrayList<>();
		
		@Override
		protected boolean isSelectedItem(int index) {
			return selectedIndex == index;
		}
		
		@Override
		protected StateEntry getEntry(int index) {
			return states.get(index);
		}
		
		@Override
		protected int getItemCount() {
			return states.size();
		}
		
		public List<StateEntry> getStates() {
			return states;
		}
	}
	
}
