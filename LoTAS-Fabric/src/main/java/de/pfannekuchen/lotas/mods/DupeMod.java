package de.pfannekuchen.lotas.mods;

import de.pfannekuchen.lotas.core.utils.DefaultedList;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

/**
 * Dupe Mod for Post 1.14.
 * @author Dean
 * @since v1.0
 * @version v1.0
 */
public class DupeMod {
	/** Items in the Inventories */
	private static DefaultedList<ItemStack> main = DefaultedList.ofSize(36, ItemStack.EMPTY);
	/** Items in the Inventories */
	private static DefaultedList<ItemStack> armor = DefaultedList.ofSize(4, ItemStack.EMPTY);
	/** Items in the Inventories */
	private static DefaultedList<ItemStack> offHand = DefaultedList.ofSize(1, ItemStack.EMPTY);

	/**
	 * Saves the Players Inventory to 3 array lists
	 */
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

	/**
	 * Loads the Players Inventory to 3 array lists
	 */
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
