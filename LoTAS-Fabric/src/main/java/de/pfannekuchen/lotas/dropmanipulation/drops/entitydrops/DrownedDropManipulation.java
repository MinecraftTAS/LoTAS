package de.pfannekuchen.lotas.dropmanipulation.drops.entitydrops;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;

import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.gui.DropManipulationScreen;
import de.pfannekuchen.lotas.gui.widgets.ImageButton;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;

public class DrownedDropManipulation extends DropManipulationScreen.DropManipulation {

	public static ImageButton drops = new ImageButton(x, y, c -> {
		DrownedDropManipulation.drops.setToggled(!DrownedDropManipulation.drops.isToggled());
	}, 
	//#if MC>=11700
//$$ 	new ResourceLocation("lotas", "drops/copper.png"));
	//#else
	new ResourceLocation("lotas", "drops/gold.png"));
	//#endif

	public DrownedDropManipulation(int x, int y, int width, int height) {
		DrownedDropManipulation.x = x;
		DrownedDropManipulation.y = y;
		DrownedDropManipulation.width = width;
		DrownedDropManipulation.height = height;
		enabled = MCVer.Checkbox(x, y, 150, 20, "Override Drowned Drops", false);
	}

	@Override
	public String getName() {
		return "Drowned";
	}

	@Override
	public List<ItemStack> redirectDrops(BlockState block) {
		return ImmutableList.of();
	}

	@Override
	public List<ItemStack> redirectDrops(Entity entity) {
		List<ItemStack> list = new ArrayList<>();
		if (entity instanceof Drowned) {
			list.add(new ItemStack(Items.ROTTEN_FLESH, 2));

			if (drops.isToggled())
				//#if MC>=11700
//$$ 				list.add(new ItemStack(Items.COPPER_INGOT));
				//#else
				list.add(new ItemStack(Items.GOLD_INGOT));
				//#endif
		}
		return list;
	}

	@Override
	public void update() {
		enabled.x = x;
		enabled.y = y;
		drops.x = x;
		drops.y = y + 96;
	}

	@Override
	public void mouseAction(double mouseX, double mouseY, int button) {
		enabled.mouseClicked(mouseX, mouseY, button);
		if (enabled.selected()) {
			drops.mouseClicked(mouseX, mouseY, button);
		}
	}

	@Override
	public void render(int mouseX, int mouseY, float delta) {
		MCVer.render(enabled, mouseX, mouseY, delta);

		if (!enabled.selected()) {
			MCVer.color4f(.5f, .5f, .5f, .4f);
		} else {
			//#if MC>=11700
//$$ 			MCVer.drawShadow("Drowned drop: 2 Rotten Flesh" + (drops.isToggled() ? ", 1 Copper Ingot" : ""), x, y + 64, 0xFFFFFF);
			//#else
			MCVer.drawShadow("Drowned drop: 2 Rotten Flesh" + (drops.isToggled() ? ", 1 Gold Ingot" : ""), x, y + 64, 0xFFFFFF);
			//#endif
			MCVer.render(drops, mouseX, mouseY, delta);
		}

		MCVer.bind(Minecraft.getInstance().getTextureManager(),new ResourceLocation("lotas", "drops/drowned.png"));
		int scaleX=30;
		int scaleY=50;
		MCVer.blit(width - 115, y + 24, 0.0F, 0.0F, 122-scaleX, 199-scaleY, 122-scaleX, 199-scaleY);
	}

}
