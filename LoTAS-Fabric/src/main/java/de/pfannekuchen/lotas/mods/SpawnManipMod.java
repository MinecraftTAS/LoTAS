package de.pfannekuchen.lotas.mods;

import java.util.ArrayList;
import java.util.List;

import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.mixin.accessors.AccessorDimensionTypes;
import de.pfannekuchen.lotas.mixin.accessors.AccessorMobEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.monster.CaveSpider;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.monster.Husk;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.phys.Vec3;

/**
 * Allows for spawning entities at valid locations
 * @author ScribbleLP
 */
public class SpawnManipMod {
	
	private Minecraft mc = Minecraft.getInstance();
	
	static Entity entity;
	
	static Vec3 target;
	
	static Vec3 playerPos;
	
	private final Direction orientation;
	
	private final EnchantmentInstance[] skelBow = new EnchantmentInstance[] { new EnchantmentInstance(Enchantments.UNBREAKING, 1), new EnchantmentInstance(Enchantments.POWER_ARROWS, 1) };
	private final EnchantmentInstance[] zombieSword = new EnchantmentInstance[] { new EnchantmentInstance(Enchantments.SHARPNESS, 2), new EnchantmentInstance(Enchantments.UNBREAKING, 2) };
	
	public SpawnManipMod() {
		orientation =  mc.player.getDirection();
		target=mc.player.position();
		for(int i=0; i<25;i++) {
			changeTargetForward();
		}
		playerPos=mc.player.position();
	}
	
	public void debugSpawn() {
		ServerLevel world=(ServerLevel) MCVer.getCurrentLevel();
		Entity entity = new Skeleton(EntityType.SKELETON, world);
		entity.setPos(target.x, target.y, target.z);
		//#if MC>=11600
//$$ 		((Mob)entity).finalizeSpawn(world, world.getCurrentDifficultyAt(entity.blockPosition()), MobSpawnType.NATURAL, null, null);
		//#else
		((Mob)entity).finalizeSpawn(world, world.getCurrentDifficultyAt(entity.getCommandSenderBlockPosition()), MobSpawnType.NATURAL, null, null);
		//#endif
		world.addFreshEntity(entity);
	}
	
	public void setEntity(Entity entity) {
		SpawnManipMod.entity=entity;
	}
	
	public void confirm() {
		if(canSpawn()) {
			ServerLevel world=(ServerLevel) MCVer.getCurrentLevel();
			int targetX=(int) Math.round(target.x-0.5);
			int targetY=(int) Math.round(target.y);
			int targetZ=(int) Math.round(target.z-0.5);
			
			entity.setPos(targetX, targetY, targetZ);
			world.addFreshEntity(entity);
		}
	}
	
	public void changeTargetForward() {
		switch(orientation) {
		case NORTH:
			target=target.add(0, 0, -1);
			break;
		case EAST:
			target=target.add(1, 0, 0);
			break;
		case SOUTH:
			target=target.add(0, 0, 1);
			break;
		case WEST:
			target=target.add(-1, 0, 0);
			break;
		default:
			break;
		}
	}
	
	public void changeTargetBack() {
		switch(orientation) {
		case NORTH:
			target=target.add(0, 0, 1);
			break;
		case EAST:
			target=target.add(-1, 0, 0);
			break;
		case SOUTH:
			target=target.add(0, 0, -1);
			break;
		case WEST:
			target=target.add(1, 0, 0);
			break;
		default:
			break;
		}
	}
	
	public void changeTargetLeft() {
		switch(orientation) {
		case NORTH:
			target=target.add(-1, 0, 0);
			break;
		case EAST:
			target=target.add(0, 0, -1);
			break;
		case SOUTH:
			target=target.add(1, 0, 0);
			break;
		case WEST:
			target=target.add(0, 0, 1);
			break;
		default:
			break;
		}
	}
	
	public void changeTargetRight() {
		switch(orientation) {
		case NORTH:
			target=target.add(1, 0, 0);
			break;
		case EAST:
			target=target.add(0, 0, 1);
			break;
		case SOUTH:
			target=target.add(-1, 0, 0);
			break;
		case WEST:
			target=target.add(0, 0, -1);
			break;
		default:
			break;
		}
	}
	
	public void changeTargetUp() {
		target=target.add(0, 1, 0);
	}
	
	public void changeTargetDown() {
		target=target.add(0, -1, 0);
	}
	
	public void setTarget(Vec3 target) {
		SpawnManipMod.target = target;
	}
	
	public void setTargetToPlayer() {
		target=mc.player.position();
	}
	
	public static Vec3 getTargetPos() {
		return target;
	}
	
	public List<EntityOptions> getManipList(){
		List<EntityOptions> entities=new ArrayList<EntityOptions>();
		//#if MC>=11600
//$$ 		DimensionType dimension = MCVer.getCurrentLevel().dimensionType();
		//#else
		DimensionType dimension = MCVer.getCurrentLevel().getDimension().getType();
		//#endif
		ServerLevel world = mc.getSingleplayerServer().getPlayerList().getPlayers().get(0).getLevel();
		float[] armor={1f,1f,1f,1f};
		float[] hand= {1f,1f};
		
		if(dimension == AccessorDimensionTypes.getOverworld()) {
			entities.add(new EntityOptions("Cave Spider", new CaveSpider(EntityType.CAVE_SPIDER, world)));
			entities.add(new EntityOptions("Creeper", new Creeper(EntityType.CREEPER, world)));
			entities.add(new EntityOptions("Enderman", new EnderMan(EntityType.ENDERMAN, world)));
			entities.add(new EntityOptions("Husk", new Husk(EntityType.HUSK, world)));
			entities.add(new EntityOptions("Iron Golem", new IronGolem(EntityType.IRON_GOLEM, world)));
			LivingEntity entity = new Skeleton(EntityType.SKELETON, world);
			entity.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.BOW));
			entities.add(new EntityOptions("Skeleton", entity));
			entities.add(new EntityOptions("Slime", new Slime(EntityType.SLIME, world)));
			entities.add(new EntityOptions("Spider", new Spider(EntityType.SPIDER, world)));
			entities.add(new EntityOptions("Witch", new Witch(EntityType.WITCH, world)));
			entities.add(new EntityOptions("Zombie", new Zombie(world)));
			entities.add(new EntityOptions("Zombievillager", new ZombieVillager(EntityType.ZOMBIE_VILLAGER, world)));
			
			if (Minecraft.getInstance().level.getDifficulty() == Difficulty.HARD) {
				entity = new Skeleton(EntityType.SKELETON, world);
				entity.setItemInHand(InteractionHand.MAIN_HAND, addEnchants(new ItemStack(Items.BOW), skelBow));
				((AccessorMobEntity)entity).setArmorDropChances(armor);
				((AccessorMobEntity)entity).setHandDropChances(hand);
				entities.add(new EntityOptions("Skeleton (Enchanted Bow)", entity));
				
				entity = new Zombie(world);
				entity.setItemInHand(InteractionHand.MAIN_HAND, addEnchants(new ItemStack(Items.IRON_SWORD), zombieSword));
				((AccessorMobEntity)entity).setArmorDropChances(armor);
				((AccessorMobEntity)entity).setHandDropChances(hand);
				entities.add(new EntityOptions("Zombie (Enchanted Sword)", entity));

				entity = new Skeleton(EntityType.SKELETON, world);
				entity.setItemInHand(InteractionHand.MAIN_HAND, addEnchants(new ItemStack(Items.BOW), skelBow));
				entity.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.LEATHER_BOOTS));
				entity.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.LEATHER_CHESTPLATE));
				entity.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.LEATHER_LEGGINGS));
				entity.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.LEATHER_HELMET));
				((AccessorMobEntity)entity).setArmorDropChances(armor);
				((AccessorMobEntity)entity).setHandDropChances(hand);
				entities.add(new EntityOptions("Skeleton (Leather Armor, Enchanted Bow)", entity));
				
				entity = new Zombie(world);
				entity.setItemInHand(InteractionHand.MAIN_HAND, addEnchants(new ItemStack(Items.IRON_SWORD), zombieSword));
				entity.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.LEATHER_BOOTS));
				entity.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.LEATHER_CHESTPLATE));
				entity.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.LEATHER_LEGGINGS));
				entity.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.LEATHER_HELMET));
				((AccessorMobEntity)entity).setArmorDropChances(armor);
				((AccessorMobEntity)entity).setHandDropChances(hand);
				entities.add(new EntityOptions("Zombie (Leather Armor, Enchanted Sword)", entity));
				
				entity = new Skeleton(EntityType.SKELETON, world);
				entity.setItemInHand(InteractionHand.MAIN_HAND, addEnchants(new ItemStack(Items.BOW), skelBow));
				entity.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.GOLDEN_BOOTS));
				entity.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.GOLDEN_CHESTPLATE));
				entity.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.GOLDEN_LEGGINGS));
				entity.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.GOLDEN_HELMET));
				((AccessorMobEntity)entity).setArmorDropChances(armor);
				((AccessorMobEntity)entity).setHandDropChances(hand);
				entities.add(new EntityOptions("Skeleton (Gold Armor, Enchanted Bow)", entity));
				
				entity = new Zombie(world);
				entity.setItemInHand(InteractionHand.MAIN_HAND, addEnchants(new ItemStack(Items.IRON_SWORD), zombieSword));
				entity.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.GOLDEN_BOOTS));
				entity.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.GOLDEN_CHESTPLATE));
				entity.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.GOLDEN_LEGGINGS));
				entity.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.GOLDEN_HELMET));
				((AccessorMobEntity)entity).setArmorDropChances(armor);
				((AccessorMobEntity)entity).setHandDropChances(hand);
				entities.add(new EntityOptions("Zombie (Gold Armor, Enchanted Sword)", entity));
				
				entity = new Skeleton(EntityType.SKELETON, world);
				entity.setItemInHand(InteractionHand.MAIN_HAND, addEnchants(new ItemStack(Items.BOW), skelBow));
				entity.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.CHAINMAIL_BOOTS));
				entity.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.CHAINMAIL_CHESTPLATE));
				entity.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.CHAINMAIL_LEGGINGS));
				entity.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.CHAINMAIL_HELMET));
				((AccessorMobEntity)entity).setArmorDropChances(armor);
				((AccessorMobEntity)entity).setHandDropChances(hand);
				entities.add(new EntityOptions("Skeleton (Chain Armor, Enchanted Bow)", entity));
				
				entity = new Zombie(world);
				entity.setItemInHand(InteractionHand.MAIN_HAND, addEnchants(new ItemStack(Items.IRON_SWORD), zombieSword));
				entity.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.CHAINMAIL_BOOTS));
				entity.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.CHAINMAIL_CHESTPLATE));
				entity.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.CHAINMAIL_LEGGINGS));
				entity.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.CHAINMAIL_HELMET));
				((AccessorMobEntity)entity).setArmorDropChances(armor);
				((AccessorMobEntity)entity).setHandDropChances(hand);
				entities.add(new EntityOptions("Zombie (Chain Armor, Enchanted Sword)", entity));
				
				entity = new Skeleton(EntityType.SKELETON, world);
				entity.setItemInHand(InteractionHand.MAIN_HAND, addEnchants(new ItemStack(Items.BOW), skelBow));
				entity.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.IRON_BOOTS));
				entity.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
				entity.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.IRON_LEGGINGS));
				entity.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
				((AccessorMobEntity)entity).setArmorDropChances(armor);
				((AccessorMobEntity)entity).setHandDropChances(hand);
				entities.add(new EntityOptions("Skeleton (Iron Armor, Enchanted Sword)", entity));
				
				entity = new Zombie(world);
				entity.setItemInHand(InteractionHand.MAIN_HAND, addEnchants(new ItemStack(Items.IRON_SWORD), zombieSword));
				entity.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.IRON_BOOTS));
				entity.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
				entity.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.IRON_LEGGINGS));
				entity.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
				((AccessorMobEntity)entity).setArmorDropChances(armor);
				((AccessorMobEntity)entity).setHandDropChances(hand);
				entities.add(new EntityOptions("Zombie (Iron Armor, Enchanted Sword)", entity));
				
				entity = new Skeleton(EntityType.SKELETON, world);
				entity.setItemInHand(InteractionHand.MAIN_HAND, addEnchants(new ItemStack(Items.BOW), skelBow));
				entity.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.BOW));
				entity.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.DIAMOND_BOOTS));
				entity.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE));
				entity.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS));
				entity.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.DIAMOND_HELMET));
				((AccessorMobEntity)entity).setArmorDropChances(armor);
				((AccessorMobEntity)entity).setHandDropChances(hand);
				entities.add(new EntityOptions("Skeleton (Diamond Armor, Enchanted Bow)", entity));
				
				entity = new Zombie(world);
				entity.setItemInHand(InteractionHand.MAIN_HAND, addEnchants(new ItemStack(Items.IRON_SWORD), zombieSword));
				entity.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.DIAMOND_BOOTS));
				entity.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE));
				entity.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS));
				entity.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.DIAMOND_HELMET));
				((AccessorMobEntity)entity).setArmorDropChances(armor);
				((AccessorMobEntity)entity).setHandDropChances(hand);
				entities.add(new EntityOptions("Zombie (Diamond Armor, Enchanted Sword)", entity));
			}
		}else if(dimension==AccessorDimensionTypes.getNether()) {
			entities.add(new EntityOptions("Blaze", new Blaze(EntityType.BLAZE, world)));
			entities.add(new EntityOptions("Ghast", new Ghast(EntityType.GHAST, world)));
			entities.add(new EntityOptions("Magma Cube", new Ghast(EntityType.GHAST, world)));
			LivingEntity entity = new Skeleton(EntityType.SKELETON, world);
			entity.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.BOW));
			entities.add(new EntityOptions("Skeleton", entity));
			entity = new WitherSkeleton(EntityType.WITHER_SKELETON, world);
			entity.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.STONE_SWORD));
			entities.add(new EntityOptions("Wither Skeleton", entity));
			
			if (Minecraft.getInstance().level.getDifficulty() == Difficulty.HARD) {
				entity = new Skeleton(EntityType.SKELETON, world);
				entity.setItemInHand(InteractionHand.MAIN_HAND, addEnchants(new ItemStack(Items.BOW), skelBow));
				((AccessorMobEntity)entity).setArmorDropChances(armor);
				((AccessorMobEntity)entity).setHandDropChances(hand);
				entities.add(new EntityOptions("Skeleton (Enchanted Bow)", entity));
				
				entity = new Skeleton(EntityType.SKELETON, world);
				entity.setItemInHand(InteractionHand.MAIN_HAND, addEnchants(new ItemStack(Items.BOW), skelBow));
				entity.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.LEATHER_BOOTS));
				entity.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.LEATHER_CHESTPLATE));
				entity.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.LEATHER_LEGGINGS));
				entity.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.LEATHER_HELMET));
				((AccessorMobEntity)entity).setArmorDropChances(armor);
				((AccessorMobEntity)entity).setHandDropChances(hand);
				entities.add(new EntityOptions("Skeleton (Leather Armor, Enchanted Bow)", entity));
				
				entity = new Skeleton(EntityType.SKELETON, world);
				entity.setItemInHand(InteractionHand.MAIN_HAND, addEnchants(new ItemStack(Items.BOW), skelBow));
				entity.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.GOLDEN_BOOTS));
				entity.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.GOLDEN_CHESTPLATE));
				entity.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.GOLDEN_LEGGINGS));
				entity.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.GOLDEN_HELMET));
				((AccessorMobEntity)entity).setArmorDropChances(armor);
				((AccessorMobEntity)entity).setHandDropChances(hand);
				entities.add(new EntityOptions("Skeleton (Gold Armor, Enchanted Bow)", entity));
				
				entity = new Skeleton(EntityType.SKELETON, world);
				entity.setItemInHand(InteractionHand.MAIN_HAND, addEnchants(new ItemStack(Items.BOW), skelBow));
				entity.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.CHAINMAIL_BOOTS));
				entity.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.CHAINMAIL_CHESTPLATE));
				entity.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.CHAINMAIL_LEGGINGS));
				entity.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.CHAINMAIL_HELMET));
				((AccessorMobEntity)entity).setArmorDropChances(armor);
				((AccessorMobEntity)entity).setHandDropChances(hand);
				entities.add(new EntityOptions("Skeleton (Chain Armor, Enchanted Bow)", entity));
				
				entity = new Skeleton(EntityType.SKELETON, world);
				entity.setItemInHand(InteractionHand.MAIN_HAND, addEnchants(new ItemStack(Items.BOW), skelBow));
				entity.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.IRON_BOOTS));
				entity.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
				entity.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.IRON_LEGGINGS));
				entity.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
				((AccessorMobEntity)entity).setArmorDropChances(armor);
				((AccessorMobEntity)entity).setHandDropChances(hand);
				entities.add(new EntityOptions("Skeleton (Iron Armor, Enchanted Sword)", entity));
				
				entity = new Skeleton(EntityType.SKELETON, world);
				entity.setItemInHand(InteractionHand.MAIN_HAND, addEnchants(new ItemStack(Items.BOW), skelBow));
				entity.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.BOW));
				entity.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.DIAMOND_BOOTS));
				entity.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE));
				entity.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS));
				entity.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.DIAMOND_HELMET));
				((AccessorMobEntity)entity).setArmorDropChances(armor);
				((AccessorMobEntity)entity).setHandDropChances(hand);
				entities.add(new EntityOptions("Skeleton (Diamond Armor, Enchanted Bow)", entity));
			}
		}else if(dimension==AccessorDimensionTypes.getEnd()) {
			entities.add(new EntityOptions("Enderman", new EnderMan(EntityType.ENDERMAN, world)));
		}
		return entities;
	}
	
	public static boolean canSpawn() {
		entity.setPos(target.x, target.y, target.z);
		boolean flag=entity.position().distanceTo(playerPos)>24D && entity.position().distanceTo(playerPos)<128D;
		if(flag) {
			ServerLevel world=(ServerLevel) MCVer.getCurrentLevel();
			return ((Mob) entity).checkSpawnRules(world, net.minecraft.world.entity.MobSpawnType.NATURAL) && world.noCollision(entity);
		}else return false;
	}
	
	public class EntityOptions{
		public final String title;
		
		public final LivingEntity entity;

		public EntityOptions(String title, LivingEntity entity) {
			this.title = title;
			this.entity = entity;
		}
	}
	
	public ItemStack addEnchants(ItemStack item, EnchantmentInstance[] enchants) {
		for (EnchantmentInstance enchantmentData : enchants) {
			item.enchant(enchantmentData.enchantment, enchantmentData.level);
		}
		return item;
	}
}
