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

public class DeadbushDropManipulation extends DropManipulationScreen.DropManipulation {

	public static int sticks = 0;

	public static ButtonWidget drop0Stick = new NewButtonWidget(x, y, 98, 20, "0 Sticks", button -> {
		press0Stick();
	});
	public static ButtonWidget drop1Stick = new NewButtonWidget(x, y, 98, 20, "1 Sticks", button -> {
		press1Stick();
	});
	public static ButtonWidget drop2Stick = new NewButtonWidget(x, y, 98, 20, "2 Sticks", button -> {
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
		//#if MC>=11601
//$$ 		enabled = new CheckboxWidget(x, y, 150, 20, new LiteralText("Override Dead Bush Drops"), false);
		//#else
		enabled = new CheckboxWidget(x, y, 150, 20, "Override Dead Bush Drops", false);
		//#endif
		drop0Stick.active = false;
	}

	@Override
	public String getName() {
		return "Dead Bush";
	}

	@Override
	public List<ItemStack> redirectDrops(BlockState block) {
		if (block.getBlock().getDefaultState().getBlock() != Blocks.DEAD_BUSH)
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
		if (enabled.isChecked()) {
			drop0Stick.mouseClicked(mouseX, mouseY, button);
			drop1Stick.mouseClicked(mouseX, mouseY, button);
			drop2Stick.mouseClicked(mouseX, mouseY, button);
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
//$$ 			MinecraftClient.getInstance().textRenderer.drawWithShadow((MatrixStack) matrices, "Drop " + sticks + " Sticks when breaking Gravel", x, y + 64, 0xFFFFFF);
//$$ 			drop2Stick.render((MatrixStack) matrices, mouseX, mouseY, delta);
//$$ 			drop1Stick.render((MatrixStack) matrices, mouseX, mouseY, delta);
//$$ 			drop0Stick.render((MatrixStack) matrices, mouseX, mouseY, delta);
			//#else
			MinecraftClient.getInstance().textRenderer.drawWithShadow("Drop " + sticks + " Sticks when breaking Gravel", x, y + 64, 0xFFFFFF);
			drop2Stick.render(mouseX, mouseY, delta);
			drop1Stick.render(mouseX, mouseY, delta);
			drop0Stick.render(mouseX, mouseY, delta);
			//#endif

		}

		MinecraftClient.getInstance().getTextureManager().bindTexture(new Identifier("lotas", "drops/deadbush.png"));
		//#if MC>=11601
//$$ 		DrawableHelper.drawTexture((MatrixStack) matrices, width - 128, y + 24, 0.0F, 0.0F, 96, 96, 96, 96);
		//#else
		DrawableHelper.blit(width - 128, y + 24, 0.0F, 0.0F, 96, 96, 96, 96);
		//#endif

	}

}
