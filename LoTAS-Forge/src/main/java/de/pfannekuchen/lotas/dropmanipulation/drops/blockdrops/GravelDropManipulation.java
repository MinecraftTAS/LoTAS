package de.pfannekuchen.lotas.dropmanipulation.drops.blockdrops;

import java.util.List;

import com.google.common.collect.ImmutableList;

import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.gui.GuiDropManipulation;
import de.pfannekuchen.lotas.gui.widgets.ButtonWidget;
import de.pfannekuchen.lotas.gui.widgets.CheckboxWidget;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class GravelDropManipulation extends GuiDropManipulation.DropManipulation {

    public static boolean flint = false;

    public static ButtonWidget dropGravel = new ButtonWidget(x, y, 98, 20, I18n.format("dropmanipgui.lotas.blocks.gravel.gravel"), button -> {
        pressGravel();
    });
    public static ButtonWidget dropFlint = new ButtonWidget(x, y, 98, 20, I18n.format("dropmanipgui.lotas.blocks.gravel.flint"), button -> {
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
        enabled = new CheckboxWidget(x, y, 150, 20, I18n.format("dropmanipgui.lotas.blocks.gravel.override"), false);
        dropGravel.enabled = false;
    }

    @Override
    public String getName() {
        return I18n.format("dropmanipgui.lotas.blocks.gravel.name");
    }

    @Override
    public List<ItemStack> redirectDrops(IBlockState block) {
        if (block.getBlock().getDefaultState().getBlock() != MCVer.getBlock("GRAVEL")) return ImmutableList.of();
        if (flint) {
            return ImmutableList.of(new ItemStack(MCVer.getItem("FLINT")));
        }
        return ImmutableList.of(new ItemStack(MCVer.getBlock("GRAVEL")));
    }

    @Override
    public List<ItemStack> redirectDrops(Entity entity, int lootingValue) {
        return ImmutableList.of();
    }

    @Override
    public void update() {
    	updateX(enabled, x);
    	updateY(enabled, y);
        updateX(dropGravel, x);
        updateY(dropGravel, y + 96);
        updateX(dropFlint, x);
        updateY(dropFlint, y + 120);
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
            MCVer.getFontRenderer(Minecraft.getMinecraft()).drawStringWithShadow(I18n.format("dropmanipgui.lotas.blocks.gravel.description", (flint ? I18n.format("dropmanipgui.lotas.blocks.gravel.flint") : I18n.format("dropmanipgui.lotas.blocks.gravel.gravel"))), x, y + 64, 0xFFFFFF);
            dropGravel.render(mouseX, mouseY, delta);
            dropFlint.render(mouseX, mouseY, delta);
            GlStateManager.color(1f, 1f, 1f, 1f);
        }
        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("lotas", "drops/gravel.png"));
        Gui.drawModalRectWithCustomSizedTexture(width - 128, y + 24, 0.0F, 0.0F, 96, 96, 96, 96);
    }

}
