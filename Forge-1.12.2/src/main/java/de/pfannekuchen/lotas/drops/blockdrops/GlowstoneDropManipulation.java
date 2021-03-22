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

public class GlowstoneDropManipulation extends GuiLootManipulation.DropManipulation {

    public static int dust = 2;

    public static ButtonWidget drop2Glowstonedust = new ButtonWidget(x, y, 98, 20, "2 Glowstone Dust", button -> {
        press2Glowstonedust();
    });
    public static ButtonWidget drop3Glowstonedust = new ButtonWidget(x, y, 98, 20, "3 Glowstone Dust", button -> {
        press3Glowstonedust();
    });
    public static ButtonWidget drop4Glowstonedust = new ButtonWidget(x, y, 98, 20, "4 Glowstone Dust", button -> {
        press4Glowstonedust();
    });

    public static void press2Glowstonedust() {
        drop2Glowstonedust.enabled = false;
        drop3Glowstonedust.enabled = true;
        drop4Glowstonedust.enabled = true;
        dust = 2;
    }

    public static void press3Glowstonedust() {
        drop2Glowstonedust.enabled = true;
        drop3Glowstonedust.enabled = false;
        drop4Glowstonedust.enabled = true;
        dust = 3;
    }

    public static void press4Glowstonedust() {
        drop2Glowstonedust.enabled = true;
        drop3Glowstonedust.enabled = true;
        drop4Glowstonedust.enabled = false;
        dust = 4;
    }

    public GlowstoneDropManipulation(int x, int y, int width, int height) {
    	GlowstoneDropManipulation.x = x;
        GlowstoneDropManipulation.y = y;
        GlowstoneDropManipulation.width = width;
        GlowstoneDropManipulation.height = height;
        enabled = new CheckboxWidget(x, y, 150, 20, "Override Glowstone Drops", false);
        drop2Glowstonedust.enabled = false;
    }

    @Override
    public String getName() {
        return "Glowstone";
    }

    @Override
    public List<ItemStack> redirectDrops(IBlockState block) {
        if (block.getBlock().getDefaultState().getBlock() != Blocks.GLOWSTONE) return ImmutableList.of();
        return ImmutableList.of(new ItemStack(Items.GLOWSTONE_DUST, dust));
    }

    @Override
    public List<ItemStack> redirectDrops(Entity entity) {
        return ImmutableList.of();
    }

    @Override
    public void update() {
        enabled.x = x;
        enabled.y = y;

        drop2Glowstonedust.x = x;
        drop2Glowstonedust.y = y + 96;
        drop3Glowstonedust.x = x;
        drop3Glowstonedust.y = y + 120;
        drop4Glowstonedust.x = x;
        drop4Glowstonedust.y = y + 144;

        drop2Glowstonedust.setWidth(width - x - 128 - 16);
        drop3Glowstonedust.setWidth(width - x - 128 - 16);
        drop4Glowstonedust.setWidth(width - x - 128 - 16);
    }

    @Override
    public void mouseAction(int mouseX, int mouseY, int button) {
        enabled.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY);
        if (enabled.isChecked()) {
            drop2Glowstonedust.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY);
            drop3Glowstonedust.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY);
            drop4Glowstonedust.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY);
        }
    }

    @Override
    public void render(int mouseX, int mouseY, float delta) {
        enabled.render(mouseX, mouseY, delta);

        if (!enabled.isChecked()) {
            GlStateManager.color(.5f, .5f, .5f, .4f);
        } else {
            Minecraft.getMinecraft().fontRenderer.drawStringWithShadow("Drop " + dust + " Glowstone Dust when breaking Glowstone", x, y + 64, 0xFFFFFF);
            drop4Glowstonedust.render(mouseX, mouseY, delta);
            drop3Glowstonedust.render(mouseX, mouseY, delta);
            drop2Glowstonedust.render(mouseX, mouseY, delta);
        }

        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("lotas", "drops/glowstone.png"));
        Gui.drawModalRectWithCustomSizedTexture(width - 128, y + 24, 0.0F, 0.0F, 96, 96, 96, 96);
    }

}
