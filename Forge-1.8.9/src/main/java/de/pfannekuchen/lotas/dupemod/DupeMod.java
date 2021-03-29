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
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class DupeMod {
	
	public static List<EntityItem> items;
	public static List<TileEntity> tileentities;
	public static ArrayList<EntityItem> trackedObjects = new ArrayList<>();
	
	/**
	 * The Dupe Mod allows you to save items and chests without relogging. And Loading Items and Chests without alt + f4'ing the game
	 */
	
	public static void loadChests() {
		logDebug("[DupeMod] Trying to load Chests");
		Minecraft mc = Minecraft.getMinecraft();
		if (DupeMod.tileentities == null) return;
		for (TileEntity tileentity : DupeMod.tileentities) {
			mc.getIntegratedServer().worldServerForDimension(mc.thePlayer.dimension).getTileEntity(tileentity.getPos()).deserializeNBT(((TileEntityChest) tileentity).serializeNBT());
			logDebug("[DupeMod] Loaded Chest at " + tileentity.getPos());
		}
		logDebug("[DupeMod] Chests loaded");
	}
	
	public static void loadItems() {
		logDebug("[DupeMod] Trying to load Items");
		Minecraft mc = Minecraft.getMinecraft();
		
		mc.thePlayer.motionX = 0;
		mc.thePlayer.motionY = 0;
		mc.thePlayer.motionZ = 0;
		
		if (DupeMod.items != null) {
			if (!DupeMod.items.isEmpty()) {
                mc.displayGuiScreen((GuiScreen)null);
                mc.setIngameFocus();
				for (Entity entity : new ArrayList<Entity>(mc.getIntegratedServer().worldServerForDimension(mc.thePlayer.dimension).loadedEntityList)) {
					if (entity instanceof EntityItem) mc.getIntegratedServer().worldServerForDimension(mc.thePlayer.dimension).removeEntity(entity);
				}
				for (EntityItem item : DupeMod.items) {
					World w = item.worldObj;
					EntityItem itemDupe = new EntityItem(w, item.posX, item.posY, item.posZ, item.getEntityItem().copy());
					itemDupe.motionX = item.motionX;
					itemDupe.motionY = item.motionY;
					itemDupe.motionZ = item.motionZ;
					itemDupe.setAgeToCreativeDespawnTime();
					itemDupe.setOwner(mc.thePlayer.getName());
					itemDupe.setNoPickupDelay();
					itemDupe.rotationYaw = item.rotationYaw;
					itemDupe.rotationPitch = item.rotationPitch;
					w.spawnEntityInWorld(itemDupe);
					logDebug("[DupeMod] Loaded Item at " + item.getPosition());
				}
			}
		}
		logDebug("[DupeMod] Items loaded");
	}
	
	public static void saveItems() {
		logDebug("[DupeMod] Trying to save Items");
		Minecraft mc = Minecraft.getMinecraft();
		double pX = mc.thePlayer.posX;
		double pY = mc.thePlayer.posY;
		double pZ = mc.thePlayer.posZ;

		mc.thePlayer.motionX = 0;
		mc.thePlayer.motionY = 0;
		mc.thePlayer.motionZ = 0;
		
		DupeMod.items = new LinkedList<EntityItem>();
		for (EntityItem item : mc.getIntegratedServer().worldServerForDimension(mc.thePlayer.dimension).getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pX - 16, pY - 16, pZ - 16, pX + 16, pY + 16, pZ + 16))) {
			EntityItem itemDupe = new EntityItem(item.worldObj, item.posX, item.posY, item.posZ, item.getEntityItem().copy());
			itemDupe.motionX = item.motionX;
			itemDupe.motionY = item.motionY;
			itemDupe.motionZ = item.motionZ;
			itemDupe.rotationYaw = item.rotationYaw;
			itemDupe.rotationPitch = item.rotationPitch;
			DupeMod.items.add(itemDupe);
			logDebug("[DupeMod] Saved Item at " + item.getPosition());
		}
		logDebug("[DupeMod] Items saved");
	}
	
	public static void saveChests() {
		logDebug("[DupeMod] Trying to save Chests");
		Minecraft mc = Minecraft.getMinecraft();
		WorldServer world = mc.getIntegratedServer().worldServerForDimension(mc.thePlayer.dimension);
		BlockPos playerPos = mc.thePlayer.getPosition();
		
		DupeMod.tileentities = new LinkedList<TileEntity>();
		for (int x =- 5; x <= 5; x++) {
			for (int y =- 5; y <= 5; y++) {
				for (int z =- 5; z <= 5; z++) {
					if (mc.getIntegratedServer().worldServerForDimension(mc.thePlayer.dimension).getBlockState(playerPos.add(x, y, z)).getBlock() == Blocks.chest || world.getBlockState(playerPos.add(x, y, z)).getBlock() == Blocks.trapped_chest) {
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
	}
	
}
