package de.pfannekuchen.lotas.dropmanipulation.drops.rest;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.platform.GlStateManager;

import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.gui.DropManipulationScreen;
import de.pfannekuchen.lotas.gui.widgets.DropdownWidget;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.util.Identifier;

public class BarteringDropManipulation extends DropManipulationScreen.DropManipulation {

	public static String f;
	
	public static DropdownWidget<String> elementwidgets = new DropdownWidget<String>(MinecraftClient.getInstance().textRenderer, 
			//#if MC>=11602
			//$$ Arrays.asList("Soul Speed Boots", "Soul Speed Book", "Splash Potion of Fire Resistance", "Potion of Fire Resistance", "Water Bottle", "Iron Nugget", "Ender Pearl", "String", "Nether Quartz", "Obsidian", "Crying Obsidian", "Fire Charge", "Leather", "Soul Sand", "Nether Brick", "Spectral Arrow", "Arrow", "Gravel", "Blackstone")
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
		enabled = MCVer.CheckboxWidget(x, y, 150, 20, "Override Bartering Drops", false);
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
	public List<ItemStack> redirectDrops(Entity entity) {
		//#if MC>=11601
//$$ 		/*"soul speed boots", "soul speed book", "splash potion of fire resistance", 
//$$ 		"potion of fire resistance", "iron nugget", 
//$$ 		"ender pearl", "string", "nether quartz", "obsidian", 
//$$ 		"crying obsidian", "fire charge", "leather", "soul sand",
//$$ 		"nether brick", "arrow", "gravel", "magma cream",
//$$ 		"glowstone dust"*/
//$$ 		if (entity == null) {
//$$ 			System.out.println(f.toLowerCase());
//$$ 			switch (f.toLowerCase()) {
//$$ 				case "soul speed boots": return ImmutableList.of(soulspeed(new ItemStack(Items.IRON_BOOTS)));
//$$ 				case "soul speed book": return ImmutableList.of();
//$$ 				case "splash potion of fire resistance": return ImmutableList.of(PotionUtil.setPotion(new ItemStack(Items.SPLASH_POTION), Potions.FIRE_RESISTANCE));
//$$ 				case "potion of fire resistance": return ImmutableList.of(PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.FIRE_RESISTANCE));
//$$ 				case "iron nugget": return ImmutableList.of(new ItemStack(Items.IRON_NUGGET, 36));
//$$ 				case "ender pearl": return ImmutableList.of(new ItemStack(Items.ENDER_PEARL, 4));
//$$ 				case "string": return ImmutableList.of(new ItemStack(Items.STRING, 9));
//$$ 				case "nether quartz": return ImmutableList.of(new ItemStack(Items.QUARTZ, 12));
//$$ 				case "obsidian": return ImmutableList.of(new ItemStack(Items.OBSIDIAN, 1));
//$$ 				case "crying obsidian": return ImmutableList.of(new ItemStack(Items.CRYING_OBSIDIAN, 3));
//$$ 				case "fire charge": return ImmutableList.of(new ItemStack(Items.FIRE_CHARGE, 1));
//$$ 				case "leather": return ImmutableList.of(new ItemStack(Items.LEATHER, 4));
//$$ 				case "soul sand": return ImmutableList.of(new ItemStack(Items.SOUL_SAND, 8));
//$$ 				case "nether brick": return ImmutableList.of(new ItemStack(Items.NETHER_BRICK, 8));
//$$ 				case "spectral arrow": return ImmutableList.of(new ItemStack(Items.SPECTRAL_ARROW, 16));
//$$ 				case "arrow": return ImmutableList.of(new ItemStack(Items.ARROW, 12));
//$$ 				case "gravel": return ImmutableList.of(new ItemStack(Items.GRAVEL, 16));
//$$ 				case "magma cream": return ImmutableList.of();
//$$ 				case "glowstone dust": return ImmutableList.of();
//$$ 				case "blackstone": return ImmutableList.of(new ItemStack(Items.BLACKSTONE, 16));
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
//$$ 		itemStack.addEnchantment(Enchantments.SOUL_SPEED, 3);
//$$ 		return itemStack;
//$$ 	}
	//#endif
	
	@Override
	public void update() {
		enabled.x = x;
		enabled.y = y;
		elementwidgets.x = x;
		elementwidgets.y = y + 80;
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
		elementwidgets.mouseScrolled(e, e, amount);
	}
	
	@Override
	public void mouseAction(double mouseX, double mouseY, int button) {
		enabled.mouseClicked(mouseX, mouseY, button);
		if (enabled.isChecked()) {
			elementwidgets.mouseClicked(mouseX, mouseY, button);
		}
	}

	@Override
	public void render(Object matrices, int mouseX, int mouseY, float delta) {
		MCVer.render(enabled, mouseX, mouseY, delta);

		if (!enabled.isChecked()) {
			MCVer.color(.5f, .5f, .5f, .4f);
		} else {
			GlStateManager.pushMatrix();
			//#if MC>=11601
//$$ 			elementwidgets.render((net.minecraft.client.util.math.MatrixStack) matrices, mouseX, mouseY, delta);
//$$ 			elementwidgets.renderBg((net.minecraft.client.util.math.MatrixStack) matrices, MinecraftClient.getInstance(), mouseX, mouseY);
			//#else
			elementwidgets.render(mouseX, mouseY, delta);
			elementwidgets.renderBg(MinecraftClient.getInstance(), mouseX, mouseY);
			//#endif
			GlStateManager.popMatrix();
		}

		MinecraftClient.getInstance().getTextureManager().bindTexture(new Identifier("lotas", "drops/zombie.png"));
		MCVer.renderImage(width - 228, y + 24, 0.0F, 0.0F, 118, 198, 118, 198);
	}

}
