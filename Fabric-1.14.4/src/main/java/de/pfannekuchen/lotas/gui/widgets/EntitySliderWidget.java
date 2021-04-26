package de.pfannekuchen.lotas.gui.widgets;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import com.mojang.blaze3d.platform.GlStateManager;

import de.pfannekuchen.lotas.gui.GuiEntitySpawner;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget.PressAction;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.LivingEntity;
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
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.WorldServer;
import rlog.RLogAPI;

public class EntitySliderWidget extends AbstractButtonWidget {

	public HashMap<Integer, String> entities;

	private float sliderPosition = 1.0F;
	public boolean isMouseDown;
	private final String name;
	private final float min;
	private final float max;

	public EntitySliderWidget(int xPos, int yPos, HashMap<Integer, String> ent, int width, int height, PressAction c) {
		super(xPos, yPos, width, height, "");
		this.name = "Entity";
		this.entities = ent;
		this.min = 0;
		this.max = ent.size() - 1;
		this.sliderPosition = -min / (max - min);
		this.setMessage("Entity: " + ent.get(0));
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
		this.setMessage(this.getDisplayString());
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

	@Override
	protected void renderBg(MinecraftClient client, int mouseX, int mouseY) {
		client.getTextureManager().bindTexture(WIDGETS_LOCATION);
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		int i = (this.isHovered() ? 2 : 1) * 20;
		this.blit(this.x + (int) (this.sliderPosition * (double) (this.width - 8)), this.y, 0, 46 + i, 4, 20);
		this.blit(this.x + (int) (this.sliderPosition * (double) (this.width - 8)) + 4, this.y, 196, 46 + i, 4, 20);
	}

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

				setMessage("Entity: " + entities.get((int) Math.round(sliderPosition * (max - min) + min)));
			}

			GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
			blit(this.x + (int) (this.sliderPosition * (float) (this.width - 8)), this.y, 0, 66, 4, 20);
			blit(this.x + (int) (this.sliderPosition * (float) (this.width - 8)) + 4, this.y, 196, 66, 4, 20);
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
		this.setMessage(this.getDisplayString());
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

			this.setMessage("Entity: " + entities.get((int) Math.round(sliderPosition * (max - min) + min)));
			this.isMouseDown = true;
			return true;
		} else {
			return false;
		}
	}
	
	public LivingEntity getEntity(WorldServer world) {
		LivingEntity entity = null;
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
			entity = new EntityHusk(world);
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
			entity = new EntityWitherSkeleton(world);
			break;
		case 13:
			entity = new EntityZombie(world);
			break;
		case 14:
			entity = new EntityZombieVillager(world);
			break;
		}
		if (world.getDifficulty() == EnumDifficulty.HARD) {
			switch ((int) getSliderValue()) {
			case 15:
				entity = new EntitySkeleton(world);
				entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, addEnchants(new ItemStack(Items.BOW), GuiEntitySpawner.skelBow));
				break;
			case 17:
				entity = new EntitySkeleton(world);
				entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, addEnchants(new ItemStack(Items.BOW), GuiEntitySpawner.skelBow));
				entity.setItemStackToSlot(EntityEquipmentSlot.FEET, new ItemStack(Items.LEATHER_BOOTS));
				entity.setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(Items.LEATHER_CHESTPLATE));
				entity.setItemStackToSlot(EntityEquipmentSlot.LEGS, new ItemStack(Items.LEATHER_LEGGINGS));
				entity.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.LEATHER_HELMET));
				break;
			case 19:
				entity = new EntitySkeleton(world);
				entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, addEnchants(new ItemStack(Items.BOW), GuiEntitySpawner.skelBow));
				entity.setItemStackToSlot(EntityEquipmentSlot.FEET, new ItemStack(Items.GOLDEN_BOOTS));
				entity.setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(Items.GOLDEN_CHESTPLATE));
				entity.setItemStackToSlot(EntityEquipmentSlot.LEGS, new ItemStack(Items.GOLDEN_LEGGINGS));
				entity.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.GOLDEN_HELMET));
				break;
			case 21:
				entity = new EntitySkeleton(world);
				entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, addEnchants(new ItemStack(Items.BOW), GuiEntitySpawner.skelBow));
				entity.setItemStackToSlot(EntityEquipmentSlot.FEET, new ItemStack(Items.CHAINMAIL_BOOTS));
				entity.setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(Items.CHAINMAIL_CHESTPLATE));
				entity.setItemStackToSlot(EntityEquipmentSlot.LEGS, new ItemStack(Items.CHAINMAIL_LEGGINGS));
				entity.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.CHAINMAIL_HELMET));
				break;
			case 23:
				entity = new EntitySkeleton(world);
				entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, addEnchants(new ItemStack(Items.BOW), GuiEntitySpawner.skelBow));
				entity.setItemStackToSlot(EntityEquipmentSlot.FEET, new ItemStack(Items.IRON_BOOTS));
				entity.setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
				entity.setItemStackToSlot(EntityEquipmentSlot.LEGS, new ItemStack(Items.IRON_LEGGINGS));
				entity.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
				break;
			case 25:
				entity = new EntitySkeleton(world);
				entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, addEnchants(new ItemStack(Items.BOW), GuiEntitySpawner.skelBow));
				entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
				entity.setItemStackToSlot(EntityEquipmentSlot.FEET, new ItemStack(Items.DIAMOND_BOOTS));
				entity.setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE));
				entity.setItemStackToSlot(EntityEquipmentSlot.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS));
				entity.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.DIAMOND_HELMET));
				break;
			case 16:
				entity = new EntityZombie(world);
				entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, addEnchants(new ItemStack(Items.IRON_SWORD), GuiEntitySpawner.zombieSword));
				break;
			case 18:
				entity = new EntityZombie(world);
				entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, addEnchants(new ItemStack(Items.IRON_SWORD), GuiEntitySpawner.zombieSword));
				entity.setItemStackToSlot(EntityEquipmentSlot.FEET, new ItemStack(Items.LEATHER_BOOTS));
				entity.setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(Items.LEATHER_CHESTPLATE));
				entity.setItemStackToSlot(EntityEquipmentSlot.LEGS, new ItemStack(Items.LEATHER_LEGGINGS));
				entity.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.LEATHER_HELMET));
				break;
			case 20:
				entity = new EntityZombie(world);
				entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, addEnchants(new ItemStack(Items.IRON_SWORD), GuiEntitySpawner.zombieSword));
				entity.setItemStackToSlot(EntityEquipmentSlot.FEET, new ItemStack(Items.GOLDEN_BOOTS));
				entity.setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(Items.GOLDEN_CHESTPLATE));
				entity.setItemStackToSlot(EntityEquipmentSlot.LEGS, new ItemStack(Items.GOLDEN_LEGGINGS));
				entity.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.GOLDEN_HELMET));
				break;
			case 22:
				entity = new EntityZombie(world);
				entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, addEnchants(new ItemStack(Items.IRON_SWORD), GuiEntitySpawner.zombieSword));
				entity.setItemStackToSlot(EntityEquipmentSlot.FEET, new ItemStack(Items.CHAINMAIL_BOOTS));
				entity.setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(Items.CHAINMAIL_CHESTPLATE));
				entity.setItemStackToSlot(EntityEquipmentSlot.LEGS, new ItemStack(Items.CHAINMAIL_LEGGINGS));
				entity.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.CHAINMAIL_HELMET));
				break;
			case 24:
				entity = new EntityZombie(world);
				entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, addEnchants(new ItemStack(Items.IRON_SWORD), GuiEntitySpawner.zombieSword));
				entity.setItemStackToSlot(EntityEquipmentSlot.FEET, new ItemStack(Items.IRON_BOOTS));
				entity.setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
				entity.setItemStackToSlot(EntityEquipmentSlot.LEGS, new ItemStack(Items.IRON_LEGGINGS));
				entity.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
				break;
			case 26:
				entity = new EntityZombie(world);
				entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, addEnchants(new ItemStack(Items.IRON_SWORD), GuiEntitySpawner.zombieSword));
				entity.setItemStackToSlot(EntityEquipmentSlot.FEET, new ItemStack(Items.DIAMOND_BOOTS));
				entity.setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE));
				entity.setItemStackToSlot(EntityEquipmentSlot.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS));
				entity.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.DIAMOND_HELMET));
				break;
			}
		}
		entity.inventoryArmorDropChances = new float[] { 1.0f, 1.0f, 1.0f, 1.0f };
		entity.inventoryHandsDropChances = new float[] { 1.0f, 1.0f };
		return entity;
	}
	
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