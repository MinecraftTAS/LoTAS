package de.pfannekuchen.lotas.mods;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.mixin.accessors.AccessorEntityLiving;
import de.pfannekuchen.lotas.mods.AIManipMod.Vec;
import net.minecraft.client.Minecraft;
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
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerProfession;

/**
 * Allows for spawning entities at valid locations
 * @author ScribbleLP
 */
public class SpawnManipMod {
	
	private Minecraft mc = Minecraft.getMinecraft();
	
	private static EntityLiving entity;
	
	private static Vec target;
	
	private static Vec playerPos;
	
	private final EnumFacing orientation;
	
	//#if MC>=10900
	private EnchantmentData[] skelBow = new EnchantmentData[] {new EnchantmentData(Enchantments.UNBREAKING, 1), new EnchantmentData(Enchantments.POWER, 1)};
	private EnchantmentData[] zombieSword = new EnchantmentData[] {new EnchantmentData(Enchantments.SHARPNESS, 2), new EnchantmentData(Enchantments.UNBREAKING, 2)};
	//#else
//$$ 	private EnchantmentData[] skelBow = new EnchantmentData[] {new EnchantmentData(Enchantment.unbreaking, 1), new EnchantmentData(Enchantment.power, 1)};
//$$ 	private EnchantmentData[] zombieSword = new EnchantmentData[] {new EnchantmentData(Enchantment.sharpness, 2), new EnchantmentData(Enchantment.unbreaking, 2)};
	//#endif
	
	public SpawnManipMod() {
		orientation =  MCVer.player(mc).getHorizontalFacing();
		target=new Vec(MCVer.player(mc).getPositionVector());
		for(int i=0; i<25;i++) {
			changeTargetForward();
		}
		playerPos=new Vec(MCVer.player(mc).getPositionVector());
	}
	
	public void setEntity(EntityLiving entity) {
		SpawnManipMod.entity=entity;
	}
	
	public void confirm() {
		if(canSpawn()) {
			WorldServer world=(WorldServer) MCVer.world(mc.getIntegratedServer(), MCVer.player(Minecraft.getMinecraft()).dimension);
			int targetX=(int) Math.round(target.x-0.5);
			int targetY=(int) Math.round(target.y);
			int targetZ=(int) Math.round(target.z-0.5);
			
			entity.setPosition(targetX, targetY, targetZ);
			MCVer.spawnEntity(world, entity);
		}
	}
	
	public void changeTargetForward() {
		switch(orientation) {
		case NORTH:
			target=target.addVector(0, 0, -1);
			break;
		case EAST:
			target=target.addVector(1, 0, 0);
			break;
		case SOUTH:
			target=target.addVector(0, 0, 1);
			break;
		case WEST:
			target=target.addVector(-1, 0, 0);
			break;
		default:
			break;
		}
	}
	
	public void changeTargetBack() {
		switch(orientation) {
		case NORTH:
			target=target.addVector(0, 0, 1);
			break;
		case EAST:
			target=target.addVector(-1, 0, 0);
			break;
		case SOUTH:
			target=target.addVector(0, 0, -1);
			break;
		case WEST:
			target=target.addVector(1, 0, 0);
			break;
		default:
			break;
		}
	}
	
	public void changeTargetLeft() {
		switch(orientation) {
		case NORTH:
			target=target.addVector(-1, 0, 0);
			break;
		case EAST:
			target=target.addVector(0, 0, -1);
			break;
		case SOUTH:
			target=target.addVector(1, 0, 0);
			break;
		case WEST:
			target=target.addVector(0, 0, 1);
			break;
		default:
			break;
		}
	}
	
	public void changeTargetRight() {
		switch(orientation) {
		case NORTH:
			target=target.addVector(1, 0, 0);
			break;
		case EAST:
			target=target.addVector(0, 0, 1);
			break;
		case SOUTH:
			target=target.addVector(-1, 0, 0);
			break;
		case WEST:
			target=target.addVector(0, 0, -1);
			break;
		default:
			break;
		}
	}
	
	public void changeTargetUp() {
		target=target.addVector(0, 1, 0);
	}
	
	public void changeTargetDown() {
		target=target.addVector(0, -1, 0);
	}
	
	public void setTarget(Vec target) {
		SpawnManipMod.target = target;
	}
	
	public void setTargetToPlayer() {
		target=new Vec(MCVer.player(mc).getPositionVector());
	}
	
	public static Vec getTargetPos() {
		return target;
	}
	
	public List<EntityOptions> getManipList(){
		List<EntityOptions> entities=new ArrayList<EntityOptions>();
		int dim = MCVer.player(mc).dimension;
		WorldServer world=MCVer.world(mc.getIntegratedServer(), dim);
		
		if(dim == 0) {
			entities.add(new EntityOptions("Cave Spider", new EntityCaveSpider(world)));
			entities.add(new EntityOptions("Creeper", new EntityCreeper(world)));
			entities.add(new EntityOptions("Enderman", new EntityEnderman(world)));
			//#if MC>=11100
			entities.add(new EntityOptions("Husk", new net.minecraft.entity.monster.EntityHusk(world)));
			//#endif
			entities.add(new EntityOptions("Iron Golem", new EntityIronGolem(world)));
			EntityLiving entity = new EntitySkeleton(world);
			entity.setItemStackToSlot(net.minecraft.inventory.EntityEquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
			entities.add(new EntityOptions("Skeleton", entity));
			entities.add(new EntityOptions("Slime", new EntitySlime(world)));
			entities.add(new EntityOptions("Spider", new EntitySpider(world)));
			entities.add(new EntityOptions("Witch", new EntityWitch(world)));
			entities.add(new EntityOptions("Zombie", new EntityZombie(world)));
			//#if MC>=11100
			entities.add(new EntityOptions("Zombievillager", new net.minecraft.entity.monster.EntityZombieVillager(world)));
			//#else
//$$ 			entity = new EntityZombie(world);
//$$ 			EntityZombie zombentity=(EntityZombie) entity;
//$$ 			net.minecraftforge.fml.common.registry.VillagerRegistry.setRandomProfession(zombentity, new Random());
//$$ 			entities.add(new EntityOptions("Zombievillager", zombentity));
			//#endif
			
			if (MCVer.world(mc).getDifficulty() == EnumDifficulty.HARD) {
				entity = new EntitySkeleton(world);
				MCVer.setItemToSlot(entity, 0, addEnchants(new ItemStack(Items.BOW), skelBow));
				((AccessorEntityLiving) entity).inventoryArmorDropChances(new float[] { 1.0f, 1.0f, 1.0f, 1.0f });
				((AccessorEntityLiving) entity).inventoryHandsDropChances(new float[] { 1.0f, 1.0f });
				entities.add(new EntityOptions("Skeleton (Enchanted Bow)", entity));
				
				entity = new EntityZombie(world);
				MCVer.setItemToSlot(entity, 0, addEnchants(new ItemStack(Items.IRON_SWORD), zombieSword));
				((AccessorEntityLiving) entity).inventoryArmorDropChances(new float[] { 1.0f, 1.0f, 1.0f, 1.0f });
				((AccessorEntityLiving) entity).inventoryHandsDropChances(new float[] { 1.0f, 1.0f });
				entities.add(new EntityOptions("Zombie (Enchanted Sword)", entity));

				entity = new EntitySkeleton(world);
				MCVer.setItemToSlot(entity, 0, addEnchants(new ItemStack(Items.BOW), skelBow));
				MCVer.setItemToSlot(entity, 1, new ItemStack(Items.LEATHER_BOOTS));
				MCVer.setItemToSlot(entity, 2, new ItemStack(Items.LEATHER_CHESTPLATE));
				MCVer.setItemToSlot(entity, 3, new ItemStack(Items.LEATHER_LEGGINGS));
				MCVer.setItemToSlot(entity, 4, new ItemStack(Items.LEATHER_HELMET));
				((AccessorEntityLiving) entity).inventoryArmorDropChances(new float[] { 1.0f, 1.0f, 1.0f, 1.0f });
				((AccessorEntityLiving) entity).inventoryHandsDropChances(new float[] { 1.0f, 1.0f });
				entities.add(new EntityOptions("Skeleton (Leather Armor, Enchanted Bow)", entity));
				
				entity = new EntityZombie(world);
				MCVer.setItemToSlot(entity, 0, addEnchants(new ItemStack(Items.IRON_SWORD), zombieSword));
				MCVer.setItemToSlot(entity, 1, new ItemStack(Items.LEATHER_BOOTS));
				MCVer.setItemToSlot(entity, 2, new ItemStack(Items.LEATHER_CHESTPLATE));
				MCVer.setItemToSlot(entity, 3, new ItemStack(Items.LEATHER_LEGGINGS));
				MCVer.setItemToSlot(entity, 4, new ItemStack(Items.LEATHER_HELMET));
				((AccessorEntityLiving) entity).inventoryArmorDropChances(new float[] { 1.0f, 1.0f, 1.0f, 1.0f });
				((AccessorEntityLiving) entity).inventoryHandsDropChances(new float[] { 1.0f, 1.0f });
				entities.add(new EntityOptions("Zombie (Leather Armor, Enchanted Sword)", entity));
				
				entity = new EntitySkeleton(world);
				MCVer.setItemToSlot(entity, 0, addEnchants(new ItemStack(Items.BOW), skelBow));
				MCVer.setItemToSlot(entity, 1, new ItemStack(Items.GOLDEN_BOOTS));
				MCVer.setItemToSlot(entity, 2, new ItemStack(Items.GOLDEN_CHESTPLATE));
				MCVer.setItemToSlot(entity, 3, new ItemStack(Items.GOLDEN_LEGGINGS));
				MCVer.setItemToSlot(entity, 4, new ItemStack(Items.GOLDEN_HELMET));
				((AccessorEntityLiving) entity).inventoryArmorDropChances(new float[] { 1.0f, 1.0f, 1.0f, 1.0f });
				((AccessorEntityLiving) entity).inventoryHandsDropChances(new float[] { 1.0f, 1.0f });
				entities.add(new EntityOptions("Skeleton (Gold Armor, Enchanted Bow)", entity));
				
				entity = new EntityZombie(world);
				MCVer.setItemToSlot(entity, 0, addEnchants(new ItemStack(Items.IRON_SWORD), zombieSword));
				MCVer.setItemToSlot(entity, 1, new ItemStack(Items.GOLDEN_BOOTS));
				MCVer.setItemToSlot(entity, 2, new ItemStack(Items.GOLDEN_CHESTPLATE));
				MCVer.setItemToSlot(entity, 3, new ItemStack(Items.GOLDEN_LEGGINGS));
				MCVer.setItemToSlot(entity, 4, new ItemStack(Items.GOLDEN_HELMET));
				((AccessorEntityLiving) entity).inventoryArmorDropChances(new float[] { 1.0f, 1.0f, 1.0f, 1.0f });
				((AccessorEntityLiving) entity).inventoryHandsDropChances(new float[] { 1.0f, 1.0f });
				entities.add(new EntityOptions("Zombie (Gold Armor, Enchanted Sword)", entity));
				
				entity = new EntitySkeleton(world);
				MCVer.setItemToSlot(entity, 0, addEnchants(new ItemStack(Items.BOW), skelBow));
				MCVer.setItemToSlot(entity, 1, new ItemStack(Items.CHAINMAIL_BOOTS));
				MCVer.setItemToSlot(entity, 2, new ItemStack(Items.CHAINMAIL_CHESTPLATE));
				MCVer.setItemToSlot(entity, 3, new ItemStack(Items.CHAINMAIL_LEGGINGS));
				MCVer.setItemToSlot(entity, 4, new ItemStack(Items.CHAINMAIL_HELMET));
				((AccessorEntityLiving) entity).inventoryArmorDropChances(new float[] { 1.0f, 1.0f, 1.0f, 1.0f });
				((AccessorEntityLiving) entity).inventoryHandsDropChances(new float[] { 1.0f, 1.0f });
				entities.add(new EntityOptions("Skeleton (Chain Armor, Enchanted Bow)", entity));
				
				entity = new EntityZombie(world);
				MCVer.setItemToSlot(entity, 0, addEnchants(new ItemStack(Items.IRON_SWORD), zombieSword));
				MCVer.setItemToSlot(entity, 1, new ItemStack(Items.CHAINMAIL_BOOTS));
				MCVer.setItemToSlot(entity, 2, new ItemStack(Items.CHAINMAIL_CHESTPLATE));
				MCVer.setItemToSlot(entity, 3, new ItemStack(Items.CHAINMAIL_LEGGINGS));
				MCVer.setItemToSlot(entity, 4, new ItemStack(Items.CHAINMAIL_HELMET));
				((AccessorEntityLiving) entity).inventoryArmorDropChances(new float[] { 1.0f, 1.0f, 1.0f, 1.0f });
				((AccessorEntityLiving) entity).inventoryHandsDropChances(new float[] { 1.0f, 1.0f });
				entities.add(new EntityOptions("Zombie (Chain Armor, Enchanted Sword)", entity));
				
				entity = new EntitySkeleton(world);
				MCVer.setItemToSlot(entity, 0, addEnchants(new ItemStack(Items.BOW), skelBow));
				MCVer.setItemToSlot(entity, 1, new ItemStack(Items.IRON_BOOTS));
				MCVer.setItemToSlot(entity, 2, new ItemStack(Items.IRON_CHESTPLATE));
				MCVer.setItemToSlot(entity, 3, new ItemStack(Items.IRON_LEGGINGS));
				MCVer.setItemToSlot(entity, 4, new ItemStack(Items.IRON_HELMET));
				((AccessorEntityLiving) entity).inventoryArmorDropChances(new float[] { 1.0f, 1.0f, 1.0f, 1.0f });
				((AccessorEntityLiving) entity).inventoryHandsDropChances(new float[] { 1.0f, 1.0f });
				entities.add(new EntityOptions("Skeleton (Iron Armor, Enchanted Sword)", entity));
				
				entity = new EntityZombie(world);
				MCVer.setItemToSlot(entity, 0, addEnchants(new ItemStack(Items.IRON_SWORD), zombieSword));
				MCVer.setItemToSlot(entity, 1, new ItemStack(Items.IRON_BOOTS));
				MCVer.setItemToSlot(entity, 2, new ItemStack(Items.IRON_CHESTPLATE));
				MCVer.setItemToSlot(entity, 3, new ItemStack(Items.IRON_LEGGINGS));
				MCVer.setItemToSlot(entity, 4, new ItemStack(Items.IRON_HELMET));
				((AccessorEntityLiving) entity).inventoryArmorDropChances(new float[] { 1.0f, 1.0f, 1.0f, 1.0f });
				((AccessorEntityLiving) entity).inventoryHandsDropChances(new float[] { 1.0f, 1.0f });
				entities.add(new EntityOptions("Zombie (Iron Armor, Enchanted Sword)", entity));
				
				entity = new EntitySkeleton(world);
				MCVer.setItemToSlot(entity, 0, addEnchants(new ItemStack(Items.BOW), skelBow));
				MCVer.setItemToSlot(entity, 1, new ItemStack(Items.DIAMOND_BOOTS));
				MCVer.setItemToSlot(entity, 2, new ItemStack(Items.DIAMOND_CHESTPLATE));
				MCVer.setItemToSlot(entity, 3, new ItemStack(Items.DIAMOND_LEGGINGS));
				MCVer.setItemToSlot(entity, 4, new ItemStack(Items.DIAMOND_HELMET));
				((AccessorEntityLiving) entity).inventoryArmorDropChances(new float[] { 1.0f, 1.0f, 1.0f, 1.0f });
				((AccessorEntityLiving) entity).inventoryHandsDropChances(new float[] { 1.0f, 1.0f });
				entities.add(new EntityOptions("Skeleton (Diamond Armor, Enchanted Bow)", entity));
				
				entity = new EntityZombie(world);
				MCVer.setItemToSlot(entity, 0, addEnchants(new ItemStack(Items.IRON_SWORD), zombieSword));
				MCVer.setItemToSlot(entity, 1, new ItemStack(Items.DIAMOND_BOOTS));
				MCVer.setItemToSlot(entity, 2, new ItemStack(Items.DIAMOND_CHESTPLATE));
				MCVer.setItemToSlot(entity, 3, new ItemStack(Items.DIAMOND_LEGGINGS));
				MCVer.setItemToSlot(entity, 4, new ItemStack(Items.DIAMOND_HELMET));
				((AccessorEntityLiving) entity).inventoryArmorDropChances(new float[] { 1.0f, 1.0f, 1.0f, 1.0f });
				((AccessorEntityLiving) entity).inventoryHandsDropChances(new float[] { 1.0f, 1.0f });
				entities.add(new EntityOptions("Zombie (Diamond Armor, Enchanted Sword)", entity));
			}
		}else if(dim==-1) {
			entities.add(new EntityOptions("Blaze", new EntityBlaze(world)));
			entities.add(new EntityOptions("Ghast", new EntityGhast(world)));
			entities.add(new EntityOptions("Magma Cube", new EntityMagmaCube(world)));
			EntityLiving entity = new EntitySkeleton(world);
			MCVer.setItemToSlot(entity, 0, new ItemStack(Items.BOW));
			entities.add(new EntityOptions("Skeleton", entity));
			//#if MC>=11100
			entity = new net.minecraft.entity.monster.EntityWitherSkeleton(world);
			//#else
//$$ 			entity = new EntitySkeleton(world);
//$$ 			EntitySkeleton skelEntity=(EntitySkeleton)entity;
//$$ 			skelEntity.setSkeletonType(net.minecraft.entity.monster.SkeletonType.WITHER);
			//#endif
			MCVer.setItemToSlot(entity, 0, new ItemStack(Items.STONE_SWORD));
			entities.add(new EntityOptions("Wither Skeleton", entity));
			
			
			if (MCVer.world(mc).getDifficulty() == EnumDifficulty.HARD) {
				entity = new EntitySkeleton(world);
				MCVer.setItemToSlot(entity, 0, addEnchants(new ItemStack(Items.BOW), skelBow));
				((AccessorEntityLiving) entity).inventoryArmorDropChances(new float[] { 1.0f, 1.0f, 1.0f, 1.0f });
				((AccessorEntityLiving) entity).inventoryHandsDropChances(new float[] { 1.0f, 1.0f });
				entities.add(new EntityOptions("Skeleton (Enchanted Bow)", entity));
				
				entity = new EntitySkeleton(world);
				MCVer.setItemToSlot(entity, 0, addEnchants(new ItemStack(Items.BOW), skelBow));
				MCVer.setItemToSlot(entity, 1, new ItemStack(Items.LEATHER_BOOTS));
				MCVer.setItemToSlot(entity, 2, new ItemStack(Items.LEATHER_CHESTPLATE));
				MCVer.setItemToSlot(entity, 3, new ItemStack(Items.LEATHER_LEGGINGS));
				MCVer.setItemToSlot(entity, 4, new ItemStack(Items.LEATHER_HELMET));
				((AccessorEntityLiving) entity).inventoryArmorDropChances(new float[] { 1.0f, 1.0f, 1.0f, 1.0f });
				((AccessorEntityLiving) entity).inventoryHandsDropChances(new float[] { 1.0f, 1.0f });
				entities.add(new EntityOptions("Skeleton (Leather Armor, Enchanted Bow)", entity));
				
				entity = new EntitySkeleton(world);
				MCVer.setItemToSlot(entity, 0, addEnchants(new ItemStack(Items.BOW), skelBow));
				MCVer.setItemToSlot(entity, 1, new ItemStack(Items.GOLDEN_BOOTS));
				MCVer.setItemToSlot(entity, 2, new ItemStack(Items.GOLDEN_CHESTPLATE));
				MCVer.setItemToSlot(entity, 3, new ItemStack(Items.GOLDEN_LEGGINGS));
				MCVer.setItemToSlot(entity, 4, new ItemStack(Items.GOLDEN_HELMET));
				((AccessorEntityLiving) entity).inventoryArmorDropChances(new float[] { 1.0f, 1.0f, 1.0f, 1.0f });
				((AccessorEntityLiving) entity).inventoryHandsDropChances(new float[] { 1.0f, 1.0f });
				entities.add(new EntityOptions("Skeleton (Gold Armor, Enchanted Bow)", entity));
				
				entity = new EntitySkeleton(world);
				MCVer.setItemToSlot(entity, 0, addEnchants(new ItemStack(Items.BOW), skelBow));
				MCVer.setItemToSlot(entity, 1, new ItemStack(Items.CHAINMAIL_BOOTS));
				MCVer.setItemToSlot(entity, 2, new ItemStack(Items.CHAINMAIL_CHESTPLATE));
				MCVer.setItemToSlot(entity, 3, new ItemStack(Items.CHAINMAIL_LEGGINGS));
				MCVer.setItemToSlot(entity, 4, new ItemStack(Items.CHAINMAIL_HELMET));
				((AccessorEntityLiving) entity).inventoryArmorDropChances(new float[] { 1.0f, 1.0f, 1.0f, 1.0f });
				((AccessorEntityLiving) entity).inventoryHandsDropChances(new float[] { 1.0f, 1.0f });
				entities.add(new EntityOptions("Skeleton (Chain Armor, Enchanted Bow)", entity));
				
				entity = new EntitySkeleton(world);
				MCVer.setItemToSlot(entity, 0, addEnchants(new ItemStack(Items.BOW), skelBow));
				MCVer.setItemToSlot(entity, 1, new ItemStack(Items.IRON_BOOTS));
				MCVer.setItemToSlot(entity, 2, new ItemStack(Items.IRON_CHESTPLATE));
				MCVer.setItemToSlot(entity, 3, new ItemStack(Items.IRON_LEGGINGS));
				MCVer.setItemToSlot(entity, 4, new ItemStack(Items.IRON_HELMET));
				((AccessorEntityLiving) entity).inventoryArmorDropChances(new float[] { 1.0f, 1.0f, 1.0f, 1.0f });
				((AccessorEntityLiving) entity).inventoryHandsDropChances(new float[] { 1.0f, 1.0f });
				entities.add(new EntityOptions("Skeleton (Iron Armor, Enchanted Sword)", entity));
				
				entity = new EntitySkeleton(world);
				MCVer.setItemToSlot(entity, 0, addEnchants(new ItemStack(Items.BOW), skelBow));
				MCVer.setItemToSlot(entity, 1, new ItemStack(Items.DIAMOND_BOOTS));
				MCVer.setItemToSlot(entity, 2, new ItemStack(Items.DIAMOND_CHESTPLATE));
				MCVer.setItemToSlot(entity, 3, new ItemStack(Items.DIAMOND_LEGGINGS));
				MCVer.setItemToSlot(entity, 4, new ItemStack(Items.DIAMOND_HELMET));
				((AccessorEntityLiving) entity).inventoryArmorDropChances(new float[] { 1.0f, 1.0f, 1.0f, 1.0f });
				((AccessorEntityLiving) entity).inventoryHandsDropChances(new float[] { 1.0f, 1.0f });
				entities.add(new EntityOptions("Skeleton (Diamond Armor, Enchanted Bow)", entity));
			}
		}else if(dim==1) {
			entities.add(new EntityOptions("Enderman", new EntityEnderman(world)));
		}
		return entities;
	}
	
	public static boolean canSpawn() {
		entity.setPosition(target.x, target.y, target.z);
		boolean flag=new AIManipMod.Vec(entity.getPositionVector()).distanceTo(playerPos)>24D && new AIManipMod.Vec(entity.getPositionVector()).distanceTo(playerPos)<128D;
		if(flag) {
			WorldServer world=MCVer.world(Minecraft.getMinecraft().getIntegratedServer(), entity.dimension);
			return ((EntityLiving) entity).getCanSpawnHere() && !world.collidesWithAnyBlock(entity.getEntityBoundingBox());
		}else return false;
	}
	
	public class EntityOptions{
		public final String title;
		
		public final EntityLiving entity;

		public EntityOptions(String title, EntityLiving entity) {
			this.title = title;
			this.entity = entity;
		}
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
}
