package de.pfannekuchen.lotas.dropmanipulation.drops.entitydrops;

import java.util.List;

import com.google.common.collect.ImmutableList;

import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.gui.GuiDropChanceManipulation;
import de.pfannekuchen.lotas.gui.widgets.CheckboxWidget;
import de.pfannekuchen.lotas.gui.widgets.ModifiedCheckBoxWidget;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class NetherMobDropManipulation extends GuiDropChanceManipulation.DropManipulation {

    public static ModifiedCheckBoxWidget optimizeBlaze = new ModifiedCheckBoxWidget(999, 0, 0, "Optimize Blaze Drops", false);
    public static ModifiedCheckBoxWidget optimizeGhast = new ModifiedCheckBoxWidget(999, 0, 0, "Optimize Ghast Drops", false);
    public static ModifiedCheckBoxWidget optimizeWitherskeleton = new ModifiedCheckBoxWidget(999, 0, 0, "Optimize Witherskeleton Drops", false);
    public static ModifiedCheckBoxWidget optimizePigman = new ModifiedCheckBoxWidget(999, 0, 0, "Optimize Zombie Pigman Drops", false);
    public static ModifiedCheckBoxWidget optimizeMagmaCube = new ModifiedCheckBoxWidget(999, 0, 0, "Optimize Magma Cube Drops", false);


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
        if (entity instanceof EntityBlaze && optimizeBlaze.isChecked()) return ImmutableList.of(new ItemStack(MCVer.getItem("BLAZE_ROD")));
        if (entity instanceof EntityGhast && optimizeGhast.isChecked()) return ImmutableList.of(new ItemStack(MCVer.getItem("GHAST_TEAR")), new ItemStack(MCVer.getItem("GUNPOWDER"), 2));
        //#if MC>=11100
        if (entity instanceof net.minecraft.entity.monster.EntityWitherSkeleton && optimizeWitherskeleton.isChecked()) return ImmutableList.of(new ItemStack(Items.COAL, 1), new ItemStack(Items.BONE, 2), new ItemStack(Items.SKULL, 1, 1));
        //#endif
        if (entity instanceof EntityPigZombie && optimizePigman.isChecked()) if (!((EntityPigZombie) entity).isChild()) return ImmutableList.of(new ItemStack(MCVer.getItem("ROTTEN_FLESH"), 2), new ItemStack(MCVer.getItem("GOLD_NUGGET")), new ItemStack(MCVer.getItem("GOLD_INGOT")));
        if (entity instanceof EntityMagmaCube && optimizeMagmaCube.isChecked()) if (((EntityMagmaCube) entity).getSlimeSize() != 1) return ImmutableList.of(new ItemStack(MCVer.getItem("MAGMA_CREAM")));

        return ImmutableList.of();
    }

    @Override
    public void update() {
        updateX(enabled, x);
        updateY(enabled, y);
        updateY(optimizeBlaze, 64);
        updateY(optimizePigman, 80);
        updateY(optimizeMagmaCube, 96);
        updateY(optimizeWitherskeleton, 112);
        updateY(optimizeGhast, 128);
        updateX(optimizeBlaze, x);
        updateX(optimizePigman, x);
        updateX(optimizeMagmaCube, x);
        updateX(optimizeWitherskeleton, x);
        updateX(optimizeGhast, x);
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
            optimizeBlaze.drawButton(Minecraft.getMinecraft(), mouseX, mouseY, delta);
            optimizePigman.drawButton(Minecraft.getMinecraft(), mouseX, mouseY, delta);
            optimizeMagmaCube.drawButton(Minecraft.getMinecraft(), mouseX, mouseY, delta);
            optimizeGhast.drawButton(Minecraft.getMinecraft(), mouseX, mouseY, delta);
            optimizeWitherskeleton.drawButton(Minecraft.getMinecraft(), mouseX, mouseY, delta);
        }

        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("lotas", "drops/wither_skeleton.png"));
        Gui.drawModalRectWithCustomSizedTexture(width - 128, y + 24, 0.0F, 0.0F, 100, 150, 100, 150);
    }

}
