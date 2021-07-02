package de.pfannekuchen.lotas.dropmanipulation.drops.entitydrops;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;

import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.gui.DropManipulationScreen;
import de.pfannekuchen.lotas.gui.widgets.ImageButton;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.DrownedEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

public class DrownedDropManipulation extends DropManipulationScreen.DropManipulation {

	public static ImageButton dropGold = new ImageButton(x, y, c -> {
		DrownedDropManipulation.dropGold.setToggled(!DrownedDropManipulation.dropGold.isToggled());
	}, new Identifier("lotas", "drops/gold.png"));

	public DrownedDropManipulation(int x, int y, int width, int height) {
		DrownedDropManipulation.x = x;
		DrownedDropManipulation.y = y;
		DrownedDropManipulation.width = width;
		DrownedDropManipulation.height = height;
		enabled = MCVer.CheckboxWidget(x, y, 150, 20, "Override Drowned Drops", false);
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
		if (entity instanceof DrownedEntity) {
			list.add(new ItemStack(Items.ROTTEN_FLESH, 2));

			if (dropGold.isToggled())
				list.add(new ItemStack(Items.GOLD_INGOT));
		}
		return list;
	}

	@Override
	public void update() {
		enabled.x = x;
		enabled.y = y;
		dropGold.x = x;
		dropGold.y = y + 96;
	}

	@Override
	public void mouseAction(double mouseX, double mouseY, int button) {
		enabled.mouseClicked(mouseX, mouseY, button);
		if (enabled.isChecked()) {
			dropGold.mouseClicked(mouseX, mouseY, button);
		}
	}

	@Override
	public void render(Object matrices, int mouseX, int mouseY, float delta) {
		MCVer.render(enabled, mouseX, mouseY, delta);

		if (!enabled.isChecked()) {
			MCVer.color(.5f, .5f, .5f, .4f);
		} else {
			MinecraftClient.getInstance().textRenderer.drawWithShadow("Drowned drop: 2 Rotten Flesh" + (dropGold.isToggled() ? ", 1 Iron Ingot" : ""), x, y + 64, 0xFFFFFF);
			MCVer.render(dropGold, mouseX, mouseY, delta);
		}

		MinecraftClient.getInstance().getTextureManager().bindTexture(new Identifier("lotas", "drops/drowned.png"));
		MCVer.renderImage(width - 228, y + 24, 0.0F, 0.0F, 122, 199, 122, 199);
	}

}
