package de.pfannekuchen.lotas.mods;

import de.pfannekuchen.lotas.core.utils.DefaultedList;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class DupeMod {
	private static DefaultedList<ItemStack> main = DefaultedList.ofSize(36, ItemStack.EMPTY);
	private static DefaultedList<ItemStack> armor = DefaultedList.ofSize(4, ItemStack.EMPTY);
	private static DefaultedList<ItemStack> offHand = DefaultedList.ofSize(1, ItemStack.EMPTY);

	public static void save(Minecraft client) {
		Inventory inventory = client.getSingleplayerServer().getPlayerList().getPlayer(client.player.getUUID()).inventory;
		for (int i = 0; i < inventory.items.size(); i++) {
			main.set(i, inventory.items.get(i).copy());
		}
		for (int i = 0; i < inventory.armor.size(); i++) {
			armor.set(i, inventory.armor.get(i).copy());
		}
		for (int i = 0; i < inventory.offhand.size(); i++) {
			offHand.set(i, inventory.offhand.get(i).copy());
		}
		client.player.setDeltaMovement(0, 0, 0);
	}

	public static void load(Minecraft client) {
		Inventory inventory = client.getSingleplayerServer().getPlayerList().getPlayer(client.player.getUUID()).inventory;
		for (int i = 0; i < main.size(); i++) {
			inventory.items.set(i, main.get(i));
		}
		for (int i = 0; i < armor.size(); i++) {
			inventory.armor.set(i, armor.get(i));
		}
		for (int i = 0; i < offHand.size(); i++) {
			inventory.offhand.set(i, offHand.get(i));
		}
		client.player.setDeltaMovement(0, 0, 0);
	}

}
