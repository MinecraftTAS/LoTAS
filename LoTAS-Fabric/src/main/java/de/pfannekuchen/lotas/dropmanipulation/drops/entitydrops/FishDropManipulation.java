package de.pfannekuchen.lotas.dropmanipulation.drops.entitydrops;

import java.util.List;

import com.google.common.collect.ImmutableList;

import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.gui.DropManipulationScreen;
import de.pfannekuchen.lotas.gui.widgets.SmallCheckboxWidget;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.CodEntity;
import net.minecraft.entity.passive.DolphinEntity;
import net.minecraft.entity.passive.PufferfishEntity;
import net.minecraft.entity.passive.SalmonEntity;
import net.minecraft.entity.passive.TropicalFishEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

public class FishDropManipulation extends DropManipulationScreen.DropManipulation {

	public static SmallCheckboxWidget optimizeCod = new SmallCheckboxWidget(0, 0, "Cod drop Bone Meal", false);
	public static SmallCheckboxWidget optimizeSalmon = new SmallCheckboxWidget(0, 0, "Salmon drop Bone Meal", false);
	public static SmallCheckboxWidget optimizeDolphin = new SmallCheckboxWidget(0, 0, "Dolphin drop 1 cod", false);
	public static SmallCheckboxWidget optimizePufferfish = new SmallCheckboxWidget(0, 0, "Pufferfish drop Bone Meal", false);
	public static SmallCheckboxWidget optimizeTropical = new SmallCheckboxWidget(0, 0, "Tropical Fish drop Bone Meal", false);

	public FishDropManipulation(int x, int y, int width, int height) {
		FishDropManipulation.x = x;
		FishDropManipulation.y = y;
		FishDropManipulation.width = width;
		FishDropManipulation.height = height;
		enabled = MCVer.CheckboxWidget(x, y, 150, 20, "Override Fish Drops", false);
	}

	@Override
	public String getName() {
		return "Fish";
	}

	@Override
	public List<ItemStack> redirectDrops(BlockState blockstate) {
		return ImmutableList.of();
	}

	@Override
	public List<ItemStack> redirectDrops(Entity entity) {
		if (entity instanceof CodEntity && optimizeCod.isChecked())
			return ImmutableList.of(new ItemStack(Items.COD), new ItemStack(Items.BONE_MEAL, 1));
		if (entity instanceof SalmonEntity && optimizeSalmon.isChecked())
			return ImmutableList.of(new ItemStack(Items.SALMON), new ItemStack(Items.BONE_MEAL, 1));
		if (entity instanceof DolphinEntity && optimizeDolphin.isChecked())
			return ImmutableList.of(new ItemStack(Items.COD));
		if (entity instanceof PufferfishEntity && optimizePufferfish.isChecked())
			return ImmutableList.of(new ItemStack(Items.PUFFERFISH, 1), new ItemStack(Items.BONE_MEAL, 1));
		if (entity instanceof TropicalFishEntity && optimizeTropical.isChecked())
			return ImmutableList.of(new ItemStack(Items.TROPICAL_FISH, 1), new ItemStack(Items.BONE_MEAL, 1));
		return ImmutableList.of();
	}

	@Override
	public void update() {
		enabled.x = x;
		enabled.y = y;
		optimizeCod.y = 64;
		optimizePufferfish.y = 80;
		optimizeDolphin.y = 96;
		optimizeSalmon.y = 112;
		optimizeTropical.y = 128;
		optimizeCod.x = x;
		optimizePufferfish.x = x;
		optimizeDolphin.x = x;
		optimizeSalmon.x = x;
		optimizeTropical.x = x;
	}

	@Override
	public void mouseAction(double mouseX, double mouseY, int button) {
		enabled.mouseClicked(mouseX, mouseY, button);
		if (enabled.isChecked()) {
			optimizeCod.mouseClicked(mouseX, mouseY, button);
			optimizePufferfish.mouseClicked(mouseX, mouseY, button);
			optimizeSalmon.mouseClicked(mouseX, mouseY, button);
			optimizeDolphin.mouseClicked(mouseX, mouseY, button);
			optimizeTropical.mouseClicked(mouseX, mouseY, button);
		}
	}

	@Override
	public void render(Object matrices, int mouseX, int mouseY, float delta) {
		MCVer.render(enabled, mouseX, mouseY, delta);
		if (!enabled.isChecked()) {
			MCVer.color(.5f, .5f, .5f, .4f);
		} else {
			MCVer.render(optimizeCod, mouseX, mouseY, delta);
			MCVer.render(optimizePufferfish, mouseX, mouseY, delta);
			MCVer.render(optimizeSalmon, mouseX, mouseY, delta);
			MCVer.render(optimizeDolphin, mouseX, mouseY, delta);
			MCVer.render(optimizeTropical, mouseX, mouseY, delta);
		}

		MinecraftClient.getInstance().getTextureManager().bindTexture(new Identifier("lotas", "drops/fish.gif"));
		MCVer.renderImage(width - 128, y + 24, 0.0F, 0.0F, 96, 76, 96, 76);
	}

}
