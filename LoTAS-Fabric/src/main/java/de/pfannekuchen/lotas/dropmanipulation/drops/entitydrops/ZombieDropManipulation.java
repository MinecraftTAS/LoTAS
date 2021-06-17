package de.pfannekuchen.lotas.dropmanipulation.drops.entitydrops;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.platform.GlStateManager;

import de.pfannekuchen.lotas.gui.DropManipulationScreen;
import de.pfannekuchen.lotas.gui.widgets.ImageButton;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.widget.CheckboxWidget;
//#if MC>=11601
//$$ import net.minecraft.client.util.math.MatrixStack;
//#endif
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.HuskEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.mob.ZombieVillagerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;

public class ZombieDropManipulation extends DropManipulationScreen.DropManipulation {

	public static ImageButton dropIron = new ImageButton(x, y, c -> {
		ZombieDropManipulation.dropPotato.setToggled(false);
		ZombieDropManipulation.dropCarrot.setToggled(false);
		ZombieDropManipulation.dropIron.setToggled(!ZombieDropManipulation.dropIron.isToggled());
	}, new Identifier("lotas", "drops/iron.png"));
	public static ImageButton dropPotato = new ImageButton(x, y, c -> {
		ZombieDropManipulation.dropIron.setToggled(false);
		ZombieDropManipulation.dropCarrot.setToggled(false);
		ZombieDropManipulation.dropPotato.setToggled(!ZombieDropManipulation.dropPotato.isToggled());
	}, new Identifier("lotas", "drops/potato.png"));
	public static ImageButton dropCarrot = new ImageButton(x, y, c -> {
		ZombieDropManipulation.dropIron.setToggled(false);
		ZombieDropManipulation.dropPotato.setToggled(false);
		ZombieDropManipulation.dropCarrot.setToggled(!ZombieDropManipulation.dropCarrot.isToggled());
	}, new Identifier("lotas", "drops/carrot.png"));

	public ZombieDropManipulation(int x, int y, int width, int height) {
		ZombieDropManipulation.x = x;
		ZombieDropManipulation.y = y;
		ZombieDropManipulation.width = width;
		ZombieDropManipulation.height = height;
		//#if MC>=11601
//$$ 			enabled = new CheckboxWidget(x, y, 150, 20, new LiteralText("Override Zombie/Zombie-Villager/Husk Drops"), false);
		//#else
		enabled = new CheckboxWidget(x, y, 150, 20, "Override Zombie/Zombie-Villager/Husk Drops", false);
		//#endif
	}

	@Override
	public String getName() {
		return "Zombie";
	}

	@Override
	public List<ItemStack> redirectDrops(BlockState block) {
		return ImmutableList.of();
	}

	@Override
	public List<ItemStack> redirectDrops(Entity entity) {
		List<ItemStack> list = new ArrayList<>();
		if (entity instanceof ZombieEntity || entity instanceof HuskEntity || entity instanceof ZombieVillagerEntity) {
			list.add(new ItemStack(Items.ROTTEN_FLESH, 2));

			if (dropIron.isToggled())
				list.add(new ItemStack(Items.IRON_INGOT));
			if (dropPotato.isToggled())
				list.add(new ItemStack(Items.POTATO));
			if (dropCarrot.isToggled())
				list.add(new ItemStack(Items.CARROT));

		}
		return list;
	}

	@Override
	public void update() {
		enabled.x = x;
		enabled.y = y;
		dropIron.x = x;
		dropIron.y = y + 96;
		dropPotato.x = x + 22;
		dropPotato.y = y + 96;
		dropCarrot.x = x + 44;
		dropCarrot.y = y + 96;
	}

	@Override
	public void mouseAction(double mouseX, double mouseY, int button) {
		enabled.mouseClicked(mouseX, mouseY, button);
		if (enabled.isChecked()) {
			dropIron.mouseClicked(mouseX, mouseY, button);
			dropPotato.mouseClicked(mouseX, mouseY, button);
			dropCarrot.mouseClicked(mouseX, mouseY, button);
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
			GlStateManager.color4f(.5f, .5f, .5f, .4f);
		} else {
			//#if MC>=11601
//$$ 			MinecraftClient.getInstance().textRenderer.drawWithShadow((MatrixStack) matrices, "Zombies drop: 2 Rotten Flesh" + (dropIron.isToggled() ? ", 1 Iron Ingot" : "") + (dropPotato.isToggled() ? ", 1 Potato" : "") + (dropCarrot.isToggled() ? ", 1 Carrot" : ""), x, y + 64, 0xFFFFFF);
//$$ 			dropIron.render((MatrixStack) matrices, mouseX, mouseY, delta);
//$$ 			dropPotato.render((MatrixStack) matrices, mouseX, mouseY, delta);
//$$ 			dropCarrot.render((MatrixStack) matrices, mouseX, mouseY, delta);
			//#else
			MinecraftClient.getInstance().textRenderer.drawWithShadow("Zombies drop: 2 Rotten Flesh" + (dropIron.isToggled() ? ", 1 Iron Ingot" : "") + (dropPotato.isToggled() ? ", 1 Potato" : "") + (dropCarrot.isToggled() ? ", 1 Carrot" : ""), x, y + 64, 0xFFFFFF);
			dropIron.render(mouseX, mouseY, delta);
			dropPotato.render(mouseX, mouseY, delta);
			dropCarrot.render(mouseX, mouseY, delta);
			//#endif
		}

		MinecraftClient.getInstance().getTextureManager().bindTexture(new Identifier("lotas", "drops/zombie.png"));
		//#if MC>=11601
//$$ 		DrawableHelper.drawTexture((MatrixStack) matrices, x - 228, y + 24, 0.0F, 0.0F, 118, 198, 118, 198);
		//#else
		DrawableHelper.blit(width - 228, y + 24, 0.0F, 0.0F, 118, 198, 118, 198);
		//#endif
	}

}
