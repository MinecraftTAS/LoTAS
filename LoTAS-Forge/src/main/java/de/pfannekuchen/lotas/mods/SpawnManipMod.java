package de.pfannekuchen.lotas.mods;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.mixin.accessors.AccessorEntityLiving;
import de.pfannekuchen.lotas.mods.AIManipMod.Vec;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.entity.EntityCreature;
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
//#if MC>10900
import net.minecraft.init.Enchantments;
//#else
//$$ import net.minecraft.enchantment.Enchantment;
//#endif
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
//#if MC>10900
import net.minecraft.util.math.BlockPos;
//#else
//$$ import net.minecraft.util.BlockPos;
//#endif
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.EnumSkyBlock;
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
			double targetX=Math.floor(target.x)+0.5;
			double targetY=Math.floor(target.y);
			double targetZ=Math.floor(target.z)+0.5;
			
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
			MCVer.setItemToSlot(entity, 0, new ItemStack(MCVer.getItem("bow")));
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
			//#if MC>=11000
//$$ 			net.minecraftforge.fml.common.registry.VillagerRegistry.setRandomProfession(zombentity, new Random());
			//#else
			//#if MC>=10900
//$$ 			zombentity.setVillagerType(new Random().nextInt(5));
			//#else
//$$ 			zombentity.setVillager(true);
			//#endif
			//#endif
//$$ 			entities.add(new EntityOptions("Zombievillager", zombentity));
			//#endif
			
			if (MCVer.world(mc).getDifficulty() == EnumDifficulty.HARD) {
				entity = new EntitySkeleton(world);
				MCVer.setItemToSlot(entity, 0, addEnchants(new ItemStack(MCVer.getItem("bow")), skelBow));
				MCVer.setArmorDropChances(entity, new float[] { 1.0f, 1.0f, 1.0f, 1.0f });
				MCVer.setHandDropChances(entity, new float[] { 1.0f, 1.0f });
				entities.add(new EntityOptions("Skeleton (Enchanted Bow)", entity));
				
				entity = new EntityZombie(world);
				MCVer.setItemToSlot(entity, 0, addEnchants(new ItemStack(MCVer.getItem("iron_sword")), zombieSword));
				MCVer.setArmorDropChances(entity, new float[] { 1.0f, 1.0f, 1.0f, 1.0f });
				MCVer.setHandDropChances(entity, new float[] { 1.0f, 1.0f });
				entities.add(new EntityOptions("Zombie (Enchanted Sword)", entity));

				entity = new EntitySkeleton(world);
				MCVer.setItemToSlot(entity, 0, addEnchants(new ItemStack(MCVer.getItem("bow")), skelBow));
				MCVer.setItemToSlot(entity, 1, new ItemStack(MCVer.getItem("leather_boots")));
				MCVer.setItemToSlot(entity, 2, new ItemStack(MCVer.getItem("leather_chestplate")));
				MCVer.setItemToSlot(entity, 3, new ItemStack(MCVer.getItem("leather_leggings")));
				MCVer.setItemToSlot(entity, 4, new ItemStack(MCVer.getItem("leather_helmet")));
				MCVer.setArmorDropChances(entity, new float[] { 1.0f, 1.0f, 1.0f, 1.0f });
				MCVer.setHandDropChances(entity, new float[] { 1.0f, 1.0f });
				entities.add(new EntityOptions("Skeleton (Leather Armor, Enchanted Bow)", entity));
				
				entity = new EntityZombie(world);
				MCVer.setItemToSlot(entity, 0, addEnchants(new ItemStack(MCVer.getItem("iron_sword")), zombieSword));
				MCVer.setItemToSlot(entity, 1, new ItemStack(MCVer.getItem("leather_boots")));     
				MCVer.setItemToSlot(entity, 2, new ItemStack(MCVer.getItem("leather_chestplate")));
				MCVer.setItemToSlot(entity, 3, new ItemStack(MCVer.getItem("leather_leggings")));   
				MCVer.setItemToSlot(entity, 4, new ItemStack(MCVer.getItem("leather_helmet")));    
				MCVer.setArmorDropChances(entity, new float[] { 1.0f, 1.0f, 1.0f, 1.0f });
				MCVer.setHandDropChances(entity, new float[] { 1.0f, 1.0f });
				entities.add(new EntityOptions("Zombie (Leather Armor, Enchanted Sword)", entity));
				
				entity = new EntitySkeleton(world);
				MCVer.setItemToSlot(entity, 0, addEnchants(new ItemStack(MCVer.getItem("bow")), skelBow));
				MCVer.setItemToSlot(entity, 1, new ItemStack(MCVer.getItem("golden_boots")));     
				MCVer.setItemToSlot(entity, 2, new ItemStack(MCVer.getItem("golden_chestplate")));
				MCVer.setItemToSlot(entity, 3, new ItemStack(MCVer.getItem("golden_leggings")));   
				MCVer.setItemToSlot(entity, 4, new ItemStack(MCVer.getItem("golden_helmet")));    
				MCVer.setArmorDropChances(entity, new float[] { 1.0f, 1.0f, 1.0f, 1.0f });
				MCVer.setHandDropChances(entity, new float[] { 1.0f, 1.0f });
				entities.add(new EntityOptions("Skeleton (Gold Armor, Enchanted Bow)", entity));
				
				entity = new EntityZombie(world);
				MCVer.setItemToSlot(entity, 0, addEnchants(new ItemStack(MCVer.getItem("iron_sword")), zombieSword));
				MCVer.setItemToSlot(entity, 1, new ItemStack(MCVer.getItem("golden_boots")));     
				MCVer.setItemToSlot(entity, 2, new ItemStack(MCVer.getItem("golden_chestplate")));
				MCVer.setItemToSlot(entity, 3, new ItemStack(MCVer.getItem("golden_leggings")));   
				MCVer.setItemToSlot(entity, 4, new ItemStack(MCVer.getItem("golden_helmet")));    
				MCVer.setArmorDropChances(entity, new float[] { 1.0f, 1.0f, 1.0f, 1.0f });
				MCVer.setHandDropChances(entity, new float[] { 1.0f, 1.0f });
				entities.add(new EntityOptions("Zombie (Gold Armor, Enchanted Sword)", entity));
				
				entity = new EntitySkeleton(world);
				MCVer.setItemToSlot(entity, 0, addEnchants(new ItemStack(MCVer.getItem("bow")), skelBow));
				MCVer.setItemToSlot(entity, 1, new ItemStack(MCVer.getItem("chainmail_boots")));     
				MCVer.setItemToSlot(entity, 2, new ItemStack(MCVer.getItem("chainmail_chestplate")));
				MCVer.setItemToSlot(entity, 3, new ItemStack(MCVer.getItem("chainmail_leggings")));   
				MCVer.setItemToSlot(entity, 4, new ItemStack(MCVer.getItem("chainmail_helmet")));    
				MCVer.setArmorDropChances(entity, new float[] { 1.0f, 1.0f, 1.0f, 1.0f });
				MCVer.setHandDropChances(entity, new float[] { 1.0f, 1.0f });
				entities.add(new EntityOptions("Skeleton (Chain Armor, Enchanted Bow)", entity));
				
				entity = new EntityZombie(world);
				MCVer.setItemToSlot(entity, 0, addEnchants(new ItemStack(MCVer.getItem("iron_sword")), zombieSword));
				MCVer.setItemToSlot(entity, 1, new ItemStack(MCVer.getItem("chainmail_boots")));     
				MCVer.setItemToSlot(entity, 2, new ItemStack(MCVer.getItem("chainmail_chestplate")));
				MCVer.setItemToSlot(entity, 3, new ItemStack(MCVer.getItem("chainmail_leggings")));   
				MCVer.setItemToSlot(entity, 4, new ItemStack(MCVer.getItem("chainmail_helmet")));    
				MCVer.setArmorDropChances(entity, new float[] { 1.0f, 1.0f, 1.0f, 1.0f });
				MCVer.setHandDropChances(entity, new float[] { 1.0f, 1.0f });
				entities.add(new EntityOptions("Zombie (Chain Armor, Enchanted Sword)", entity));
				
				entity = new EntitySkeleton(world);
				MCVer.setItemToSlot(entity, 0, addEnchants(new ItemStack(MCVer.getItem("bow")), skelBow));
				MCVer.setItemToSlot(entity, 1, new ItemStack(MCVer.getItem("iron_boots")));      
				MCVer.setItemToSlot(entity, 2, new ItemStack(MCVer.getItem("iron_chestplate"))); 
				MCVer.setItemToSlot(entity, 3, new ItemStack(MCVer.getItem("iron_leggings")));    
				MCVer.setItemToSlot(entity, 4, new ItemStack(MCVer.getItem("iron_helmet")));     
				MCVer.setArmorDropChances(entity, new float[] { 1.0f, 1.0f, 1.0f, 1.0f });
				MCVer.setHandDropChances(entity, new float[] { 1.0f, 1.0f });
				entities.add(new EntityOptions("Skeleton (Iron Armor, Enchanted Sword)", entity));
				
				entity = new EntityZombie(world);
				MCVer.setItemToSlot(entity, 0, addEnchants(new ItemStack(MCVer.getItem("iron_sword")), zombieSword));
				MCVer.setItemToSlot(entity, 1, new ItemStack(MCVer.getItem("iron_boots")));     
				MCVer.setItemToSlot(entity, 2, new ItemStack(MCVer.getItem("iron_chestplate")));
				MCVer.setItemToSlot(entity, 3, new ItemStack(MCVer.getItem("iron_leggings")));   
				MCVer.setItemToSlot(entity, 4, new ItemStack(MCVer.getItem("iron_helmet")));    
				MCVer.setArmorDropChances(entity, new float[] { 1.0f, 1.0f, 1.0f, 1.0f });
				MCVer.setHandDropChances(entity, new float[] { 1.0f, 1.0f });
				entities.add(new EntityOptions("Zombie (Iron Armor, Enchanted Sword)", entity));
				
				entity = new EntitySkeleton(world);
				MCVer.setItemToSlot(entity, 0, addEnchants(new ItemStack(MCVer.getItem("bow")), skelBow));
				MCVer.setItemToSlot(entity, 1, new ItemStack(MCVer.getItem("diamond_boots")));     
				MCVer.setItemToSlot(entity, 2, new ItemStack(MCVer.getItem("diamond_chestplate")));
				MCVer.setItemToSlot(entity, 3, new ItemStack(MCVer.getItem("diamond_leggings")));   
				MCVer.setItemToSlot(entity, 4, new ItemStack(MCVer.getItem("diamond_helmet")));    
				MCVer.setArmorDropChances(entity, new float[] { 1.0f, 1.0f, 1.0f, 1.0f });
				MCVer.setHandDropChances(entity, new float[] { 1.0f, 1.0f });
				entities.add(new EntityOptions("Skeleton (Diamond Armor, Enchanted Bow)", entity));
				
				entity = new EntityZombie(world);
				MCVer.setItemToSlot(entity, 0, addEnchants(new ItemStack(MCVer.getItem("iron_sword")), zombieSword));
				MCVer.setItemToSlot(entity, 1, new ItemStack(MCVer.getItem("diamond_boots")));     
				MCVer.setItemToSlot(entity, 2, new ItemStack(MCVer.getItem("diamond_chestplate")));
				MCVer.setItemToSlot(entity, 3, new ItemStack(MCVer.getItem("diamond_leggings")));   
				MCVer.setItemToSlot(entity, 4, new ItemStack(MCVer.getItem("diamond_helmet")));    
				MCVer.setArmorDropChances(entity, new float[] { 1.0f, 1.0f, 1.0f, 1.0f });
				MCVer.setHandDropChances(entity, new float[] { 1.0f, 1.0f });
				entities.add(new EntityOptions("Zombie (Diamond Armor, Enchanted Sword)", entity));
			}
		}else if(dim==-1) {
			entities.add(new EntityOptions("Blaze", new EntityBlaze(world)));
			entities.add(new EntityOptions("Ghast", new EntityGhast(world)));
			entities.add(new EntityOptions("Magma Cube", new EntityMagmaCube(world)));
			EntityLiving entity = new EntitySkeleton(world);
			MCVer.setItemToSlot(entity, 0, new ItemStack(MCVer.getItem("bow")));
			entities.add(new EntityOptions("Skeleton", entity));
			//#if MC>=11100
			entity = new net.minecraft.entity.monster.EntityWitherSkeleton(world);
			//#else
//$$ 			entity = new EntitySkeleton(world);
//$$ 			EntitySkeleton skelEntity=(EntitySkeleton)entity;
			//#if MC>=11000
//$$ 			skelEntity.setSkeletonType(net.minecraft.entity.monster.SkeletonType.WITHER);
			//#else
//$$ 			skelEntity.setSkeletonType(1);
			//#endif
			//#endif
			MCVer.setItemToSlot(entity, 0, new ItemStack(MCVer.getItem("stone_sword")));
			entities.add(new EntityOptions("Wither Skeleton", entity));
			
			
			if (MCVer.world(mc).getDifficulty() == EnumDifficulty.HARD) {
				entity = new EntitySkeleton(world);
				MCVer.setItemToSlot(entity, 0, addEnchants(new ItemStack(MCVer.getItem("bow")), skelBow));
				MCVer.setArmorDropChances(entity, new float[] { 1.0f, 1.0f, 1.0f, 1.0f });
				MCVer.setHandDropChances(entity, new float[] { 1.0f, 1.0f });
				entities.add(new EntityOptions("Skeleton (Enchanted Bow)", entity));
				
				entity = new EntitySkeleton(world);
				MCVer.setItemToSlot(entity, 0, addEnchants(new ItemStack(MCVer.getItem("bow")), skelBow));
				MCVer.setItemToSlot(entity, 1, new ItemStack(MCVer.getItem("leather_boots")));     
				MCVer.setItemToSlot(entity, 2, new ItemStack(MCVer.getItem("leather_chestplate")));
				MCVer.setItemToSlot(entity, 3, new ItemStack(MCVer.getItem("leather_leggings")));   
				MCVer.setItemToSlot(entity, 4, new ItemStack(MCVer.getItem("leather_helmet")));    
				MCVer.setArmorDropChances(entity, new float[] { 1.0f, 1.0f, 1.0f, 1.0f });
				MCVer.setHandDropChances(entity, new float[] { 1.0f, 1.0f });
				entities.add(new EntityOptions("Skeleton (Leather Armor, Enchanted Bow)", entity));
				
				entity = new EntitySkeleton(world);
				MCVer.setItemToSlot(entity, 0, addEnchants(new ItemStack(MCVer.getItem("bow")), skelBow));
				MCVer.setItemToSlot(entity, 1, new ItemStack(MCVer.getItem("golden_boots")));     
				MCVer.setItemToSlot(entity, 2, new ItemStack(MCVer.getItem("golden_chestplate")));
				MCVer.setItemToSlot(entity, 3, new ItemStack(MCVer.getItem("golden_leggings")));   
				MCVer.setItemToSlot(entity, 4, new ItemStack(MCVer.getItem("golden_helmet")));    
				MCVer.setArmorDropChances(entity, new float[] { 1.0f, 1.0f, 1.0f, 1.0f });
				MCVer.setHandDropChances(entity, new float[] { 1.0f, 1.0f });
				entities.add(new EntityOptions("Skeleton (Gold Armor, Enchanted Bow)", entity));
				
				entity = new EntitySkeleton(world);
				MCVer.setItemToSlot(entity, 0, addEnchants(new ItemStack(MCVer.getItem("bow")), skelBow));
				MCVer.setItemToSlot(entity, 1, new ItemStack(MCVer.getItem("chainmail_boots")));     
				MCVer.setItemToSlot(entity, 2, new ItemStack(MCVer.getItem("chainmail_chestplate")));
				MCVer.setItemToSlot(entity, 3, new ItemStack(MCVer.getItem("chainmail_leggings")));   
				MCVer.setItemToSlot(entity, 4, new ItemStack(MCVer.getItem("chainmail_helmet")));    
				MCVer.setArmorDropChances(entity, new float[] { 1.0f, 1.0f, 1.0f, 1.0f });
				MCVer.setHandDropChances(entity, new float[] { 1.0f, 1.0f });
				entities.add(new EntityOptions("Skeleton (Chain Armor, Enchanted Bow)", entity));
				
				entity = new EntitySkeleton(world);
				MCVer.setItemToSlot(entity, 0, addEnchants(new ItemStack(MCVer.getItem("bow")), skelBow));
				MCVer.setItemToSlot(entity, 1, new ItemStack(MCVer.getItem("iron_boots")));     
				MCVer.setItemToSlot(entity, 2, new ItemStack(MCVer.getItem("iron_chestplate")));
				MCVer.setItemToSlot(entity, 3, new ItemStack(MCVer.getItem("iron_leggings")));   
				MCVer.setItemToSlot(entity, 4, new ItemStack(MCVer.getItem("iron_helmet")));    
				MCVer.setArmorDropChances(entity, new float[] { 1.0f, 1.0f, 1.0f, 1.0f });
				MCVer.setHandDropChances(entity, new float[] { 1.0f, 1.0f });
				entities.add(new EntityOptions("Skeleton (Iron Armor, Enchanted Sword)", entity));
				
				entity = new EntitySkeleton(world);
				MCVer.setItemToSlot(entity, 0, addEnchants(new ItemStack(MCVer.getItem("bow")), skelBow));
				MCVer.setItemToSlot(entity, 1, new ItemStack(MCVer.getItem("diamond_boots")));     
				MCVer.setItemToSlot(entity, 2, new ItemStack(MCVer.getItem("diamond_chestplate")));
				MCVer.setItemToSlot(entity, 3, new ItemStack(MCVer.getItem("diamond_leggings")));   
				MCVer.setItemToSlot(entity, 4, new ItemStack(MCVer.getItem("diamond_helmet")));    
				MCVer.setArmorDropChances(entity, new float[] { 1.0f, 1.0f, 1.0f, 1.0f });
				MCVer.setHandDropChances(entity, new float[] { 1.0f, 1.0f });
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
			//#if MC>=10900
			return	isValidLightLevel(entity) && !world.collidesWithAnyBlock(entity.getEntityBoundingBox());
			//#else
//$$ 			return	isValidLightLevel(entity) && !world.checkBlockCollision(entity.getEntityBoundingBox());
			//#endif
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
	
	private static boolean isValidLightLevel(EntityLiving entity) {
        BlockPos blockpos = new BlockPos(entity.posX, entity.getEntityBoundingBox().minY, entity.posZ);

        if (MCVer.world(entity).getLightFor(EnumSkyBlock.SKY, blockpos) > 31) {
            return false;
        } else {
            int i = MCVer.world(entity).getLightFromNeighbors(blockpos);

            if (MCVer.world(entity).isThundering()) {
                int j = MCVer.world(entity).getSkylightSubtracted();
                MCVer.world(entity).setSkylightSubtracted(10);
                i = MCVer.world(entity).getLightFromNeighbors(blockpos);
                MCVer.world(entity).setSkylightSubtracted(j);
            }

            return i <= 7;
        }
	}
}
