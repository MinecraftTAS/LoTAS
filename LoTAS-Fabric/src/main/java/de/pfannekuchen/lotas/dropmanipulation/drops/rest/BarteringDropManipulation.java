package de.pfannekuchen.lotas.dropmanipulation.drops.rest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import com.google.common.collect.ImmutableList;

import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.gui.DropManipulationScreen;
import de.pfannekuchen.lotas.gui.widgets.DropdownWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class BarteringDropManipulation extends DropManipulationScreen.DropManipulation {

	public static BarteringDrops f;
	
	public static DropdownWidget<BarteringDrops> elementwidgets = new DropdownWidget<BarteringDrops>(Minecraft.getInstance().font, 
			Arrays.asList(BarteringDrops.values()), new Function<BarteringDrops, String>() {

		@Override
		public String apply(BarteringDrops t) {
			return t.toString();
		}
	}, 0, 0, 200, 20, I18n.get("dropmanipgui.lotas.entity.bartering.boxname"), (s) -> {//"Bartering Rng"
		f = s;
	});
	
	public enum BarteringDrops{
		Soul_Speed_Boots(I18n.get("dropmanipgui.lotas.entity.bartering.soulboots")),//"Soul Speed Boots"
		Soul_Speed_Book(I18n.get("dropmanipgui.lotas.entity.bartering.soulbook")),//"Soul Speed Book"
		Splash_Potion_of_Fire_Resistance(I18n.get("dropmanipgui.lotas.entity.bartering.splashfireres")),//"Splash Potion of Fire Resistance"
		Potion_of_Fire_Resistance(I18n.get("dropmanipgui.lotas.entity.bartering.fireres")),//"Potion of Fire Resistance"
		Iron_Nugget(I18n.get("dropmanipgui.lotas.entity.bartering.ironnugget")),//"Iron Nugget"
		Ender_Pearl(I18n.get("dropmanipgui.lotas.entity.bartering.enderpearl")),//"Ender Pearl"
		StringItem(I18n.get("dropmanipgui.lotas.entity.bartering.string")),//"String"
		Nether_Quartz(I18n.get("dropmanipgui.lotas.entity.bartering.netherquartz")),//"Nether Quartz"
		Obsidian(I18n.get("dropmanipgui.lotas.entity.bartering.obsidian")),//"Obsidian"
		Crying_Obsidian(I18n.get("dropmanipgui.lotas.entity.bartering.cryingobsidian")),//"Crying Obsidian"
		Fire_Charge(I18n.get("dropmanipgui.lotas.entity.bartering.firecharge")),//"Fire Charge"
		Leather(I18n.get("dropmanipgui.lotas.entity.bartering.leather")),//"Leather"
		Soul_Sand(I18n.get("dropmanipgui.lotas.entity.bartering.soulsand")),//"Soul Sand"
		Nether_Brick(I18n.get("dropmanipgui.lotas.entity.bartering.netherbrick")),//"Nether Brick"
		Arrow(I18n.get("dropmanipgui.lotas.entity.bartering.arrow")),//"Arrow"
		Gravel(I18n.get("dropmanipgui.lotas.entity.bartering.gravel")),//"Gravel"
		Glowstone_Dust(I18n.get("dropmanipgui.lotas.entity.bartering.glowstonedust")),//"Glowstone Dust"
		//#if MC>=11602
//$$ 		Water_Bottle(I18n.get("dropmanipgui.lotas.entity.bartering.waterbottle")),//"Water Bottle"
//$$ 		Spectral_Arrow(I18n.get("dropmanipgui.lotas.entity.bartering.spectralarrow")),//"Spectral Arrow"
//$$ 		Blackstone(I18n.get("dropmanipgui.lotas.entity.bartering.blackstone"));//"Blackstone"
		//#else
		Magma_Cream(I18n.get("dropmanipgui.lotas.entity.bartering.magmacream"));//"Magma Cream"
		//#endif
		private String name;

		private BarteringDrops(String name) {
			this.name = name;
		}
		
		@Override
		public String toString() {
			return name;
		}
	}
	
	public BarteringDropManipulation(int x, int y, int width, int height) {
		BarteringDropManipulation.x = x;
		BarteringDropManipulation.y = y;
		BarteringDropManipulation.width = width;
		BarteringDropManipulation.height = height;
		enabled = MCVer.Checkbox(x, y, 150, 20, I18n.get("dropmanipgui.lotas.entity.bartering.override"), false);//"Override Bartering Drops"
	}

	@Override
	public String getName() {
		return I18n.get("dropmanipgui.lotas.entity.bartering.name");//"Bartering"
	}

	@Override
	public List<ItemStack> redirectDrops(BlockState block) {
		return ImmutableList.of();
	}

	@Override
	public List<ItemStack> redirectDrops(Entity entity, int lootingBonus) {
		//#if MC>=11601
//$$ 		if (entity == null) {
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
//$$ 			switch (f) {
//$$ 				case Soul_Speed_Boots: return ImmutableList.of(soulspeed(new ItemStack(net.minecraft.world.item.Items.IRON_BOOTS)));
//$$ 				case Soul_Speed_Book: return ImmutableList.of();
//$$ 				case Splash_Potion_of_Fire_Resistance: return ImmutableList.of(net.minecraft.world.item.alchemy.PotionUtils.setPotion(new ItemStack(net.minecraft.world.item.Items.SPLASH_POTION), net.minecraft.world.item.alchemy.Potions.FIRE_RESISTANCE));
//$$ 				case Potion_of_Fire_Resistance: return ImmutableList.of(net.minecraft.world.item.alchemy.PotionUtils.setPotion(new ItemStack(net.minecraft.world.item.Items.POTION), net.minecraft.world.item.alchemy.Potions.FIRE_RESISTANCE));
//$$ 				case Iron_Nugget: return ImmutableList.of(new ItemStack(net.minecraft.world.item.Items.IRON_NUGGET, 36));
//$$ 				case Ender_Pearl: return ImmutableList.of(new ItemStack(net.minecraft.world.item.Items.ENDER_PEARL, pearl));
//$$ 				case StringItem: return ImmutableList.of(new ItemStack(net.minecraft.world.item.Items.STRING, string));
//$$ 				case Nether_Quartz: return ImmutableList.of(new ItemStack(net.minecraft.world.item.Items.QUARTZ, quartz));
//$$ 				case Obsidian: return ImmutableList.of(new ItemStack(net.minecraft.world.item.Items.OBSIDIAN, 1));
//$$ 				case Crying_Obsidian: return ImmutableList.of(new ItemStack(net.minecraft.world.item.Items.CRYING_OBSIDIAN, 3));
//$$ 				case Fire_Charge: return ImmutableList.of(new ItemStack(net.minecraft.world.item.Items.FIRE_CHARGE, firecharge));
//$$ 				case Leather: return ImmutableList.of(new ItemStack(net.minecraft.world.item.Items.LEATHER, leather));
//$$ 				case Soul_Sand: return ImmutableList.of(new ItemStack(net.minecraft.world.item.Items.SOUL_SAND, soulsand));
//$$ 				case Nether_Brick: return ImmutableList.of(new ItemStack(net.minecraft.world.item.Items.NETHER_BRICK, netherbrick));
//$$ 				case Arrow: return ImmutableList.of(new ItemStack(net.minecraft.world.item.Items.ARROW, 12));
//$$ 				case Gravel: return ImmutableList.of(new ItemStack(net.minecraft.world.item.Items.GRAVEL, 16));
//$$ 				case Glowstone_Dust: return ImmutableList.of(new ItemStack(net.minecraft.world.item.Items.GLOWSTONE_DUST, 12));
				//#if MC>=11602
//$$ 				case Water_Bottle: return ImmutableList.of(net.minecraft.world.item.alchemy.PotionUtils.setPotion(new ItemStack(net.minecraft.world.item.Items.POTION), net.minecraft.world.item.alchemy.Potions.WATER));
//$$ 				case Spectral_Arrow: return ImmutableList.of(new ItemStack(net.minecraft.world.item.Items.SPECTRAL_ARROW, 16));
//$$ 				case Blackstone: return ImmutableList.of(new ItemStack(net.minecraft.world.item.Items.BLACKSTONE, 16));
				//#else
//$$ 				case Magma_Cream: return ImmutableList.of(new ItemStack(net.minecraft.world.item.Items.MAGMA_CREAM, 6));
				//#endif
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
			//#if MC>=11904
//$$ 			elementwidgets.render(MCVer.stack, mouseX, mouseY, delta);
			//#else
			//#if MC>=11601
//$$ 			elementwidgets.renderBg(MCVer.stack, Minecraft.getInstance(), mouseX, mouseY);
			//#else
			elementwidgets.renderBg(Minecraft.getInstance(), mouseX, mouseY);
			//#endif
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
