package de.pfannekuchen.lotas.gui;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import de.pfannekuchen.lotas.gui.parts.CustomEntitySlider;
import de.pfannekuchen.lotas.gui.parts.RightGuiCheckBox;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityHusk;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityWitherSkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.WorldEntitySpawner;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.client.config.GuiCheckBox;
import rlog.RLogAPI;

/**
 * Draws a gui where the player can decide to spawn an entity
 * @author Pancake
 *
 */
public class GuiEntitySpawner extends GuiScreen {
	
	public static HashMap<Integer, String> entities = new HashMap<Integer, String>();
	public GuiTextField xText;
	public GuiTextField yText;
	public GuiTextField zText;
	
	public static EntityLiving e;
	
	public static List<EnchantmentData> enchHelmetZombie = new ArrayList<>();
	public static List<EnchantmentData> enchChestZombie = new ArrayList<>();
	public static List<EnchantmentData> enchLeggingsZombie = new ArrayList<>();
	public static List<EnchantmentData> enchBootsZombie = new ArrayList<>();
	public static List<EnchantmentData> enchSwordZombie = new ArrayList<>();
	
	public static List<EnchantmentData> enchHelmetSkel = new ArrayList<>();
	public static List<EnchantmentData> enchChestSkel = new ArrayList<>();
	public static List<EnchantmentData> enchLeggingsSkel = new ArrayList<>();
	public static List<EnchantmentData> enchBootsSkel = new ArrayList<>();
	public static List<EnchantmentData> enchBowSkel = new ArrayList<>();
	public static GuiCheckBox leatherZombie = new GuiCheckBox(2, 1, - 12, "This is", false);
	public static GuiCheckBox chainZombie = new GuiCheckBox(2, 1,  - 12, "just random", false);
	public static GuiCheckBox ironZombie = new GuiCheckBox(2, 1, - 12, "numbers", false);
	public static GuiCheckBox goldZombie = new GuiCheckBox(2, 1, - 12, "so it", false);
	public static GuiCheckBox diamondZombie = new GuiCheckBox(2, 1, 12, "isn't", false);
	public static GuiCheckBox leatherSkel = new GuiCheckBox(2, 1, 1, "NULL", false);
	public static GuiCheckBox chainSkel = new GuiCheckBox(2, 1, 1, "When someone", false);
	public static GuiCheckBox ironSkel = new GuiCheckBox(2, 1, - 12, "Checks", false);
	public static GuiCheckBox goldSkel = new GuiCheckBox(2, 1, - 12, "Without the gui", false);
	public static GuiCheckBox diamondSkel = new GuiCheckBox(2, 2, - 12, "ever opened", false);
	
	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		check();
		super.mouseReleased(mouseX, mouseY, state);
	}
	
	static {
		entities.put(0, "Blaze");
		entities.put(1, "Cave Spider");
		entities.put(2, "Creeper");
		entities.put(3, "Enderman");
		entities.put(4, "Ghast");
		entities.put(5, "Husk");
		entities.put(6, "Iron Golem");
		entities.put(7, "Magma Cube");
		entities.put(8, "Skeleton");
		entities.put(9, "Slime");
		entities.put(10, "Spider");
		entities.put(11, "Witch");
		entities.put(12, "Witherskeleton");
		entities.put(13, "Zombie");
		entities.put(14, "Zombievillager");
	}
	
	public int spawnX = (int) Minecraft.getMinecraft().player.posX;
	public int spawnY = (int) Minecraft.getMinecraft().player.posY;
	public int spawnZ = (int) Minecraft.getMinecraft().player.posZ;
	public CustomEntitySlider entity;
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		drawCenteredString(mc.fontRenderer, "Entity Spawning Manipulator", width / 2, 20, 0xFFFFFF);
		int y = 75;
		if (e instanceof EntityZombie) {
			drawCenteredString(mc.fontRenderer, "Zombie", (int) ((width / 2) * 0 + (width / 4F)) - 20, 20, 0xFFFFFF);
			
			drawCenteredString(mc.fontRenderer, "\u00a7bHelmet: ", (int) ((width / 2) * 0 + (width / 4F)) - 20, y, 0xFFFFFF);
			if (enchHelmetZombie != null) for (EnchantmentData ench : enchHelmetZombie) {
				y += 11;
				drawCenteredString(mc.fontRenderer, I18n.format(ench.enchantment.getName()) + " " + ench.enchantmentLevel, (int) ((width / 2) * 0 + (width / 4F)) - 20, y, 0xFFFFFF);
			}
			y += 16;
			drawCenteredString(mc.fontRenderer, "\u00a7bChestplate: ", (int) ((width / 2)* 0 + (width / 4F)) - 20, y, 0xFFFFFF);
			if (enchChestZombie != null) for (EnchantmentData ench : enchChestZombie) {
				y += 11;
				drawCenteredString(mc.fontRenderer, I18n.format(ench.enchantment.getName()) + " " + ench.enchantmentLevel, (int) ((width / 2) * 0 + (width / 4F)) - 20 , y, 0xFFFFFF);
			}
			y += 16;
			drawCenteredString(mc.fontRenderer, "\u00a7bLeggings: ", (int) ((width / 2) * 0 + (width / 4F)) - 20, y, 0xFFFFFF);
			if (enchLeggingsZombie != null) for (EnchantmentData ench : enchLeggingsZombie) {
				y += 11;
				drawCenteredString(mc.fontRenderer, I18n.format(ench.enchantment.getName()) + " " + ench.enchantmentLevel, (int) ((width / 2) * 0 + (width / 4F)) - 20 , y, 0xFFFFFF);
			}
			y += 16;
			drawCenteredString(mc.fontRenderer, "\u00a7bBoots: ", (int) ((width / 2) * 0 + (width / 4F)) - 20 , y, 0xFFFFFF);
			if (enchBootsZombie != null) for (EnchantmentData ench : enchBootsZombie) {
				y += 11;
				drawCenteredString(mc.fontRenderer, I18n.format(ench.enchantment.getName()) + " " + ench.enchantmentLevel, (int) ((width / 2) * 0 + (width / 4F)) - 20 , y, 0xFFFFFF);
			}
			y += 16;
			drawCenteredString(mc.fontRenderer, "\u00a7bSword: ", (int) ((width / 2) * 0 + (width / 4F)) - 20 , y, 0xFFFFFF);
			if (enchSwordZombie != null) for (EnchantmentData ench : enchSwordZombie) {
				y += 11;
				drawCenteredString(mc.fontRenderer, I18n.format(ench.enchantment.getName()) + " " + ench.enchantmentLevel, (int) ((width / 2) * 0 + (width / 4F)) - 20, y, 0xFFFFFF);
			}
		}
		if (e instanceof EntitySkeleton) {
			drawCenteredString(mc.fontRenderer, "Skeleton", (int) ((width / 2) * 1 + (width / 4F)) + 20, 20, 0xFFFFFF);
			y = 75;
			drawCenteredString(mc.fontRenderer, "\u00a7bHelmet: ", (int) ((width / 2) * 1 + (width / 4F)) + 20, y, 0xFFFFFF);
			if (enchHelmetSkel != null) for (EnchantmentData ench : enchHelmetSkel) {
				y += 11;
				drawCenteredString(mc.fontRenderer, I18n.format(ench.enchantment.getName()) + " " + ench.enchantmentLevel, (int) ((width / 2) * 1 + (width / 4F)) + 20, y, 0xFFFFFF);
			}
			y += 16;
			drawCenteredString(mc.fontRenderer, "\u00a7bChestplate: ", (int) ((width / 2) * 1 + (width / 4F)) + 20, y, 0xFFFFFF);
			if (enchChestSkel != null) for (EnchantmentData ench : enchChestSkel) {
				y += 11;
				drawCenteredString(mc.fontRenderer, I18n.format(ench.enchantment.getName()) + " " + ench.enchantmentLevel, (int) ((width / 2) * 1 + (width / 4F)) + 20, y, 0xFFFFFF);
			}
			y += 16;
			drawCenteredString(mc.fontRenderer, "\u00a7bLeggings: ", (int) ((width / 2) * 1 + (width / 4F)) + 20, y, 0xFFFFFF);
			if (enchLeggingsSkel != null) for (EnchantmentData ench : enchLeggingsSkel) {
				y += 11;
				drawCenteredString(mc.fontRenderer, I18n.format(ench.enchantment.getName()) + " " + ench.enchantmentLevel, (int) ((width / 2) * 1 + (width / 4F)) + 20, y, 0xFFFFFF);
			}
			y += 16;
			drawCenteredString(mc.fontRenderer, "\u00a7bBoots: ", (int) ((width / 2) * 1 + (width / 4F)) + 20, y, 0xFFFFFF);
			if (enchBootsSkel != null) for (EnchantmentData ench : enchBootsSkel) {
				y += 11;
				drawCenteredString(mc.fontRenderer, I18n.format(ench.enchantment.getName()) + " " + ench.enchantmentLevel, (int) ((width / 2) * 1 + (width / 4F)) + 20, y, 0xFFFFFF);
			}
			y += 16;
			drawCenteredString(mc.fontRenderer, "\u00a7bBow: ", (int) ((width / 2) * 1 + (width / 4F)) + 20, y, 0xFFFFFF);
			if (enchBowSkel != null) for (EnchantmentData ench : enchBowSkel) {
				y += 11;
				drawCenteredString(mc.fontRenderer, I18n.format(ench.enchantment.getName()) + " " + ench.enchantmentLevel, (int) ((width / 2) * 1 + (width / 4F)) + 20, y, 0xFFFFFF);
			}
		}
		xText.drawTextBox();
		yText.drawTextBox();
		zText.drawTextBox();
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (Character.isDigit(typedChar) || !Character.isLetter(typedChar)) {
			xText.textboxKeyTyped(typedChar, keyCode);
			yText.textboxKeyTyped(typedChar, keyCode);
			zText.textboxKeyTyped(typedChar, keyCode);
		}
		try {
			spawnX = Integer.parseInt(xText.getText());
			spawnY = Integer.parseInt(yText.getText());
			spawnZ = Integer.parseInt(zText.getText());
		} catch (Exception e) {
			RLogAPI.logError(e, "Parse Invalid Data #2");
		}
		check();
		super.keyTyped(typedChar, keyCode);
	}
	
	@Override
	public void initGui() {
		entity = new CustomEntitySlider(1, width / 2 - (width / 8), 50, entities, width / 4, 20);
		this.buttonList.add(entity);
		this.buttonList.add(new GuiButton(8, width / 2 - 35, 80, 30, 20, "X++"));
		this.buttonList.add(new GuiButton(2, width / 2 + 5, 80, 30, 20, "X--"));
		this.buttonList.add(new GuiButton(5, width / 2 - 35, 105, 30, 20, "Y++"));
		this.buttonList.add(new GuiButton(9, width / 2 + 5, 105, 30, 20, "Y--"));
		this.buttonList.add(new GuiButton(6, width / 2 - 35, 130, 30, 20, "Z++"));
		this.buttonList.add(new GuiButton(4, width / 2 + 5, 130, 30, 20, "Z--"));
		
		buttonList.add(new GuiButton(20, (width / 4) * 0 - 20 + (width / 8), 50, (width / 4), 20, "Reroll Zombie Enchants"));
		buttonList.add(new GuiButton(21, (width / 4) * 2 + 20 + (width / 8), 50, (width / 4), 20, "Reroll Skeleton Enchants"));
		
		xText = new GuiTextField(91, Minecraft.getMinecraft().fontRenderer, width / 2 - 50, 155, 30, 15);
		xText.setText(spawnX + "");
		yText = new GuiTextField(92, Minecraft.getMinecraft().fontRenderer, width / 2 - 15, 155, 30, 15);
		yText.setText(spawnY + "");
		zText = new GuiTextField(93, Minecraft.getMinecraft().fontRenderer, width / 2 + 20, 155, 30, 15);
		zText.setText(spawnZ + "");
		
		leatherZombie = new GuiCheckBox(22, (width / 2) * 0 + 2, height - 12, "Leather Armor", leatherZombie.isChecked());
		chainZombie = new GuiCheckBox(23, (width / 2) * 0 + 2, height - 24, "Chain Armor", chainZombie.isChecked());
		ironZombie = new GuiCheckBox(24, (width / 2) * 0 + 2, height - 36, "Iron Armor", ironZombie.isChecked());
		goldZombie = new GuiCheckBox(25, (width / 2) * 0 + 2, height - 48, "Gold Armor", goldZombie.isChecked());
		diamondZombie = new GuiCheckBox(26, (width / 2) * 0 + 2, height - 60, "Diamond Armor", diamondZombie.isChecked());
		buttonList.add(leatherZombie);
		buttonList.add(chainZombie);
		buttonList.add(ironZombie);
		buttonList.add(goldZombie);
		buttonList.add(diamondZombie);
		
		leatherSkel = new RightGuiCheckBox(27, width - 26 - mc.fontRenderer.getStringWidth("Leather Armor"), height - 12, "Leather Armor", leatherSkel.isChecked());
		chainSkel = new RightGuiCheckBox(28, width - 26 - mc.fontRenderer.getStringWidth("Chain Armor"), height - 24, "Chain Armor", chainSkel.isChecked());
		ironSkel = new RightGuiCheckBox(29, width - 26 - mc.fontRenderer.getStringWidth("Iron Armor"), height - 36, "Iron Armor", ironSkel.isChecked());
		goldSkel = new RightGuiCheckBox(30, width - 26 - mc.fontRenderer.getStringWidth("Gold Armor"), height - 48, "Gold Armor", goldSkel.isChecked());
		diamondSkel = new RightGuiCheckBox(31, width - 26 - mc.fontRenderer.getStringWidth("Diamond Armor"), height - 60, "Diamond Armor", diamondSkel.isChecked());
		buttonList.add(leatherSkel);
		buttonList.add(chainSkel);
		buttonList.add(ironSkel);
		buttonList.add(goldSkel);
		buttonList.add(diamondSkel);
		
		this.buttonList.add(new GuiButton(69, width / 2 - 90, height - 24, 170, 20, "Spawn"));
		this.buttonList.add(new GuiButton(12, width / 2 - 90, height - 48, 170, 20, "Done"));
		check();
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		xText.mouseClicked(mouseX, mouseY, mouseButton);
		yText.mouseClicked(mouseX, mouseY, mouseButton);
		zText.mouseClicked(mouseX, mouseY, mouseButton);
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		switch (button.id) {
		case 20:
			Random rng = new Random();
			RLogAPI.logDebug("[EntitySpawner] Rerolling Zombie Enchantment List");
			enchHelmetZombie = EnchantmentHelper.buildEnchantmentList(rng, new ItemStack(Items.DIAMOND_HELMET), (int) (5 + (rng.nextInt(5) + 13) * (mc.world.getDifficulty() == EnumDifficulty.HARD ? .13f : 0)), false);
			enchSwordZombie = EnchantmentHelper.buildEnchantmentList(new Random(System.currentTimeMillis()), new ItemStack(Items.IRON_SWORD), 5 + (rng.nextInt(5) + 13) * (mc.world.getDifficulty() == EnumDifficulty.HARD ? 1 : 0), false);
			enchChestZombie = EnchantmentHelper.buildEnchantmentList(rng, new ItemStack(Items.DIAMOND_CHESTPLATE), (int) (5 + (rng.nextInt(5) + 13) * (mc.world.getDifficulty() == EnumDifficulty.HARD ? .13f : 0)), false);
			enchLeggingsZombie = EnchantmentHelper.buildEnchantmentList(rng, new ItemStack(Items.DIAMOND_LEGGINGS), (int) (5 + (rng.nextInt(5) + 13) * (mc.world.getDifficulty() == EnumDifficulty.HARD ? .13f : 0)), false);
			enchBootsZombie = EnchantmentHelper.buildEnchantmentList(rng, new ItemStack(Items.DIAMOND_BOOTS), (int) (5 + (rng.nextInt(5) + 13) * (mc.world.getDifficulty() == EnumDifficulty.HARD ? .13f : 0)), false);
			return;
		case 21:
			Random rng2 = new Random();
			RLogAPI.logDebug("[EntitySpawner] Rerolling Skeleton Enchantment List");
			enchHelmetSkel = EnchantmentHelper.buildEnchantmentList(rng2, new ItemStack(Items.DIAMOND_HELMET), (int) (5 + (rng2.nextInt(5) + 13) * (mc.world.getDifficulty() == EnumDifficulty.HARD ? .13f : 0)), false);
			enchBowSkel = EnchantmentHelper.buildEnchantmentList(rng2, new ItemStack(Items.BOW), (int) (5 + (rng2.nextInt(5) + 13) * (mc.world.getDifficulty() == EnumDifficulty.HARD ? .13f : 0)), false);
			enchChestSkel = EnchantmentHelper.buildEnchantmentList(rng2, new ItemStack(Items.DIAMOND_CHESTPLATE), (int) (5 + (rng2.nextInt(5) + 13) * (mc.world.getDifficulty() == EnumDifficulty.HARD ? .13f : 0)), false);
			enchLeggingsSkel = EnchantmentHelper.buildEnchantmentList(rng2, new ItemStack(Items.DIAMOND_LEGGINGS), (int) (5 + (rng2.nextInt(5) + 13) * (mc.world.getDifficulty() == EnumDifficulty.HARD ? .13f : 0)), false);
			enchBootsSkel = EnchantmentHelper.buildEnchantmentList(rng2, new ItemStack(Items.DIAMOND_BOOTS), (int) (5 + (rng2.nextInt(5) + 13) * (mc.world.getDifficulty() == EnumDifficulty.HARD ? .13f : 0)), false);
			break;
		default:
			break;
		}
		
		if (button.id >= 22 && button.id <= 26) {
			((GuiCheckBox) buttonList.get(9)).setIsChecked(false);
			((GuiCheckBox) buttonList.get(10)).setIsChecked(false);
			((GuiCheckBox) buttonList.get(11)).setIsChecked(false);
			((GuiCheckBox) buttonList.get(12)).setIsChecked(false);
			((GuiCheckBox) buttonList.get(13)).setIsChecked(false);
			((GuiCheckBox) button).setIsChecked(true);
		} else if (button.id >= 27 && button.id <= 31) {
			((GuiCheckBox) buttonList.get(14)).setIsChecked(false);
			((GuiCheckBox) buttonList.get(15)).setIsChecked(false);
			((GuiCheckBox) buttonList.get(16)).setIsChecked(false);
			((GuiCheckBox) buttonList.get(17)).setIsChecked(false);
			((GuiCheckBox) buttonList.get(18)).setIsChecked(false);
			((GuiCheckBox) button).setIsChecked(true);
		}
		
		switch (button.id) {
		case 8:
			spawnX++;
			break;
		case 2:
			spawnX--;
			break;
		case 5:
			spawnY++;
			break;
		case 9:
			spawnY--;
			break;
		case 6:
			spawnZ++;
			break;
		case 4:
			spawnZ--;
			break;
		case 69:
			RLogAPI.logDebug("[EntitySpawner] Trying to spawn entity");
			Entity e = null;
			WorldServer world = mc.getIntegratedServer().getWorld(mc.player.dimension);
			switch (entity.displayString.split(": ")[1].trim()) {
			case "Ghast":
				e = new EntityGhast(world);
				break;
			case "Blaze":
				e = new EntityBlaze(world);
				break;
			case "Enderman":
				e = new EntityEnderman(world);
				break;
			case "Zombie":
				e = new EntityZombie(world);
				break;
			case "Skeleton":
				e = new EntitySkeleton(world);
				break;
			case "Witch":
				e = new EntityWitch(world);
				break;
			case "Witherskeleton":
				e = new EntityWitherSkeleton(world);
				break;
			case "Cat":
				return;
			case "Chicken":
				e = new EntityChicken(world);
				break;
			case "Cave Spider":
				e = new EntityCaveSpider(world);
				break;
			case "Cow":
				e = new EntityCow(world);
				break;
			case "Creeper":
				e = new EntityCreeper(world);
				break;
			case "Husk":
				e = new EntityHusk(world);
				break;
			case "Iron Golem":
				e = new EntityIronGolem(world);
				break;
			case "Magma Cube":
				e = new EntityMagmaCube(world);
				break;
			case "Mooshroom":
				e = new EntityMooshroom(world);
				break;
			case "Ocelot":
				e = new EntityOcelot(world);
				break;
			case "Pig":
				e = new EntityPig(world);
				break;
			case "Rabbit":
				e = new EntityRabbit(world);
				break;
			case "Sheep":
				e = new EntitySheep(world);
				break;
			case "Slime":
				e = new EntitySlime(world);
				break;
			case "Spider":
				e = new EntitySpider(world);
				break;
			case "Villager":
				e = new EntityVillager(world);
				break;
			case "Wolf":
				e = new EntityWolf(world);
				break;
			case "Zombievillager":
				e = new EntityZombieVillager(world);
				break;
			default:
				return;
			}
			e.setLocationAndAngles(spawnX, spawnY, spawnZ, 0, 0);
			
			EntityLiving living = (EntityLiving) e;
			try {
				Method method = EntityLiving.class.getDeclaredMethod("setEquipmentBasedOnDifficulty", DifficultyInstance.class);
				method.setAccessible(true);
				method.invoke(living, mc.getIntegratedServer().getWorld(mc.player.dimension).getDifficultyForLocation(mc.player.getPosition()));
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
					| NoSuchMethodException | SecurityException e1) {
				RLogAPI.logDebug("[EntitySpawner] Field not found");
			}
			if (living instanceof EntityZombie) {
				living.inventoryArmorDropChances = new float[] { 1.0f, 1.0f, 1.0f, 1.0f };
				living.inventoryHandsDropChances = new float[] { 1.0f, 1.0f };
				if (GuiEntitySpawner.enchSwordZombie != null) living.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, addEnchants(new ItemStack(Items.IRON_SWORD), GuiEntitySpawner.enchSwordZombie));
				if (GuiEntitySpawner.diamondZombie.isChecked() && GuiEntitySpawner.enchHelmetZombie != null) { 
					living.setItemStackToSlot(EntityEquipmentSlot.FEET, addEnchants(new ItemStack(Items.DIAMOND_BOOTS), GuiEntitySpawner.enchBootsZombie));
					living.setItemStackToSlot(EntityEquipmentSlot.CHEST, addEnchants(new ItemStack(Items.DIAMOND_CHESTPLATE), GuiEntitySpawner.enchChestZombie));
					living.setItemStackToSlot(EntityEquipmentSlot.LEGS, addEnchants(new ItemStack(Items.DIAMOND_LEGGINGS), GuiEntitySpawner.enchLeggingsZombie));
					living.setItemStackToSlot(EntityEquipmentSlot.HEAD, addEnchants(new ItemStack(Items.DIAMOND_HELMET), GuiEntitySpawner.enchHelmetZombie));
				} else if (GuiEntitySpawner.ironZombie.isChecked() && GuiEntitySpawner.enchHelmetZombie != null) { 
					living.setItemStackToSlot(EntityEquipmentSlot.FEET, addEnchants(new ItemStack(Items.IRON_BOOTS), GuiEntitySpawner.enchBootsZombie));
					living.setItemStackToSlot(EntityEquipmentSlot.CHEST, addEnchants(new ItemStack(Items.IRON_CHESTPLATE), GuiEntitySpawner.enchChestZombie));
					living.setItemStackToSlot(EntityEquipmentSlot.LEGS, addEnchants(new ItemStack(Items.IRON_LEGGINGS), GuiEntitySpawner.enchLeggingsZombie));
					living.setItemStackToSlot(EntityEquipmentSlot.HEAD, addEnchants(new ItemStack(Items.IRON_HELMET), GuiEntitySpawner.enchHelmetZombie));
				} else if (GuiEntitySpawner.chainZombie.isChecked() && GuiEntitySpawner.enchHelmetZombie != null) { 
					living.setItemStackToSlot(EntityEquipmentSlot.FEET, addEnchants(new ItemStack(Items.CHAINMAIL_BOOTS), GuiEntitySpawner.enchChestZombie));
					living.setItemStackToSlot(EntityEquipmentSlot.CHEST, addEnchants(new ItemStack(Items.CHAINMAIL_CHESTPLATE), GuiEntitySpawner.enchChestZombie));
					living.setItemStackToSlot(EntityEquipmentSlot.LEGS, addEnchants(new ItemStack(Items.CHAINMAIL_LEGGINGS), GuiEntitySpawner.enchLeggingsZombie));
					living.setItemStackToSlot(EntityEquipmentSlot.HEAD, addEnchants(new ItemStack(Items.CHAINMAIL_HELMET), GuiEntitySpawner.enchHelmetZombie));
				} else if (GuiEntitySpawner.goldZombie.isChecked() && GuiEntitySpawner.enchHelmetZombie != null) { 
					living.setItemStackToSlot(EntityEquipmentSlot.FEET, addEnchants(new ItemStack(Items.GOLDEN_BOOTS), GuiEntitySpawner.enchBootsZombie));
					living.setItemStackToSlot(EntityEquipmentSlot.CHEST, addEnchants(new ItemStack(Items.GOLDEN_CHESTPLATE), GuiEntitySpawner.enchChestZombie));
					living.setItemStackToSlot(EntityEquipmentSlot.LEGS, addEnchants(new ItemStack(Items.GOLDEN_LEGGINGS), GuiEntitySpawner.enchLeggingsZombie));
					living.setItemStackToSlot(EntityEquipmentSlot.HEAD, addEnchants(new ItemStack(Items.GOLDEN_HELMET), GuiEntitySpawner.enchHelmetZombie));
				} else if (GuiEntitySpawner.leatherZombie.isChecked()) { 
					living.setItemStackToSlot(EntityEquipmentSlot.FEET, addEnchants(new ItemStack(Items.LEATHER_BOOTS), GuiEntitySpawner.enchBootsZombie));
					living.setItemStackToSlot(EntityEquipmentSlot.CHEST, addEnchants(new ItemStack(Items.LEATHER_CHESTPLATE), GuiEntitySpawner.enchChestZombie));
					living.setItemStackToSlot(EntityEquipmentSlot.LEGS, addEnchants(new ItemStack(Items.LEATHER_LEGGINGS), GuiEntitySpawner.enchLeggingsZombie));
					living.setItemStackToSlot(EntityEquipmentSlot.HEAD, addEnchants(new ItemStack(Items.LEATHER_HELMET), GuiEntitySpawner.enchHelmetZombie));
				}
			} else if (living instanceof EntitySkeleton) {
				living.inventoryArmorDropChances = new float[] { 1.0f, 1.0f, 1.0f, 1.0f };
				living.inventoryHandsDropChances = new float[] { 1.0f, 1.0f };
				if (GuiEntitySpawner.enchBowSkel != null) living.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, addEnchants(new ItemStack(Items.BOW), GuiEntitySpawner.enchBowSkel));
				if (GuiEntitySpawner.diamondSkel.isChecked() && GuiEntitySpawner.enchHelmetSkel != null) { 
					living.setItemStackToSlot(EntityEquipmentSlot.FEET, addEnchants(new ItemStack(Items.DIAMOND_BOOTS), GuiEntitySpawner.enchBootsSkel));
					living.setItemStackToSlot(EntityEquipmentSlot.CHEST, addEnchants(new ItemStack(Items.DIAMOND_CHESTPLATE), GuiEntitySpawner.enchChestSkel));
					living.setItemStackToSlot(EntityEquipmentSlot.LEGS, addEnchants(new ItemStack(Items.DIAMOND_LEGGINGS), GuiEntitySpawner.enchLeggingsSkel));
					living.setItemStackToSlot(EntityEquipmentSlot.HEAD, addEnchants(new ItemStack(Items.DIAMOND_HELMET), GuiEntitySpawner.enchHelmetSkel));
				} else if (GuiEntitySpawner.ironSkel.isChecked() && GuiEntitySpawner.enchHelmetSkel != null) { 
					living.setItemStackToSlot(EntityEquipmentSlot.FEET, addEnchants(new ItemStack(Items.IRON_BOOTS), GuiEntitySpawner.enchBootsSkel));
					living.setItemStackToSlot(EntityEquipmentSlot.CHEST, addEnchants(new ItemStack(Items.IRON_CHESTPLATE), GuiEntitySpawner.enchChestSkel));
					living.setItemStackToSlot(EntityEquipmentSlot.LEGS, addEnchants(new ItemStack(Items.IRON_LEGGINGS), GuiEntitySpawner.enchLeggingsSkel));
					living.setItemStackToSlot(EntityEquipmentSlot.HEAD, addEnchants(new ItemStack(Items.IRON_HELMET), GuiEntitySpawner.enchHelmetSkel));
				} else if (GuiEntitySpawner.chainSkel.isChecked() && GuiEntitySpawner.enchHelmetSkel != null) { 
					living.setItemStackToSlot(EntityEquipmentSlot.FEET, addEnchants(new ItemStack(Items.CHAINMAIL_BOOTS), GuiEntitySpawner.enchChestSkel));
					living.setItemStackToSlot(EntityEquipmentSlot.CHEST, addEnchants(new ItemStack(Items.CHAINMAIL_CHESTPLATE), GuiEntitySpawner.enchChestSkel));
					living.setItemStackToSlot(EntityEquipmentSlot.LEGS, addEnchants(new ItemStack(Items.CHAINMAIL_LEGGINGS), GuiEntitySpawner.enchLeggingsSkel));
					living.setItemStackToSlot(EntityEquipmentSlot.HEAD, addEnchants(new ItemStack(Items.CHAINMAIL_HELMET), GuiEntitySpawner.enchHelmetSkel));
				} else if (GuiEntitySpawner.goldSkel.isChecked() && GuiEntitySpawner.enchHelmetSkel != null) { 
					living.setItemStackToSlot(EntityEquipmentSlot.FEET, addEnchants(new ItemStack(Items.GOLDEN_BOOTS), GuiEntitySpawner.enchBootsSkel));
					living.setItemStackToSlot(EntityEquipmentSlot.CHEST, addEnchants(new ItemStack(Items.GOLDEN_CHESTPLATE), GuiEntitySpawner.enchChestSkel));
					living.setItemStackToSlot(EntityEquipmentSlot.LEGS, addEnchants(new ItemStack(Items.GOLDEN_LEGGINGS), GuiEntitySpawner.enchLeggingsSkel));
					living.setItemStackToSlot(EntityEquipmentSlot.HEAD, addEnchants(new ItemStack(Items.GOLDEN_HELMET), GuiEntitySpawner.enchHelmetSkel));
				} else if (GuiEntitySpawner.leatherSkel.isChecked()) { 
					living.setItemStackToSlot(EntityEquipmentSlot.FEET, addEnchants(new ItemStack(Items.LEATHER_BOOTS), GuiEntitySpawner.enchBootsSkel));
					living.setItemStackToSlot(EntityEquipmentSlot.CHEST, addEnchants(new ItemStack(Items.LEATHER_CHESTPLATE), GuiEntitySpawner.enchChestSkel));
					living.setItemStackToSlot(EntityEquipmentSlot.LEGS, addEnchants(new ItemStack(Items.LEATHER_LEGGINGS), GuiEntitySpawner.enchLeggingsSkel));
					living.setItemStackToSlot(EntityEquipmentSlot.HEAD, addEnchants(new ItemStack(Items.LEATHER_HELMET), GuiEntitySpawner.enchHelmetSkel));
				}
			}
			world.spawnEntity(e);
			RLogAPI.logDebug("[EntitySpawner] Entity has been spawned");
			break;
		case 12:
			Minecraft.getMinecraft().displayGuiScreen(new GuiIngameMenu());
			break;
		default:
			break;
		}
		
		xText.setText(spawnX + "");
		yText.setText(spawnY + "");
		zText.setText(spawnZ + "" );
		check();
	}
	
	public void check() {
		RLogAPI.logDebug("[EntitySpawner] Checking Entity Spawn Location");
		e = null;
		WorldServer world = mc.getIntegratedServer().getWorld(mc.player.dimension);
		switch (entity.displayString.split(": ")[1].trim()) {
		case "Ghast":
			e = new EntityGhast(world);
			break;
		case "Blaze":
			e = new EntityBlaze(world);
			break;
		case "Enderman":
			e = new EntityEnderman(world);
			break;
		case "Zombie":
			e = new EntityZombie(world);
			break;
		case "Skeleton":
			e = new EntitySkeleton(world);
			break;
		case "Witch":
			e = new EntityWitch(world);
			break;
		case "Witherskeleton":
			e = new EntityWitherSkeleton(world);
			break;
		case "Cat":
			return;
		case "Chicken":
			e = new EntityChicken(world);
			break;
		case "Cave Spider":
			e = new EntityCaveSpider(world);
			break;
		case "Cow":
			e = new EntityCow(world);
			break;
		case "Creeper":
			e = new EntityCreeper(world);
			break;
		case "Husk":
			e = new EntityHusk(world);
			break;
		case "Iron Golem":
			e = new EntityIronGolem(world);
			break;
		case "Magma Cube":
			e = new EntityMagmaCube(world);
			break;
		case "Mooshroom":
			e = new EntityMooshroom(world);
			break;
		case "Ocelot":
			e = new EntityOcelot(world);
			break;
		case "Pig":
			e = new EntityPig(world);
			break;
		case "Rabbit":
			e = new EntityRabbit(world);
			break;
		case "Sheep":
			e = new EntitySheep(world);
			break;
		case "Slime":
			e = new EntitySlime(world);
			break;
		case "Spider":
			e = new EntitySpider(world);
			break;
		case "Villager":
			e = new EntityVillager(world);
			break;
		case "Wolf":
			e = new EntityWolf(world);
			break;
		case "Zombievillager":
			e = new EntityZombieVillager(world);
			break;
		default:
			return;
		}
		e.setPosition(spawnX, spawnY, spawnZ);
		
		
		if (e instanceof EntityZombie) {
			buttonList.get(7).visible = true;
			((GuiCheckBox) buttonList.get(9)).visible = true;
			((GuiCheckBox) buttonList.get(10)).visible = true;
			((GuiCheckBox) buttonList.get(11)).visible = true;
			((GuiCheckBox) buttonList.get(12)).visible = true;
			((GuiCheckBox) buttonList.get(13)).visible = true;
			
		} else {
			buttonList.get(7).visible = false;
			((GuiCheckBox) buttonList.get(9)).visible = false;
			((GuiCheckBox) buttonList.get(10)).visible = false;
			((GuiCheckBox) buttonList.get(11)).visible = false;
			((GuiCheckBox) buttonList.get(12)).visible = false;
			((GuiCheckBox) buttonList.get(13)).visible = false;
		}
		
		if (e instanceof EntitySkeleton) {
			buttonList.get(8).visible = true;
			((GuiCheckBox) buttonList.get(14)).visible = true;
			((GuiCheckBox) buttonList.get(15)).visible = true;
			((GuiCheckBox) buttonList.get(16)).visible = true;
			((GuiCheckBox) buttonList.get(17)).visible = true;
			((GuiCheckBox) buttonList.get(18)).visible = true;
		} else {
			buttonList.get(8).visible = false;
			((GuiCheckBox) buttonList.get(14)).visible = false;
			((GuiCheckBox) buttonList.get(15)).visible = false;
			((GuiCheckBox) buttonList.get(16)).visible = false;
			((GuiCheckBox) buttonList.get(17)).visible = false;
			((GuiCheckBox) buttonList.get(18)).visible = false;
		}
		
		// Check Position
		boolean canSpawn = WorldEntitySpawner.canCreatureTypeSpawnAtLocation(EntityLiving.SpawnPlacementType.ON_GROUND, world, new BlockPos(spawnX, spawnY, spawnZ));
		// Check Block
		RLogAPI.logDebug("[EntitySpawner] Entity can spawn: " + canSpawn);
		if (canSpawn) canSpawn = ((EntityLiving) e).getCanSpawnHere();
		this.buttonList.get(this.buttonList.size() - 2).enabled = canSpawn;
	}


	public ItemStack addEnchants(ItemStack item, List<EnchantmentData> enchants) {
		for (EnchantmentData enchantmentData : enchants) {
			item.addEnchantment(enchantmentData.enchantment, enchantmentData.enchantmentLevel);
		}
		return item;
	}
	
	
}
