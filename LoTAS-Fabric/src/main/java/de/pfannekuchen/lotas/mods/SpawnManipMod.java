package de.pfannekuchen.lotas.mods;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.mixin.accessors.AccessorDimensionTypes;
import de.pfannekuchen.lotas.mixin.accessors.AccessorMobEntity;
import de.pfannekuchen.lotas.mods.SpawnManipMod.EntityOptions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
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
import net.minecraft.world.entity.monster.MagmaCube;
import net.minecraft.world.entity.monster.Phantom;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.phys.Vec3;

/**
 * Allows for spawning entities at valid locations
 * @author Scribble
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
			Vec3 target=getTargetPos();
			entity.setPos(target.x+0.5, target.y, target.z+0.5);
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
		double targetX=Math.floor(target.x);
		double targetY=Math.floor(target.y);
		double targetZ=Math.floor(target.z);
		
		return new Vec3(targetX, targetY, targetZ);
	}
	
	public List<EntityOptions> getManipList(){
		List<EntityOptions> entities=new ArrayList<EntityOptions>();
		//#if MC>=11601
//$$ 		net.minecraft.resources.ResourceKey<Level> dimension = MCVer.getCurrentLevel().dimension();
		//#else
		DimensionType dimension = MCVer.getCurrentLevel().getDimension().getType();
		//#endif
		//#if MC>=12000
//$$ 		ServerLevel world = mc.getSingleplayerServer().getPlayerList().getPlayers().get(0).serverLevel();
		//#else
		ServerLevel world = mc.getSingleplayerServer().getPlayerList().getPlayers().get(0).getLevel();
		//#endif
		Minecraft mc = Minecraft.getInstance();
		float[] armor={1f,1f,1f,1f};
		float[] hand= {1f,1f};
		
		//#if MC>=11601
//$$ 		if(dimension == Level.OVERWORLD) {
		//#else
		if(dimension == AccessorDimensionTypes.getOverworld()) {
		//#endif
			entities.add(new EntityOptions(I18n.get("entity.minecraft.cave_spider"), new CaveSpider(EntityType.CAVE_SPIDER, world)));
			entities.add(new EntityOptions(I18n.get("entity.minecraft.creeper"), new Creeper(EntityType.CREEPER, world)));
			entities.add(new EntityOptions(I18n.get("entity.minecraft.enderman"), new EnderMan(EntityType.ENDERMAN, world)));
			entities.add(new EntityOptions(I18n.get("entity.minecraft.husk"), new Husk(EntityType.HUSK, world)));
			entities.add(new EntityOptions(I18n.get("entity.minecraft.iron_golem"), new IronGolem(EntityType.IRON_GOLEM, world)));
			LivingEntity entity = new Skeleton(EntityType.SKELETON, world);
			entity.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.BOW));
			entities.add(new EntityOptions(I18n.get("entity.minecraft.skeleton"), entity));
			entities.add(new EntityOptions(I18n.get("entity.minecraft.slime"), new Slime(EntityType.SLIME, world)));
			entities.add(new EntityOptions(I18n.get("entity.minecraft.spider"), new Spider(EntityType.SPIDER, world)));
			entities.add(new EntityOptions(I18n.get("entity.minecraft.witch"), new Witch(EntityType.WITCH, world)));
			entities.add(new EntityOptions(I18n.get("entity.minecraft.zombie"), new Zombie(world)));
			entities.add(new EntityOptions(I18n.get("entity.minecraft.zombie_villager"), new ZombieVillager(EntityType.ZOMBIE_VILLAGER, world)));
			
			if (mc.level.getDifficulty() == Difficulty.HARD) {
				entity = new Skeleton(EntityType.SKELETON, world);
				entity.setItemInHand(InteractionHand.MAIN_HAND, addEnchants(new ItemStack(Items.BOW), skelBow));
				((AccessorMobEntity)entity).setArmorDropChances(armor);
				((AccessorMobEntity)entity).setHandDropChances(hand);
				entities.add(new EntityOptions(String.format("%s (%s)", I18n.get("entity.minecraft.skeleton"), I18n.get("spawnmanip.lotas.ench_bow")), entity));
				
				entity = new Zombie(world);
				entity.setItemInHand(InteractionHand.MAIN_HAND, addEnchants(new ItemStack(Items.IRON_SWORD), zombieSword));
				((AccessorMobEntity)entity).setArmorDropChances(armor);
				((AccessorMobEntity)entity).setHandDropChances(hand);
				entities.add(new EntityOptions(String.format("%s (%s)", I18n.get("entity.minecraft.zombie"), I18n.get("spawnmanip.lotas.ench_sword")), entity));

				entity = new Skeleton(EntityType.SKELETON, world);
				entity.setItemInHand(InteractionHand.MAIN_HAND, addEnchants(new ItemStack(Items.BOW), skelBow));
				entity.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.LEATHER_BOOTS));
				entity.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.LEATHER_CHESTPLATE));
				entity.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.LEATHER_LEGGINGS));
				entity.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.LEATHER_HELMET));
				((AccessorMobEntity)entity).setArmorDropChances(armor);
				((AccessorMobEntity)entity).setHandDropChances(hand);
				entities.add(new EntityOptions(String.format("%s (%s, %s)",I18n.get("entity.minecraft.skeleton"), I18n.get("spawnmanip.lotas.leather_armor"), I18n.get("spawnmanip.lotas.ench_bow")), entity));
				
				entity = new Zombie(world);
				entity.setItemInHand(InteractionHand.MAIN_HAND, addEnchants(new ItemStack(Items.IRON_SWORD), zombieSword));
				entity.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.LEATHER_BOOTS));
				entity.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.LEATHER_CHESTPLATE));
				entity.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.LEATHER_LEGGINGS));
				entity.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.LEATHER_HELMET));
				((AccessorMobEntity)entity).setArmorDropChances(armor);
				((AccessorMobEntity)entity).setHandDropChances(hand);
				entities.add(new EntityOptions(String.format("%s (%s, %s)",I18n.get("entity.minecraft.zombie"), I18n.get("spawnmanip.lotas.leather_armor"), I18n.get("spawnmanip.lotas.ench_sword")), entity));
				
				entity = new Skeleton(EntityType.SKELETON, world);
				entity.setItemInHand(InteractionHand.MAIN_HAND, addEnchants(new ItemStack(Items.BOW), skelBow));
				entity.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.GOLDEN_BOOTS));
				entity.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.GOLDEN_CHESTPLATE));
				entity.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.GOLDEN_LEGGINGS));
				entity.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.GOLDEN_HELMET));
				((AccessorMobEntity)entity).setArmorDropChances(armor);
				((AccessorMobEntity)entity).setHandDropChances(hand);
				entities.add(new EntityOptions(String.format("%s (%s, %s)",I18n.get("entity.minecraft.skeleton"), I18n.get("spawnmanip.lotas.gold_armor"), I18n.get("spawnmanip.lotas.ench_bow")), entity));
				
				entity = new Zombie(world);
				entity.setItemInHand(InteractionHand.MAIN_HAND, addEnchants(new ItemStack(Items.IRON_SWORD), zombieSword));
				entity.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.GOLDEN_BOOTS));
				entity.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.GOLDEN_CHESTPLATE));
				entity.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.GOLDEN_LEGGINGS));
				entity.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.GOLDEN_HELMET));
				((AccessorMobEntity)entity).setArmorDropChances(armor);
				((AccessorMobEntity)entity).setHandDropChances(hand);
				entities.add(new EntityOptions(String.format("%s (%s, %s)",I18n.get("entity.minecraft.zombie"), I18n.get("spawnmanip.lotas.gold_armor"), I18n.get("spawnmanip.lotas.ench_sword")), entity));
				
				entity = new Skeleton(EntityType.SKELETON, world);
				entity.setItemInHand(InteractionHand.MAIN_HAND, addEnchants(new ItemStack(Items.BOW), skelBow));
				entity.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.CHAINMAIL_BOOTS));
				entity.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.CHAINMAIL_CHESTPLATE));
				entity.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.CHAINMAIL_LEGGINGS));
				entity.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.CHAINMAIL_HELMET));
				((AccessorMobEntity)entity).setArmorDropChances(armor);
				((AccessorMobEntity)entity).setHandDropChances(hand);
				entities.add(new EntityOptions(String.format("%s (%s, %s)", I18n.get("entity.minecraft.skeleton"), I18n.get("spawnmanip.lotas.chain_armor"), I18n.get("spawnmanip.lotas.ench_bow")), entity));
				
				entity = new Zombie(world);
				entity.setItemInHand(InteractionHand.MAIN_HAND, addEnchants(new ItemStack(Items.IRON_SWORD), zombieSword));
				entity.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.CHAINMAIL_BOOTS));
				entity.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.CHAINMAIL_CHESTPLATE));
				entity.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.CHAINMAIL_LEGGINGS));
				entity.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.CHAINMAIL_HELMET));
				((AccessorMobEntity)entity).setArmorDropChances(armor);
				((AccessorMobEntity)entity).setHandDropChances(hand);
				entities.add(new EntityOptions(String.format("%s (%s, %s)", I18n.get("entity.minecraft.zombie"), I18n.get("spawnmanip.lotas.chain_armor"), I18n.get("spawnmanip.lotas.ench_sword")), entity));
				
				entity = new Skeleton(EntityType.SKELETON, world);
				entity.setItemInHand(InteractionHand.MAIN_HAND, addEnchants(new ItemStack(Items.BOW), skelBow));
				entity.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.IRON_BOOTS));
				entity.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
				entity.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.IRON_LEGGINGS));
				entity.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
				((AccessorMobEntity)entity).setArmorDropChances(armor);
				((AccessorMobEntity)entity).setHandDropChances(hand);
				entities.add(new EntityOptions(String.format("%s (%s, %s)", I18n.get("entity.minecraft.skeleton"), I18n.get("spawnmanip.lotas.iron_armor"), I18n.get("spawnmanip.lotas.ench_bow")), entity));
				
				entity = new Zombie(world);
				entity.setItemInHand(InteractionHand.MAIN_HAND, addEnchants(new ItemStack(Items.IRON_SWORD), zombieSword));
				entity.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.IRON_BOOTS));
				entity.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
				entity.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.IRON_LEGGINGS));
				entity.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
				((AccessorMobEntity)entity).setArmorDropChances(armor);
				((AccessorMobEntity)entity).setHandDropChances(hand);
				entities.add(new EntityOptions(String.format("%s (%s, %s)",I18n.get("entity.minecraft.zombie"), I18n.get("spawnmanip.lotas.iron_armor"), I18n.get("spawnmanip.lotas.ench_sword")), entity));
				
				entity = new Skeleton(EntityType.SKELETON, world);
				entity.setItemInHand(InteractionHand.MAIN_HAND, addEnchants(new ItemStack(Items.BOW), skelBow));
				entity.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.DIAMOND_BOOTS));
				entity.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE));
				entity.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS));
				entity.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.DIAMOND_HELMET));
				((AccessorMobEntity)entity).setArmorDropChances(armor);
				((AccessorMobEntity)entity).setHandDropChances(hand);
				entities.add(new EntityOptions(String.format("%s (%s, %s)", I18n.get("entity.minecraft.skeleton"), I18n.get("spawnmanip.lotas.diamond_armor"), I18n.get("spawnmanip.lotas.ench_bow")), entity));
				
				entity = new Zombie(world);
				entity.setItemInHand(InteractionHand.MAIN_HAND, addEnchants(new ItemStack(Items.IRON_SWORD), zombieSword));
				entity.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.DIAMOND_BOOTS));
				entity.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE));
				entity.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS));
				entity.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.DIAMOND_HELMET));
				((AccessorMobEntity)entity).setArmorDropChances(armor);
				((AccessorMobEntity)entity).setHandDropChances(hand);
				entities.add(new EntityOptions(String.format("%s (%s, %s)", I18n.get("entity.minecraft.zombie"), I18n.get("spawnmanip.lotas.diamond_armor"), I18n.get("spawnmanip.lotas.ench_sword")), entity));
			}
			
		//#if MC>=11601
//$$ 		}else if(dimension == Level.NETHER) {
		//#else
		}else if(dimension == AccessorDimensionTypes.getNether()) {
		//#endif
			entities.add(new EntityOptions(I18n.get("entity.minecraft.blaze"), new Blaze(EntityType.BLAZE, world)));
			entities.add(new EntityOptions(I18n.get("entity.minecraft.enderman"), new EnderMan(EntityType.ENDERMAN, world)));
			entities.add(new EntityOptions(I18n.get("entity.minecraft.ghast"), new Ghast(EntityType.GHAST, world)));
			entities.add(new EntityOptions(I18n.get("entity.minecraft.magma_cube"), new MagmaCube(EntityType.MAGMA_CUBE, world)));
			//#if MC>=11600
//$$ 			LivingEntity piglin=new net.minecraft.world.entity.monster.piglin.Piglin(EntityType.PIGLIN, world);
//$$ 			piglin.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.GOLDEN_SWORD));
//$$ 			((AccessorMobEntity)piglin).setHandDropChances(hand);
//$$ 			entities.add(new EntityOptions(String.format("%s (%s)", I18n.get("entity.minecraft.piglin"), I18n.get("item.minecraft.golden_sword")), piglin));
//$$
//$$ 			piglin=new net.minecraft.world.entity.monster.piglin.Piglin(EntityType.PIGLIN, world);
//$$ 			piglin.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.GOLDEN_BOOTS));
//$$ 			piglin.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.GOLDEN_CHESTPLATE));
//$$ 			piglin.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.GOLDEN_LEGGINGS));
//$$ 			piglin.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.GOLDEN_HELMET));
//$$ 			piglin.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.GOLDEN_SWORD));
//$$ 			((AccessorMobEntity)piglin).setArmorDropChances(armor);
//$$ 			((AccessorMobEntity)piglin).setHandDropChances(hand);
//$$ 			entities.add(new EntityOptions(String.format("%s (%s, %s)", I18n.get("entity.minecraft.piglin"), I18n.get("item.minecraft.golden_sword"), I18n.get("spawnmanip.lotas.gold_armor")), piglin));
//$$
//$$ 			piglin=new net.minecraft.world.entity.monster.piglin.Piglin(EntityType.PIGLIN, world);
//$$ 			piglin.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.CROSSBOW));
//$$ 			((AccessorMobEntity)piglin).setHandDropChances(hand);
//$$ 			entities.add(new EntityOptions(String.format("%s (%s)", I18n.get("entity.minecraft.piglin"), I18n.get("item.minecraft.crossbow")), piglin));
//$$
//$$ 			piglin=new net.minecraft.world.entity.monster.piglin.Piglin(EntityType.PIGLIN, world);
//$$ 			piglin.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.GOLDEN_BOOTS));
//$$ 			piglin.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.GOLDEN_CHESTPLATE));
//$$ 			piglin.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.GOLDEN_LEGGINGS));
//$$ 			piglin.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.GOLDEN_HELMET));
//$$ 			((AccessorMobEntity)piglin).setArmorDropChances(armor);
//$$ 			((AccessorMobEntity)piglin).setHandDropChances(hand);
//$$ 			piglin.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.CROSSBOW));
//$$ 			entities.add(new EntityOptions(String.format("%s (%s, %s)", I18n.get("entity.minecraft.piglin"), I18n.get("item.minecraft.crossbow"), I18n.get("spawnmanip.lotas.gold_armor")), piglin));
			//#endif
			LivingEntity entity = new Skeleton(EntityType.SKELETON, world);
			entity.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.BOW));
			entities.add(new EntityOptions(I18n.get("entity.minecraft.skeleton"), entity));
			entity = new WitherSkeleton(EntityType.WITHER_SKELETON, world);
			entity.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.STONE_SWORD));
			entities.add(new EntityOptions(I18n.get("entity.minecraft.wither_skeleton"), entity));
			
			if (mc.level.getDifficulty() == Difficulty.HARD) {
				entity = new Skeleton(EntityType.SKELETON, world);
				entity.setItemInHand(InteractionHand.MAIN_HAND, addEnchants(new ItemStack(Items.BOW), skelBow));
				((AccessorMobEntity)entity).setArmorDropChances(armor);
				((AccessorMobEntity)entity).setHandDropChances(hand);
				entities.add(new EntityOptions(String.format("%s (%s)", I18n.get("entity.minecraft.skeleton"), I18n.get("spawnmanip.lotas.ench_bow")), entity));
				
				entity = new Skeleton(EntityType.SKELETON, world);
				entity.setItemInHand(InteractionHand.MAIN_HAND, addEnchants(new ItemStack(Items.BOW), skelBow));
				entity.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.LEATHER_BOOTS));
				entity.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.LEATHER_CHESTPLATE));
				entity.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.LEATHER_LEGGINGS));
				entity.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.LEATHER_HELMET));
				((AccessorMobEntity)entity).setArmorDropChances(armor);
				((AccessorMobEntity)entity).setHandDropChances(hand);
				entities.add(new EntityOptions(String.format("%s (%s, %s)", I18n.get("entity.minecraft.skeleton"), I18n.get("spawnmanip.lotas.leather_armor"), I18n.get("spawnmanip.lotas.ench_bow")), entity));
				
				entity = new Skeleton(EntityType.SKELETON, world);
				entity.setItemInHand(InteractionHand.MAIN_HAND, addEnchants(new ItemStack(Items.BOW), skelBow));
				entity.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.GOLDEN_BOOTS));
				entity.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.GOLDEN_CHESTPLATE));
				entity.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.GOLDEN_LEGGINGS));
				entity.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.GOLDEN_HELMET));
				((AccessorMobEntity)entity).setArmorDropChances(armor);
				((AccessorMobEntity)entity).setHandDropChances(hand);
				entities.add(new EntityOptions(String.format("%s (%s, %s)", I18n.get("entity.minecraft.skeleton"), I18n.get("spawnmanip.lotas.gold_armor"), I18n.get("spawnmanip.lotas.ench_bow")), entity));
				
				entity = new Skeleton(EntityType.SKELETON, world);
				entity.setItemInHand(InteractionHand.MAIN_HAND, addEnchants(new ItemStack(Items.BOW), skelBow));
				entity.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.CHAINMAIL_BOOTS));
				entity.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.CHAINMAIL_CHESTPLATE));
				entity.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.CHAINMAIL_LEGGINGS));
				entity.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.CHAINMAIL_HELMET));
				((AccessorMobEntity)entity).setArmorDropChances(armor);
				((AccessorMobEntity)entity).setHandDropChances(hand);
				entities.add(new EntityOptions(String.format("%s (%s, %s)", I18n.get("entity.minecraft.skeleton"), I18n.get("spawnmanip.lotas.chain_armor"), I18n.get("spawnmanip.lotas.ench_bow")), entity));
				
				entity = new Skeleton(EntityType.SKELETON, world);
				entity.setItemInHand(InteractionHand.MAIN_HAND, addEnchants(new ItemStack(Items.BOW), skelBow));
				entity.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.IRON_BOOTS));
				entity.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
				entity.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.IRON_LEGGINGS));
				entity.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
				((AccessorMobEntity)entity).setArmorDropChances(armor);
				((AccessorMobEntity)entity).setHandDropChances(hand);
				entities.add(new EntityOptions(String.format("%s (%s, %s)", I18n.get("entity.minecraft.skeleton"), I18n.get("spawnmanip.lotas.iron_armor"), I18n.get("spawnmanip.lotas.ench_sword")), entity));
				
				entity = new Skeleton(EntityType.SKELETON, world);
				entity.setItemInHand(InteractionHand.MAIN_HAND, addEnchants(new ItemStack(Items.BOW), skelBow));
				entity.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.DIAMOND_BOOTS));
				entity.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE));
				entity.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS));
				entity.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.DIAMOND_HELMET));
				((AccessorMobEntity)entity).setArmorDropChances(armor);
				((AccessorMobEntity)entity).setHandDropChances(hand);
				entities.add(new EntityOptions(String.format("%s (%s, %s)", I18n.get("entity.minecraft.skeleton"), I18n.get("spawnmanip.lotas.diamond_armor"), I18n.get("spawnmanip.lotas.ench_bow")), entity));
			}
			
		//#if MC>=11601
//$$ 		}else if(dimension == Level.END) {
		//#else
		} else if(dimension == AccessorDimensionTypes.getEnd()) {
		//#endif
			entities.add(new EntityOptions(I18n.get("entity.minecraft.enderman"), new EnderMan(EntityType.ENDERMAN, world)));
		} else {
			entities.add(new EntityOptions(I18n.get("entity.minecraft.blaze"), new Blaze(EntityType.BLAZE, world)));
			entities.add(new EntityOptions(I18n.get("entity.minecraft.cave_spider"), new CaveSpider(EntityType.CAVE_SPIDER, world)));
			entities.add(new EntityOptions(I18n.get("entity.minecraft.creeper"), new Creeper(EntityType.CREEPER, world)));
			entities.add(new EntityOptions(I18n.get("entity.minecraft.enderman"), new EnderMan(EntityType.ENDERMAN, world)));
			entities.add(new EntityOptions(I18n.get("entity.minecraft.ghast"), new Ghast(EntityType.GHAST, world)));
			entities.add(new EntityOptions(I18n.get("entity.minecraft.husk"), new Husk(EntityType.HUSK, world)));
			entities.add(new EntityOptions(I18n.get("entity.minecraft.iron_golem"), new IronGolem(EntityType.IRON_GOLEM, world)));
			entities.add(new EntityOptions(I18n.get("entity.minecraft.magma_cube"), new MagmaCube(EntityType.MAGMA_CUBE, world)));
			entities.add(new EntityOptions(I18n.get("entity.minecraft.phantom"), new Phantom(EntityType.PHANTOM, world)));
			LivingEntity entity = new Skeleton(EntityType.SKELETON, world);
			entity.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.BOW));
			entities.add(new EntityOptions(I18n.get("entity.minecraft.skeleton"), entity));
			entities.add(new EntityOptions(I18n.get("entity.minecraft.slime"), new Slime(EntityType.SLIME, world)));
			entities.add(new EntityOptions(I18n.get("entity.minecraft.spider"), new Spider(EntityType.SPIDER, world)));
			entities.add(new EntityOptions(I18n.get("entity.minecraft.witch"), new Witch(EntityType.WITCH, world)));
			entity = new WitherSkeleton(EntityType.WITHER_SKELETON, world);
			entity.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.STONE_SWORD));
			entities.add(new EntityOptions(I18n.get("entity.minecraft.wither_skeleton"), entity));
			entities.add(new EntityOptions(I18n.get("entity.minecraft.zombie"), new Zombie(world)));
			entities.add(new EntityOptions(I18n.get("entity.minecraft.zombie_villager"), new ZombieVillager(EntityType.ZOMBIE_VILLAGER, world)));
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
