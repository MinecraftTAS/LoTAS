package de.pfannekuchen.lotas.dropmanipulation.drops.blockdrops;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.platform.GlStateManager;

import de.pfannekuchen.lotas.gui.DropManipulationScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class SealanternDropManipulation extends DropManipulationScreen.DropManipulation {

	public static int pris = 2;

	public static Button drop2Pris = new Button(x, y, 98, 20, "2 Prismarine Crystals", button -> {
		press2pris();
	});
	public static Button drop3Pris = new Button(x, y, 98, 20, "3 Prismarine Crystals", button -> {
		press3pris();
	});

	public static void press2pris() {
		drop2Pris.active = false;
		drop3Pris.active = true;
		pris = 2;
	}

	public static void press3pris() {
		drop2Pris.active = true;
		drop3Pris.active = false;
		pris = 3;
	}

	public SealanternDropManipulation(int x, int y, int width, int height) {
		SealanternDropManipulation.x = x;
		SealanternDropManipulation.y = y;
		SealanternDropManipulation.width = width;
		SealanternDropManipulation.height = height;
		enabled = new Checkbox(x, y, 150, 20, "Override Sea Lantern Drops", false);
		drop2Pris.active = false;
	}

	@Override
	public String getName() {
		return "Sea Lantern";
	}

	@Override
	public List<ItemStack> redirectDrops(BlockState block) {
		if (block.getBlock().defaultBlockState().getBlock() != Blocks.SEA_LANTERN)
			return ImmutableList.of();
		return ImmutableList.of(new ItemStack(Items.PRISMARINE_CRYSTALS, pris));
	}

	@Override
	public List<ItemStack> redirectDrops(Entity entity) {
		return ImmutableList.of();
	}

	@Override
	public void update() {
		enabled.x = x;
		enabled.y = y;

		drop2Pris.x = x;
		drop2Pris.y = y + 96;
		drop3Pris.x = x;
		drop3Pris.y = y + 120;

		drop2Pris.setWidth(width - x - 128 - 16);
		drop3Pris.setWidth(width - x - 128 - 16);

	}

	@Override
	public void mouseAction(double mouseX, double mouseY, int button) {
		enabled.mouseClicked(mouseX, mouseY, button);
		if (enabled.selected()) {
			drop2Pris.mouseClicked(mouseX, mouseY, button);
			drop3Pris.mouseClicked(mouseX, mouseY, button);
		}
	}

	@Override
	public void render(int mouseX, int mouseY, float delta) {
		enabled.render(mouseX, mouseY, delta);

		if (!enabled.selected()) {
			GlStateManager.color4f(.5f, .5f, .5f, .4f);
		} else {
			Minecraft.getInstance().font.drawShadow("Drop " + pris + " Prismarine Crystals when breaking Sea Lanterns", x, y + 64, 0xFFFFFF);
			drop2Pris.render(mouseX, mouseY, delta);
			drop3Pris.render(mouseX, mouseY, delta);
		}

		Minecraft.getInstance().getTextureManager().bind(new ResourceLocation("lotas", "drops/sealantern.gif"));
		GuiComponent.blit(width - 128, y + 24, 0.0F, 0.0F, 96, 96, 96, 96);
	}

}
