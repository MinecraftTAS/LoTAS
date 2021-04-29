package de.pfannekuchen.lotas.drops.blockdrops;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.platform.GlStateManager;

import de.pfannekuchen.lotas.gui.LootManipulationScreen;
import de.pfannekuchen.lotas.gui.widgets.SmallCheckboxWidget;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

public class OreDropManipulation extends LootManipulationScreen.DropManipulation {

    public static SmallCheckboxWidget optimizeLapis = new SmallCheckboxWidget(0, 0, "Full Lapis Drops", false);
    public static SmallCheckboxWidget optimizeRedstone = new SmallCheckboxWidget(0, 0, "Full Redstone Drops", false);

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
    public List<ItemStack> redirectDrops(BlockState blockstate) {
        Block block = blockstate.getBlock();
        if (block.getDefaultState().getBlock() == Blocks.LAPIS_ORE && optimizeLapis.isChecked()) {
            return ImmutableList.of(new ItemStack(Items.LAPIS_LAZULI, 9));
        } else if (block.getDefaultState().getBlock() == Blocks.REDSTONE_ORE && optimizeRedstone.isChecked()) {
            return ImmutableList.of(new ItemStack(Items.REDSTONE, 5));
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
        optimizeRedstone.y = 64;
        optimizeLapis.y = 80;
        optimizeRedstone.x = x;
        optimizeLapis.x = x;
    }

    @Override
    public void mouseAction(double mouseX, double mouseY, int button) {
        enabled.mouseClicked(mouseX, mouseY, button);
        if (enabled.isChecked()) {
            optimizeRedstone.mouseClicked(mouseX, mouseY, button);
            optimizeLapis.mouseClicked(mouseX, mouseY, button);
        }
    }

    @Override
    public void render(int mouseX, int mouseY, float delta) {
        enabled.render(mouseX, mouseY, delta);

        if (!enabled.isChecked()) {
            GlStateManager.color4f(.5f, .5f, .5f, .4f);
        } else {
            optimizeRedstone.render(mouseX, mouseY, delta);
            optimizeLapis.render(mouseX, mouseY, delta);
        }

        MinecraftClient.getInstance().getTextureManager().bindTexture(new Identifier("lotas", "drops/diamond_ore.png"));
        DrawableHelper.blit(width - 128, y + 24, 0.0F, 0.0F, 96, 96, 96, 96);
    }

}
