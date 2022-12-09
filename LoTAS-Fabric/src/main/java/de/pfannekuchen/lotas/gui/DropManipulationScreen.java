package de.pfannekuchen.lotas.gui;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.platform.GlStateManager.DestFactor;
import com.mojang.blaze3d.platform.GlStateManager.SourceFactor;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Draws a List of Drop Manipulations
 * @author Pancake
 */
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
		//#if MC>=11600
//$$ 		manipulations.add(new BarteringDropManipulation(0, 0, 0, 0));
		//#endif
	}

	public DropManipulationScreen(PauseScreen gameMenuScreen) {
		super(MCVer.literal("Drop Manipulation Screen"));
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

	//#if MC>=11601
//$$ 	@Override public void render(com.mojang.blaze3d.vertex.PoseStack stack, int mouseX, int mouseY, float delta) {
//$$ 		MCVer.stack = stack;
	//#else
	@Override public void render(int mouseX, int mouseY, float delta) {
	//#endif
		MCVer.renderBackground(this);
		Tesselator tessellator = Tesselator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBuilder();
		
		MCVer.disableTexture();
		
		//#if MC>=11700
//$$ 		MCVer.color4f(1f, 1f, 1f, 0.5F);
//$$ 		MCVer.enableBlend();
//$$ 		MCVer.enableDepthTest();
//$$ 		MCVer.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
//$$
		//#if MC>=11903
//$$ 		org.joml.Matrix4f matrix = MCVer.stack.last().pose();
		//#else
//$$ 		com.mojang.math.Matrix4f matrix = MCVer.stack.last().pose();
		//#endif
//$$ 		com.mojang.blaze3d.systems.RenderSystem.setShader(net.minecraft.client.renderer.GameRenderer::getPositionShader);
//$$ 		bufferBuilder.begin(com.mojang.blaze3d.vertex.VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);
//$$ 		bufferBuilder.vertex(matrix, 24, 24, 0).endVertex();
//$$ 		bufferBuilder.vertex(matrix, 24, height - 24, 0).endVertex();
//$$ 		bufferBuilder.vertex(matrix, width / 3.5f + 1, height - 24, 0).endVertex();
//$$ 		bufferBuilder.vertex(matrix, width / 3.5f + 1, 24, 0).endVertex();
		//#if MC>=11900
//$$ 		BufferUploader.drawWithShader(bufferBuilder.end());
		//#else
//$$ 		bufferBuilder.end();
//$$ 		BufferUploader.end(bufferBuilder);
		//#endif
		//#else
		MCVer.color4f(.5f, .5f, .5f, 0.5F);
		bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormat.POSITION);

		bufferBuilder.vertex(24, 24, 0).endVertex();
		bufferBuilder.vertex(24, height - 24, 0).endVertex();
		bufferBuilder.vertex(width / 3.5f + 1, height - 24, 0).endVertex();
		bufferBuilder.vertex(width / 3.5f + 1, 24, 0).endVertex();
		tessellator.end();

		//#endif
		
		//#if MC>=11700
//$$ 		MCVer.color4f(0, 0, 0, 1f);
//$$ 		com.mojang.blaze3d.systems.RenderSystem.setShader(net.minecraft.client.renderer.GameRenderer::getPositionShader);
//$$ 		bufferBuilder.begin(com.mojang.blaze3d.vertex.VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);
//$$ 		bufferBuilder.vertex(matrix, 25, 25, 0).endVertex();
//$$ 		bufferBuilder.vertex(matrix, 25, height - 25, 0).endVertex();
//$$ 		bufferBuilder.vertex(matrix, width / 3.5f, height - 25, 0).endVertex();
//$$ 		bufferBuilder.vertex(matrix, width / 3.5f, 25, 0).endVertex();
		//#if MC>=11900
//$$ 		BufferUploader.drawWithShader(bufferBuilder.end());
		//#else
//$$ 		bufferBuilder.end();
//$$ 		BufferUploader.end(bufferBuilder);
		//#endif
		//#else
		MCVer.color4f(0, 0, 0, 0.5F);
		bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormat.POSITION);
		bufferBuilder.vertex(25, 25, 0).endVertex();
		bufferBuilder.vertex(25, height - 25, 0).endVertex();
		bufferBuilder.vertex(width / 3.5f, height - 25, 0).endVertex();
		bufferBuilder.vertex(width / 3.5f, 25, 0).endVertex();
		tessellator.end();
		//#endif

		int boxY = 30 + selected * 15;
		
		//#if MC>=11700
//$$ 		MCVer.color4f(1f, 1f, 1f, 1f);
//$$ 		bufferBuilder.begin(com.mojang.blaze3d.vertex.VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);
//$$ 		bufferBuilder.vertex(matrix, 27, boxY - 4, 0).endVertex();
//$$ 		bufferBuilder.vertex(matrix, 27, boxY + 11, 0).endVertex();
//$$ 		bufferBuilder.vertex(matrix, width / 3.5f - 2, boxY + 11, 0).endVertex();
//$$ 		bufferBuilder.vertex(matrix, width / 3.5f - 2, boxY - 4, 0).endVertex();
		//#if MC>=11900
//$$ 		BufferUploader.drawWithShader(bufferBuilder.end());
		//#else
//$$ 		bufferBuilder.end();
//$$ 		BufferUploader.end(bufferBuilder);
		//#endif
		//#else
		MCVer.color4f(1f, 1f, 1f, 1f);
		bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormat.POSITION);

		bufferBuilder.vertex(27, boxY - 4, 0).endVertex();
		bufferBuilder.vertex(27, boxY + 11, 0).endVertex();
		bufferBuilder.vertex(width / 3.5f - 2, boxY + 11, 0).endVertex();
		bufferBuilder.vertex(width / 3.5f - 2, boxY - 4, 0).endVertex();
		tessellator.end();
		//#endif
		
		//#if MC>=11700
//$$ 		MCVer.color4f(0, 0, 0, 1F);
//$$ 		bufferBuilder.begin(com.mojang.blaze3d.vertex.VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);
//$$ 		bufferBuilder.vertex(matrix, 28, boxY - 3, 0).endVertex();
//$$ 		bufferBuilder.vertex(matrix, 28, boxY + 10, 0).endVertex();
//$$ 		bufferBuilder.vertex(matrix, width / 3.5f - 3, boxY + 10, 0).endVertex();
//$$ 		bufferBuilder.vertex(matrix, width / 3.5f - 3, boxY - 3, 0).endVertex();
		//#if MC>=11900
//$$ 		BufferUploader.drawWithShader(bufferBuilder.end());
		//#else
//$$ 		bufferBuilder.end();
//$$ 		BufferUploader.end(bufferBuilder);
		//#endif
//$$ 		MCVer.disableBlend();
//$$ 		MCVer.enableTexture();
//$$ 		MCVer.disableDepthTest();
		//#else
		MCVer.color4f(0, 0, 0, 0.5F);
		bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormat.POSITION);
		bufferBuilder.vertex(28, boxY - 3, 0).endVertex();
		bufferBuilder.vertex(28, boxY + 10, 0).endVertex();
		bufferBuilder.vertex(width / 3.5f - 3, boxY + 10, 0).endVertex();
		bufferBuilder.vertex(width / 3.5f - 3, boxY - 3, 0).endVertex();
		tessellator.end();
		//#endif
		
		MCVer.enableTexture();
		int y = 30;
		for (DropManipulation m : manipulations) {
			MCVer.drawShadow(m.getName(), 32, y, 0xFFFFFF);
			y += 15;
		}
		manipulations.get(selected).render(mouseX, mouseY, delta);
		for(int k = 0; k < MCVer.getButtonSize(this); ++k) {
			MCVer.render(((AbstractWidget)MCVer.getButton(this, k)), mouseX, mouseY, delta);
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

		public abstract List<ItemStack> redirectDrops(Entity entity, int lootingBonus);

		public abstract void update();

		public abstract void mouseAction(double mouseX, double mouseY, int button);
		
		public void mouseScrolled(double d, double e, double amount) {}
		
		public void charTyped(char chr, int keyCode) {}
		
		public void keyPressed(int keyCode, int scanCode, int modifiers) {}
		
		public abstract void render(int mouseX, int mouseY, float delta);
	}
}
