package de.pfannekuchen.lotas.dropmanipulation.drops.blockdrops;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;

import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.gui.GuiDropChanceManipulation;
import de.pfannekuchen.lotas.gui.widgets.CheckboxWidget;
import de.pfannekuchen.lotas.gui.widgets.ImageWidget;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockPlanks.EnumType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class LeavesDropManipulation extends GuiDropChanceManipulation.DropManipulation {

    public static ImageWidget dropApple = new ImageWidget(x, y, c -> {LeavesDropManipulation.dropApple.setToggled(!LeavesDropManipulation.dropApple.isToggled());}, new ResourceLocation("lotas", "drops/apple.png"));
    public static ImageWidget dropSapling = new ImageWidget(x, y, c -> {LeavesDropManipulation.dropSapling.setToggled(!LeavesDropManipulation.dropSapling.isToggled());}, new ResourceLocation("lotas", "drops/sapling.png"));

    public LeavesDropManipulation(int x, int y, int width, int height) {
    	LeavesDropManipulation.x = x;
    	LeavesDropManipulation.y = y;
    	LeavesDropManipulation.width = width;
        LeavesDropManipulation.height = height;
        enabled = new CheckboxWidget(x, y, 150, 20, "Override Leave Drops", false);
    }

    @Override
    public String getName() {
        return "Leaves";
    }

    @Override
    public List<ItemStack> redirectDrops(IBlockState block) {
        List<ItemStack> list = new ArrayList<>();
        
        if (block.getBlock().getDefaultState().getBlock() == MCVer.getBlock("LEAVES")) {
        	BlockPlanks.EnumType leave = ((BlockLeaves) block.getBlock()).getWoodType(block.getBlock().getMetaFromState(block));
			if (leave == EnumType.OAK && dropApple.isToggled()) list.add(new ItemStack(MCVer.getItem("APPLE")));
			if (dropSapling.isToggled()) list.add(new ItemStack(Item.getItemFromBlock(MCVer.getBlock("SAPLING")), 1, leave.getMetadata()));
        } else if (block.getBlock().getDefaultState().getBlock() == MCVer.getBlock("LEAVES2")) {
        	BlockPlanks.EnumType leave = ((BlockLeaves) block.getBlock()).getWoodType(block.getBlock().getMetaFromState(block));
			if (leave == EnumType.DARK_OAK && dropApple.isToggled()) list.add(new ItemStack(MCVer.getItem("APPLE")));
			if (dropSapling.isToggled()) list.add(new ItemStack(Item.getItemFromBlock(MCVer.getBlock("SAPLING")), 1, leave.getMetadata()));
        } else {
        	return ImmutableList.<ItemStack>of();
        }
        return list;
    }

    @Override
    public List<ItemStack> redirectDrops(Entity entity) {
        return ImmutableList.of();
    }

    @Override
    public void update() {
        updateX(enabled, x);
        updateY(enabled, y);
        updateX(dropApple, x);
        updateY(dropApple, y + 96);
        updateX(dropSapling, x + 22);
        updateY(dropSapling, y + 96);
    }

    @Override
    public void mouseAction(int mouseX, int mouseY, int button) {
        enabled.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY);
        if (enabled.isChecked()) {
            dropApple.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY);
            dropSapling.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY);
        }
    }

    @Override
    public void render(int mouseX, int mouseY, float delta) {
        enabled.render(mouseX, mouseY, delta);

        if (!enabled.isChecked()) {
            GlStateManager.color(.5f, .5f, .5f, .4f);
        } else {
            MCVer.getFontRenderer(Minecraft.getMinecraft()).drawStringWithShadow("Leaves drop:" + (dropApple.isToggled() ? " 1 Apple" : "") + (dropSapling.isToggled() ? " 1 Sapling" : ""), x, y + 64, 0xFFFFFF);
            dropApple.render(mouseX, mouseY, delta);
            dropSapling.render(mouseX, mouseY, delta);
        }

        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("lotas", "drops/leave.png"));
        Gui.drawModalRectWithCustomSizedTexture(width - 128, y + 24, 0.0F, 0.0F, 96, 96, 96, 96);
    }

}
