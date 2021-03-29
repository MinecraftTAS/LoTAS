package de.pfannekuchen.lotas.drops.blockdrops;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;

import de.pfannekuchen.lotas.gui.GuiLootManipulation;
import de.pfannekuchen.lotas.gui.parts.CheckboxWidget;
import de.pfannekuchen.lotas.gui.parts.ImageButton;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockPlanks.EnumType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class LeaveDropManipulation extends GuiLootManipulation.DropManipulation {

    public static ImageButton dropApple = new ImageButton(x, y, c -> {LeaveDropManipulation.dropApple.setToggled(!LeaveDropManipulation.dropApple.isToggled());}, new ResourceLocation("lotas", "drops/apple.png"));
    public static ImageButton dropSapling = new ImageButton(x, y, c -> {LeaveDropManipulation.dropSapling.setToggled(!LeaveDropManipulation.dropSapling.isToggled());}, new ResourceLocation("lotas", "drops/sapling.png"));

    public LeaveDropManipulation(int x, int y, int width, int height) {
    	LeaveDropManipulation.x = x;
    	LeaveDropManipulation.y = y;
    	LeaveDropManipulation.width = width;
        LeaveDropManipulation.height = height;
        enabled = new CheckboxWidget(x, y, 150, 20, "Override Leave Drops", false);
    }

    @Override
    public String getName() {
        return "Leaves";
    }

    @Override
    public List<ItemStack> redirectDrops(IBlockState block) {
        List<ItemStack> list = new ArrayList<>();
        
        if (block.getBlock().getDefaultState().getBlock() == Blocks.leaves) {
        	BlockPlanks.EnumType leave = ((BlockLeaves) block.getBlock()).getWoodType(block.getBlock().getMetaFromState(block));
			if (leave == EnumType.OAK && dropApple.isToggled()) list.add(new ItemStack(Items.apple));
			if (dropSapling.isToggled()) list.add(new ItemStack(Item.getItemFromBlock(Blocks.sapling), 1, leave.getMetadata()));
        } else if (block.getBlock().getDefaultState().getBlock() == Blocks.leaves2) {
        	BlockPlanks.EnumType leave = ((BlockLeaves) block.getBlock()).getWoodType(block.getBlock().getMetaFromState(block));
			if (leave == EnumType.DARK_OAK && dropApple.isToggled()) list.add(new ItemStack(Items.apple));
			if (dropSapling.isToggled()) list.add(new ItemStack(Item.getItemFromBlock(Blocks.sapling), 1, leave.getMetadata()));
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
        enabled.xPosition = x;
        enabled.yPosition = y;
        dropApple.xPosition = x;
        dropApple.yPosition = y + 96;
        dropSapling.xPosition = x + 22;
        dropSapling.yPosition = y + 96;
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
            Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow("Leaves drop:" + (dropApple.isToggled() ? " 1 Apple" : "") + (dropSapling.isToggled() ? " 1 Sapling" : ""), x, y + 64, 0xFFFFFF);
            dropApple.render(mouseX, mouseY, delta);
            dropSapling.render(mouseX, mouseY, delta);
        }

        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("lotas", "drops/leave.png"));
        Gui.drawModalRectWithCustomSizedTexture(width - 128, y + 24, 0.0F, 0.0F, 96, 96, 96, 96);
    }

}
