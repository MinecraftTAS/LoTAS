package de.pfannekuchen.lotas.dropmanipulation.drops.blockdrops;

import java.util.List;

import com.google.common.collect.ImmutableList;

import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.gui.DropManipulationScreen;
import de.pfannekuchen.lotas.gui.widgets.SmallCheckboxWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class OreDropManipulation extends DropManipulationScreen.DropManipulation {

	public static SmallCheckboxWidget optimizeLapis = new SmallCheckboxWidget(0, 0, I18n.get("dropmanipgui.lotas.blocks.ore.lapis"), false);//"Full Lapis Drops"
	public static SmallCheckboxWidget optimizeRedstone = new SmallCheckboxWidget(0, 0, I18n.get("dropmanipgui.lotas.blocks.ore.redstone"), false);//"Full Redstone Drops"
	//#if MC>=11700
//$$ 	public static SmallCheckboxWidget optimizeCopper = new SmallCheckboxWidget(0, 0, I18n.get("dropmanipgui.lotas.blocks.ore.copper"), false);//"Full Copper Drops"
	//#endif

	public OreDropManipulation(int x, int y, int width, int height) {
		OreDropManipulation.x = x;
		OreDropManipulation.y = y;
		OreDropManipulation.width = width;
		OreDropManipulation.height = height;
		enabled = MCVer.Checkbox(x, y, 150, 20, I18n.get("dropmanipgui.lotas.blocks.ore.override"), false);//"Override Ore Drops"
	}

	@Override
	public String getName() {
		return I18n.get("dropmanipgui.lotas.blocks.ore.name");//"Ores"
	}

	@Override
	public List<ItemStack> redirectDrops(BlockState blockstate) {
		Block block = blockstate.getBlock();
		if (block.defaultBlockState().getBlock() == Blocks.LAPIS_ORE && optimizeLapis.isChecked()) {
			return ImmutableList.of(new ItemStack(Items.LAPIS_LAZULI, 9));
		} else if (block.defaultBlockState().getBlock() == Blocks.REDSTONE_ORE && optimizeRedstone.isChecked()) {
			return ImmutableList.of(new ItemStack(Items.REDSTONE, 5));
		}
		//#if MC>=11700
		//#if MC>=11800
//$$ 		else if(block.defaultBlockState().getBlock() == Blocks.COPPER_ORE && optimizeCopper.isChecked()) {
//$$ 			return ImmutableList.of(new ItemStack(Items.RAW_COPPER, 5));
//$$ 		}
		//#else
//$$ 		else if(block.defaultBlockState().getBlock() == Blocks.COPPER_ORE && optimizeCopper.isChecked()) {
//$$ 			return ImmutableList.of(new ItemStack(Items.RAW_COPPER, 3));
//$$ 		}
		//#endif
		//#endif
		
		return ImmutableList.of();
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
//$$ 		optimizeRedstone.setPosition(x, 64);
//$$ 		optimizeLapis.setPosition(x, 80);
//$$ 		optimizeCopper.setPosition(x, 96);
		//#else
		enabled.x = x;
		enabled.y = y;
		optimizeRedstone.y = 64;
		optimizeLapis.y = 80;
		optimizeRedstone.x = x;
		optimizeLapis.x = x;
		//#if MC>=11700
//$$ 		optimizeCopper.x = x;
//$$ 		optimizeCopper.y = 96;
		//#endif
		//#endif
	}

	@Override
	public void mouseAction(double mouseX, double mouseY, int button) {
		enabled.mouseClicked(mouseX, mouseY, button);
		if (enabled.selected()) {
			optimizeRedstone.mouseClicked(mouseX, mouseY, button);
			optimizeLapis.mouseClicked(mouseX, mouseY, button);
			//#if MC>=11700
//$$ 			optimizeCopper.mouseClicked(mouseX, mouseY, button);
			//#endif
		}
	}

	@Override
	public void render(int mouseX, int mouseY, float delta) {
		MCVer.render(enabled, mouseX, mouseY, delta);

		if (!enabled.selected()) {
			MCVer.color4f(.5f, .5f, .5f, .4f);
		} else {
			MCVer.render(optimizeRedstone, mouseX, mouseY, delta);
			MCVer.render(optimizeLapis, mouseX, mouseY, delta);
			//#if MC>=11700
//$$ 			MCVer.render(optimizeCopper, mouseX, mouseY, delta);
			//#endif
		}

		MCVer.bind(Minecraft.getInstance().getTextureManager(), new ResourceLocation("lotas", "drops/diamond_ore.png"));
		MCVer.blit(width - 128, y + 24, 0.0F, 0.0F, 96, 96, 96, 96);
	}

}
