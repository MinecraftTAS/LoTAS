package de.pfannekuchen.lotas.dropmanipulation.drops.entitydrops;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;

import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.gui.GuiDropChanceManipulation;
import de.pfannekuchen.lotas.gui.widgets.CheckboxWidget;
import de.pfannekuchen.lotas.gui.widgets.ImageWidget;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ZombieDropManipulation extends GuiDropChanceManipulation.DropManipulation {

    public static ImageWidget dropIron = new ImageWidget(x, y, c -> {
        ZombieDropManipulation.dropPotato.setToggled(false);
        ZombieDropManipulation.dropCarrot.setToggled(false);
        ZombieDropManipulation.dropIron.setToggled(!ZombieDropManipulation.dropIron.isToggled());
    }, new ResourceLocation("lotas", "drops/iron.png"));
    public static ImageWidget dropPotato = new ImageWidget(x, y, c -> {
        ZombieDropManipulation.dropIron.setToggled(false);
        ZombieDropManipulation.dropCarrot.setToggled(false);
        ZombieDropManipulation.dropPotato.setToggled(!ZombieDropManipulation.dropPotato.isToggled());
    }, new ResourceLocation("lotas", "drops/potato.png"));
    public static ImageWidget dropCarrot = new ImageWidget(x, y, c -> {
        ZombieDropManipulation.dropIron.setToggled(false);
        ZombieDropManipulation.dropPotato.setToggled(false);
        ZombieDropManipulation.dropCarrot.setToggled(!ZombieDropManipulation.dropCarrot.isToggled());
    }, new ResourceLocation("lotas", "drops/carrot.png"));

    public ZombieDropManipulation(int x, int y, int width, int height) {
    	ZombieDropManipulation.x = x;
    	ZombieDropManipulation.y = y;
    	ZombieDropManipulation.width = width;
    	ZombieDropManipulation.height = height;
    	enabled = new CheckboxWidget(x, y, 150, 20, "Override Zombie", false);
    }

    @Override
    public String getName() {
        return "Zombie";
    }

    @Override
    public List<ItemStack> redirectDrops(IBlockState block) {  return ImmutableList.of(); }

    @Override
    public List<ItemStack> redirectDrops(Entity entity, int lootingValue) {
        List<ItemStack> list = new ArrayList<>();
        if (entity instanceof EntityZombie && !(entity instanceof EntityPigZombie)) {
            list.add(new ItemStack(MCVer.getItem("ROTTEN_FLESH"), 2 + lootingValue));

            if (dropIron.isToggled()) list.add(new ItemStack(MCVer.getItem("IRON_INGOT")));
            if (dropPotato.isToggled()) list.add(new ItemStack(MCVer.getItem("POTATO")));
            if (dropCarrot.isToggled()) list.add(new ItemStack(MCVer.getItem("CARROT")));

        }
        return list;
    }

    @Override
    public void update() {
    	updateX(enabled, x);
    	updateY(enabled, y);
        updateX(dropIron, x);
        updateY(dropIron, y + 96);
        updateX(dropPotato, x + 22);
        updateY(dropPotato, y + 96);
        updateX(dropCarrot, x + 44);
        updateY(dropCarrot, y + 96);
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
            MCVer.getFontRenderer(Minecraft.getMinecraft()).drawStringWithShadow("Zombies drop: 2 Rotten Flesh" + (dropIron.isToggled() ? ", 1 Iron Ingot" : "") + (dropPotato.isToggled() ? ", 1 Potato" : "") + (dropCarrot.isToggled() ? ", 1 Carrot" : ""), x, y + 64, 0xFFFFFF);
            dropIron.render(mouseX, mouseY, delta);
            dropPotato.render(mouseX, mouseY, delta);
            dropCarrot.render(mouseX, mouseY, delta);
        }

        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("lotas", "drops/zombie.png"));
        int scaleX=30;
		int scaleY=50;
        Gui.drawModalRectWithCustomSizedTexture(width - 110, y + 24, 0.0F, 0.0F, 118-scaleX, 198-scaleY, 118-scaleX, 198-scaleY);
    }

}
