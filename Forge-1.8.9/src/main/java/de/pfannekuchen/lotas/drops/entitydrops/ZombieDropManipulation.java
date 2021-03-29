package de.pfannekuchen.lotas.drops.entitydrops;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;

import de.pfannekuchen.lotas.gui.GuiLootManipulation;
import de.pfannekuchen.lotas.gui.parts.CheckboxWidget;
import de.pfannekuchen.lotas.gui.parts.ImageButton;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ZombieDropManipulation extends GuiLootManipulation.DropManipulation {

    public static ImageButton dropIron = new ImageButton(x, y, c -> {
        ZombieDropManipulation.dropPotato.setToggled(false);
        ZombieDropManipulation.dropCarrot.setToggled(false);
        ZombieDropManipulation.dropIron.setToggled(!ZombieDropManipulation.dropIron.isToggled());
    }, new ResourceLocation("lotas", "drops/iron.png"));
    public static ImageButton dropPotato = new ImageButton(x, y, c -> {
        ZombieDropManipulation.dropIron.setToggled(false);
        ZombieDropManipulation.dropCarrot.setToggled(false);
        ZombieDropManipulation.dropPotato.setToggled(!ZombieDropManipulation.dropPotato.isToggled());
    }, new ResourceLocation("lotas", "drops/potato.png"));
    public static ImageButton dropCarrot = new ImageButton(x, y, c -> {
        ZombieDropManipulation.dropIron.setToggled(false);
        ZombieDropManipulation.dropPotato.setToggled(false);
        ZombieDropManipulation.dropCarrot.setToggled(!ZombieDropManipulation.dropCarrot.isToggled());
    }, new ResourceLocation("lotas", "drops/carrot.png"));

    public ZombieDropManipulation(int x, int y, int width, int height) {
    	ZombieDropManipulation.x = x;
    	ZombieDropManipulation.y = y;
    	ZombieDropManipulation.width = width;
    	ZombieDropManipulation.height = height;
        enabled = new CheckboxWidget(x, y, 150, 20, "Override Zombie/Zombie-Villager/Husk Drops", false);
    }

    @Override
    public String getName() {
        return "Zombie";
    }

    @Override
    public List<ItemStack> redirectDrops(IBlockState block) {  return ImmutableList.of(); }

    @Override
    public List<ItemStack> redirectDrops(Entity entity) {
        List<ItemStack> list = new ArrayList<>();
        if (entity instanceof EntityZombie) {
            list.add(new ItemStack(Items.rotten_flesh, 2));

            if (dropIron.isToggled()) list.add(new ItemStack(Items.iron_ingot));
            if (dropPotato.isToggled()) list.add(new ItemStack(Items.potato));
            if (dropCarrot.isToggled()) list.add(new ItemStack(Items.carrot));

        }
        return list;
    }

    @Override
    public void update() {
        enabled.xPosition = x;
        enabled.yPosition = y;
        dropIron.xPosition = x;
        dropIron.yPosition = y + 96;
        dropPotato.xPosition = x + 22;
        dropPotato.yPosition = y + 96;
        dropCarrot.xPosition = x + 44;
        dropCarrot.yPosition = y + 96;
    }

    @Override
    public void mouseAction(int mouseX, int mouseY, int button) {
        enabled.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY);
        if (enabled.isChecked()) {
            dropIron.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY);
            dropPotato.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY);
            dropCarrot.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY);
        }
    }

    @Override
    public void render(int mouseX, int mouseY, float delta) {
        enabled.render(mouseX, mouseY, delta);

        if (!enabled.isChecked()) {
            GlStateManager.color(.5f, .5f, .5f, .4f);
        } else {
            Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow("Zombies drop: 2 Rotten Flesh" + (dropIron.isToggled() ? ", 1 Iron Ingot" : "") + (dropPotato.isToggled() ? ", 1 Potato" : "") + (dropCarrot.isToggled() ? ", 1 Carrot" : ""), x, y + 64, 0xFFFFFF);
            dropIron.render(mouseX, mouseY, delta);
            dropPotato.render(mouseX, mouseY, delta);
            dropCarrot.render(mouseX, mouseY, delta);
        }

        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("lotas", "drops/zombie.png"));
        Gui.drawModalRectWithCustomSizedTexture(width - 228, y + 24, 0.0F, 0.0F, 118, 198, 118, 198);
    }

}
