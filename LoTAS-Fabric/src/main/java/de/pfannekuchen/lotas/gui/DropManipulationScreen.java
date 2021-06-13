package de.pfannekuchen.lotas.gui;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.platform.GlStateManager;

import de.pfannekuchen.lotas.dropmanipulation.drops.blockdrops.DeadbushDropManipulation;
import de.pfannekuchen.lotas.dropmanipulation.drops.blockdrops.GlowstoneDropManipulation;
import de.pfannekuchen.lotas.dropmanipulation.drops.blockdrops.GravelDropManipulation;
import de.pfannekuchen.lotas.dropmanipulation.drops.blockdrops.LeaveDropManipulation;
import de.pfannekuchen.lotas.dropmanipulation.drops.blockdrops.OreDropManipulation;
import de.pfannekuchen.lotas.dropmanipulation.drops.blockdrops.PlantsDropManipulation;
import de.pfannekuchen.lotas.dropmanipulation.drops.blockdrops.SealanternDropManipulation;
import de.pfannekuchen.lotas.dropmanipulation.drops.entitydrops.DrownedDropManipulation;
import de.pfannekuchen.lotas.dropmanipulation.drops.entitydrops.FishDropManipulation;
import de.pfannekuchen.lotas.dropmanipulation.drops.entitydrops.MonsterDropManipulation;
import de.pfannekuchen.lotas.dropmanipulation.drops.entitydrops.NetherMobDropManipulation;
import de.pfannekuchen.lotas.dropmanipulation.drops.entitydrops.PassiveDropManipulation;
import de.pfannekuchen.lotas.dropmanipulation.drops.entitydrops.ZombieDropManipulation;
import net.minecraft.block.BlockState;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;

public class DropManipulationScreen extends Screen {

    public static ArrayList<DropManipulation> manipulations;
    public static int selected = 0;

    static {
        manipulations = new ArrayList<>();
        manipulations.add(new GravelDropManipulation(0, 0, 0, 0));
        manipulations.add(new LeaveDropManipulation(0, 0, 0, 0));
        manipulations.add(new PlantsDropManipulation(0, 0, 0, 0));
        manipulations.add(new DeadbushDropManipulation(0, 0, 0, 0));
        manipulations.add(new GlowstoneDropManipulation(0, 0, 0, 0));
        manipulations.add(new SealanternDropManipulation(0, 0, 0, 0));
        manipulations.add(new OreDropManipulation(0, 0, 0, 0));
        manipulations.add(new NetherMobDropManipulation(0, 0, 0, 0));
        manipulations.add(new PassiveDropManipulation(0, 0, 0, 0));
        manipulations.add(new MonsterDropManipulation(0, 0, 0, 0));
        manipulations.add(new ZombieDropManipulation(0, 0, 0, 0));
        manipulations.add(new DrownedDropManipulation(0, 0, 0, 0));
        manipulations.add(new FishDropManipulation(0, 0, 0, 0));
    }

    public DropManipulationScreen(GameMenuScreen gameMenuScreen) {
        super(new LiteralText("Drop Manipulation Screen"));
    }

    @Override
    protected void init() {
        for (DropManipulation man : manipulations) {
            DropManipulation.width = width;
            DropManipulation.height = height;
            DropManipulation.y = 25;
            DropManipulation.x = (int) (width / 3.5f + 24);
            man.update();
        }
        super.init();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        manipulations.get(selected).mouseAction(mouseX, mouseY, button);
        if (mouseX > width / 3.5f || mouseX < 25) return false;
        mouseY-=30;
        mouseY /= 15;
        if (mouseY < 0) return false;
        if (manipulations.size() - 1 >= (int) mouseY)
            selected = (int) mouseY;
        return false;
    }

    @Override
    public void render(int mouseX, int mouseY, float delta) {
        renderBackground();

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();

        GlStateManager.disableTexture();
        GlStateManager.color4f(.5f, .5f, .5f, 0.5F);
        bufferBuilder.begin(7, VertexFormats.POSITION);
        bufferBuilder.vertex(24, 24, 0).next();
        bufferBuilder.vertex(24, height - 24, 0).next();
        bufferBuilder.vertex(width / 3.5f + 1, height - 24, 0).next();
        bufferBuilder.vertex(width / 3.5f + 1, 24, 0).next();
        tessellator.draw();

        GlStateManager.color4f(0, 0, 0, 0.5F);
        bufferBuilder.begin(7, VertexFormats.POSITION);
        bufferBuilder.vertex(25, 25, 0).next();
        bufferBuilder.vertex(25, height - 25, 0).next();
        bufferBuilder.vertex(width / 3.5f, height - 25, 0).next();
        bufferBuilder.vertex(width / 3.5f, 25, 0).next();
        tessellator.draw();

        int boxY = 30 + selected * 15;
        GlStateManager.color4f(1f, 1f, 1f, 1F);
        bufferBuilder.begin(7, VertexFormats.POSITION);
        bufferBuilder.vertex(27, boxY - 4, 0).next();
        bufferBuilder.vertex(27, boxY + 11, 0).next();
        bufferBuilder.vertex(width / 3.5f - 2, boxY + 11, 0).next();
        bufferBuilder.vertex(width / 3.5f - 2, boxY - 4, 0).next();
        tessellator.draw();
        GlStateManager.color4f(0f, 0f, 0f, .5F);
        bufferBuilder.begin(7, VertexFormats.POSITION);
        bufferBuilder.vertex(28, boxY - 3, 0).next();
        bufferBuilder.vertex(28, boxY + 10, 0).next();
        bufferBuilder.vertex(width / 3.5f - 3, boxY + 10, 0).next();
        bufferBuilder.vertex(width / 3.5f - 3, boxY - 3, 0).next();
        tessellator.draw();

        GlStateManager.enableTexture();
        int y = 30;
        for (DropManipulation m : manipulations) {
            drawString(minecraft.textRenderer, m.getName(), 32, y, 0xFFFFFF);
            y += 15;
        }
        manipulations.get(selected).render(mouseX, mouseY, delta);

        super.render(mouseX, mouseY, delta);
    }

    public static abstract class DropManipulation {
        public static int x;
        public static int y;
        public static int width;
        public static int height;
        public CheckboxWidget enabled;
        public abstract String getName();
        public abstract List<ItemStack> redirectDrops(BlockState block);
        public abstract List<ItemStack> redirectDrops(Entity entity);
        public abstract void update();
        public abstract void mouseAction(double mouseX, double mouseY, int button);
        public abstract void render(int mouseX, int mouseY, float delta);
    }
}
