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
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiCheckBox;

public class MonsterDropManipulation extends GuiLootManipulation.DropManipulation {

    public static GuiCheckBox optimizeCaveSpider = new GuiCheckBox(999, 0, 0, "Optimize Cave Spider Drops", false);
    public static GuiCheckBox optimizeCreeper = new GuiCheckBox(999, 0, 0, "Optimize Creeper Drops", false);
    public static GuiCheckBox optimizeEnderman = new GuiCheckBox(999, 0, 0, "Optimize Enderman Drops", false);
    public static GuiCheckBox optimizeSlime = new GuiCheckBox(999, 0, 0, "Optimize Slime Drops", false);
    public static GuiCheckBox optimizeSkeleton = new GuiCheckBox(999, 0, 0, "Optimize Skeleton Drops", false);
    public static GuiCheckBox optimizeGuardian = new GuiCheckBox(999, 0, 0, "Optimize Guardian Drops", false);
    public static GuiCheckBox optimizeWitch = new GuiCheckBox(999, 0, 0, "Optimize Witch Drops", false);

    public MonsterDropManipulation(int x, int y, int width, int height) {
    	MonsterDropManipulation.x = x;
    	MonsterDropManipulation.y = y;
    	MonsterDropManipulation.width = width;
        MonsterDropManipulation.height = height;
        enabled = new CheckboxWidget(x, y, 150, 20, "Override Monster Drops", false);
    }

    @Override
    public String getName() {
        return "Monsters";
    }

    @Override
    public List<ItemStack> redirectDrops(IBlockState blockstate) { return ImmutableList.of(); }

    @Override
    public List<ItemStack> redirectDrops(Entity entity) {
        if (entity instanceof EntityCaveSpider && optimizeCaveSpider.isChecked())
            return ImmutableList.of(new ItemStack(Items.spider_eye), new ItemStack(Items.string, 2));
        if (entity instanceof EntityCreeper && optimizeCreeper.isChecked())
            return ImmutableList.of(new ItemStack(Items.gunpowder, 2));
        if (entity instanceof EntityEnderman && optimizeEnderman.isChecked())
            return ImmutableList.of(new ItemStack(Items.ender_pearl, 1));
        if ((entity instanceof EntitySkeleton) && optimizeSkeleton.isChecked())
            return ImmutableList.of(new ItemStack(Items.arrow, 2), new ItemStack(Items.bone, 2));
        if (entity instanceof EntitySlime && optimizeSlime.isChecked())
            if (((EntitySlime) entity).getSlimeSize() == 1) return ImmutableList.of(new ItemStack(Items.slime_ball, 2));
        if (entity instanceof EntityGuardian && optimizeGuardian.isChecked())
            return ImmutableList.of(new ItemStack(Items.prismarine_shard, 2));
        if (entity instanceof EntityWitch && optimizeWitch.isChecked()) {
            if (!((EntityWitch) entity).isChild())
                return ImmutableList.of(new ItemStack(Items.glowstone_dust), new ItemStack(Items.stick), new ItemStack(Items.redstone), new ItemStack(Items.gunpowder), new ItemStack(Items.glass_bottle), new ItemStack(Items.spider_eye));
        }
        return ImmutableList.of();
    }

    @Override
    public void update() {
        enabled.xPosition = x;
        enabled.yPosition = y;
        optimizeCaveSpider.yPosition = 64;
        optimizeEnderman.yPosition = 80;
        optimizeCreeper.yPosition = 112;
        optimizeSlime.yPosition = 144;
        optimizeWitch.yPosition = 128;
        optimizeSkeleton.yPosition = 176;
        optimizeGuardian.yPosition = 208;
        optimizeCaveSpider.xPosition = x;
        optimizeEnderman.xPosition = x;
        optimizeCreeper.xPosition = x;
        optimizeSlime.xPosition = x;
        optimizeSkeleton.xPosition = x;
        optimizeGuardian.xPosition = x;
        optimizeWitch.xPosition = x;
    }

    @Override
    public void mouseAction(int mouseX, int mouseY, int button) {
        enabled.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY);
        if (enabled.isChecked()) {
            optimizeCaveSpider.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY);
            optimizeEnderman.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY);
            optimizeCreeper.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY);
            optimizeSlime.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY);
            optimizeSkeleton.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY);
            optimizeGuardian.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY);
            optimizeWitch.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY);
        }
    }

    @Override
    public void render(int mouseX, int mouseY, float delta) {
        enabled.render(mouseX, mouseY, delta);

        if (!enabled.isChecked()) {
            GlStateManager.color(.5f, .5f, .5f, .4f);
        } else {
            optimizeCaveSpider.drawButton(Minecraft.getMinecraft(), mouseX, mouseY);
            optimizeEnderman.drawButton(Minecraft.getMinecraft(), mouseX, mouseY);
            optimizeCreeper.drawButton(Minecraft.getMinecraft(), mouseX, mouseY);
            optimizeSlime.drawButton(Minecraft.getMinecraft(), mouseX, mouseY);
            optimizeSkeleton.drawButton(Minecraft.getMinecraft(), mouseX, mouseY);
            optimizeGuardian.drawButton(Minecraft.getMinecraft(), mouseX, mouseY);
            optimizeWitch.drawButton(Minecraft.getMinecraft(), mouseX, mouseY);
        }

        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("lotas", "drops/spider.png"));
        Gui.drawModalRectWithCustomSizedTexture(width - 128, y + 24, 0.0F, 0.0F, 109, 85, 109, 85);
    }

}
