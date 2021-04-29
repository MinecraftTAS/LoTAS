package de.pfannekuchen.lotas.drops.blockdrops;

import java.util.List;

import org.lwjgl.opengl.GL11;

import com.google.common.collect.ImmutableList;

import cpw.mods.fml.client.config.GuiCheckBox;
import de.pfannekuchen.lotas.gui.GuiLootManipulation;
import de.pfannekuchen.lotas.gui.parts.CheckboxWidget;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class PlantsDropManipulation extends GuiLootManipulation.DropManipulation {

    public static GuiCheckBox optimizeCarrots = new GuiCheckBox(999, 0, 0, "Optimize Carrot Drops", false);
    public static GuiCheckBox optimizePotato = new GuiCheckBox(999, 0, 0, "Optimize Potato Drops", false);
    public static GuiCheckBox optimizeWheat = new GuiCheckBox(999, 0, 0, "Optimize Wheat Drops", false);
    public static GuiCheckBox optimizeMelons = new GuiCheckBox(999, 0, 0, "Optimize Melon Drops", false);
    public static GuiCheckBox optimizeCocoa = new GuiCheckBox(999, 0, 0, "Optimize Cocoa Bean Drops", false);
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
    public List<ItemStack> redirectDrops(Block block) {
        if (block == Blocks.carrots && optimizeCarrots.isChecked()) {
            return ImmutableList.of(new ItemStack(Items.carrot, 5));
        } else if (block == Blocks.potatoes && optimizePotato.isChecked()) {
            return ImmutableList.of(new ItemStack(Items.potato, 5), new ItemStack(Items.poisonous_potato, 1));
        } else if (block == Blocks.wheat && optimizeWheat.isChecked()) {
            return ImmutableList.of(new ItemStack(Items.wheat_seeds, 3), new ItemStack(Items.wheat, 1));
        } else if (block == Blocks.melon_block && optimizeMelons.isChecked()) {
            return ImmutableList.of(new ItemStack(Items.melon, 7));
        } else if (block == Blocks.cocoa && optimizeCocoa.isChecked()) {
            return ImmutableList.of(new ItemStack(Items.dye, 3, 3));
        } else if (block == Blocks.brown_mushroom_block && optimizeMushroom.isChecked()) {
            return ImmutableList.of(new ItemStack(Blocks.brown_mushroom, 2));
        } else if (block == Blocks.red_mushroom_block && optimizeMushroom.isChecked()) {
            return ImmutableList.of(new ItemStack(Blocks.red_mushroom, 2));
        }  else if (block == Blocks.nether_wart && optimizeNetherwart.isChecked()) {
            return ImmutableList.of(new ItemStack(Items.nether_wart, 4));
        }
        return ImmutableList.of();
    }

    @Override
    public List<ItemStack> redirectDrops(Entity entity) {
        return ImmutableList.of();
    }

    @Override
    public void update() {
        enabled.xPosition = x;
        enabled.yPosition = y;
        optimizeCarrots.yPosition = 64;
        optimizeMelons.yPosition = 96;
        optimizeWheat.yPosition = 112;
        optimizePotato.yPosition = 128;
        optimizeCocoa.yPosition = 144;
        optimizeNetherwart.yPosition = 160;
        optimizeMushroom.yPosition = 80;
        optimizeCarrots.xPosition = x;
        optimizeMelons.xPosition = x;
        optimizeWheat.xPosition = x;
        optimizePotato.xPosition = x;
        optimizeCocoa.xPosition = x;
        optimizeNetherwart.xPosition = x;
        optimizeMushroom.xPosition = x;
    }

    @Override
    public void mouseAction(int mouseX, int mouseY, int button) {
        enabled.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY);
        if (enabled.isChecked()) {
            optimizeCarrots.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY);
            optimizeMelons.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY);
            optimizePotato.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY);
            optimizeWheat.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY);
            optimizeCocoa.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY);
            optimizeMushroom.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY);
            optimizeNetherwart.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY);
        }
    }

    @Override
    public void render(int mouseX, int mouseY, float delta) {
        enabled.drawButton(Minecraft.getMinecraft(), mouseX, mouseY, delta);

        if (!enabled.isChecked()) {
            GL11.glColor4f(.5f, .5f, .5f, .4f);
        } else {
            optimizeCarrots.drawButton(Minecraft.getMinecraft(), mouseX, mouseY);
            optimizeMelons.drawButton(Minecraft.getMinecraft(), mouseX, mouseY);
            optimizePotato.drawButton(Minecraft.getMinecraft(), mouseX, mouseY);
            optimizeWheat.drawButton(Minecraft.getMinecraft(), mouseX, mouseY);
            optimizeCocoa.drawButton(Minecraft.getMinecraft(), mouseX, mouseY);
            optimizeMushroom.drawButton(Minecraft.getMinecraft(), mouseX, mouseY);
            optimizeNetherwart.drawButton(Minecraft.getMinecraft(), mouseX, mouseY);
        }

        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("lotas", "drops/plants.png"));
        Gui.drawModalRectWithCustomSizedTexture(width - 128, y + 24, 0.0F, 0.0F, 96, 96, 96, 96);
    }

}
