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

public class SealanternDropManipulation extends DropManipulationScreen.DropManipulation {

	public static int pris = 2;

	public static ButtonWidget drop2Pris = new NewButtonWidget(x, y, 98, 20, "2 Prismarine Crystals", button -> {
		press2pris();
	});
	public static ButtonWidget drop3Pris = new NewButtonWidget(x, y, 98, 20, "3 Prismarine Crystals", button -> {
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
		//#if MC>=11601
//$$ 		enabled = new CheckboxWidget(x, y, 150, 20, new LiteralText("Override Sea Lantern Drops"), false);
		//#else
		enabled = new CheckboxWidget(x, y, 150, 20, "Override Sea Lantern Drops", false);
		//#endif
		drop2Pris.active = false;
	}

	@Override
	public String getName() {
		return "Sea Lantern";
	}

	@Override
	public List<ItemStack> redirectDrops(BlockState block) {
		if (block.getBlock().getDefaultState().getBlock() != Blocks.SEA_LANTERN)
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
		if (enabled.isChecked()) {
			drop2Pris.mouseClicked(mouseX, mouseY, button);
			drop3Pris.mouseClicked(mouseX, mouseY, button);
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
//$$ 			MinecraftClient.getInstance().textRenderer.drawWithShadow((MatrixStack) matrices, "Drop " + pris + " Prismarine Crystals when breaking Sea Lanterns", x, y + 64, 0xFFFFFF);
//$$ 			drop2Pris.render((MatrixStack) matrices, mouseX, mouseY, delta);
//$$ 			drop3Pris.render((MatrixStack) matrices, mouseX, mouseY, delta);
			//#else
			MinecraftClient.getInstance().textRenderer.drawWithShadow("Drop " + pris + " Prismarine Crystals when breaking Sea Lanterns", x, y + 64, 0xFFFFFF);
			drop2Pris.render(mouseX, mouseY, delta);
			drop3Pris.render(mouseX, mouseY, delta);
			//#endif
		}

		MinecraftClient.getInstance().getTextureManager().bindTexture(new Identifier("lotas", "drops/sealantern.gif"));
		//#if MC>=11601
//$$ 		DrawableHelper.drawTexture((MatrixStack) matrices, width - 128, y + 24, 0.0F, 0.0F, 96, 96, 96, 96);
		//#else
		DrawableHelper.blit(width - 128, y + 24, 0.0F, 0.0F, 96, 96, 96, 96);
		//#endif
	}

}
