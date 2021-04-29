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
import net.minecraft.entity.monster.EntityElderGuardian;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.monster.EntityShulker;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntityStray;
import net.minecraft.entity.monster.EntityVindicator;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiCheckBox;

public class MonsterDropManipulation extends GuiLootManipulation.DropManipulation {

    public static GuiCheckBox optimizeCaveSpider = new GuiCheckBox(999, 0, 0, "Optimize Cave Spider Drops", false);
    public static GuiCheckBox optimizeCreeper = new GuiCheckBox(999, 0, 0, "Optimize Creeper Drops", false);
    public static GuiCheckBox optimizeElderGuardian = new GuiCheckBox(999, 0, 0, "Optimize Elder Guardian Drops", false);
    public static GuiCheckBox optimizeEnderman = new GuiCheckBox(999, 0, 0, "Optimize Enderman Drops", false);
    public static GuiCheckBox optimizeSlime = new GuiCheckBox(999, 0, 0, "Optimize Slime Drops", false);
    public static GuiCheckBox optimizeVindicator = new GuiCheckBox(999, 0, 0, "Optimize Vindicator Drops", false);
    public static GuiCheckBox optimizeSkeleton = new GuiCheckBox(999, 0, 0, "Optimize Skeleton Drops", false);
    public static GuiCheckBox optimizeShulker = new GuiCheckBox(999, 0, 0, "Optimize Shulker Drops", false);
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
            return ImmutableList.of(new ItemStack(Items.SPIDER_EYE), new ItemStack(Items.STRING, 2));
        if (entity instanceof EntityCreeper && optimizeCreeper.isChecked())
            return ImmutableList.of(new ItemStack(Items.GUNPOWDER, 2));
        if (entity instanceof EntityElderGuardian && optimizeElderGuardian.isChecked())
            return ImmutableList.of(new ItemStack(Items.PRISMARINE_CRYSTALS, 1), new ItemStack(Items.PRISMARINE_SHARD, 2), new ItemStack(Blocks.SPONGE), new ItemStack(Item.getItemById(349)));
        if (entity instanceof EntityEnderman && optimizeEnderman.isChecked())
            return ImmutableList.of(new ItemStack(Items.ENDER_PEARL, 1));
        if ((entity instanceof EntitySkeleton || entity instanceof EntityStray) && optimizeSkeleton.isChecked())
            return ImmutableList.of(new ItemStack(Items.ARROW, 2), new ItemStack(Items.BONE, 2));
        if (entity instanceof EntitySlime && optimizeSlime.isChecked())
            if (((EntitySlime) entity).getSlimeSize() == 1) return ImmutableList.of(new ItemStack(Items.SLIME_BALL, 2));
        if (entity instanceof EntityVindicator && optimizeVindicator.isChecked())
            return ImmutableList.of(new ItemStack(Items.EMERALD, 1));
        if (entity instanceof EntityGuardian && optimizeGuardian.isChecked())
            return ImmutableList.of(new ItemStack(Items.PRISMARINE_SHARD, 2));
        if (entity instanceof EntityShulker && optimizeShulker.isChecked())
            return ImmutableList.of(new ItemStack(Items.SHULKER_SHELL, 1));
        if (entity instanceof EntityWitch && optimizeWitch.isChecked()) {
            if (!((EntityWitch) entity).isChild())
                return ImmutableList.of(new ItemStack(Items.GLOWSTONE_DUST), new ItemStack(Items.STICK), new ItemStack(Items.REDSTONE), new ItemStack(Items.GUNPOWDER), new ItemStack(Items.GLASS_BOTTLE), new ItemStack(Items.SPIDER_EYE));
        }
        return ImmutableList.of();
    }

    @Override
    public void update() {
        enabled.xPosition = x;
        enabled.yPosition = y;
        optimizeCaveSpider.yPosition = 64;
        optimizeEnderman.yPosition = 80;
        optimizeElderGuardian.yPosition = 96;
        optimizeCreeper.yPosition = 112;
        optimizeSlime.yPosition = 144;
        optimizeWitch.yPosition = 128;
        optimizeVindicator.yPosition = 160;
        optimizeSkeleton.yPosition = 176;
        optimizeShulker.yPosition = 192;
        optimizeGuardian.yPosition = 208;
        optimizeCaveSpider.xPosition = x;
        optimizeEnderman.xPosition = x;
        optimizeElderGuardian.xPosition = x;
        optimizeCreeper.xPosition = x;
        optimizeSlime.xPosition = x;
        optimizeSkeleton.xPosition = x;
        optimizeVindicator.xPosition = x;
        optimizeShulker.xPosition = x;
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
            optimizeElderGuardian.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY);
            optimizeSlime.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY);
            optimizeVindicator.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY);
            optimizeSkeleton.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY);
            optimizeShulker.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY);
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
            optimizeElderGuardian.drawButton(Minecraft.getMinecraft(), mouseX, mouseY);
            optimizeSlime.drawButton(Minecraft.getMinecraft(), mouseX, mouseY);
            optimizeVindicator.drawButton(Minecraft.getMinecraft(), mouseX, mouseY);
            optimizeSkeleton.drawButton(Minecraft.getMinecraft(), mouseX, mouseY);
            optimizeShulker.drawButton(Minecraft.getMinecraft(), mouseX, mouseY);
            optimizeGuardian.drawButton(Minecraft.getMinecraft(), mouseX, mouseY);
            optimizeWitch.drawButton(Minecraft.getMinecraft(), mouseX, mouseY);
        }

        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("lotas", "drops/spider.png"));
        Gui.drawModalRectWithCustomSizedTexture(width - 128, y + 24, 0.0F, 0.0F, 109, 85, 109, 85);
    }

}
