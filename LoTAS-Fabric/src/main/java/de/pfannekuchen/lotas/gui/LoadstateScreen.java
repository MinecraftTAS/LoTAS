package de.pfannekuchen.lotas.gui;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Comparator;

import com.google.common.io.Files;

import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.mods.SavestateMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;

/**
 * List of States to load/delete
 * @author Pancake
 */
public class LoadstateScreen extends Screen {

	public LoadstateScreen() {
		super(new TextComponent("Loadstate Screen"));
	}

	GuiLoadstateList list;

	@Override
	public void init() {
		try {
			list = new GuiLoadstateList(width, height, 32, height - 64, 36);
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}
		MCVer.addButton(this, MCVer.Button(width / 2 - 102, height - 52, 204, 20, "Loadstate", btn -> {
			SavestateMod.loadstate(list.getSelected().index + 1);
		}));
		MCVer.addButton(this, MCVer.Button(width / 2 - 102, height - 31, 204, 20, "Delete State", btn -> {
			SavestateMod.yeet(list.getSelected().index + 1);
			Minecraft.getInstance().setScreen(new LoadstateScreen());
		}));
		super.init();
	}
	
	//#if MC>=11600
//$$ 	@Override public void render(com.mojang.blaze3d.vertex.PoseStack matrices, int mouseX, int mouseY, float partialTicks) {
//$$ 		MCVer.stack = matrices;
//$$ 		list.render(matrices, mouseX, mouseY, partialTicks);
	//#else
	@Override public void render(int mouseX, int mouseY, float partialTicks) {
	list.render(mouseX, mouseY, partialTicks);
	//#endif
		MCVer.drawCenteredString(this, "Select State to load", width / 2, 16, 0xFFFFFF);
		for(int k = 0; k < MCVer.getButtonSize(this); ++k) {
			MCVer.render(((AbstractWidget)MCVer.getButton(this, k)), mouseX, mouseY, partialTicks);
		}
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		list.mouseClicked(mouseX, mouseY, button);
		return super.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		list.mouseReleased(mouseX, mouseY, button);
		return super.mouseReleased(mouseX, mouseY, button);
	}

	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
		list.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
		return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
	}

	@Override
	public void mouseMoved(double mouseX, double mouseY) {
		list.mouseMoved(mouseX, mouseY);
		super.mouseMoved(mouseX, mouseY);
	}

	@Override
	public boolean mouseScrolled(double d, double e, double amount) {
		list.mouseScrolled(d, e, amount);
		return super.mouseScrolled(d, e, amount);
	}

	public static class GuiLoadstateList extends ObjectSelectionList<GuiLoadstateList.StateEntry> {

		public GuiLoadstateList(int widthIn, int heightIn, int topIn, int bottomIn, int slotHeightIn) throws NumberFormatException, IOException {
			super(Minecraft.getInstance(), widthIn, heightIn, topIn, bottomIn, slotHeightIn);

			File[] f = new File(Minecraft.getInstance().gameDirectory, "saves/savestates/").listFiles(new FilenameFilter() {

				@Override
				public boolean accept(File dir, String name) {
					return name.startsWith(MCVer.getCurrentWorldFolder() + "-Savestate");
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
			int fallbackentry=0;
			for (File file : f) {
				fallbackentry++;
				try {
				addEntry(new StateEntry(Files.readLines(new File(file, "lotas.dat"), StandardCharsets.UTF_8).get(0), "Savestate " + file.getName().split("-Savestate")[1], Integer.parseInt(file.getName().split("-Savestate")[1]) - 1));
				} catch(Exception e) {
					addEntry(new StateEntry("Error while reading the file", "responsible for this text", fallbackentry));
					e.printStackTrace();
				}
			}
		}

		public final class StateEntry extends ObjectSelectionList.Entry<GuiLoadstateList.StateEntry> {

			public String name;
			public String description;
			private int index;

			public StateEntry(String name, String description, int index) {
				this.name = name;
				this.description = description;
				this.index = index;
			}

			//#if MC>=11600
//$$ 			@Override public void render(com.mojang.blaze3d.vertex.PoseStack matrices, int slotIndex, int y, int x, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected, float partialTicks) {
//$$ 				MCVer.stack = matrices;
			//#else
			@Override public void render(int slotIndex, int y, int x, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected, float partialTicks) {
			//#endif
				String s = name;
				String s1 = description;

				MCVer.drawShadow(s, x + 32 + 3, y + 1, 16777215);
				MCVer.drawShadow(s1, x + 32 + 3, y + Minecraft.getInstance().font.lineHeight + 3, 8421504);
				MCVer.color4f(1.0F, 1.0F, 1.0F, 1.0F);
			}

			@Override
			public boolean mouseClicked(double mouseX, double mouseY, int button) {
				GuiLoadstateList.this.setSelected(this);
				return super.mouseClicked(mouseX, mouseY, button);
			}

			@Override
			public boolean mouseReleased(double mouseX, double mouseY, int button) {
				GuiLoadstateList.this.setSelected(this);
				return super.mouseReleased(mouseX, mouseY, button);
			}
			//#if MC>=11700
//$$ 			@Override
//$$ 			public Component getNarration() {
//$$ 				return null;
//$$ 			}
			//#endif
		}

	}

}
