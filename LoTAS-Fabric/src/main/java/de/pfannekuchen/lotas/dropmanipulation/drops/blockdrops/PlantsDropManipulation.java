package de.pfannekuchen.lotas.dropmanipulation.drops.blockdrops;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.platform.GlStateManager;

import de.pfannekuchen.lotas.gui.DropManipulationScreen;
import de.pfannekuchen.lotas.gui.widgets.SmallCheckboxWidget;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.widget.CheckboxWidget;
//#if MC>=11601
//$$ import net.minecraft.client.util.math.MatrixStack;
//#endif
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.property.IntProperty;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;

public class PlantsDropManipulation extends DropManipulationScreen.DropManipulation {

	public static SmallCheckboxWidget optimizeCarrots = new SmallCheckboxWidget(0, 0, "Optimize Carrot Drops", false);
	public static SmallCheckboxWidget optimizePotato = new SmallCheckboxWidget(0, 0, "Optimize Potato Drops", false);
	public static SmallCheckboxWidget optimizeWheat = new SmallCheckboxWidget(0, 0, "Optimize Wheat Drops", false);
	public static SmallCheckboxWidget optimizeBeetroot = new SmallCheckboxWidget(0, 0, "Optimize Beetroot Drops", false);
	public static SmallCheckboxWidget optimizeMelons = new SmallCheckboxWidget(0, 0, "Optimize Melon Drops", false);
	public static SmallCheckboxWidget optimizeCocoa = new SmallCheckboxWidget(0, 0, "Optimize Cocoa Bean Drops", false);
	public static SmallCheckboxWidget optimizeChorus = new SmallCheckboxWidget(0, 0, "Optimize Chorus Plant Drops", false);
	public static SmallCheckboxWidget optimizeNetherwart = new SmallCheckboxWidget(0, 0, "Optimize Nether Wart Drops", false);
	public static SmallCheckboxWidget optimizeMushroom = new SmallCheckboxWidget(0, 0, "Optimize Mushroom Block Drops", false);
	public static SmallCheckboxWidget optimizeSweetBerry = new SmallCheckboxWidget(0, 0, "Optimize Sweet Berry Bush Drops", false);

	public PlantsDropManipulation(int x, int y, int width, int height) {
		PlantsDropManipulation.x = x;
		PlantsDropManipulation.y = y;
		PlantsDropManipulation.width = width;
		PlantsDropManipulation.height = height;
		//#if MC>=11601
//$$ 		enabled = new CheckboxWidget(x, y, 150, 20, new LiteralText("Override Plant Drops"), false);
		//#else
		enabled = new CheckboxWidget(x, y, 150, 20, "Override Plant Drops", false);
		//#endif

	}

	@Override
	public String getName() {
		return "Plants";
	}

	@Override
	public List<ItemStack> redirectDrops(BlockState blockstate) {
		Block block = blockstate.getBlock();
		if (block.getDefaultState().getBlock() == Blocks.CARROTS && optimizeCarrots.isChecked()) {
			if (blockstate.get(IntProperty.of("age", 0, 7)) == 7)
				return ImmutableList.of(new ItemStack(Items.CARROT, 5));
		} else if (block.getDefaultState().getBlock() == Blocks.POTATOES && optimizePotato.isChecked()) {
			if (blockstate.get(IntProperty.of("age", 0, 7)) == 7)
				return ImmutableList.of(new ItemStack(Items.POTATO, 5), new ItemStack(Items.POISONOUS_POTATO, 1));
		} else if (block.getDefaultState().getBlock() == Blocks.WHEAT && optimizeWheat.isChecked()) {
			if (blockstate.get(IntProperty.of("age", 0, 7)) == 7)
				return ImmutableList.of(new ItemStack(Items.WHEAT_SEEDS, 3), new ItemStack(Items.WHEAT, 1));
		} else if (block.getDefaultState().getBlock() == Blocks.BEETROOTS && optimizeBeetroot.isChecked()) {
			if (blockstate.get(IntProperty.of("age", 0, 3)) == 3)
				return ImmutableList.of(new ItemStack(Items.BEETROOT, 1), new ItemStack(Items.BEETROOT_SEEDS, 4));
		} else if (block.getDefaultState().getBlock() == Blocks.MELON && optimizeMelons.isChecked()) {
			return ImmutableList.of(new ItemStack(Items.MELON_SLICE, 7));
		} else if (block.getDefaultState().getBlock() == Blocks.COCOA && optimizeCocoa.isChecked()) {
			if (blockstate.get(IntProperty.of("age", 0, 2)) == 2)
				return ImmutableList.of(new ItemStack(Items.COCOA_BEANS, 3));
		} else if (block.getDefaultState().getBlock() == Blocks.CHORUS_PLANT && optimizeChorus.isChecked()) {
			return ImmutableList.of(new ItemStack(Items.CHORUS_FRUIT, 1));
		} else if (block.getDefaultState().getBlock() == Blocks.BROWN_MUSHROOM_BLOCK && optimizeMushroom.isChecked()) {
			return ImmutableList.of(new ItemStack(Items.BROWN_MUSHROOM, 2));
		} else if (block.getDefaultState().getBlock() == Blocks.RED_MUSHROOM_BLOCK && optimizeMushroom.isChecked()) {
			return ImmutableList.of(new ItemStack(Items.RED_MUSHROOM, 2));
		} else if (block.getDefaultState().getBlock() == Blocks.NETHER_WART && optimizeNetherwart.isChecked()) {
			if (blockstate.get(IntProperty.of("age", 0, 3)) == 3)
				return ImmutableList.of(new ItemStack(Items.NETHER_WART, 4));
		} else if (block.getDefaultState().getBlock() == Blocks.SWEET_BERRY_BUSH && optimizeSweetBerry.isChecked()) {
			if (blockstate.get(IntProperty.of("age", 0, 3)) == 3)
				return ImmutableList.of(new ItemStack(Items.SWEET_BERRIES, 3));
			else if (blockstate.get(IntProperty.of("age", 0, 3)) == 2)
				return ImmutableList.of(new ItemStack(Items.SWEET_BERRIES, 2));
		}
		return ImmutableList.of();
	}

	@Override
	public List<ItemStack> redirectDrops(Entity entity) {
		return ImmutableList.of();
	}

	@Override
	public void update() {
		enabled.x = x;
		enabled.y = y;
		optimizeCarrots.y = 64;
		optimizeBeetroot.y = 80;
		optimizeMelons.y = 96;
		optimizeWheat.y = 112;
		optimizePotato.y = 128;
		optimizeCocoa.y = 144;
		optimizeChorus.y = 160;
		optimizeNetherwart.y = 176;
		optimizeSweetBerry.y = 192;
		optimizeMushroom.y = 208;
		optimizeCarrots.x = x;
		optimizeBeetroot.x = x;
		optimizeMelons.x = x;
		optimizeWheat.x = x;
		optimizePotato.x = x;
		optimizeCocoa.x = x;
		optimizeChorus.x = x;
		optimizeNetherwart.x = x;
		optimizeSweetBerry.x = x;
		optimizeMushroom.x = x;
	}

	@Override
	public void mouseAction(double mouseX, double mouseY, int button) {
		enabled.mouseClicked(mouseX, mouseY, button);
		if (enabled.isChecked()) {
			optimizeCarrots.mouseClicked(mouseX, mouseY, button);
			optimizeBeetroot.mouseClicked(mouseX, mouseY, button);
			optimizeMelons.mouseClicked(mouseX, mouseY, button);
			optimizePotato.mouseClicked(mouseX, mouseY, button);
			optimizeWheat.mouseClicked(mouseX, mouseY, button);
			optimizeCocoa.mouseClicked(mouseX, mouseY, button);
			optimizeChorus.mouseClicked(mouseX, mouseY, button);
			optimizeMushroom.mouseClicked(mouseX, mouseY, button);
			optimizeNetherwart.mouseClicked(mouseX, mouseY, button);
			optimizeSweetBerry.mouseClicked(mouseX, mouseY, button);
		}
	}

	@Override
	public void render(Object matrices, int mouseX, int mouseY, float delta) {
		//#if MC>=11601
//$$ 		enabled.render((MatrixStack) matrices, mouseX, mouseY, delta);
		//#else
		enabled.render(mouseX, mouseY, delta);
		//#endif

		if (!enabled.isChecked()) {
			//#if MC>=11700
//$$ 			com.mojang.blaze3d.systems.RenderSystem.setShaderColor(.5f, .5f, .5f, .4f);
			//#else
			GlStateManager.color4f(.5f, .5f, .5f, .4f);
			//#endif
		} else {
			//#if MC>=11601
//$$ 			optimizeCarrots.render((MatrixStack) matrices, mouseX, mouseY, delta);
//$$ 			optimizeBeetroot.render((MatrixStack) matrices, mouseX, mouseY, delta);
//$$ 			optimizeMelons.render((MatrixStack) matrices, mouseX, mouseY, delta);
//$$ 			optimizePotato.render((MatrixStack) matrices, mouseX, mouseY, delta);
//$$ 			optimizeWheat.render((MatrixStack) matrices, mouseX, mouseY, delta);
//$$ 			optimizeCocoa.render((MatrixStack) matrices, mouseX, mouseY, delta);
//$$ 			optimizeChorus.render((MatrixStack) matrices, mouseX, mouseY, delta);
//$$ 			optimizeSweetBerry.render((MatrixStack) matrices, mouseX, mouseY, delta);
//$$ 			optimizeMushroom.render((MatrixStack) matrices, mouseX, mouseY, delta);
//$$ 			optimizeNetherwart.render((MatrixStack) matrices, mouseX, mouseY, delta);
			//#else
			optimizeCarrots.render(mouseX, mouseY, delta);
			optimizeBeetroot.render(mouseX, mouseY, delta);
			optimizeMelons.render(mouseX, mouseY, delta);
			optimizePotato.render(mouseX, mouseY, delta);
			optimizeWheat.render(mouseX, mouseY, delta);
			optimizeCocoa.render(mouseX, mouseY, delta);
			optimizeChorus.render(mouseX, mouseY, delta);
			optimizeSweetBerry.render(mouseX, mouseY, delta);
			optimizeMushroom.render(mouseX, mouseY, delta);
			optimizeNetherwart.render(mouseX, mouseY, delta);
			//#endif
		}

		MinecraftClient.getInstance().getTextureManager().bindTexture(new Identifier("lotas", "drops/plants.png"));
		//#if MC>=11601
//$$ 		DrawableHelper.drawTexture((MatrixStack) matrices, width - 128, y + 24, 0.0F, 0.0F, 96, 96, 96, 96);
		//#else
		DrawableHelper.blit(width - 128, y + 24, 0.0F, 0.0F, 96, 96, 96, 96);
		//#endif

	}

}
