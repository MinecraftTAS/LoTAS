package de.pfannekuchen.lotas.gui;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import de.pfannekuchen.lotas.drops.blockdrops.DeadbushDropManipulation;
import de.pfannekuchen.lotas.drops.blockdrops.GlowstoneDropManipulation;
import de.pfannekuchen.lotas.drops.blockdrops.GravelDropManipulation;
import de.pfannekuchen.lotas.drops.blockdrops.LeaveDropManipulation;
import de.pfannekuchen.lotas.drops.blockdrops.OreDropManipulation;
import de.pfannekuchen.lotas.drops.blockdrops.PlantsDropManipulation;
import de.pfannekuchen.lotas.drops.entitydrops.MonsterDropManipulation;
import de.pfannekuchen.lotas.drops.entitydrops.NetherMobDropManipulation;
import de.pfannekuchen.lotas.drops.entitydrops.PassiveDropManipulation;
import de.pfannekuchen.lotas.drops.entitydrops.ZombieDropManipulation;
import de.pfannekuchen.lotas.gui.parts.CheckboxWidget;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;

public class GuiLootManipulation extends GuiScreen {


    public static ArrayList<DropManipulation> manipulations;
    public static int selected = 0;

    static {
        manipulations = new ArrayList<DropManipulation>();
        manipulations.add(new GravelDropManipulation(0, 0, 0, 0));
        manipulations.add(new LeaveDropManipulation(0, 0, 0, 0));
        manipulations.add(new PlantsDropManipulation(0, 0, 0, 0));
        manipulations.add(new DeadbushDropManipulation(0, 0, 0, 0));
        manipulations.add(new GlowstoneDropManipulation(0, 0, 0, 0));
        manipulations.add(new OreDropManipulation(0, 0, 0, 0));
        manipulations.add(new NetherMobDropManipulation(0, 0, 0, 0));
        manipulations.add(new PassiveDropManipulation(0, 0, 0, 0));
        manipulations.add(new MonsterDropManipulation(0, 0, 0, 0));
        manipulations.add(new ZombieDropManipulation(0, 0, 0, 0));
    }

    public GuiLootManipulation(GuiIngameMenu gameMenuScreen) {
        super();
    }

    @Override
	public void initGui() {
        for (DropManipulation man : manipulations) {
            DropManipulation.width = width;
            DropManipulation.height = height;
            DropManipulation.y = 25;
            DropManipulation.x = (int) (width / 3.5f + 24);
            man.update();
        }
        super.initGui();
    }
    
    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        manipulations.get(selected).mouseAction(mouseX, mouseY, button);
        if (mouseX > width / 3.5f || mouseX < 25) return;
        mouseY-=30;
        mouseY /= 15;
        if (mouseY < 0) return;
        if (manipulations.size() - 1 >= (int) mouseY)
            selected = (int) mouseY;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float delta) {
    	drawDefaultBackground();
        drawBackground(0);
        
        Tessellator tessellator = Tessellator.instance;

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glColor4f(.5f, .5f, .5f, 0.5F);
        tessellator.startDrawing(7);
        tessellator.addVertex(24, 24, 0);
        tessellator.addVertex(24, height - 24, 0);
        tessellator.addVertex(width / 3.5f + 1, height - 24, 0);
        tessellator.addVertex(width / 3.5f + 1, 24, 0);
        tessellator.draw();

        GL11.glColor4f(0, 0, 0, 0.5F);
        tessellator.startDrawing(7);
        tessellator.addVertex(25, 25, 0);
        tessellator.addVertex(25, height - 25, 0);
        tessellator.addVertex(width / 3.5f, height - 25, 0);
        tessellator.addVertex(width / 3.5f, 25, 0);
        tessellator.draw();

        int boxY = 30 + selected * 15;
        GL11.glColor4f(1f, 1f, 1f, 1F);
        tessellator.startDrawing(7);
        tessellator.addVertex(27, boxY - 4, 0);
        tessellator.addVertex(27, boxY + 11, 0);
        tessellator.addVertex(width / 3.5f - 2, boxY + 11, 0);
        tessellator.addVertex(width / 3.5f - 2, boxY - 4, 0);
        tessellator.draw();
        GL11.glColor4f(0f, 0f, 0f, .5F);
        tessellator.startDrawing(7);tessellator.addVertex(28, boxY - 3, 0);
        tessellator.addVertex(28, boxY + 10, 0);
        tessellator.addVertex(width / 3.5f - 3, boxY + 10, 0);
        tessellator.addVertex(width / 3.5f - 3, boxY - 3, 0);
        tessellator.draw();

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        int y = 30;
        for (DropManipulation m : manipulations) {
            drawString(mc.fontRendererObj, m.getName(), 32, y, 0xFFFFFF);
            y += 15;
        }
        manipulations.get(selected).render(mouseX, mouseY, delta);

        super.drawScreen(mouseX, mouseY, delta);
    }

    public static abstract class DropManipulation {
        public static int x;
        public static int y;
        public static int width;
        public static int height;
        public CheckboxWidget enabled;
        public abstract String getName();
        public abstract List<ItemStack> redirectDrops(Block block);
        public abstract List<ItemStack> redirectDrops(Entity entity);
        public abstract void update();
        public abstract void mouseAction(int mouseX, int mouseY, int button);
        public abstract void render(int mouseX, int mouseY, float delta);
    }

}
