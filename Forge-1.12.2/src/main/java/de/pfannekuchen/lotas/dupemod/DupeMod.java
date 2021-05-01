package de.pfannekuchen.lotas.dupemod;

import static rlog.RLogAPI.logDebug;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;

public class DupeMod {
	
	public static List<EntityItem> items;
	public static List<TileEntity> tileentities;
	public static ArrayList<EntityItem> trackedObjects = new ArrayList<>();
	
	/**
	 * The Dupe Mod allows you to save items and chests without relogging. And Loading Items and Chests without alt + f4'ing the game
	 */
	
	public static void loadChests() {
		try {
			logDebug("[DupeMod] Trying to load Chests");
			Minecraft mc = Minecraft.getMinecraft();
			if (DupeMod.tileentities == null) return;
			for (TileEntity tileentity : DupeMod.tileentities) {
				mc.getIntegratedServer().getWorld(mc.player.dimension).getTileEntity(tileentity.getPos()).deserializeNBT(((TileEntityChest) tileentity).serializeNBT());
				logDebug("[DupeMod] Loaded Chest at " + tileentity.getPos());
			}
			logDebug("[DupeMod] Chests loaded");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void loadItems() {
		try {
			logDebug("[DupeMod] Trying to load Items");
			Minecraft mc = Minecraft.getMinecraft();
			
			mc.player.motionX = 0;
			mc.player.motionY = 0;
			mc.player.motionZ = 0;
			
			if (DupeMod.items != null) {
				if (!DupeMod.items.isEmpty()) {
			        mc.displayGuiScreen((GuiScreen)null);
			        mc.setIngameFocus();
					for (Entity entity : new ArrayList<Entity>(mc.getIntegratedServer().getWorld(mc.player.dimension).loadedEntityList)) {
						if (entity instanceof EntityItem) mc.getIntegratedServer().getWorld(mc.player.dimension).removeEntity(entity);
					}
					for (EntityItem item : DupeMod.items) {
						WorldServer w = mc.getIntegratedServer().getPlayerList().getPlayers().get(0).getServerWorld();
						EntityItem itemDupe = new EntityItem(w, item.posX, item.posY, item.posZ, item.getItem().copy());
						itemDupe.motionX = item.motionX;
						itemDupe.motionY = item.motionY;
						itemDupe.motionZ = item.motionZ;
						itemDupe.setPickupDelay(item.pickupDelay);
						itemDupe.setAgeToCreativeDespawnTime();
						itemDupe.setOwner(mc.player.getName());
						itemDupe.setNoPickupDelay();
						itemDupe.rotationYaw = item.rotationYaw;
						itemDupe.rotationPitch = item.rotationPitch;
						w.spawnEntity(itemDupe);
						logDebug("[DupeMod] Loaded Item at " + item.getPosition());
					}
				}
			}
			logDebug("[DupeMod] Items loaded");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void saveItems() {
		try {
			logDebug("[DupeMod] Trying to save Items");
			Minecraft mc = Minecraft.getMinecraft();
			double pX = mc.player.posX;
			double pY = mc.player.posY;
			double pZ = mc.player.posZ;

			mc.player.motionX = 0;
			mc.player.motionY = 0;
			mc.player.motionZ = 0;
			
			DupeMod.items = new LinkedList<EntityItem>();
			for (EntityItem item : mc.getIntegratedServer().getWorld(mc.player.dimension).getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pX - 16, pY - 16, pZ - 16, pX + 16, pY + 16, pZ + 16))) {
				EntityItem itemDupe = new EntityItem(item.world, item.posX, item.posY, item.posZ, item.getItem().copy());
				itemDupe.motionX = item.motionX;
				itemDupe.setPickupDelay(item.pickupDelay);
				itemDupe.motionY = item.motionY;
				itemDupe.motionZ = item.motionZ;
				itemDupe.rotationYaw = item.rotationYaw;
				itemDupe.rotationPitch = item.rotationPitch;
				DupeMod.items.add(itemDupe);
				logDebug("[DupeMod] Saved Item at " + item.getPosition());
			}
			logDebug("[DupeMod] Items saved");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void saveChests() {
		try {
			logDebug("[DupeMod] Trying to save Chests");
			Minecraft mc = Minecraft.getMinecraft();
			WorldServer world = mc.getIntegratedServer().getWorld(mc.player.dimension);
			BlockPos playerPos = mc.player.getPosition();
			
			DupeMod.tileentities = new LinkedList<TileEntity>();
			for (int x =- 5; x <= 5; x++) {
				for (int y =- 5; y <= 5; y++) {
					for (int z =- 5; z <= 5; z++) {
						if (mc.getIntegratedServer().getWorld(mc.player.dimension).getBlockState(playerPos.add(x, y, z)).getBlock() == Blocks.CHEST || world.getBlockState(playerPos.add(x, y, z)).getBlock() == Blocks.TRAPPED_CHEST) {
							TileEntityChest foundchest = (TileEntityChest) world.getTileEntity(playerPos.add(x,y,z));
							TileEntityChest newchest = new TileEntityChest(((TileEntityChest) world.getTileEntity(playerPos.add(x,y,z))).getChestType());
							newchest.deserializeNBT(foundchest.serializeNBT());
							tileentities.add(newchest);
							logDebug("[DupeMod] Saved Chest at " + foundchest.getPos());
						}
					}
				}
			}
			logDebug("[DupeMod] Chests saved");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
