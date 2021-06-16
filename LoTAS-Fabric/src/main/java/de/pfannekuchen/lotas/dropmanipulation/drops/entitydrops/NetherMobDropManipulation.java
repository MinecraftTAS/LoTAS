package de.pfannekuchen.lotas.dropmanipulation.drops.entitydrops;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.platform.GlStateManager;

import de.pfannekuchen.lotas.gui.DropManipulationScreen;
import de.pfannekuchen.lotas.gui.widgets.SmallCheckboxWidget;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.widget.CheckboxWidget;
//#if MC>=11601
//$$ import net.minecraft.client.util.math.MatrixStack;
//#endif
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.BlazeEntity;
import net.minecraft.entity.mob.GhastEntity;
import net.minecraft.entity.mob.MagmaCubeEntity;
import net.minecraft.entity.mob.WitherSkeletonEntity;
//#if MC>=11601
//$$ import net.minecraft.entity.mob.ZombifiedPiglinEntity;
//#else
import net.minecraft.entity.mob.ZombiePigmanEntity;
//#endif
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;

public class NetherMobDropManipulation extends DropManipulationScreen.DropManipulation {

    public static SmallCheckboxWidget optimizeBlaze = new SmallCheckboxWidget(0, 0, "Optimize Blaze Drops", false);
    public static SmallCheckboxWidget optimizeGhast = new SmallCheckboxWidget(0, 0, "Optimize Ghast Drops", false);
    public static SmallCheckboxWidget optimizeWitherskeleton = new SmallCheckboxWidget(0, 0, "Optimize Witherskeleton Drops", false);
    public static SmallCheckboxWidget optimizePigman = new SmallCheckboxWidget(0, 0, "Optimize Zombie Pigman Drops", false);
    public static SmallCheckboxWidget optimizeMagmaCube = new SmallCheckboxWidget(0, 0, "Optimize Magma Cube Drops", false);


    public NetherMobDropManipulation(int x, int y, int width, int height) {
    	NetherMobDropManipulation.x = x;
    	NetherMobDropManipulation.y = y;
    	NetherMobDropManipulation.width = width;
    	NetherMobDropManipulation.height = height;
    	//#if MC>=11601
    	//$$ enabled = new CheckboxWidget(x, y, 150, 20, new LiteralText("Override Nether Mob Drops"), false);
        //#else
        enabled = new CheckboxWidget(x, y, 150, 20, "Override Nether Mob Drops", false);
        //#endif
    }

    @Override
    public String getName() {
        return "Nether Mobs";
    }

    @Override
    public List<ItemStack> redirectDrops(BlockState blockstate) { return ImmutableList.of(); }

    @Override
    public List<ItemStack> redirectDrops(Entity entity) {
        if (entity instanceof BlazeEntity && optimizeBlaze.isChecked()) return ImmutableList.of(new ItemStack(Items.BLAZE_ROD));
        if (entity instanceof GhastEntity && optimizeGhast.isChecked()) return ImmutableList.of(new ItemStack(Items.GHAST_TEAR), new ItemStack(Items.GUNPOWDER, 2));
        if (entity instanceof WitherSkeletonEntity && optimizeWitherskeleton.isChecked()) return ImmutableList.of(new ItemStack(Items.COAL, 1), new ItemStack(Items.BONE, 2), new ItemStack(Items.WITHER_SKELETON_SKULL));
        //#if MC>=11601
        //$$ if (entity instanceof ZombifiedPiglinEntity && optimizePigman.isChecked()) if (!((ZombifiedPiglinEntity) entity).isBaby()) return ImmutableList.of(new ItemStack(Items.ROTTEN_FLESH, 2), new ItemStack(Items.GOLD_NUGGET), new ItemStack(Items.GOLD_INGOT));
        //#else
        if (entity instanceof ZombiePigmanEntity && optimizePigman.isChecked()) if (!((ZombiePigmanEntity) entity).isBaby()) return ImmutableList.of(new ItemStack(Items.ROTTEN_FLESH, 2), new ItemStack(Items.GOLD_NUGGET), new ItemStack(Items.GOLD_INGOT));
        //#endif
        if (entity instanceof MagmaCubeEntity && optimizeMagmaCube.isChecked()) if (((MagmaCubeEntity) entity).getSize() != 1) return ImmutableList.of(new ItemStack(Items.MAGMA_CREAM));

        return ImmutableList.of();
    }

    @Override
    public void update() {
        enabled.x = x;
        enabled.y = y;
        optimizeBlaze.y = 64;
        optimizePigman.y = 80;
        optimizeMagmaCube.y = 96;
        optimizeWitherskeleton.y = 112;
        optimizeGhast.y = 128;
        optimizeBlaze.x = x;
        optimizePigman.x = x;
        optimizeMagmaCube.x = x;
        optimizeWitherskeleton.x = x;
        optimizeGhast.x = x;
    }

    @Override
    public void mouseAction(double mouseX, double mouseY, int button) {
        enabled.mouseClicked(mouseX, mouseY, button);
        if (enabled.isChecked()) {
            optimizeBlaze.mouseClicked(mouseX, mouseY, button);
            optimizePigman.mouseClicked(mouseX, mouseY, button);
            optimizeMagmaCube.mouseClicked(mouseX, mouseY, button);
            optimizeGhast.mouseClicked(mouseX, mouseY, button);
            optimizeWitherskeleton.mouseClicked(mouseX, mouseY, button);
        }
    }

    @Override
    public void render(Object matrices, int mouseX, int mouseY, float delta) {
        //#if MC>=11601
        //$$ enabled.render((MatrixStack) matrices, mouseX, mouseY, delta);
        //#else
        enabled.render(mouseX, mouseY, delta);
        //#endif

        if (!enabled.isChecked()) {
            GlStateManager.color4f(.5f, .5f, .5f, .4f);
        } else {
            //#if MC>=11601
            //$$ optimizeBlaze.render((MatrixStack) matrices, mouseX, mouseY, delta);
            //$$ optimizePigman.render((MatrixStack) matrices, mouseX, mouseY, delta);
            //$$ optimizeMagmaCube.render((MatrixStack) matrices, mouseX, mouseY, delta);
            //$$ optimizeGhast.render((MatrixStack) matrices, mouseX, mouseY, delta);
            //$$ optimizeWitherskeleton.render((MatrixStack) matrices, mouseX, mouseY, delta);
            //#else
            optimizeBlaze.render(mouseX, mouseY, delta);
            optimizePigman.render(mouseX, mouseY, delta);
            optimizeMagmaCube.render(mouseX, mouseY, delta);
            optimizeGhast.render(mouseX, mouseY, delta);
            optimizeWitherskeleton.render(mouseX, mouseY, delta);
            //#endif

        }

        MinecraftClient.getInstance().getTextureManager().bindTexture(new Identifier("lotas", "drops/wither_skeleton.png"));
        //#if MC>=11601
        //$$ DrawableHelper.drawTexture((MatrixStack) matrices, width - 128, y + 24, 0.0F, 0.0F, 100, 150, 100, 150);
        //#else
        DrawableHelper.blit(width - 128, y + 24, 0.0F, 0.0F, 100, 150, 100, 150);
        //#endif
    }

}
