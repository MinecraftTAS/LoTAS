package de.pfannekuchen.lotas.dupemod;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DefaultedList;
import rlog.RLogAPI;

public class DupeMod {
    private static DefaultedList<ItemStack> main = DefaultedList.ofSize(36, ItemStack.EMPTY);
    private static DefaultedList<ItemStack> armor = DefaultedList.ofSize(4, ItemStack.EMPTY);
    private static DefaultedList<ItemStack> offHand = DefaultedList.ofSize(1, ItemStack.EMPTY);

    public static void save(MinecraftClient client) {
        PlayerInventory inventory = client.getServer().getPlayerManager().getPlayer(client.player.getUuid()).inventory;
        for (int i = 0; i < inventory.main.size(); i++) {
            main.set(i, inventory.main.get(i).copy());
        }
        for (int i = 0; i < inventory.armor.size(); i++) {
            armor.set(i, inventory.armor.get(i).copy());
        }
        for (int i = 0; i < inventory.offHand.size(); i++) {
            offHand.set(i, inventory.offHand.get(i).copy());
        }
        RLogAPI.logDebug("[DupeMod] Inventory saved");
    }

    public static void load(MinecraftClient client) {
        PlayerInventory inventory = client.getServer().getPlayerManager().getPlayer(client.player.getUuid()).inventory;
        for (int i = 0; i < main.size(); i++) {
            inventory.main.set(i, main.get(i));
        }
        for (int i = 0; i < armor.size(); i++) {
            inventory.armor.set(i, armor.get(i));
        }
        for (int i = 0; i < offHand.size(); i++) {
            inventory.offHand.set(i, offHand.get(i));
        }
        RLogAPI.logDebug("[DupeMod] Inventory loaded");
    }

}
