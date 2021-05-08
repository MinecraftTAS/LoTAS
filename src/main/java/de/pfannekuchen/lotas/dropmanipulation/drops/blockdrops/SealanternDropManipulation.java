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
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class SealanternDropManipulation extends GuiDropChanceManipulation.DropManipulation {

    public static int pris = 2;

    public static ButtonWidget drop2Pris = new ButtonWidget(x, y, 98, 20, "2 Prismarine Crystals", button -> {
        press2pris();
    });
    public static ButtonWidget drop3Pris = new ButtonWidget(x, y, 98, 20, "3 Prismarine Crystals", button -> {
        press3pris();
    });

    public static void press2pris() {
        drop2Pris.enabled = false;
        drop3Pris.enabled = true;
        pris = 2;
    }

    public static void press3pris() {
        drop2Pris.enabled = true;
        drop3Pris.enabled = false;
        pris = 3;
    }

    public SealanternDropManipulation(int x, int y, int width, int height) {
    	SealanternDropManipulation.x = x;
    	SealanternDropManipulation.y = y;
    	SealanternDropManipulation.width = width;
        SealanternDropManipulation.height = height;
        enabled = new CheckboxWidget(x, y, 150, 20, "Override Sea Lantern Drops", false);
        drop2Pris.enabled = false;
    }

    @Override
    public String getName() {
        return "Sea Lantern";
    }

    @Override
    public List<ItemStack> redirectDrops(IBlockState block) {
        if (block.getBlock().getDefaultState().getBlock() != Blocks.SEA_LANTERN) return ImmutableList.of();
        return ImmutableList.of(new ItemStack(Items.PRISMARINE_CRYSTALS, pris));
    }

    @Override
    public List<ItemStack> redirectDrops(Entity entity) {
        return ImmutableList.of();
    }

    @Override
    public void update() {
    	updateX(enabled, x);
    	updateY(enabled, y);
    	
    	updateX(drop2Pris, x);
    	updateY(drop2Pris, y + 96);
    	updateX(drop3Pris, x);
    	updateY(drop3Pris, y + 120);

        drop2Pris.setWidth(width - x - 128 - 16);
        drop3Pris.setWidth(width - x - 128 - 16);

    }

    @Override
    public void mouseAction(int mouseX, int mouseY, int button) {
        enabled.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY);
        if (enabled.isChecked()) {
            drop2Pris.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY);
            drop3Pris.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY);
        }
    }

    @Override
    public void render(int mouseX, int mouseY, float delta) {
        enabled.render(mouseX, mouseY, delta);

        if (!enabled.isChecked()) {
            GlStateManager.color(.5f, .5f, .5f, .4f);
        } else {
            MCVer.getFontRenderer(Minecraft.getMinecraft()).drawStringWithShadow("Drop " + pris + " Prismarine Crystals when breaking Sea Lanterns", x, y + 64, 0xFFFFFF);
            drop2Pris.render(mouseX, mouseY, delta);
            drop3Pris.render(mouseX, mouseY, delta);
        }

        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("lotas", "drops/sealantern.gif"));
        Gui.drawModalRectWithCustomSizedTexture(width - 128, y + 24, 0.0F, 0.0F, 96, 96, 96, 96);
    }

}
