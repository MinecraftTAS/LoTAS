package de.pfannekuchen.lotas.dropmanipulation.drops.blockdrops;

import java.util.List;

import com.google.common.collect.ImmutableList;

import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.gui.DropManipulationScreen;
import de.pfannekuchen.lotas.gui.widgets.SmallCheckboxWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class OreDropManipulation extends DropManipulationScreen.DropManipulation {

	public static SmallCheckboxWidget optimizeLapis = new SmallCheckboxWidget(0, 0, "Full Lapis Drops", false);
	public static SmallCheckboxWidget optimizeRedstone = new SmallCheckboxWidget(0, 0, "Full Redstone Drops", false);

	public OreDropManipulation(int x, int y, int width, int height) {
		OreDropManipulation.x = x;
		OreDropManipulation.y = y;
		OreDropManipulation.width = width;
		OreDropManipulation.height = height;
		enabled = MCVer.Checkbox(x, y, 150, 20, "Override Ore Drops", false);
	}

	@Override
	public String getName() {
		return "Ores";
	}

	@Override
	public List<ItemStack> redirectDrops(BlockState blockstate) {
		Block block = blockstate.getBlock();
		if (block.defaultBlockState().getBlock() == Blocks.LAPIS_ORE && optimizeLapis.isChecked()) {
			return ImmutableList.of(new ItemStack(Items.LAPIS_LAZULI, 9));
		} else if (block.defaultBlockState().getBlock() == Blocks.REDSTONE_ORE && optimizeRedstone.isChecked()) {
			return ImmutableList.of(new ItemStack(Items.REDSTONE, 5));
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
		optimizeRedstone.y = 64;
		optimizeLapis.y = 80;
		optimizeRedstone.x = x;
		optimizeLapis.x = x;
	}

	@Override
	public void mouseAction(double mouseX, double mouseY, int button) {
		enabled.mouseClicked(mouseX, mouseY, button);
		if (enabled.selected()) {
			optimizeRedstone.mouseClicked(mouseX, mouseY, button);
			optimizeLapis.mouseClicked(mouseX, mouseY, button);
		}
	}

	@Override
	public void render(int mouseX, int mouseY, float delta) {
		MCVer.render(enabled, mouseX, mouseY, delta);

		if (!enabled.selected()) {
			MCVer.color4f(.5f, .5f, .5f, .4f);
		} else {
			MCVer.render(optimizeRedstone, mouseX, mouseY, delta);
			MCVer.render(optimizeLapis, mouseX, mouseY, delta);
		}

		MCVer.bind(Minecraft.getInstance().getTextureManager(), new ResourceLocation("lotas", "drops/diamond_ore.png"));
		MCVer.blit(width - 128, y + 24, 0.0F, 0.0F, 96, 96, 96, 96);
	}

}
