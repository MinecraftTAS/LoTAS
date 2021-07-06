package de.pfannekuchen.lotas.mods;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.pfannekuchen.lotas.mixin.accessors.AccessorMobEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.enchantment.Enchantments;
//#if MC>=11601
//$$ import net.minecraft.enchantment.Enchantment;
//#else
import net.minecraft.enchantment.InfoEnchantment;
import net.minecraft.entity.SpawnType;
//#endif

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.BlazeEntity;
import net.minecraft.entity.mob.CaveSpiderEntity;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.mob.GhastEntity;
import net.minecraft.entity.mob.HuskEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.entity.mob.SpiderEntity;
import net.minecraft.entity.mob.WitchEntity;
import net.minecraft.entity.mob.WitherSkeletonEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.mob.ZombieVillagerEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.dimension.DimensionType;

public class SpawnManipMod {
	
	private MinecraftClient mc = MinecraftClient.getInstance();
	
	static Entity entity;
	
	static Vec3d target;
	
	static Vec3d playerPos;
	
	private final Direction orientation;
	
	//#if MC>=11601
//$$ 		@SuppressWarnings("serial")
//$$ 		private final HashMap<Enchantment, Integer> skelBow = new HashMap<Enchantment, Integer>() {{
//$$ 	   	put(Enchantments.UNBREAKING, 1);
//$$ 	   	put(Enchantments.POWER, 1);}};
//$$ 		@SuppressWarnings("serial")
//$$ 		private final HashMap<Enchantment, Integer> zombieSword = new HashMap<Enchantment, Integer>() {{
//$$ 	   	put(Enchantments.SHARPNESS, 2);
//$$ 	   	put(Enchantments.UNBREAKING, 2);}};
		//#else
		private final InfoEnchantment[] skelBow = new InfoEnchantment[] { new InfoEnchantment(Enchantments.UNBREAKING, 1), new InfoEnchantment(Enchantments.POWER, 1) };
		private final InfoEnchantment[] zombieSword = new InfoEnchantment[] { new InfoEnchantment(Enchantments.SHARPNESS, 2), new InfoEnchantment(Enchantments.UNBREAKING, 2) };
		//#endif
	
	public SpawnManipMod() {
		orientation =  mc.player.getHorizontalFacing();
		target=mc.player.getPos();
		for(int i=0; i<3;i++) {
			changeTargetForward();
		}
		playerPos=mc.player.getPos();
	}
	
	public void debugSpawn() {
		ServerWorld world=mc.getServer().getWorld(mc.player.dimension);
		Entity entity = new SkeletonEntity(EntityType.SKELETON, world);
		entity.updatePosition(target.x, target.y, target.z);
		((MobEntity)entity).initialize(world, world.getLocalDifficulty(entity.getBlockPos()), SpawnType.NATURAL, null, null);
		world.spawnEntity(entity);
	}
	
	public void setEntity(Entity entity) {
		SpawnManipMod.entity=entity;
	}
	
	public void confirm() {
		if(canSpawn()) {
			ServerWorld world=mc.getServer().getWorld(mc.player.dimension);
			entity.updatePosition(Math.round(target.x)-0.5, Math.round(target.y), Math.round(target.z)-0.5);
			world.spawnEntity(entity);
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
		}
	}
	
	public void changeTargetUp() {
		target=target.add(0, 1, 0);
	}
	
	public void changeTargetDown() {
		target=target.add(0, -1, 0);
	}
	
	public void setTarget(Vec3d target) {
		SpawnManipMod.target = target;
	}
	
	public void setTargetToPlayer() {
		target=mc.player.getPos();
	}
	
	public static Vec3d getTargetPos() {
		return target;
	}
	
	public List<EntityOptions> getManipList(){
		List<EntityOptions> entities=new ArrayList<EntityOptions>();
		DimensionType dimension = mc.player.dimension;
		ServerWorld world = mc.getServer().getPlayerManager().getPlayerList().get(0).getServerWorld();
		float[] armor={1f,1f,1f,1f};
		float[] hand= {1f,1f};
		
		if(dimension == DimensionType.OVERWORLD) {
			entities.add(new EntityOptions("Cave Spider", new CaveSpiderEntity(EntityType.CAVE_SPIDER, world)));
			entities.add(new EntityOptions("Creeper", new CreeperEntity(EntityType.CREEPER, world)));
			entities.add(new EntityOptions("Enderman", new EndermanEntity(EntityType.ENDERMAN, world)));
			entities.add(new EntityOptions("Husk", new HuskEntity(EntityType.HUSK, world)));
			entities.add(new EntityOptions("Iron Golem", new IronGolemEntity(EntityType.IRON_GOLEM, world)));
			LivingEntity entity = new SkeletonEntity(EntityType.SKELETON, world);
			entity.setStackInHand(Hand.MAIN_HAND, new ItemStack(Items.BOW));
			entities.add(new EntityOptions("Skeleton", entity));
			entities.add(new EntityOptions("Slime", new SlimeEntity(EntityType.SLIME, world)));
			entities.add(new EntityOptions("Spider", new SpiderEntity(EntityType.SPIDER, world)));
			entities.add(new EntityOptions("Witch", new WitchEntity(EntityType.WITCH, world)));
			entities.add(new EntityOptions("Zombie", new ZombieEntity(world)));
			entities.add(new EntityOptions("Zombievillager", new ZombieVillagerEntity(EntityType.ZOMBIE_VILLAGER, world)));
			
			if (MinecraftClient.getInstance().world.getDifficulty() == Difficulty.HARD) {
				entity = new SkeletonEntity(EntityType.SKELETON, world);
				entity.setStackInHand(Hand.MAIN_HAND, addEnchants(new ItemStack(Items.BOW), skelBow));
				((AccessorMobEntity)entity).setArmorDropChances(armor);
				((AccessorMobEntity)entity).setHandDropChances(hand);
				entities.add(new EntityOptions("Skeleton (Enchanted Bow)", entity));
				
				entity = new ZombieEntity(world);
				entity.setStackInHand(Hand.MAIN_HAND, addEnchants(new ItemStack(Items.IRON_SWORD), zombieSword));
				((AccessorMobEntity)entity).setArmorDropChances(armor);
				((AccessorMobEntity)entity).setHandDropChances(hand);
				entities.add(new EntityOptions("Zombie (Enchanted Sword)", entity));

				entity = new SkeletonEntity(EntityType.SKELETON, world);
				entity.setStackInHand(Hand.MAIN_HAND, addEnchants(new ItemStack(Items.BOW), skelBow));
				entity.equipStack(EquipmentSlot.FEET, new ItemStack(Items.LEATHER_BOOTS));
				entity.equipStack(EquipmentSlot.CHEST, new ItemStack(Items.LEATHER_CHESTPLATE));
				entity.equipStack(EquipmentSlot.LEGS, new ItemStack(Items.LEATHER_LEGGINGS));
				entity.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.LEATHER_HELMET));
				((AccessorMobEntity)entity).setArmorDropChances(armor);
				((AccessorMobEntity)entity).setHandDropChances(hand);
				entities.add(new EntityOptions("Skeleton (Leather Armor, Enchanted Bow)", entity));
				
				entity = new ZombieEntity(world);
				entity.setStackInHand(Hand.MAIN_HAND, addEnchants(new ItemStack(Items.IRON_SWORD), zombieSword));
				entity.equipStack(EquipmentSlot.FEET, new ItemStack(Items.LEATHER_BOOTS));
				entity.equipStack(EquipmentSlot.CHEST, new ItemStack(Items.LEATHER_CHESTPLATE));
				entity.equipStack(EquipmentSlot.LEGS, new ItemStack(Items.LEATHER_LEGGINGS));
				entity.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.LEATHER_HELMET));
				((AccessorMobEntity)entity).setArmorDropChances(armor);
				((AccessorMobEntity)entity).setHandDropChances(hand);
				entities.add(new EntityOptions("Zombie (Leather Armor, Enchanted Sword)", entity));
				
				entity = new SkeletonEntity(EntityType.SKELETON, world);
				entity.setStackInHand(Hand.MAIN_HAND, addEnchants(new ItemStack(Items.BOW), skelBow));
				entity.equipStack(EquipmentSlot.FEET, new ItemStack(Items.GOLDEN_BOOTS));
				entity.equipStack(EquipmentSlot.CHEST, new ItemStack(Items.GOLDEN_CHESTPLATE));
				entity.equipStack(EquipmentSlot.LEGS, new ItemStack(Items.GOLDEN_LEGGINGS));
				entity.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.GOLDEN_HELMET));
				((AccessorMobEntity)entity).setArmorDropChances(armor);
				((AccessorMobEntity)entity).setHandDropChances(hand);
				entities.add(new EntityOptions("Skeleton (Gold Armor, Enchanted Bow)", entity));
				
				entity = new ZombieEntity(world);
				entity.setStackInHand(Hand.MAIN_HAND, addEnchants(new ItemStack(Items.IRON_SWORD), zombieSword));
				entity.equipStack(EquipmentSlot.FEET, new ItemStack(Items.GOLDEN_BOOTS));
				entity.equipStack(EquipmentSlot.CHEST, new ItemStack(Items.GOLDEN_CHESTPLATE));
				entity.equipStack(EquipmentSlot.LEGS, new ItemStack(Items.GOLDEN_LEGGINGS));
				entity.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.GOLDEN_HELMET));
				((AccessorMobEntity)entity).setArmorDropChances(armor);
				((AccessorMobEntity)entity).setHandDropChances(hand);
				entities.add(new EntityOptions("Zombie (Gold Armor, Enchanted Sword)", entity));
				
				entity = new SkeletonEntity(EntityType.SKELETON, world);
				entity.setStackInHand(Hand.MAIN_HAND, addEnchants(new ItemStack(Items.BOW), skelBow));
				entity.equipStack(EquipmentSlot.FEET, new ItemStack(Items.CHAINMAIL_BOOTS));
				entity.equipStack(EquipmentSlot.CHEST, new ItemStack(Items.CHAINMAIL_CHESTPLATE));
				entity.equipStack(EquipmentSlot.LEGS, new ItemStack(Items.CHAINMAIL_LEGGINGS));
				entity.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.CHAINMAIL_HELMET));
				((AccessorMobEntity)entity).setArmorDropChances(armor);
				((AccessorMobEntity)entity).setHandDropChances(hand);
				entities.add(new EntityOptions("Skeleton (Chain Armor, Enchanted Bow)", entity));
				
				entity = new ZombieEntity(world);
				entity.setStackInHand(Hand.MAIN_HAND, addEnchants(new ItemStack(Items.IRON_SWORD), zombieSword));
				entity.equipStack(EquipmentSlot.FEET, new ItemStack(Items.CHAINMAIL_BOOTS));
				entity.equipStack(EquipmentSlot.CHEST, new ItemStack(Items.CHAINMAIL_CHESTPLATE));
				entity.equipStack(EquipmentSlot.LEGS, new ItemStack(Items.CHAINMAIL_LEGGINGS));
				entity.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.CHAINMAIL_HELMET));
				((AccessorMobEntity)entity).setArmorDropChances(armor);
				((AccessorMobEntity)entity).setHandDropChances(hand);
				entities.add(new EntityOptions("Zombie (Chain Armor, Enchanted Sword)", entity));
				
				entity = new SkeletonEntity(EntityType.SKELETON, world);
				entity.setStackInHand(Hand.MAIN_HAND, addEnchants(new ItemStack(Items.BOW), skelBow));
				entity.equipStack(EquipmentSlot.FEET, new ItemStack(Items.IRON_BOOTS));
				entity.equipStack(EquipmentSlot.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
				entity.equipStack(EquipmentSlot.LEGS, new ItemStack(Items.IRON_LEGGINGS));
				entity.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
				((AccessorMobEntity)entity).setArmorDropChances(armor);
				((AccessorMobEntity)entity).setHandDropChances(hand);
				entities.add(new EntityOptions("Skeleton (Iron Armor, Enchanted Sword)", entity));
				
				entity = new ZombieEntity(world);
				entity.setStackInHand(Hand.MAIN_HAND, addEnchants(new ItemStack(Items.IRON_SWORD), zombieSword));
				entity.equipStack(EquipmentSlot.FEET, new ItemStack(Items.IRON_BOOTS));
				entity.equipStack(EquipmentSlot.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
				entity.equipStack(EquipmentSlot.LEGS, new ItemStack(Items.IRON_LEGGINGS));
				entity.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
				((AccessorMobEntity)entity).setArmorDropChances(armor);
				((AccessorMobEntity)entity).setHandDropChances(hand);
				entities.add(new EntityOptions("Zombie (Iron Armor, Enchanted Sword)", entity));
				
				entity = new SkeletonEntity(EntityType.SKELETON, world);
				entity.setStackInHand(Hand.MAIN_HAND, addEnchants(new ItemStack(Items.BOW), skelBow));
				entity.setStackInHand(Hand.MAIN_HAND, new ItemStack(Items.BOW));
				entity.equipStack(EquipmentSlot.FEET, new ItemStack(Items.DIAMOND_BOOTS));
				entity.equipStack(EquipmentSlot.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE));
				entity.equipStack(EquipmentSlot.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS));
				entity.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.DIAMOND_HELMET));
				((AccessorMobEntity)entity).setArmorDropChances(armor);
				((AccessorMobEntity)entity).setHandDropChances(hand);
				entities.add(new EntityOptions("Skeleton (Diamond Armor, Enchanted Bow)", entity));
				
				entity = new ZombieEntity(world);
				entity.setStackInHand(Hand.MAIN_HAND, addEnchants(new ItemStack(Items.IRON_SWORD), zombieSword));
				entity.equipStack(EquipmentSlot.FEET, new ItemStack(Items.DIAMOND_BOOTS));
				entity.equipStack(EquipmentSlot.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE));
				entity.equipStack(EquipmentSlot.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS));
				entity.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.DIAMOND_HELMET));
				((AccessorMobEntity)entity).setArmorDropChances(armor);
				((AccessorMobEntity)entity).setHandDropChances(hand);
				entities.add(new EntityOptions("Zombie (Diamond Armor, Enchanted Sword)", entity));
			}
		}else if(dimension==DimensionType.THE_NETHER) {
			entities.add(new EntityOptions("Blaze", new BlazeEntity(EntityType.BLAZE, world)));
			entities.add(new EntityOptions("Ghast", new GhastEntity(EntityType.GHAST, world)));
			entities.add(new EntityOptions("Magma Cube", new GhastEntity(EntityType.GHAST, world)));
			LivingEntity entity = new SkeletonEntity(EntityType.SKELETON, world);
			entity.setStackInHand(Hand.MAIN_HAND, new ItemStack(Items.BOW));
			entities.add(new EntityOptions("Skeleton", entity));
			entities.add(new EntityOptions("Witherskeleton", new WitherSkeletonEntity(EntityType.WITHER_SKELETON, world)));
			
			if (MinecraftClient.getInstance().world.getDifficulty() == Difficulty.HARD) {
				entity = new SkeletonEntity(EntityType.SKELETON, world);
				entity.setStackInHand(Hand.MAIN_HAND, addEnchants(new ItemStack(Items.BOW), skelBow));
				((AccessorMobEntity)entity).setArmorDropChances(armor);
				((AccessorMobEntity)entity).setHandDropChances(hand);
				entities.add(new EntityOptions("Skeleton (Enchanted Bow)", entity));
				
				entity = new SkeletonEntity(EntityType.SKELETON, world);
				entity.setStackInHand(Hand.MAIN_HAND, addEnchants(new ItemStack(Items.BOW), skelBow));
				entity.equipStack(EquipmentSlot.FEET, new ItemStack(Items.LEATHER_BOOTS));
				entity.equipStack(EquipmentSlot.CHEST, new ItemStack(Items.LEATHER_CHESTPLATE));
				entity.equipStack(EquipmentSlot.LEGS, new ItemStack(Items.LEATHER_LEGGINGS));
				entity.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.LEATHER_HELMET));
				((AccessorMobEntity)entity).setArmorDropChances(armor);
				((AccessorMobEntity)entity).setHandDropChances(hand);
				entities.add(new EntityOptions("Skeleton (Leather Armor, Enchanted Bow)", entity));
				
				entity = new SkeletonEntity(EntityType.SKELETON, world);
				entity.setStackInHand(Hand.MAIN_HAND, addEnchants(new ItemStack(Items.BOW), skelBow));
				entity.equipStack(EquipmentSlot.FEET, new ItemStack(Items.GOLDEN_BOOTS));
				entity.equipStack(EquipmentSlot.CHEST, new ItemStack(Items.GOLDEN_CHESTPLATE));
				entity.equipStack(EquipmentSlot.LEGS, new ItemStack(Items.GOLDEN_LEGGINGS));
				entity.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.GOLDEN_HELMET));
				((AccessorMobEntity)entity).setArmorDropChances(armor);
				((AccessorMobEntity)entity).setHandDropChances(hand);
				entities.add(new EntityOptions("Skeleton (Gold Armor, Enchanted Bow)", entity));
				
				entity = new SkeletonEntity(EntityType.SKELETON, world);
				entity.setStackInHand(Hand.MAIN_HAND, addEnchants(new ItemStack(Items.BOW), skelBow));
				entity.equipStack(EquipmentSlot.FEET, new ItemStack(Items.CHAINMAIL_BOOTS));
				entity.equipStack(EquipmentSlot.CHEST, new ItemStack(Items.CHAINMAIL_CHESTPLATE));
				entity.equipStack(EquipmentSlot.LEGS, new ItemStack(Items.CHAINMAIL_LEGGINGS));
				entity.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.CHAINMAIL_HELMET));
				((AccessorMobEntity)entity).setArmorDropChances(armor);
				((AccessorMobEntity)entity).setHandDropChances(hand);
				entities.add(new EntityOptions("Skeleton (Chain Armor, Enchanted Bow)", entity));
				
				entity = new SkeletonEntity(EntityType.SKELETON, world);
				entity.setStackInHand(Hand.MAIN_HAND, addEnchants(new ItemStack(Items.BOW), skelBow));
				entity.equipStack(EquipmentSlot.FEET, new ItemStack(Items.IRON_BOOTS));
				entity.equipStack(EquipmentSlot.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
				entity.equipStack(EquipmentSlot.LEGS, new ItemStack(Items.IRON_LEGGINGS));
				entity.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
				((AccessorMobEntity)entity).setArmorDropChances(armor);
				((AccessorMobEntity)entity).setHandDropChances(hand);
				entities.add(new EntityOptions("Skeleton (Iron Armor, Enchanted Sword)", entity));
				
				entity = new SkeletonEntity(EntityType.SKELETON, world);
				entity.setStackInHand(Hand.MAIN_HAND, addEnchants(new ItemStack(Items.BOW), skelBow));
				entity.setStackInHand(Hand.MAIN_HAND, new ItemStack(Items.BOW));
				entity.equipStack(EquipmentSlot.FEET, new ItemStack(Items.DIAMOND_BOOTS));
				entity.equipStack(EquipmentSlot.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE));
				entity.equipStack(EquipmentSlot.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS));
				entity.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.DIAMOND_HELMET));
				((AccessorMobEntity)entity).setArmorDropChances(armor);
				((AccessorMobEntity)entity).setHandDropChances(hand);
				entities.add(new EntityOptions("Skeleton (Diamond Armor, Enchanted Bow)", entity));
			}
		}else if(dimension==DimensionType.THE_END) {
			entities.add(new EntityOptions("Enderman", new EndermanEntity(EntityType.ENDERMAN, world)));
		}
		return entities;
	}
	
	public static boolean canSpawn() {
		entity.updatePosition(target.x, target.y, target.z);
		boolean flag=entity.getPos().distanceTo(playerPos)>24D && entity.getPos().distanceTo(playerPos)<128D;
		if(flag) {
			ServerWorld world=MinecraftClient.getInstance().getServer().getPlayerManager().getPlayerList().get(0).getServerWorld();
			
			return ((MobEntity) entity).canSpawn(world, net.minecraft.entity.SpawnType.NATURAL) && world.doesNotCollide(entity);
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
	
	//#if MC>=11601
//$$ 			public ItemStack addEnchants(ItemStack item,  HashMap<Enchantment, Integer> enchs) {
//$$ 			    enchs.entrySet().forEach(entry->{
//$$ 			        item.addEnchantment(entry.getKey(), entry.getValue());
//$$ 			    });;
//$$ 				return item;
//$$ 			}
		//#else
		public ItemStack addEnchants(ItemStack item, InfoEnchantment[] enchants) {
			for (InfoEnchantment enchantmentData : enchants) {
				item.addEnchantment(enchantmentData.enchantment, enchantmentData.level);
			}
			return item;
		}
		//#endif
}
