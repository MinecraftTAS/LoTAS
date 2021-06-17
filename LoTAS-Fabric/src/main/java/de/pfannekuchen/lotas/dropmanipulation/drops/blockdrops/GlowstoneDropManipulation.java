package de.pfannekuchen.lotas.dropmanipulation.drops.blockdrops;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.platform.GlStateManager;

import de.pfannekuchen.lotas.gui.DropManipulationScreen;
import de.pfannekuchen.lotas.gui.widgets.NewButtonWidget;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CheckboxWidget;
//#if MC>=11601
//$$ import net.minecraft.client.util.math.MatrixStack;
//#endif
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;

public class GlowstoneDropManipulation extends DropManipulationScreen.DropManipulation {

	public static int dust = 2;

	public static ButtonWidget drop2Glowstonedust = new NewButtonWidget(x, y, 98, 20, "2 Glowstone Dust", button -> {
		press2Glowstonedust();
	});
	public static ButtonWidget drop3Glowstonedust = new NewButtonWidget(x, y, 98, 20, "3 Glowstone Dust", button -> {
		press3Glowstonedust();
	});
	public static ButtonWidget drop4Glowstonedust = new NewButtonWidget(x, y, 98, 20, "4 Glowstone Dust", button -> {
		press4Glowstonedust();
	});

	public static void press2Glowstonedust() {
		drop2Glowstonedust.active = false;
		drop3Glowstonedust.active = true;
		drop4Glowstonedust.active = true;
		dust = 2;
	}

	public static void press3Glowstonedust() {
		drop2Glowstonedust.active = true;
		drop3Glowstonedust.active = false;
		drop4Glowstonedust.active = true;
		dust = 3;
	}

	public static void press4Glowstonedust() {
		drop2Glowstonedust.active = true;
		drop3Glowstonedust.active = true;
		drop4Glowstonedust.active = false;
		dust = 4;
	}

	public GlowstoneDropManipulation(int x, int y, int width, int height) {
		GlowstoneDropManipulation.x = x;
		GlowstoneDropManipulation.y = y;
		GlowstoneDropManipulation.width = width;
		GlowstoneDropManipulation.height = height;
		//#if MC>=11601
//$$ 		enabled = new CheckboxWidget(x, y, 150, 20, new LiteralText("Override Glowstone Drops"), false);
		//#else
		enabled = new CheckboxWidget(x, y, 150, 20, "Override Glowstone Drops", false);
		//#endif
		drop2Glowstonedust.active = false;
	}

	@Override
	public String getName() {
		return "Glowstone";
	}

	@Override
	public List<ItemStack> redirectDrops(BlockState block) {
		if (block.getBlock().getDefaultState().getBlock() != Blocks.GLOWSTONE)
			return ImmutableList.of();
		return ImmutableList.of(new ItemStack(Items.GLOWSTONE_DUST, dust));
	}

	@Override
	public List<ItemStack> redirectDrops(Entity entity) {
		return ImmutableList.of();
	}

	@Override
	public void update() {
		enabled.x = x;
		enabled.y = y;

		drop2Glowstonedust.x = x;
		drop2Glowstonedust.y = y + 96;
		drop3Glowstonedust.x = x;
		drop3Glowstonedust.y = y + 120;
		drop4Glowstonedust.x = x;
		drop4Glowstonedust.y = y + 144;

		drop2Glowstonedust.setWidth(width - x - 128 - 16);
		drop3Glowstonedust.setWidth(width - x - 128 - 16);
		drop4Glowstonedust.setWidth(width - x - 128 - 16);
	}

	@Override
	public void mouseAction(double mouseX, double mouseY, int button) {
		enabled.mouseClicked(mouseX, mouseY, button);
		if (enabled.isChecked()) {
			drop2Glowstonedust.mouseClicked(mouseX, mouseY, button);
			drop3Glowstonedust.mouseClicked(mouseX, mouseY, button);
			drop4Glowstonedust.mouseClicked(mouseX, mouseY, button);
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
//$$ 			MinecraftClient.getInstance().textRenderer.drawWithShadow((MatrixStack) matrices, "Drop " + dust + " Glowstone Dust when breaking Glowstone", x, y + 64, 0xFFFFFF);
//$$ 			drop4Glowstonedust.render((MatrixStack) matrices, mouseX, mouseY, delta);
//$$ 			drop3Glowstonedust.render((MatrixStack) matrices, mouseX, mouseY, delta);
//$$ 			drop2Glowstonedust.render((MatrixStack) matrices, mouseX, mouseY, delta);
			//#else
			MinecraftClient.getInstance().textRenderer.drawWithShadow("Drop " + dust + " Glowstone Dust when breaking Glowstone", x, y + 64, 0xFFFFFF);
			drop4Glowstonedust.render(mouseX, mouseY, delta);
			drop3Glowstonedust.render(mouseX, mouseY, delta);
			drop2Glowstonedust.render(mouseX, mouseY, delta);
			//#endif
		}

		MinecraftClient.getInstance().getTextureManager().bindTexture(new Identifier("lotas", "drops/glowstone.png"));
		//#if MC>=11601
//$$ 		DrawableHelper.drawTexture((MatrixStack) matrices, width - 128, y + 24, 0.0F, 0.0F, 96, 96, 96, 96);
		//#else
		DrawableHelper.blit(width - 128, y + 24, 0.0F, 0.0F, 96, 96, 96, 96);
		//#endif
	}

}
