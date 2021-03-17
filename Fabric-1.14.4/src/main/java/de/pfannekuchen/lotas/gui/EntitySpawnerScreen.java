package de.pfannekuchen.lotas.gui;

import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

import de.pfannekuchen.lotas.gui.widgets.EntitySliderWidget;
import de.pfannekuchen.lotas.gui.widgets.SmallCheckboxWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.InfoEnchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnType;
import net.minecraft.entity.mob.BlazeEntity;
import net.minecraft.entity.mob.CaveSpiderEntity;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.mob.GhastEntity;
import net.minecraft.entity.mob.HuskEntity;
import net.minecraft.entity.mob.MagmaCubeEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.entity.mob.SpiderEntity;
import net.minecraft.entity.mob.WitchEntity;
import net.minecraft.entity.mob.WitherSkeletonEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.mob.ZombieVillagerEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.MooshroomEntity;
import net.minecraft.entity.passive.OcelotEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.passive.RabbitEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.LiteralText;
import net.minecraft.world.Difficulty;
import rlog.RLogAPI;

public class EntitySpawnerScreen extends Screen {

	public EntitySpawnerScreen() {
		super(new LiteralText("Entity Spawner"));
	}

	public static HashMap<Integer, String> entities = new HashMap<Integer, String>();
	public TextFieldWidget xText;
	public TextFieldWidget yText;
	public TextFieldWidget zText;
	
	public Consumer<SmallCheckboxWidget> cc2 = button -> {
		((SmallCheckboxWidget) buttons.get(14)).silentPress(false);
		((SmallCheckboxWidget) buttons.get(15)).silentPress(false);
		((SmallCheckboxWidget) buttons.get(16)).silentPress(false);
		((SmallCheckboxWidget) buttons.get(17)).silentPress(false);
		((SmallCheckboxWidget) buttons.get(18)).silentPress(false);
		button.silentPress(true);
	};
	
	
	public Consumer<SmallCheckboxWidget> cc = button -> {
		((SmallCheckboxWidget) buttons.get(9)).silentPress(false);
		((SmallCheckboxWidget) buttons.get(10)).silentPress(false);
		((SmallCheckboxWidget) buttons.get(11)).silentPress(false);
		((SmallCheckboxWidget) buttons.get(12)).silentPress(false);
		((SmallCheckboxWidget) buttons.get(13)).silentPress(false);
		button.silentPress(true);
	};
	
	public static LivingEntity e;
	
	public static List<InfoEnchantment> enchHelmetZombie;
	public static List<InfoEnchantment> enchChestZombie;
	public static List<InfoEnchantment> enchLeggingsZombie;
	public static List<InfoEnchantment> enchBootsZombie;
	public static List<InfoEnchantment> enchSwordZombie;
	
	public static List<InfoEnchantment> enchHelmetSkel;
	public static List<InfoEnchantment> enchChestSkel;
	public static List<InfoEnchantment> enchLeggingsSkel;
	public static List<InfoEnchantment> enchBootsSkel;
	public static List<InfoEnchantment> enchBowSkel;
	public static SmallCheckboxWidget leatherZombie = new SmallCheckboxWidget(1, - 12, "This is", false);
	public static SmallCheckboxWidget chainZombie = new SmallCheckboxWidget(1,  - 12, "just random", false);
	public static SmallCheckboxWidget ironZombie = new SmallCheckboxWidget(1, - 12, "text", false);
	public static SmallCheckboxWidget goldZombie = new SmallCheckboxWidget(1, - 12, "so it", false);
	public static SmallCheckboxWidget diamondZombie = new SmallCheckboxWidget(1, 12, "isn't", false);
	public static SmallCheckboxWidget leatherSkel = new SmallCheckboxWidget(1, 1, "NULL", false);
	public static SmallCheckboxWidget chainSkel = new SmallCheckboxWidget(1, 1, "When someone", false);
	public static SmallCheckboxWidget ironSkel = new SmallCheckboxWidget(1, - 12, "Checks", false);
	public static SmallCheckboxWidget goldSkel = new SmallCheckboxWidget(1, - 12, "Without the gui", false);
	public static SmallCheckboxWidget diamondSkel = new SmallCheckboxWidget(2, - 12, "ever opened", false);
	
	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int state) {
		check();
		return super.mouseReleased(mouseX, mouseY, state);
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
	
	public int spawnX = (int) MinecraftClient.getInstance().player.x;
	public int spawnY = (int) MinecraftClient.getInstance().player.y;
	public int spawnZ = (int) MinecraftClient.getInstance().player.z;
	public EntitySliderWidget entity;
	
	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		super.render(mouseX, mouseY, partialTicks);
		drawCenteredString(minecraft.textRenderer, "Entity Spawning Manipulator", width / 2, 20, 0xFFFFFF);
		int y = 75;
		if (e instanceof ZombieEntity) {
			drawCenteredString(minecraft.textRenderer, "Zombie", (int) ((width / 2) * 0 + (width / 4F)) - 20, 20, 0xFFFFFF);
			
			drawCenteredString(minecraft.textRenderer, "\u00a7bHelmet: ", (int) ((width / 2) * 0 + (width / 4F)) - 20, y, 0xFFFFFF);
			if (enchHelmetZombie != null) for (InfoEnchantment ench : enchHelmetZombie) {
				y += 11;
				drawCenteredString(minecraft.textRenderer, I18n.translate(ench.enchantment.getTranslationKey()) + " " + ench.level, (int) ((width / 2) * 0 + (width / 4F)) - 20, y, 0xFFFFFF);
			}
			y += 16;
			drawCenteredString(minecraft.textRenderer, "\u00a7bChestplate: ", (int) ((width / 2)* 0 + (width / 4F)) - 20, y, 0xFFFFFF);
			if (enchChestZombie != null) for (InfoEnchantment ench : enchChestZombie) {
				y += 11;
				drawCenteredString(minecraft.textRenderer, I18n.translate(ench.enchantment.getTranslationKey()) + " " + ench.level, (int) ((width / 2) * 0 + (width / 4F)) - 20 , y, 0xFFFFFF);
			}
			y += 16;
			drawCenteredString(minecraft.textRenderer, "\u00a7bLeggings: ", (int) ((width / 2) * 0 + (width / 4F)) - 20, y, 0xFFFFFF);
			if (enchLeggingsZombie != null) for (InfoEnchantment ench : enchLeggingsZombie) {
				y += 11;
				drawCenteredString(minecraft.textRenderer, I18n.translate(ench.enchantment.getTranslationKey()) + " " + ench.level, (int) ((width / 2) * 0 + (width / 4F)) - 20 , y, 0xFFFFFF);
			}
			y += 16;
			drawCenteredString(minecraft.textRenderer, "\u00a7bBoots: ", (int) ((width / 2) * 0 + (width / 4F)) - 20 , y, 0xFFFFFF);
			if (enchBootsZombie != null) for (InfoEnchantment ench : enchBootsZombie) {
				y += 11;
				drawCenteredString(minecraft.textRenderer, I18n.translate(ench.enchantment.getTranslationKey()) + " " + ench.level, (int) ((width / 2) * 0 + (width / 4F)) - 20 , y, 0xFFFFFF);
			}
			y += 16;
			drawCenteredString(minecraft.textRenderer, "\u00a7bSword: ", (int) ((width / 2) * 0 + (width / 4F)) - 20 , y, 0xFFFFFF);
			if (enchSwordZombie != null) for (InfoEnchantment ench : enchSwordZombie) {
				y += 11;
				drawCenteredString(minecraft.textRenderer, I18n.translate(ench.enchantment.getTranslationKey()) + " " + ench.level, (int) ((width / 2) * 0 + (width / 4F)) - 20, y, 0xFFFFFF);
			}
		}
		if (e instanceof SkeletonEntity) {
			drawCenteredString(minecraft.textRenderer, "Skeleton", (int) ((width / 2) * 1 + (width / 4F)) + 20, 20, 0xFFFFFF);
			y = 75;
			drawCenteredString(minecraft.textRenderer, "\u00a7bHelmet: ", (int) ((width / 2) * 1 + (width / 4F)) + 20, y, 0xFFFFFF);
			if (enchHelmetSkel != null) for (InfoEnchantment ench : enchHelmetSkel) {
				y += 11;
				drawCenteredString(minecraft.textRenderer, I18n.translate(ench.enchantment.getTranslationKey()) + " " + ench.level, (int) ((width / 2) * 1 + (width / 4F)) + 20, y, 0xFFFFFF);
			}
			y += 16;
			drawCenteredString(minecraft.textRenderer, "\u00a7bChestplate: ", (int) ((width / 2) * 1 + (width / 4F)) + 20, y, 0xFFFFFF);
			if (enchChestSkel != null) for (InfoEnchantment ench : enchChestSkel) {
				y += 11;
				drawCenteredString(minecraft.textRenderer, I18n.translate(ench.enchantment.getTranslationKey()) + " " + ench.level, (int) ((width / 2) * 1 + (width / 4F)) + 20, y, 0xFFFFFF);
			}
			y += 16;
			drawCenteredString(minecraft.textRenderer, "\u00a7bLeggings: ", (int) ((width / 2) * 1 + (width / 4F)) + 20, y, 0xFFFFFF);
			if (enchLeggingsSkel != null) for (InfoEnchantment ench : enchLeggingsSkel) {
				y += 11;
				drawCenteredString(minecraft.textRenderer, I18n.translate(ench.enchantment.getTranslationKey()) + " " + ench.level, (int) ((width / 2) * 1 + (width / 4F)) + 20, y, 0xFFFFFF);
			}
			y += 16;
			drawCenteredString(minecraft.textRenderer, "\u00a7bBoots: ", (int) ((width / 2) * 1 + (width / 4F)) + 20, y, 0xFFFFFF);
			if (enchBootsSkel != null) for (InfoEnchantment ench : enchBootsSkel) {
				y += 11;
				drawCenteredString(minecraft.textRenderer, I18n.translate(ench.enchantment.getTranslationKey()) + " " + ench.level, (int) ((width / 2) * 1 + (width / 4F)) + 20, y, 0xFFFFFF);
			}
			y += 16;
			drawCenteredString(minecraft.textRenderer, "\u00a7bBow: ", (int) ((width / 2) * 1 + (width / 4F)) + 20, y, 0xFFFFFF);
			if (enchBowSkel != null) for (InfoEnchantment ench : enchBowSkel) {
				y += 11;
				drawCenteredString(minecraft.textRenderer, I18n.translate(ench.enchantment.getTranslationKey()) + " " + ench.level, (int) ((width / 2) * 1 + (width / 4F)) + 20, y, 0xFFFFFF);
			}
		}
		xText.render(mouseX, mouseY, partialTicks);
		yText.render(mouseX, mouseY, partialTicks);
		zText.render(mouseX, mouseY, partialTicks);
	}
	
	@Override
	public boolean charTyped(char chr, int keyCode) {
		xText.charTyped(chr, keyCode);
		yText.charTyped(chr, keyCode);
		zText.charTyped(chr, keyCode);
		try {
			spawnX = Integer.parseInt(xText.getText());
			spawnY = Integer.parseInt(yText.getText());
			spawnZ = Integer.parseInt(zText.getText());
		} catch (Exception e) {
			RLogAPI.logError(e, "Parse Invalid Data #2");
		}
		return super.charTyped(chr, keyCode);
	}
	
	@Override
	public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
		xText.keyReleased(keyCode, scanCode, modifiers);
		yText.keyReleased(keyCode, scanCode, modifiers);
		zText.keyReleased(keyCode, scanCode, modifiers);
		try {
			spawnX = Integer.parseInt(xText.getText());
			spawnY = Integer.parseInt(yText.getText());
			spawnZ = Integer.parseInt(zText.getText());
		} catch (Exception e) {
			RLogAPI.logError(e, "Parse Invalid Data #2");
		}
		check();
		return super.keyReleased(keyCode, scanCode, modifiers);
	}
	
	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		xText.keyPressed(keyCode, scanCode, modifiers);
		yText.keyPressed(keyCode, scanCode, modifiers);
		zText.keyPressed(keyCode, scanCode, modifiers);
		try {
			spawnX = Integer.parseInt(xText.getText());
			spawnY = Integer.parseInt(yText.getText());
			spawnZ = Integer.parseInt(zText.getText());
		} catch (Exception e) {
			RLogAPI.logError(e, "Parse Invalid Data #2");
		}
		check();
		return super.keyPressed(keyCode, scanCode, modifiers);
	}
	
	@Override
	public void init() {
		entity = new EntitySliderWidget(width / 2 - (width / 8), 50, entities, width / 4, 20, e -> {
			
		});
		addButton(entity);
		addButton(new ButtonWidget(width / 2 - 35, 80, 30, 20, "X++", b -> spawnX++));
		addButton(new ButtonWidget(width / 2 + 5, 80, 30, 20, "X--", b -> spawnX--));
		addButton(new ButtonWidget(width / 2 - 35, 105, 30, 20, "Y++", b -> spawnY++));
		addButton(new ButtonWidget(width / 2 + 5, 105, 30, 20, "Y--", b -> spawnY--));
		addButton(new ButtonWidget(width / 2 - 35, 130, 30, 20, "Z++", b -> spawnZ++));
		addButton(new ButtonWidget(width / 2 + 5, 130, 30, 20, "Z--", b -> spawnZ--));
		
		addButton(new ButtonWidget((width / 4) * 0 - 20 + (width / 8), 50, (width / 4), 20, "Reroll Zombie Enchants", b -> {
			Random rng = new Random();
			enchHelmetZombie = EnchantmentHelper.getEnchantments(rng, new ItemStack(Items.DIAMOND_HELMET), (int) (5 + (rng.nextInt(5) + 13) * (minecraft.world.getDifficulty() == Difficulty.HARD ? .13f : 0)), false);
			enchSwordZombie = EnchantmentHelper.getEnchantments(new Random(System.currentTimeMillis()), new ItemStack(Items.IRON_SWORD), 5 + (rng.nextInt(5) + 13) * (minecraft.world.getDifficulty() == Difficulty.HARD ? 1 : 0), false);
			enchChestZombie = EnchantmentHelper.getEnchantments(rng, new ItemStack(Items.DIAMOND_CHESTPLATE), (int) (5 + (rng.nextInt(5) + 13) * (minecraft.world.getDifficulty() == Difficulty.HARD ? .13f : 0)), false);
			enchLeggingsZombie = EnchantmentHelper.getEnchantments(rng, new ItemStack(Items.DIAMOND_LEGGINGS), (int) (5 + (rng.nextInt(5) + 13) * (minecraft.world.getDifficulty() == Difficulty.HARD ? .13f : 0)), false);
			enchBootsZombie = EnchantmentHelper.getEnchantments(rng, new ItemStack(Items.DIAMOND_BOOTS), (int) (5 + (rng.nextInt(5) + 13) * (minecraft.world.getDifficulty() == Difficulty.HARD ? .13f : 0)), false);
		}));
		addButton(new ButtonWidget((width / 4) * 2 + 20 + (width / 8), 50, (width / 4), 20, "Reroll Skeleton Enchants", b -> {
			Random rng2 = new Random();
			enchHelmetSkel = EnchantmentHelper.getEnchantments(rng2, new ItemStack(Items.DIAMOND_HELMET), (int) (5 + (rng2.nextInt(5) + 13) * (minecraft.world.getDifficulty() == Difficulty.HARD ? .13f : 0)), false);
			enchBowSkel = EnchantmentHelper.getEnchantments(rng2, new ItemStack(Items.BOW), (int) (5 + (rng2.nextInt(5) + 13) * (minecraft.world.getDifficulty() == Difficulty.HARD ? .13f : 0)), false);
			enchChestSkel = EnchantmentHelper.getEnchantments(rng2, new ItemStack(Items.DIAMOND_CHESTPLATE), (int) (5 + (rng2.nextInt(5) + 13) * (minecraft.world.getDifficulty() == Difficulty.HARD ? .13f : 0)), false);
			enchLeggingsSkel = EnchantmentHelper.getEnchantments(rng2, new ItemStack(Items.DIAMOND_LEGGINGS), (int) (5 + (rng2.nextInt(5) + 13) * (minecraft.world.getDifficulty() == Difficulty.HARD ? .13f : 0)), false);
			enchBootsSkel = EnchantmentHelper.getEnchantments(rng2, new ItemStack(Items.DIAMOND_BOOTS), (int) (5 + (rng2.nextInt(5) + 13) * (minecraft.world.getDifficulty() == Difficulty.HARD ? .13f : 0)), false);
		}));
		
		xText = new TextFieldWidget(MinecraftClient.getInstance().textRenderer, width / 2 - 50, 155, 30, 15, "");
		xText.setText(spawnX + "");
		yText = new TextFieldWidget(MinecraftClient.getInstance().textRenderer, width / 2 - 15, 155, 30, 15, "");
		yText.setText(spawnY + "");
		zText = new TextFieldWidget(MinecraftClient.getInstance().textRenderer, width / 2 + 20, 155, 30, 15, "");
		zText.setText(spawnZ + "");
		
		leatherZombie = new SmallCheckboxWidget((width / 2) * 0 + 2, height - 12, "Leather Armor", leatherZombie.isChecked(), cc);
		chainZombie = new SmallCheckboxWidget((width / 2) * 0 + 2, height - 24, "Chain Armor", chainZombie.isChecked(), cc);
		ironZombie = new SmallCheckboxWidget((width / 2) * 0 + 2, height - 36, "Iron Armor", ironZombie.isChecked(), cc);
		goldZombie = new SmallCheckboxWidget((width / 2) * 0 + 2, height - 48, "Gold Armor", goldZombie.isChecked(), cc);
		diamondZombie = new SmallCheckboxWidget((width / 2) * 0 + 2, height - 60, "Diamond Armor", diamondZombie.isChecked(), cc);
		addButton(leatherZombie);
		addButton(chainZombie);
		addButton(ironZombie);
		addButton(goldZombie);
		addButton(diamondZombie);
		
		leatherSkel = new SmallCheckboxWidget(width - 26 - minecraft.textRenderer.getStringWidth("Leather Armor"), height - 12, "Leather Armor", leatherSkel.isChecked(), cc2);
		chainSkel = new SmallCheckboxWidget(width - 26 - minecraft.textRenderer.getStringWidth("Chain Armor"), height - 24, "Chain Armor", chainSkel.isChecked(), cc2);
		ironSkel = new SmallCheckboxWidget(width - 26 - minecraft.textRenderer.getStringWidth("Iron Armor"), height - 36, "Iron Armor", ironSkel.isChecked(), cc2);
		goldSkel = new SmallCheckboxWidget(width - 26 - minecraft.textRenderer.getStringWidth("Gold Armor"), height - 48, "Gold Armor", goldSkel.isChecked(), cc2);
		diamondSkel = new SmallCheckboxWidget(width - 26 - minecraft.textRenderer.getStringWidth("Diamond Armor"), height - 60, "Diamond Armor", diamondSkel.isChecked(), cc2);
		addButton(leatherSkel);
		addButton(chainSkel);
		addButton(ironSkel);
		addButton(goldSkel);
		addButton(diamondSkel);
		
		addButton(new ButtonWidget(width / 2 - 90, height - 24, 170, 20, "Spawn", b3 -> {
			Entity e = null;
			ServerWorld world = minecraft.getServer().getWorld(minecraft.player.dimension);
			switch (entity.getMessage().split(": ")[1].trim()) {
			case "Ghast":
				e = new GhastEntity(EntityType.GHAST, world);
				break;
			case "Blaze":
				e = new BlazeEntity(EntityType.BLAZE, world);
				break;
			case "Enderman":
				e = new EndermanEntity(EntityType.ENDERMAN, world);
				break;
			case "Zombie":
				e = new ZombieEntity(EntityType.ZOMBIE, world);
				break;
			case "Skeleton":
				e = new SkeletonEntity(EntityType.SKELETON, world);
				break;
			case "Witch":
				e = new WitchEntity(EntityType.WITCH, world);
				break;
			case "Witherskeleton":
				e = new WitherSkeletonEntity(EntityType.WITHER_SKELETON, world);
				break;
			case "Chicken":
				e = new ChickenEntity(EntityType.CHICKEN, world);
				break;
			case "Cave Spider":
				e = new CaveSpiderEntity(EntityType.CAVE_SPIDER, world);
				break;
			case "Cow":
				e = new CowEntity(EntityType.COW, world);
				break;
			case "Creeper":
				e = new CreeperEntity(EntityType.CREEPER, world);
				break;
			case "Husk":
				e = new HuskEntity(EntityType.HUSK, world);
				break;
			case "Iron Golem":
				e = new IronGolemEntity(EntityType.IRON_GOLEM, world);
				break;
			case "Magma Cube":
				e = new MagmaCubeEntity(EntityType.MAGMA_CUBE, world);
				break;
			case "Mooshroom":
				e = new MooshroomEntity(EntityType.MOOSHROOM, world);
				break;
			case "Ocelot":
				e = new OcelotEntity(EntityType.OCELOT, world);
				break;
			case "Pig":
				e = new PigEntity(EntityType.PIG, world);
				break;
			case "Rabbit":
				e = new RabbitEntity(EntityType.RABBIT, world);
				break;
			case "Sheep":
				e = new SheepEntity(EntityType.SHEEP, world);
				break;
			case "Slime":
				e = new SlimeEntity(EntityType.SLIME, world);
				break;
			case "Spider":
				e = new SpiderEntity(EntityType.SPIDER, world);
				break;
			case "Villager":
				e = new VillagerEntity(EntityType.VILLAGER, world);
				break;
			case "Wolf":
				e = new WolfEntity(EntityType.WOLF, world);
				break;
			case "Zombievillager":
				e = new ZombieVillagerEntity(EntityType.ZOMBIE_VILLAGER, world);
				break;
			default:
				return;
			}
			e.refreshPositionAndAngles(spawnX, spawnY, spawnZ, 0, 0);
			LivingEntity living = (LivingEntity) e;
			if (living instanceof MobEntity) {
				((MobEntity) living).initEquipment(MinecraftClient.getInstance().world.getLocalDifficulty(living.getBlockPos()));
				((MobEntity) living).setEquipmentDropChance(EquipmentSlot.MAINHAND, 1.0F);
				((MobEntity) living).setEquipmentDropChance(EquipmentSlot.OFFHAND, 1.0F);
				((MobEntity) living).setEquipmentDropChance(EquipmentSlot.CHEST, 1.0F);
				((MobEntity) living).setEquipmentDropChance(EquipmentSlot.FEET, 1.0F);
				((MobEntity) living).setEquipmentDropChance(EquipmentSlot.HEAD, 1.0F);
				((MobEntity) living).setEquipmentDropChance(EquipmentSlot.LEGS, 1.0F);
			}
			if (living instanceof ZombieEntity) {
				if (EntitySpawnerScreen.enchSwordZombie != null) living.equipStack(EquipmentSlot.MAINHAND, addEnchants(new ItemStack(Items.IRON_SWORD), EntitySpawnerScreen.enchSwordZombie));
				if (EntitySpawnerScreen.diamondZombie.isChecked() && EntitySpawnerScreen.enchHelmetZombie != null) { 
					living.equipStack(EquipmentSlot.FEET, addEnchants(new ItemStack(Items.DIAMOND_BOOTS), EntitySpawnerScreen.enchBootsZombie));
					living.equipStack(EquipmentSlot.CHEST, addEnchants(new ItemStack(Items.DIAMOND_CHESTPLATE), EntitySpawnerScreen.enchChestZombie));
					living.equipStack(EquipmentSlot.LEGS, addEnchants(new ItemStack(Items.DIAMOND_LEGGINGS), EntitySpawnerScreen.enchLeggingsZombie));
					living.equipStack(EquipmentSlot.HEAD, addEnchants(new ItemStack(Items.DIAMOND_HELMET), EntitySpawnerScreen.enchHelmetZombie));
				} else if (EntitySpawnerScreen.ironZombie.isChecked() && EntitySpawnerScreen.enchHelmetZombie != null) { 
					living.equipStack(EquipmentSlot.FEET, addEnchants(new ItemStack(Items.IRON_BOOTS), EntitySpawnerScreen.enchBootsZombie));
					living.equipStack(EquipmentSlot.CHEST, addEnchants(new ItemStack(Items.IRON_CHESTPLATE), EntitySpawnerScreen.enchChestZombie));
					living.equipStack(EquipmentSlot.LEGS, addEnchants(new ItemStack(Items.IRON_LEGGINGS), EntitySpawnerScreen.enchLeggingsZombie));
					living.equipStack(EquipmentSlot.HEAD, addEnchants(new ItemStack(Items.IRON_HELMET), EntitySpawnerScreen.enchHelmetZombie));
				} else if (EntitySpawnerScreen.chainZombie.isChecked() && EntitySpawnerScreen.enchHelmetZombie != null) { 
					living.equipStack(EquipmentSlot.FEET, addEnchants(new ItemStack(Items.CHAINMAIL_BOOTS), EntitySpawnerScreen.enchChestZombie));
					living.equipStack(EquipmentSlot.CHEST, addEnchants(new ItemStack(Items.CHAINMAIL_CHESTPLATE), EntitySpawnerScreen.enchChestZombie));
					living.equipStack(EquipmentSlot.LEGS, addEnchants(new ItemStack(Items.CHAINMAIL_LEGGINGS), EntitySpawnerScreen.enchLeggingsZombie));
					living.equipStack(EquipmentSlot.HEAD, addEnchants(new ItemStack(Items.CHAINMAIL_HELMET), EntitySpawnerScreen.enchHelmetZombie));
				} else if (EntitySpawnerScreen.goldZombie.isChecked() && EntitySpawnerScreen.enchHelmetZombie != null) { 
					living.equipStack(EquipmentSlot.FEET, addEnchants(new ItemStack(Items.GOLDEN_BOOTS), EntitySpawnerScreen.enchBootsZombie));
					living.equipStack(EquipmentSlot.CHEST, addEnchants(new ItemStack(Items.GOLDEN_CHESTPLATE), EntitySpawnerScreen.enchChestZombie));
					living.equipStack(EquipmentSlot.LEGS, addEnchants(new ItemStack(Items.GOLDEN_LEGGINGS), EntitySpawnerScreen.enchLeggingsZombie));
					living.equipStack(EquipmentSlot.HEAD, addEnchants(new ItemStack(Items.GOLDEN_HELMET), EntitySpawnerScreen.enchHelmetZombie));
				} else if (EntitySpawnerScreen.leatherZombie.isChecked()) { 
					living.equipStack(EquipmentSlot.FEET, addEnchants(new ItemStack(Items.LEATHER_BOOTS), EntitySpawnerScreen.enchBootsZombie));
					living.equipStack(EquipmentSlot.CHEST, addEnchants(new ItemStack(Items.LEATHER_CHESTPLATE), EntitySpawnerScreen.enchChestZombie));
					living.equipStack(EquipmentSlot.LEGS, addEnchants(new ItemStack(Items.LEATHER_LEGGINGS), EntitySpawnerScreen.enchLeggingsZombie));
					living.equipStack(EquipmentSlot.HEAD, addEnchants(new ItemStack(Items.LEATHER_HELMET), EntitySpawnerScreen.enchHelmetZombie));
				}
			} else if (living instanceof SkeletonEntity) {
				if (EntitySpawnerScreen.enchBowSkel != null) living.equipStack(EquipmentSlot.MAINHAND, addEnchants(new ItemStack(Items.BOW), EntitySpawnerScreen.enchBowSkel));
				if (EntitySpawnerScreen.diamondSkel.isChecked() && EntitySpawnerScreen.enchHelmetSkel != null) { 
					living.equipStack(EquipmentSlot.FEET, addEnchants(new ItemStack(Items.DIAMOND_BOOTS), EntitySpawnerScreen.enchBootsSkel));
					living.equipStack(EquipmentSlot.CHEST, addEnchants(new ItemStack(Items.DIAMOND_CHESTPLATE), EntitySpawnerScreen.enchChestSkel));
					living.equipStack(EquipmentSlot.LEGS, addEnchants(new ItemStack(Items.DIAMOND_LEGGINGS), EntitySpawnerScreen.enchLeggingsSkel));
					living.equipStack(EquipmentSlot.HEAD, addEnchants(new ItemStack(Items.DIAMOND_HELMET), EntitySpawnerScreen.enchHelmetSkel));
				} else if (EntitySpawnerScreen.ironSkel.isChecked() && EntitySpawnerScreen.enchHelmetSkel != null) { 
					living.equipStack(EquipmentSlot.FEET, addEnchants(new ItemStack(Items.IRON_BOOTS), EntitySpawnerScreen.enchBootsSkel));
					living.equipStack(EquipmentSlot.CHEST, addEnchants(new ItemStack(Items.IRON_CHESTPLATE), EntitySpawnerScreen.enchChestSkel));
					living.equipStack(EquipmentSlot.LEGS, addEnchants(new ItemStack(Items.IRON_LEGGINGS), EntitySpawnerScreen.enchLeggingsSkel));
					living.equipStack(EquipmentSlot.HEAD, addEnchants(new ItemStack(Items.IRON_HELMET), EntitySpawnerScreen.enchHelmetSkel));
				} else if (EntitySpawnerScreen.chainSkel.isChecked() && EntitySpawnerScreen.enchHelmetSkel != null) { 
					living.equipStack(EquipmentSlot.FEET, addEnchants(new ItemStack(Items.CHAINMAIL_BOOTS), EntitySpawnerScreen.enchChestSkel));
					living.equipStack(EquipmentSlot.CHEST, addEnchants(new ItemStack(Items.CHAINMAIL_CHESTPLATE), EntitySpawnerScreen.enchChestSkel));
					living.equipStack(EquipmentSlot.LEGS, addEnchants(new ItemStack(Items.CHAINMAIL_LEGGINGS), EntitySpawnerScreen.enchLeggingsSkel));
					living.equipStack(EquipmentSlot.HEAD, addEnchants(new ItemStack(Items.CHAINMAIL_HELMET), EntitySpawnerScreen.enchHelmetSkel));
				} else if (EntitySpawnerScreen.goldSkel.isChecked() && EntitySpawnerScreen.enchHelmetSkel != null) { 
					living.equipStack(EquipmentSlot.FEET, addEnchants(new ItemStack(Items.GOLDEN_BOOTS), EntitySpawnerScreen.enchBootsSkel));
					living.equipStack(EquipmentSlot.CHEST, addEnchants(new ItemStack(Items.GOLDEN_CHESTPLATE), EntitySpawnerScreen.enchChestSkel));
					living.equipStack(EquipmentSlot.LEGS, addEnchants(new ItemStack(Items.GOLDEN_LEGGINGS), EntitySpawnerScreen.enchLeggingsSkel));
					living.equipStack(EquipmentSlot.HEAD, addEnchants(new ItemStack(Items.GOLDEN_HELMET), EntitySpawnerScreen.enchHelmetSkel));
				} else if (EntitySpawnerScreen.leatherSkel.isChecked()) { 
					living.equipStack(EquipmentSlot.FEET, addEnchants(new ItemStack(Items.LEATHER_BOOTS), EntitySpawnerScreen.enchBootsSkel));
					living.equipStack(EquipmentSlot.CHEST, addEnchants(new ItemStack(Items.LEATHER_CHESTPLATE), EntitySpawnerScreen.enchChestSkel));
					living.equipStack(EquipmentSlot.LEGS, addEnchants(new ItemStack(Items.LEATHER_LEGGINGS), EntitySpawnerScreen.enchLeggingsSkel));
					living.equipStack(EquipmentSlot.HEAD, addEnchants(new ItemStack(Items.LEATHER_HELMET), EntitySpawnerScreen.enchHelmetSkel));
				}
			}
			world.spawnEntity(e);
			RLogAPI.logDebug("[EntitySpawner] Entity spawned");
		}));
		addButton(new ButtonWidget(width / 2 - 90, height - 48, 170, 20, "Done", b -> {
			MinecraftClient.getInstance().openScreen(new GameMenuScreen(true));
		}));
		check();
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
		xText.mouseClicked(mouseX, mouseY, mouseButton);
		yText.mouseClicked(mouseX, mouseY, mouseButton);
		zText.mouseClicked(mouseX, mouseY, mouseButton);
		xText.setText(spawnX + "");
		yText.setText(spawnY + "");
		zText.setText(spawnZ + "" );
		check();
		return super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	public void check() {
		RLogAPI.logDebug("[EntitySpawner] Checking Entity Spawn Location");
		e = null;
		ServerWorld world = minecraft.getServer().getWorld(minecraft.player.dimension);
		switch (entity.getMessage().split(": ")[1].trim()) {
		case "Ghast":
			e = new GhastEntity(EntityType.GHAST, world);
			break;
		case "Blaze":
			e = new BlazeEntity(EntityType.BLAZE, world);
			break;
		case "Enderman":
			e = new EndermanEntity(EntityType.ENDERMAN, world);
			break;
		case "Zombie":
			e = new ZombieEntity(EntityType.ZOMBIE, world);
			break;
		case "Skeleton":
			e = new SkeletonEntity(EntityType.SKELETON, world);
			break;
		case "Witch":
			e = new WitchEntity(EntityType.WITCH, world);
			break;
		case "Witherskeleton":
			e = new WitherSkeletonEntity(EntityType.WITHER_SKELETON, world);
			break;
		case "Chicken":
			e = new ChickenEntity(EntityType.CHICKEN, world);
			break;
		case "Cave Spider":
			e = new CaveSpiderEntity(EntityType.CAVE_SPIDER, world);
			break;
		case "Cow":
			e = new CowEntity(EntityType.COW, world);
			break;
		case "Creeper":
			e = new CreeperEntity(EntityType.CREEPER, world);
			break;
		case "Husk":
			e = new HuskEntity(EntityType.HUSK, world);
			break;
		case "Iron Golem":
			e = new IronGolemEntity(EntityType.IRON_GOLEM, world);
			break;
		case "Magma Cube":
			e = new MagmaCubeEntity(EntityType.MAGMA_CUBE, world);
			break;
		case "Mooshroom":
			e = new MooshroomEntity(EntityType.MOOSHROOM, world);
			break;
		case "Ocelot":
			e = new OcelotEntity(EntityType.OCELOT, world);
			break;
		case "Pig":
			e = new PigEntity(EntityType.PIG, world);
			break;
		case "Rabbit":
			e = new RabbitEntity(EntityType.RABBIT, world);
			break;
		case "Sheep":
			e = new SheepEntity(EntityType.SHEEP, world);
			break;
		case "Slime":
			e = new SlimeEntity(EntityType.SLIME, world);
			break;
		case "Spider":
			e = new SpiderEntity(EntityType.SPIDER, world);
			break;
		case "Villager":
			e = new VillagerEntity(EntityType.VILLAGER, world);
			break;
		case "Wolf":
			e = new WolfEntity(EntityType.WOLF, world);
			break;
		case "Zombievillager":
			e = new ZombieVillagerEntity(EntityType.ZOMBIE_VILLAGER, world);
			break;
		default:
			return;
		}
		e.updatePosition(spawnX, spawnY, spawnZ);
		
		
		if (e instanceof ZombieEntity) {
			buttons.get(7).visible = true;
			((SmallCheckboxWidget) buttons.get(9)).visible = true;
			((SmallCheckboxWidget) buttons.get(10)).visible = true;
			((SmallCheckboxWidget) buttons.get(11)).visible = true;
			((SmallCheckboxWidget) buttons.get(12)).visible = true;
			((SmallCheckboxWidget) buttons.get(13)).visible = true;
			
		} else {
			buttons.get(7).visible = false;
			((SmallCheckboxWidget) buttons.get(9)).visible = false;
			((SmallCheckboxWidget) buttons.get(10)).visible = false;
			((SmallCheckboxWidget) buttons.get(11)).visible = false;
			((SmallCheckboxWidget) buttons.get(12)).visible = false;
			((SmallCheckboxWidget) buttons.get(13)).visible = false;
		}
		
		if (e instanceof SkeletonEntity) {
			buttons.get(8).visible = true;
			((SmallCheckboxWidget) buttons.get(14)).visible = true;
			((SmallCheckboxWidget) buttons.get(15)).visible = true;
			((SmallCheckboxWidget) buttons.get(16)).visible = true;
			((SmallCheckboxWidget) buttons.get(17)).visible = true;
			((SmallCheckboxWidget) buttons.get(18)).visible = true;
		} else {
			buttons.get(8).visible = false;
			((SmallCheckboxWidget) buttons.get(14)).visible = false;
			((SmallCheckboxWidget) buttons.get(15)).visible = false;
			((SmallCheckboxWidget) buttons.get(16)).visible = false;
			((SmallCheckboxWidget) buttons.get(17)).visible = false;
			((SmallCheckboxWidget) buttons.get(18)).visible = false;
		}
		
		this.buttons.get(this.buttons.size() - 2).active = ((MobEntity) e).canSpawn(world, SpawnType.NATURAL);
	}


	public ItemStack addEnchants(ItemStack item, List<InfoEnchantment> enchants) {
		for (InfoEnchantment enchantment : enchants) {
			item.addEnchantment(enchantment.enchantment, enchantment.level);
		}
		return item;
	}
	
}
