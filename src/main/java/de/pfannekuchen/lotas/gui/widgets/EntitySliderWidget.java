package de.pfannekuchen.lotas.gui.widgets;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import de.pfannekuchen.lotas.gui.GuiEntitySpawnManipulation;
import de.pfannekuchen.lotas.mixin.accessors.AccessorEntityLiving;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityZombie;
//#if MC>=11100
import net.minecraft.entity.monster.EntityHusk;
import net.minecraft.entity.monster.EntityWitherSkeleton;
import net.minecraft.entity.monster.EntityZombieVillager;
//#endif

import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.client.config.GuiSlider;

public class EntitySliderWidget extends GuiSlider {

	public HashMap<Integer, String> entities;

	public EntitySliderWidget(int id, int xPos, int yPos, HashMap<Integer, String> ent, int width, int height) {
		super(id, xPos, yPos, width, height, "Entity: ", "", 0, ent.size() - 1, 0, true, true, null);
		showDecimal = false;
		this.displayString = dispString + GuiEntitySpawnManipulation.entities.get(0);
		this.entities = ent;
	}

	public EntityLiving getEntity(WorldServer world) {
		EntityLiving entity = null;
		switch (getValueInt()) {
		case 0:
			entity = new EntityBlaze(world);
			break;
		case 1:
			entity = new EntityCaveSpider(world);
			break;
		case 2:
			entity = new EntityCreeper(world);
			break;
		case 3:
			entity = new EntityEnderman(world);
			break;
		case 4:
			entity = new EntityGhast(world);
			break;
		case 5:
			//#if MC>=11100
			entity = new EntityHusk(world);
			//#else
//$$ 			entity = new EntityZombie(world);
			//#endif
			break;
		case 6:
			entity = new EntityGhast(world);
			break;
		case 7:
			entity = new EntityMagmaCube(world);
			break;
		case 8:
			entity = new EntitySkeleton(world);
			break;
		case 9:
			entity = new EntitySlime(world);
			break;
		case 10:
			entity = new EntitySpider(world);
			break;
		case 11:
			entity = new EntityWitch(world);
			break;
		//#if MC>=11100
		case 12:
			entity = new EntityWitherSkeleton(world);
			break;
		case 13:
			entity = new EntityZombie(world);
			break;
		case 14:
			entity = new EntityZombieVillager(world);
			break;
		//#endif
		}
		
		try {
			Method method = EntityLiving.class.getDeclaredMethod("func_180481_a", DifficultyInstance.class);
			method.setAccessible(true);
			//#if MC>=11200
			if (entity != null) method.invoke(entity, Minecraft.getMinecraft().getIntegratedServer().getWorld(entity.dimension).getDifficultyForLocation(Minecraft.getMinecraft().player.getPosition()));
			//#else
			//#if MC>=11100
			//$$ if (entity != null) method.invoke(entity, Minecraft.getMinecraft().getIntegratedServer().worldServerForDimension(entity.dimension).getDifficultyForLocation(Minecraft.getMinecraft().player.getPosition()));
			//#else
//$$ 			if (entity != null) method.invoke(entity, Minecraft.getMinecraft().getIntegratedServer().worldServerForDimension(entity.dimension).getDifficultyForLocation(Minecraft.getMinecraft().thePlayer.getPosition()));
			//#endif
			//#endif
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e1) {
			e1.printStackTrace();
		}
		if (world.getDifficulty() == EnumDifficulty.HARD) {
			switch (getValueInt()) {
			case 15:
				entity = new EntitySkeleton(world);
				entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, addEnchants(new ItemStack(Items.BOW), GuiEntitySpawnManipulation.skelBow));
				break;
			case 17:
				entity = new EntitySkeleton(world);
				entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, addEnchants(new ItemStack(Items.BOW), GuiEntitySpawnManipulation.skelBow));
				entity.setItemStackToSlot(EntityEquipmentSlot.FEET, new ItemStack(Items.LEATHER_BOOTS));
				entity.setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(Items.LEATHER_CHESTPLATE));
				entity.setItemStackToSlot(EntityEquipmentSlot.LEGS, new ItemStack(Items.LEATHER_LEGGINGS));
				entity.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.LEATHER_HELMET));
				break;
			case 19:
				entity = new EntitySkeleton(world);
				entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, addEnchants(new ItemStack(Items.BOW), GuiEntitySpawnManipulation.skelBow));
				entity.setItemStackToSlot(EntityEquipmentSlot.FEET, new ItemStack(Items.GOLDEN_BOOTS));
				entity.setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(Items.GOLDEN_CHESTPLATE));
				entity.setItemStackToSlot(EntityEquipmentSlot.LEGS, new ItemStack(Items.GOLDEN_LEGGINGS));
				entity.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.GOLDEN_HELMET));
				break;
			case 21:
				entity = new EntitySkeleton(world);
				entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, addEnchants(new ItemStack(Items.BOW), GuiEntitySpawnManipulation.skelBow));
				entity.setItemStackToSlot(EntityEquipmentSlot.FEET, new ItemStack(Items.CHAINMAIL_BOOTS));
				entity.setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(Items.CHAINMAIL_CHESTPLATE));
				entity.setItemStackToSlot(EntityEquipmentSlot.LEGS, new ItemStack(Items.CHAINMAIL_LEGGINGS));
				entity.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.CHAINMAIL_HELMET));
				break;
			case 23:
				entity = new EntitySkeleton(world);
				entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, addEnchants(new ItemStack(Items.BOW), GuiEntitySpawnManipulation.skelBow));
				entity.setItemStackToSlot(EntityEquipmentSlot.FEET, new ItemStack(Items.IRON_BOOTS));
				entity.setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
				entity.setItemStackToSlot(EntityEquipmentSlot.LEGS, new ItemStack(Items.IRON_LEGGINGS));
				entity.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
				break;
			case 25:
				entity = new EntitySkeleton(world);
				entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, addEnchants(new ItemStack(Items.BOW), GuiEntitySpawnManipulation.skelBow));
				entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
				entity.setItemStackToSlot(EntityEquipmentSlot.FEET, new ItemStack(Items.DIAMOND_BOOTS));
				entity.setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE));
				entity.setItemStackToSlot(EntityEquipmentSlot.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS));
				entity.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.DIAMOND_HELMET));
				break;
			case 16:
				entity = new EntityZombie(world);
				entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, addEnchants(new ItemStack(Items.IRON_SWORD), GuiEntitySpawnManipulation.zombieSword));
				break;
			case 18:
				entity = new EntityZombie(world);
				entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, addEnchants(new ItemStack(Items.IRON_SWORD), GuiEntitySpawnManipulation.zombieSword));
				entity.setItemStackToSlot(EntityEquipmentSlot.FEET, new ItemStack(Items.LEATHER_BOOTS));
				entity.setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(Items.LEATHER_CHESTPLATE));
				entity.setItemStackToSlot(EntityEquipmentSlot.LEGS, new ItemStack(Items.LEATHER_LEGGINGS));
				entity.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.LEATHER_HELMET));
				break;
			case 20:
				entity = new EntityZombie(world);
				entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, addEnchants(new ItemStack(Items.IRON_SWORD), GuiEntitySpawnManipulation.zombieSword));
				entity.setItemStackToSlot(EntityEquipmentSlot.FEET, new ItemStack(Items.GOLDEN_BOOTS));
				entity.setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(Items.GOLDEN_CHESTPLATE));
				entity.setItemStackToSlot(EntityEquipmentSlot.LEGS, new ItemStack(Items.GOLDEN_LEGGINGS));
				entity.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.GOLDEN_HELMET));
				break;
			case 22:
				entity = new EntityZombie(world);
				entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, addEnchants(new ItemStack(Items.IRON_SWORD), GuiEntitySpawnManipulation.zombieSword));
				entity.setItemStackToSlot(EntityEquipmentSlot.FEET, new ItemStack(Items.CHAINMAIL_BOOTS));
				entity.setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(Items.CHAINMAIL_CHESTPLATE));
				entity.setItemStackToSlot(EntityEquipmentSlot.LEGS, new ItemStack(Items.CHAINMAIL_LEGGINGS));
				entity.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.CHAINMAIL_HELMET));
				break;
			case 24:
				entity = new EntityZombie(world);
				entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, addEnchants(new ItemStack(Items.IRON_SWORD), GuiEntitySpawnManipulation.zombieSword));
				entity.setItemStackToSlot(EntityEquipmentSlot.FEET, new ItemStack(Items.IRON_BOOTS));
				entity.setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
				entity.setItemStackToSlot(EntityEquipmentSlot.LEGS, new ItemStack(Items.IRON_LEGGINGS));
				entity.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
				break;
			case 26:
				entity = new EntityZombie(world);
				entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, addEnchants(new ItemStack(Items.IRON_SWORD), GuiEntitySpawnManipulation.zombieSword));
				entity.setItemStackToSlot(EntityEquipmentSlot.FEET, new ItemStack(Items.DIAMOND_BOOTS));
				entity.setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE));
				entity.setItemStackToSlot(EntityEquipmentSlot.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS));
				entity.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.DIAMOND_HELMET));
				break;
			}
		}
		((AccessorEntityLiving) entity).inventoryArmorDropChances(new float[] { 1.0f, 1.0f, 1.0f, 1.0f });
		((AccessorEntityLiving) entity).inventoryHandsDropChances(new float[] { 1.0f, 1.0f });
		return entity;
	}

	public ItemStack addEnchants(ItemStack item,  EnchantmentData[] enchants) {
		for (EnchantmentData enchantmentData : enchants) {
			//#if MC>=11200
			item.addEnchantment(enchantmentData.enchantment, enchantmentData.enchantmentLevel);
			//#else
//$$ 			item.addEnchantment(enchantmentData.enchantmentobj, enchantmentData.enchantmentLevel);
			//#endif
		}
		return item;
	}
	
	public void updateSlider() {
		if (this.sliderValue < 0.0F) {
			this.sliderValue = 0.0F;
		}

		if (this.sliderValue > 1.0F) {
			this.sliderValue = 1.0F;
		}

		displayString = dispString + entities.get((int) Math.round(sliderValue * (maxValue - minValue) + minValue))
				+ suffix;
	}

}
