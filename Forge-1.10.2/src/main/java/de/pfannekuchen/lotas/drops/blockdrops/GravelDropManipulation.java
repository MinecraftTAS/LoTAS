package de.pfannekuchen.lotas.drops.blockdrops;

import java.util.List;

import com.google.common.collect.ImmutableList;

import de.pfannekuchen.lotas.gui.GuiLootManipulation;
import de.pfannekuchen.lotas.gui.parts.ButtonWidget;
import de.pfannekuchen.lotas.gui.parts.CheckboxWidget;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class GravelDropManipulation extends GuiLootManipulation.DropManipulation {

    public static boolean flint = false;

    public static ButtonWidget dropGravel = new ButtonWidget(x, y, 98, 20, "Gravel", button -> {
        pressGravel();
    });
    public static ButtonWidget dropFlint = new ButtonWidget(x, y, 98, 20, "Flint", button -> {
        pressFlint();
    });

    public static void pressGravel() {
        dropGravel.enabled = false;
        dropFlint.enabled = true;
        flint = false;
    }

    public static void pressFlint() {
        dropFlint.enabled = false;
        dropGravel.enabled = true;
        flint = true;
    }

    public GravelDropManipulation(int x, int y, int width, int height) {
    	GravelDropManipulation.x = x;
    	GravelDropManipulation.y = y;
    	GravelDropManipulation.width = width;
        GravelDropManipulation.height = height;
        enabled = new CheckboxWidget(x, y, 150, 20, "Override Gravel Drops", false);
        dropGravel.enabled = false;
    }

    @Override
    public String getName() {
        return "Gravel";
    }

    @Override
    public List<ItemStack> redirectDrops(IBlockState block) {
        if (block.getBlock().getDefaultState().getBlock() != Blocks.GRAVEL) return ImmutableList.of();
        if (flint) {
            return ImmutableList.of(new ItemStack(Items.FLINT));
        }
        return ImmutableList.of(new ItemStack(Blocks.GRAVEL));
    }

    @Override
    public List<ItemStack> redirectDrops(Entity entity) {
        return ImmutableList.of();
    }

    @Override
    public void update() {
        enabled.xPosition = x;
        enabled.yPosition = y;
        dropGravel.xPosition = x;
        dropGravel.yPosition = y + 96;
        dropFlint.xPosition = x;
        dropFlint.yPosition = y + 120;
        dropGravel.setWidth(width - x - 128 - 16);
        dropFlint.setWidth(width - x - 128 - 16);
    }

    @Override
    public void mouseAction(int mouseX, int mouseY, int button) {
        enabled.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY);
        if (enabled.isChecked()) {
            dropGravel.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY);
            dropFlint.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY);
        }
    }

    @Override
    public void render(int mouseX, int mouseY, float delta) {
        enabled.render(mouseX, mouseY, delta);

        if (!enabled.isChecked()) {
            GlStateManager.color(.5f, .5f, .5f, .4f);
        } else {
            Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow("Drop " + (flint ? "Flint" : "Gravel") + " when breaking Gravel", x, y + 64, 0xFFFFFF);
            dropGravel.render(mouseX, mouseY, delta);
            dropFlint.render(mouseX, mouseY, delta);
        }

        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("lotas", "drops/gravel.png"));
        Gui.drawModalRectWithCustomSizedTexture(width - 128, y + 24, 0.0F, 0.0F, 96, 96, 96, 96);
    }

}
