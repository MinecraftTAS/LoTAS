package de.pfannekuchen.lotas.gui;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Comparator;

import com.google.common.io.Files;
import com.mojang.blaze3d.platform.GlStateManager;

import de.pfannekuchen.lotas.gui.widgets.NewButtonWidget;
import de.pfannekuchen.lotas.mods.SavestateMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
//#if MC>=11601
//$$ import net.minecraft.client.util.math.MatrixStack;
//#endif
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

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
		//#if MC>=11700
//$$ 		addDrawable(new NewButtonWidget(width / 2 - 102, height - 52, 204, 20, "Loadstate", btn -> {
//$$ 			SavestateMod.loadstate(list.getSelectedOrNull().index + 1);
//$$ 		}));
//$$ 		addDrawable(new NewButtonWidget(width / 2 - 102, height - 31, 204, 20, "Delete State", btn -> {
//$$ 			SavestateMod.yeet(list.getSelectedOrNull().index + 1);
//$$ 			MinecraftClient.getInstance().openScreen(new LoadstateScreen());
//$$ 		}));
		//#else
		addButton(new NewButtonWidget(width / 2 - 102, height - 52, 204, 20, "Loadstate", btn -> {
			SavestateMod.loadstate(list.getSelected().index + 1);
		}));
		addButton(new NewButtonWidget(width / 2 - 102, height - 31, 204, 20, "Delete State", btn -> {
			SavestateMod.yeet(list.getSelected().index + 1);
			MinecraftClient.getInstance().openScreen(new LoadstateScreen());
		}));
		//#endif
		super.init();
	}

	//#if MC>=11601
//$$ 	@Override public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
//$$ 		list.render(stack, mouseX, mouseY, partialTicks);
		//#if MC>=11700
//$$ 		drawCenteredText(stack, MinecraftClient.getInstance().textRenderer, "Select State to load", width / 2, 16, 0xFFFFFF);
		//#else
//$$ 		drawCenteredString(stack, MinecraftClient.getInstance().textRenderer, "Select State to load", width / 2, 16, 0xFFFFFF);
		//#endif
//$$ 		super.render(stack, mouseX, mouseY, partialTicks);
//$$ 	}
	//#else
	@Override public void render(int mouseX, int mouseY, float partialTicks) {
		list.render(mouseX, mouseY, partialTicks);
		drawCenteredString(MinecraftClient.getInstance().textRenderer, "Select State to load", width / 2, 16, 0xFFFFFF);
		super.render(mouseX, mouseY, partialTicks);
	}
	//#endif

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

	public static class GuiLoadstateList extends AlwaysSelectedEntryListWidget<GuiLoadstateList.StateEntry> {

		public GuiLoadstateList(int widthIn, int heightIn, int topIn, int bottomIn, int slotHeightIn) throws NumberFormatException, IOException {
			super(MinecraftClient.getInstance(), widthIn, heightIn, topIn, bottomIn, slotHeightIn);

			File[] f = new File(MinecraftClient.getInstance().runDirectory, "saves/savestates/").listFiles(new FilenameFilter() {

				@Override
				public boolean accept(File dir, String name) {
					//#if MC>=11601
//$$ 					return name.startsWith(MinecraftClient.getInstance().getServer().getSaveProperties().getLevelName() + "-Savestate");
					//#else
					return name.startsWith(MinecraftClient.getInstance().getServer().getLevelName() + "-Savestate");
					//#endif
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
				addEntry(new StateEntry(Files.readLines(new File(file, "lotas.dat"), StandardCharsets.UTF_8).get(0), "Savestate " + file.getName().split("-Savestate")[1], Integer.parseInt(file.getName().split("-Savestate")[1]) - 1));
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

			//#if MC>=11601
//$$ 			@Override public void render(MatrixStack matrices, int slotIndex, int y, int x, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected, float partialTicks) {
			//#else
			@Override public void render(int slotIndex, int y, int x, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected, float partialTicks) {
			//#endif
				String s = name;
				String s1 = description;

				//#if MC>=11601
//$$ 				MinecraftClient.getInstance().textRenderer.draw(matrices, s, x + 32 + 3, y + 1, 16777215);
//$$ 				MinecraftClient.getInstance().textRenderer.draw(matrices, s1, x + 32 + 3, y + MinecraftClient.getInstance().textRenderer.fontHeight + 3, 8421504);
				//#else
				MinecraftClient.getInstance().textRenderer.draw(s, x + 32 + 3, y + 1, 16777215);
				MinecraftClient.getInstance().textRenderer.draw(s1, x + 32 + 3, y + MinecraftClient.getInstance().textRenderer.fontHeight + 3, 8421504);
				//#endif
				//#if MC>=11700
//$$ 				com.mojang.blaze3d.systems.RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
				//#else
				GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
				//#endif
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
//$$ 			public Text method_37006() {
//$$ 				// TODO Auto-generated method stub
//$$ 				return null;
//$$ 			}
			//#endif
		}

	}

}
