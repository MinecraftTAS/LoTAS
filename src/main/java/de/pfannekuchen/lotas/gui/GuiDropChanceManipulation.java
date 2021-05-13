package de.pfannekuchen.lotas.gui;

import java.util.ArrayList;
import java.util.List;

import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.dropmanipulation.drops.blockdrops.DeadbushDropManipulation;
import de.pfannekuchen.lotas.dropmanipulation.drops.blockdrops.GlowstoneDropManipulation;
import de.pfannekuchen.lotas.dropmanipulation.drops.blockdrops.GravelDropManipulation;
import de.pfannekuchen.lotas.dropmanipulation.drops.blockdrops.LeavesDropManipulation;
import de.pfannekuchen.lotas.dropmanipulation.drops.blockdrops.OreDropManipulation;
import de.pfannekuchen.lotas.dropmanipulation.drops.blockdrops.PlantsDropManipulation;
import de.pfannekuchen.lotas.dropmanipulation.drops.blockdrops.SealanternDropManipulation;
import de.pfannekuchen.lotas.dropmanipulation.drops.entitydrops.MonsterDropManipulation;
import de.pfannekuchen.lotas.dropmanipulation.drops.entitydrops.NetherMobDropManipulation;
import de.pfannekuchen.lotas.dropmanipulation.drops.entitydrops.PassiveDropManipulation;
import de.pfannekuchen.lotas.dropmanipulation.drops.entitydrops.ZombieDropManipulation;
import de.pfannekuchen.lotas.gui.widgets.CheckboxWidget;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiScreen;
//#if MC>=11200
import net.minecraft.client.renderer.BufferBuilder;
//#else
//$$ import net.minecraft.client.renderer.VertexBuffer;
//#endif
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;

public class GuiDropChanceManipulation extends GuiScreen {


	public static ArrayList<DropManipulation> manipulations;
	public static int selected = 0;

	static {
		manipulations = new ArrayList<>();
		manipulations.add(new GravelDropManipulation(0, 0, 0, 0));
		manipulations.add(new LeavesDropManipulation(0, 0, 0, 0));
		manipulations.add(new PlantsDropManipulation(0, 0, 0, 0));
		manipulations.add(new DeadbushDropManipulation(0, 0, 0, 0));
		manipulations.add(new GlowstoneDropManipulation(0, 0, 0, 0));
		manipulations.add(new SealanternDropManipulation(0, 0, 0, 0));
		manipulations.add(new OreDropManipulation(0, 0, 0, 0));
		manipulations.add(new NetherMobDropManipulation(0, 0, 0, 0));
		manipulations.add(new PassiveDropManipulation(0, 0, 0, 0));
		manipulations.add(new MonsterDropManipulation(0, 0, 0, 0));
		manipulations.add(new ZombieDropManipulation(0, 0, 0, 0));
	}

	public GuiDropChanceManipulation(GuiIngameMenu gameMenuScreen) {
		super();
	}

	@Override
	public void initGui() {
		for (DropManipulation man : manipulations) {
			DropManipulation.width = width;
			DropManipulation.height = height;
			DropManipulation.y = 25;
			DropManipulation.x = (int) (width / 3.5f + 24);
			man.update();
		}
		super.initGui();
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int button) {
		manipulations.get(selected).mouseAction(mouseX, mouseY, button);
		if (mouseX > width / 3.5f || mouseX < 25) return;
		mouseY-=30;
		mouseY /= 15;
		if (mouseY < 0) return;
		if (manipulations.size() - 1 >= (int) mouseY)
			selected = (int) mouseY;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float delta) {
		drawDefaultBackground();
		drawBackground(0);

		Tessellator tessellator = Tessellator.getInstance();
		//#if MC>=11200
		BufferBuilder bufferBuilder = tessellator.getBuffer();
		//#else
//$$ 		VertexBuffer bufferBuilder = tessellator.getBuffer();
		//#endif
		
		GlStateManager.disableTexture2D();
		GlStateManager.color(.5f, .5f, .5f, 0.5F);
		bufferBuilder.begin(7, DefaultVertexFormats.POSITION);
		bufferBuilder.pos(24, 24, 0).endVertex();
		bufferBuilder.pos(24, height - 24, 0).endVertex();
		bufferBuilder.pos(width / 3.5f + 1, height - 24, 0).endVertex();
		bufferBuilder.pos(width / 3.5f + 1, 24, 0).endVertex();
		tessellator.draw();

		GlStateManager.color(0, 0, 0, 0.5F);
		bufferBuilder.begin(7, DefaultVertexFormats.POSITION);
		bufferBuilder.pos(25, 25, 0).endVertex();
		bufferBuilder.pos(25, height - 25, 0).endVertex();
		bufferBuilder.pos(width / 3.5f, height - 25, 0).endVertex();
		bufferBuilder.pos(width / 3.5f, 25, 0).endVertex();
		tessellator.draw();

		int boxY = 30 + selected * 15;
		GlStateManager.color(1f, 1f, 1f, 1F);
		bufferBuilder.begin(7, DefaultVertexFormats.POSITION);
		bufferBuilder.pos(27, boxY - 4, 0).endVertex();
		bufferBuilder.pos(27, boxY + 11, 0).endVertex();
		bufferBuilder.pos(width / 3.5f - 2, boxY + 11, 0).endVertex();
		bufferBuilder.pos(width / 3.5f - 2, boxY - 4, 0).endVertex();
		tessellator.draw();
		GlStateManager.color(0f, 0f, 0f, .5F);
		bufferBuilder.begin(7, DefaultVertexFormats.POSITION);
		bufferBuilder.pos(28, boxY - 3, 0).endVertex();
		bufferBuilder.pos(28, boxY + 10, 0).endVertex();
		bufferBuilder.pos(width / 3.5f - 3, boxY + 10, 0).endVertex();
		bufferBuilder.pos(width / 3.5f - 3, boxY - 3, 0).endVertex();
		tessellator.draw();

		GlStateManager.enableTexture2D();
		int y = 30;
		for (DropManipulation m : manipulations) {
			drawString(MCVer.getFontRenderer(mc), m.getName(), 32, y, 0xFFFFFF);
			y += 15;
		}
		manipulations.get(selected).render(mouseX, mouseY, delta);

		super.drawScreen(mouseX, mouseY, delta);
	}

	public static abstract class DropManipulation {
		public static int x;
		public static int y;
		public static int width;
		public static int height;
		public CheckboxWidget enabled;
		public abstract String getName();
		public abstract List<ItemStack> redirectDrops(IBlockState block);
		public abstract List<ItemStack> redirectDrops(Entity entity);
		public abstract void update();
		public abstract void mouseAction(int mouseX, int mouseY, int button);
		public abstract void render(int mouseX, int mouseY, float delta);
		
		public void updateY(GuiButton btn, int y) {
			//#if MC>=11200
			btn.x = y;
			//#else
//$$ 			btn.xPosition = y;
			//#endif
		}
		
		public void updateX(GuiButton btn, int x) {
			//#if MC>=11200
			btn.x = x;
			//#else
//$$ 			btn.xPosition = x;
			//#endif
		}
	}

}