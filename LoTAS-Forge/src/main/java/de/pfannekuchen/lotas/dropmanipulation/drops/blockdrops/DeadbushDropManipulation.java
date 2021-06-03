package de.pfannekuchen.lotas.dropmanipulation.drops.blockdrops;

import java.util.List;

import com.google.common.collect.ImmutableList;

import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.gui.GuiDropChanceManipulation;
import de.pfannekuchen.lotas.gui.widgets.ButtonWidget;
import de.pfannekuchen.lotas.gui.widgets.CheckboxWidget;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class DeadbushDropManipulation extends GuiDropChanceManipulation.DropManipulation {

    public static int sticks = 0;

    public static ButtonWidget drop0Stick = new ButtonWidget(x, y, 98, 20, "0 Sticks", button -> {
        press0Stick();
    });
    public static ButtonWidget drop1Stick = new ButtonWidget(x, y, 98, 20, "1 Sticks", button -> {
        press1Stick();
    });
    public static ButtonWidget drop2Stick = new ButtonWidget(x, y, 98, 20, "2 Sticks", button -> {
        press2Stick();
    });

    public static void press0Stick() {
        drop0Stick.enabled = false;
        drop1Stick.enabled = true;
        drop2Stick.enabled = true;
        sticks = 0;
    }

    public static void press1Stick() {
        drop0Stick.enabled = true;
        drop1Stick.enabled = false;
        drop2Stick.enabled = true;
        sticks = 1;
    }

    public static void press2Stick() {
        drop0Stick.enabled = true;
        drop1Stick.enabled = true;
        drop2Stick.enabled = false;
        sticks = 2;
    }

    public DeadbushDropManipulation(int x, int y, int width, int height) {
    	DeadbushDropManipulation.x = x;
        DeadbushDropManipulation.y = y;
        DeadbushDropManipulation.width = width;
        DeadbushDropManipulation.height = height;
        enabled = new CheckboxWidget(x, y, 150, 20, "Override Dead Bush Drops", false);
        drop0Stick.enabled = false;
    }

    @Override
    public String getName() {
        return "Dead Bush";
    }

    @Override
    public List<ItemStack> redirectDrops(IBlockState block) {
        if (block.getBlock().getDefaultState().getBlock() != MCVer.getBlock("DEADBUSH")) return ImmutableList.of();
        return ImmutableList.of(new ItemStack(MCVer.getItem("stick"), sticks));
    }

    @Override
    public List<ItemStack> redirectDrops(Entity entity) {
        return ImmutableList.of();
    }

    @Override
    public void update() {
        updateX(enabled, x);
        updateY(enabled, y);

        updateX(drop0Stick, x);
        updateY(drop0Stick, y + 96);
        updateX(drop1Stick, x);
        updateY(drop1Stick, y + 120);
        updateX(drop2Stick, x);
        updateY(drop2Stick, y + 144);

        drop0Stick.setWidth(width - x - 128 - 16);
        drop1Stick.setWidth(width - x - 128 - 16);
        drop2Stick.setWidth(width - x - 128 - 16);
    }

    @Override
    public void mouseAction(int mouseX, int mouseY, int button) {
        enabled.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY);
        if (enabled.isChecked()) {
            drop0Stick.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY);
            drop1Stick.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY);
            drop2Stick.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY);
        }
    }

    @Override
    public void render(int mouseX, int mouseY, float delta) {
        enabled.render(mouseX, mouseY, delta);

        if (!enabled.isChecked()) {
            GlStateManager.color(.5f, .5f, .5f, .4f);
        } else {
            MCVer.getFontRenderer(Minecraft.getMinecraft()).drawStringWithShadow("Drop " + sticks + " Sticks when breaking a Dead Bush", x, y + 64, 0xFFFFFF);
            drop2Stick.render(mouseX, mouseY, delta);
            drop1Stick.render(mouseX, mouseY, delta);
            drop0Stick.render(mouseX, mouseY, delta);
        }

        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("lotas", "drops/deadbush.png"));
        Gui.drawModalRectWithCustomSizedTexture(width - 128, y + 24, 0.0F, 0.0F, 96, 96, 96, 96);
    }

}
