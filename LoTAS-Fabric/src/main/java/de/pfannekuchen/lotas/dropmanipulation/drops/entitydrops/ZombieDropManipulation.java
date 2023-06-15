package de.pfannekuchen.lotas.dropmanipulation.drops.entitydrops;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;

import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.gui.DropManipulationScreen;
import de.pfannekuchen.lotas.gui.widgets.ImageButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Husk;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;

public class ZombieDropManipulation extends DropManipulationScreen.DropManipulation {

	public static ImageButton dropIron = new ImageButton(x, y, c -> {
		ZombieDropManipulation.dropPotato.setToggled(false);
		ZombieDropManipulation.dropCarrot.setToggled(false);
		ZombieDropManipulation.dropIron.setToggled(!ZombieDropManipulation.dropIron.isToggled());
	}, new ResourceLocation("lotas", "drops/iron.png"));
	public static ImageButton dropPotato = new ImageButton(x, y, c -> {
		ZombieDropManipulation.dropIron.setToggled(false);
		ZombieDropManipulation.dropCarrot.setToggled(false);
		ZombieDropManipulation.dropPotato.setToggled(!ZombieDropManipulation.dropPotato.isToggled());
	}, new ResourceLocation("lotas", "drops/potato.png"));
	public static ImageButton dropCarrot = new ImageButton(x, y, c -> {
		ZombieDropManipulation.dropIron.setToggled(false);
		ZombieDropManipulation.dropPotato.setToggled(false);
		ZombieDropManipulation.dropCarrot.setToggled(!ZombieDropManipulation.dropCarrot.isToggled());
	}, new ResourceLocation("lotas", "drops/carrot.png"));

	public ZombieDropManipulation(int x, int y, int width, int height) {
		ZombieDropManipulation.x = x;
		ZombieDropManipulation.y = y;
		ZombieDropManipulation.width = width;
		ZombieDropManipulation.height = height;
		enabled = MCVer.Checkbox(x, y, 150, 20, I18n.get("dropmanipgui.lotas.entity.zombie.override"), false);//"Override Zombie/Zombie-Villager/Husk Drops"
	}

	@Override
	public String getName() {
		return I18n.get("dropmanipgui.lotas.entity.zombie.name");//"Zombie"
	}

	@Override
	public List<ItemStack> redirectDrops(BlockState block) {
		return ImmutableList.of();
	}

	@Override
	public List<ItemStack> redirectDrops(Entity entity, int lootingBonus) {
		List<ItemStack> list = new ArrayList<>();
		if (entity instanceof Zombie &&
				//#if MC>=11600
//$$ 				!(entity instanceof net.minecraft.world.entity.monster.ZombifiedPiglin)) {
				//#else
				!(entity instanceof net.minecraft.world.entity.monster.PigZombie)) {
				//#endif
			list.add(new ItemStack(Items.ROTTEN_FLESH, 2 +lootingBonus));

			if (dropIron.isToggled())
				list.add(new ItemStack(Items.IRON_INGOT));
			if (dropPotato.isToggled())
				list.add(new ItemStack(Items.POTATO));
			if (dropCarrot.isToggled())
				list.add(new ItemStack(Items.CARROT));

		}
		return list;
	}

	@Override
	public void update() {
		//#if MC>=11903
//$$ 		enabled.setPosition(x, y);
//$$
//$$ 		dropIron.setPosition(x, y + 96);
//$$ 		dropPotato.setPosition(x+22, y + 96);
//$$ 		dropCarrot.setPosition(x+44, y + 96);
		//#else
		enabled.x = x;
		enabled.y = y;
		dropIron.x = x;
		dropIron.y = y + 96;
		dropPotato.x = x + 22;
		dropPotato.y = y + 96;
		dropCarrot.x = x + 44;
		dropCarrot.y = y + 96;
		//#endif
	}

	@Override
	public void mouseAction(double mouseX, double mouseY, int button) {
		enabled.mouseClicked(mouseX, mouseY, button);
		if (enabled.selected()) {
			dropIron.mouseClicked(mouseX, mouseY, button);
			dropPotato.mouseClicked(mouseX, mouseY, button);
			dropCarrot.mouseClicked(mouseX, mouseY, button);
		}
	}

	@Override
	public void render(int mouseX, int mouseY, float delta) {
		MCVer.render(enabled, mouseX, mouseY, delta);

		if (!enabled.selected()) {
			MCVer.color4f(.5f, .5f, .5f, .4f);
		} else {
			String zombie = I18n.get("dropmanipgui.lotas.entity.zombie.zombie");//"Zombies drop:"
			String rottenflesh = I18n.get("dropmanipgui.lotas.entity.zombie.rottenflesh");//" 2 Rotten Flesh"
			String iron = I18n.get("dropmanipgui.lotas.entity.zombie.iron");//", 1 Iron Ingot"
			String potato = I18n.get("dropmanipgui.lotas.entity.zombie.potato");//", 1 Potato"
			String carrot = I18n.get("dropmanipgui.lotas.entity.zombie.carrot");//", 1 Carrot"
			
			MCVer.drawShadow(zombie+rottenflesh + (dropIron.isToggled() ? iron : "") + (dropPotato.isToggled() ? potato : "") + (dropCarrot.isToggled() ? carrot : ""), x, y + 64, 0xFFFFFF);
			MCVer.render(dropIron, mouseX, mouseY, delta);
			MCVer.render(dropPotato, mouseX, mouseY, delta);
			MCVer.render(dropCarrot, mouseX, mouseY, delta);
		}

		MCVer.bind(Minecraft.getInstance().getTextureManager(), new ResourceLocation("lotas", "drops/zombie.png"));
		int scaleX=30;
		int scaleY=50;
		MCVer.blit(width - 110, y + 24, 0.0F, 0.0F, 118-scaleX, 198-scaleY, 118-scaleX, 198-scaleY);
	}

}
