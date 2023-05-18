package de.pfannekuchen.lotas.dropmanipulation.drops.blockdrops;

import java.util.List;

import com.google.common.collect.ImmutableList;

import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.gui.GuiDropManipulation;
import de.pfannekuchen.lotas.gui.widgets.CheckboxWidget;
import de.pfannekuchen.lotas.gui.widgets.ModifiedCheckBoxWidget;
import net.minecraft.block.Block;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class PlantsDropManipulation extends GuiDropManipulation.DropManipulation {

    public static ModifiedCheckBoxWidget optimizeCarrots = new ModifiedCheckBoxWidget(999, 0, 0, I18n.format("dropmanipgui.lotas.blocks.plants.carrot"), false);
    public static ModifiedCheckBoxWidget optimizePotato = new ModifiedCheckBoxWidget(999, 0, 0, I18n.format("dropmanipgui.lotas.blocks.plants.potato"), false);
    public static ModifiedCheckBoxWidget optimizeWheat = new ModifiedCheckBoxWidget(999, 0, 0, I18n.format("dropmanipgui.lotas.blocks.plants.wheat"), false);
    public static ModifiedCheckBoxWidget optimizeBeetroot = new ModifiedCheckBoxWidget(999, 0, 0, I18n.format("dropmanipgui.lotas.blocks.plants.beetroot"), false);
    public static ModifiedCheckBoxWidget optimizeMelons = new ModifiedCheckBoxWidget(999, 0, 0, I18n.format("dropmanipgui.lotas.blocks.plants.melon"), false);
    public static ModifiedCheckBoxWidget optimizeCocoa = new ModifiedCheckBoxWidget(999, 0, 0, I18n.format("dropmanipgui.lotas.blocks.plants.cocoa"), false);
    public static ModifiedCheckBoxWidget optimizeChorus = new ModifiedCheckBoxWidget(999, 0, 0, I18n.format("dropmanipgui.lotas.blocks.plants.chorus"), false);
    public static ModifiedCheckBoxWidget optimizeNetherwart = new ModifiedCheckBoxWidget(999, 0, 0, I18n.format("dropmanipgui.lotas.blocks.plants.netherwart"), false);
    public static ModifiedCheckBoxWidget optimizeMushroom = new ModifiedCheckBoxWidget(999, 0, 0, I18n.format("dropmanipgui.lotas.blocks.plants.mushroom"), false);

    public PlantsDropManipulation(int x, int y, int width, int height) {
    	PlantsDropManipulation.x = x;
        PlantsDropManipulation.y = y;
        PlantsDropManipulation.width = width;
        PlantsDropManipulation.height = height;
        enabled = new CheckboxWidget(x, y, 150, 20, I18n.format("dropmanipgui.lotas.blocks.plants.override"), false);
    }

    @Override
    public String getName() {
        return I18n.format("dropmanipgui.lotas.blocks.plants.name");
    }

    @Override
    public List<ItemStack> redirectDrops(IBlockState blockstate) {
        Block block = blockstate.getBlock();
        if (block.getDefaultState().getBlock() == MCVer.getBlock("CARROTS") && optimizeCarrots.isChecked()) {
            if (blockstate.getValue(PropertyInteger.create("age", 0, 7)) == 7) return ImmutableList.of(new ItemStack(MCVer.getItem("CARROTS"), 5));
        } else if (block.getDefaultState().getBlock() == MCVer.getBlock("POTATOES") && optimizePotato.isChecked()) {
            if (blockstate.getValue(PropertyInteger.create("age", 0, 7)) == 7) return ImmutableList.of(new ItemStack(MCVer.getItem("POTATOES"), 5), new ItemStack(MCVer.getItem("POISONOUS_POTATO"), 1));
        } else if (block.getDefaultState().getBlock() == MCVer.getBlock("WHEAT") && optimizeWheat.isChecked()) {
            if (blockstate.getValue(PropertyInteger.create("age", 0, 7)) == 7) return ImmutableList.of(new ItemStack(MCVer.getItem("WHEAT_SEEDS"), 3), new ItemStack(MCVer.getItem("WHEAT"), 1));
        //#if MC>=10900
        } else if (block.getDefaultState().getBlock() == Blocks.BEETROOTS && optimizeBeetroot.isChecked()) {
        	if (blockstate.getValue(PropertyInteger.create("age", 0, 3)) == 3) return ImmutableList.of(new ItemStack(Items.BEETROOT, 1), new ItemStack(Items.BEETROOT_SEEDS, 4));
        } else if (block.getDefaultState().getBlock() == Blocks.CHORUS_PLANT && optimizeChorus.isChecked()) {
        	return ImmutableList.of(new ItemStack(Items.CHORUS_FRUIT, 1));
        //#endif
        } else if (block.getDefaultState().getBlock() == MCVer.getBlock("MELON_BLOCK") && optimizeMelons.isChecked()) {
            return ImmutableList.of(new ItemStack(MCVer.getItem("MELON"), 7));
        } else if (block.getDefaultState().getBlock() == MCVer.getBlock("COCOA") && optimizeCocoa.isChecked()) {
            if (blockstate.getValue(PropertyInteger.create("age", 0, 2)) == 2) return ImmutableList.of(new ItemStack(MCVer.getItem("DYE"), 3, 3));
        } else if (block.getDefaultState().getBlock() == MCVer.getBlock("BROWN_MUSHROOM_BLOCK") && optimizeMushroom.isChecked()) {
            return ImmutableList.of(new ItemStack(MCVer.getBlock("BROWN_MUSHROOM"), 2));
        } else if (block.getDefaultState().getBlock() == MCVer.getBlock("RED_MUSHROOM_BLOCK") && optimizeMushroom.isChecked()) {
            return ImmutableList.of(new ItemStack(MCVer.getBlock("RED_MUSHROOM"), 2));
        }  else if (block.getDefaultState().getBlock() == MCVer.getBlock("NETHER_WART") && optimizeNetherwart.isChecked()) {
            if (blockstate.getValue(PropertyInteger.create("age", 0, 3)) == 3) return ImmutableList.of(new ItemStack(MCVer.getItem("NETHER_WART"), 4));
        }
        return ImmutableList.of();
    }

    @Override
    public List<ItemStack> redirectDrops(Entity entity, int lootingValue) {
        return ImmutableList.of();
    }

    @Override
    public void update() {
        updateX(enabled, x);
        updateY(enabled, y);
        updateY(optimizeCarrots, 64);
        updateY(optimizeCarrots, 64);
        updateY(optimizeBeetroot, 80);
        updateY(optimizeMelons, 96);
        updateY(optimizeWheat, 112);
        updateY(optimizePotato, 128);
        updateY(optimizeCocoa, 144);
        updateY(optimizeChorus, 160);
        updateY(optimizeNetherwart, 176);
        updateY(optimizeMushroom, 192);
        updateX(optimizeCarrots, x);
        updateX(optimizeBeetroot, x);
        updateX(optimizeMelons, x);
        updateX(optimizeWheat, x);
        updateX(optimizePotato, x);
        updateX(optimizeCocoa, x);
        updateX(optimizeChorus, x);
        updateX(optimizeNetherwart, x);
        updateX(optimizeMushroom, x);
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
