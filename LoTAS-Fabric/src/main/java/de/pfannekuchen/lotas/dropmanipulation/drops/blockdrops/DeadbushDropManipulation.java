package de.pfannekuchen.lotas.dropmanipulation.drops.blockdrops;

import java.util.List;

import com.google.common.collect.ImmutableList;

import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.gui.DropManipulationScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class DeadbushDropManipulation extends DropManipulationScreen.DropManipulation {

	public static int sticks = 0;

	public static Button drop0Stick = MCVer.Button(x, y, 98, 20, "0 Sticks", button -> {
		press0Stick();
	});
	public static Button drop1Stick = MCVer.Button(x, y, 98, 20, "1 Sticks", button -> {
		press1Stick();
	});
	public static Button drop2Stick = MCVer.Button(x, y, 98, 20, "2 Sticks", button -> {
		press2Stick();
	});

	public static void press0Stick() {
		drop0Stick.active = false;
		drop1Stick.active = true;
		drop2Stick.active = true;
		sticks = 0;
	}

	public static void press1Stick() {
		drop0Stick.active = true;
		drop1Stick.active = false;
		drop2Stick.active = true;
		sticks = 1;
	}

	public static void press2Stick() {
		drop0Stick.active = true;
		drop1Stick.active = true;
		drop2Stick.active = false;
		sticks = 2;
	}

	public DeadbushDropManipulation(int x, int y, int width, int height) {
		DeadbushDropManipulation.x = x;
		DeadbushDropManipulation.y = y;
		DeadbushDropManipulation.width = width;
		DeadbushDropManipulation.height = height;
		enabled = MCVer.Checkbox(x, y, 150, 20, "Override Dead Bush Drops", false);
		drop0Stick.active = false;
	}

	@Override
	public String getName() {
		return "Dead Bush";
	}

	@Override
	public List<ItemStack> redirectDrops(BlockState block) {
		if (block.getBlock().defaultBlockState().getBlock() != Blocks.DEAD_BUSH)
			return ImmutableList.of();
		return ImmutableList.of(new ItemStack(Items.STICK, sticks));
	}

	@Override
	public List<ItemStack> redirectDrops(Entity entity) {
		return ImmutableList.of();
	}

	@Override
	public void update() {
		enabled.x = x;
		enabled.y = y;

		drop0Stick.x = x;
		drop0Stick.y = y + 96;
		drop1Stick.x = x;
		drop1Stick.y = y + 120;
		drop2Stick.x = x;
		drop2Stick.y = y + 144;

		drop0Stick.setWidth(width - x - 128 - 16);
		drop1Stick.setWidth(width - x - 128 - 16);
		drop2Stick.setWidth(width - x - 128 - 16);
	}

	@Override
	public void mouseAction(double mouseX, double mouseY, int button) {
		enabled.mouseClicked(mouseX, mouseY, button);
		if (enabled.selected()) {
			drop0Stick.mouseClicked(mouseX, mouseY, button);
			drop1Stick.mouseClicked(mouseX, mouseY, button);
			drop2Stick.mouseClicked(mouseX, mouseY, button);
		}
	}

	@Override
	public void render(int mouseX, int mouseY, float delta) {
		MCVer.render(enabled, mouseX, mouseY, delta);

		if (!enabled.selected()) {
			MCVer.color4f(.5f, .5f, .5f, .4f);
		} else {
			MCVer.render(drop0Stick, mouseX, mouseY, delta);
			MCVer.render(drop1Stick, mouseX, mouseY, delta);
			MCVer.render(drop2Stick, mouseX, mouseY, delta);
			MCVer.drawShadow("Drop " + sticks + " Sticks when breaking Dead Bush", x, y + 64, 0xFFFFFF);
		}

		Minecraft.getInstance().getTextureManager().bind(new ResourceLocation("lotas", "drops/deadbush.png"));
		MCVer.blit(width - 128, y + 24, 0.0F, 0.0F, 96, 96, 96, 96);

	}

}
