package de.pfannekuchen.lotas.dropmanipulation.drops.blockdrops;

import java.util.List;

import com.google.common.collect.ImmutableList;

import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.gui.DropManipulationScreen;
import de.pfannekuchen.lotas.gui.widgets.SmallCheckboxWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class PlantsDropManipulation extends DropManipulationScreen.DropManipulation {

	public static SmallCheckboxWidget optimizeCarrots = new SmallCheckboxWidget(0, 0, I18n.get("dropmanipgui.lotas.blocks.plants.carrot"), false);//"Optimize Carrot Drops"
	public static SmallCheckboxWidget optimizePotato = new SmallCheckboxWidget(0, 0, I18n.get("dropmanipgui.lotas.blocks.plants.potato"), false);//"Optimize Potato Drops"
	public static SmallCheckboxWidget optimizeWheat = new SmallCheckboxWidget(0, 0, I18n.get("dropmanipgui.lotas.blocks.plants.wheat"), false);//"Optimize Wheat Drops"
	public static SmallCheckboxWidget optimizeBeetroot = new SmallCheckboxWidget(0, 0, I18n.get("dropmanipgui.lotas.blocks.plants.beetroot"), false);//"Optimize Beetroot Drops"
	public static SmallCheckboxWidget optimizeMelons = new SmallCheckboxWidget(0, 0, I18n.get("dropmanipgui.lotas.blocks.plants.melon"), false);//"Optimize Melon Drops"
	public static SmallCheckboxWidget optimizeCocoa = new SmallCheckboxWidget(0, 0, I18n.get("dropmanipgui.lotas.blocks.plants.cocoa"), false);//"Optimize Cocoa Bean Drops"
	public static SmallCheckboxWidget optimizeChorus = new SmallCheckboxWidget(0, 0, I18n.get("dropmanipgui.lotas.blocks.plants.chorus"), false);//"Optimize Chorus Plant Drops"
	public static SmallCheckboxWidget optimizeNetherwart = new SmallCheckboxWidget(0, 0, I18n.get("dropmanipgui.lotas.blocks.plants.netherwart"), false);//"Optimize Nether Wart Drops"
	public static SmallCheckboxWidget optimizeMushroom = new SmallCheckboxWidget(0, 0, I18n.get("dropmanipgui.lotas.blocks.plants.mushroom"), false);//"Optimize Mushroom Block Drops"
	public static SmallCheckboxWidget optimizeSweetBerry = new SmallCheckboxWidget(0, 0, I18n.get("dropmanipgui.lotas.blocks.plants.berrybush"), false);//"Optimize Sweet Berry Bush Drops"

	public PlantsDropManipulation(int x, int y, int width, int height) {
		PlantsDropManipulation.x = x;
		PlantsDropManipulation.y = y;
		PlantsDropManipulation.width = width;
		PlantsDropManipulation.height = height;
		enabled = MCVer.Checkbox(x, y, 150, 20, I18n.get("dropmanipgui.lotas.blocks.plants.override"), false);//"Override Plant Drops"
	}

	@Override
	public String getName() {
		return I18n.get("dropmanipgui.lotas.blocks.plants.name");//"Plants"
	}

	@Override
	public List<ItemStack> redirectDrops(BlockState blockstate) {
		Block block = blockstate.getBlock();
		if (block.defaultBlockState().getBlock() == Blocks.CARROTS && optimizeCarrots.isChecked()) {
			if (blockstate.getValue(IntegerProperty.create("age", 0, 7)) == 7)
				return ImmutableList.of(new ItemStack(Items.CARROT, 5));
		} else if (block.defaultBlockState().getBlock() == Blocks.POTATOES && optimizePotato.isChecked()) {
			if (blockstate.getValue(IntegerProperty.create("age", 0, 7)) == 7)
				return ImmutableList.of(new ItemStack(Items.POTATO, 5), new ItemStack(Items.POISONOUS_POTATO, 1));
		} else if (block.defaultBlockState().getBlock() == Blocks.WHEAT && optimizeWheat.isChecked()) {
			if (blockstate.getValue(IntegerProperty.create("age", 0, 7)) == 7)
				return ImmutableList.of(new ItemStack(Items.WHEAT_SEEDS, 3), new ItemStack(Items.WHEAT, 1));
		} else if (block.defaultBlockState().getBlock() == Blocks.BEETROOTS && optimizeBeetroot.isChecked()) {
			if (blockstate.getValue(IntegerProperty.create("age", 0, 3)) == 3)
				return ImmutableList.of(new ItemStack(Items.BEETROOT, 1), new ItemStack(Items.BEETROOT_SEEDS, 4));
		} else if (block.defaultBlockState().getBlock() == Blocks.MELON && optimizeMelons.isChecked()) {
			return ImmutableList.of(new ItemStack(Items.MELON_SLICE, 7));
		} else if (block.defaultBlockState().getBlock() == Blocks.COCOA && optimizeCocoa.isChecked()) {
			if (blockstate.getValue(IntegerProperty.create("age", 0, 2)) == 2)
				return ImmutableList.of(new ItemStack(Items.COCOA_BEANS, 3));
		} else if (block.defaultBlockState().getBlock() == Blocks.CHORUS_PLANT && optimizeChorus.isChecked()) {
			return ImmutableList.of(new ItemStack(Items.CHORUS_FRUIT, 1));
		} else if (block.defaultBlockState().getBlock() == Blocks.BROWN_MUSHROOM_BLOCK && optimizeMushroom.isChecked()) {
			return ImmutableList.of(new ItemStack(Items.BROWN_MUSHROOM, 2));
		} else if (block.defaultBlockState().getBlock() == Blocks.RED_MUSHROOM_BLOCK && optimizeMushroom.isChecked()) {
			return ImmutableList.of(new ItemStack(Items.RED_MUSHROOM, 2));
		} else if (block.defaultBlockState().getBlock() == Blocks.NETHER_WART && optimizeNetherwart.isChecked()) {
			if (blockstate.getValue(IntegerProperty.create("age", 0, 3)) == 3)
				return ImmutableList.of(new ItemStack(Items.NETHER_WART, 4));
		} else if (block.defaultBlockState().getBlock() == Blocks.SWEET_BERRY_BUSH && optimizeSweetBerry.isChecked()) {
			if (blockstate.getValue(IntegerProperty.create("age", 0, 3)) == 3)
				return ImmutableList.of(new ItemStack(Items.SWEET_BERRIES, 3));
			else if (blockstate.getValue(IntegerProperty.create("age", 0, 3)) == 2)
				return ImmutableList.of(new ItemStack(Items.SWEET_BERRIES, 2));
		}
		return ImmutableList.of();
	}

	@Override
	public List<ItemStack> redirectDrops(Entity entity, int lootingBonus) {
		return ImmutableList.of();
	}

	@Override
	public void update() {
		//#if MC>=11903
//$$ 		enabled.setPosition(x, y);
//$$
//$$ 		optimizeCarrots.setPosition(x, 64);
//$$ 		optimizeBeetroot.setPosition(x, 80);
//$$ 		optimizeMelons.setPosition(x, 96);
//$$ 		optimizeWheat.setPosition(x, 112);
//$$ 		optimizePotato.setPosition(x, 128);
//$$ 		optimizeCocoa.setPosition(x, 144);
//$$ 		optimizeChorus.setPosition(x, 160);
//$$ 		optimizeNetherwart.setPosition(x, 176);
//$$ 		optimizeSweetBerry.setPosition(x, 192);
//$$ 		optimizeMushroom.setPosition(x, 208);
		//#else
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
		//#endif
	}

	@Override
	public void mouseAction(double mouseX, double mouseY, int button) {
		enabled.mouseClicked(mouseX, mouseY, button);
		if (enabled.selected()) {
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
	public void render(int mouseX, int mouseY, float delta) {
		MCVer.render(enabled, mouseX, mouseY, delta);

		if (!enabled.selected()) {
			MCVer.color4f(.5f, .5f, .5f, .4f);
		} else {
			MCVer.render(optimizeCarrots, mouseX, mouseY, delta);
			MCVer.render(optimizeBeetroot, mouseX, mouseY, delta);
			MCVer.render(optimizeMelons, mouseX, mouseY, delta);
			MCVer.render(optimizePotato, mouseX, mouseY, delta);
			MCVer.render(optimizeWheat, mouseX, mouseY, delta);
			MCVer.render(optimizeCocoa, mouseX, mouseY, delta);
			MCVer.render(optimizeChorus, mouseX, mouseY, delta);
			MCVer.render(optimizeSweetBerry, mouseX, mouseY, delta);
			MCVer.render(optimizeMushroom, mouseX, mouseY, delta);
			MCVer.render(optimizeNetherwart, mouseX, mouseY, delta);
		}

		MCVer.bind(Minecraft.getInstance().getTextureManager(), new ResourceLocation("lotas", "drops/plants.png"));
		MCVer.blit(width - 128, y + 24, 0.0F, 0.0F, 96, 96, 96, 96);
	}

}
