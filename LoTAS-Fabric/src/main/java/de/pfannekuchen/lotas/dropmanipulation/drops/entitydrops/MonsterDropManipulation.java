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
import net.minecraft.entity.mob.CaveSpiderEntity;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.ElderGuardianEntity;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.mob.GuardianEntity;
import net.minecraft.entity.mob.PhantomEntity;
import net.minecraft.entity.mob.ShulkerEntity;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.entity.mob.StrayEntity;
import net.minecraft.entity.mob.VindicatorEntity;
import net.minecraft.entity.mob.WitchEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;

public class MonsterDropManipulation extends DropManipulationScreen.DropManipulation {

	public static SmallCheckboxWidget optimizeCaveSpider = new SmallCheckboxWidget(0, 0, "Optimize Cave Spider Drops", false);
	public static SmallCheckboxWidget optimizeCreeper = new SmallCheckboxWidget(0, 0, "Optimize Creeper Drops", false);
	public static SmallCheckboxWidget optimizeElderGuardian = new SmallCheckboxWidget(0, 0, "Optimize Elder Guardian Drops", false);
	public static SmallCheckboxWidget optimizeEnderman = new SmallCheckboxWidget(0, 0, "Optimize Enderman Drops", false);
	public static SmallCheckboxWidget optimizePhantom = new SmallCheckboxWidget(0, 0, "Optimize Phantom Drops", false);
	public static SmallCheckboxWidget optimizeSlime = new SmallCheckboxWidget(0, 0, "Optimize Slime Drops", false);
	public static SmallCheckboxWidget optimizeVindicator = new SmallCheckboxWidget(0, 0, "Optimize Vindicator Drops", false);
	public static SmallCheckboxWidget optimizeSkeleton = new SmallCheckboxWidget(0, 0, "Optimize Skeleton Drops", false);
	public static SmallCheckboxWidget optimizeShulker = new SmallCheckboxWidget(0, 0, "Optimize Shulker Drops", false);
	public static SmallCheckboxWidget optimizeGuardian = new SmallCheckboxWidget(0, 0, "Optimize Guardian Drops", false);
	public static SmallCheckboxWidget optimizeWitch = new SmallCheckboxWidget(0, 0, "Optimize Witch Drops", false);

	public MonsterDropManipulation(int x, int y, int width, int height) {
		MonsterDropManipulation.x = x;
		MonsterDropManipulation.y = y;
		MonsterDropManipulation.width = width;
		MonsterDropManipulation.height = height;
		//#if MC>=11601
//$$ 		enabled = new CheckboxWidget(x, y, 150, 20, new LiteralText("Override Monster Drops"), false);
		//#else
		enabled = new CheckboxWidget(x, y, 150, 20, "Override Monster Drops", false);
		//#endif
	}

	@Override
	public String getName() {
		return "Monsters";
	}

	@Override
	public List<ItemStack> redirectDrops(BlockState blockstate) {
		return ImmutableList.of();
	}

	@Override
	public List<ItemStack> redirectDrops(Entity entity) {
		if (entity instanceof CaveSpiderEntity && optimizeCaveSpider.isChecked())
			return ImmutableList.of(new ItemStack(Items.SPIDER_EYE), new ItemStack(Items.STRING, 2));
		if (entity instanceof CreeperEntity && optimizeCreeper.isChecked())
			return ImmutableList.of(new ItemStack(Items.GUNPOWDER, 2));
		if (entity instanceof ElderGuardianEntity && optimizeElderGuardian.isChecked())
			return ImmutableList.of(new ItemStack(Items.PRISMARINE_CRYSTALS, 1), new ItemStack(Items.PRISMARINE_SHARD, 2), new ItemStack(Items.WET_SPONGE), new ItemStack(Items.PUFFERFISH));
		if (entity instanceof EndermanEntity && optimizeEnderman.isChecked())
			return ImmutableList.of(new ItemStack(Items.ENDER_PEARL, 1));
		if (entity instanceof PhantomEntity && optimizePhantom.isChecked())
			return ImmutableList.of(new ItemStack(Items.PHANTOM_MEMBRANE, 1));
		if ((entity instanceof SkeletonEntity || entity instanceof StrayEntity) && optimizeSkeleton.isChecked())
			return ImmutableList.of(new ItemStack(Items.ARROW, 2), new ItemStack(Items.BONE, 2));
		if (entity instanceof SlimeEntity && optimizeSlime.isChecked())
			if (((SlimeEntity) entity).getSize() == 1) {
				return ImmutableList.of(new ItemStack(Items.SLIME_BALL, 2));
			}
		if (entity instanceof SlimeEntity && optimizeSlime.isChecked())
			if (((SlimeEntity) entity).getSize() == 1)
				return ImmutableList.of(new ItemStack(Items.SLIME_BALL, 2));
		if (entity instanceof VindicatorEntity && optimizeVindicator.isChecked())
			return ImmutableList.of(new ItemStack(Items.EMERALD, 1));
		if (entity instanceof GuardianEntity && optimizeGuardian.isChecked())
			return ImmutableList.of(new ItemStack(Items.PRISMARINE_SHARD, 2));
		if (entity instanceof ShulkerEntity && optimizeShulker.isChecked())
			return ImmutableList.of(new ItemStack(Items.SHULKER_SHELL, 1));
		if (entity instanceof WitchEntity && optimizeWitch.isChecked()) {
			if (!((LivingEntity) entity).isBaby())
				return ImmutableList.of(new ItemStack(Items.GLOWSTONE_DUST), new ItemStack(Items.STICK), new ItemStack(Items.REDSTONE), new ItemStack(Items.GUNPOWDER), new ItemStack(Items.GLASS_BOTTLE), new ItemStack(Items.SPIDER_EYE));
		}
		return ImmutableList.of();
	}

	@Override
	public void update() {
		enabled.x = x;
		enabled.y = y;
		optimizeCaveSpider.y = 64;
		optimizeEnderman.y = 80;
		optimizeElderGuardian.y = 96;
		optimizeCreeper.y = 112;
		optimizePhantom.y = 128;
		optimizeSlime.y = 144;
		optimizeVindicator.y = 160;
		optimizeSkeleton.y = 176;
		optimizeShulker.y = 192;
		optimizeGuardian.y = 208;
		optimizeWitch.y = 224;
		optimizeCaveSpider.x = x;
		optimizeEnderman.x = x;
		optimizeElderGuardian.x = x;
		optimizeCreeper.x = x;
		optimizePhantom.x = x;
		optimizeSlime.x = x;
		optimizeSkeleton.x = x;
		optimizeVindicator.x = x;
		optimizeShulker.x = x;
		optimizeGuardian.x = x;
		optimizeWitch.x = x;
	}

	@Override
	public void mouseAction(double mouseX, double mouseY, int button) {
		enabled.mouseClicked(mouseX, mouseY, button);
		if (enabled.isChecked()) {
			optimizeCaveSpider.mouseClicked(mouseX, mouseY, button);
			optimizeEnderman.mouseClicked(mouseX, mouseY, button);
			optimizeCreeper.mouseClicked(mouseX, mouseY, button);
			optimizeElderGuardian.mouseClicked(mouseX, mouseY, button);
			optimizePhantom.mouseClicked(mouseX, mouseY, button);
			optimizeSlime.mouseClicked(mouseX, mouseY, button);
			optimizeVindicator.mouseClicked(mouseX, mouseY, button);
			optimizeSkeleton.mouseClicked(mouseX, mouseY, button);
			optimizeShulker.mouseClicked(mouseX, mouseY, button);
			optimizeGuardian.mouseClicked(mouseX, mouseY, button);
			optimizeWitch.mouseClicked(mouseX, mouseY, button);
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
//$$ 			optimizeCaveSpider.render((MatrixStack) matrices, mouseX, mouseY, delta);
//$$ 			optimizeEnderman.render((MatrixStack) matrices, mouseX, mouseY, delta);
//$$ 			optimizeCreeper.render((MatrixStack) matrices, mouseX, mouseY, delta);
//$$ 			optimizeElderGuardian.render((MatrixStack) matrices, mouseX, mouseY, delta);
//$$ 			optimizePhantom.render((MatrixStack) matrices, mouseX, mouseY, delta);
//$$ 			optimizeSlime.render((MatrixStack) matrices, mouseX, mouseY, delta);
//$$ 			optimizeVindicator.render((MatrixStack) matrices, mouseX, mouseY, delta);
//$$ 			optimizeSkeleton.render((MatrixStack) matrices, mouseX, mouseY, delta);
//$$ 			optimizeShulker.render((MatrixStack) matrices, mouseX, mouseY, delta);
//$$ 			optimizeGuardian.render((MatrixStack) matrices, mouseX, mouseY, delta);
//$$ 			optimizeWitch.render((MatrixStack) matrices, mouseX, mouseY, delta);
			//#else
			optimizeCaveSpider.render(mouseX, mouseY, delta);
			optimizeEnderman.render(mouseX, mouseY, delta);
			optimizeCreeper.render(mouseX, mouseY, delta);
			optimizeElderGuardian.render(mouseX, mouseY, delta);
			optimizePhantom.render(mouseX, mouseY, delta);
			optimizeSlime.render(mouseX, mouseY, delta);
			optimizeVindicator.render(mouseX, mouseY, delta);
			optimizeSkeleton.render(mouseX, mouseY, delta);
			optimizeShulker.render(mouseX, mouseY, delta);
			optimizeGuardian.render(mouseX, mouseY, delta);
			optimizeWitch.render(mouseX, mouseY, delta);
			//#endif
		}

		MinecraftClient.getInstance().getTextureManager().bindTexture(new Identifier("lotas", "drops/spider.png"));
		//#if MC>=11601
//$$ 		DrawableHelper.drawTexture((MatrixStack) matrices, width - 128, y + 24, 0.0F, 0.0F, 109, 85, 109, 85);
		//#else
		DrawableHelper.blit(width - 128, y + 24, 0.0F, 0.0F, 109, 85, 109, 85);
		//#endif
	}

}
