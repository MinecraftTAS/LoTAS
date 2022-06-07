package de.pfannekuchen.lotas.dropmanipulation.drops.blockdrops;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;

import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.gui.DropManipulationScreen;
import de.pfannekuchen.lotas.gui.widgets.ImageButton;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class LeafDropManipulation extends DropManipulationScreen.DropManipulation {

	public static ImageButton dropApple = new ImageButton(x, y, c -> {
		LeafDropManipulation.dropApple.setToggled(!LeafDropManipulation.dropApple.isToggled());
	}, new ResourceLocation("lotas", "drops/apple.png"));
	public static ImageButton dropStick = new ImageButton(x, y, c -> {
		LeafDropManipulation.dropStick.setToggled(!LeafDropManipulation.dropStick.isToggled());
	}, new ResourceLocation("lotas", "drops/stick.png"));
	public static ImageButton dropSapling = new ImageButton(x, y, c -> {
		LeafDropManipulation.dropSapling.setToggled(!LeafDropManipulation.dropSapling.isToggled());
	}, new ResourceLocation("lotas", "drops/sapling.png"));

	public LeafDropManipulation(int x, int y, int width, int height) {
		LeafDropManipulation.x = x;
		LeafDropManipulation.y = y;
		LeafDropManipulation.width = width;
		LeafDropManipulation.height = height;
		enabled = MCVer.Checkbox(x, y, 150, 20, "Override Leaf Drops", false);
	}

	@Override
	public String getName() {
		return "Leaves";
	}

	@Override
	public List<ItemStack> redirectDrops(BlockState block) {
		List<ItemStack> list = new ArrayList<>();
		if (Blocks.OAK_LEAVES.equals(block.getBlock().defaultBlockState().getBlock())) {
			if (dropSapling.isToggled())
				list.add(new ItemStack(Items.OAK_SAPLING));
		} else if (Blocks.BIRCH_LEAVES.equals(block.getBlock().defaultBlockState().getBlock())) {
			if (dropSapling.isToggled())
				list.add(new ItemStack(Items.BIRCH_SAPLING));
		} else if (Blocks.SPRUCE_LEAVES.equals(block.getBlock().defaultBlockState().getBlock())) {
			if (dropSapling.isToggled())
				list.add(new ItemStack(Items.SPRUCE_SAPLING));
		} else if (Blocks.JUNGLE_LEAVES.equals(block.getBlock().defaultBlockState().getBlock())) {
			if (dropSapling.isToggled())
				list.add(new ItemStack(Items.JUNGLE_SAPLING));
		} else if (Blocks.DARK_OAK_LEAVES.equals(block.getBlock().defaultBlockState().getBlock())) {
			if (dropSapling.isToggled())
				list.add(new ItemStack(Items.DARK_OAK_SAPLING));
		} else if (Blocks.ACACIA_LEAVES.equals(block.getBlock().defaultBlockState().getBlock())) {
			if (dropSapling.isToggled())
				list.add(new ItemStack(Items.ACACIA_SAPLING));
		} else {
			return ImmutableList.of();
		}
		if (dropApple.isToggled() && (Blocks.OAK_LEAVES.equals(block.getBlock().defaultBlockState().getBlock()) || Blocks.DARK_OAK_LEAVES.equals(block.getBlock().defaultBlockState().getBlock())))
			list.add(new ItemStack(Items.APPLE));
		if (dropStick.isToggled())
			list.add(new ItemStack(Items.STICK, 2));
		return list;
	}

	@Override
	public List<ItemStack> redirectDrops(Entity entity, int lootingBonus) {
		return ImmutableList.of();
	}

	@Override
	public void update() {
		enabled.x = x;
		enabled.y = y;
		dropApple.x = x;
		dropApple.y = y + 96;
		dropStick.x = x + 22;
		dropStick.y = y + 96;
		dropSapling.x = x + 44;
		dropSapling.y = y + 96;
	}

	@Override
	public void mouseAction(double mouseX, double mouseY, int button) {
		enabled.mouseClicked(mouseX, mouseY, button);
		if (enabled.selected()) {
			dropApple.mouseClicked(mouseX, mouseY, button);
			dropStick.mouseClicked(mouseX, mouseY, button);
			dropSapling.mouseClicked(mouseX, mouseY, button);
		}
	}

	@Override
	public void render(int mouseX, int mouseY, float delta) {
		MCVer.render(enabled, mouseX, mouseY, delta);

		if (!enabled.selected()) {
			MCVer.color4f(.5f, .5f, .5f, .4f);
		} else {
			MCVer.drawShadow("Leaves drop:" + (dropApple.isToggled() ? " 1 Apple" : "") + (dropStick.isToggled() ? " 2 Sticks" : "") + (dropSapling.isToggled() ? " 1 Sapling" : ""), x, y + 64, 0xFFFFFF);
			MCVer.render(dropApple, mouseX, mouseY, delta);
			MCVer.render(dropStick, mouseX, mouseY, delta);
			MCVer.render(dropSapling, mouseX, mouseY, delta);
		}

		MCVer.bind(Minecraft.getInstance().getTextureManager(), new ResourceLocation("lotas", "drops/leaf.png"));
		MCVer.blit(width - 128, y + 24, 0.0F, 0.0F, 96, 96, 96, 96);
	}

}
