package de.pfannekuchen.lotas.gui.parts;

import java.util.HashMap;

import cpw.mods.fml.client.config.GuiSlider;
import de.pfannekuchen.lotas.gui.GuiEntitySpawner;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.WorldServer;

public class CustomEntitySlider extends GuiSlider {

	public HashMap<Integer, String> entities;
	
	public CustomEntitySlider(int id, int xPos, int yPos, HashMap<Integer, String> ent, int width, int height) {
		super(id, xPos, yPos, width, 20, "Entity: ", "", 0, ent.size() - 1, 0, false, true); // "Entity: ", "", 
		this.displayString = dispString + GuiEntitySpawner.entities.get(0);
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
			entity = new EntityZombie(world);
			break;
		case 6:
			entity = new EntityIronGolem(world);
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
		case 12:
			entity = new EntitySkeleton(world);
			((EntitySkeleton) entity).setSkeletonType(1);
			break;
		case 13:
			entity = new EntityZombie(world);
			break;
		case 14:
			entity = new EntityZombie(world);
			((EntityZombie) entity).setVillager(true);
			break;
		}
		if (world.difficultySetting == EnumDifficulty.HARD) {
			switch (getValueInt()) {
			case 15:
				entity = new EntitySkeleton(world);
				entity.setCurrentItemOrArmor(0, addEnchants(new ItemStack(Items.bow), GuiEntitySpawner.skelBow));
				break;
			case 17:
				entity = new EntitySkeleton(world);
				entity.setCurrentItemOrArmor(0, addEnchants(new ItemStack(Items.bow), GuiEntitySpawner.skelBow));
				entity.setCurrentItemOrArmor(1, new ItemStack(Items.leather_boots));
				entity.setCurrentItemOrArmor(2, new ItemStack(Items.leather_chestplate));
				entity.setCurrentItemOrArmor(3, new ItemStack(Items.leather_leggings));
				entity.setCurrentItemOrArmor(4, new ItemStack(Items.leather_helmet));
				break;
			case 19:
				entity = new EntitySkeleton(world);
				entity.setCurrentItemOrArmor(0, addEnchants(new ItemStack(Items.bow), GuiEntitySpawner.skelBow));
				entity.setCurrentItemOrArmor(1, new ItemStack(Items.golden_boots));
				entity.setCurrentItemOrArmor(2, new ItemStack(Items.golden_chestplate));
				entity.setCurrentItemOrArmor(3, new ItemStack(Items.golden_leggings));
				entity.setCurrentItemOrArmor(4, new ItemStack(Items.golden_helmet));
				break;
			case 21:
				entity = new EntitySkeleton(world);
				entity.setCurrentItemOrArmor(0, addEnchants(new ItemStack(Items.bow), GuiEntitySpawner.skelBow));
				entity.setCurrentItemOrArmor(1, new ItemStack(Items.chainmail_boots));
				entity.setCurrentItemOrArmor(2, new ItemStack(Items.chainmail_chestplate));
				entity.setCurrentItemOrArmor(3, new ItemStack(Items.chainmail_leggings));
				entity.setCurrentItemOrArmor(4, new ItemStack(Items.chainmail_helmet));
				break;
			case 23:
				entity = new EntitySkeleton(world);
				entity.setCurrentItemOrArmor(0, addEnchants(new ItemStack(Items.bow), GuiEntitySpawner.skelBow));
				entity.setCurrentItemOrArmor(1, new ItemStack(Items.iron_boots));
				entity.setCurrentItemOrArmor(2, new ItemStack(Items.iron_chestplate));
				entity.setCurrentItemOrArmor(3, new ItemStack(Items.iron_leggings));
				entity.setCurrentItemOrArmor(4, new ItemStack(Items.iron_helmet));
				break;
			case 25:
				entity = new EntitySkeleton(world);
				entity.setCurrentItemOrArmor(0, addEnchants(new ItemStack(Items.bow), GuiEntitySpawner.skelBow));
				entity.setCurrentItemOrArmor(0, new ItemStack(Items.bow));
				entity.setCurrentItemOrArmor(1, new ItemStack(Items.diamond_boots));
				entity.setCurrentItemOrArmor(2, new ItemStack(Items.diamond_chestplate));
				entity.setCurrentItemOrArmor(3, new ItemStack(Items.diamond_leggings));
				entity.setCurrentItemOrArmor(4, new ItemStack(Items.diamond_helmet));
				break;
			case 16:
				entity = new EntityZombie(world);
				entity.setCurrentItemOrArmor(0, addEnchants(new ItemStack(Items.iron_sword), GuiEntitySpawner.zombieSword));
				break;
			case 18:
				entity = new EntityZombie(world);
				entity.setCurrentItemOrArmor(0, addEnchants(new ItemStack(Items.iron_sword), GuiEntitySpawner.zombieSword));
				entity.setCurrentItemOrArmor(1, new ItemStack(Items.leather_boots));
				entity.setCurrentItemOrArmor(2, new ItemStack(Items.leather_chestplate));
				entity.setCurrentItemOrArmor(3, new ItemStack(Items.leather_leggings));
				entity.setCurrentItemOrArmor(4, new ItemStack(Items.leather_helmet));
				break;
			case 20:
				entity = new EntityZombie(world);
				entity.setCurrentItemOrArmor(0, addEnchants(new ItemStack(Items.iron_sword), GuiEntitySpawner.zombieSword));
				entity.setCurrentItemOrArmor(1, new ItemStack(Items.golden_boots));
				entity.setCurrentItemOrArmor(2, new ItemStack(Items.golden_chestplate));
				entity.setCurrentItemOrArmor(3, new ItemStack(Items.golden_leggings));
				entity.setCurrentItemOrArmor(4, new ItemStack(Items.golden_helmet));
				break;
			case 22:
				entity = new EntityZombie(world);
				entity.setCurrentItemOrArmor(0, addEnchants(new ItemStack(Items.iron_sword), GuiEntitySpawner.zombieSword));
				entity.setCurrentItemOrArmor(1, new ItemStack(Items.chainmail_boots));
				entity.setCurrentItemOrArmor(2, new ItemStack(Items.chainmail_chestplate));
				entity.setCurrentItemOrArmor(3, new ItemStack(Items.chainmail_leggings));
				entity.setCurrentItemOrArmor(4, new ItemStack(Items.chainmail_helmet));
				break;
			case 24:
				entity = new EntityZombie(world);
				entity.setCurrentItemOrArmor(0, addEnchants(new ItemStack(Items.iron_sword), GuiEntitySpawner.zombieSword));
				entity.setCurrentItemOrArmor(1, new ItemStack(Items.iron_boots));
				entity.setCurrentItemOrArmor(2, new ItemStack(Items.iron_chestplate));
				entity.setCurrentItemOrArmor(3, new ItemStack(Items.iron_leggings));
				entity.setCurrentItemOrArmor(4, new ItemStack(Items.iron_helmet));
				break;
			case 26:
				entity = new EntityZombie(world);
				entity.setCurrentItemOrArmor(0, addEnchants(new ItemStack(Items.iron_sword), GuiEntitySpawner.zombieSword));
				entity.setCurrentItemOrArmor(1, new ItemStack(Items.diamond_boots));
				entity.setCurrentItemOrArmor(2, new ItemStack(Items.diamond_chestplate));
				entity.setCurrentItemOrArmor(3, new ItemStack(Items.diamond_leggings));
				entity.setCurrentItemOrArmor(4, new ItemStack(Items.diamond_helmet));
				break;
			}
		}
		entity.equipmentDropChances = new float[] { 1.0f, 1.0f, 1.0f, 1.0f, 1.0f };
		return entity;
	}

	public ItemStack addEnchants(ItemStack item,  EnchantmentData[] enchants) {
		for (EnchantmentData enchantmentData : enchants) {
			item.addEnchantment(enchantmentData.enchantmentobj, enchantmentData.enchantmentLevel);
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

		displayString = dispString + entities.get(getValueInt());
	}

}
