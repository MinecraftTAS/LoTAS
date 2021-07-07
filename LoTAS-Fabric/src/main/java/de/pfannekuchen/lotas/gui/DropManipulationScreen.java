package de.pfannekuchen.lotas.gui;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;

import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.dropmanipulation.drops.blockdrops.DeadbushDropManipulation;
import de.pfannekuchen.lotas.dropmanipulation.drops.blockdrops.GlowstoneDropManipulation;
import de.pfannekuchen.lotas.dropmanipulation.drops.blockdrops.GravelDropManipulation;
import de.pfannekuchen.lotas.dropmanipulation.drops.blockdrops.LeafDropManipulation;
import de.pfannekuchen.lotas.dropmanipulation.drops.blockdrops.OreDropManipulation;
import de.pfannekuchen.lotas.dropmanipulation.drops.blockdrops.PlantsDropManipulation;
import de.pfannekuchen.lotas.dropmanipulation.drops.blockdrops.SealanternDropManipulation;
import de.pfannekuchen.lotas.dropmanipulation.drops.entitydrops.DrownedDropManipulation;
import de.pfannekuchen.lotas.dropmanipulation.drops.entitydrops.FishDropManipulation;
import de.pfannekuchen.lotas.dropmanipulation.drops.entitydrops.MonsterDropManipulation;
import de.pfannekuchen.lotas.dropmanipulation.drops.entitydrops.NetherMobDropManipulation;
import de.pfannekuchen.lotas.dropmanipulation.drops.entitydrops.PassiveDropManipulation;
import de.pfannekuchen.lotas.dropmanipulation.drops.entitydrops.ZombieDropManipulation;
import de.pfannekuchen.lotas.dropmanipulation.drops.rest.BarteringDropManipulation;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class DropManipulationScreen extends Screen {

	public static ArrayList<DropManipulation> manipulations;
	public static int selected = 0;

	static {
		manipulations = new ArrayList<>();
		manipulations.add(new GravelDropManipulation(0, 0, 0, 0));
		manipulations.add(new LeafDropManipulation(0, 0, 0, 0));
		manipulations.add(new PlantsDropManipulation(0, 0, 0, 0));
		manipulations.add(new DeadbushDropManipulation(0, 0, 0, 0));
		manipulations.add(new GlowstoneDropManipulation(0, 0, 0, 0));
		manipulations.add(new SealanternDropManipulation(0, 0, 0, 0));
		manipulations.add(new OreDropManipulation(0, 0, 0, 0));
		manipulations.add(new NetherMobDropManipulation(0, 0, 0, 0));
		manipulations.add(new PassiveDropManipulation(0, 0, 0, 0));
		manipulations.add(new MonsterDropManipulation(0, 0, 0, 0));
		manipulations.add(new ZombieDropManipulation(0, 0, 0, 0));
		manipulations.add(new DrownedDropManipulation(0, 0, 0, 0));
		manipulations.add(new FishDropManipulation(0, 0, 0, 0));
		manipulations.add(new BarteringDropManipulation(0, 0, 0, 0));
	}

	public DropManipulationScreen(PauseScreen gameMenuScreen) {
		super(new TextComponent("Drop Manipulation Screen"));
	}

	@Override
	protected void init() {
		for (DropManipulation man : manipulations) {
			DropManipulation.width = width;
			DropManipulation.height = height;
			DropManipulation.y = 25;
			DropManipulation.x = (int) (width / 3.5f + 24);
			man.update();
		}
		super.init();
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		manipulations.get(selected).keyPressed(keyCode, scanCode, modifiers);
		return super.keyPressed(keyCode, scanCode, modifiers);
	}
	
	@Override
	public boolean charTyped(char chr, int keyCode) {
		manipulations.get(selected).charTyped(chr, keyCode);
		return super.charTyped(chr, keyCode);
	}

	@Override
	public boolean mouseScrolled(double d, double e, double amount) {
		manipulations.get(selected).mouseScrolled(d, e, amount);
		return super.mouseScrolled(d, e, amount);
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		manipulations.get(selected).mouseAction(mouseX, mouseY, button);
		if (mouseX > width / 3.5f || mouseX < 25)
			return false;
		mouseY -= 30;
		mouseY /= 15;
		if (mouseY < 0)
			return false;
		if (manipulations.size() - 1 >= (int) mouseY)
			selected = (int) mouseY;
		return false;
	}

	//#if MC>=11600
//$$ 	@Override public void render(com.mojang.blaze3d.vertex.PoseStack stack, int mouseX, int mouseY, float delta) {
//$$ 		MCVer.stack = stack;
	//#else
	@Override public void render(int mouseX, int mouseY, float delta) {
	//#endif
		MCVer.renderBackground(this);
		Tesselator tessellator = Tesselator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBuilder();
		
		MCVer.disableTexture();
		MCVer.color4f(.5f, .5f, .5f, 0.5F);
		bufferBuilder.begin(7, DefaultVertexFormat.POSITION);
		bufferBuilder.vertex(24, 24, 0).endVertex();
		bufferBuilder.vertex(24, height - 24, 0).endVertex();
		bufferBuilder.vertex(width / 3.5f + 1, height - 24, 0).endVertex();
		bufferBuilder.vertex(width / 3.5f + 1, 24, 0).endVertex();
		tessellator.end();

		MCVer.color4f(0, 0, 0, 0.5F);
		bufferBuilder.begin(7, DefaultVertexFormat.POSITION);
		bufferBuilder.vertex(25, 25, 0).endVertex();
		bufferBuilder.vertex(25, height - 25, 0).endVertex();
		bufferBuilder.vertex(width / 3.5f, height - 25, 0).endVertex();
		bufferBuilder.vertex(width / 3.5f, 25, 0).endVertex();
		tessellator.end();

		int boxY = 30 + selected * 15;
		MCVer.color4f(1f, 1f, 1f, 1F);
		bufferBuilder.begin(7, DefaultVertexFormat.POSITION);
		bufferBuilder.vertex(27, boxY - 4, 0).endVertex();
		bufferBuilder.vertex(27, boxY + 11, 0).endVertex();
		bufferBuilder.vertex(width / 3.5f - 2, boxY + 11, 0).endVertex();
		bufferBuilder.vertex(width / 3.5f - 2, boxY - 4, 0).endVertex();
		tessellator.end();
		MCVer.color4f(0, 0, 0, 0.5F);
		bufferBuilder.begin(7, DefaultVertexFormat.POSITION);
		bufferBuilder.vertex(28, boxY - 3, 0).endVertex();
		bufferBuilder.vertex(28, boxY + 10, 0).endVertex();
		bufferBuilder.vertex(width / 3.5f - 3, boxY + 10, 0).endVertex();
		bufferBuilder.vertex(width / 3.5f - 3, boxY - 3, 0).endVertex();
		tessellator.end();

		MCVer.enableTexture();
		int y = 30;
		for (DropManipulation m : manipulations) {
			MCVer.drawShadow(m.getName(), 32, y, 0xFFFFFF);
			y += 15;
		}
		manipulations.get(selected).render(mouseX, mouseY, delta);
		for(int k = 0; k < this.buttons.size(); ++k) {
			MCVer.render(((AbstractWidget)this.buttons.get(k)), mouseX, mouseY, delta);
		}
	}

	public static abstract class DropManipulation {
		public static int x;
		public static int y;
		public static int width;
		public static int height;
		public Checkbox enabled;

		public abstract String getName();

		public abstract List<ItemStack> redirectDrops(BlockState block);

		public abstract List<ItemStack> redirectDrops(Entity entity);

		public abstract void update();

		public abstract void mouseAction(double mouseX, double mouseY, int button);
		
		public void mouseScrolled(double d, double e, double amount) {}
		
		public void charTyped(char chr, int keyCode) {}
		
		public void keyPressed(int keyCode, int scanCode, int modifiers) {}
		
		public abstract void render(int mouseX, int mouseY, float delta);
	}
}
