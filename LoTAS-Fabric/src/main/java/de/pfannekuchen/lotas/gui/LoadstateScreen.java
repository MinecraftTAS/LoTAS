package de.pfannekuchen.lotas.gui;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Comparator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;
import com.google.common.io.Files;
import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.gui.components.Button;
import de.pfannekuchen.lotas.mods.SavestateMod;

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
		addButton(new Button(width / 2 - 102, height - 52, 204, 20, "Loadstate", btn -> {
			SavestateMod.loadstate(list.getSelected().index + 1);
		}));
		addButton(new Button(width / 2 - 102, height - 31, 204, 20, "Delete State", btn -> {
			SavestateMod.yeet(list.getSelected().index + 1);
			Minecraft.getInstance().setScreen(new LoadstateScreen());
		}));
		super.init();
	}

	@Override public void render(int mouseX, int mouseY, float partialTicks) {
		list.render(mouseX, mouseY, partialTicks);
		drawCenteredString(Minecraft.getInstance().font, "Select State to load", width / 2, 16, 0xFFFFFF);
		super.render(mouseX, mouseY, partialTicks);
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
					return name.startsWith(Minecraft.getInstance().getSingleplayerServer().getLevelIdName() + "-Savestate");
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

			@Override public void render(int slotIndex, int y, int x, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected, float partialTicks) {
				String s = name;
				String s1 = description;

				Minecraft.getInstance().font.draw(s, x + 32 + 3, y + 1, 16777215);
				Minecraft.getInstance().font.draw(s1, x + 32 + 3, y + Minecraft.getInstance().font.lineHeight + 3, 8421504);
				GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
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
		}

	}

}
