package de.pfannekuchen.lotas.manipulation;

import static rlog.RLogAPI.logError;

import java.io.IOException;
import java.util.Random;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.FloatControl.Type;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.lwjgl.opengl.GL11;

import de.pfannekuchen.lotas.LoTASModContainer;
import de.pfannekuchen.lotas.gui.GuiAIRig;
import de.pfannekuchen.lotas.gui.GuiEntitySpawner;
import de.pfannekuchen.lotas.gui.GuiLootManipulation;
import de.pfannekuchen.lotas.util.RenderUtils;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockPlanks.EnumType;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityElderGuardian;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntityShulker;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityWitherSkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtils;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import rlog.RLogAPI;

/**
*
* This Listener drops the best loot that is able to be dropped from entities and blocks.
*
*/
public class WorldManipulation {

	/**
	 * Changes entity drop chances
	 * @param e
	 */
	@SubscribeEvent
	public void onDrop(LivingDropsEvent e) {
		if (e.getEntity() instanceof EntityZombie && GuiLootManipulation.entityDrops.get("Zombie_Iron")) {
			e.getDrops().clear();
			e.getEntity().dropItem(Items.IRON_INGOT, 1);
		} else if (e.getEntity() instanceof EntityZombie && GuiLootManipulation.entityDrops.get("Zombie_Carrot")) {
			e.getDrops().clear();
			e.getEntity().dropItem(Items.CARROT, 1);
		} else if (e.getEntity() instanceof EntityZombie && GuiLootManipulation.entityDrops.get("Zombie_Potato")) {
			e.getDrops().clear();
			e.getEntity().dropItem(Items.POTATO, 1);
		} else if (e.getEntity() instanceof EntityWitherSkeleton && GuiLootManipulation.entityDrops.get("Witherskeleton_Skull")) {
			e.getDrops().clear();
			e.getEntity().entityDropItem(new ItemStack(Items.SKULL, 1, 1), 0.0F);
			e.getEntity().entityDropItem(new ItemStack(Items.COAL, 1), 0.0F);
			e.getEntity().entityDropItem(new ItemStack(Items.BONE, 2), 0.0F);
		} else if (e.getEntity() instanceof EntityWitch && GuiLootManipulation.entityDrops.get("Witch_General")) {
			e.getDrops().clear();
			e.getEntity().dropItem(Items.GLASS_BOTTLE, 3);
			e.getEntity().dropItem(Items.GLOWSTONE_DUST, 1);
			e.getEntity().dropItem(Items.GUNPOWDER, 1);
			e.getEntity().dropItem(Items.REDSTONE, 1);
			e.getEntity().dropItem(Items.SPIDER_EYE, 1);
			e.getEntity().dropItem(Items.STICK, 1);
			e.getEntity().dropItem(Items.SUGAR, 1);
		} else if (e.getEntity() instanceof EntityWitch && GuiLootManipulation.entityDrops.get("Witch_Healingpot")) {
			e.getDrops().clear();
			ItemStack stack = new ItemStack(Items.POTIONITEM);
			stack = PotionUtils.addPotionToItemStack(stack, PotionTypes.HEALING);
			if (((EntityWitch) e.getEntity()).isDrinkingPotion()) e.getEntity().entityDropItem(stack, 0);
		} else if (e.getEntity() instanceof EntityWitch && GuiLootManipulation.entityDrops.get("Witch_Firepot")) {
			e.getDrops().clear();
			ItemStack stack = new ItemStack(Items.POTIONITEM);
			stack = PotionUtils.addPotionToItemStack(stack, PotionTypes.FIRE_RESISTANCE);
			if (((EntityWitch) e.getEntity()).isDrinkingPotion()) e.getEntity().entityDropItem(stack, 0);
		} else if (e.getEntity() instanceof EntityWitch && GuiLootManipulation.entityDrops.get("Witch_Swiftness")) {
			e.getDrops().clear();
			ItemStack stack = new ItemStack(Items.POTIONITEM);
			stack = PotionUtils.addPotionToItemStack(stack, PotionTypes.SWIFTNESS);
			if (((EntityWitch) e.getEntity()).isDrinkingPotion()) e.getEntity().entityDropItem(stack, 0);
		} else if (e.getEntity() instanceof EntityWitch && GuiLootManipulation.entityDrops.get("Witch_Waterbreathing")) {
			e.getDrops().clear();
			ItemStack stack = new ItemStack(Items.POTIONITEM);
			stack = PotionUtils.addPotionToItemStack(stack, PotionTypes.WATER_BREATHING);
			if (((EntityWitch) e.getEntity()).isDrinkingPotion()) e.getEntity().entityDropItem(stack, 0);
		} else if (e.getEntity() instanceof EntitySlime && GuiLootManipulation.entityDrops.get("Slime_Slimeball")) {
			e.getDrops().clear();			
			ItemStack stack = new ItemStack(Items.SLIME_BALL, 2);
			if (((EntitySlime) e.getEntity()).getSlimeSize() == 1) e.getEntity().entityDropItem(stack, 0);
		} else if (e.getEntity() instanceof EntityBlaze && GuiLootManipulation.entityDrops.get("Blaze_Rod")) {
			e.getDrops().clear();
			ItemStack stack = new ItemStack(Items.BLAZE_ROD, 1);
			e.getEntity().entityDropItem(stack, 0);
		} else if (e.getEntity() instanceof EntityEnderman && GuiLootManipulation.entityDrops.get("Enderman_Enderpearl")) {
			e.getDrops().clear();
			ItemStack stack = new ItemStack(Items.ENDER_PEARL, 1);
			e.getEntity().entityDropItem(stack, 0);
		} else if (e.getEntity() instanceof EntityGhast && GuiLootManipulation.entityDrops.get("Ghast_Tear")) {
			e.getDrops().clear();
			ItemStack stack = new ItemStack(Items.GHAST_TEAR, 1);
			e.getEntity().entityDropItem(stack, 0);
		} else if (e.getEntity() instanceof EntitySkeleton && GuiLootManipulation.entityDrops.get("Skeleton_BowArrow")) {
			e.getDrops().clear();
			ItemStack stack2 = new ItemStack(Items.ARROW, 1);
			ItemStack stac = new ItemStack(Items.BOW, 2);
			e.getEntity().entityDropItem(stack2, 0);
			e.getEntity().entityDropItem(stac, 0);
		} else if (e.getEntity() instanceof EntityPigZombie && GuiLootManipulation.entityDrops.get("Zombiepigman_Loot")) {
			e.getDrops().clear();
			ItemStack stack = new ItemStack(Items.ROTTEN_FLESH, 1);
			ItemStack stack2 = new ItemStack(Items.GOLD_NUGGET, 1);
			ItemStack stack3 = new ItemStack(Items.GOLD_INGOT, 1);
			ItemStack stack4 = new ItemStack(Items.GOLDEN_SWORD, 1);
			e.getEntity().entityDropItem(stack, 0);
			e.getEntity().entityDropItem(stack2, 0);
			e.getEntity().entityDropItem(stack3, 0);
			e.getEntity().entityDropItem(stack4, 0);
		} else if ((e.getEntity() instanceof EntitySpider || e.getEntity() instanceof EntityCaveSpider) && GuiLootManipulation.entityDrops.get("Spider_Eye")) {
			e.getDrops().clear();
			ItemStack stack = new ItemStack(Items.STRING, 2);
			ItemStack stack2 = new ItemStack(Items.SPIDER_EYE, 1);
			e.getEntity().entityDropItem(stack, 0);
			e.getEntity().entityDropItem(stack2, 0);
		} else if (e.getEntity() instanceof EntityShulker && GuiLootManipulation.entityDrops.get("Shulker_Shell")) {
			e.getDrops().clear();
			ItemStack stack = new ItemStack(Items.SHULKER_SHELL, 1);
			e.getEntity().entityDropItem(stack, 0);
		} else if (e.getEntity() instanceof EntityCreeper && GuiLootManipulation.entityDrops.get("Creeper_Gunpowder")) {
			e.getDrops().clear();
			ItemStack stack = new ItemStack(Items.GUNPOWDER, 2);
			e.getEntity().entityDropItem(stack, 0);
		} else if (e.getEntity() instanceof EntityMagmaCube && GuiLootManipulation.entityDrops.get("Magma_Creme")) {
			e.getDrops().clear();
			if (((EntityMagmaCube) e.getEntity()).getSlimeSize() != 1) {
				ItemStack stack = new ItemStack(Items.MAGMA_CREAM);
				e.getEntity().entityDropItem(stack, 0);
			}
		} else if (e.getEntity() instanceof EntityElderGuardian && GuiLootManipulation.entityDrops.get("Elder_Guardian")) {
			e.getDrops().clear();
			ItemStack stack = new ItemStack(Items.PRISMARINE_SHARD, 2);
			ItemStack stack2 = new ItemStack(Items.PRISMARINE_CRYSTALS, 1);
			ItemStack stack3 = new ItemStack(Item.getItemById(19), 1, 1);
			e.getEntity().entityDropItem(stack, 0);
			e.getEntity().entityDropItem(stack2, 0);
			e.getEntity().entityDropItem(stack3, 0);
		} else if (e.getEntity() instanceof EntityGuardian && GuiLootManipulation.entityDrops.get("Guardian")) {
			e.getDrops().clear();
			ItemStack stack = new ItemStack(Items.PRISMARINE_SHARD, 2);
			e.getEntity().entityDropItem(stack, 0);
		} else if (e.getEntity() instanceof EntityChicken && GuiLootManipulation.entityDrops.get("Chicken")) {
			e.getDrops().clear();
			if (!((EntityChicken) e.getEntity()).isChild()) {
				e.getEntity().dropItem(new ItemStack(Items.FEATHER, 1).getItem(), 2);
				e.getEntity().dropItem(new ItemStack(((EntityChicken) e.getEntity()).isBurning() ? Items.COOKED_CHICKEN : Items.CHICKEN).getItem(), 3);
			}
		} else if (e.getEntity() instanceof EntityCow && GuiLootManipulation.entityDrops.get("Cow")) {
			e.getDrops().clear();
			if (!((EntityCow) e.getEntity()).isChild()) {
				e.getEntity().dropItem(new ItemStack(Items.LEATHER, 1).getItem(), 2);
				e.getEntity().dropItem(new ItemStack(((EntityCow) e.getEntity()).isBurning() ? Items.COOKED_BEEF : Items.BEEF, 1).getItem(), 3);
			}
		} else if (e.getEntity() instanceof EntityMooshroom && GuiLootManipulation.entityDrops.get("Mooshroom")) {
			e.getDrops().clear();
			if (!((EntityMooshroom) e.getEntity()).isChild()) {
				e.getEntity().dropItem(new ItemStack(Items.LEATHER, 1).getItem(), 2);
				e.getEntity().dropItem(new ItemStack(((EntityMooshroom) e.getEntity()).isBurning() ? Items.COOKED_BEEF : Items.BEEF, 1).getItem(), 3);
			}
		} else if (e.getEntity() instanceof EntityPig && GuiLootManipulation.entityDrops.get("Pig")) {
			e.getDrops().clear();
			if (!((EntityPig) e.getEntity()).isChild()) {
				e.getEntity().dropItem(new ItemStack(((EntityPig) e.getEntity()).isBurning() ? Items.COOKED_PORKCHOP : Items.PORKCHOP, 3).getItem(), 1);
			}
		} else if (e.getEntity() instanceof EntityRabbit && GuiLootManipulation.entityDrops.get("Rabbit")) {
			e.getDrops().clear();
			if (!((EntityRabbit) e.getEntity()).isChild()) {
				e.getEntity().dropItem(new ItemStack(Items.RABBIT_FOOT, 1).getItem(), 1);
				e.getEntity().dropItem(new ItemStack(Items.RABBIT_HIDE, 1).getItem(), 1);
				e.getEntity().dropItem(new ItemStack(((EntityRabbit) e.getEntity()).isBurning() ? Items.COOKED_RABBIT : Items.RABBIT, 3).getItem(), 1);
			}
		} else if (e.getEntity() instanceof EntitySheep && GuiLootManipulation.entityDrops.get("Sheep")) {
			e.getDrops().remove(1);
			if (!((EntitySheep) e.getEntity()).isChild()) {
				e.getEntity().dropItem(new ItemStack(((EntitySheep) e.getEntity()).isBurning() ? Items.COOKED_MUTTON : Items.MUTTON, 1).getItem(), 2);
			}
		} else if (e.getEntity() instanceof EntitySquid && GuiLootManipulation.entityDrops.get("Squid")) {
			e.getDrops().clear();
			e.getEntity().dropItem(new ItemStack(Items.DYE, 1, 0).getItem(), 3);
		} else if (e.getEntity() instanceof EntityIronGolem && GuiLootManipulation.entityDrops.get("Iron_Golem")) {
			e.getDrops().clear();
			e.getEntity().dropItem(Items.IRON_INGOT, 5);
		} 
		
		
	}
	
	@SubscribeEvent
	public void onSound(PlaySoundEvent e) {
		if (e.getSound().getSoundLocation().getResourcePath().contains("blaze") && e.getSound().getSoundLocation().getResourcePath().contains("death")) {
			if (new Random().nextBoolean()) {
				LoTASModContainer.playSound = true;
				RLogAPI.logDebug("[Sounds] Hijacking Blaze Sound");
				
				try {
					Clip clip = AudioSystem.getClip();
					clip.open(AudioSystem.getAudioInputStream(LoTASModContainer.class.getResourceAsStream("data.wav")));
					((FloatControl) clip.getControl(Type.MASTER_GAIN)).setValue(-10);
					clip.start();
					e.setResultSound(null);
				} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e1) {
					logError(e1, "Sound couldn't be played #5");
					e1.printStackTrace();
					FMLCommonHandler.instance().exitJava(1, true);
				}
				
			}
		}
	}
	
	/**
	 * Draws a visual help where a new entity is spawned in
	 * @param e
	 */
	@SubscribeEvent
	public void onRender(RenderWorldLastEvent e) {
		GuiScreen gui = Minecraft.getMinecraft().currentScreen;
		if (gui instanceof GuiEntitySpawner) {
			GL11.glPushMatrix();
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glEnable(GL11.GL_LINE_SMOOTH);
			GL11.glLineWidth(2);
			
			RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
			double renderX = ((double) ((GuiEntitySpawner) gui).spawnX - 0.5f) - renderManager.renderPosX;
			double renderY = ((double) ((GuiEntitySpawner) gui).spawnY) - renderManager.renderPosY;
			double renderZ = ((double) ((GuiEntitySpawner) gui).spawnZ - 0.5F) - renderManager.renderPosZ;
			
			GL11.glTranslated(renderX, renderY, renderZ);
			GL11.glScalef(1, 2, 1);
			GL11.glColor4f(28, 188, 220, 0.5f);
			RenderUtils.drawOutlinedBox();
			RenderUtils.drawCrossBox();
			GL11.glColor4f(0F, 1F, 0F, 0.15F);
			RenderUtils.drawSolidBox();
			
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_LINE_SMOOTH);
			GL11.glPopMatrix();
		} else if (gui instanceof GuiAIRig) {
			if (GuiAIRig.entities.size() == 0) return;
			GL11.glPushMatrix();
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glEnable(GL11.GL_LINE_SMOOTH);
			GL11.glLineWidth(2);
			
			RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
			double renderX = ((double) GuiAIRig.entities.get(GuiAIRig.selectedIndex).posX - 0.5f) - renderManager.renderPosX;
			double renderY = ((double) GuiAIRig.entities.get(GuiAIRig.selectedIndex).posY) - renderManager.renderPosY;
			double renderZ = ((double) GuiAIRig.entities.get(GuiAIRig.selectedIndex).posZ - 0.5F) - renderManager.renderPosZ;
			
			GL11.glTranslated(renderX, renderY, renderZ);
			GL11.glScalef(1, 2, 1);
			GL11.glColor4f(28, 188, 220, 0.5f);
			RenderUtils.drawOutlinedBox();
			RenderUtils.drawCrossBox();
			GL11.glColor4f(1F, 0F, 0F, 0.15F);
			RenderUtils.drawSolidBox();
			
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_LINE_SMOOTH);
			GL11.glPopMatrix();
			
			GL11.glPushMatrix();
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glEnable(GL11.GL_LINE_SMOOTH);
			GL11.glLineWidth(2);
			
			// Draw output
			
			renderX = GuiAIRig.spawnX - renderManager.renderPosX;
			renderY = GuiAIRig.spawnY - renderManager.renderPosY;
			renderZ = GuiAIRig.spawnZ - renderManager.renderPosZ;
			
			GL11.glTranslated(renderX, renderY, renderZ);
			GL11.glScalef(1, 1, 1);
			GL11.glColor4f(28, 188, 220, 0.5f);
			RenderUtils.drawOutlinedBox();
			RenderUtils.drawCrossBox();
			GL11.glColor4f(0F, 0F, 1F, 0.15F);
			RenderUtils.drawSolidBox();
			
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_LINE_SMOOTH);
			GL11.glPopMatrix();
		}
	}
	
	/**
	 * Changes block drop chances
	 * @param e
	 */
	@SubscribeEvent
	public void onDrop(BlockEvent.HarvestDropsEvent e) {
		if (e.getState().getBlock() == Blocks.GRAVEL && GuiLootManipulation.blockDrops.get("Flint")) {
			e.getDrops().clear();
			e.getDrops().add(new ItemStack(Items.FLINT));
		} else if (e.getState().getBlock() == Blocks.GLOWSTONE && GuiLootManipulation.blockDrops.get("Glowstone")) {
			e.getDrops().clear();
			e.getDrops().add(new ItemStack(Items.GLOWSTONE_DUST, 4));
		} else if (e.getState().getBlock() == Blocks.SEA_LANTERN && GuiLootManipulation.blockDrops.get("Sealantern")) {
			e.getDrops().clear();
			e.getDrops().add(new ItemStack(Items.PRISMARINE_CRYSTALS, 3));
		} else if (e.getState().getBlock() == Blocks.TALLGRASS && GuiLootManipulation.blockDrops.get("Seed")) {
			e.getDrops().clear();
			e.getDrops().add(new ItemStack(Items.WHEAT_SEEDS, 1));
		} else if (e.getState().getBlock() == Blocks.WHEAT && GuiLootManipulation.blockDrops.get("Wheatseeds")) {
			if (e.getState().getValue(PropertyInteger.create("age", 0, 7)) == 7) e.getDrops().add(new ItemStack(Items.WHEAT_SEEDS, 3));
		} else if (e.getState().getBlock() == Blocks.MELON_BLOCK && GuiLootManipulation.blockDrops.get("Melons")) {
			e.getDrops().clear();
			e.getDrops().add(new ItemStack(Items.MELON, 7));
		} else if (e.getState().getBlock() == Blocks.LEAVES && GuiLootManipulation.blockDrops.get("Apple")) {
			e.getDrops().clear();
			BlockPlanks.EnumType leave = ((BlockLeaves) e.getState().getBlock()).getWoodType(e.getState().getBlock().getMetaFromState(e.getState()));
			if (leave == EnumType.OAK) e.getDrops().add(new ItemStack(Items.APPLE, 1));
			e.getDrops().add(new ItemStack(Item.getItemFromBlock(Blocks.SAPLING), 1, leave.getMetadata()));
		} else if (e.getState().getBlock() == Blocks.LEAVES2 && GuiLootManipulation.blockDrops.get("Apple")) {
			e.getDrops().clear();
			BlockPlanks.EnumType leave = ((BlockLeaves) e.getState().getBlock()).getWoodType(e.getState().getBlock().getMetaFromState(e.getState()));
			if (leave == EnumType.DARK_OAK) e.getDrops().add(new ItemStack(Items.APPLE, 1));
			e.getDrops().add(new ItemStack(Item.getItemFromBlock(Blocks.SAPLING), 1, leave.getMetadata()));
		} else if (e.getState().getBlock() == Blocks.POTATOES && GuiLootManipulation.blockDrops.get("Potato")) {
			if (e.getState().getValue(PropertyInteger.create("age", 0, 7)) == 7)e.getDrops().clear();
			if (e.getState().getValue(PropertyInteger.create("age", 0, 7)) == 7)e.getDrops().add(new ItemStack(Items.POTATO, 4));
			if (e.getState().getValue(PropertyInteger.create("age", 0, 7)) == 7)e.getDrops().add(new ItemStack(Items.POISONOUS_POTATO, 1));
		} else if (e.getState().getBlock() == Blocks.DEADBUSH && GuiLootManipulation.blockDrops.get("Deadbush")) {
			e.getDrops().clear();
			e.getDrops().add(new ItemStack(Items.STICK, 2));
		} else if (e.getState().getBlock() == Blocks.CARROTS && GuiLootManipulation.blockDrops.get("Carrot")) {
			if (e.getState().getValue(PropertyInteger.create("age", 0, 7)) == 7)e.getDrops().clear();
			if (e.getState().getValue(PropertyInteger.create("age", 0, 7)) == 7)e.getDrops().add(new ItemStack(Items.CARROT, 4));
		} else if (e.getState().getBlock() == Blocks.REDSTONE_ORE && GuiLootManipulation.blockDrops.get("Redstone")) {
			e.getDrops().clear();
			e.getDrops().add(new ItemStack(Items.REDSTONE, 5));
		} else if (e.getState().getBlock() == Blocks.LAPIS_ORE && GuiLootManipulation.blockDrops.get("Lapis")) {
			e.getDrops().clear();
			e.getDrops().add(new ItemStack(Items.DYE, 9, 4));
		} else if (e.getState().getBlock() == Blocks.COCOA && GuiLootManipulation.blockDrops.get("Cocoa_Beans")) {
			e.getDrops().clear();
			if (e.getState().getValue(PropertyInteger.create("age", 0, 2)) == 2) e.getDrops().add(new ItemStack(Items.DYE, 3, 3));
		} else if (e.getState().getBlock() == Blocks.CHORUS_PLANT && GuiLootManipulation.blockDrops.get("Chorus_Plant")) {
			e.getDrops().clear();
			e.getDrops().add(new ItemStack(Items.CHORUS_FRUIT, 1));
		} else if (e.getState().getBlock() == Blocks.NETHER_WART && GuiLootManipulation.blockDrops.get("Nether_Wart")) {
			e.getDrops().clear();
			if (e.getState().getValue(PropertyInteger.create("age", 0, 3)) == 3) e.getDrops().add(new ItemStack(Items.NETHER_WART, 4));
		} else if (e.getState().getBlock() == Blocks.BROWN_MUSHROOM_BLOCK && GuiLootManipulation.blockDrops.get("Mushroom_Block")) {
			e.getDrops().clear();
			e.getDrops().add(new ItemStack(Item.getItemById(39), 2));
		} else if (e.getState().getBlock() == Blocks.RED_MUSHROOM_BLOCK && GuiLootManipulation.blockDrops.get("Mushroom_Block")) {
			e.getDrops().clear();
			e.getDrops().add(new ItemStack(Item.getItemById(40), 2));
		}
	}
	
}
