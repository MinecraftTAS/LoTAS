package de.pfannekuchen.lotas.dropmanipulation.drops.entitydrops;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.platform.GlStateManager;

import de.pfannekuchen.lotas.gui.DropManipulationScreen;
import de.pfannekuchen.lotas.gui.widgets.SmallCheckboxWidget;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.widget.CheckboxWidget;
//#if MC>=11601
//$$ import net.minecraft.client.util.math.MatrixStack;
//#endif
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.SkeletonHorseEntity;
import net.minecraft.entity.mob.ZombieHorseEntity;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.passive.DonkeyEntity;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.LlamaEntity;
import net.minecraft.entity.passive.MooshroomEntity;
import net.minecraft.entity.passive.MuleEntity;
import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.passive.PolarBearEntity;
import net.minecraft.entity.passive.RabbitEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.passive.SnowGolemEntity;
import net.minecraft.entity.passive.SquidEntity;
import net.minecraft.entity.passive.TraderLlamaEntity;
import net.minecraft.entity.passive.TurtleEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;

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
		//#if MC>=11601
//$$ 		enabled = new CheckboxWidget(x, y, 150, 20, new LiteralText("Override Passive Mob Drops"), false);
		//#else
		enabled = new CheckboxWidget(x, y, 150, 20, "Override Passive Mob Drops", false);
		//#endif
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
		if (entity instanceof ChickenEntity && optimizeChicken.isChecked()) {
			if (!((LivingEntity) entity).isBaby())
				return ImmutableList.of(new ItemStack(Items.FEATHER, 2), entity.isOnFire() ? new ItemStack(Items.COOKED_CHICKEN) : new ItemStack(Items.CHICKEN));
		} else if (entity instanceof CowEntity && optimizeCow.isChecked()) {
			if (!((LivingEntity) entity).isBaby())
				return ImmutableList.of(new ItemStack(Items.LEATHER), entity.isOnFire() ? new ItemStack(Items.COOKED_BEEF, 3) : new ItemStack(Items.BEEF, 3));
		} else if (entity instanceof MooshroomEntity && optimizeMooshroom.isChecked()) {
			if (!((LivingEntity) entity).isBaby())
				return ImmutableList.of(new ItemStack(Items.LEATHER, 2), entity.isOnFire() ? new ItemStack(Items.COOKED_BEEF, 3) : new ItemStack(Items.BEEF, 3));
		} else if (entity instanceof SkeletonHorseEntity && optimizeSkeletonhorse.isChecked()) {
			if (!((LivingEntity) entity).isBaby())
				return ImmutableList.of(new ItemStack(Items.BONE, 2));
		} else if (entity instanceof CatEntity && optimizeCat.isChecked()) {
			if (!((LivingEntity) entity).isBaby())
				return ImmutableList.of(new ItemStack(Items.STRING, 2));
		} else if (entity instanceof PigEntity && optimizePig.isChecked()) {
			if (!((LivingEntity) entity).isBaby())
				return ImmutableList.of(entity.isOnFire() ? new ItemStack(Items.COOKED_PORKCHOP, 3) : new ItemStack(Items.PORKCHOP, 3));
		} else if (entity instanceof ParrotEntity && optimizeParrot.isChecked()) {
			if (!((LivingEntity) entity).isBaby())
				return ImmutableList.of(new ItemStack(Items.FEATHER, 2));
		} else if (entity instanceof RabbitEntity && optimizeRabbit.isChecked()) {
			if (!((LivingEntity) entity).isBaby())
				return ImmutableList.of(new ItemStack(Items.RABBIT_FOOT, 1), new ItemStack(Items.RABBIT_HIDE, 1), entity.isOnFire() ? new ItemStack(Items.COOKED_RABBIT) : new ItemStack(Items.RABBIT));
		} else if (entity instanceof SheepEntity && optimizeSheep.isChecked()) {
			if (!((LivingEntity) entity).isBaby()) {
				try {
					return ImmutableList.of(entity.isOnFire() ? new ItemStack(Items.COOKED_MUTTON, 3) : new ItemStack(Items.MUTTON, 3), new ItemStack((Item) Items.class.getField(((SheepEntity) entity).getColor().name() + "_WOOL").get(null)));
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (NoSuchFieldException e) {
					e.printStackTrace();
				}
			}
		} else if (entity instanceof SnowGolemEntity && optimizeSnowgolem.isChecked()) {
			if (!((LivingEntity) entity).isBaby())
				return ImmutableList.of(new ItemStack(Items.SNOWBALL, 15));
		} else if (entity instanceof SquidEntity && optimizeSquid.isChecked()) {
			if (!((LivingEntity) entity).isBaby())
				return ImmutableList.of(new ItemStack(Items.INK_SAC, 3));
		} else if ((entity instanceof HorseEntity || entity instanceof MuleEntity || entity instanceof DonkeyEntity || entity instanceof LlamaEntity || entity instanceof TraderLlamaEntity) && optimizeHorses.isChecked()) {
			if (!((LivingEntity) entity).isBaby())
				return ImmutableList.of(new ItemStack(Items.LEATHER, 2));
		} else if (entity instanceof ZombieHorseEntity && optimizeHorses.isChecked()) {
			if (!((LivingEntity) entity).isBaby())
				return ImmutableList.of(new ItemStack(Items.ROTTEN_FLESH, 2));
		} else if (entity instanceof TurtleEntity && optimizeTurtle.isChecked()) {
			if (!((LivingEntity) entity).isBaby())
				return ImmutableList.of(new ItemStack(Items.SEAGRASS, 2));
		} else if (entity instanceof IronGolemEntity && optimizeIronGolem.isChecked()) {
			if (!((LivingEntity) entity).isBaby())
				return ImmutableList.of(new ItemStack(Items.IRON_INGOT, 5), new ItemStack(Items.POPPY));
		} else if (entity instanceof PolarBearEntity && optimizePolarbear.isChecked()) {
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
		if (enabled.isChecked()) {
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
//$$ 			optimizeChicken.render((MatrixStack) matrices, mouseX, mouseY, delta);
//$$ 			optimizeSkeletonhorse.render((MatrixStack) matrices, mouseX, mouseY, delta);
//$$ 			optimizeCat.render((MatrixStack) matrices, mouseX, mouseY, delta);
//$$ 			optimizeCow.render((MatrixStack) matrices, mouseX, mouseY, delta);
//$$ 			optimizeMooshroom.render((MatrixStack) matrices, mouseX, mouseY, delta);
//$$ 			optimizePig.render((MatrixStack) matrices, mouseX, mouseY, delta);
//$$ 			optimizeParrot.render((MatrixStack) matrices, mouseX, mouseY, delta);
//$$ 			optimizeSnowgolem.render((MatrixStack) matrices, mouseX, mouseY, delta);
//$$ 			optimizeSheep.render((MatrixStack) matrices, mouseX, mouseY, delta);
//$$ 			optimizeRabbit.render((MatrixStack) matrices, mouseX, mouseY, delta);
//$$ 			optimizeSquid.render((MatrixStack) matrices, mouseX, mouseY, delta);
//$$ 			optimizeTurtle.render((MatrixStack) matrices, mouseX, mouseY, delta);
//$$ 			optimizeIronGolem.render((MatrixStack) matrices, mouseX, mouseY, delta);
//$$ 			optimizePolarbear.render((MatrixStack) matrices, mouseX, mouseY, delta);
//$$ 			optimizeHorses.render((MatrixStack) matrices, mouseX, mouseY, delta);
			//#else
			optimizeChicken.render(mouseX, mouseY, delta);
			optimizeSkeletonhorse.render(mouseX, mouseY, delta);
			optimizeCat.render(mouseX, mouseY, delta);
			optimizeCow.render(mouseX, mouseY, delta);
			optimizeMooshroom.render(mouseX, mouseY, delta);
			optimizePig.render(mouseX, mouseY, delta);
			optimizeParrot.render(mouseX, mouseY, delta);
			optimizeSnowgolem.render(mouseX, mouseY, delta);
			optimizeSheep.render(mouseX, mouseY, delta);
			optimizeRabbit.render(mouseX, mouseY, delta);
			optimizeSquid.render(mouseX, mouseY, delta);
			optimizeTurtle.render(mouseX, mouseY, delta);
			optimizeIronGolem.render(mouseX, mouseY, delta);
			optimizePolarbear.render(mouseX, mouseY, delta);
			optimizeHorses.render(mouseX, mouseY, delta);
			//#endif
		}

		MinecraftClient.getInstance().getTextureManager().bindTexture(new Identifier("lotas", "drops/sheep.png"));
		//#if MC>=11601
//$$ 		DrawableHelper.drawTexture((MatrixStack) matrices, width - 128, y + 24, 0.0F, 0.0F, 102, 120, 102, 120);
		//#else
		DrawableHelper.blit(width - 128, y + 24, 0.0F, 0.0F, 102, 120, 102, 120);
		//#endif
	}

}
