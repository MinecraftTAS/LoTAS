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

public class GlowstoneDropManipulation extends DropManipulationScreen.DropManipulation {

	public static int dust = 2;

	public static Button drop2Glowstonedust = MCVer.Button(x, y, 98, 20, I18n.get("dropmanipgui.lotas.blocks.glowstone.2"), button -> {//"2 Glowstone Dust"
		press2Glowstonedust();
	});
	public static Button drop3Glowstonedust = MCVer.Button(x, y, 98, 20, I18n.get("dropmanipgui.lotas.blocks.glowstone.3"), button -> {//"3 Glowstone Dust"
		press3Glowstonedust();
	});
	public static Button drop4Glowstonedust = MCVer.Button(x, y, 98, 20, I18n.get("dropmanipgui.lotas.blocks.glowstone.4"), button -> {//"4 Glowstone Dust"
		press4Glowstonedust();
	});

	public static void press2Glowstonedust() {
		drop2Glowstonedust.active = false;
		drop3Glowstonedust.active = true;
		drop4Glowstonedust.active = true;
		dust = 2;
	}

	public static void press3Glowstonedust() {
		drop2Glowstonedust.active = true;
		drop3Glowstonedust.active = false;
		drop4Glowstonedust.active = true;
		dust = 3;
	}

	public static void press4Glowstonedust() {
		drop2Glowstonedust.active = true;
		drop3Glowstonedust.active = true;
		drop4Glowstonedust.active = false;
		dust = 4;
	}

	public GlowstoneDropManipulation(int x, int y, int width, int height) {
		GlowstoneDropManipulation.x = x;
		GlowstoneDropManipulation.y = y;
		GlowstoneDropManipulation.width = width;
		GlowstoneDropManipulation.height = height;
		enabled = MCVer.Checkbox(x, y, 150, 20, I18n.get("dropmanipgui.lotas.blocks.glowstone.override"), false);//"Override Glowstone Drops"
		drop2Glowstonedust.active = false;
	}

	@Override
	public String getName() {
		return I18n.get("dropmanipgui.lotas.blocks.glowstone.name");//"Glowstone"
	}

	@Override
	public List<ItemStack> redirectDrops(BlockState block) {
		if (block.getBlock().defaultBlockState().getBlock() != Blocks.GLOWSTONE)
			return ImmutableList.of();
		return ImmutableList.of(new ItemStack(Items.GLOWSTONE_DUST, dust));
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
//$$ 		drop2Glowstonedust.setPosition(x, y + 96);
//$$ 		drop3Glowstonedust.setPosition(x, y + 120);
//$$ 		drop4Glowstonedust.setPosition(x, y + 144);
		//#else
		enabled.x = x;
		enabled.y = y;

		drop2Glowstonedust.x = x;
		drop2Glowstonedust.y = y + 96;
		drop3Glowstonedust.x = x;
		drop3Glowstonedust.y = y + 120;
		drop4Glowstonedust.x = x;
		drop4Glowstonedust.y = y + 144;
		//#endif

		drop2Glowstonedust.setWidth(width - x - 128 - 16);
		drop3Glowstonedust.setWidth(width - x - 128 - 16);
		drop4Glowstonedust.setWidth(width - x - 128 - 16);
	}

	@Override
	public void mouseAction(double mouseX, double mouseY, int button) {
		enabled.mouseClicked(mouseX, mouseY, button);
		if (enabled.selected()) {
			drop2Glowstonedust.mouseClicked(mouseX, mouseY, button);
			drop3Glowstonedust.mouseClicked(mouseX, mouseY, button);
			drop4Glowstonedust.mouseClicked(mouseX, mouseY, button);
		}
	}

	@Override
	public void render(int mouseX, int mouseY, float delta) {
		MCVer.render(enabled, mouseX, mouseY, delta);
		
		if (!enabled.selected()) {
			MCVer.color4f(0.5f, 0.5f, 0.5f, 0.4f);
		} else {
			MCVer.drawShadow(I18n.get("dropmanipgui.lotas.blocks.glowstone.description", dust), x, y + 64, 0xFFFFFF);//"Drop %i Glowstone Dust when breaking Glowstone"
			MCVer.render(drop4Glowstonedust, mouseX, mouseY, delta);
			MCVer.render(drop3Glowstonedust, mouseX, mouseY, delta);
			MCVer.render(drop2Glowstonedust, mouseX, mouseY, delta);
		}

		MCVer.bind(Minecraft.getInstance().getTextureManager(), new ResourceLocation("lotas", "drops/glowstone.png"));
		MCVer.blit(width - 128, y + 24, 0.0F, 0.0F, 96, 96, 96, 96);
	}

}
