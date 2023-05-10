package de.pfannekuchen.lotas.dropmanipulation.drops.blockdrops;

import java.util.List;

import com.google.common.collect.ImmutableList;

import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.gui.DropManipulationScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class GravelDropManipulation extends DropManipulationScreen.DropManipulation {

	public static boolean flint = false;

	public static Button dropGravel = MCVer.Button(x, y, 98, 20, I18n.get("dropmanipgui.lotas.blocks.gravel.gravel"), button -> {//"Gravel"
		pressGravel();
	});
	public static Button dropFlint = MCVer.Button(x, y, 98, 20, I18n.get("dropmanipgui.lotas.blocks.gravel.flint"), button -> {//"Flint"
		pressFlint();
	});

	public static void pressGravel() {
		dropGravel.active = false;
		dropFlint.active = true;
		flint = false;
	}

	public static void pressFlint() {
		dropFlint.active = false;
		dropGravel.active = true;
		flint = true;
	}

	public GravelDropManipulation(int x, int y, int width, int height) {
		GravelDropManipulation.x = x;
		GravelDropManipulation.y = y;
		GravelDropManipulation.width = width;
		GravelDropManipulation.height = height;
		enabled = MCVer.Checkbox(x, y, 150, 20, I18n.get("dropmanipgui.lotas.blocks.gravel.override"), false);//"Override Gravel Drops"
		dropGravel.active = false;
	}

	@Override
	public String getName() {
		return I18n.get("dropmanipgui.lotas.blocks.gravel.name");//"Gravel"
	}

	@Override
	public List<ItemStack> redirectDrops(BlockState block) {
		if (block.getBlock().defaultBlockState().getBlock() != Blocks.GRAVEL)
			return ImmutableList.of();
		if (flint) {
			return ImmutableList.of(new ItemStack(Items.FLINT));
		}
		return ImmutableList.of(new ItemStack(Items.GRAVEL));
	}

	@Override
	public List<ItemStack> redirectDrops(Entity entity, int lootingBonus) {
		return ImmutableList.of();
	}

	@Override
	public void update() {
		//#if MC>=11903
//$$ 		enabled.setPosition(x, y);
//$$
//$$ 		dropGravel.setPosition(x, y + 96);
//$$ 		dropFlint.setPosition(x, y + 120);
		//#else
		enabled.x = x;
		enabled.y = y;
		dropGravel.x = x;
		dropGravel.y = y + 96;
		dropFlint.x = x;
		dropFlint.y = y + 120;
		//#endif
		
		dropGravel.setWidth(width - x - 128 - 16);
		dropFlint.setWidth(width - x - 128 - 16);
	}

	@Override
	public void mouseAction(double mouseX, double mouseY, int button) {
		enabled.mouseClicked(mouseX, mouseY, button);
		if (enabled.selected()) {
			dropGravel.mouseClicked(mouseX, mouseY, button);
			dropFlint.mouseClicked(mouseX, mouseY, button);
		}
	}

	@Override
	public void render(int mouseX, int mouseY, float delta) {
		MCVer.render(enabled, mouseX, mouseY, delta);

		if (!enabled.selected()) {
			MCVer.color4f(.5f, .5f, .5f, .4f);
		} else {
			MCVer.drawShadow(I18n.get("dropmanipgui.lotas.blocks.gravel.description", (flint ? I18n.get("dropmanipgui.lotas.blocks.gravel.flint") : I18n.get("dropmanipgui.lotas.blocks.gravel.gravel"))), x, y + 64, 0xFFFFFF);//"Drop %s when breaking Gravel"
			MCVer.render(dropGravel, mouseX, mouseY, delta);
			MCVer.render(dropFlint, mouseX, mouseY, delta);
		}

		MCVer.bind(Minecraft.getInstance().getTextureManager(), new ResourceLocation("lotas", "drops/gravel.png"));
		MCVer.blit(width - 128, y + 24, 0.0F, 0.0F, 96, 96, 96, 96);
	}

}
