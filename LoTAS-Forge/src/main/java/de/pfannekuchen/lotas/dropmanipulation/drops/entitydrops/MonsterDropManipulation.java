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
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class MonsterDropManipulation extends GuiDropChanceManipulation.DropManipulation {

    public static ModifiedCheckBoxWidget optimizeCreeper = new ModifiedCheckBoxWidget(999, 0, 0, "Optimize Creeper Drops", false);
    public static ModifiedCheckBoxWidget optimizeElderGuardian = new ModifiedCheckBoxWidget(999, 0, 0, "Optimize Elder Guardian Drops", false);
    public static ModifiedCheckBoxWidget optimizeEnderman = new ModifiedCheckBoxWidget(999, 0, 0, "Optimize Enderman Drops", false);
    public static ModifiedCheckBoxWidget optimizeSlime = new ModifiedCheckBoxWidget(999, 0, 0, "Optimize Slime Drops", false);
    public static ModifiedCheckBoxWidget optimizeVindicator = new ModifiedCheckBoxWidget(999, 0, 0, "Optimize Vindicator Drops", false);
    public static ModifiedCheckBoxWidget optimizeShulker = new ModifiedCheckBoxWidget(999, 0, 0, "Optimize Shulker Drops", false);
    public static ModifiedCheckBoxWidget optimizeSkeleton = new ModifiedCheckBoxWidget(999, 0, 0, "Optimize Skeleton Drops", false);
    public static ModifiedCheckBoxWidget optimizeSpider = new ModifiedCheckBoxWidget(999, 0, 0, "Optimize Spider Drops", false);
    public static ModifiedCheckBoxWidget optimizeGuardian = new ModifiedCheckBoxWidget(999, 0, 0, "Optimize Guardian Drops", false);
    public static ModifiedCheckBoxWidget optimizeWitch = new ModifiedCheckBoxWidget(999, 0, 0, "Optimize Witch Drops", false);

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
        if ((entity instanceof EntityCaveSpider || entity instanceof EntitySpider) && optimizeSpider.isChecked())
            return ImmutableList.of(new ItemStack(MCVer.getItem("SPIDER_EYE")), new ItemStack(MCVer.getItem("STRING"), 2));
        if (entity instanceof EntityCreeper && optimizeCreeper.isChecked())
            return ImmutableList.of(new ItemStack(MCVer.getItem("GUNPOWDER"), 2));
        if (entity instanceof EntityEnderman && optimizeEnderman.isChecked())
            return ImmutableList.of(new ItemStack(MCVer.getItem("ENDER_PEARL"), 1));
        if (entity instanceof EntitySkeleton && optimizeSkeleton.isChecked())
            return ImmutableList.of(new ItemStack(MCVer.getItem("ARROW"), 2), new ItemStack(MCVer.getItem("BONE"), 2));
        if (entity instanceof EntitySlime && optimizeSlime.isChecked())
            if (((EntitySlime) entity).getSlimeSize() == 1) return ImmutableList.of(new ItemStack(MCVer.getItem("SLIME_BALL"), 2));
        if (entity instanceof EntityGuardian && optimizeGuardian.isChecked())
            return ImmutableList.of(new ItemStack(MCVer.getItem("PRISMARINE_SHARD"), 2));
        if (entity instanceof EntityWitch && optimizeWitch.isChecked()) {
            if (!((EntityWitch) entity).isChild())
            	return ImmutableList.of(new ItemStack(MCVer.getItem("GLOWSTONE_DUST")), new ItemStack(MCVer.getItem("REDSTONE")), new ItemStack(MCVer.getItem("GUNPOWDER")), 
            			// Witches don't drop potions in 1.8
            			//#if MC>=10900
            			((EntityWitch) entity).isDrinkingPotion() ? entity.getHeldEquipment().iterator().next() : new ItemStack(MCVer.getItem("GLASS_BOTTLE"))
            			//#else
            //$$ 			new ItemStack(MCVer.getItem("GLASS_BOTTLE"))
            			//#endif
            					, new ItemStack(MCVer.getItem("SPIDER_EYE")));
        }
        
        //#if MC>=11100
        if (entity instanceof net.minecraft.entity.monster.EntityVindicator && optimizeVindicator.isChecked())
            return ImmutableList.of(new ItemStack(Items.EMERALD, 1));
        if (entity instanceof net.minecraft.entity.monster.EntityShulker && optimizeShulker.isChecked())
            return ImmutableList.of(new ItemStack(Items.SHULKER_SHELL, 1));
        if (entity instanceof net.minecraft.entity.monster.EntityElderGuardian && optimizeElderGuardian.isChecked())
            return ImmutableList.of(new ItemStack(Items.PRISMARINE_CRYSTALS, 1), new ItemStack(Items.PRISMARINE_SHARD, 2), new ItemStack(Blocks.SPONGE), new ItemStack(Item.getItemById(349)));
        //#endif
        return ImmutableList.of();
    }

    @Override
    public void update() {
        updateX(enabled, x);
        updateY(enabled, y);
        int y=64;
        updateY(optimizeEnderman, y);
        updateY(optimizeElderGuardian, y+=16);
        updateY(optimizeCreeper, y+=16);
        updateY(optimizeSlime, y+=16);
        updateY(optimizeWitch, y+=16);
        updateY(optimizeVindicator, y+=16);
        updateY(optimizeShulker, y+=16);
        updateY(optimizeSkeleton, y+=16);
        updateY(optimizeSpider, y+=16);
        updateY(optimizeGuardian, y+=16);
        updateX(optimizeEnderman, x);
        updateX(optimizeElderGuardian, x);
        updateX(optimizeCreeper, x);
        updateX(optimizeSlime, x);
        updateX(optimizeSkeleton, x);
        updateX(optimizeShulker, x);
        updateX(optimizeSpider, x);
        updateX(optimizeVindicator, x);
        updateX(optimizeGuardian, x);
        updateX(optimizeWitch, x);
    }

    @Override
    public void mouseAction(int mouseX, int mouseY, int button) {
        enabled.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY);
        if (enabled.isChecked()) {
            optimizeEnderman.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY);
            optimizeCreeper.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY);
            optimizeElderGuardian.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY);
            optimizeSlime.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY);
            optimizeVindicator.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY);
            optimizeShulker.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY);
            optimizeSkeleton.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY);
            optimizeSpider.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY);
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
            optimizeEnderman.drawButton(Minecraft.getMinecraft(), mouseX, mouseY, delta);
            optimizeCreeper.drawButton(Minecraft.getMinecraft(), mouseX, mouseY, delta);
            optimizeElderGuardian.drawButton(Minecraft.getMinecraft(), mouseX, mouseY, delta);
            optimizeSlime.drawButton(Minecraft.getMinecraft(), mouseX, mouseY, delta);
            optimizeVindicator.drawButton(Minecraft.getMinecraft(), mouseX, mouseY, delta);
            optimizeShulker.drawButton(Minecraft.getMinecraft(), mouseX, mouseY, delta);
            optimizeSkeleton.drawButton(Minecraft.getMinecraft(), mouseX, mouseY, delta);
            optimizeSpider.drawButton(Minecraft.getMinecraft(), mouseX, mouseY, delta);
            optimizeGuardian.drawButton(Minecraft.getMinecraft(), mouseX, mouseY, delta);
            optimizeWitch.drawButton(Minecraft.getMinecraft(), mouseX, mouseY, delta);
        }

        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("lotas", "drops/spider.png"));
        Gui.drawModalRectWithCustomSizedTexture(width - 128, y + 24, 0.0F, 0.0F, 109, 85, 109, 85);
    }

}
