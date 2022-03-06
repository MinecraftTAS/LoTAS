package de.pfannekuchen.lotas.dropmanipulation.drops.blockdrops;

import java.util.List;

import com.google.common.collect.ImmutableList;

import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.gui.GuiDropChanceManipulation;
import de.pfannekuchen.lotas.gui.widgets.CheckboxWidget;
import de.pfannekuchen.lotas.gui.widgets.ModifiedCheckBoxWidget;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class OreDropManipulation extends GuiDropChanceManipulation.DropManipulation {

    public static ModifiedCheckBoxWidget optimizeLapis = new ModifiedCheckBoxWidget(999, 0, 0, "Full Lapis Drops", false);
    public static ModifiedCheckBoxWidget optimizeRedstone = new ModifiedCheckBoxWidget(999, 0, 0, "Full Redstone Drops", false);

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
        if (block.getDefaultState().getBlock() == MCVer.getBlock("LAPIS_ORE") && optimizeLapis.isChecked()) {
            return ImmutableList.of(new ItemStack(MCVer.getItem("DYE"), 9, 4));
        } else if (block.getDefaultState().getBlock() == MCVer.getBlock("REDSTONE_ORE") && optimizeRedstone.isChecked()) {
            return ImmutableList.of(new ItemStack(MCVer.getItem("REDSTONE"), 5));
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
        updateY(optimizeRedstone, 64);
        updateY(optimizeLapis, 80);
        updateX(optimizeRedstone, x);
        updateX(optimizeLapis, x);
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
            optimizeRedstone.drawButton(Minecraft.getMinecraft(), mouseX, mouseY, delta);
            optimizeLapis.drawButton(Minecraft.getMinecraft(), mouseX, mouseY, delta);
        }

        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("lotas", "drops/diamond_ore.png"));
        Gui.drawModalRectWithCustomSizedTexture(width - 128, y + 24, 0.0F, 0.0F, 96, 96, 96, 96);
    }

}
