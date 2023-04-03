package de.pfannekuchen.lotas.dropmanipulation.drops.entitydrops;

import java.util.List;

import com.google.common.collect.ImmutableList;

import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.gui.DropManipulationScreen;
import de.pfannekuchen.lotas.gui.widgets.SmallCheckboxWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Cod;
import net.minecraft.world.entity.animal.Dolphin;
import net.minecraft.world.entity.animal.Pufferfish;
import net.minecraft.world.entity.animal.Salmon;
import net.minecraft.world.entity.animal.TropicalFish;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;

public class FishDropManipulation extends DropManipulationScreen.DropManipulation {

	public static SmallCheckboxWidget optimizeCod = new SmallCheckboxWidget(0, 0, I18n.get("dropmanipgui.lotas.entity.fish.cod"), false);//"Cod drop Bone Meal"
	public static SmallCheckboxWidget optimizeSalmon = new SmallCheckboxWidget(0, 0, I18n.get("dropmanipgui.lotas.entity.fish.salmon"), false);//"Salmon drop Bone Meal"
	public static SmallCheckboxWidget optimizeDolphin = new SmallCheckboxWidget(0, 0, I18n.get("dropmanipgui.lotas.entity.fish.dolphin"), false);//"Dolphin drop 1 Cod"
	public static SmallCheckboxWidget optimizePufferfish = new SmallCheckboxWidget(0, 0, I18n.get("dropmanipgui.lotas.entity.fish.pufferfish"), false);//"Pufferfish drop Bone Meal"
	public static SmallCheckboxWidget optimizeTropical = new SmallCheckboxWidget(0, 0, I18n.get("dropmanipgui.lotas.entity.fish.tropicalfish"), false);//"Tropical Fish drop Bone Meal"

	public FishDropManipulation(int x, int y, int width, int height) {
		FishDropManipulation.x = x;
		FishDropManipulation.y = y;
		FishDropManipulation.width = width;
		FishDropManipulation.height = height;
		enabled = MCVer.Checkbox(x, y, 150, 20, I18n.get("dropmanipgui.lotas.entity.fish.override"), false);//"Override Fish Drops"
	}

	@Override
	public String getName() {
		return I18n.get("dropmanipgui.lotas.entity.fish.name");//"Fish"
	}

	@Override
	public List<ItemStack> redirectDrops(BlockState blockstate) {
		return ImmutableList.of();
	}

	@Override
	public List<ItemStack> redirectDrops(Entity entity, int lootingBonus) {
		if (entity instanceof Cod && optimizeCod.isChecked())
			return ImmutableList.of(new ItemStack(Items.COD), new ItemStack(Items.BONE_MEAL, 1+lootingBonus));
		if (entity instanceof Salmon && optimizeSalmon.isChecked())
			return ImmutableList.of(new ItemStack(Items.SALMON), new ItemStack(Items.BONE_MEAL, 1+lootingBonus));
		if (entity instanceof Dolphin && optimizeDolphin.isChecked())
			return ImmutableList.of(new ItemStack(Items.COD));
		if (entity instanceof Pufferfish && optimizePufferfish.isChecked())
			return ImmutableList.of(new ItemStack(Items.PUFFERFISH, 1), new ItemStack(Items.BONE_MEAL, 1+lootingBonus));
		if (entity instanceof TropicalFish && optimizeTropical.isChecked())
			return ImmutableList.of(new ItemStack(Items.TROPICAL_FISH, 1), new ItemStack(Items.BONE_MEAL, 1+lootingBonus));
		return ImmutableList.of();
	}

	@Override
	public void update() {
		//#if MC>=11903
//$$ 		enabled.setPosition(x, y);
//$$
//$$ 		optimizeCod.setPosition(x, y + 64);
//$$ 		optimizePufferfish.setPosition(x, y + 80);
//$$ 		optimizeDolphin.setPosition(x, y + 96);
//$$ 		optimizeSalmon.setPosition(x, y + 112);
//$$ 		optimizeTropical.setPosition(x, y + 128);
		//#else
		enabled.x = x;
		enabled.y = y;
		optimizeCod.y = 64;
		optimizePufferfish.y = 80;
		optimizeDolphin.y = 96;
		optimizeSalmon.y = 112;
		optimizeTropical.y = 128;
		optimizeCod.x = x;
		optimizePufferfish.x = x;
		optimizeDolphin.x = x;
		optimizeSalmon.x = x;
		optimizeTropical.x = x;
		//#endif
	}

	@Override
	public void mouseAction(double mouseX, double mouseY, int button) {
		enabled.mouseClicked(mouseX, mouseY, button);
		if (enabled.selected()) {
			optimizeCod.mouseClicked(mouseX, mouseY, button);
			optimizePufferfish.mouseClicked(mouseX, mouseY, button);
			optimizeSalmon.mouseClicked(mouseX, mouseY, button);
			optimizeDolphin.mouseClicked(mouseX, mouseY, button);
			optimizeTropical.mouseClicked(mouseX, mouseY, button);
		}
	}

	@Override
	public void render(int mouseX, int mouseY, float delta) {
		MCVer.render(enabled, mouseX, mouseY, delta);
		if (!enabled.selected()) {
			MCVer.color4f(.5f, .5f, .5f, .4f);
		} else {
			MCVer.render(optimizeCod, mouseX, mouseY, delta);
			MCVer.render(optimizePufferfish, mouseX, mouseY, delta);
			MCVer.render(optimizeSalmon, mouseX, mouseY, delta);
			MCVer.render(optimizeDolphin, mouseX, mouseY, delta);
			MCVer.render(optimizeTropical, mouseX, mouseY, delta);
		}

		MCVer.bind(Minecraft.getInstance().getTextureManager(), new ResourceLocation("lotas", "drops/fish.gif"));
		MCVer.blit(width - 128, y + 24, 0.0F, 0.0F, 96, 76, 96, 76);
	}

}
