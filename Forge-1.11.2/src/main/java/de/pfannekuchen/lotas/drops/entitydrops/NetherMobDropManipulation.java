package de.pfannekuchen.lotas.drops.entitydrops;

import java.util.List;

import com.google.common.collect.ImmutableList;

import de.pfannekuchen.lotas.gui.GuiLootManipulation;
import de.pfannekuchen.lotas.gui.parts.CheckboxWidget;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntityWitherSkeleton;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiCheckBox;

public class NetherMobDropManipulation extends GuiLootManipulation.DropManipulation {

    public static GuiCheckBox optimizeBlaze = new GuiCheckBox(999, 0, 0, "Optimize Blaze Drops", false);
    public static GuiCheckBox optimizeGhast = new GuiCheckBox(999, 0, 0, "Optimize Ghast Drops", false);
    public static GuiCheckBox optimizeWitherskeleton = new GuiCheckBox(999, 0, 0, "Optimize Witherskeleton Drops", false);
    public static GuiCheckBox optimizePigman = new GuiCheckBox(999, 0, 0, "Optimize Zombie Pigman Drops", false);
    public static GuiCheckBox optimizeMagmaCube = new GuiCheckBox(999, 0, 0, "Optimize Magma Cube Drops", false);


    public NetherMobDropManipulation(int x, int y, int width, int height) {
    	NetherMobDropManipulation.x = x;
    	NetherMobDropManipulation.y = y;
    	NetherMobDropManipulation.width = width;
    	NetherMobDropManipulation.height = height;
        enabled = new CheckboxWidget(x, y, 150, 20, "Override Nether Mob Drops", false);
    }

    @Override
    public String getName() {
        return "Nether Mobs";
    }

    @Override
    public List<ItemStack> redirectDrops(IBlockState blockstate) { return ImmutableList.of(); }

    @Override
    public List<ItemStack> redirectDrops(Entity entity) {
        if (entity instanceof EntityBlaze && optimizeBlaze.isChecked()) return ImmutableList.of(new ItemStack(Items.BLAZE_ROD));
        if (entity instanceof EntityGhast && optimizeGhast.isChecked()) return ImmutableList.of(new ItemStack(Items.GHAST_TEAR), new ItemStack(Items.GUNPOWDER, 2));
        if (entity instanceof EntityWitherSkeleton && optimizeWitherskeleton.isChecked()) return ImmutableList.of(new ItemStack(Items.COAL, 1), new ItemStack(Items.BONE, 2), new ItemStack(Items.SKULL, 1, 1));
        if (entity instanceof EntityPigZombie && optimizePigman.isChecked()) if (!((EntityPigZombie) entity).isChild()) return ImmutableList.of(new ItemStack(Items.ROTTEN_FLESH, 2), new ItemStack(Items.GOLD_NUGGET), new ItemStack(Items.GOLD_INGOT));
        if (entity instanceof EntityMagmaCube && optimizeMagmaCube.isChecked()) if (((EntityMagmaCube) entity).getSlimeSize() != 1) return ImmutableList.of(new ItemStack(Items.MAGMA_CREAM));

        return ImmutableList.of();
    }

    @Override
    public void update() {
        enabled.xPosition = x;
        enabled.yPosition = y;
        optimizeBlaze.yPosition = 64;
        optimizePigman.yPosition = 80;
        optimizeMagmaCube.yPosition = 96;
        optimizeWitherskeleton.yPosition = 112;
        optimizeGhast.yPosition = 128;
        optimizeBlaze.xPosition = x;
        optimizePigman.xPosition = x;
        optimizeMagmaCube.xPosition = x;
        optimizeWitherskeleton.xPosition = x;
        optimizeGhast.xPosition = x;
    }

    @Override
    public void mouseAction(int mouseX, int mouseY, int button) {
        enabled.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY);
        if (enabled.isChecked()) {
            optimizeBlaze.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY);
            optimizePigman.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY);
            optimizeMagmaCube.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY);
            optimizeGhast.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY);
            optimizeWitherskeleton.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY);
        }
    }

    @Override
    public void render(int mouseX, int mouseY, float delta) {
        enabled.render(mouseX, mouseY, delta);

        if (!enabled.isChecked()) {
            GlStateManager.color(.5f, .5f, .5f, .4f);
        } else {
            optimizeBlaze.drawButton(Minecraft.getMinecraft(), mouseX, mouseY);
            optimizePigman.drawButton(Minecraft.getMinecraft(), mouseX, mouseY);
            optimizeMagmaCube.drawButton(Minecraft.getMinecraft(), mouseX, mouseY);
            optimizeGhast.drawButton(Minecraft.getMinecraft(), mouseX, mouseY);
            optimizeWitherskeleton.drawButton(Minecraft.getMinecraft(), mouseX, mouseY);
        }

        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("lotas", "drops/wither_skeleton.png"));
        Gui.drawModalRectWithCustomSizedTexture(width - 128, y + 24, 0.0F, 0.0F, 100, 150, 100, 150);
    }

}
