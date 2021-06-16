package de.pfannekuchen.lotas.gui.widgets;
import java.util.HashMap;

import com.mojang.blaze3d.platform.GlStateManager;

import de.pfannekuchen.lotas.gui.SpawnManipulationScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget.PressAction;
//#if MC>=11601
//$$ import net.minecraft.client.util.math.MatrixStack;
//#endif
import net.minecraft.enchantment.Enchantment;
//#if MC<=11502
import net.minecraft.enchantment.InfoEnchantment;
//#endif
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.BlazeEntity;
import net.minecraft.entity.mob.CaveSpiderEntity;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.mob.GhastEntity;
import net.minecraft.entity.mob.HuskEntity;
import net.minecraft.entity.mob.MagmaCubeEntity;
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
import net.minecraft.text.LiteralText;
import net.minecraft.util.Hand;
import net.minecraft.world.Difficulty;

public class EntitySliderWidget extends AbstractButtonWidget {

	public HashMap<Integer, String> entities;

	private float sliderPosition = 1.0F;
	public boolean isMouseDown;
	private final String name;
	private final float min;
	private final float max;

	public EntitySliderWidget(int xPos, int yPos, HashMap<Integer, String> ent, int width, int height, PressAction c) {
	    //#if MC>=11601
//$$ 		super(xPos, yPos, width, height, new LiteralText(""));
		//#else
		super(xPos, yPos, width, height, "");
		//#endif
		this.name = "Entity";
		this.entities = ent;
		this.min = 0;
		this.max = ent.size() - 1;
		this.sliderPosition = -min / (max - min);
		//#if MC>=11601
//$$ 		this.setMessage(new LiteralText("Entity: " + ent.get(0)));
		//#else
		this.setMessage("Entity: " + ent.get(0));
		//#endif
	}

	/**
	 * Gets the value of the slider.
	 * 
	 * @return A value that will under normal circumstances be between the slider's
	 *         {@link #min} and {@link #max} values, unless it was manually set out
	 *         of that range.
	 */
	public float getSliderValue() {
		return this.min + (this.max - this.min) * this.sliderPosition;
	}

	/**
	 * Sets the slider's value, optionally notifying the associated
	 * {@linkplain GuiPageButtonList.GuiResponder responder} of the change.
	 */
	public void setSliderValue(float value) {
		this.sliderPosition = (value - this.min) / (this.max - this.min);
		//#if MC>=11601
//$$ 	    this.setMessage(new LiteralText(this.getDisplayString()));
	    //#else
		this.setMessage(this.getDisplayString());
        //#endif
	}

	/**
	 * Gets the slider's position.
	 * 
	 * @return The position of the slider, which will under normal circumstances be
	 *         between 0 and 1, unless it was manually set out of that range.
	 */
	public float getSliderPosition() {
		return this.sliderPosition;
	}

	private String getDisplayString() {
		return this.name + ": " + this.getSliderValue();
	}

	//#if MC>=11601
//$$ 	@Override
//$$ 	public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
//$$ 	         MinecraftClient.getInstance().getTextureManager().bindTexture(WIDGETS_LOCATION);
//$$         GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
//$$         int i = (this.isHovered() ? 2 : 1) * 20;
//$$         this.drawTexture(matrices, this.x + (int) (this.sliderPosition * (double) (this.width - 8)), this.y, 0, 46 + i, 4, 20);
//$$         this.drawTexture(matrices, this.x + (int) (this.sliderPosition * (double) (this.width - 8)), this.y, 0, 46 + i, 4, 20);
//$$ 	    super.renderButton(matrices, mouseX, mouseY, delta);
//$$ 	}
	//#else
	@Override
	protected void renderBg(MinecraftClient client, int mouseX, int mouseY) {
	    client.getTextureManager().bindTexture(WIDGETS_LOCATION);
	    GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
	    int i = (this.isHovered() ? 2 : 1) * 20;
	    this.blit(this.x + (int) (this.sliderPosition * (double) (this.width - 8)), this.y, 0, 46 + i, 4, 20);
	    this.blit(this.x + (int) (this.sliderPosition * (double) (this.width - 8)) + 4, this.y, 196, 46 + i, 4, 20);
	}
	//#endif

	/**
	 * Fired when the mouse button is dragged. Equivalent of
	 * MouseListener.mouseDragged(MouseEvent e).
	 */
	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
		if (this.visible) {
			if (this.isMouseDown) {
				this.sliderPosition = (float) (mouseX - (this.x + 4)) / (float) (this.width - 8);

				if (this.sliderPosition < 0.0F) {
					this.sliderPosition = 0.0F;
				}

				if (this.sliderPosition > 1.0F) {
					this.sliderPosition = 1.0F;
				}
			    //#if MC>=11601
//$$ 				setMessage(new LiteralText("Entity: " + entities.get((int) Math.round(sliderPosition * (max - min) + min))));
				//#else
				setMessage("Entity: " + entities.get((int) Math.round(sliderPosition * (max - min) + min)));
				//#endif
			}

			GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
			//#if MC>=11601
//$$ 			this.drawTexture(null, this.x + (int) (this.sliderPosition * (float) (this.width - 8)), this.y, 0, 66, 4, 20);
//$$ 			this.drawTexture(null, this.x + (int) (this.sliderPosition * (float) (this.width - 8)) + 4, this.y, 196, 66, 4, 20);
			//#else
			blit(this.x + (int) (this.sliderPosition * (float) (this.width - 8)), this.y, 0, 66, 4, 20);
			blit(this.x + (int) (this.sliderPosition * (float) (this.width - 8)) + 4, this.y, 196, 66, 4, 20);
			//#endif
		}
		return true;
	}

	@Override
	protected int getYImage(boolean isHovered) {
		return 0;
	}

	/**
	 * Sets the position of the slider and notifies the associated
	 * {@linkplain GuiPageButtonList.GuiResponder responder} of the change
	 */
	public void setSliderPosition(float position) {
		this.sliderPosition = position;
		//#if MC>=11601
//$$ 		this.setMessage(new LiteralText(this.getDisplayString()));
	    //#else
	    this.setMessage(this.getDisplayString());
		//#endif
	}

	/**
	 * Returns true if the mouse has been pressed on this control. Equivalent of
	 * MouseListener.mousePressed(MouseEvent e).
	 */
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int delta) {
		if (super.mouseClicked(mouseX, mouseY, delta)) {
			this.sliderPosition = (float) (mouseX - (this.x + 4)) / (float) (this.width - 8);

			if (this.sliderPosition < 0.0F) {
				this.sliderPosition = 0.0F;
			}

			if (this.sliderPosition > 1.0F) {
				this.sliderPosition = 1.0F;
			}
			//#if MC>=11601
//$$ 			this.setMessage(new LiteralText("Entity: " + entities.get((int) Math.round(sliderPosition * (max - min) + min))));
	        //#else
			this.setMessage("Entity: " + entities.get((int) Math.round(sliderPosition * (max - min) + min)));
			//#endif
			this.isMouseDown = true;
			return true;
		} else {
			return false;
		}
	}
	
	public LivingEntity getEntity(ServerWorld world) {
		LivingEntity entity = new BlazeEntity(EntityType.BLAZE, world);
		switch ((int) Math.round(sliderPosition * (max - min) + min)) {
		case 0:
			entity = new BlazeEntity(EntityType.BLAZE, world);
			break;
		case 1:
			entity = new CaveSpiderEntity(EntityType.CAVE_SPIDER, world);
			break;
		case 2:
			entity = new CreeperEntity(EntityType.CREEPER, world);
			break;
		case 3:
			entity = new EndermanEntity(EntityType.ENDERMAN, world);
			break;
		case 4:
			entity = new GhastEntity(EntityType.GHAST, world);
			break;
		case 5:
			entity = new HuskEntity(EntityType.HUSK, world);
			break;
		case 6:
			entity = new IronGolemEntity(EntityType.IRON_GOLEM, world);
			break;
		case 7:
			entity = new MagmaCubeEntity(EntityType.MAGMA_CUBE, world);
			break;
		case 8:
			entity = new SkeletonEntity(EntityType.SKELETON, world);
			break;
		case 9:
			entity = new SlimeEntity(EntityType.SLIME, world);
			break;
		case 10:
			entity = new SpiderEntity(EntityType.SPIDER, world);
			break;
		case 11:
			entity = new WitchEntity(EntityType.WITCH, world);
			break;
		case 12:
			entity = new WitherSkeletonEntity(EntityType.WITHER_SKELETON, world);
			break;
		case 13:
			entity = new ZombieEntity(world);
			break;
		case 14:
			entity = new ZombieVillagerEntity(EntityType.ZOMBIE_VILLAGER, world);
			break;
		}
		if (world.getDifficulty() == Difficulty.HARD) {
			switch ((int) Math.round(sliderPosition * (max - min) + min)) {
			case 15:
				entity = new SkeletonEntity(EntityType.SKELETON, world);
				entity.setStackInHand(Hand.MAIN_HAND, addEnchants(new ItemStack(Items.BOW), SpawnManipulationScreen.skelBow));
				break;
			case 17:
				entity = new SkeletonEntity(EntityType.SKELETON, world);
				entity.setStackInHand(Hand.MAIN_HAND, addEnchants(new ItemStack(Items.BOW), SpawnManipulationScreen.skelBow));
				entity.equipStack(EquipmentSlot.FEET, new ItemStack(Items.LEATHER_BOOTS));
				entity.equipStack(EquipmentSlot.CHEST, new ItemStack(Items.LEATHER_CHESTPLATE));
				entity.equipStack(EquipmentSlot.LEGS, new ItemStack(Items.LEATHER_LEGGINGS));
				entity.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.LEATHER_HELMET));
				break;
			case 19:
				entity = new SkeletonEntity(EntityType.SKELETON, world);
				entity.setStackInHand(Hand.MAIN_HAND, addEnchants(new ItemStack(Items.BOW), SpawnManipulationScreen.skelBow));
				entity.equipStack(EquipmentSlot.FEET, new ItemStack(Items.GOLDEN_BOOTS));
				entity.equipStack(EquipmentSlot.CHEST, new ItemStack(Items.GOLDEN_CHESTPLATE));
				entity.equipStack(EquipmentSlot.LEGS, new ItemStack(Items.GOLDEN_LEGGINGS));
				entity.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.GOLDEN_HELMET));
				break;
			case 21:
				entity = new SkeletonEntity(EntityType.SKELETON, world);
				entity.setStackInHand(Hand.MAIN_HAND, addEnchants(new ItemStack(Items.BOW), SpawnManipulationScreen.skelBow));
				entity.equipStack(EquipmentSlot.FEET, new ItemStack(Items.CHAINMAIL_BOOTS));
				entity.equipStack(EquipmentSlot.CHEST, new ItemStack(Items.CHAINMAIL_CHESTPLATE));
				entity.equipStack(EquipmentSlot.LEGS, new ItemStack(Items.CHAINMAIL_LEGGINGS));
				entity.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.CHAINMAIL_HELMET));
				break;
			case 23:
				entity = new SkeletonEntity(EntityType.SKELETON, world);
				entity.setStackInHand(Hand.MAIN_HAND, addEnchants(new ItemStack(Items.BOW), SpawnManipulationScreen.skelBow));
				entity.equipStack(EquipmentSlot.FEET, new ItemStack(Items.IRON_BOOTS));
				entity.equipStack(EquipmentSlot.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
				entity.equipStack(EquipmentSlot.LEGS, new ItemStack(Items.IRON_LEGGINGS));
				entity.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
				break;
			case 25:
				entity = new SkeletonEntity(EntityType.SKELETON, world);
				entity.setStackInHand(Hand.MAIN_HAND, addEnchants(new ItemStack(Items.BOW), SpawnManipulationScreen.skelBow));
				entity.setStackInHand(Hand.MAIN_HAND, new ItemStack(Items.BOW));
				entity.equipStack(EquipmentSlot.FEET, new ItemStack(Items.DIAMOND_BOOTS));
				entity.equipStack(EquipmentSlot.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE));
				entity.equipStack(EquipmentSlot.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS));
				entity.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.DIAMOND_HELMET));
				break;
			case 16:
				entity = new ZombieEntity(world);
				entity.setStackInHand(Hand.MAIN_HAND, addEnchants(new ItemStack(Items.IRON_SWORD), SpawnManipulationScreen.zombieSword));
				break;
			case 18:
				entity = new ZombieEntity(world);
				entity.setStackInHand(Hand.MAIN_HAND, addEnchants(new ItemStack(Items.IRON_SWORD), SpawnManipulationScreen.zombieSword));
				entity.equipStack(EquipmentSlot.FEET, new ItemStack(Items.LEATHER_BOOTS));
				entity.equipStack(EquipmentSlot.CHEST, new ItemStack(Items.LEATHER_CHESTPLATE));
				entity.equipStack(EquipmentSlot.LEGS, new ItemStack(Items.LEATHER_LEGGINGS));
				entity.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.LEATHER_HELMET));
				break;
			case 20:
				entity = new ZombieEntity(world);
				entity.setStackInHand(Hand.MAIN_HAND, addEnchants(new ItemStack(Items.IRON_SWORD), SpawnManipulationScreen.zombieSword));
				entity.equipStack(EquipmentSlot.FEET, new ItemStack(Items.GOLDEN_BOOTS));
				entity.equipStack(EquipmentSlot.CHEST, new ItemStack(Items.GOLDEN_CHESTPLATE));
				entity.equipStack(EquipmentSlot.LEGS, new ItemStack(Items.GOLDEN_LEGGINGS));
				entity.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.GOLDEN_HELMET));
				break;
			case 22:
				entity = new ZombieEntity(world);
				entity.setStackInHand(Hand.MAIN_HAND, addEnchants(new ItemStack(Items.IRON_SWORD), SpawnManipulationScreen.zombieSword));
				entity.equipStack(EquipmentSlot.FEET, new ItemStack(Items.CHAINMAIL_BOOTS));
				entity.equipStack(EquipmentSlot.CHEST, new ItemStack(Items.CHAINMAIL_CHESTPLATE));
				entity.equipStack(EquipmentSlot.LEGS, new ItemStack(Items.CHAINMAIL_LEGGINGS));
				entity.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.CHAINMAIL_HELMET));
				break;
			case 24:
				entity = new ZombieEntity(world);
				entity.setStackInHand(Hand.MAIN_HAND, addEnchants(new ItemStack(Items.IRON_SWORD), SpawnManipulationScreen.zombieSword));
				entity.equipStack(EquipmentSlot.FEET, new ItemStack(Items.IRON_BOOTS));
				entity.equipStack(EquipmentSlot.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
				entity.equipStack(EquipmentSlot.LEGS, new ItemStack(Items.IRON_LEGGINGS));
				entity.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
				break;
			case 26:
				entity = new ZombieEntity(world);
				entity.setStackInHand(Hand.MAIN_HAND, addEnchants(new ItemStack(Items.IRON_SWORD), SpawnManipulationScreen.zombieSword));
				entity.equipStack(EquipmentSlot.FEET, new ItemStack(Items.DIAMOND_BOOTS));
				entity.equipStack(EquipmentSlot.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE));
				entity.equipStack(EquipmentSlot.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS));
				entity.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.DIAMOND_HELMET));
				break;
			}
		}
//		entity.inventoryArmorDropChances = new float[] { 1.0f, 1.0f, 1.0f, 1.0f };
//		entity.inventoryHandsDropChances = new float[] { 1.0f, 1.0f };
		return entity;
	}
	
	//#if MC>=11601
//$$ 	public ItemStack addEnchants(ItemStack item,  HashMap<Enchantment, Integer> enchs) {
//$$ 	    enchs.entrySet().forEach(entry->{
//$$ 	        item.addEnchantment(entry.getKey(), entry.getValue());
//$$ 	    });;
//$$ 		return item;
//$$ 	}
	//#else
	public ItemStack addEnchants(ItemStack item,  InfoEnchantment[] enchants) {
        for (InfoEnchantment enchantmentData : enchants) {
            item.addEnchantment(enchantmentData.enchantment, enchantmentData.level);
        }
        return item;
    }
	//#endif
	
	/**
	 * Fired when the mouse button is released. Equivalent of
	 * MouseListener.mouseReleased(MouseEvent e).
	 */
	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		this.isMouseDown = false;
		return super.mouseReleased(mouseX, mouseY, button);
	}

}