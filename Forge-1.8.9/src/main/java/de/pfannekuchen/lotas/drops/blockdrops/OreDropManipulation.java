package de.pfannekuchen.lotas.drops.blockdrops;

import java.util.List;

import com.google.common.collect.ImmutableList;

import de.pfannekuchen.lotas.gui.GuiLootManipulation;
import de.pfannekuchen.lotas.gui.parts.CheckboxWidget;
import net.minecraft.block.Block;
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

public class OreDropManipulation extends GuiLootManipulation.DropManipulation {

    public static GuiCheckBox optimizeLapis = new GuiCheckBox(999, 0, 0, "Full Lapis Drops", false);
    public static GuiCheckBox optimizeRedstone = new GuiCheckBox(999, 0, 0, "Full Redstone Drops", false);

    public OreDropManipulation(int x, int y, int width, int height) {
    	OreDropManipulation.x = x;
    	OreDropManipulation.y = y;
    	OreDropManipulation.width = width;
    	OreDropManipulation.height = height;
        enabled = new CheckboxWidget(x, y, 150, 20, "Override Ore Drops", false);
    }

    @Override
    public String getName() {
        return "Ores";
    }

    @Override
    public List<ItemStack> redirectDrops(IBlockState blockstate) {
        Block block = blockstate.getBlock();
        if (block.getDefaultState().getBlock() == Blocks.lapis_ore && optimizeLapis.isChecked()) {
            return ImmutableList.of(new ItemStack(Items.dye, 9, 4));
        } else if (block.getDefaultState().getBlock() == Blocks.redstone_ore && optimizeRedstone.isChecked()) {
            return ImmutableList.of(new ItemStack(Items.redstone, 5));
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
        optimizeRedstone.yPosition = 64;
        optimizeLapis.yPosition = 80;
        optimizeRedstone.xPosition = x;
        optimizeLapis.xPosition = x;
    }

    @Override
    public void mouseAction(int mouseX, int mouseY, int button) {
        enabled.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY);
        if (enabled.isChecked()) {
            optimizeRedstone.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY);
            optimizeLapis.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY);
        }
    }

    @Override
    public void render(int mouseX, int mouseY, float delta) {
        enabled.render(mouseX, mouseY, delta);

        if (!enabled.isChecked()) {
            GlStateManager.color(.5f, .5f, .5f, .4f);
        } else {
            optimizeRedstone.drawButton(Minecraft.getMinecraft(), mouseX, mouseY);
            optimizeLapis.drawButton(Minecraft.getMinecraft(), mouseX, mouseY);
        }

        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("lotas", "drops/diamond_ore.png"));
        Gui.drawModalRectWithCustomSizedTexture(width - 128, y + 24, 0.0F, 0.0F, 96, 96, 96, 96);
    }

}
