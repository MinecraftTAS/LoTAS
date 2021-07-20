package de.pfannekuchen.lotas.dropmanipulation.drops.entitydrops;

import java.util.List;

import com.google.common.collect.ImmutableList;

import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.gui.DropManipulationScreen;
import de.pfannekuchen.lotas.gui.widgets.SmallCheckboxWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.monster.MagmaCube;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;

public class NetherMobDropManipulation extends DropManipulationScreen.DropManipulation {

	public static SmallCheckboxWidget optimizeBlaze = new SmallCheckboxWidget(0, 0, "Optimize Blaze Drops", false);
	public static SmallCheckboxWidget optimizeGhast = new SmallCheckboxWidget(0, 0, "Optimize Ghast Drops", false);
	public static SmallCheckboxWidget optimizeWitherskeleton = new SmallCheckboxWidget(0, 0, "Optimize Witherskeleton Drops", false);
	public static SmallCheckboxWidget optimizePigman = new SmallCheckboxWidget(0, 0, "Optimize Zombie Pigman Drops", false);
	public static SmallCheckboxWidget optimizeMagmaCube = new SmallCheckboxWidget(0, 0, "Optimize Magma Cube Drops", false);

	public NetherMobDropManipulation(int x, int y, int width, int height) {
		NetherMobDropManipulation.x = x;
		NetherMobDropManipulation.y = y;
		NetherMobDropManipulation.width = width;
		NetherMobDropManipulation.height = height;
		enabled = MCVer.Checkbox(x, y, 150, 20, "Override Nether Mob Drops", false);
	}

	@Override
	public String getName() {
		return "Nether Mobs";
	}

	@Override
	public List<ItemStack> redirectDrops(BlockState blockstate) {
		return ImmutableList.of();
	}

	@Override
	public List<ItemStack> redirectDrops(Entity entity) {
		if (entity instanceof Blaze && optimizeBlaze.isChecked())
			return ImmutableList.of(new ItemStack(Items.BLAZE_ROD));
		if (entity instanceof Ghast && optimizeGhast.isChecked())
			return ImmutableList.of(new ItemStack(Items.GHAST_TEAR), new ItemStack(Items.GUNPOWDER, 2));
		if (entity instanceof WitherSkeleton && optimizeWitherskeleton.isChecked())
			return ImmutableList.of(new ItemStack(Items.COAL, 1), new ItemStack(Items.BONE, 2), new ItemStack(Items.WITHER_SKELETON_SKULL));
		//#if MC>=11601
//$$ 		if (entity instanceof net.minecraft.world.entity.monster.ZombifiedPiglin && optimizePigman.isChecked()) if (!((net.minecraft.world.entity.monster.ZombifiedPiglin) entity).isBaby()) return ImmutableList.of(new ItemStack(Items.ROTTEN_FLESH, 2), new ItemStack(Items.GOLD_NUGGET), new ItemStack(Items.GOLD_INGOT));
		//#else
		if (entity instanceof net.minecraft.world.entity.monster.PigZombie && optimizePigman.isChecked()) if (!((net.minecraft.world.entity.monster.PigZombie) entity).isBaby()) return ImmutableList.of(new ItemStack(Items.ROTTEN_FLESH, 2), new ItemStack(Items.GOLD_NUGGET), new ItemStack(Items.GOLD_INGOT));
		//#endif
		if (entity instanceof MagmaCube && optimizeMagmaCube.isChecked())
			if (((MagmaCube) entity).getSize() != 1)
				return ImmutableList.of(new ItemStack(Items.MAGMA_CREAM));

		return ImmutableList.of();
	}

	@Override
	public void update() {
		enabled.x = x;
		enabled.y = y;
		optimizeBlaze.y = 64;
		optimizePigman.y = 80;
		optimizeMagmaCube.y = 96;
		optimizeWitherskeleton.y = 112;
		optimizeGhast.y = 128;
		optimizeBlaze.x = x;
		optimizePigman.x = x;
		optimizeMagmaCube.x = x;
		optimizeWitherskeleton.x = x;
		optimizeGhast.x = x;
	}

	@Override
	public void mouseAction(double mouseX, double mouseY, int button) {
		enabled.mouseClicked(mouseX, mouseY, button);
		if (enabled.selected()) {
			optimizeBlaze.mouseClicked(mouseX, mouseY, button);
			optimizePigman.mouseClicked(mouseX, mouseY, button);
			optimizeMagmaCube.mouseClicked(mouseX, mouseY, button);
			optimizeGhast.mouseClicked(mouseX, mouseY, button);
			optimizeWitherskeleton.mouseClicked(mouseX, mouseY, button);
		}
	}

	@Override
	public void render(int mouseX, int mouseY, float delta) {
		MCVer.render(enabled, mouseX, mouseY, delta);

		if (!enabled.selected()) {
			MCVer.color4f(.5f, .5f, .5f, .4f);
		} else {
			MCVer.render(optimizeBlaze, mouseX, mouseY, delta);
			MCVer.render(optimizePigman, mouseX, mouseY, delta);
			MCVer.render(optimizeMagmaCube, mouseX, mouseY, delta);
			MCVer.render(optimizeGhast, mouseX, mouseY, delta);
			MCVer.render(optimizeWitherskeleton, mouseX, mouseY, delta);
		}

		Minecraft.getInstance().getTextureManager().bind(new ResourceLocation("lotas", "drops/wither_skeleton.png"));
		MCVer.blit(width - 128, y + 24, 0.0F, 0.0F, 100, 150, 100, 150);
	}

}
