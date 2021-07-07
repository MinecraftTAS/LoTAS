package de.pfannekuchen.lotas.dropmanipulation.drops.entitydrops;

import java.util.List;

import com.google.common.collect.ImmutableList;

import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.gui.DropManipulationScreen;
import de.pfannekuchen.lotas.gui.widgets.SmallCheckboxWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.MushroomCow;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.animal.PolarBear;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.entity.animal.Squid;
import net.minecraft.world.entity.animal.Turtle;
import net.minecraft.world.entity.animal.horse.Donkey;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.entity.animal.horse.Mule;
import net.minecraft.world.entity.animal.horse.SkeletonHorse;
import net.minecraft.world.entity.animal.horse.TraderLlama;
import net.minecraft.world.entity.animal.horse.ZombieHorse;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;

public class PassiveDropManipulation extends DropManipulationScreen.DropManipulation {

	public static SmallCheckboxWidget optimizeChicken = new SmallCheckboxWidget(0, 0, "Optimize Chicken Drops", false);
	public static SmallCheckboxWidget optimizeCow = new SmallCheckboxWidget(0, 0, "Optimize Cow Drops", false);
	public static SmallCheckboxWidget optimizeMooshroom = new SmallCheckboxWidget(0, 0, "Optimize Mooshroom Drops", false);
	public static SmallCheckboxWidget optimizeSkeletonhorse = new SmallCheckboxWidget(0, 0, "Optimize Skeleton Horse Drops", false);
	public static SmallCheckboxWidget optimizeCat = new SmallCheckboxWidget(0, 0, "Optimize Cat Drops", false);
	public static SmallCheckboxWidget optimizePig = new SmallCheckboxWidget(0, 0, "Optimize Pig Drops", false);
	public static SmallCheckboxWidget optimizeParrot = new SmallCheckboxWidget(0, 0, "Optimize Parrot Drops", false);
	public static SmallCheckboxWidget optimizeRabbit = new SmallCheckboxWidget(0, 0, "Optimize Rabbit Drops", false);
	public static SmallCheckboxWidget optimizeSheep = new SmallCheckboxWidget(0, 0, "Optimize Sheep Drops", false);
	public static SmallCheckboxWidget optimizeSnowgolem = new SmallCheckboxWidget(0, 0, "Optimize Snowgolem Drops", false);
	public static SmallCheckboxWidget optimizeSquid = new SmallCheckboxWidget(0, 0, "Optimize Squid Drops", false);
	public static SmallCheckboxWidget optimizeHorses = new SmallCheckboxWidget(0, 0, "Optimize All Horse Drops", false);
	public static SmallCheckboxWidget optimizeTurtle = new SmallCheckboxWidget(0, 0, "Optimize Turtle Drops", false);
	public static SmallCheckboxWidget optimizeIronGolem = new SmallCheckboxWidget(0, 0, "Optimize Iron Golem Drops", false);
	public static SmallCheckboxWidget optimizePolarbear = new SmallCheckboxWidget(0, 0, "Optimize Polarbear Drops", false);

	public PassiveDropManipulation(int x, int y, int width, int height) {
		PassiveDropManipulation.x = x;
		PassiveDropManipulation.y = y;
		PassiveDropManipulation.width = width;
		PassiveDropManipulation.height = height;
		enabled = MCVer.Checkbox(x, y, 150, 20, "Override Passive Mob Drops", false);
	}

	@Override
	public String getName() {
		return "Passive Mobs";
	}

	@Override
	public List<ItemStack> redirectDrops(BlockState blockstate) {
		return ImmutableList.of();
	}

	@Override
	public List<ItemStack> redirectDrops(Entity entity) {
		if (entity instanceof Chicken && optimizeChicken.isChecked()) {
			if (!((LivingEntity) entity).isBaby())
				return ImmutableList.of(new ItemStack(Items.FEATHER, 2), entity.isOnFire() ? new ItemStack(Items.COOKED_CHICKEN) : new ItemStack(Items.CHICKEN));
		} else if (entity instanceof Cow && optimizeCow.isChecked()) {
			if (!((LivingEntity) entity).isBaby())
				return ImmutableList.of(new ItemStack(Items.LEATHER), entity.isOnFire() ? new ItemStack(Items.COOKED_BEEF, 3) : new ItemStack(Items.BEEF, 3));
		} else if (entity instanceof MushroomCow && optimizeMooshroom.isChecked()) {
			if (!((LivingEntity) entity).isBaby())
				return ImmutableList.of(new ItemStack(Items.LEATHER, 2), entity.isOnFire() ? new ItemStack(Items.COOKED_BEEF, 3) : new ItemStack(Items.BEEF, 3));
		} else if (entity instanceof SkeletonHorse && optimizeSkeletonhorse.isChecked()) {
			if (!((LivingEntity) entity).isBaby())
				return ImmutableList.of(new ItemStack(Items.BONE, 2));
		} else if (entity instanceof Cat && optimizeCat.isChecked()) {
			if (!((LivingEntity) entity).isBaby())
				return ImmutableList.of(new ItemStack(Items.STRING, 2));
		} else if (entity instanceof Pig && optimizePig.isChecked()) {
			if (!((LivingEntity) entity).isBaby())
				return ImmutableList.of(entity.isOnFire() ? new ItemStack(Items.COOKED_PORKCHOP, 3) : new ItemStack(Items.PORKCHOP, 3));
		} else if (entity instanceof Parrot && optimizeParrot.isChecked()) {
			if (!((LivingEntity) entity).isBaby())
				return ImmutableList.of(new ItemStack(Items.FEATHER, 2));
		} else if (entity instanceof Rabbit && optimizeRabbit.isChecked()) {
			if (!((LivingEntity) entity).isBaby())
				return ImmutableList.of(new ItemStack(Items.RABBIT_FOOT, 1), new ItemStack(Items.RABBIT_HIDE, 1), entity.isOnFire() ? new ItemStack(Items.COOKED_RABBIT) : new ItemStack(Items.RABBIT));
		} else if (entity instanceof Sheep && optimizeSheep.isChecked()) {
			if (!((LivingEntity) entity).isBaby()) {
				try {
					return ImmutableList.of(entity.isOnFire() ? new ItemStack(Items.COOKED_MUTTON, 3) : new ItemStack(Items.MUTTON, 3), new ItemStack((Item) Items.class.getField(((Sheep) entity).getColor().name() + "_WOOL").get(null)));
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (NoSuchFieldException e) {
					e.printStackTrace();
				}
			}
		} else if (entity instanceof SnowGolem && optimizeSnowgolem.isChecked()) {
			if (!((LivingEntity) entity).isBaby())
				return ImmutableList.of(new ItemStack(Items.SNOWBALL, 15));
		} else if (entity instanceof Squid && optimizeSquid.isChecked()) {
			if (!((LivingEntity) entity).isBaby())
				return ImmutableList.of(new ItemStack(Items.INK_SAC, 3));
		} else if ((entity instanceof Horse || entity instanceof Mule || entity instanceof Donkey || entity instanceof Llama || entity instanceof TraderLlama) && optimizeHorses.isChecked()) {
			if (!((LivingEntity) entity).isBaby())
				return ImmutableList.of(new ItemStack(Items.LEATHER, 2));
		} else if (entity instanceof ZombieHorse && optimizeHorses.isChecked()) {
			if (!((LivingEntity) entity).isBaby())
				return ImmutableList.of(new ItemStack(Items.ROTTEN_FLESH, 2));
		} else if (entity instanceof Turtle && optimizeTurtle.isChecked()) {
			if (!((LivingEntity) entity).isBaby())
				return ImmutableList.of(new ItemStack(Items.SEAGRASS, 2));
		} else if (entity instanceof IronGolem && optimizeIronGolem.isChecked()) {
			if (!((LivingEntity) entity).isBaby())
				return ImmutableList.of(new ItemStack(Items.IRON_INGOT, 5), new ItemStack(Items.POPPY));
		} else if (entity instanceof PolarBear && optimizePolarbear.isChecked()) {
			if (!((LivingEntity) entity).isBaby())
				return ImmutableList.of(new ItemStack(Items.COD, 2));
		}
		return ImmutableList.of();
	}

	@Override
	public void update() {
		enabled.x = x;
		enabled.y = y;
		optimizeChicken.y = 64;
		optimizeSkeletonhorse.y = 80;
		optimizeCat.y = 96;
		optimizeMooshroom.y = 112;
		optimizeCow.y = 128;
		optimizePig.y = 144;
		optimizeParrot.y = 160;
		optimizeRabbit.y = 176;
		optimizeSnowgolem.y = 192;
		optimizeSheep.y = 208;
		optimizeSquid.y = 224;
		optimizeHorses.y = 240;
		optimizeTurtle.y = 256;
		optimizeIronGolem.y = 272;
		optimizePolarbear.y = 288;
		optimizeChicken.x = x;
		optimizeSkeletonhorse.x = x;
		optimizeCat.x = x;
		optimizeMooshroom.x = x;
		optimizeCow.x = x;
		optimizePig.x = x;
		optimizeParrot.x = x;
		optimizeRabbit.x = x;
		optimizeSnowgolem.x = x;
		optimizeSheep.x = x;
		optimizeSquid.x = x;
		optimizeHorses.x = x;
		optimizeTurtle.x = x;
		optimizeIronGolem.x = x;
		optimizePolarbear.x = x;
	}

	@Override
	public void mouseAction(double mouseX, double mouseY, int button) {
		enabled.mouseClicked(mouseX, mouseY, button);
		if (enabled.selected()) {
			optimizeChicken.mouseClicked(mouseX, mouseY, button);
			optimizeSkeletonhorse.mouseClicked(mouseX, mouseY, button);
			optimizeCat.mouseClicked(mouseX, mouseY, button);
			optimizeCow.mouseClicked(mouseX, mouseY, button);
			optimizeMooshroom.mouseClicked(mouseX, mouseY, button);
			optimizePig.mouseClicked(mouseX, mouseY, button);
			optimizeParrot.mouseClicked(mouseX, mouseY, button);
			optimizeSheep.mouseClicked(mouseX, mouseY, button);
			optimizeRabbit.mouseClicked(mouseX, mouseY, button);
			optimizeSnowgolem.mouseClicked(mouseX, mouseY, button);
			optimizeSquid.mouseClicked(mouseX, mouseY, button);
			optimizeTurtle.mouseClicked(mouseX, mouseY, button);
			optimizeHorses.mouseClicked(mouseX, mouseY, button);
			optimizePolarbear.mouseClicked(mouseX, mouseY, button);
			optimizeIronGolem.mouseClicked(mouseX, mouseY, button);
		}
	}

	@Override
	public void render(int mouseX, int mouseY, float delta) {
		MCVer.render(enabled, mouseX, mouseY, delta);

		if (!enabled.selected()) {
			MCVer.color4f(.5f, .5f, .5f, .4f);
		} else {
			MCVer.render(optimizeChicken, mouseX, mouseY, delta);
			MCVer.render(optimizeSkeletonhorse, mouseX, mouseY, delta);
			MCVer.render(optimizeCat, mouseX, mouseY, delta);
			MCVer.render(optimizeCow, mouseX, mouseY, delta);
			MCVer.render(optimizeMooshroom, mouseX, mouseY, delta);
			MCVer.render(optimizePig, mouseX, mouseY, delta);
			MCVer.render(optimizeParrot, mouseX, mouseY, delta);
			MCVer.render(optimizeSnowgolem, mouseX, mouseY, delta);
			MCVer.render(optimizeSheep, mouseX, mouseY, delta);
			MCVer.render(optimizeRabbit, mouseX, mouseY, delta);
			MCVer.render(optimizeSquid, mouseX, mouseY, delta);
			MCVer.render(optimizeTurtle, mouseX, mouseY, delta);
			MCVer.render(optimizeIronGolem, mouseX, mouseY, delta);
			MCVer.render(optimizePolarbear, mouseX, mouseY, delta);
			MCVer.render(optimizeHorses, mouseX, mouseY, delta);
		}

		Minecraft.getInstance().getTextureManager().bind(new ResourceLocation("lotas", "drops/sheep.png"));
		MCVer.blit(width - 128, y + 24, 0.0F, 0.0F, 102, 120, 102, 120);
	}

}
