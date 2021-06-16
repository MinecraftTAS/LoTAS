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

public class GravelDropManipulation extends DropManipulationScreen.DropManipulation {

    public static boolean flint = false;

    public static ButtonWidget dropGravel = new NewButtonWidget(x, y, 98, 20, "Gravel", button -> {
        pressGravel();
    });
    public static ButtonWidget dropFlint = new NewButtonWidget(x, y, 98, 20, "Flint", button -> {
        pressFlint();
    });

    public static void pressGravel() {
        dropGravel.active = false;
        dropFlint.active = true;
        flint = false;
    }

    public static void pressFlint() {
        dropFlint.active = false;
        dropGravel.active = true;
        flint = true;
    }

    public GravelDropManipulation(int x, int y, int width, int height) {
    	GravelDropManipulation.x = x;
    	GravelDropManipulation.y = y;
    	GravelDropManipulation.width = width;
        GravelDropManipulation.height = height;
        //#if MC>=11601
        //$$ enabled = new CheckboxWidget(x, y, 150, 20, new LiteralText("Override Gravel Drops"), false);
        //#else
        enabled = new CheckboxWidget(x, y, 150, 20, "Override Gravel Drops", false);
        //#endif
        dropGravel.active = false;
    }

    @Override
    public String getName() {
        return "Gravel";
    }

    @Override
    public List<ItemStack> redirectDrops(BlockState block) {
        if (block.getBlock().getDefaultState().getBlock() != Blocks.GRAVEL) return ImmutableList.of();
        if (flint) {
            return ImmutableList.of(new ItemStack(Items.FLINT));
        }
        return ImmutableList.of(new ItemStack(Items.GRAVEL));
    }

    @Override
    public List<ItemStack> redirectDrops(Entity entity) {
        return ImmutableList.of();
    }

    @Override
    public void update() {
        enabled.x = x;
        enabled.y = y;
        dropGravel.x = x;
        dropGravel.y = y + 96;
        dropFlint.x = x;
        dropFlint.y = y + 120;
        dropGravel.setWidth(width - x - 128 - 16);
        dropFlint.setWidth(width - x - 128 - 16);
    }

    @Override
    public void mouseAction(double mouseX, double mouseY, int button) {
        enabled.mouseClicked(mouseX, mouseY, button);
        if (enabled.isChecked()) {
            dropGravel.mouseClicked(mouseX, mouseY, button);
            dropFlint.mouseClicked(mouseX, mouseY, button);
        }
    }

    @Override
    public void render(Object matrices, int mouseX, int mouseY, float delta) {
        //#if MC>=11601
        //$$ enabled.render((MatrixStack) matrices, mouseX, mouseY, delta);
        //#else
        enabled.render(mouseX, mouseY, delta);
        //#endif

        if (!enabled.isChecked()) {
            GlStateManager.color4f(.5f, .5f, .5f, .4f);
        } else {
            //#if MC>=11601
            //$$ MinecraftClient.getInstance().textRenderer.drawWithShadow((MatrixStack) matrices, "Drop " + (flint ? "Flint" : "Gravel") + " when breaking Gravel", x, y + 64, 0xFFFFFF);
            //$$ dropGravel.render((MatrixStack) matrices, mouseX, mouseY, delta);
            //$$ dropFlint.render((MatrixStack) matrices, mouseX, mouseY, delta);
            //#else
            MinecraftClient.getInstance().textRenderer.drawWithShadow("Drop " + (flint ? "Flint" : "Gravel") + " when breaking Gravel", x, y + 64, 0xFFFFFF);
            dropGravel.render(mouseX, mouseY, delta);
            dropFlint.render(mouseX, mouseY, delta);
            //#endif
        }

        MinecraftClient.getInstance().getTextureManager().bindTexture(new Identifier("lotas", "drops/gravel.png"));
        //#if MC>=11601
        //$$ DrawableHelper.drawTexture((MatrixStack) matrices, width - 128, y + 24, 0.0F, 0.0F, 96, 96, 96, 96);
        //#else
        DrawableHelper.blit(width - 128, y + 24, 0.0F, 0.0F, 96, 96, 96, 96);
        //#endif
    }

}
