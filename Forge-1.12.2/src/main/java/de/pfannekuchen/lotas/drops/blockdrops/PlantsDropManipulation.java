package de.pfannekuchen.lotas.drops.blockdrops;

import java.util.List;

import com.google.common.collect.ImmutableList;

import de.pfannekuchen.lotas.gui.GuiLootManipulation;
import de.pfannekuchen.lotas.gui.parts.CheckboxWidget;
import net.minecraft.block.Block;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiCheckBox;

public class PlantsDropManipulation extends GuiLootManipulation.DropManipulation {

    public static GuiCheckBox optimizeCarrots = new GuiCheckBox(999, 0, 0, "Optimize Carrot Drops", false);
    public static GuiCheckBox optimizePotato = new GuiCheckBox(999, 0, 0, "Optimize Potato Drops", false);
    public static GuiCheckBox optimizeWheat = new GuiCheckBox(999, 0, 0, "Optimize Wheat Drops", false);
    public static GuiCheckBox optimizeBeetroot = new GuiCheckBox(999, 0, 0, "Optimize Beetroot Drops", false);
    public static GuiCheckBox optimizeMelons = new GuiCheckBox(999, 0, 0, "Optimize Melon Drops", false);
    public static GuiCheckBox optimizeCocoa = new GuiCheckBox(999, 0, 0, "Optimize Cocoa Bean Drops", false);
    public static GuiCheckBox optimizeChorus = new GuiCheckBox(999, 0, 0, "Optimize Chorus Plant Drops", false);
    public static GuiCheckBox optimizeNetherwart = new GuiCheckBox(999, 0, 0, "Optimize Nether Wart Drops", false);
    public static GuiCheckBox optimizeMushroom = new GuiCheckBox(999, 0, 0, "Optimize Mushroom Block Drops", false);

    public PlantsDropManipulation(int x, int y, int width, int height) {
    	PlantsDropManipulation.x = x;
        PlantsDropManipulation.y = y;
        PlantsDropManipulation.width = width;
        PlantsDropManipulation.height = height;
        enabled = new CheckboxWidget(x, y, 150, 20, "Override Plant Drops", false);
    }

    @Override
    public String getName() {
        return "Plants";
    }

    @Override
    public List<ItemStack> redirectDrops(IBlockState blockstate) {
        Block block = blockstate.getBlock();
        if (block.getDefaultState().getBlock() == Blocks.CARROTS && optimizeCarrots.isChecked()) {
            if (blockstate.getValue(PropertyInteger.create("age", 0, 7)) == 7) return ImmutableList.of(new ItemStack(Items.CARROT, 5));
        } else if (block.getDefaultState().getBlock() == Blocks.POTATOES && optimizePotato.isChecked()) {
            if (blockstate.getValue(PropertyInteger.create("age", 0, 7)) == 7) return ImmutableList.of(new ItemStack(Items.POTATO, 5), new ItemStack(Items.POISONOUS_POTATO, 1));
        } else if (block.getDefaultState().getBlock() == Blocks.WHEAT && optimizeWheat.isChecked()) {
            if (blockstate.getValue(PropertyInteger.create("age", 0, 7)) == 7) return ImmutableList.of(new ItemStack(Items.WHEAT_SEEDS, 3), new ItemStack(Items.WHEAT, 1));
        } else if (block.getDefaultState().getBlock() == Blocks.BEETROOTS && optimizeBeetroot.isChecked()) {
            if (blockstate.getValue(PropertyInteger.create("age", 0, 3)) == 3) return ImmutableList.of(new ItemStack(Items.BEETROOT, 1), new ItemStack(Items.BEETROOT_SEEDS, 4));
        } else if (block.getDefaultState().getBlock() == Blocks.MELON_BLOCK && optimizeMelons.isChecked()) {
            return ImmutableList.of(new ItemStack(Items.MELON, 7));
        } else if (block.getDefaultState().getBlock() == Blocks.COCOA && optimizeCocoa.isChecked()) {
            if (blockstate.getValue(PropertyInteger.create("age", 0, 2)) == 2) return ImmutableList.of(new ItemStack(Items.DYE, 3, 3));
        } else if (block.getDefaultState().getBlock() == Blocks.CHORUS_PLANT && optimizeChorus.isChecked()) {
            return ImmutableList.of(new ItemStack(Items.CHORUS_FRUIT, 1));
        } else if (block.getDefaultState().getBlock() == Blocks.BROWN_MUSHROOM_BLOCK && optimizeMushroom.isChecked()) {
            return ImmutableList.of(new ItemStack(Blocks.BROWN_MUSHROOM, 2));
        } else if (block.getDefaultState().getBlock() == Blocks.RED_MUSHROOM_BLOCK && optimizeMushroom.isChecked()) {
            return ImmutableList.of(new ItemStack(Blocks.RED_MUSHROOM, 2));
        }  else if (block.getDefaultState().getBlock() == Blocks.NETHER_WART && optimizeNetherwart.isChecked()) {
            if (blockstate.getValue(PropertyInteger.create("age", 0, 3)) == 3) return ImmutableList.of(new ItemStack(Items.NETHER_WART, 4));
        }
        return ImmutableList.of();
    }

    @Override
    public List<ItemStack> redirectDrops(Entity entity) {
        return ImmutableList.of();
    }

    @Override
    public void update() {
        enabled.x = x;
        enabled.y = y;
        optimizeCarrots.y = 64;
        optimizeBeetroot.y = 80;
        optimizeMelons.y = 96;
        optimizeWheat.y = 112;
        optimizePotato.y = 128;
        optimizeCocoa.y = 144;
        optimizeChorus.y = 160;
        optimizeNetherwart.y = 176;
        optimizeMushroom.y = 192;
        optimizeCarrots.x = x;
        optimizeBeetroot.x = x;
        optimizeMelons.x = x;
        optimizeWheat.x = x;
        optimizePotato.x = x;
        optimizeCocoa.x = x;
        optimizeChorus.x = x;
        optimizeNetherwart.x = x;
        optimizeMushroom.x = x;
    }

    @Override
    public void mouseAction(int mouseX, int mouseY, int button) {
        enabled.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY);
        if (enabled.isChecked()) {
            optimizeCarrots.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY);
            optimizeBeetroot.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY);
            optimizeMelons.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY);
            optimizePotato.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY);
            optimizeWheat.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY);
            optimizeCocoa.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY);
            optimizeChorus.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY);
            optimizeMushroom.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY);
            optimizeNetherwart.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY);
        }
    }

    @Override
    public void render(int mouseX, int mouseY, float delta) {
        enabled.drawButton(Minecraft.getMinecraft(), mouseX, mouseY, delta);

        if (!enabled.isChecked()) {
            GlStateManager.color(.5f, .5f, .5f, .4f);
        } else {
            optimizeCarrots.drawButton(Minecraft.getMinecraft(), mouseX, mouseY, delta);
            optimizeBeetroot.drawButton(Minecraft.getMinecraft(), mouseX, mouseY, delta);
            optimizeMelons.drawButton(Minecraft.getMinecraft(), mouseX, mouseY, delta);
            optimizePotato.drawButton(Minecraft.getMinecraft(), mouseX, mouseY, delta);
            optimizeWheat.drawButton(Minecraft.getMinecraft(), mouseX, mouseY, delta);
            optimizeCocoa.drawButton(Minecraft.getMinecraft(), mouseX, mouseY, delta);
            optimizeChorus.drawButton(Minecraft.getMinecraft(), mouseX, mouseY, delta);
            optimizeMushroom.drawButton(Minecraft.getMinecraft(), mouseX, mouseY, delta);
            optimizeNetherwart.drawButton(Minecraft.getMinecraft(), mouseX, mouseY, delta);
        }

        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("lotas", "drops/plants.png"));
        Gui.drawModalRectWithCustomSizedTexture(width - 128, y + 24, 0.0F, 0.0F, 96, 96, 96, 96);
    }

}
