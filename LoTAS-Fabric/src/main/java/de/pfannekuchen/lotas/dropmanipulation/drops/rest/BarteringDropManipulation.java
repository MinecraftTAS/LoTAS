package de.pfannekuchen.lotas.dropmanipulation.drops.rest;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import com.google.common.collect.ImmutableList;

import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.gui.DropManipulationScreen;
import de.pfannekuchen.lotas.gui.widgets.DropdownWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class BarteringDropManipulation extends DropManipulationScreen.DropManipulation {

	public static String f;
	
	public static DropdownWidget<String> elementwidgets = new DropdownWidget<String>(Minecraft.getInstance().font, 
			//#if MC>=11602
//$$ 			Arrays.asList("Soul Speed Boots", "Soul Speed Book", "Splash Potion of Fire Resistance", "Potion of Fire Resistance", "Water Bottle", "Iron Nugget", "Ender Pearl", "String", "Nether Quartz", "Obsidian", "Crying Obsidian", "Fire Charge", "Leather", "Soul Sand", "Nether Brick", "Spectral Arrow", "Arrow", "Gravel", "Blackstone")
			//#else
			Arrays.asList("Soul Speed Boots", "Soul Speed Book", "Splash Potion of Fire Resistance", "Potion of Fire Resistance", "Iron Nugget", "Ender Pearl", "String", "Nether Quartz", "Obsidian", "Crying Obsidian", "Fire Charge", "Leather", "Soul Sand", "Nether Brick", "Arrow", "Gravel", "Magma Cream", "Glowstone Dust")
			//#endif
			, new Function<String, String>() {
		@Override
		public String apply(String t) {
			return t;
		}
	}, 0, 0, 200, 20, "Bartering Rng", "???", (s) -> {
		f = s;
	});
	
	public BarteringDropManipulation(int x, int y, int width, int height) {
		BarteringDropManipulation.x = x;
		BarteringDropManipulation.y = y;
		BarteringDropManipulation.width = width;
		BarteringDropManipulation.height = height;
		enabled = MCVer.Checkbox(x, y, 150, 20, "Override Bartering Drops", false);
	}

	@Override
	public String getName() {
		return "Bartering";
	}

	@Override
	public List<ItemStack> redirectDrops(BlockState block) {
		return ImmutableList.of();
	}

	@Override
	public List<ItemStack> redirectDrops(Entity entity, int lootingBonus) {
		//#if MC>=11601
//$$ 		if (entity == null) {
//$$ 			System.out.println(f.toLowerCase());
			//#if MC>=11605
//$$ 			int quartz=12;
//$$ 			int soulsand=8;
//$$ 			int string=9;
//$$ 			int pearl=4;
//$$ 			int netherbrick=8;
//$$ 			int firecharge=1;
//$$ 			int leather=4;
			//#else
//$$ 			int quartz=16;
//$$ 			int soulsand=16;
//$$ 			int string=24;
//$$ 			int pearl=8;
//$$ 			int netherbrick=16;
//$$ 			int firecharge=5;
//$$ 			int leather=10;
			//#endif
//$$ 			switch (f.toLowerCase()) {
//$$ 				case "soul speed boots": return ImmutableList.of(soulspeed(new ItemStack(net.minecraft.world.item.Items.IRON_BOOTS)));
//$$ 				case "soul speed book": return ImmutableList.of();
//$$ 				case "splash potion of fire resistance": return ImmutableList.of(net.minecraft.world.item.alchemy.PotionUtils.setPotion(new ItemStack(net.minecraft.world.item.Items.SPLASH_POTION), net.minecraft.world.item.alchemy.Potions.FIRE_RESISTANCE));
//$$ 				case "potion of fire resistance": return ImmutableList.of(net.minecraft.world.item.alchemy.PotionUtils.setPotion(new ItemStack(net.minecraft.world.item.Items.POTION), net.minecraft.world.item.alchemy.Potions.FIRE_RESISTANCE));
//$$ 				case "iron nugget": return ImmutableList.of(new ItemStack(net.minecraft.world.item.Items.IRON_NUGGET, 36));
//$$ 				case "ender pearl": return ImmutableList.of(new ItemStack(net.minecraft.world.item.Items.ENDER_PEARL, pearl));
//$$ 				case "string": return ImmutableList.of(new ItemStack(net.minecraft.world.item.Items.STRING, string));
//$$ 				case "nether quartz": return ImmutableList.of(new ItemStack(net.minecraft.world.item.Items.QUARTZ, quartz));
//$$ 				case "obsidian": return ImmutableList.of(new ItemStack(net.minecraft.world.item.Items.OBSIDIAN, 1));
//$$ 				case "crying obsidian": return ImmutableList.of(new ItemStack(net.minecraft.world.item.Items.CRYING_OBSIDIAN, 3));
//$$ 				case "fire charge": return ImmutableList.of(new ItemStack(net.minecraft.world.item.Items.FIRE_CHARGE, firecharge));
//$$ 				case "leather": return ImmutableList.of(new ItemStack(net.minecraft.world.item.Items.LEATHER, leather));
//$$ 				case "soul sand": return ImmutableList.of(new ItemStack(net.minecraft.world.item.Items.SOUL_SAND, soulsand));
//$$ 				case "nether brick": return ImmutableList.of(new ItemStack(net.minecraft.world.item.Items.NETHER_BRICK, netherbrick));
//$$ 				case "spectral arrow": return ImmutableList.of(new ItemStack(net.minecraft.world.item.Items.SPECTRAL_ARROW, 16));
//$$ 				case "arrow": return ImmutableList.of(new ItemStack(net.minecraft.world.item.Items.ARROW, 12));
//$$ 				case "gravel": return ImmutableList.of(new ItemStack(net.minecraft.world.item.Items.GRAVEL, 16));
//$$ 				case "magma cream": return ImmutableList.of(new ItemStack(net.minecraft.world.item.Items.MAGMA_CREAM, 6));
//$$ 				case "glowstone dust": return ImmutableList.of(new ItemStack(net.minecraft.world.item.Items.GLOWSTONE_DUST, 12));
//$$ 				case "blackstone": return ImmutableList.of(new ItemStack(net.minecraft.world.item.Items.BLACKSTONE, 16));
//$$ 				default: return ImmutableList.of();
//$$ 			}
//$$ 		}
//$$ 		return ImmutableList.of();
		//#else
		return ImmutableList.of();
		//#endif
	}

	//#if MC>=11601
//$$ 	private ItemStack soulspeed(ItemStack itemStack) {
//$$ 		itemStack.enchant(net.minecraft.world.item.enchantment.Enchantments.SOUL_SPEED, 3);
//$$ 		return itemStack;
//$$ 	}
	//#endif
	
	@Override
	public void update() {
		//#if MC>=11903
//$$ 		enabled.setPosition(x, y);
//$$
//$$ 		elementwidgets.setPosition(x, y + 80);
		//#else
		enabled.x = x;
		enabled.y = y;
		elementwidgets.x = x;
		elementwidgets.y = y + 80;
		//#endif
	}

	@Override
	public void keyPressed(int keyCode, int scanCode, int modifiers) {
		elementwidgets.keyPressed(keyCode, scanCode, modifiers);
	}
	
	@Override
	public void charTyped(char chr, int keyCode) {
		elementwidgets.charTyped(chr, keyCode);
	}
	
	@Override
	public void mouseScrolled(double d, double e, double amount) {
		elementwidgets.mouseScrolled(d, e, amount);
	}
	
	@Override
	public void mouseAction(double mouseX, double mouseY, int button) {
		enabled.mouseClicked(mouseX, mouseY, button);
		if (enabled.selected()) {
			elementwidgets.mouseClicked(mouseX, mouseY, button);
		}
	}

	@Override
	public void render(int mouseX, int mouseY, float delta) {
		MCVer.render(enabled, mouseX, mouseY, delta);

		if (!enabled.selected()) {
			MCVer.color4f(.5f, .5f, .5f, .4f);
		} else {
			//#if MC>=11700
//$$ 			MCVer.pushMatrix(MCVer.stack);
			//#else
			MCVer.pushMatrix(null);
			//#endif
			MCVer.render(elementwidgets, mouseX, mouseY, delta);
			//#if MC>=11601
//$$ 			elementwidgets.renderBg(MCVer.stack, Minecraft.getInstance(), mouseX, mouseY);
			//#else
			elementwidgets.renderBg(Minecraft.getInstance(), mouseX, mouseY);
			//#endif
			//#if MC>=11700
//$$ 			MCVer.popMatrix(MCVer.stack);
			//#else
			MCVer.popMatrix(null);
			//#endif
		}
		
		MCVer.bind(Minecraft.getInstance().getTextureManager(), new ResourceLocation("lotas", "drops/piglin.png"));
		int scaleX=10;
		int scaleY=50;
		MCVer.blit(width - 130, y + 24, 0.0F, 0.0F, 118-scaleX, 198-scaleY, 118-scaleX, 198-scaleY);
	}

}
