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
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityIronGolem;
//#if MC>=11000
import net.minecraft.entity.monster.EntityPolarBear;
//#endif
import net.minecraft.entity.monster.EntitySnowman;
//#if MC>=11100
import net.minecraft.entity.passive.AbstractHorse;
//#endif
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityMooshroom;
//#if MC>=11200
import net.minecraft.entity.passive.EntityParrot;
//#endif
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class PassiveDropManipulation extends GuiDropChanceManipulation.DropManipulation {

    public static ModifiedCheckBoxWidget optimizeChicken = new ModifiedCheckBoxWidget(999, 0, 0, "Optimize Chicken Drops", false);
    public static ModifiedCheckBoxWidget optimizeCow = new ModifiedCheckBoxWidget(999, 0, 0, "Optimize Cow Drops", false);
    public static ModifiedCheckBoxWidget optimizeMooshroom = new ModifiedCheckBoxWidget(999, 0, 0, "Optimize Mooshroom Drops", false);
    public static ModifiedCheckBoxWidget optimizePig = new ModifiedCheckBoxWidget(999, 0, 0, "Optimize Pig Drops", false);
    public static ModifiedCheckBoxWidget optimizeParrot = new ModifiedCheckBoxWidget(999, 0, 0, "Optimize Parrot Drops", false);
    public static ModifiedCheckBoxWidget optimizeRabbit = new ModifiedCheckBoxWidget(999, 0, 0, "Optimize Rabbit Drops", false);
    public static ModifiedCheckBoxWidget optimizeSheep = new ModifiedCheckBoxWidget(999, 0, 0, "Optimize Sheep Drops", false);
    public static ModifiedCheckBoxWidget optimizeSnowgolem = new ModifiedCheckBoxWidget(999, 0, 0, "Optimize Snowgolem Drops", false);
    public static ModifiedCheckBoxWidget optimizeSquid = new ModifiedCheckBoxWidget(999, 0, 0, "Optimize Squid Drops", false);
    public static ModifiedCheckBoxWidget optimizeHorses = new ModifiedCheckBoxWidget(999, 0, 0, "Optimize All Horse Drops", false);
    public static ModifiedCheckBoxWidget optimizeIronGolem = new ModifiedCheckBoxWidget(999, 0, 0, "Optimize Iron Golem Drops", false);
    public static ModifiedCheckBoxWidget optimizePolarbear = new ModifiedCheckBoxWidget(999, 0, 0, "Optimize Polarbear Drops", false);


    public PassiveDropManipulation(int x, int y, int width, int height) {
    	PassiveDropManipulation.x = x;
    	PassiveDropManipulation.y = y;
    	PassiveDropManipulation.width = width;
        PassiveDropManipulation.height = height;
        //#if MC<=11102
        //$$ optimizeParrot.enabled = false;
        //$$ optimizeHorses.enabled = false;
        //#endif
        
        //#if MC<=11002
        //$$ optimizePolarbear.enabled = false;
        //#endif
        enabled = new CheckboxWidget(x, y, 150, 20, "Override Passive Mob Drops", false);
    }

    @Override
    public String getName() {
        return "Passive Mobs";
    }

    @Override
    public List<ItemStack> redirectDrops(IBlockState blockstate) { return ImmutableList.of(); }

    @Override
    public List<ItemStack> redirectDrops(Entity entity) {
        if (entity instanceof EntityChicken && optimizeChicken.isChecked()) {
            if (!((EntityLiving) entity).isChild())
                return ImmutableList.of(new ItemStack(MCVer.getItem("FEATHER"), 2), entity.isBurning() ? new ItemStack(MCVer.getItem("COOKED_CHICKEN")) : new ItemStack(MCVer.getItem("CHICKEN")));
        } else if (entity instanceof EntityCow && optimizeCow.isChecked()) {
            if (!((EntityLiving) entity).isChild())
                return ImmutableList.of(new ItemStack(MCVer.getItem("LEATHER")), entity.isBurning() ? new ItemStack(MCVer.getItem("COOKED_BEEF"), 3) : new ItemStack(MCVer.getItem("BEEF"), 3));
        } else if (entity instanceof EntityMooshroom && optimizeMooshroom.isChecked()) {
            if (!((EntityLiving) entity).isChild())
                return ImmutableList.of(new ItemStack(MCVer.getItem("LEATHER"), 2), entity.isBurning() ? new ItemStack(MCVer.getItem("COOKED_BEEF"), 3) : new ItemStack(MCVer.getItem("BEEF"), 3));
        } else if (entity instanceof EntityPig && optimizePig.isChecked()) {
            if (!((EntityPig) entity).isChild())
                return ImmutableList.of(entity.isBurning() ? new ItemStack(MCVer.getItem("COOKED_PORKCHOP"), 3) : new ItemStack(MCVer.getItem("PORKCHOP"), 3));
        } 
        //#if MC>=11200
        else if (entity instanceof EntityParrot && optimizeParrot.isChecked()) {
            if (!((EntityParrot) entity).isChild())
                return ImmutableList.of(new ItemStack(Items.FEATHER, 2));
        } else if ((entity instanceof AbstractHorse) && optimizeHorses.isChecked()) {
            if (!((EntityHorse) entity).isChild())
                return ImmutableList.of(new ItemStack(Items.LEATHER, 2));
        }
        //#endif
        else if (entity instanceof EntityRabbit && optimizeRabbit.isChecked()) {
            if (!((EntityRabbit) entity).isChild())
               return ImmutableList.of(new ItemStack(MCVer.getItem("RABBIT_FOOT"), 1), new ItemStack(MCVer.getItem("RABBIT_HIDE"), 1), entity.isBurning() ? new ItemStack(MCVer.getItem("COOKED_RABBIT")) : new ItemStack(MCVer.getItem("RABBIT")));
        } else if (entity instanceof EntitySheep && optimizeSheep.isChecked()) {
            if (!((EntitySheep) entity).isChild()) 
               return ImmutableList.of(entity.isBurning() ? new ItemStack(MCVer.getItem("COOKED_MUTTON"), 3) : new ItemStack(MCVer.getItem("MUTTON"), 3), new ItemStack(Item.getItemFromBlock(MCVer.getBlock("WOOL")), 1, ((EntitySheep) entity).getFleeceColor().getMetadata()));
        } else if (entity instanceof EntitySnowman && optimizeSnowgolem.isChecked()) {
            if (!((EntitySnowman) entity).isChild())
                return ImmutableList.of(new ItemStack(MCVer.getItem("SNOWBALL"), 15));
        } else if (entity instanceof EntitySquid && optimizeSquid.isChecked()) {
            if (!((EntitySquid) entity).isChild())
                return ImmutableList.of(new ItemStack(MCVer.getItem("DYE"), 3, 0));
        } else if (entity instanceof EntityIronGolem && optimizeIronGolem.isChecked()) {
            if (!((EntityIronGolem) entity).isChild())
                return ImmutableList.of(new ItemStack(MCVer.getItem("IRON_INGOT"), 5), new ItemStack(MCVer.getBlock("RED_FLOWER")));
        } 
        //#if MC>=11000
        else if (entity instanceof EntityPolarBear && optimizePolarbear.isChecked()) {
            if (!((EntityPolarBear) entity).isChild())
                return ImmutableList.of(new ItemStack(Items.FISH, 2));
        }
        //#endif
        return ImmutableList.of();
    }

    @Override
    public void update() {
        updateX(enabled, x);
        updateY(enabled, y);
        updateY(optimizeChicken, 64);
        updateY( optimizeMooshroom, 112);
        updateY( optimizeCow, 128);
        updateY(optimizePig, 144);
        updateY(optimizeParrot, 160);
        updateY(optimizeRabbit, 176);
        updateY(optimizeSnowgolem, 192);
        updateY(optimizeSheep, 208);
        updateY(optimizeSquid, 224);
        updateY(optimizeHorses, 240);
        updateY(optimizeIronGolem, 96);
        updateY(optimizePolarbear, 80);
        updateX(optimizeChicken, x);
        updateX(optimizeMooshroom, x);
        updateX(optimizeCow, x);
        updateX(optimizePig, x);
        updateX(optimizeParrot, x);
        updateX(optimizeRabbit, x);
        updateX(optimizeSnowgolem, x);
        updateX(optimizeSheep, x);
        updateX(optimizeSquid, x);
        updateX(optimizeHorses, x);
        updateX(optimizeIronGolem, x);
        updateX(optimizePolarbear, x);
    }

    @Override
    public void mouseAction(int mouseX, int mouseY, int button) {
        enabled.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY);
        if (enabled.isChecked()) {
            optimizeChicken.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY);
            optimizeCow.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY);
            optimizeMooshroom.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY);
            optimizePig.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY);
            optimizeParrot.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY);
            optimizeSheep.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY);
            optimizeRabbit.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY);
            optimizeSnowgolem.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY);
            optimizeSquid.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY);
            optimizeHorses.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY);
            optimizePolarbear.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY);
            optimizeIronGolem.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY);
        }
    }

    @Override
    public void render(int mouseX, int mouseY, float delta) {
        enabled.render(mouseX, mouseY, delta);

        if (!enabled.isChecked()) {
            GlStateManager.color(.5f, .5f, .5f, .4f);
        } else {
            optimizeChicken.drawButton(Minecraft.getMinecraft(), mouseX, mouseY, delta);
            optimizeCow.drawButton(Minecraft.getMinecraft(), mouseX, mouseY, delta);
            optimizeMooshroom.drawButton(Minecraft.getMinecraft(), mouseX, mouseY, delta);
            optimizePig.drawButton(Minecraft.getMinecraft(), mouseX, mouseY, delta);
            optimizeParrot.drawButton(Minecraft.getMinecraft(), mouseX, mouseY, delta);
            optimizeSnowgolem.drawButton(Minecraft.getMinecraft(), mouseX, mouseY, delta);
            optimizeSheep.drawButton(Minecraft.getMinecraft(), mouseX, mouseY, delta);
            optimizeRabbit.drawButton(Minecraft.getMinecraft(), mouseX, mouseY, delta);
            optimizeSquid.drawButton(Minecraft.getMinecraft(), mouseX, mouseY, delta);
            optimizeIronGolem.drawButton(Minecraft.getMinecraft(), mouseX, mouseY, delta);
            optimizePolarbear.drawButton(Minecraft.getMinecraft(), mouseX, mouseY, delta);
            optimizeHorses.drawButton(Minecraft.getMinecraft(), mouseX, mouseY, delta);
        }

        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("lotas", "drops/sheep.png"));
        Gui.drawModalRectWithCustomSizedTexture(width - 128, y + 24, 0.0F, 0.0F, 102, 120, 102, 120);
    }

}
